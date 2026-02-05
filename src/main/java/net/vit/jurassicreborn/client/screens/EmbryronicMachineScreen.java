package net.vit.jurassicreborn.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.common.blocks.entities.Embryoncis.EmbryonicMachine.EmbryonicMachineMenu;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;

public class EmbryronicMachineScreen extends AbstractContainerScreen<EmbryonicMachineMenu> {
    private static final ResourceLocation TEXTURE = JurassicReborn.resource("textures/gui/embryonic_machine.png");



    public EmbryronicMachineScreen(EmbryonicMachineMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
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


        int x = this.leftPos;
        int y = this.topPos;

        //render background
        this.blit(pPoseStack, x, y, 0, 0, this.getXSize(), this.getYSize());

        int progress = Mth.ceil(this.menu.getField(0) * 0.12);

        //render progress bar, because its
        if(progress > 0) {

            if(progress >= 2)
                this.blit(pPoseStack, x + 79, y + 34, 176, 14, progress, 16);

            this.blit(pPoseStack, x + 79, y + 34, 176, 14, progress == 1 ? 1 : 2, 16);

        }
    }
}
