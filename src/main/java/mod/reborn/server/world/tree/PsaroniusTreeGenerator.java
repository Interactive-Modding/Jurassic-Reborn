package mod.reborn.server.world.tree;

import mod.reborn.server.block.BlockHandler;
import mod.reborn.server.block.tree.AncientLeavesBlock;
import mod.reborn.server.block.tree.AncientLogBlock;
import mod.reborn.server.block.tree.AncientSaplingBlock;
import mod.reborn.server.block.tree.TreeType;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

import java.util.Random;

public class PsaroniusTreeGenerator extends WorldGenAbstractTree {
    public PsaroniusTreeGenerator() {
        super(true);
    }

    @Override
    public boolean generate(World world, Random rand, BlockPos position) {
        IBlockState log = BlockHandler.ANCIENT_LOGS.get(TreeType.PSARONIUS).getDefaultState();
        IBlockState leaves = BlockHandler.ANCIENT_LEAVES.get(TreeType.PSARONIUS).getDefaultState();
        int scale = rand.nextInt(1) + 1;
        int height = scale + 6 + rand.nextInt(2);
        BlockPos topPosition = position.up(height);
        for (int y = 0; y < height; y++) {
            BlockPos logPosition = position.up(y);
            this.setBlockState(world, logPosition, log);
        }
        int leafScale = scale + 2;
        for (int x = -leafScale; x <= leafScale; x++) {
            this.setBlockState(world, topPosition.add(x, 0, 0), leaves);
        }
        for (int z = -leafScale; z <= leafScale; z++) {
            this.setBlockState(world, topPosition.add(0, 0, z), leaves);
        }
        this.setBlockState(world, topPosition.add(-leafScale - 1, -1, 0), leaves);
        this.setBlockState(world, topPosition.add(leafScale + 1, -1, 0), leaves);
        this.setBlockState(world, topPosition.add(0, -1, -leafScale - 1), leaves);
        this.setBlockState(world, topPosition.add(0, -1, leafScale + 1), leaves);
        this.setBlockState(world, topPosition.add(-2, -1, -2), leaves);
        this.setBlockState(world, topPosition.add(-2, -1, 2), leaves);
        this.setBlockState(world, topPosition.add(2, -1, 2), leaves);
        this.setBlockState(world, topPosition.add(2, -1, -2), leaves);
        this.generateClump(world, topPosition, 2.5, leaves);
        return true;
    }

    private void generateClump(World world, BlockPos pos, double size, IBlockState state) {
        int blockRadius = (int) Math.ceil(size);
        for (int x = -blockRadius; x < blockRadius; x++) {
            for (int y = -blockRadius; y < blockRadius; y++) {
                for (int z = -blockRadius; z < blockRadius; z++) {
                    if (Math.abs(x * x + y * y + z * z) <= size) {
                        BlockPos leafPos = pos.add(x, y, z);
                        if (world.isAirBlock(leafPos)) {
                            this.setBlockState(world, leafPos, state);
                        }
                    }
                }
            }
        }
    }

    private void setBlockState(World world, BlockPos pos, IBlockState state) {
        Block block = world.getBlockState(pos).getBlock();
        if (this.canGrowInto(block) || block instanceof AncientLeavesBlock || block instanceof AncientSaplingBlock || block instanceof AncientLogBlock) {
            world.setBlockState(pos, state);
        }
    }
}
