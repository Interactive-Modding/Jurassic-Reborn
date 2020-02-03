package mod.reborn.client.gui.app;

import mod.reborn.RebornMod;
import mod.reborn.client.gui.PaleoPadGui;
import mod.reborn.server.paleopad.App;
import mod.reborn.server.paleopad.FlappyDinoApp;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import scala.util.Random;

import java.util.HashMap;
import java.util.Map;

public class FlappyDinoGuiApp extends GuiApp
{
    private static final ResourceLocation texture = new ResourceLocation(RebornMod.MODID, "textures/gui/paleo_pad/apps/flappy_dino.png");
    private static final ResourceLocation logo = new ResourceLocation(RebornMod.MODID, "textures/gui/paleo_pad/apps/background/flappy_dino.png");
    private static final ResourceLocation pteranodon = new ResourceLocation(RebornMod.MODID, "textures/gui/paleo_pad/apps/background/flappy_dino_pteranodon.png");
    private static final ResourceLocation character = new ResourceLocation(RebornMod.MODID, "textures/gui/paleo_pad/apps/background/pteranodon_char.png");
    private static final ResourceLocation pillar_bottom = new ResourceLocation(RebornMod.MODID, "textures/gui/paleo_pad/apps/background/pillar_bottom.png");
    private static final ResourceLocation pillar_top = new ResourceLocation(RebornMod.MODID, "textures/gui/paleo_pad/apps/background/pillar_top.png");
    private static final ResourceLocation background = new ResourceLocation(RebornMod.MODID, "textures/gui/paleo_pad/apps/background/flappy_dino_background.png");

    private boolean mainScreen;

    private int x;
    private int y;
    private int motionY;

    private Map<Integer, Integer> pillars = new HashMap<Integer, Integer>();

    public FlappyDinoGuiApp(App app)
    {
        super(app);
    }

    @Override
    public void render(int mouseX, int mouseY, PaleoPadGui gui)
    {
        super.renderButtons(mouseX, mouseY, gui);

        FlappyDinoApp app = (FlappyDinoApp) getApp();

        mc.getTextureManager().bindTexture(background);
        gui.drawScaledTexturedModalRect(0, 0, 0, 0, 229, 150, 229, 150, 1.0F);

        if (mainScreen)
        {
            mc.getTextureManager().bindTexture(logo);
            gui.drawScaledTexturedModalRect(5, 5, 0, 0, 128, 64, 128, 64, 1.0F);

            mc.getTextureManager().bindTexture(pteranodon);
            gui.drawScaledTexturedModalRect(145, 15, 0, 0, 128, 64, 128, 64, 1.0F);

            gui.drawBoxOutline(90, 100, 50, 20, 1, 1.0F, 0x545454);

            gui.drawScaledRect(91, 101, 49, 19, 1.0F, 0x747474);

            gui.drawScaledText("Play", 105, 107, 1.0F, 0xFFFFFF);
        }
        else
        {
            mc.getTextureManager().bindTexture(character);
            gui.drawScaledTexturedModalRect(5, 150 - y, 0, 0, 32, 32, 32, 32, 1.0F);

            mc.getTextureManager().bindTexture(pillar_bottom);

            for (Map.Entry<Integer, Integer> entry : pillars.entrySet())
            {
                int drawX = entry.getKey() - this.x;

                if (drawX > 0 && drawX < 200)
                {
                    for (int height = 0; height < entry.getValue(); height++)
                    {
                        gui.drawScaledTexturedModalRect(drawX, 130 - (height * 20), 0, 12, 32, 20, 32, 32, 1.0F);
                    }

                    gui.drawScaledTexturedModalRect(drawX, 139 - (entry.getValue() * 20), 0, 0, 32, 12, 32, 32, 1.0F);
                }
            }

            mc.getTextureManager().bindTexture(pillar_top);

            for (Map.Entry<Integer, Integer> entry : pillars.entrySet())
            {
                int drawX = entry.getKey() - this.x;

                if (drawX > 0 && drawX < 200)
                {
                    Integer totalHeight = 4 - entry.getValue();

                    for (int height = 0; height < totalHeight; height++)
                    {
                        gui.drawScaledTexturedModalRect(drawX, (height * 20), 0, 0, 32, 20, 32, 32, 1.0F);
                    }

                    gui.drawScaledTexturedModalRect(drawX, (totalHeight * 20), 0, 20, 32, 12, 32, 32, 1.0F);

                    if (mc.getRenderManager().isDebugBoundingBox())
                    {
                        int topHeight = ((4 - entry.getValue()) * 20) + 11;
                        int bottomHeight = (150 - (entry.getValue() * 20)) - 11;

                        int actualY = 150 - y;

                        gui.drawScaledRect(drawX + 1, topHeight, 30, 1, 1.0F, 0xFFFFFF);
                        gui.drawScaledRect(drawX + 1, bottomHeight, 30, 1, 1.0F, 0xFFFF00);

                        gui.drawScaledRect(6, actualY + 5, 30, 1, 1.0F, 0xFF0000);
                        gui.drawScaledRect(6, actualY + 23, 30, 1, 1.0F, 0xFF00FF);
                    }
                }
            }
        }
    }

    @Override
    public void actionPerformed(GuiButton button)
    {

    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, PaleoPadGui gui)
    {
        ScaledResolution dimensions = new ScaledResolution(mc);
        mouseX -= dimensions.getScaledWidth() / 2 - 115;
        mouseY -= 65;

        if (mainScreen)
        {
            if (mouseX > 90 && mouseX < 140 && mouseY > 100 && mouseY < 120)
            {
                mainScreen = false;
            }
        }
        else
        {
            motionY = 6;
        }
    }

    @Override
    public void init()
    {
        x = 0;
        y = 150;
        motionY = 0;

        pillars.clear();

        Random rand = new Random();

        for (int i = 0; i < 100; i++)
        {
            pillars.put((i * 70) + 70, rand.nextInt(5));
        }

        mainScreen = true;
    }

    @Override
    public void update()
    {
        if (!mainScreen && mc.player.ticksExisted % 2 == 0)
        {
            x++;

            if (y > 140)
            {
                y = 140;
            }

            if (motionY < -5)
            {
                motionY = -5;
            }

            y += motionY;

            motionY--;

            boolean died = false;

            for (Map.Entry<Integer, Integer> entry : pillars.entrySet())
            {
                int renderX = entry.getKey() - x;

                int height = entry.getValue();
                int bottomHeight = (150 - (entry.getValue() * 20)) - 11;
                int topHeight = ((4 - height) * 20) + 11;

                int actualY = 150 - y;

                if (renderX > 0 && renderX < 200)
                {
                    boolean collideX = (renderX) < 30;
                    boolean collideY = (actualY + 5) < topHeight || (actualY + 23) > bottomHeight;

                    if (collideX && collideY)
                    {
                        died = true;
                        break;
                    }
                }
            }

            if (y < 20)
            {
                died = true;
            }

            if (died)
            {
                init();
            }
        }
    }

    @Override
    public ResourceLocation getTexture(PaleoPadGui gui)
    {
        return texture;
    }
}