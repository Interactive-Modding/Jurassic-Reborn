package net.vit.jurassicreborn.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.vit.jurassicreborn.common.blocks.entities.cleaner.CleanerBlockEntity;
import net.vit.jurassicreborn.common.blocks.entities.cleaner.CleanerMenu;
import net.vit.jurassicreborn.common.blocks.entities.grinder.FossilGrinderMenu;
import org.jetbrains.annotations.NotNull;

import static net.vit.jurassicreborn.JurassicReborn.resource;

public class CleanerScreen extends AbstractContainerScreen<CleanerMenu> implements MenuAccess<CleanerMenu> {

    private static final ResourceLocation TEXTURE = resource("textures/gui/cleaning_station.png");

    private static final int progressBarXOffset = 176;
    private static final int progressBarYOffset = 14;

    private static final int fluidBarXOffset = 177;
    private static final int fluidBarYOffset = 32; // move this DOWN (your note)
    private static final int fluidBarWidth = 4;
    private static final int fluidBarHeight = 50;

    public CleanerScreen(CleanerMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
//        this.playerInventory = pPlayerInventory;

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

        // Your original GUI size values
        guiGraphics.blit(TEXTURE, i, j, 0, 0, 175, 165);

        // render progress bar (your logic)
        if (this.menu.isCleaning()) {
            int progress = menu.getProgress();
            guiGraphics.blit(
                    TEXTURE,
                    i + 79, j + 34,
                    progressBarXOffset, progressBarYOffset,
                    progress + 1, 16 // "or 17 if it doesn't work" (your note)
            );
        }

        // fluid bar background (your logic)
        int fluidX = i + 47;
        int fluidY = j + 19;
        guiGraphics.fill(fluidX, fluidY, fluidX + fluidBarWidth, fluidY + fluidBarHeight, 0xFF686868);

        // fluid amount (your logic)
        int fluidHeight = Math.min(fluidBarHeight, menu.getAmountOfFluid() / 20);

        if (fluidHeight > 0) {
            guiGraphics.blit(
                    TEXTURE,
                    fluidX,
                    fluidY + (fluidBarHeight - fluidHeight),
                    fluidBarXOffset,
                    fluidBarYOffset + (fluidBarHeight - fluidHeight),
                    fluidBarWidth,
                    fluidHeight
            );
        }
    }
}
