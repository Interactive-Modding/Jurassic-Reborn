package net.vit.jurassicreborn.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.common.blocks.entities.DNABlocks.DNACombinatorHybridizer.DNACombinatorHybridizerBlockEntity;
import net.vit.jurassicreborn.common.blocks.entities.DNABlocks.DNACombinatorHybridizer.DNACombinatorHybridizerMenu;
import net.vit.jurassicreborn.common.network.Network;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class DNACombinatorHybridizerScreen extends AbstractContainerScreen<DNACombinatorHybridizerMenu> {
    private static final ResourceLocation hybridizerTexture = JurassicReborn.resource("textures/gui/dna_hybridizer.png");
    private static final ResourceLocation combinatorTexture = JurassicReborn.resource("textures/gui/dna_combinator.png");

    private final Inventory playerInventory;
//    private DNACombinatorHybridizerBlockEntity inventory;
    private DNACombinatorHybridizerMenu menu;

    private Button switchModeButton;



    public DNACombinatorHybridizerScreen(DNACombinatorHybridizerMenu menu, Inventory playerInv, Component title) {
        super(menu, playerInv, title);
        this.playerInventory = playerInv;
        this.menu = menu;
    }


    @Override
    public @NotNull Component getTitle() {
        if(Minecraft.getInstance().level.getBlockEntity(new BlockPos(this.menu.getField(3), this.menu.getField(4), this.menu.getField(5))) instanceof DNACombinatorHybridizerBlockEntity e){
            return e.getDisplayName();
        }
        return this.title;
    }




//    @Override
//    public void initGui() {
//        super.initGui();
//
//
//    }

    @Override
    protected void init() {
        super.init();
        int xSize = this.leftPos;
        int ySize = this.topPos;
        this.titleLabelY -= 3;



        this.switchModeButton = this.addRenderableWidget(Button.builder(Component.literal("<->"), (w) -> {
            BlockPos entityPos = new BlockPos(menu.getField(3), menu.getField(4), menu.getField(5));

            boolean mode = !this.menu.getMode();
            this.menu.updateSlots(!mode);
            this.menu.setMode(mode);

            Network.switchHybridizerCombinerMode(mode, entityPos, playerInventory.player.level().dimension());
        }).bounds(xSize + 128, ySize + 64, 30, 12).build());
    }

//    @Override
//    public void actionPerformed(GuiButton button) {
//
//    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int id) {
//        if (id == 0) {
//        }
        return super.mouseClicked(pMouseX, pMouseY, id);
    }



    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        boolean isHybridizer = this.menu.getMode();
        this.menu.updateSlots(!isHybridizer);
//        this.title = Component.translatable(isHybridizer ? "container.dna_hybridizer" : "container.dna_combinator");
        this.renderBackground(graphics,mouseX, mouseY, partialTick);
        super.render(graphics, mouseX, mouseY, partialTick);
//        this.renderLabels(pPoseStack, mouseX, mouseY);


        this.renderTooltip(graphics, mouseX, mouseY);

    }

    //    @Override
//    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
////        String name = this.menu.getDisplayName().getUnformattedText();
//        this.fontRenderer.drawString(name, this.xSize / 2 - this.fontRenderer.getStringWidth(name) / 2, 4, 4210752);
//        this.fontRenderer.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);
//    }

    @Override
    protected void renderBg(@NotNull GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        boolean isHybridizer = this.menu.getMode();
        this.menu.updateSlots(!isHybridizer);


        RenderSystem.setShaderTexture(0, isHybridizer ? hybridizerTexture : combinatorTexture);

//        this.mc.getTextureManager().bindTexture();
        int xSize = this.leftPos;
        int ySize = this.topPos;

        int centerX = (this.width - xSize) / 2;
        int centerY = (this.height - ySize) / 2;
        graphics.blit(isHybridizer ? hybridizerTexture : combinatorTexture, xSize, ySize, 0, 0, 176, 166);

        int progress = this.getProgress(isHybridizer ? 27 : 24);


        if (isHybridizer) {
            graphics.blit(hybridizerTexture, this.leftPos + 86, this.topPos + 25, 176, 0, 4, progress);
        } else {

            if(progress >= 2)
                graphics.blit(combinatorTexture, this.leftPos + 93, this.topPos + 31, 176, 1, 8, progress-1);

            if(progress >= 1)
                graphics.blit(combinatorTexture, this.leftPos + 93, this.topPos + 30, 176, 1, 8, progress == 1 ? 1 : 2);
        }

    }

    private int getProgress(int scale) {
        int j = this.menu.getField(0);
        int k = this.menu.getField(1);
        return k != 0 && j != 0 ? j * scale / k : 0;
    }
}