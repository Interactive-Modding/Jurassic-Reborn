package net.vit.jurassicreborn.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.vit.jurassicreborn.common.blocks.entities.bugcrate.BugCrateMenu;

public class BugCrateScreen extends AbstractContainerScreen<BugCrateMenu> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("jurassicreborn", "textures/gui/bug_crate.png");

    public BugCrateScreen(BugCrateMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    protected void renderBg(PoseStack poseStack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.setShaderTexture(0, TEXTURE);
        this.blit(poseStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        // --- Draw progress arrow (if using one) ---
        int progress = this.menu.getProgress();
        int max = this.menu.getMaxProgress();
        if (max > 0 && progress > 0) {
            int arrowPixels = (int)(24.0F * progress / max);
            // (x, y, u, v, width, height)
            this.blit(poseStack, this.leftPos + 76, this.topPos + 36, 176, 0, arrowPixels, 17);
        }
    }

    @Override
    protected void renderLabels(PoseStack poseStack, int mouseX, int mouseY) {
        // Title
        this.font.draw(poseStack, this.title, 8, 6, 0x404040);
        // Player Inventory label
        this.font.draw(poseStack, this.playerInventoryTitle, 8, this.imageHeight - 94, 0x404040);
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(poseStack, mouseX, mouseY);
    }
}
