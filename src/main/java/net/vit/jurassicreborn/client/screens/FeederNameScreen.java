package net.vit.jurassicreborn.client.screens;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.vit.jurassicreborn.client.JurassicClient;
import net.vit.jurassicreborn.common.network.NameFeederPacket;
import net.vit.jurassicreborn.common.network.Network;
import net.vit.jurassicreborn.common.paleopad.AppHandler;

public class FeederNameScreen extends Screen {
    private final BlockPos pos;
    private EditBox nameField;

    public FeederNameScreen(BlockPos pos) {
        super(new TextComponent("Name Feeder"));
        this.pos = pos;
    }

    @Override
    protected void init() {
        this.nameField = new EditBox(this.font, this.width / 2 - 100, this.height / 2 - 10, 200, 20, new TextComponent("Feeder Name"));
        this.addRenderableWidget(this.nameField);

        this.addRenderableWidget(new Button(
                this.width / 2 - 40, // x position
                this.height / 2 + 20, // y position
                80, // width
                20, // height
                new TextComponent("Done"), // button text
                b -> finish() // onPress action
        ));

        this.setInitialFocus(this.nameField);
    }

    private void finish() {
        String name = nameField.getValue().trim();
        if (!name.isEmpty()) {
            Network.sendToServer(new NameFeederPacket(pos, name));
        }
        JurassicClient.openPaleoPad(AppHandler.INSTANCE.feederTracker);
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, partialTicks);
        this.nameField.render(poseStack, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}