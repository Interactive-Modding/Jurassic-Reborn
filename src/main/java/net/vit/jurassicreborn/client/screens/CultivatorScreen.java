package net.vit.jurassicreborn.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.common.blocks.entities.cultivator.CultivatorBlockEntity;
import net.vit.jurassicreborn.common.blocks.entities.cultivator.CultivatorMenu;
import net.vit.jurassicreborn.common.network.Network;

public class CultivatorScreen extends AbstractContainerScreen<CultivatorMenu> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(JurassicReborn.MODID, "textures/gui/cultivator.png");
    private static final ResourceLocation NUTRIENTS_TEXTURE = new ResourceLocation(JurassicReborn.MODID, "textures/gui/cultivator_nutrients.png");

    public CultivatorScreen(CultivatorMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
        this.imageWidth = 352;
        this.imageHeight = 188;
    }

    @Override protected void init() { super.init(); this.titleLabelY -= 3; this.inventoryLabelY += 24; }

    @Override
    public void render(PoseStack pose, int mouseX, int mouseY, float partial) {
        this.renderBackground(pose); // fixes JEI warning
        super.render(pose, mouseX, mouseY, partial);
        int time = this.menu.getField(6);
        if (time > 0) {
            int progress = time * 100 / CultivatorBlockEntity.STACK_PROCESS_TIME;
            Component text = Component.translatable("container.cultivator.progress")
                    .append(": " + progress + "%");
            int x = this.leftPos + (this.imageWidth - this.font.width(text)) / 3;
            this.font.draw(pose, text, x, this.topPos + 5, 0x404040);
        }
        this.font.draw(pose, Component.translatable("cultivator.proximates"), this.leftPos + 190, this.topPos + 48, 0x404040);
        this.font.draw(pose, Component.translatable("cultivator.minerals"),   this.leftPos + 190, this.topPos + 74, 0x404040);
        this.font.draw(pose, Component.translatable("cultivator.vitamins"),   this.leftPos + 190, this.topPos + 100,0x404040);
        this.font.draw(pose, Component.translatable("cultivator.lipids"),     this.leftPos + 190, this.topPos + 126,0x404040);

        this.renderTooltip(pose, mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(double x, double y, int button) {
        boolean flag = super.mouseClicked(x, y, button);
        dragTemperatureSlider(x, y);
        return flag;
    }

    @Override
    public boolean mouseDragged(double x, double y, int button, double dx, double dy) {
        boolean flag = super.mouseDragged(x, y, button, dx, dy);
        dragTemperatureSlider(x, y);
        return flag;
    }

    @Override
    protected void renderBg(PoseStack pose, float partial, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1,1,1,1);

        // Left panel
        RenderSystem.setShaderTexture(0, TEXTURE);
        this.blit(pose, this.leftPos, this.topPos, 0, 0, 176, 188);

        // Water column
        this.blit(pose, this.leftPos + 48, this.topPos + 18, 0, 188, 42, 67 - getScaled(this.menu.getField(0), 2000, 67));

        // Temperature handle
        drawTemperatureSlider(pose);

        // Right panel (nutrients)
        RenderSystem.setShaderTexture(0, NUTRIENTS_TEXTURE);
        this.blit(pose, this.leftPos + 176, this.topPos, 0, 0, 176, 166);

        int max = CultivatorBlockEntity.MAX_NUTRIENTS;
        int nx = this.leftPos + 190;
        this.blit(pose, nx, this.topPos + 56, 0, 166, getScaled(this.menu.getField(2), max, 150), 9);
        this.blit(pose, nx, this.topPos + 82, 0, 175, getScaled(this.menu.getField(3), max, 150), 9);
        this.blit(pose, nx, this.topPos + 108,0, 184, getScaled(this.menu.getField(4), max, 150), 9);
        this.blit(pose, nx, this.topPos + 134,0, 193, getScaled(this.menu.getField(1), max, 150), 9);

        int time = this.menu.getField(6);     // processTime
        int maxT = 2000;                       // STACK_PROCESS_TIME
        int w = (time > 0 ? (time * 24 / maxT) : 0);
        if (w > 0) this.blit(pose, this.leftPos + 90, this.topPos + 44, 176, 5, w, 16);
    }

    private void drawTemperatureSlider(PoseStack pose) {
        int x = this.leftPos + 59, y = this.topPos + 88;
        int t = this.menu.getField(5) * 20 / 100;
        this.blit(pose, x + t, y, 176, 0, 3, 5);
    }

    private void dragTemperatureSlider(double mx, double my) {
        if (this.minecraft == null || this.minecraft.level == null) return;
        int x = this.leftPos + 59, y = this.topPos + 88;

        if (mx > x && my > y && mx < x + 21 && my < y + 5) {
            int temp = (int)(mx - x + 1) * 4;
            if (temp != this.menu.getField(5)) {
                this.menu.setField(5, temp);
                Network.setIncubatorTemperature(this.menu.getBlockPos(), 0, temp, this.minecraft.level.dimension());
            }
        }
    }

    private int getScaled(int value, int maxValue, int scale) {
        return maxValue != 0 && value != 0 ? value * scale / maxValue : 0;
    }
}
