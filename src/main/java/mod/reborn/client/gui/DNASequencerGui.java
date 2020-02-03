package mod.reborn.client.gui;

import mod.reborn.server.container.DNASequencerContainer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class DNASequencerGui extends GuiContainer {
    private static final ResourceLocation TEXTURE = new ResourceLocation("rebornmod:textures/gui/dna_sequencer.png");

    private final InventoryPlayer playerInventory;
    private IInventory dnaSequencer;

    public DNASequencerGui(InventoryPlayer playerInv, IInventory dnaSequencer) {
        super(new DNASequencerContainer(playerInv, (TileEntity) dnaSequencer));
        this.playerInventory = playerInv;
        this.dnaSequencer = dnaSequencer;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String s = this.dnaSequencer.getDisplayName().getUnformattedText();
        this.fontRenderer.drawString(s, this.xSize / 2 - this.fontRenderer.getStringWidth(s) / 2, 6, 4210752);
        this.fontRenderer.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(TEXTURE);
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);

        for (int i = 0; i < 3; i++) {
            int progress = this.getProgress(22, i);
            this.drawTexturedModalRect(x + 87, y + 21 + i * 20, 176, 0, progress, 6);
        }
    }

    private int getProgress(int scale, int index) {
        int progress = this.dnaSequencer.getField(index);
        int maxProgress = this.dnaSequencer.getField(index + 3);

        return maxProgress != 0 && progress != 0 ? progress * scale / maxProgress : 0;
    }
}
