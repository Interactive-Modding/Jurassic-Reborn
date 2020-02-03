package mod.reborn.server.block.plant;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class SmallCycadBlock extends AncientPlantBlock {
    private static final AxisAlignedBB BOUNDS = new AxisAlignedBB(0.1F, 0.0F, 0.1F, 0.9F, 0.8F, 0.9F);

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess blockAccess, BlockPos pos) {
        return BOUNDS;
    }
}
