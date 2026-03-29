package net.vit.jurassicreborn.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.common.blocks.entities.incubator.IncubatorBlockEntity;
import net.vit.jurassicreborn.common.blocks.entities.incubator.IncubatorMenu;
import net.vit.jurassicreborn.common.network.Network;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class IncubatorScreen extends AbstractContainerScreen<IncubatorMenu> {
    private static final ResourceLocation TEXTURE = JurassicReborn.resource( "textures/gui/incubator.png");


    public IncubatorScreen(IncubatorMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }


    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        boolean flag = super.mouseClicked(pMouseX, pMouseY, pButton);
        this.dragTemperatureSlider(pMouseX, pMouseY);
        return flag;
    }

    @Override
    protected void init() {
        super.init();
        this.titleLabelY -= 3;
    }

    @Override
    public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {
        boolean flag = super.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);
        this.dragTemperatureSlider(pMouseX, pMouseY);
        return flag;
    }

    private void dragTemperatureSlider(double mouseX, double mouseY) {
        int k = this.leftPos;
        int l = this.topPos;

        for (int i = 0; i < 5; i++) {
            int x = 0;
            int y = 0;

            switch (i) {
                case 0:
                    x = 33;
                    y = 28;
                    break;
                case 1:
                    x = 56;
                    y = 21;
                    break;
                case 2:
                    x = 79;
                    y = 14;
                    break;
                case 3:
                    x = 102;
                    y = 21;
                    break;
                case 4:
                    x = 125;
                    y = 28;
                    break;
            }

            x += k - 2;
            y += 18 + l;

            if (mouseX > x && mouseY > y && mouseX < x + 21 && mouseY < y + 5) {
                int temp = (((int) mouseX) - x + 1) * 4;

                if (temp != this.menu.getField(i)) {
                    this.menu.setField(i, temp);
                    assert Minecraft.getInstance().level != null;
                    Network.setIncubatorTemperature(this.menu.getBlockPos(), i, temp, Minecraft.getInstance().level.dimension());
//                    jurassicreborn.NETWORK_WRAPPER.sendToServer(new ChangeTemperatureMessage(((TileEntity) this.incubator).getPos(), i, temp, ((TileEntity) this.incubator).getWorld().provider.getDimension()));
                }

                break;
            }
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(guiGraphics,pMouseX, pMouseY, pPartialTick);
        super.render(guiGraphics, pMouseX, pMouseY, pPartialTick);
        this.renderTooltip(guiGraphics, pMouseX, pMouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);


        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.getXSize(), this.getYSize());


        for (int i = 0; i < 5; i++) {
            int progress = this.getProgress(i, 14);

            int x = 0;
            int y = 0;

            switch (i) {
                case 0:
                    x = 33;
                    y = 28;
                    break;
                case 1:
                    x = 56;
                    y = 21;
                    break;
                case 2:
                    x = 79;
                    y = 14;
                    break;
                case 3:
                    x = 102;
                    y = 21;
                    break;
                case 4:
                    x = 125;
                    y = 28;
                    break;
            }

            x++;
            y += 24;

            guiGraphics.blit(TEXTURE, this.leftPos + x, this.topPos + y, 176, 5, progress, 5);

            int temp = this.getTemperature(i, 20);

            guiGraphics.blit(TEXTURE, this.leftPos + x + temp - 3, this.topPos + y - 6, 176, 0, 3, 5);
        }
    }

    private int getProgress(int slot, int scale) {
        int j = this.menu.getField(slot + 5);
        int k = IncubatorBlockEntity.PROCESS_TIME;
        return j != 0 ? j * scale / k : 0;
    }

    private int getTemperature(int slot, int scale) {
        int j = this.menu.getField(slot);
        int k = 100;
        return j != 0 ? j * scale / k : 0;
    }
}
