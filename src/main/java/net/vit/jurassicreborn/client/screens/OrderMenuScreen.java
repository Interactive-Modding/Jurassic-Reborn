package net.vit.jurassicreborn.client.screens;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.vit.jurassicreborn.common.entities.DinosaurEntity;
import net.vit.jurassicreborn.common.network.Network;
import net.vit.jurassicreborn.common.network.SetOrderPacket;

public class OrderMenuScreen extends Screen {
    private final DinosaurEntity entity;

    public OrderMenuScreen(DinosaurEntity entity) {
        super(Component.translatable("gui.select_order"));
        this.entity = entity;
    }

    @Override
    protected void init() {
        int cx = this.width / 2;
        int cy = this.height / 2;
        int y = cy - 10;
        addRenderableWidget(new Button(cx - 50, y - 20, 100, 20,
                Component.translatable("order.wander"),
                b -> sendOrder(DinosaurEntity.Order.WANDER)));
        addRenderableWidget(new Button(cx - 50, y + 10, 100, 20,
                Component.translatable("order.follow"),
                b -> sendOrder(DinosaurEntity.Order.FOLLOW)));
        addRenderableWidget(new Button(cx - 50, y + 40, 100, 20,
                Component.translatable("order.sit"),
                b -> sendOrder(DinosaurEntity.Order.SIT)));
    }

    private void sendOrder(DinosaurEntity.Order order) {
        Minecraft.getInstance().setScreen(null);
        Network.sendToServer(new SetOrderPacket(entity.getId(), order.ordinal()));
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, partialTicks);
    }
}