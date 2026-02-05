package net.vit.jurassicreborn.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.common.blocks.entities.DNABlocks.DNASequencer.DNASequencerMenu;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;

public class DNASequencerScreen extends AbstractContainerScreen<DNASequencerMenu> {
    private static final ResourceLocation TEXTURE = JurassicReborn.resource("textures/gui/dna_sequencer.png");



    public DNASequencerScreen(DNASequencerMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
        this.renderTooltip(pPoseStack, pMouseX, pMouseY);
    }

    @Override
    protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);

        int x = this.leftPos;
        int y = this.topPos;
        this.blit(pPoseStack, x, y, 0, 0, 176, 166);

        for (int i = 0; i < 3; i++) {

            renderProgressBar(i, x, y, pPoseStack);
        }
    }

    protected void renderProgressBar(int index, int x, int y, PoseStack poseStack){
        this.blit(poseStack, x + 87, y + 21 + index * 20, 176, 0, Mth.ceil(this.menu.getField(index) * 0.011), 6);
    }
}
