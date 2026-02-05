package net.vit.jurassicreborn.client.screens;


import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.vit.jurassicreborn.common.blocks.entities.cleaner.CleanerBlockEntity;
import net.vit.jurassicreborn.common.blocks.entities.cleaner.CleanerMenu;
import org.jetbrains.annotations.NotNull;

import static net.vit.jurassicreborn.JurassicReborn.resource;

public class CleanerScreen extends AbstractContainerScreen<CleanerMenu> {

    private static final ResourceLocation TEXTURE = resource("textures/gui/cleaning_station.png");
    private static final int progressBarXOffset = 176;
    private static final int progressBarYOffset = 14;

    private static final int fluidBarXOffset = 177;
    private static final int fluidBarYOffset = 32;//move this DOWN
    private static final int fluidBarWidth = 4;
    private static final int fluidBarHeight = 50;




    public CleanerScreen(CleanerMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);

//        BlockEntity tempOwner = pPlayerInventory.player.getCommandSenderWorld().getBlockEntity( pPlayerInventory.player.getCommandSenderWorld().clip(new ClipContext( pPlayerInventory.player.getLookAngle(), pPlayerInventory.player.getLookAngle().add(5, 5, 5), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, pPlayerInventory.player)).getBlockPos());
//        pMenu.setInstance(tempOwner instanceof CleanerBlockEntity ? (CleanerBlockEntity) tempOwner : null);
    }

    public CleanerScreen(CleanerMenu menu, Inventory inv, Component title, BlockEntity entity){
        super(menu, inv, title);
        this.itemRenderer = Minecraft.getInstance().getItemRenderer();
        this.minecraft = Minecraft.getInstance();
        this.itemRenderer = minecraft.getItemRenderer();
        this.font = minecraft.font;

        if(entity instanceof CleanerBlockEntity e)
            menu.setInstance(e);

    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
        this.renderTooltip(pPoseStack, pMouseX, pMouseY);
    }

    @Override
    protected void renderBg(@NotNull PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int i = this.leftPos;
        int j = this.topPos;
        this.blit(pPoseStack, i, j, 0, 0, 175, 165);

//        boolean isInstanceNull = menu.isInstanceNull();

        //render progress bar
        if(this.menu.isCleaning()){
            int progress = menu.getProgress();
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.setShaderTexture(0, TEXTURE);
            this.blit(pPoseStack, i+79, j+34, progressBarXOffset, progressBarYOffset,   progress + 1, 16/*or 17 if it doesn't work thats why*/);
        }

        int fluidX = i + 47;
        int fluidY = j + 19;
        GuiComponent.fill(pPoseStack, fluidX, fluidY, fluidX + fluidBarWidth, fluidY + fluidBarHeight, 0xFF686868);

        int fluidHeight = Math.min(fluidBarHeight, menu.getAmountOfFluid() / 20);
        //render fluid amount
        if(fluidHeight > 0){
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.setShaderTexture(0, TEXTURE);
            this.blit(pPoseStack, fluidX, fluidY + fluidBarHeight - fluidHeight, fluidBarXOffset, fluidBarYOffset + ( fluidBarHeight - fluidHeight ), fluidBarWidth, fluidHeight);
        }

    }
}