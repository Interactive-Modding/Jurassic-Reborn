package mod.reborn.client.gui.app;

import com.google.common.collect.Lists;
import mod.reborn.client.gui.PaleoPadGui;
import mod.reborn.server.datafixers.PlayerData;
import mod.reborn.server.paleopad.App;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import mod.reborn.server.paleopad.*;

import java.util.List;

@SideOnly(Side.CLIENT)
public abstract class GuiApp
{
    protected App app;

    protected static final Minecraft mc = Minecraft.getMinecraft();

    public GuiApp(App app)
    {
        this.app = app;
    }

    public List<GuiButton> buttons = Lists.newArrayList();

    private boolean requestShutdown;

    public void requestShutdown()
    {
        this.requestShutdown = true;

        PlayerData.get(mc.player).closeApp(app);
    }

    public boolean doesRequestShutdown()
    {
        return requestShutdown;
    }

    public abstract void render(int mouseX, int mouseY, PaleoPadGui gui);

    protected void renderButtons(int mouseX, int mouseY, PaleoPadGui gui)
    {
        for (GuiButton button : buttons)
        {
            button.drawButton(Minecraft.getMinecraft(), mouseX, mouseY, 0);
        }
    }

    public void keyPressed(int key)
    {
    }


    public abstract void actionPerformed(GuiButton button);

    public abstract void mouseClicked(int mouseX, int mouseY, PaleoPadGui gui);

    public abstract void init();

    public abstract ResourceLocation getTexture(PaleoPadGui gui);

    public App getApp()
    {
        return app;
    }

    public void update()
    {
    }
}
