package net.vit.jurassicreborn.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
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
    protected void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.setShaderTexture(0, TEXTURE);
        graphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        // --- Draw progress arrow (if using one) ---
        int progress = this.menu.getProgress();
        int max = this.menu.getMaxProgress();
        if (max > 0 && progress > 0) {
            int arrowPixels = (int)(24.0F * progress / max);
            // (x, y, u, v, width, height)
            graphics.blit(TEXTURE, this.leftPos + 76, this.topPos + 36, 176, 0, arrowPixels, 17);
        }
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        // Title
        graphics.drawString(this.font, this.title, 8, 6, 0x404040, false);
        // Player Inventory label
        graphics.drawString(this.font, this.playerInventoryTitle, 8, this.imageHeight - 94, 0x404040, false);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(graphics, mouseX, mouseY);
    }
}
