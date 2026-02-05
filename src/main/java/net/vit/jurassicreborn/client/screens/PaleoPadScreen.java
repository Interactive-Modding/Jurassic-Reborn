package net.vit.jurassicreborn.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.gui.GuiGraphics;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.client.screens.paleopad.GuiApp;
import net.vit.jurassicreborn.client.screens.paleopad.GuiAppRegistry;
import net.vit.jurassicreborn.common.paleopad.App;
import net.vit.jurassicreborn.common.paleopad.AppHandler;
import net.vit.jurassicreborn.common.util.networking.PlayerData;

import java.util.List;

public class PaleoPadScreen extends Screen {
    private static final ResourceLocation TEXTURE = new ResourceLocation(JurassicReborn.MODID, "textures/gui/paleo_pad/paleo_pad.png");
    private final App initialApp;
    private GuiApp focus;

    public PaleoPadScreen() {
        this(null);
    }

    public PaleoPadScreen(App initialApp) {
        super(Component.literal("PaleoPad"));
        this.initialApp = initialApp;
    }
    @Override
    public void tick() {
        super.tick();
        if (focus != null) {
            focus.update();
            if (focus.doesRequestShutdown()) {
                focus = null;
            }
        }
    }
    @Override
    protected void init() {
        super.init();
        if (initialApp != null && minecraft != null && minecraft.player != null) {
            focus = GuiAppRegistry.getGui(initialApp);
            focus.init();
            PlayerData.get(minecraft.player).openApp(initialApp);
        }
        // Set up buttons here if needed
    }

    @Override
    public void removed() {
        // This is called when the screen is closed
        if (focus != null && minecraft != null && minecraft.player != null) {
            PlayerData.get(minecraft.player).closeApp(focus.getApp());
        }
    }
    public void drawScaledRect(GuiGraphics guiGraphics, int x, int y, int w, int h, float scale, int color) {
        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();
        poseStack.translate(x, y, 0);
        poseStack.scale(scale, scale, 1.0F);
        guiGraphics.fill(0, 0, w, h, color);
        poseStack.popPose();
    }
    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        int width = this.width;
        int height = this.height;
        Minecraft mc = Minecraft.getInstance();

        if (focus == null) {
            List<App> apps = AppHandler.INSTANCE.getApps();

            for (int i = 0; i < apps.size(); i++) {
                int x = ((i % 4) * 55) + width / 2 - 110;
                int y = ((i / 4) * 38) + 70;

                if (mouseX > x && mouseY > y && mouseX < x + 32 && mouseY < y + 32) {
                    App app = apps.get(i);
                    focus = GuiAppRegistry.getGui(app);
                    focus.init();
                    PlayerData.get(mc.player).openApp(app);

                    // TODO: Button handling if needed
                    break;
                }
            }
        } else {
            focus.mouseClicked((int) mouseX, (int) mouseY, this);

            int adjX = (int) (mouseX - width / 2 + 115);
            int adjY = (int) (mouseY - 65);

            if (adjX > 97 && adjY > 153 && adjX < 131 && adjY < 157) {
                focus.requestShutdown();
                focus = null;
            }
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        renderBackground(guiGraphics);

        Minecraft mc = Minecraft.getInstance();
        Font font = mc.font;

        // Draw background
        RenderSystem.setShaderTexture(0, TEXTURE);
        guiGraphics.blit(TEXTURE, width / 2 - 128, 40, 0, 0, 256, 256, 256, 256);

        List<App> apps = AppHandler.INSTANCE.getApps();

        // World clock logic - adjust as needed for MC 1.19.2
        double worldTime = mc.level != null ? (mc.level.getDayTime() + 6000) % 24000 : 0;
        int hours = (int) (worldTime / 1000) % 24;
        int minutes = (int) ((worldTime / 1000.0 - hours) * 60);

        String hoursStr = String.format("%02d", hours);
        String minutesStr = String.format("%02d", minutes);

        drawCenteredScaledText(guiGraphics, hoursStr + ":" + minutesStr, width / 2, 50, 1.0F, 0xFFFFFF);

        // Draw separator (simulate scaled line)

        if (focus == null) {
//            fill(poseStack, width / 2 - 229, 75, width / 2 + 229, 77, 0xFF404040);
            for (int i = 0; i < apps.size(); i++) {
                int x = (i % 4) * 50 + 5;
                int y = (i / 4) * 42;

                App app = apps.get(i);
                GuiApp gui = GuiAppRegistry.getGui(app);

                RenderSystem.setShaderTexture(0, gui.getTexture(this));
                guiGraphics.blit(gui.getTexture(this), x + 5 + width / 2 - 115, y + 5 + 65, 0, 0, 32, 32, 32, 32);

                drawCenteredScaledText(guiGraphics, app.getName(), x + 22 + width / 2 - 115, y + 39 + 65, 0.7F, 0xFFFFFF);
            }
            drawScaledText(guiGraphics, Component.translatable("paleopad.os.name").getString(), width / 2 - 115 + 2, 65 - 10, 1.0F, 0xFFFFFF);
        } else {
            if (focus.getApp().getName() != null) {
                drawScaledText(guiGraphics, focus.getApp().getName(), width / 2 - 115 + 2, 65 - 10, 1.0F, 0xFFFFFF);
                focus.render(guiGraphics, mouseX, mouseY, this, partialTicks);
            } else {
                focus = null;
            }
        }

        super.render(guiGraphics, mouseX, mouseY, partialTicks);
    }

    // ----------- Helper Methods ---------------

    public void drawCenteredScaledText(GuiGraphics guiGraphics, String text, int x, int y, float scale, int color) {
        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();
        poseStack.translate(x, y, 0);
        poseStack.scale(scale, scale, 1.0F);
        int width = Minecraft.getInstance().font.width(text);
        int drawX = Math.round(-width / 2F);
        guiGraphics.drawString(Minecraft.getInstance().font, text, drawX, 0, color, false);
        poseStack.popPose();
    }

    public void drawScaledText(GuiGraphics guiGraphics, String text, int x, int y, float scale, int color) {
        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();
        poseStack.translate(x, y, 0);
        poseStack.scale(scale, scale, 1.0F);
        guiGraphics.drawString(Minecraft.getInstance().font, text, 0, 0, color, false);
        poseStack.popPose();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
