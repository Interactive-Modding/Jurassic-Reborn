package net.vit.jurassicreborn.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.common.blocks.entities.grinder.FossilGrinderBlockEntity;
import net.vit.jurassicreborn.common.blocks.entities.grinder.FossilGrinderMenu;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class FossilGrinderScreen extends AbstractContainerScreen<FossilGrinderMenu> {
    private static final ResourceLocation TEXTURE = JurassicReborn.resource("textures/gui/fossil_grinder.png");
//    private final Inventory playerInventory;
//    private FossilGrinderMenu inventory;

    public FossilGrinderScreen(FossilGrinderMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
//        this.playerInventory = pPlayerInventory;

    }





//    public FeederScreen(Fe) {
//        super(new FeederContainer(playerInventory, (FeederBlockEntity) inventory));
//        this.playerInventory = playerInventory;
//        this.inventory = inventory;
//    }


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

        this.blit(pPoseStack, this.leftPos, this.topPos, 0, 0, 176, 166);

        int progress = this.getProgress(24);
        this.blit(pPoseStack, this.leftPos + 79, this.topPos + 34, 176, 14, progress + 1, 16);
    }

    private int getProgress(int scale) {
        int j = this.menu.getField(0);
        int k = FossilGrinderBlockEntity.PROCESS_TIME;
        return j != 0 ? j * scale / k : 0;
    }
}
