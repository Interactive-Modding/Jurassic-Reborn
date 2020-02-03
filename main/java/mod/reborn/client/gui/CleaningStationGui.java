package mod.reborn.client.gui;

import mod.reborn.server.block.entity.CleaningStationBlockEntity;
import mod.reborn.server.container.CleaningStationContainer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class CleaningStationGui extends GuiContainer {
    private static final ResourceLocation TEXTURE = new ResourceLocation("rebornmod:textures/gui/cleaning_station.png");

    private final InventoryPlayer playerInventory;
    private IInventory inventory;

    public CleaningStationGui(InventoryPlayer playerInv, IInventory inventory) {
        super(new CleaningStationContainer(playerInv, (TileEntity) inventory));
        this.playerInventory = playerInv;
        this.inventory = inventory;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String name = this.inventory.getDisplayName().getUnformattedText();
        this.fontRenderer.drawString(name, this.xSize / 2 - this.fontRenderer.getStringWidth(name) / 2, 6, 4210752);
        this.fontRenderer.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(TEXTURE);
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
        int progress;

        if (CleaningStationBlockEntity.isCleaning(this.inventory)) {
            progress = this.func_175382_i(51);
            this.drawTexturedModalRect(x + 46, y + 18 + 51 - progress, 176, 81 - progress, 14, progress + 1);
        }

        progress = this.getProgress(24);
        this.drawTexturedModalRect(x + 79, y + 34, 176, 14, progress + 1, 16);
    }

    private int getProgress(int scale) {
        int j = this.inventory.getField(2);
        int k = this.inventory.getField(3);
        return k != 0 && j != 0 ? j * scale / k : 0;
    }

    private int func_175382_i(int p_175382_1_) {
        int j = this.inventory.getField(1);

        if (j == 0) {
            j = 200;
        }

        return this.inventory.getField(0) * p_175382_1_ / j;
    }
}
