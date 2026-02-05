package net.vit.jurassicreborn.client.screens.paleopad;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;
import net.vit.jurassicreborn.common.paleopad.App;
import net.vit.jurassicreborn.common.util.networking.PlayerData;

import java.util.ArrayList;
import java.util.List;

public abstract class GuiApp {
    protected App app;
    protected static final Minecraft mc = Minecraft.getInstance();

    public List<Button> buttons = new ArrayList<>();

    private boolean requestShutdown;

    public GuiApp(App app) {
        this.app = app;
    }

    public void requestShutdown() {
        this.requestShutdown = true;
        if (mc.player != null) {
            PlayerData.get(mc.player).closeApp(app);
        }
    }

    public boolean doesRequestShutdown() {
        return requestShutdown;
    }

    /**
     * Render the App. Implementations should draw to the screen and call renderButtons if needed.
     */
    public abstract void render(PoseStack poseStack, int mouseX, int mouseY, Screen screen, float partialTicks);

    /**
     * Render all buttons for this App (optional helper).
     */
    protected void renderButtons(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        for (Button button : buttons) {
            button.render(poseStack, mouseX, mouseY, partialTicks);
        }
    }

    public void keyPressed(int keyCode, int scanCode, int modifiers) {}

    public abstract void actionPerformed(Button button);

    public abstract void mouseClicked(double mouseX, double mouseY, Screen screen);

    public abstract void init();

    public abstract ResourceLocation getTexture(Screen screen);

    public App getApp() {
        return app;
    }

    public void update() {
        // Optionally override for ticking logic.
    }
}
