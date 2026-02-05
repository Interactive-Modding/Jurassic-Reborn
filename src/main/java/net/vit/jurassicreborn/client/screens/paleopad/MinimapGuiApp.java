package net.vit.jurassicreborn.client.screens.paleopad;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MaterialColor;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.client.screens.PaleoPadScreen;
import net.vit.jurassicreborn.common.entities.DinosaurEntity;
import net.vit.jurassicreborn.common.paleopad.App;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MinimapGuiApp extends GuiApp {

    private static final ResourceLocation TEXTURE = new ResourceLocation(JurassicReborn.MODID, "textures/gui/paleo_pad/apps/minimap.png");
    private final Map<BlockPos, Integer> heights = new HashMap<>();
    private int scroll;

    public MinimapGuiApp(App app) {
        super(app);
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, Screen screen, float partialTicks) {
        Player player = Minecraft.getInstance().player;
        if (player == null) return;
        Level world = player.level;
        if (world == null) return;

        int left = screen.width / 2 - 115;
        int top = 65;

        // Display player coordinates INSIDE the minimap app panel!
        String loc = "Location: " + Mth.floor(player.getX()) + " " + Mth.floor(player.getY()) + " " + Mth.floor(player.getZ());

        // === Show ONLY dinosaurs tracked by this player ===
        UUID playerUUID = player.getUUID();
        List<DinosaurEntity> tracked = new ArrayList<>();
        for (Entity entity : world.getEntities(player, player.getBoundingBox().inflate(256))) {
            if (entity instanceof DinosaurEntity dino && dino.getTrackers().contains(playerUUID)) {
                tracked.add(dino);
            }
        }

        // ==== Draw minimap below the entity list (unchanged) ====
        int playerX = Mth.floor(player.getX());
        int playerZ = Mth.floor(player.getZ());
        int playerChunkX = playerX >> 4;
        int playerChunkZ = playerZ >> 4;
        int mapStartX = left + 80;
        int mapStartZ = top + 20;
        int mapSize = 16 * 8;
        for (int chunkX = playerChunkX - 4, renderChunkX = 0; chunkX < playerChunkX + 4; chunkX++, renderChunkX++) {
            for (int chunkZ = playerChunkZ - 4, renderChunkZ = 0; chunkZ < playerChunkZ + 4; chunkZ++, renderChunkZ++) {
                LevelChunk chunk = world.getChunk(chunkX, chunkZ);
                for (int x = 0; x < 16; x++) {
                    for (int z = 0; z < 16; z++) {
                        int blockX = x + (chunkX * 16);
                        int blockZ = z + (chunkZ * 16);

                        BlockPos pos = getHeight(world, blockX, blockZ);
                        int blockY = pos.getY();

                        BlockState blockState = world.getBlockState(pos);
                        MaterialColor materialColor = blockState.getMapColor(world, pos);
                        int rgb = materialColor.col;

                        int r = (rgb >> 16) & 0xff;
                        int g = (rgb >> 8) & 0xff;
                        int b = rgb & 0xff;

                        int lightnessOffset = 0;
                        lightnessOffset -= getHeight(world, blockX - 1, blockZ).getY() > blockY ? 10 : 0;
                        lightnessOffset -= getHeight(world, blockX, blockZ - 1).getY() > blockY ? 10 : 0;
                        lightnessOffset -= getHeight(world, blockX + 1, blockZ).getY() > blockY ? 10 : 0;
                        lightnessOffset -= getHeight(world, blockX, blockZ + 1).getY() > blockY ? 10 : 0;

                        r = Mth.clamp(r + lightnessOffset, 0, 255);
                        g = Mth.clamp(g + lightnessOffset, 0, 255);
                        b = Mth.clamp(b + lightnessOffset, 0, 255);

                        rgb = (r << 16) | (g << 8) | b;
                        int argb = 0xFF000000 | rgb;
                        // Each block is a single pixel on the minimap
                        int px = mapStartX + renderChunkX * 16 + x; // shift left slightly
                        int pz = mapStartZ + renderChunkZ * 16 + z; // shift up slightly

                        ((PaleoPadScreen) screen).drawScaledRect(poseStack, px, pz, 1, 1, 1.0F, argb);
                    }
                }
            }
        }
        // === Player marker ===
        int playerPx = mapStartX + playerX - ((playerChunkX - 4) * 16);
        int playerPz = mapStartZ + playerZ - ((playerChunkZ - 4) * 16);
        if (playerPx >= mapStartX && playerPx < mapStartX + mapSize && playerPz >= mapStartZ && playerPz < mapStartZ + mapSize) {
            ((PaleoPadScreen) screen).drawScaledRect(poseStack, playerPx - 1, playerPz - 1, 3, 3, 1.0F, 0xFFFFFFFF);
            ((PaleoPadScreen) screen).drawScaledText(poseStack, "YOU", playerPx + 2, playerPz - 5, 0.6F, 0xFFFFFF);
        }

        // === Dinosaur markers ===
        for (Entity entity : world.getEntities(player, player.getBoundingBox().inflate(256))) {
            if (entity instanceof DinosaurEntity dino && dino.getTrackers().contains(playerUUID)) {
                int dinoX = Mth.floor(dino.getX());
                int dinoZ = Mth.floor(dino.getZ());
                int px = mapStartX + dinoX - ((playerChunkX - 4) * 16);
                int pz = mapStartZ + dinoZ - ((playerChunkZ - 4) * 16);
                if (px >= mapStartX && px < mapStartX + mapSize && pz >= mapStartZ && pz < mapStartZ + mapSize) {
                    int color = 0xFF000000 | (dino.getUUID().hashCode() & 0xFFFFFF);
                    ((PaleoPadScreen) screen).drawScaledRect(poseStack, px - 1, pz - 1, 3, 3, 1.0F, color);
                    String name = dino.getDisplayName().getString();
                    ((PaleoPadScreen) screen).drawScaledText(poseStack, name, px + 3, pz - 5, 0.6F, color);
                }
            }
        }

        // === Draw text overlays last so they appear above the minimap ===
        ((PaleoPadScreen) screen).drawScaledText(poseStack, loc, left + 10, top + 10, 1.0F, 0xFFFFFF);

        int visible = 4;
        int itemHeight = 11;
        int trackedY = top + 30;
        for (int i = 0; i < visible && scroll + i < tracked.size(); i++) {
            DinosaurEntity dino = tracked.get(scroll + i);
            String label = dino.getDisplayName().getString() + " at " + dino.blockPosition().toShortString();
            ((PaleoPadScreen) screen).drawScaledText(poseStack, label, left + 10, trackedY, 0.85F, 0xA0FF40);
            trackedY += itemHeight;
        }

        // Draw scroll bar
        int trackX = left + 74;
        int trackY = top + 30;
        int trackHeight = visible * itemHeight;
        ((PaleoPadScreen) screen).drawScaledRect(poseStack, trackX, trackY, 4, trackHeight, 1.0F, 0xFF303030);
        int total = tracked.size();
        if (total > visible) {
            int knobHeight = Math.max(8, trackHeight * visible / total);
            int maxScroll = total - visible;
            int knobY = trackY + (trackHeight - knobHeight) * scroll / maxScroll;
            ((PaleoPadScreen) screen).drawScaledRect(poseStack, trackX, knobY, 4, knobHeight, 1.0F, 0xFF808080);
        }
    }

    private BlockPos getHeight(Level world, int x, int z) {
        BlockPos posKey = new BlockPos(x, 0, z);

        if (heights.containsKey(posKey)) {
            return new BlockPos(x, heights.get(posKey), z);
        } else {
            int y = world.getHeight(Heightmap.Types.WORLD_SURFACE, x, z) - 1;
            BlockPos pos = new BlockPos(x, y, z);

            while ((world.getBlockState(pos).isAir() || world.getBlockState(pos).getFluidState().is(Fluids.WATER))
                    && y > world.getMinBuildHeight()) {
                y--;
                pos = new BlockPos(x, y, z);
            }
            BlockPos up = pos.above();
            if (!world.getBlockState(up).isAir()) {
                pos = up;
            }
            heights.put(posKey, pos.getY());
            return pos;
        }
    }

    @Override
    public void actionPerformed(Button button) {
        // Implement button logic as needed
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, Screen screen) {
        Player player = Minecraft.getInstance().player;
        if (player == null) return;
        Level world = player.level;
        if (world == null) return;

        int left = screen.width / 2 - 115;
        int top = 65;
        int visible = 4;
        int itemHeight = 11;
        int trackX = left + 74;
        int trackY = top + 30;
        int trackHeight = visible * itemHeight;

        UUID playerUUID = player.getUUID();
        List<DinosaurEntity> tracked = new ArrayList<>();
        for (Entity entity : world.getEntities(player, player.getBoundingBox().inflate(256))) {
            if (entity instanceof DinosaurEntity dino && dino.getTrackers().contains(playerUUID)) {
                tracked.add(dino);
            }
        }
        int total = tracked.size();
        if (mouseX >= trackX && mouseX <= trackX + 4 && mouseY >= trackY && mouseY <= trackY + trackHeight && total > visible) {
            int knobHeight = Math.max(8, trackHeight * visible / total);
            int maxScroll = total - visible;
            int rel = (int) mouseY - trackY - knobHeight / 2;
            scroll = Mth.clamp((int) ((double) rel / (trackHeight - knobHeight) * maxScroll), 0, maxScroll);
        }
    }

    @Override
    public void init() {
        heights.clear();
        scroll = 0;
    }

    @Override
    public ResourceLocation getTexture(Screen screen) {
        return TEXTURE;
    }
}
