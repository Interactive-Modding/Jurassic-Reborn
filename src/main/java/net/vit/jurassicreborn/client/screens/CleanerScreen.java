package net.vit.jurassicreborn.client.screens;


import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
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
        this(menu, inv, title);

        if(entity instanceof CleanerBlockEntity e)
            menu.setInstance(e);

    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        int i = this.leftPos;
        int j = this.topPos;
        guiGraphics.blit(TEXTURE, i, j, 0, 0, 175, 165);

//        boolean isInstanceNull = menu.isInstanceNull();

        //render progress bar
        if(this.menu.isCleaning()){
            int progress = menu.getProgress();
            guiGraphics.blit(TEXTURE, i + 79, j + 34, progressBarXOffset, progressBarYOffset, progress + 1, 16/*or 17 if it doesn't work thats why*/);
        }

        int fluidX = i + 47;
        int fluidY = j + 19;
        guiGraphics.fill(fluidX, fluidY, fluidX + fluidBarWidth, fluidY + fluidBarHeight, 0xFF686868);

        int fluidHeight = Math.min(fluidBarHeight, menu.getAmountOfFluid() / 20);
        //render fluid amount
        if(fluidHeight > 0){
            guiGraphics.blit(TEXTURE, fluidX, fluidY + fluidBarHeight - fluidHeight, fluidBarXOffset, fluidBarYOffset + ( fluidBarHeight - fluidHeight ), fluidBarWidth, fluidHeight);
        }

    }
}