package mod.reborn.client.gui;

import mod.reborn.server.container.EmbryoCalcificationMachineContainer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class EmbryoCalcificationMachineGui extends GuiContainer {
    private static final ResourceLocation TEXTURE = new ResourceLocation("rebornmod:textures/gui/embryo_calcification_machine.png");

    private final InventoryPlayer playerInventory;
    private IInventory inventory;

    public EmbryoCalcificationMachineGui(InventoryPlayer playerInv, IInventory inventory) {
        super(new EmbryoCalcificationMachineContainer(playerInv, (TileEntity) inventory));
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
        String displayName = this.inventory.getDisplayName().getUnformattedText();
        this.fontRenderer.drawString(displayName, this.xSize / 2 - this.fontRenderer.getStringWidth(displayName) / 2, 4, 4210752);
        this.fontRenderer.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(TEXTURE);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);

        int progress = this.getProgress(24);
        int progress1 = this.getProgress(9);
        int progress2 = this.getProgress(20);
        this.drawTexturedModalRect(k + 67, l + 31, 176, 14, progress + 1, 16);
        // Syringe Top
        this.drawTexturedModalRect(k + 38, l + 32, 177, 32, 9, progress1);
        // Syringe Inside
        this.drawTexturedModalRect(k + 38, l + 38, 197, 38, 9, progress2);
        // Clean up
        this.drawTexturedModalRect(k + 38, l + 32, 187, 32, 9, progress1 - 1);
    }

    private int getProgress(int scale) {
        int progress = this.inventory.getField(0);
        int nax = this.inventory.getField(1);
        return nax != 0 && progress != 0 ? progress * scale / nax : 0;
    }
}
