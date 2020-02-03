package mod.reborn.server.block.plant;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AncientCoralBlock extends AncientPlantBlock {
    public static final PropertyInteger LEVEL = PropertyInteger.create("level", 0, 15);

    public AncientCoralBlock() {
        super(Material.WATER);
        this.setSoundType(SoundType.PLANT);
        this.setDefaultState(this.blockState.getBaseState().withProperty(LEVEL, 0));
    }

    @Override
    protected boolean canPlace(IBlockState down, IBlockState here, IBlockState up) {
        return this.canSustainBush(down) && here.getBlock() == Blocks.WATER && up.getBlock() == Blocks.WATER;
    }

    @Override
    protected boolean canSustainBush(IBlockState ground) {
        Block block = ground.getBlock();
        return block == Blocks.SAND || block == Blocks.CLAY || block == Blocks.GRAVEL || ground.getMaterial() == Material.GROUND;
    }

    @Override
    public boolean canPlaceBlockAt(World world, BlockPos pos) {
        IBlockState down = world.getBlockState(pos.down());
        IBlockState here = world.getBlockState(pos);
        IBlockState up = world.getBlockState(pos.up());
        return this.canPlace(down, here, up);
    }

    @Override
    public boolean canBlockStay(World world, BlockPos pos, IBlockState state) {
        IBlockState down = world.getBlockState(pos.down());
        IBlockState here = world.getBlockState(pos);
        IBlockState up = world.getBlockState(pos.up());
        return this.canPlace(down, here, up);
    }

    @Override
    protected boolean isNearWater(World world, BlockPos nextPos) {
        return true;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, LEVEL);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return 0;
    }
}
