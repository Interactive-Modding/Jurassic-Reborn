package net.vit.jurassicreborn.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.common.blocks.entities.Embryoncis.EmbryoCalcificationMachine.EmbryoCalcificationMachineBlockEntity;
import net.vit.jurassicreborn.common.blocks.entities.Embryoncis.EmbryoCalcificationMachine.EmbryoCalcificationMachineMenu;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class EmbryoCalcificationMachineScreen extends AbstractContainerScreen<EmbryoCalcificationMachineMenu> {
    private static final ResourceLocation TEXTURE = JurassicReborn.resource( "textures/gui/embryo_calcification_machine.png");

    public EmbryoCalcificationMachineScreen(EmbryoCalcificationMachineMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
        this.renderTooltip(pPoseStack, pMouseX, pMouseY);
    }

    @Override
    protected void init() {
        super.init();
        this.titleLabelY-=3;
    }

    @Override
    protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);



        int k = this.leftPos;
        int l = this.topPos;

        this.blit(pPoseStack, k, l, 0, 0, this.imageWidth, this.imageHeight);

        int progress = this.getProgress(24);
        int progress1 = this.getProgress(9);
        int progress2 = this.getProgress(20);

        //background
        this.blit(pPoseStack, k + 67, l + 31, 176, 14, progress + 1, 16);

        // Syringe Top
        this.blit(pPoseStack, k + 38, l + 32, 177, 32, 9, progress1);
        // Syringe Inside
        this.blit(pPoseStack, k + 38, l + 38, 197, 38, 9, progress2);
        // Clean up
        this.blit(pPoseStack, k + 38, l + 32, 187, 32, 9, progress1 - 1);

    }

    private int getProgress(int scale) {
        int progress = this.menu.getField(0);
        int nax = EmbryoCalcificationMachineBlockEntity.STACK_PROCESS_TIME;
        return progress != 0 ? progress * scale / nax : 0;
    }
}
