package mod.reborn.client.gui;

import mod.reborn.RebornMod;
import mod.reborn.server.block.entity.DNACombinatorHybridizerBlockEntity;
import mod.reborn.server.container.DNACombinatorHybridizerContainer;
import mod.reborn.server.message.SwitchHybridizerCombinatorMode;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class DNACombinatorHybridizerGui extends GuiContainer {
    private static final ResourceLocation hybridizerTexture = new ResourceLocation("rebornmod:textures/gui/dna_hybridizer.png");
    private static final ResourceLocation combinatorTexture = new ResourceLocation("rebornmod:textures/gui/dna_combinator.png");

    private final InventoryPlayer playerInventory;
    private DNACombinatorHybridizerBlockEntity inventory;
    private DNACombinatorHybridizerContainer container;

    public DNACombinatorHybridizerGui(InventoryPlayer playerInv, DNACombinatorHybridizerBlockEntity inventory) {
        super(new DNACombinatorHybridizerContainer(playerInv, inventory));
        this.playerInventory = playerInv;
        this.inventory = inventory;
        this.container = (DNACombinatorHybridizerContainer) this.inventorySlots;
    }

    @Override
    public void initGui() {
        super.initGui();

        int guiX = (this.width - this.xSize) / 2;
        int guiY = (this.height - this.ySize) / 2;

        this.buttonList.add(new GuiButton(0, guiX + 128, guiY + 64, 30, 10, "<->"));
    }

    @Override
    public void actionPerformed(GuiButton button) {
        if (button.id == 0) {
            boolean mode = !this.inventory.getMode();
            this.container.updateSlots(mode);
            this.inventory.setMode(mode);
            RebornMod.NETWORK_WRAPPER.sendToServer(new SwitchHybridizerCombinatorMode(this.inventory.getPos(), mode));
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
        String name = this.inventory.getDisplayName().getUnformattedText();
        this.fontRenderer.drawString(name, this.xSize / 2 - this.fontRenderer.getStringWidth(name) / 2, 4, 4210752);
        this.fontRenderer.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        boolean isHybridizer = this.inventory.getMode();
        this.mc.getTextureManager().bindTexture(isHybridizer ? hybridizerTexture : combinatorTexture);

        int centerX = (this.width - this.xSize) / 2;
        int centerY = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(centerX, centerY, 0, 0, this.xSize, this.ySize);

        int progress = this.getProgress(isHybridizer ? 27 : 24);

        if (isHybridizer) {
            this.drawTexturedModalRect(centerX + 86, centerY + 25, 176, 0, 4, progress);
        } else {
            this.drawTexturedModalRect(centerX + 93, centerY + 30, 176, 0, 8, progress);
        }
    }

    private int getProgress(int scale) {
        int j = this.inventory.getField(0);
        int k = this.inventory.getField(1);
        return k != 0 && j != 0 ? j * scale / k : 0;
    }
}
