package mod.reborn.client.gui;
import mod.reborn.RebornMod;
import mod.reborn.client.gui.app.GuiApp;
import mod.reborn.client.gui.app.GuiAppRegistry;
import mod.reborn.server.datafixers.PlayerData;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import mod.reborn.server.paleopad.*;

import java.util.List;

@SideOnly(Side.CLIENT)
public class PaleoPadGui extends GuiScreen
{
    private static final ResourceLocation texture = new ResourceLocation(RebornMod.MODID, "textures/gui/paleo_pad/paleo_pad.png");

    public GuiApp focus;

    public PaleoPadGui()
    {
    }

    @Override
    public void initGui()
    {
        super.initGui();
    }

    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
    @Override
    public void onGuiClosed()
    {
        if (focus != null)
        {
            PlayerData.get(mc.player).closeApp(focus.getApp());
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state)
    {
        ScaledResolution dimensions = new ScaledResolution(mc);
        int scaledWidth = dimensions.getScaledWidth();
        int scaledHeight = dimensions.getScaledWidth();

        if (focus == null)
        {
            List<App> apps = AppHandler.INSTANCE.getApps();

            for (int i = 0; i < apps.size(); i++)
            {
                int x = ((i % 4) * 55) + scaledWidth / 2 - 110;
                int y = ((int) Math.floor((float) i / 4.0F) * 38) + 70;

                if (mouseX > x && mouseY > y && mouseX < x + 32 && mouseY < y + 32)
                {
                    App app = apps.get(i);

                    focus = GuiAppRegistry.getGui(app);
                    focus.init();
                    PlayerData.get(mc.player).openApp(app);

                    focus.buttons.clear();
                    buttonList.clear();
                    buttonList.addAll(focus.buttons);
                }
            }
        }
        else
        {
            focus.mouseClicked(mouseX, mouseY, this);

            mouseX -= dimensions.getScaledWidth() / 2 - 115;
            mouseY -= 65;

            if (mouseX > 97 && mouseY > 153 && mouseX < 131 && mouseY < 157)
            {
                focus.requestShutdown();
                focus = null;
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        ScaledResolution scaledResolution = new ScaledResolution(this.mc);
        int scaleFactor = scaledResolution.getScaleFactor();
        int scaledWidth = scaledResolution.getScaledWidth();
        int scaledHeight = scaledResolution.getScaledHeight();
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(texture);

        drawTexturedModalRect(scaledWidth / 2 - 128, 40, 0, 0, 256, 256);

        List<App> apps = AppHandler.INSTANCE.getApps();

        double worldTime = mc.world.getWorldTime() + 6000 % 24000;

        double hours = worldTime / 1000;
        double minutes = hours * 60;

        String hoursStr = "" + (int) hours % 24;

        while (hoursStr.length() < 2)
        {
            hoursStr = "0" + hoursStr;
        }

        String minutesStr = "" + (int) minutes % 60;

        while (minutesStr.length() < 2)
        {
            minutesStr = "0" + minutesStr;
        }

        drawCenteredScaledText("0", 115, -10, 1F, 0xFFFFFFF);
        drawScaledRect(0, 0, 458, 2, 0.5F, 0x404040);

        if (focus == null)
        {
            for (int i = 0; i < apps.size(); i++)
            {
                int x = (i % 4) * 50 + 5;
                int y = (int) Math.floor((float) i / 4.0F) * 42;

                App app = apps.get(i);
                GuiApp gui = GuiAppRegistry.getGui(app);

                mc.getTextureManager().bindTexture(gui.getTexture(this));

                drawScaledTexturedModalRect(x + 5, y + 5, 0, 0, 32, 32, 32, 32, 1.0F);

                drawCenteredScaledText(app.getName(), x + 22, y + 39, 0.7F, 0xFFFFFF);
            }

            drawScaledText(I18n.translateToLocal("paleopad.os.name"), 2, -10, 1.0F, 0xFFFFFF);
        }
        else {
            if (focus.getApp().getName() != null) {
                drawScaledText(focus.getApp().getName(), 2, -10, 1.0F, 0xFFFFFF);
                focus.render(mouseX, mouseY, this);
            } else {
                focus = null;
            }
        }
    }

    /**
     * Draws a textured rectangle at the stored z-value. Args: x, y, u, v, width, height
     */
    public void drawScaledTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height, int textureWidth, int textureHeight, float scale)
    {
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
        vertexBuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
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
    public void drawScaledRect(int x, int y, int width, int height, float scale, int colour)
    {
        GL11.glPushMatrix();

        ScaledResolution dimensions = new ScaledResolution(mc);
        x += dimensions.getScaledWidth() / 2 - 115;
        y += 65;

        x /= scale;
        y /= scale;

        GL11.glScalef(scale, scale, scale);

        GL11.glDisable(GL11.GL_TEXTURE_2D);

        float red = (float) (colour >> 16 & 255) / 255.0F;
        float green = (float) (colour >> 8 & 255) / 255.0F;
        float blue = (float) (colour & 255) / 255.0F;

        GL11.glColor3f(red, green, blue);

        float f = 1.0F / (float) width;
        float f1 = 1.0F / (float) height;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
        buffer.pos((double) (x), (double) (y + height), (double) this.zLevel).tex((double) (0), (double) ((float) (height) * f1)).endVertex();
        buffer.pos((double) (x + width), (double) (y + height), (double) this.zLevel).tex((double) ((float) (width) * f), (double) ((float) (height) * f1)).endVertex();
        buffer.pos((double) (x + width), (double) (y), (double) this.zLevel).tex((double) ((float) (width) * f), (double) ((float) 0)).endVertex();
        buffer.pos((double) (x), (double) (y), (double) this.zLevel).tex((double) ((float) 0), (double) ((float) 0)).endVertex();
        tessellator.draw();

        GL11.glColor3f(1.0F, 1.0F, 1.0F);

        GL11.glEnable(GL11.GL_TEXTURE_2D);

        GL11.glPopMatrix();
    }

    public void drawBoxOutline(int x, int y, int sizeX, int sizeY, int borderSize, float scale, int colour)
    {
        GL11.glPushMatrix();

        drawScaledRect(x, y, sizeX, borderSize, scale, colour);
        drawScaledRect(x + sizeX, y, borderSize, sizeY + borderSize, scale, colour);
        drawScaledRect(x, y, borderSize, sizeY + borderSize, scale, colour);
        drawScaledRect(x, y + sizeY, sizeX, borderSize, scale, colour);

        GL11.glPopMatrix();
    }

    public void drawCenteredScaledText(String text, int x, int y, float scale, int colour)
    {
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

    public void drawScaledText(String text, int x, int y, float scale, int colour)
    {
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

    @Override
    protected void actionPerformed(GuiButton button)
    {

    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }
}