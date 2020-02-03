package mod.reborn.client.render.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import mod.reborn.server.block.BlockHandler;
import mod.reborn.server.block.OrientedBlock;
import mod.reborn.server.block.entity.DNASequencerBlockEntity;

public class DNASequencerRenderer extends TileEntitySpecialRenderer<DNASequencerBlockEntity> {
    private Minecraft mc = Minecraft.getMinecraft();

    @Override
    public void render(DNASequencerBlockEntity tileEntity, double x, double y, double z, float p_180535_8_, int p_180535_9_, float alpha) {
        World world = tileEntity.getWorld();

        IBlockState state = world.getBlockState(tileEntity.getPos());

        if (state.getBlock() == BlockHandler.DNA_SEQUENCER) {
            EnumFacing value = state.getValue(OrientedBlock.FACING);

            if (value == EnumFacing.NORTH || value == EnumFacing.SOUTH) {
                value = value.getOpposite();
            }

            int rotation = value.getHorizontalIndex() * 90;

            GlStateManager.pushMatrix();
            GlStateManager.translate(x + 0.5, y + 1.5F, z + 0.5);

            GlStateManager.rotate(rotation, 0, 1, 0);
            GlStateManager.translate(-0.2, -0.6, 0.15);
            GlStateManager.scale(-0.375F, -0.375F, 0.375F);
            GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);

            this.mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

            RenderItem renderItem = this.mc.getRenderItem();

            GlStateManager.translate(0.0, 0.0, MathHelper.sin((this.mc.player.ticksExisted + this.mc.getRenderPartialTicks()) * 0.05F) * 0.025F);

            for (int inputSlot : tileEntity.getSlotsForFace(EnumFacing.UP)) {
                GlStateManager.translate(0.0, 0.0, -0.4);

                if (inputSlot % 2 == 0) {
                    ItemStack sequence = tileEntity.getStackInSlot(inputSlot);

                    if (sequence != null) {
                        renderItem.renderItem(sequence, renderItem.getItemModelMesher().getItemModel(sequence));
                    }
                }
            }

            GlStateManager.popMatrix();
        }
    }
}
