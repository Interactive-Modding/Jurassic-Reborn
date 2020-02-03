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

public class PhoenixTreeGenerator extends WorldGenAbstractTree {
    public PhoenixTreeGenerator() {
        super(true);
    }

    @Override
    public boolean generate(World world, Random rand, BlockPos position) {
        IBlockState log = BlockHandler.ANCIENT_LOGS.get(TreeType.PHOENIX).getDefaultState();
        IBlockState leaves = BlockHandler.ANCIENT_LEAVES.get(TreeType.PHOENIX).getDefaultState();

        int height = rand.nextInt(6) + 7;

        for (int y = 0; y < height; y++) {
            this.setBlockState(world, position.up(y), log);
        }

        for (int palmX = -1; palmX < 2; palmX++) {
            for (int palmY = -1; palmY < 1; palmY++) {
                for (int palmZ = -1; palmZ < 2; palmZ++) {
                    this.setBlockState(world, position.add(palmX, height + 1 + palmY, palmZ), leaves);
                }
            }
        }

        this.setBlockState(world, position.up(height + 2), leaves);
        this.setBlockState(world, position.up(height + 3), leaves);
        this.setBlockState(world, position.up(height + 4), leaves);

        this.setBlockState(world, position.add(1, height + 2, 0), leaves);
        this.setBlockState(world, position.add(-1, height + 2, 0), leaves);
        this.setBlockState(world, position.add(0, height + 2, 1), leaves);
        this.setBlockState(world, position.add(0, height + 2, -1), leaves);

        this.setBlockState(world, position.add(2, height + 2, 0), leaves);
        this.setBlockState(world, position.add(-2, height + 2, 0), leaves);
        this.setBlockState(world, position.add(0, height + 2, 2), leaves);
        this.setBlockState(world, position.add(0, height + 2, -2), leaves);

        this.setBlockState(world, position.add(-2, height + 3, 0), leaves);
        this.setBlockState(world, position.add(-3, height + 3, 0), leaves);
        this.setBlockState(world, position.add(-4, height + 2, 0), leaves);

        this.setBlockState(world, position.add(2, height + 3, 0), leaves);
        this.setBlockState(world, position.add(3, height + 3, 0), leaves);
        this.setBlockState(world, position.add(4, height + 2, 0), leaves);

        this.setBlockState(world, position.add(0, height + 3, 2), leaves);
        this.setBlockState(world, position.add(0, height + 3, 3), leaves);
        this.setBlockState(world, position.add(0, height + 2, 4), leaves);

        this.setBlockState(world, position.add(0, height + 3, -2), leaves);
        this.setBlockState(world, position.add(0, height + 3, -3), leaves);
        this.setBlockState(world, position.add(0, height + 2, -4), leaves);

        this.setBlockState(world, position.add(2, height, 0), leaves);
        this.setBlockState(world, position.add(-2, height, 0), leaves);
        this.setBlockState(world, position.add(0, height, 2), leaves);
        this.setBlockState(world, position.add(0, height, -2), leaves);

        this.setBlockState(world, position.add(3, height - 1, 0), leaves);
        this.setBlockState(world, position.add(-3, height - 1, 0), leaves);
        this.setBlockState(world, position.add(0, height - 1, 3), leaves);
        this.setBlockState(world, position.add(0, height - 1, -3), leaves);
        this.setBlockState(world, position.add(3, height - 2, 0), leaves);
        this.setBlockState(world, position.add(-3, height - 2, 0), leaves);
        this.setBlockState(world, position.add(0, height - 2, 3), leaves);
        this.setBlockState(world, position.add(0, height - 2, -3), leaves);

        if (rand.nextBoolean()) {
            this.setBlockState(world, position.add(3, height - 3, 0), leaves);
            this.setBlockState(world, position.add(-3, height - 3, 0), leaves);
            this.setBlockState(world, position.add(0, height - 3, 3), leaves);
            this.setBlockState(world, position.add(0, height - 3, -3), leaves);
        }

        this.setBlockState(world, position.add(2, height + 1, 2), leaves);
        this.setBlockState(world, position.add(2, height + 2, 2), leaves);
        this.setBlockState(world, position.add(-2, height + 1, 2), leaves);
        this.setBlockState(world, position.add(-2, height + 2, 2), leaves);
        this.setBlockState(world, position.add(2, height + 1, -2), leaves);
        this.setBlockState(world, position.add(2, height + 2, -2), leaves);
        this.setBlockState(world, position.add(-2, height + 1, -2), leaves);
        this.setBlockState(world, position.add(-2, height + 2, -2), leaves);

        this.setBlockState(world, position.add(3, height - 1, 3), leaves);
        this.setBlockState(world, position.add(3, height, 3), leaves);
        this.setBlockState(world, position.add(3, height + 1, 3), leaves);

        this.setBlockState(world, position.add(-3, height - 1, 3), leaves);
        this.setBlockState(world, position.add(-3, height, 3), leaves);
        this.setBlockState(world, position.add(-3, height + 1, 3), leaves);

        this.setBlockState(world, position.add(3, height - 1, -3), leaves);
        this.setBlockState(world, position.add(3, height, -3), leaves);
        this.setBlockState(world, position.add(3, height + 1, -3), leaves);

        this.setBlockState(world, position.add(-3, height - 1, -3), leaves);
        this.setBlockState(world, position.add(-3, height, -3), leaves);
        this.setBlockState(world, position.add(-3, height + 1, -3), leaves);

        return true;
    }

    private void setBlockState(World world, BlockPos pos, IBlockState state) {
        Block block = world.getBlockState(pos).getBlock();
        if (this.canGrowInto(block) || block instanceof AncientLeavesBlock || block instanceof AncientSaplingBlock || block instanceof AncientLogBlock) {
            world.setBlockState(pos, state);
        }
    }
}
