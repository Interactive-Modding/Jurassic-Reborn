package mod.reborn.client.gui;

import mod.reborn.RebornMod;
import mod.reborn.server.block.entity.CultivatorBlockEntity;
import mod.reborn.server.container.CultivateContainer;
import mod.reborn.server.dinosaur.Dinosaur;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class CultivateProcessGui extends GuiScreen {
    private CultivatorBlockEntity cultivator;
    private CultivateContainer container;

    private int xSize;
    private int ySize;
    private int guiLeft;
    private int guiTop;

    private static final ResourceLocation TEXTURE = new ResourceLocation(RebornMod.MODID, "textures/gui/cultivator_progress.png");

    public CultivateProcessGui(InventoryPlayer inventory, CultivatorBlockEntity entity) {
        super();
        this.container = new CultivateContainer(inventory, entity);
        this.cultivator = entity;
        this.xSize = 176;
        this.ySize = 107;
    }

    @Override
    public void initGui() {
        super.initGui();

        this.buttonList.clear();

        this.guiLeft = (this.width - this.xSize) / 2;
        this.guiTop = (this.height - this.ySize) / 2;

        this.buttonList.add(new GuiButton(0, this.guiLeft + (this.xSize - 100) / 2, this.guiTop + 70, 100, 20, I18n.format("container.close.name")));

        this.mc.player.openContainer = this.container;
    }

    @Override
    public void updateScreen() {
        if (!this.cultivator.isProcessing(0)) {
            this.mc.player.closeScreen();
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    protected void keyTyped(char c, int key) {
        if (key == 1 || key == this.mc.gameSettings.keyBindInventory.getKeyCode()) {
            this.mc.player.closeScreen();
        }
    }

    @Override
    public void actionPerformed(GuiButton button) {
        if (button.id == 0) {
            this.mc.player.closeScreen();
        }
    }

    @Override
    public void drawScreen(int x, int y, float f) {
        this.drawDefaultBackground();
        this.mc.renderEngine.bindTexture(this.TEXTURE);

        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        this.drawTexturedModalRect(this.guiLeft + 13, this.guiTop + 49, 0, 107, this.getScaled(this.cultivator.getField(0), this.cultivator.getField(1), 150), 9);

        Dinosaur dinosaur = this.cultivator.getDinosaur();

        String name;

        if (dinosaur != null) {
            name = dinosaur.getName();
        } else {
            name = "Unknown";
        }

        String cultivatingLang = I18n.format("container.cultivator.cultivating");
        String progressLang = I18n.format("container.cultivator.progress");

        String progress = progressLang + ": " + this.getScaled(this.cultivator.getField(0), this.cultivator.getField(1), 100) + "%";
        String cultivating = cultivatingLang + ": " + name;

        this.fontRenderer.drawString(cultivating, this.guiLeft + (this.xSize - this.fontRenderer.getStringWidth(cultivating)) / 2, this.guiTop + 10, 4210752);
        this.fontRenderer.drawString(progress, this.guiLeft + (this.xSize - this.fontRenderer.getStringWidth(progress)) / 2, this.guiTop + 40, 4210752);

        super.drawScreen(x, y, f);
    }

    private int getScaled(int value, int maxValue, int scale) {
        return maxValue != 0 && value != 0 ? value * scale / maxValue : 0;
    }
}
