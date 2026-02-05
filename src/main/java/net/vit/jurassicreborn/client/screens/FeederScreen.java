package net.vit.jurassicreborn.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.vit.jurassicreborn.common.blocks.entities.feeder.FeederMenu;

import static net.vit.jurassicreborn.JurassicReborn.resource;

/**
 * Simple 18-slot GUI for the Feeder.
 * Width/height are the vanilla container size (176×166).  No progress bars yet
 */
public class FeederScreen extends AbstractContainerScreen<FeederMenu> {

    private static final ResourceLocation TEXTURE =
            resource("textures/gui/feeder.png");   // 176 × 166

    /* ── ctors ─────────────────────────────────────────────────────────── */
    public FeederScreen(FeederMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
        this.imageWidth  = 176;   // explicit so we know what we’re blitting
        this.imageHeight = 166;
    }

    /** bridge ctor to mirror CleanerScreen’s convenience pattern */
    public FeederScreen(FeederMenu menu, Inventory inv, Component title,
                        BlockEntity be) {
        this(menu, inv, title);   // nothing special to store yet
    }

    /* ── draw loop ─────────────────────────────────────────────────────── */
    @Override
    public void render(PoseStack pose, int mouseX, int mouseY, float partial) {
        super.render(pose, mouseX, mouseY, partial);
        this.renderTooltip(pose, mouseX, mouseY);
    }

    @Override
    protected void renderBg(PoseStack pose, float partial,
                            int mouseX, int mouseY) {

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, TEXTURE);
        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
        this.blit(pose, this.leftPos, this.topPos,
                0, 0, this.imageWidth, this.imageHeight);

    }
}
