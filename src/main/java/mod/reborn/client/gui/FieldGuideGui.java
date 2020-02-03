package mod.reborn.client.gui;

import mod.reborn.RebornMod;
import mod.reborn.server.dinosaur.Dinosaur;
import mod.reborn.server.entity.DinosaurEntity;
import mod.reborn.server.entity.DinosaurStatus;
import mod.reborn.server.util.LangUtils;
import net.ilexiconn.llibrary.LLibrary;
import net.ilexiconn.llibrary.client.util.ClientUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@SideOnly(Side.CLIENT)
public class FieldGuideGui extends GuiScreen {
    private static final int SIZE_X = 256;
    private static final int SIZE_Y = 192;
    private static final int TOTAL_PAGES = 2;

    private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(RebornMod.MODID, "textures/field_guide/background.png");
    private static final ResourceLocation WIDGETS_TEXTURE = new ResourceLocation(RebornMod.MODID, "textures/field_guide/widgets.png");
    private static final Map<DinosaurStatus, ResourceLocation> STATUS_TEXTURES = new HashMap<>();

    static {
        for (DinosaurStatus status : DinosaurStatus.values()) {
            STATUS_TEXTURES.put(status, new ResourceLocation(RebornMod.MODID, "textures/field_guide/status/" + status.name().toLowerCase(Locale.ENGLISH) + ".png"));
        }
    }

    private DinosaurEntity entity;
    private DinosaurEntity.FieldGuideInfo fieldGuideInfo;
    private PageButton nextPage;
    private PageButton previousPage;
    private int page;

    public FieldGuideGui(DinosaurEntity entity, DinosaurEntity.FieldGuideInfo fieldGuideInfo) {
        this.entity = entity;
        this.fieldGuideInfo = fieldGuideInfo;
    }

    @Override
    public void initGui() {
        super.initGui();

        int x = (this.width - SIZE_X) / 2;
        int y = (this.height - SIZE_Y) / 2;
        this.buttonList.add(this.nextPage = new PageButton(0, x + 235, y + 180, true));
        this.buttonList.add(this.previousPage = new PageButton(1, x - 3, y + 180, false));
        this.updateButtons();
    }

    private void updateButtons() {
        this.nextPage.visible = this.page < TOTAL_PAGES - 1;
        this.previousPage.visible = this.page > 0;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ScaledResolution scaledResolution = new ScaledResolution(this.mc);
        int scaleFactor = scaledResolution.getScaleFactor();

        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);

        int x = (this.width - SIZE_X) / 2;
        int y = (this.height - SIZE_Y) / 2;

        this.zLevel = -1000;

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        this.mc.getTextureManager().bindTexture(BACKGROUND_TEXTURE);
        this.drawTexturedModalRect(x, y, 0, 0, 256, 256);

        Dinosaur dinosaur = this.entity.getDinosaur();

        this.drawScaledString(LangUtils.getDinoName(dinosaur).toUpperCase(Locale.ENGLISH), x + 15, y + 10, 1.3F, 0);

        if (this.page == 0) {
            this.drawScaledString(this.entity.getGrowthStage().getLocalization().toUpperCase(Locale.ENGLISH) + " // " + LangUtils.getGenderMode(entity.isMale() ? 1 : 2).toUpperCase(Locale.ENGLISH), x + 16, y + 24, 1.0F, 0);

            int statisticsX = x + (SIZE_X / 2) + 15;

            DecimalFormat decimalFormat = new DecimalFormat("#.#");
            decimalFormat.setRoundingMode(RoundingMode.HALF_UP);

            this.drawScaledString(LangUtils.translate(LangUtils.GUI.get("dinosaur_statistics")), statisticsX, y + 10, 1.0F, 0);
            int statisticTextX = x + (SIZE_X / 2 + SIZE_X / 4);
            this.drawCenteredScaledString(LangUtils.translate(LangUtils.GUI.get("health")), statisticTextX, y + 35, 1.0F, 0);
            this.drawCenteredScaledString(LangUtils.translate(LangUtils.GUI.get("hunger")), statisticTextX, y + 65, 1.0F, 0);
            this.drawCenteredScaledString(LangUtils.translate(LangUtils.GUI.get("thirst")), statisticTextX, y + 95, 1.0F, 0);
            this.drawCenteredScaledString(LangUtils.translate(LangUtils.GUI.get("age")), statisticTextX, y + 125, 1.0F, 0);

            this.mc.getTextureManager().bindTexture(WIDGETS_TEXTURE);

            this.drawBar(statisticsX, y + 45, this.entity.isCarcass() ? 0 : this.entity.getHealth(), this.entity.getMaxHealth(), 0xFF0000);
            this.drawBar(statisticsX, y + 75, this.fieldGuideInfo.hunger, this.entity.getMetabolism().getMaxEnergy(), 0x94745A);
            this.drawBar(statisticsX, y + 105, this.fieldGuideInfo.thirst, this.entity.getMetabolism().getMaxWater(), 0x0000FF);
            this.drawBar(statisticsX, y + 135, this.entity.getDinosaurAge(), dinosaur.getMaximumAge(), 0x00FF00);

            this.drawCenteredScaledString(LangUtils.translate(LangUtils.GUI.get("days_old")).replace("{value}", String.valueOf(this.entity.getDaysExisted())), statisticTextX, y + 155, 1.0F, 0);

            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            GL11.glEnable(GL11.GL_SCISSOR_TEST);
            GL11.glScissor((x + 15) * scaleFactor, (this.height - y - 140) * scaleFactor, 100 * scaleFactor, 100 * scaleFactor);
            this.drawEntity(x + 65, y + 110, 45.0F / this.entity.height, this.entity);
            GL11.glDisable(GL11.GL_SCISSOR_TEST);

            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            int statusX = 0;
            int statusY = 0;

            List<DinosaurStatus> activeStatuses = DinosaurStatus.getActiveStatuses(this.entity, this.fieldGuideInfo);

            for (DinosaurStatus status : activeStatuses) {
                this.mc.getTextureManager().bindTexture(STATUS_TEXTURES.get(status));

                int size = 16;

                this.drawFullTexturedRect(statusX + x + 31, statusY + y + (SIZE_Y - 40), size, size);

                statusX += 18;

                if (statusX > SIZE_X / 2 - 60) {
                    statusX = 0;
                    statusY -= 18;
                }
            }

            statusX = 0;
            statusY = 0;

            for (DinosaurStatus status : activeStatuses) {
                int size = 16;

                int renderX = statusX + x + 31;
                int renderY = statusY + y + (SIZE_Y - 40);

                if (mouseX >= renderX && mouseY >= renderY && mouseX <= renderX + size && mouseY <= renderY + size) {
                    this.drawHoveringText(LangUtils.translate(LangUtils.STATUS.get(status.name().toLowerCase(Locale.ENGLISH))), mouseX, mouseY);
                }

                statusX += 18;

                if (statusX > SIZE_X / 2 - 60) {
                    statusX = 0;
                    statusY -= 18;
                }
            }

            GlStateManager.disableLighting();
        } else {
            String text = LangUtils.getDinoInfo(dinosaur);
            List<String> lines = new ArrayList<>();

            int wrapX = 0;
            StringBuilder wrapLine = new StringBuilder();

            for (String word : text.split(" ")) {
                if (wrapX + (this.fontRenderer.getStringWidth(word)) > 90) {
                    lines.add(wrapLine.toString());
                    wrapLine = new StringBuilder();
                }

                wrapLine.append(word).append(" ");
                wrapX = this.fontRenderer.getStringWidth(wrapLine.toString().trim());
            }

            lines.add(wrapLine.toString());

            int lineX = 0;
            int lineY = y + 25;

            for (String line : lines) {
                this.drawCenteredScaledString(line.trim(), x + (SIZE_X / 4) + lineX, lineY, 1.0F, 0);

                lineY += 10;

                if (lineY > y + SIZE_Y - 35) {
                    lineX += SIZE_X / 2;
                    lineY = y + 10;
                }
            }
        }
    }

    private void drawBar(int x, int y, float value, float max, int color) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.drawTexturedModalRect(x, y, 0, 179, 98, 8);
        GlStateManager.color((color >> 16 & 255) / 255.0F, (color >> 8 & 255) / 255.0F, (color & 255) / 255.0F);
        this.drawTexturedModalRect(x, y, 0, 187, Math.max(0, Math.min(98, (int) ((value / max) * 98))), 8);
    }

    private void drawScaledString(String text, float x, float y, float scale, int color) {
        GlStateManager.pushMatrix();
        GlStateManager.scale(scale, scale, 1.0F);
        this.fontRenderer.drawString(text, x / scale, y / scale, color, false);
        GlStateManager.popMatrix();
    }

    private void drawCenteredScaledString(String text, float x, float y, float scale, int color) {
        GlStateManager.pushMatrix();
        GlStateManager.scale(scale, scale, 1.0F);
        this.fontRenderer.drawString(text, (x - this.fontRenderer.getStringWidth(text) / 2) / scale, y / scale, color, false);
        GlStateManager.popMatrix();
    }

    private void drawFullTexturedRect(int x, int y, int width, int height) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        buffer.pos(x, y + height, this.zLevel).tex(0.0F, 1.0F).endVertex();
        buffer.pos(x + width, y + height, this.zLevel).tex(1.0F, 1.0F).endVertex();
        buffer.pos(x + width, y, this.zLevel).tex(1.0F, 0.0F).endVertex();
        buffer.pos(x, y, this.zLevel).tex(0.0F, 0.0F).endVertex();
        tessellator.draw();
    }

    @Override
    public void actionPerformed(GuiButton button) {
        if (button.enabled) {
            if (button.id == 0) {
                if (this.page < TOTAL_PAGES) {
                    this.page++;
                }
            } else if (button.id == 1) {
                if (this.page > 0) {
                    this.page--;
                }
            }

            this.updateButtons();
        }
    }

    public void drawEntity(int posX, int posY, float scale, EntityLivingBase entity) {
        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) posX, (float) posY, 50.0F);
        GlStateManager.scale(-scale, scale, scale);
        GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(15.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(135.0F, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.translate(0.0F, 0.0F, 0.0F);
        RenderManager renderManager = this.mc.getRenderManager();
        renderManager.setPlayerViewY(180.0F);
        renderManager.setRenderShadow(false);
        GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
        float partialTicks = LLibrary.PROXY.getPartialTicks();
        GlStateManager.rotate(ClientUtils.interpolate(entity.prevRenderYawOffset, entity.renderYawOffset, partialTicks), 0.0F, 1.0F, 0.0F);
        renderManager.renderEntity(entity, 0.0D, 0.0D, 0.0D, 0.0F, partialTicks, false);
        renderManager.setRenderShadow(true);
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @SideOnly(Side.CLIENT)
    static class PageButton extends GuiButton {
        private final boolean isForward;

        public PageButton(int id, int x, int y, boolean isForward) {
            super(id, x, y, 23, 13, "");
            this.isForward = isForward;
        }

        @Override
        public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
            if (this.visible) {
                boolean selected = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                mc.getTextureManager().bindTexture(WIDGETS_TEXTURE);

                int u = 0;
                int v = 194;

                if (selected) {
                    u += 23;
                }

                if (!this.isForward) {
                    v += 13;
                }

                this.drawTexturedModalRect(this.x, this.y, u, v, 23, 13);
            }
        }
    }
}
