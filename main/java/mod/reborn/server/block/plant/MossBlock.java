package mod.reborn.server.block.plant;

import mod.reborn.server.block.PeatBlock;
import mod.reborn.server.tab.TabHandler;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class MossBlock extends Block {
    private static final AxisAlignedBB BOUNDS = new AxisAlignedBB(0.0F, 0.0F, 0.0F, 1.0F, 0.0625F, 1.0F);

    private static final int DENSITY_PER_AREA = 8;
    private static final int SPREAD_RADIUS = 3;

    public MossBlock() {
        super(Material.LEAVES);

        this.setHardness(0.2F);
        this.setResistance(0.0F);
        this.setTickRandomly(true);
        this.setCreativeTab(TabHandler.PLANTS);
        this.setSoundType(SoundType.PLANT);
        this.setLightOpacity(0);
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
        if (world.getBlockState(pos.down()).getBlock() instanceof PeatBlock) {
            if (rand.nextInt(8) <= 3) {
                int allowedInArea = DENSITY_PER_AREA;

                BlockPos nextPos = null;
                int placementAttempts = 3;

                while (nextPos == null && placementAttempts > 0) {
                    int doubleRadius = SPREAD_RADIUS * 2;
                    BlockPos tmp = pos.add(rand.nextInt(doubleRadius) - SPREAD_RADIUS, -SPREAD_RADIUS, rand.nextInt(doubleRadius) - SPREAD_RADIUS);
                    nextPos = this.findGround(world, tmp);
                    placementAttempts--;
                }

                if (nextPos != null) {
                    for (BlockPos neighbourPos : BlockPos.getAllInBoxMutable(nextPos.add(-2, -3, -2), nextPos.add(2, 3, 2))) {
                        if (world.getBlockState(neighbourPos).getBlock() == this) {
                            allowedInArea--;

                            if (allowedInArea <= 0) {
                                return;
                            }
                        }
                    }

                    world.setBlockState(nextPos, this.getDefaultState());
                }
            }
        }
    }

    private BlockPos findGround(World world, BlockPos start) {
        BlockPos pos = start;

        Block down = world.getBlockState(pos.down()).getBlock();
        Block here = world.getBlockState(pos).getBlock();
        Block up = world.getBlockState(pos.up()).getBlock();

        for (int i = 0; i < 8; ++i) {
            if (down instanceof PeatBlock) {
                return pos;
            }

            down = here;
            here = up;
            pos = pos.up();
            up = world.getBlockState(pos.up()).getBlock();
        }

        return null;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess blockAccess, BlockPos pos) {
        return BOUNDS;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean canPlaceBlockAt(World world, BlockPos pos) {
        IBlockState below = world.getBlockState(pos.down());
        return super.canPlaceBlockAt(world, pos) && this.canBlockStay(world, pos) && (below.isFullCube());
    }
    
    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) {
        super.neighborChanged(state, world, pos, block, fromPos);
        this.checkForDrop(world, pos, state);
    }

    private boolean checkForDrop(World world, BlockPos pos, IBlockState state) {
        if (!this.canBlockStay(world, pos)) {
            this.dropBlockAsItem(world, pos, state, 0);
            world.setBlockToAir(pos);
            return false;
        } else {
            return true;
        }
    }

    private boolean canBlockStay(World world, BlockPos pos) {
        return !world.isAirBlock(pos.down());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
        return side == EnumFacing.UP || super.shouldSideBeRendered(state, world, pos, side);
    }
}
