package net.vit.jurassicreborn.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.vit.jurassicreborn.common.blocks.entities.skeletonassembly.SkeletonAssemblerMenu;
import net.minecraft.client.gui.GuiGraphics;

import static net.vit.jurassicreborn.JurassicReborn.resource;

/** 5×5 Fossil grid + 1 result slot – no progress bars needed. */
public class SkeletonAssemblerScreen
        extends AbstractContainerScreen<SkeletonAssemblerMenu> {

    private static final ResourceLocation GUI =
            resource("textures/gui/skeleton_assembler.png");

    /* texture is 176×201 pixels (same as 1.12) */
    public SkeletonAssemblerScreen(SkeletonAssemblerMenu menu,
                                   Inventory inv,
                                   Component title) {
        super(menu, inv, title);
        this.imageWidth  = 176;
        this.imageHeight = 201;
    }

    /* ------------------------------------------------------------------ */
    /*  MAIN RENDER LOOP                                                  */
    /* ------------------------------------------------------------------ */

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partial) {
        this.renderBackground(guiGraphics);              // dark vignette
        super.render(guiGraphics, mouseX, mouseY, partial);
        this.renderTooltip(guiGraphics, mouseX, mouseY); // item tooltips
    }

    /* ------------------------------------------------------------------ */
    /*  BACKGROUND LAYER                                                  */
    /* ------------------------------------------------------------------ */

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partial, int mx, int my) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
        RenderSystem.setShaderTexture(0, GUI);

        int x = this.leftPos;
        int y = this.topPos;
        guiGraphics.blit(GUI, x, y, 0, 0, this.imageWidth, this.imageHeight);
    }

    /* ------------------------------------------------------------------ */
    /*  FOREGROUND TITLE                                                  */
    /* ------------------------------------------------------------------ */

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        Component title = Component.translatable("container.skeletonassembly");
        int titleX = (this.imageWidth - this.font.width(title)) / 2;
        guiGraphics.drawString(this.font, title, titleX, 4, 0x404040, false);
    }
}
