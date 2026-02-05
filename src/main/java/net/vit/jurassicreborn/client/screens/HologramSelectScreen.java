package net.vit.jurassicreborn.client.screens;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimation;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;
import net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurHandler;
import net.vit.jurassicreborn.common.blocks.entities.HologramBlockEntity;
import net.vit.jurassicreborn.common.network.Network;
import net.vit.jurassicreborn.common.network.SetHologramDinosaurPacket;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class HologramSelectScreen extends Screen {
    private final BlockPos blockPos;
    private List<Integer> dinoIds;
    private int currentIndex;
    private int poseIndex;
    private boolean rotating;
    private int rotation;
    private Button poseButton;
    private Button rotateButton;
    private Button angleButton;

    public HologramSelectScreen(BlockPos pos, int currentId, int pose, boolean rotating, int rotation) {
        super(Component.literal("Select Dinosaur"));
        this.blockPos = pos;
        this.currentIndex = currentId;
        this.poseIndex = pose;
        this.rotating = rotating;
        this.rotation = Math.floorMod(rotation, 360);
        if (!rotating) {
            this.rotation = HologramBlockEntity.snapRotation(this.rotation);
        }
    }

    @Override
    protected void init() {
        int total = DinosaurHandler.count();
        this.dinoIds = IntStream.range(0, total).boxed().collect(Collectors.toList());
        if (this.dinoIds.isEmpty()) {
            this.dinoIds.add(0);
        }
        this.currentIndex = Math.floorMod(this.currentIndex, this.dinoIds.size());

        int cx = this.width / 2;
        int cy = this.height / 2;

        // Previous dinosaur button
        this.addRenderableWidget(new Button(cx - 60, cy, 20, 20, Component.literal("<"), btn -> {
            currentIndex = (currentIndex - 1 + dinoIds.size()) % dinoIds.size();
            applySelectionLocally();
        }));

        // Next dinosaur button
        this.addRenderableWidget(new Button(cx + 40, cy, 20, 20, Component.literal(">"), btn -> {
            currentIndex = (currentIndex + 1) % dinoIds.size();
            applySelectionLocally();
        }));

        // Pose selection button
        this.poseButton = this.addRenderableWidget(new Button(cx - 50, cy + 40, 100, 20, Component.literal(getPoseName()), btn -> {
            poseIndex = (poseIndex + 1) % EntityAnimation.values().length;
            btn.setMessage(Component.literal(getPoseName()));
            applySelectionLocally();
        }));

        // Rotation toggle button
        this.rotateButton = this.addRenderableWidget(new Button(cx - 50, cy + 70, 100, 20, Component.literal(getRotateLabel()), btn -> {
            rotating = !rotating;
            if (!rotating) {
                rotation = HologramBlockEntity.snapRotation(rotation);
            }
            btn.setMessage(Component.literal(getRotateLabel()));
            updateAngleButtonState();
            applySelectionLocally();
        }));

        // Angle adjustment button
        this.angleButton = this.addRenderableWidget(new Button(cx - 50, cy + 100, 100, 20, Component.literal(getAngleLabel()), btn -> {
            rotation = HologramBlockEntity.snapRotation(rotation + HologramBlockEntity.ROTATION_STEP_DEGREES);
            btn.setMessage(Component.literal(getAngleLabel()));
            applySelectionLocally();
        }));
        updateAngleButtonState();

        // Confirm button - sends packet to server
        this.addRenderableWidget(new Button(cx - 50, cy + 130, 100, 20, Component.literal("Confirm"), btn -> {
            int selectedId = dinoIds.get(currentIndex);

            System.out.println("[HologramGUI] Sending packet to SERVER: dino=" + selectedId +
                    ", pose=" + poseIndex + ", rotating=" + rotating + ", rotation=" + rotation);

            // Send packet to server to save changes
            Network.sendToServer(new SetHologramDinosaurPacket(blockPos, selectedId, poseIndex, rotating, rotation));

            // Close the screen
            this.minecraft.setScreen(null);
        }));
    }

    @Override
    public void render(PoseStack ms, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(ms);
        drawCenteredString(ms, this.font, this.title, width / 2, height / 2 - 50, 0xFFFFFF);

        int id = dinoIds.get(currentIndex);
        Dinosaur dino = DinosaurHandler.getById(id);
        String name = dino.getName();
        drawCenteredString(ms, this.font, Component.literal(name), width / 2, height / 2 + 10, 0xFFFF00);

        super.render(ms, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }

    private String getPoseName() {
        EntityAnimation anim = EntityAnimation.values()[Math.floorMod(poseIndex, EntityAnimation.values().length)];
        return "Pose: " + anim.name().toLowerCase().replace('_', ' ');
    }

    private String getRotateLabel() {
        return "Rotate: " + (rotating ? "On" : "Off");
    }

    private String getAngleLabel() {
        return "Angle: " + rotation + "°";
    }

    private void updateAngleButtonState() {
        if (this.angleButton != null) {
            this.angleButton.visible = !rotating;
            this.angleButton.active = !rotating;
            this.angleButton.setMessage(Component.literal(getAngleLabel()));
        }
    }

    /**
     * Applies the current selection to the client-side block entity for immediate visual feedback
     * This does NOT save - only clicking "Confirm" saves via the server packet
     *
     * IMPORTANT: We suppress updates here so this doesn't trigger saves on the client
     */
    private void applySelectionLocally() {
        if (this.minecraft == null || this.minecraft.level == null || this.dinoIds == null || this.dinoIds.isEmpty()) {
            return;
        }

        BlockEntity be = this.minecraft.level.getBlockEntity(this.blockPos);
        if (!(be instanceof HologramBlockEntity hologram)) {
            return;
        }

        int selectedId = this.dinoIds.get(Math.floorMod(this.currentIndex, this.dinoIds.size()));

        // Suppress updates to prevent client from trying to save
        hologram.suppressUpdates = true;
        try {
            // Apply changes locally for immediate visual feedback only
            hologram.setDinosaurById(selectedId);
            hologram.setPoseIndex(this.poseIndex);
            hologram.setRotating(this.rotating);
            hologram.setRot(this.rotation);

            System.out.println("[HologramGUI] LOCAL preview: dino=" + selectedId +
                    ", pose=" + poseIndex + ", rotating=" + rotating + ", rotation=" + rotation);
        } finally {
            hologram.suppressUpdates = false;
        }
    }
}