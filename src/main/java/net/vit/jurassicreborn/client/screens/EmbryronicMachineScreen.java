package net.vit.jurassicreborn.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.common.blocks.entities.Embryoncis.EmbryonicMachine.EmbryonicMachineMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.GuiGraphics;
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
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics,mouseX, mouseY, partialTick);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void init() {
        super.init();
        this.titleLabelY-=3;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);


        int x = this.leftPos;
        int y = this.topPos;

        //render background
        guiGraphics.blit(TEXTURE, x, y, 0, 0, this.getXSize(), this.getYSize());

        int progress = Mth.ceil(this.menu.getField(0) * 0.12);

        //render progress bar, because its
        if(progress > 0) {

            if(progress >= 2)
                guiGraphics.blit(TEXTURE, x + 79, y + 34, 176, 14, progress, 16);

            guiGraphics.blit(TEXTURE, x + 79, y + 34, 176, 14, progress == 1 ? 1 : 2, 16);

        }
    }
}
