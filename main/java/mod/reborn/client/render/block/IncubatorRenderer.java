package mod.reborn.client.render.block;

import mod.reborn.client.render.RenderingHandler;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import mod.reborn.client.render.entity.dinosaur.DinosaurRenderInfo;
import mod.reborn.server.block.BlockHandler;
import mod.reborn.server.block.entity.IncubatorBlockEntity;
import mod.reborn.server.entity.EntityHandler;

public class IncubatorRenderer extends TileEntitySpecialRenderer<IncubatorBlockEntity> {
    private Minecraft mc = Minecraft.getMinecraft();

    @Override
    public void render(IncubatorBlockEntity tileEntity, double x, double y, double z, float p_180535_8_, int p_180535_9_, float alpha) {
        World world = tileEntity.getWorld();

        IBlockState state = world.getBlockState(tileEntity.getPos());

        if (state.getBlock() == BlockHandler.INCUBATOR) {
            GlStateManager.cullFace(GlStateManager.CullFace.FRONT);
            GlStateManager.disableRescaleNormal();

            this.renderEgg(tileEntity.getStackInSlot(0), x, y, z, 0.6, 0.7);
            this.renderEgg(tileEntity.getStackInSlot(1), x, y, z, 0.2, 0.1);
            this.renderEgg(tileEntity.getStackInSlot(3), x, y, z, 0.8, 0.5);
            this.renderEgg(tileEntity.getStackInSlot(4), x, y, z, 1.0, 0.2);
            this.renderEgg(tileEntity.getStackInSlot(2), x, y, z, 0.3, 0.5);

            GlStateManager.enableRescaleNormal();
            GlStateManager.cullFace(GlStateManager.CullFace.BACK);
        }
    }

    private void renderEgg(ItemStack stack, double x, double y, double z, double xOffset, double zOffset) {
        if (!stack.isEmpty()) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(x, y + 0.65, z);
            GlStateManager.translate(xOffset, 1.2, zOffset);
            GlStateManager.scale(-0.5F, -0.5F, -0.5F);
            DinosaurRenderInfo renderDef = RenderingHandler.INSTANCE.getRenderInfo(EntityHandler.getDinosaurById(stack.getItemDamage()));
            this.mc.getTextureManager().bindTexture(renderDef.getEggTexture());
            renderDef.getEggModel().render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
            GlStateManager.popMatrix();
        }
    }
}
