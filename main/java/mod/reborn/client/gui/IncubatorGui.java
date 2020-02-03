package mod.reborn.client.gui;

import mod.reborn.RebornMod;
import mod.reborn.server.message.ChangeTemperatureMessage;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import mod.reborn.server.container.IncubatorContainer;

import java.io.IOException;

@SideOnly(Side.CLIENT)
public class IncubatorGui extends GuiContainer {
    private static final ResourceLocation TEXTURE = new ResourceLocation("rebornmod:textures/gui/incubator.png");
    private final InventoryPlayer playerInventory;
    private IInventory incubator;

    public IncubatorGui(InventoryPlayer playerInv, IInventory incubator) {
        super(new IncubatorContainer(playerInv, (TileEntity) incubator));
        this.playerInventory = playerInv;
        this.incubator = incubator;
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.dragTemperatureSlider(mouseX, mouseY);
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        this.dragTemperatureSlider(mouseX, mouseY);
    }

    private void dragTemperatureSlider(int mouseX, int mouseY) {
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;

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
                int temp = (mouseX - x + 1) * 4;

                if (temp != this.incubator.getField(i + 10)) {
                    this.incubator.setField(i + 10, temp);
                    RebornMod.NETWORK_WRAPPER.sendToServer(new ChangeTemperatureMessage(((TileEntity) this.incubator).getPos(), i, temp, ((TileEntity) this.incubator).getWorld().provider.getDimension()));
                }

                break;
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String name = this.incubator.getDisplayName().getUnformattedText();
        this.fontRenderer.drawString(name, this.xSize / 2 - this.fontRenderer.getStringWidth(name) / 2, 4, 4210752);
        this.fontRenderer.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 4, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(TEXTURE);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);

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

            this.drawTexturedModalRect(k + x, l + y, 176, 5, progress, 5);

            int temp = this.getTemperature(i, 20);

            this.drawTexturedModalRect(k + x + temp - 3, l + y - 6, 176, 0, 3, 5);
        }
    }

    private int getProgress(int slot, int scale) {
        int j = this.incubator.getField(slot);
        int k = this.incubator.getField(slot + 5);
        return k != 0 && j != 0 ? j * scale / k : 0;
    }

    private int getTemperature(int slot, int scale) {
        int j = this.incubator.getField(slot + 10);
        int k = 100;
        return j != 0 ? j * scale / k : 0;
    }
}
