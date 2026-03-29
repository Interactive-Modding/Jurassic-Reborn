package net.vit.jurassicreborn.client.screens.paleopad;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.common.entities.DinosaurEntity;
import net.vit.jurassicreborn.common.entities.DinosaurStatus;
import net.vit.jurassicreborn.common.util.LangUtil;
import com.mojang.blaze3d.vertex.PoseStack;

import java.util.*;

import static com.mojang.math.Axis.ZP;

public class PaleoPadViewDinosaurScreen extends Screen {
    private static final int SIZE_X = 256;
    private static final int SIZE_Y = 192;
    private static final int TOTAL_PAGES = 2;

    private static final ResourceLocation BACKGROUND_TEXTURE = ResourceLocation.fromNamespaceAndPath(JurassicReborn.MODID,"textures/gui/paleo_pad/paleo_pad.png");
    private static final ResourceLocation WIDGETS_TEXTURE = ResourceLocation.fromNamespaceAndPath(JurassicReborn.MODID, "textures/field_guide/widgets.png");
    private static final Map<DinosaurStatus, ResourceLocation> STATUS_TEXTURES = new HashMap<>();
    @Override
    protected void renderBlurredBackground(float partialTick) {
    }

    static {
        for (DinosaurStatus status : DinosaurStatus.values()) {
            STATUS_TEXTURES.put(status, ResourceLocation.fromNamespaceAndPath(JurassicReborn.MODID, "textures/field_guide/status/" + status.name().toLowerCase(Locale.ENGLISH) + ".png"));
        }
    }

    private final DinosaurEntity entity;
    private final DinosaurEntity.FieldGuideInfo guideInfo;
    private int page = 0;
    private Button nextPage;
    private Button previousPage;

    public PaleoPadViewDinosaurScreen(DinosaurEntity entity, DinosaurEntity.FieldGuideInfo guideInfo) {
        super(Component.literal("PaleoPad Dinosaur View"));
        this.entity = entity;
        this.guideInfo = guideInfo;
    }

    @Override
    protected void init() {
        super.init();
        int x = (this.width - SIZE_X) / 2;
        int y = (this.height - SIZE_Y) / 2;
        this.nextPage = addRenderableWidget(Button.builder(Component.literal(">"), b -> {
            if (this.page < TOTAL_PAGES - 1) this.page++;
            updateButtons();
        }).bounds(x + 213, y + 160, 23, 13).build());
        this.previousPage = addRenderableWidget(Button.builder(Component.literal("<"), b -> {
            if (this.page > 0) this.page--;
            updateButtons();
        }).bounds(x + 15, y + 160, 23, 13).build());
        updateButtons();
    }

    private void updateButtons() {
        if (nextPage != null) nextPage.visible = this.page < TOTAL_PAGES - 1;
        if (previousPage != null) previousPage.visible = this.page > 0;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        renderBackground(guiGraphics,mouseX, mouseY, partialTicks);

        int x = (this.width - SIZE_X) / 2;
        int y = (this.height - SIZE_Y) / 2;

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, BACKGROUND_TEXTURE);
        guiGraphics.blit(BACKGROUND_TEXTURE, x, y, 0, 0, 256, 256, 256, 256);

        Font font = Minecraft.getInstance().font;
        String dinoName = LangUtil.getDinoName(entity.getDinosaur()).getString().toUpperCase(Locale.ENGLISH);
        drawScaledString(guiGraphics, dinoName, x + 15, y + 14, 1.3F, 0);

        if (this.page == 0) {
            drawScaledString(guiGraphics, entity.getGrowthStage().getLocalization().toUpperCase(Locale.ENGLISH) + " // " + LangUtil.getGenderMode(entity.isMale() ? 1 : 2).toUpperCase(Locale.ENGLISH), x + 16, y + 24, 1.0F, 0);

            int statisticsX = x + (SIZE_X / 2) + 15;
            int statisticTextX = x + (SIZE_X / 2 + SIZE_X / 4);

            drawScaledString(guiGraphics, LangUtil.translate(LangUtil.GUI.get("dinosaur_statistics")), statisticsX, y + 14, 1.0F, 0);
            drawCenteredScaledString(guiGraphics, LangUtil.translate(LangUtil.GUI.get("health")), statisticTextX, y + 35, 1.0F, 0);
            drawCenteredScaledString(guiGraphics, LangUtil.translate(LangUtil.GUI.get("hunger")), statisticTextX, y + 65, 1.0F, 0);
            drawCenteredScaledString(guiGraphics, LangUtil.translate(LangUtil.GUI.get("thirst")), statisticTextX, y + 95, 1.0F, 0);
            drawCenteredScaledString(guiGraphics, LangUtil.translate(LangUtil.GUI.get("age")), statisticTextX, y + 125, 1.0F, 0);

            RenderSystem.setShaderTexture(0, WIDGETS_TEXTURE);
            drawBar(guiGraphics, statisticsX, y + 45, this.entity.isCarcass() ? 0 : this.entity.getHealth(), this.entity.getMaxHealth(), 0xFF0000);
            drawBar(guiGraphics, statisticsX, y + 75, this.guideInfo.hunger, this.entity.getMetabolism().getMaxEnergy(), 0x94745A);
            drawBar(guiGraphics, statisticsX, y + 105, this.guideInfo.thirst, this.entity.getMetabolism().getMaxWater(), 0x0000FF);
            drawBar(guiGraphics, statisticsX, y + 135, this.entity.getDinosaurAge(), entity.getDinosaur().getMaximumAge(), 0x00FF00);
            drawCenteredScaledString(guiGraphics, LangUtil.translate(LangUtil.GUI.get("days_old")).replace("{value}", String.valueOf(this.entity.getDaysExisted())), statisticTextX, y + 155, 1.0F, 0);
            // Dinosaur render
            renderDinosaurEntity(guiGraphics.pose(), x + 65, y + 110, (int) (70 / (entity.getDinosaur().getAdultSizeY() + (2 * entity.getDinosaur().getScaleAdult() + entity.getDinosaur().getPaleoPadScale()))), this.entity, partialTicks);
            // Status icons
            int statusX = 0;
            int statusY = 0;
            List<DinosaurStatus> activeStatuses = DinosaurStatus.getActiveStatuses(this.entity, this.guideInfo);
            for (DinosaurStatus status : activeStatuses) {
                RenderSystem.setShaderTexture(0, STATUS_TEXTURES.get(status));
                guiGraphics.blit(STATUS_TEXTURES.get(status), statusX + x + 31, statusY + y + (SIZE_Y - 40), 0, 0, 16, 16, 16, 16);
                statusX += 18;
                if (statusX > SIZE_X / 2 - 60) {
                    statusX = 0;
                    statusY -= 18;
                }
            }

            // Tooltip for status
            statusX = 0;
            statusY = 0;
            for (DinosaurStatus status : activeStatuses) {
                int size = 16;
                int renderX = statusX + x + 31;
                int renderY = statusY + y + (SIZE_Y - 40);

                if (mouseX >= renderX && mouseY >= renderY && mouseX <= renderX + size && mouseY <= renderY + size) {
                    guiGraphics.renderTooltip(this.font, Component.literal(LangUtil.translate(LangUtil.STATUS.get(status.name().toLowerCase(Locale.ENGLISH)))), mouseX, mouseY);
                }
                statusX += 18;
                if (statusX > SIZE_X / 2 - 60) {
                    statusX = 0;
                    statusY -= 18;
                }
            }
        } else {
            // Info page
            String text = LangUtil.getDinoInfo(entity.getDinosaur());
            List<String> lines = new ArrayList<>();
            int wrapX = 0;
            StringBuilder wrapLine = new StringBuilder();

            for (String word : text.split(" ")) {
                if (wrapX + font.width(word) > 220) {
                    lines.add(wrapLine.toString());
                    wrapLine = new StringBuilder();
                }
                wrapLine.append(word).append(" ");
                wrapX = font.width(wrapLine.toString().trim());
            }
            lines.add(wrapLine.toString());

            int lineX = 0;
            int lineY = y + 35;
            for (String line : lines) {
                drawCenteredScaledString(guiGraphics, line.trim(), x + 128 + lineX, lineY, 1.0F, 0);
                lineY += 10;
                if (lineY > y + 192 - 35) {
                    lineX += 128;
                    lineY = y + 10;
                }
            }
        }

        super.render(guiGraphics, mouseX, mouseY, partialTicks);
    }

    private void drawBar(GuiGraphics guiGraphics, int x, int y, float value, float max, int color) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        guiGraphics.blit(WIDGETS_TEXTURE, x, y, 0, 179, 98, 8, 256, 256);
        float r = ((color >> 16) & 255) / 255.0F;
        float g = ((color >> 8) & 255) / 255.0F;
        float b = (color & 255) / 255.0F;
        RenderSystem.setShaderColor(r, g, b, 1.0F);
        guiGraphics.blit(WIDGETS_TEXTURE, x, y, 0, 187, Math.max(0, Math.min(98, (int) ((value / max) * 98))), 8, 256, 256);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    private void drawScaledString(GuiGraphics guiGraphics, String text, float x, float y, float scale, int color) {
        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();
        poseStack.translate(x, y, 0);
        poseStack.scale(scale, scale, 1.0F);
        guiGraphics.drawString(Minecraft.getInstance().font, text, 0, 0, color, false);
        poseStack.popPose();
    }

    private void drawCenteredScaledString(GuiGraphics guiGraphics, String text, float x, float y, float scale, int color) {
        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();
        poseStack.translate(x, y, 0);
        poseStack.scale(scale, scale, 1.0F);
        Font font = Minecraft.getInstance().font;
        int drawX = Math.round(-font.width(text) / 2F);
        guiGraphics.drawString(font, text, drawX, 0, color, false);
        poseStack.popPose();
    }

    private void renderDinosaurEntity(PoseStack poseStack, int posX, int posY, float scale, DinosaurEntity entity, float partialTicks) {
        EntityRenderDispatcher renderDispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        poseStack.pushPose();
        poseStack.translate(posX, posY, 50.0F);
        poseStack.scale(-scale, scale, scale);
        poseStack.mulPose(Axis.ZP.rotationDegrees(180.0F));
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
        renderDispatcher.setRenderShadow(false);
        Minecraft mc = Minecraft.getInstance();
        renderDispatcher.render(entity, 0.0D, 0.0D, 0.0D, 0.0F, partialTicks, poseStack, mc.renderBuffers().bufferSource(), 15728880);
        mc.renderBuffers().bufferSource().endBatch();
        renderDispatcher.setRenderShadow(true);
        poseStack.popPose();
    }
    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
