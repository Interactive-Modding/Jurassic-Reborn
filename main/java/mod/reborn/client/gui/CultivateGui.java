package mod.reborn.client.gui;

import mod.reborn.RebornMod;
import mod.reborn.server.block.entity.CultivatorBlockEntity;
import mod.reborn.server.container.CultivateContainer;
import mod.reborn.server.message.ChangeTemperatureMessage;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;

@SideOnly(Side.CLIENT)
public class CultivateGui extends GuiContainer {
    private static final ResourceLocation TEXTURE = new ResourceLocation(RebornMod.MODID, "textures/gui/cultivator.png");
    private static final ResourceLocation NUTRIENTS_TEXTURE = new ResourceLocation(RebornMod.MODID, "textures/gui/cultivator_nutrients.png");
    private final InventoryPlayer playerInventory;
    private CultivatorBlockEntity cultivator;

    public CultivateGui(InventoryPlayer inventoryPlayer, CultivatorBlockEntity entity) {
        super(new CultivateContainer(inventoryPlayer, entity));
        this.playerInventory = inventoryPlayer;
        this.cultivator = entity;
        this.xSize = 352;
        this.ySize = 188;
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

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int i, int j) {
        String name = this.cultivator.getDisplayName().getUnformattedText();
        this.fontRenderer.drawString(name, this.xSize / 2 - this.fontRenderer.getStringWidth(name) / 2 - 45, 10, 4210752);
        this.fontRenderer.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);

        this.fontRenderer.drawString(I18n.format("cultivator.proximates.name"), 200, 48, 4210752);
        this.fontRenderer.drawString(I18n.format("cultivator.minerals.name"), 200, 74, 4210752);
        this.fontRenderer.drawString(I18n.format("cultivator.vitamins.name"), 200, 100, 4210752);
        this.fontRenderer.drawString(I18n.format("cultivator.lipids.name"), 200, 126, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
        this.mc.renderEngine.bindTexture(TEXTURE);
        this.drawTexturedModalRect(this.width / 2 - this.xSize / 2, this.height / 2 - this.ySize / 2, 0, 0, 176, 188);

        this.drawTexturedModalRect(this.guiLeft + 48, this.guiTop + 18, 0, 188, 42, 67 - this.getScaled(this.cultivator.getWaterLevel(), 2, 67));

        this.drawTemperatureSlider();

        this.mc.renderEngine.bindTexture(NUTRIENTS_TEXTURE);
        this.drawTexturedModalRect(this.width / 2 + 1, this.height / 2 - this.ySize / 2, 0, 0, 176, 166);

        int maxNutrients = this.cultivator.getMaxNutrients();

        int nutrientsX = this.guiLeft + 190;

        this.drawTexturedModalRect(nutrientsX, this.guiTop + 56, 0, 166, this.getScaled(this.cultivator.getProximates(), maxNutrients, 150), 9);
        this.drawTexturedModalRect(nutrientsX, this.guiTop + 82, 0, 175, this.getScaled(this.cultivator.getMinerals(), maxNutrients, 150), 9);
        this.drawTexturedModalRect(nutrientsX, this.guiTop + 108, 0, 184, this.getScaled(this.cultivator.getVitamins(), maxNutrients, 150), 9);
        this.drawTexturedModalRect(nutrientsX, this.guiTop + 134, 0, 193, this.getScaled(this.cultivator.getLipids(), maxNutrients, 150), 9);
    }

    private void drawTemperatureSlider() {
        int screenX = (this.width - this.xSize) / 2;
        int screenY = (this.height - this.ySize) / 2;

        int x = screenX + 59;
        int y = screenY + 88;

        int temperature = this.cultivator.getTemperature(0) * 20 / 100;

        this.drawTexturedModalRect(x + temperature, y, 176, 0, 3, 5);
    }

    private void dragTemperatureSlider(int mouseX, int mouseY) {
        int screenX = (this.width - this.xSize) / 2;
        int screenY = (this.height - this.ySize) / 2;

        int x = screenX + 59;
        int y = screenY + 88;

        if (mouseX > x && mouseY > y && mouseX < x + 21 && mouseY < y + 5) {
            int mouseTemperature = (mouseX - x + 1) * 4;
            if (mouseTemperature != this.cultivator.getTemperature(0)) {
                this.cultivator.setTemperature(0, mouseTemperature);
                RebornMod.NETWORK_WRAPPER.sendToServer(new ChangeTemperatureMessage(this.cultivator.getPos(), 0, mouseTemperature, this.cultivator.getWorld().provider.getDimension()));
            }
        }
    }

    private int getScaled(int value, int maxValue, int scale) {
        return maxValue != 0 && value != 0 ? value * scale / maxValue : 0;
    }
}
