package mod.reborn.client.gui;
import mod.reborn.RebornMod;
import mod.reborn.server.dinosaur.Dinosaur;
import mod.reborn.server.entity.Diet;
import mod.reborn.server.entity.DinosaurEntity;
import mod.reborn.server.entity.GrowthStage;
import net.ilexiconn.llibrary.LLibrary;
import net.ilexiconn.llibrary.client.util.ClientUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import mod.reborn.server.paleopad.*;

import java.util.List;

import static net.minecraft.util.text.translation.I18n.translateToLocal;

@SideOnly(Side.CLIENT)
public class PaleoPadViewDinosaurGui extends GuiScreen {
    private static final int SIZE_X = 256;
    private static final int SIZE_Y = 192;
    private static final ResourceLocation texture = new ResourceLocation(RebornMod.MODID, "textures/gui/paleo_pad/paleo_pad.png");

    private DinosaurEntity entity;

    public PaleoPadViewDinosaurGui(DinosaurEntity entity) {
        this.entity = entity;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ScaledResolution scaledResolution = new ScaledResolution(this.mc);
        int scaleFactor = scaledResolution.getScaleFactor();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        super.drawScreen(mouseX, mouseY, partialTicks);
        mc.getTextureManager().bindTexture(texture);

        int x = (this.width - SIZE_X) / 2;
        int y = (this.height - SIZE_Y) / 2;

        ScaledResolution dimensions = new ScaledResolution(mc);
        int scaledWidth = dimensions.getScaledWidth();
        drawTexturedModalRect(scaledWidth / 2 - 128, 40, 0, 0, 256, 256);

        double worldTime = mc.world.getWorldTime() + 6000 % 24000;


        double hours = worldTime / 1000;
        double minutes = hours * 60;

        String hoursStr = "" + (int) hours % 24;

        while (hoursStr.length() < 2) {
            hoursStr = "0" + hoursStr;
        }

        Dinosaur dinosaur = this.entity.getDinosaur();

        String minutesStr = "" + (int) minutes % 60;

        while (minutesStr.length() < 2) {
            minutesStr = "0" + minutesStr;
        }
        Dinosaur dinoDef = this.entity.getDinosaur();

        drawScaledRect(0, 0, 458, 2, 0.5F, 0x404040);

        drawScaledText(translateToLocal("paleopad.os.name"), 2, -10, 1.0F, 0xFFFFFF);

        this.zLevel = -100;
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor((x + 13) * scaleFactor, (this.height - y - 240) * scaleFactor, 229 * scaleFactor, 110 * scaleFactor);
        this.drawEntityOnScreen(115, 140, (int) (70 / (dinoDef.getAdultSizeY() + (2 * dinoDef.getScaleAdult() + dinoDef.getPaleoPadScale()))), this.entity);
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        drawScaledText("Viewing: " + this.entity.getName(), 5, 5, 1.0F, this.entity.isMale() ? dinoDef.getEggPrimaryColorMale() : dinoDef.getEggPrimaryColorFemale());
        drawScaledText("Age: " + this.entity.getDaysExisted() + " days", 5, 20, 1.0F, 0x808080);
        drawScaledText("Hunger: " + this.entity.getMetabolism().getEnergy() + "/" + this.entity.getMetabolism().getMaxEnergy(), 5, 35, 1.0F, 0x808080);
        drawScaledText("Thirst: " + this.entity.getMetabolism().getWater() + "/" + this.entity.getMetabolism().getMaxWater(), 5, 50, 1.0F, 0x808080);

        Diet diet = dinoDef.getDiet();
        drawScaledEndText((this.entity.isMale() ? "MALE" : "FEMALE"), 225, 5, 1.0F, 0xFFFF00);
        //Not used
        drawScaledEndText(dinoDef.getDietName, 225, 35, 1.0F, diet.hashCode());
        drawScaledEndText(dinoDef.getSleepTime().toString(), 225, 20, 1.0F, 0xAAAAAA);
        drawScaledEndText(entity.getGrowthStage().toString(), 225, 50, 1.0F, 0xB200FF);
        drawScaledEndText("MARINE", 35, 60, 1.0F, 0xFF1900);
        drawScaledEndText("HYBRID", 72, 60, 1.0F, 0xFF1900);
        drawScaledEndText("MAMMAL", 111, 60, 1.0F, 0xFF1900);
        if (dinoDef.isMarineCreature()) {
            drawScaledEndText("MARINE", 35, 60, 1.0F, 0x0080FF);
        }
        if (dinoDef.isHybrid()) {
            drawScaledEndText("HYBRID", 72, 60, 1.0F, 0xFF56FF);
        }
        if (dinoDef.isMammal()) {
            drawScaledEndText("MAMMAL", 111, 60, 1.0F, 0x894900);
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    /**
     * Draws a textured rectangle at the stored z-value. Args: x, y, u, v, width, height
     */
    public void drawScaledTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height, int textureWidth, int textureHeight, float scale) {
        GL11.glPushMatrix();

        ScaledResolution dimensions = new ScaledResolution(mc);
        x += dimensions.getScaledWidth() / 2 - 115;
        y += 65;

        x /= scale;
        y /= scale;

        GL11.glScalef(scale, scale, scale);

        float f = 1.0F / (float) textureWidth;
        float f1 = 1.0F / (float) textureHeight;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder vertexBuffer = tessellator.getBuffer();
        vertexBuffer.begin(7, DefaultVertexFormats.POSITION);
        vertexBuffer.pos((double) (x), (double) (y + height), (double) this.zLevel).tex((double) ((float) (textureX) * f), (double) ((float) (textureY + height) * f1)).endVertex();
        vertexBuffer.pos((double) (x + width), (double) (y + height), (double) this.zLevel).tex((double) ((float) (textureX + width) * f), (double) ((float) (textureY + height) * f1)).endVertex();
        vertexBuffer.pos((double) (x + width), (double) (y), (double) this.zLevel).tex((double) ((float) (textureX + width) * f), (double) ((float) (textureY) * f1)).endVertex();
        vertexBuffer.pos((double) (x), (double) (y), (double) this.zLevel).tex((double) ((float) (textureX) * f), (double) ((float) (textureY) * f1)).endVertex();
        tessellator.draw();

        GL11.glColor3f(1.0F, 1.0F, 1.0F);

        GL11.glPopMatrix();
    }

    /**
     * Draws a textured rectangle at the stored z-value. Args: x, y, u, v, width, height
     */
    public void drawScaledRect(int x, int y, int width, int height, float scale, int colour) {
        GL11.glPushMatrix();

        ScaledResolution dimensions = new ScaledResolution(mc);
        x += dimensions.getScaledWidth() / 2 - 115;
        y += 65;

        x /= scale;
        y /= scale;

        GL11.glScalef(scale, scale, scale);


        float red = (float) (colour >> 16 & 255) / 255.0F;
        float green = (float) (colour >> 8 & 255) / 255.0F;
        float blue = (float) (colour & 255) / 255.0F;

        GL11.glColor3f(red, green, blue);

        float f = 1.0F / (float) width;
        float f1 = 1.0F / (float) height;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder vertexBuffer = tessellator.getBuffer();
        vertexBuffer.begin(7, DefaultVertexFormats.POSITION);
        vertexBuffer.pos((double) (x), (double) (y + height), (double) this.zLevel).tex((double) (0), (double) ((float) (height) * f1)).endVertex();
        vertexBuffer.pos((double) (x + width), (double) (y + height), (double) this.zLevel).tex((double) ((float) (width) * f), (double) ((float) (height) * f1)).endVertex();
        vertexBuffer.pos((double) (x + width), (double) (y), (double) this.zLevel).tex((double) ((float) (width) * f), (double) ((float) 0)).endVertex();
        vertexBuffer.pos((double) (x), (double) (y), (double) this.zLevel).tex((double) ((float) 0), (double) ((float) 0)).endVertex();
        tessellator.draw();

        GL11.glColor3f(1.0F, 1.0F, 1.0F);

        GL11.glPopMatrix();
    }

    public void drawBoxOutline(int x, int y, int sizeX, int sizeY, int borderSize, float scale, int colour) {
        GL11.glPushMatrix();

        drawScaledRect(x, y, sizeX, borderSize, scale, colour);
        drawScaledRect(x + sizeX, y, borderSize, sizeY + borderSize, scale, colour);
        drawScaledRect(x, y, borderSize, sizeY + borderSize, scale, colour);
        drawScaledRect(x, y + sizeY, sizeX, borderSize, scale, colour);

        GL11.glPopMatrix();
    }

    public void drawCenteredScaledText(String text, int x, int y, float scale, int colour) {
        GL11.glPushMatrix();

        ScaledResolution dimensions = new ScaledResolution(mc);
        x += dimensions.getScaledWidth() / 2 - 115;
        y += 65;

        GL11.glScalef(scale, scale, scale);

        x /= scale;
        y /= scale;

        drawCenteredString(fontRenderer, text, x, y, colour);

        GL11.glPopMatrix();
    }

    public void drawScaledText(String text, int x, int y, float scale, int colour) {
        GL11.glPushMatrix();

        ScaledResolution dimensions = new ScaledResolution(mc);
        x += dimensions.getScaledWidth() / 2 - 115;
        y += 65;

        GL11.glScalef(scale, scale, scale);

        x /= scale;
        y /= scale;

        drawString(fontRenderer, text, x, y, colour);

        GL11.glPopMatrix();
    }

    public void drawScaledEndText(String text, int x, int y, float scale, int colour) {
        GL11.glPushMatrix();

        ScaledResolution dimensions = new ScaledResolution(mc);
        x += dimensions.getScaledWidth() / 2 - 115;
        y += 65;

        GL11.glScalef(scale, scale, scale);

        x /= scale;
        y /= scale;

        drawString(fontRenderer, text, x - (fontRenderer.getStringWidth(text)), y, colour);

        GL11.glPopMatrix();
    }

    @Override
    protected void actionPerformed(GuiButton button) {

    }


    public void drawEntityOnScreen(int posX, int posY, int scale, EntityLivingBase ent)
    {
        ScaledResolution dimensions = new ScaledResolution(mc);
        posX += dimensions.getScaledWidth() / 2 - 115;
        posY += 65;

        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) posX, (float) posY, 50.0F);
        GlStateManager.scale((float) (-scale), (float) scale, (float) scale);
        GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
        float f = ent.renderYawOffset;
        float f1 = ent.rotationYaw;
        float f2 = ent.rotationPitch;
        float f3 = ent.prevRotationYawHead;
        float f4 = ent.rotationYawHead;
        GlStateManager.rotate(135.0F, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
        int rot = ent.ticksExisted % 360;
        ent.renderYawOffset = rot;
        ent.rotationYaw = rot;
        ent.rotationPitch = 0;
        ent.rotationYawHead = ent.rotationYaw;
        ent.prevRotationYawHead = ent.rotationYaw;
        GlStateManager.translate(-0.0F, 0.0F, 0.0F);
        RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        rendermanager.setPlayerViewY(300.0F);
        rendermanager.setRenderShadow(false);
        rendermanager.renderEntity(ent, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
        rendermanager.setRenderShadow(true);
        ent.renderYawOffset = f;
        ent.rotationYaw = f1;
        ent.rotationPitch = f2;
        ent.prevRotationYawHead = f3;
        ent.rotationYawHead = f4;
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
}
