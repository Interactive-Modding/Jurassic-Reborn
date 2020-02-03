package mod.reborn.server.block;

import mod.reborn.server.tab.TabHandler;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeSwamp;

import java.util.Random;

public class PeatBlock extends Block {
    public static final PropertyInteger MOISTURE = PropertyInteger.create("moisture", 0, 7);

    public PeatBlock() {
        super(Material.GROUND);
        this.setCreativeTab(TabHandler.PLANTS);
        this.setSoundType(SoundType.GROUND);
        this.setHardness(0.5F);
        this.setDefaultState(this.blockState.getBaseState().withProperty(MOISTURE, 0));
        this.setTickRandomly(true);
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
        int moisture = state.getValue(MOISTURE);

        if (!this.hasWater(world, pos) && !world.isRainingAt(pos.up()) && !(world.getBiome(pos) instanceof BiomeSwamp)) {
            if (moisture > 0) {
                world.setBlockState(pos, state.withProperty(MOISTURE, moisture - 1), 2);
            } else {
                world.setBlockState(pos, Blocks.DIRT.getDefaultState());
            }
        } else if (moisture < 7) {
            world.setBlockState(pos, state.withProperty(MOISTURE, 7), 2);
        }
    }

    private boolean hasWater(World world, BlockPos pos) {
        for (BlockPos.MutableBlockPos nearPos : BlockPos.getAllInBoxMutable(pos.add(-4, 0, -4), pos.add(4, 1, 4))) {
            if (world.getBlockState(nearPos).getMaterial() == Material.WATER) {
                return true;
            }
        }

        return false;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(MOISTURE, meta & 7);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(MOISTURE);
    }

    @Override
    public BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, MOISTURE);
    }
}
