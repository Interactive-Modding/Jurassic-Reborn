package mod.reborn.client.render.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import mod.reborn.server.block.BlockHandler;
import mod.reborn.server.block.OrientedBlock;
import mod.reborn.server.block.entity.DNAExtractorBlockEntity;
import org.lwjgl.opengl.GL11;

public class DNAExtractorRenderer extends TileEntitySpecialRenderer<DNAExtractorBlockEntity> {
    private Minecraft mc = Minecraft.getMinecraft();

    @Override
    public void render(DNAExtractorBlockEntity tileEntity, double x, double y, double z, float p_180535_8_, int p_180535_9_, float alpha) {
        IBlockState state = tileEntity.getWorld().getBlockState(tileEntity.getPos());
        ItemStack extraction = tileEntity.getStackInSlot(0);

        if (extraction != null && state.getBlock() == BlockHandler.DNA_EXTRACTOR) {
            GlStateManager.pushMatrix();
            GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GlStateManager.enableBlend();
            GlStateManager.disableCull();

            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.translate(x + 0.5, y + 1.5, z + 0.5);

            EnumFacing facing = state.getValue(OrientedBlock.FACING);

            if (facing == EnumFacing.EAST || facing == EnumFacing.WEST) {
                facing = facing.getOpposite();
            }

            int rotation = facing.getHorizontalIndex() * 90;

            GlStateManager.rotate(rotation - 180, 0, 1, 0);

            double scale = 1.0;
            GlStateManager.scale(-scale, -scale, scale);

            this.mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

            RenderItem renderItem = this.mc.getRenderItem();

            GlStateManager.translate(0.225, 1.25, -0.125);
            GlStateManager.rotate(-90, 1, 0, 0);
            GlStateManager.scale(-0.75 * 0.5, -0.75 * 0.5, 0.75 * 0.5);
            renderItem.renderItem(extraction, renderItem.getItemModelMesher().getItemModel(extraction));

            GlStateManager.disableBlend();
            GlStateManager.enableCull();
            GlStateManager.popMatrix();
        }
    }
}
