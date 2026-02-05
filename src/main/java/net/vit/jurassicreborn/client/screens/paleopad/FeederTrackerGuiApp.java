package net.vit.jurassicreborn.client.screens.paleopad;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.client.screens.PaleoPadScreen;
import net.vit.jurassicreborn.common.blocks.entities.feeder.FeederBlockEntity;
import net.vit.jurassicreborn.common.paleopad.App;
import net.vit.jurassicreborn.common.paleopad.FeederTrackerApp;

import java.util.List;

/** GUI implementation for the feeder tracker app. */
public class FeederTrackerGuiApp extends GuiApp {

    private static final ResourceLocation TEXTURE = new ResourceLocation(JurassicReborn.MODID,
            "textures/gui/paleo_pad/apps/feeder_tracker.png");

    private int scroll;

    public FeederTrackerGuiApp(App app) {
        super(app);
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, Screen screen, float partialTicks) {
        int left = screen.width / 2 - 115;
        int top = 65;

        FeederTrackerApp tracker = (FeederTrackerApp) app;
        List<FeederTrackerApp.TrackedFeeder> list = tracker.getFeeders();
        Level level = Minecraft.getInstance().level;

        int visible = 5;
        int y = top + 20;
        for (int i = 0; i < visible && scroll + i < list.size(); i++) {
            FeederTrackerApp.TrackedFeeder f = list.get(scroll + i);
            String label;
            int color = 0xFFFFFF;
            if (level != null && level.getBlockEntity(f.pos) instanceof FeederBlockEntity) {
                int food = tracker.getFood(level, f);
                color = food <= FeederTrackerApp.LOW_FOOD_THRESHOLD ? 0xFF4040 : 0xFFFFFF;
                label = f.name + " - " + food;
            } else {
                label = f.name + " - (Out of range!)";
            }
            ((PaleoPadScreen) screen).drawScaledText(poseStack, label, left + 10, y, 0.9F, color);
            y += 12;
        }

        // Draw scroll bar
        int trackX = left + 190;
        int trackY = top + 20;
        int trackHeight = visible * 12;
        ((PaleoPadScreen) screen).drawScaledRect(poseStack, trackX, trackY, 4, trackHeight, 1.0F, 0xFF303030);
        int total = list.size();
        if (total > visible) {
            int knobHeight = Math.max(8, trackHeight * visible / total);
            int maxScroll = total - visible;
            int knobY = trackY + (trackHeight - knobHeight) * scroll / maxScroll;
            ((PaleoPadScreen) screen).drawScaledRect(poseStack, trackX, knobY, 4, knobHeight, 1.0F, 0xFF808080);
        }
    }

    @Override
    public void actionPerformed(net.minecraft.client.gui.components.Button button) { }

    @Override
    public void mouseClicked(double mouseX, double mouseY, Screen screen) {
        int left = screen.width / 2 - 115;
        int top = 65;
        int trackX = left + 190;
        int trackY = top + 20;
        int trackHeight = 5 * 12;
        int total = ((FeederTrackerApp) app).getFeeders().size();
        int visible = 5;
        if (mouseX >= trackX && mouseX <= trackX + 4 && mouseY >= trackY && mouseY <= trackY + trackHeight && total > visible) {
            int knobHeight = Math.max(8, trackHeight * visible / total);
            int maxScroll = total - visible;
            int rel = (int) mouseY - trackY - knobHeight / 2;
            scroll = Mth.clamp((int) ((double) rel / (trackHeight - knobHeight) * maxScroll), 0, maxScroll);
        }
    }

    @Override
    public void init() {
        scroll = 0;
    }

    @Override
    public ResourceLocation getTexture(Screen screen) {
        return TEXTURE;
    }
}

