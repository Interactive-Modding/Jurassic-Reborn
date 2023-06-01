package mod.reborn.server.world.tree;

import mod.reborn.server.block.BlockHandler;
import mod.reborn.server.block.tree.AncientLeavesBlock;
import mod.reborn.server.block.tree.AncientLogBlock;
import mod.reborn.server.block.tree.AncientSaplingBlock;
import mod.reborn.server.block.tree.TreeType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

import java.util.Random;

public class CalamitesTreeGenerator extends WorldGenAbstractTree {
    public CalamitesTreeGenerator() {
        super(true);
    }

    @Override
    public boolean generate(World world, Random rand, BlockPos position) {
        IBlockState log = BlockHandler.ANCIENT_LOGS.get(TreeType.CALAMITES).getDefaultState();
        IBlockState leaves = BlockHandler.ANCIENT_LEAVES.get(TreeType.CALAMITES).getDefaultState();

        int height = rand.nextInt(16) + 4;

        for (int y = 0; y < height; y++) {
            BlockPos logPos = position.up(y);
            this.setBlockState(world, logPos, log);

            int branchLength = Math.max(1, (height - y) / 3);

            if (y >= 2) {
                for (int x = -1; x <= 1; x++) {
                    for (int z = -1; z <= 1; z++) {
                        if (x != 0 || z != 0) {
                            this.setBlockState(world, logPos.add(x, 0, z), leaves);
                        }
                    }
                }

                int bushSize = (int) (branchLength * 0.8);

                for (int x = -bushSize; x <= bushSize; x++) {
                    for (int z = -bushSize; z <= bushSize; z++) {
                        if ((x != 0 || z != 0) && Math.sqrt(x * x + z * z) < bushSize) {
                            this.setBlockState(world, logPos.add(x, 0, z), leaves);
                        }
                    }
                }
            }

            if (y % 3 == 2) {
                for (int face = 0; face < 4; face++) {
                    EnumFacing facing = EnumFacing.getHorizontal(face);
                    BlockPos branchPos = logPos.offset(facing);
                    IBlockState facingLog = log.withProperty(AncientLogBlock.LOG_AXIS, BlockLog.EnumAxis.fromFacingAxis(facing.getAxis()));

                    this.setBlockState(world, branchPos, facingLog);

                    this.setBlockState(world, branchPos.up(2), leaves);
                    this.setBlockState(world, branchPos.down(), leaves);
                    this.setBlockState(world, branchPos.offset(facing.rotateY(), 2), leaves);
                    this.setBlockState(world, branchPos.offset(facing.rotateYCCW(), 2), leaves);

                    for (int i = 0; i < branchLength; i++) {
                        BlockPos pos = branchPos.offset(facing, i + 1).up(i / 2 + 1);

                        this.setBlockState(world, pos, facingLog);
                        this.setBlockState(world, pos.up(), leaves);
                        this.setBlockState(world, pos.down(), leaves);
                        this.setBlockState(world, pos.offset(facing.rotateY()), leaves);
                        this.setBlockState(world, pos.offset(facing.rotateYCCW()), leaves);

                        if (i >= branchLength - 1) {
                            this.setBlockState(world, pos.offset(facing), leaves);
                        }
                    }
                }
            }
        }

        this.setBlockState(world, position.up(height), leaves);
        this.setBlockState(world, position.up(height).north(), leaves);
        this.setBlockState(world, position.up(height).south(), leaves);
        this.setBlockState(world, position.up(height).west(), leaves);
        this.setBlockState(world, position.up(height).east(), leaves);
        this.setBlockState(world, position.up(height + 1), leaves);

        return true;
    }

    private void setBlockState(World world, BlockPos pos, IBlockState state) {
        Block block = world.getBlockState(pos).getBlock();
        if (this.canGrowInto(block) || block instanceof AncientLeavesBlock || block instanceof AncientSaplingBlock || block instanceof AncientLogBlock) {
            world.setBlockState(pos, state);
        }
    }
}