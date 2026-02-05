package net.vit.jurassicreborn.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.vit.jurassicreborn.common.blocks.entities.skeletonassembly.SkeletonAssemblerMenu;

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
    public void render(PoseStack pose, int mouseX, int mouseY, float partial) {
        this.renderBackground(pose);              // dark vignette
        super.render(pose, mouseX, mouseY, partial);
        this.renderTooltip(pose, mouseX, mouseY); // item tooltips
    }

    /* ------------------------------------------------------------------ */
    /*  BACKGROUND LAYER                                                  */
    /* ------------------------------------------------------------------ */

    @Override
    protected void renderBg(PoseStack pose, float partial, int mx, int my) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
        RenderSystem.setShaderTexture(0, GUI);

        int x = this.leftPos;
        int y = this.topPos;
        this.blit(pose, x, y, 0, 0, this.imageWidth, this.imageHeight);
    }

    /* ------------------------------------------------------------------ */
    /*  FOREGROUND TITLE                                                  */
    /* ------------------------------------------------------------------ */

    @Override
    protected void renderLabels(PoseStack pose, int mouseX, int mouseY) {
        Component title = Component.translatable("container.skeletonassembly");
        int titleX = (this.imageWidth - this.font.width(title)) / 2;
        this.font.draw(pose, title, titleX, 4, 0x404040);
    }
}
