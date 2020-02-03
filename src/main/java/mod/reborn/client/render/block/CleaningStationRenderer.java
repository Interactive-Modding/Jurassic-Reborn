package mod.reborn.client.render.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import mod.reborn.server.block.BlockHandler;
import mod.reborn.server.block.OrientedBlock;
import mod.reborn.server.block.entity.CleaningStationBlockEntity;

public class CleaningStationRenderer extends TileEntitySpecialRenderer<CleaningStationBlockEntity> {
    private Minecraft mc = Minecraft.getMinecraft();

    @Override
    public void render(CleaningStationBlockEntity tileEntity, double x, double y, double z, float p_180535_8_, int p_180535_9_, float alpha) {
        World world = tileEntity.getWorld();

        IBlockState state = world.getBlockState(tileEntity.getPos());

        if (state.getBlock() == BlockHandler.CLEANING_STATION) {
            EnumFacing value = state.getValue(OrientedBlock.FACING);

            if (value == EnumFacing.NORTH || value == EnumFacing.SOUTH) {
                value = value.getOpposite();
            }

            int rotation = value.getHorizontalIndex() * 90;
            float scale = 0.25F;

            GlStateManager.pushMatrix();
            GlStateManager.translate(x + 0.5, y + 1.5F, z + 0.5);

            GlStateManager.rotate(rotation, 0, 1, 0);
            GlStateManager.translate(0.0, -1.1, -0.05);
            GlStateManager.scale(-scale, -scale, scale);
            GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);

            this.mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

            RenderItem renderItem = this.mc.getRenderItem();

            ItemStack cleanable = tileEntity.getStackInSlot(0);

            if (cleanable != null) {
                renderItem.renderItem(cleanable, renderItem.getItemModelMesher().getItemModel(cleanable));
            }

            GlStateManager.popMatrix();
        }
    }
}
