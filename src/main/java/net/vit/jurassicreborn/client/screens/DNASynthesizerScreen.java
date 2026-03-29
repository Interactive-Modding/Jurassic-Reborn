package net.vit.jurassicreborn.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.common.blocks.entities.DNABlocks.DNASynthesizer.DNASynthesizerMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;

public class DNASynthesizerScreen extends AbstractContainerScreen<DNASynthesizerMenu> {

    public static final ResourceLocation TEXTURE = JurassicReborn.resource("textures/gui/dna_synthesizer.png");
    public DNASynthesizerScreen(DNASynthesizerMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics,mouseX, mouseY, partialTick);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);


        int x = this.leftPos;
        int y = this.topPos;

        guiGraphics.blit(TEXTURE, x, y, 0, 0, this.getXSize(), this.getYSize());

        int progress = Mth.ceil(this.menu.getField(0) * 0.011);

        guiGraphics.blit(TEXTURE, x + 79, y + 34, 176, 14, progress, 16);
    }
}
