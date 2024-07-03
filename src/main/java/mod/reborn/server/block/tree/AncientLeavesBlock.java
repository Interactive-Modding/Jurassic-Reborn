package mod.reborn.server.block.tree;

import com.google.common.collect.Lists;
import mod.reborn.server.api.SubBlocksBlock;
import mod.reborn.server.block.BlockHandler;
import mod.reborn.server.item.block.AncientItemLeaves;
import mod.reborn.server.tab.TabHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class AncientLeavesBlock extends BlockLeaves implements SubBlocksBlock {
    private TreeType treeType;

    public AncientLeavesBlock(TreeType type) {
        super();
        this.treeType = type;
        this.setUnlocalizedName(type.name().toLowerCase(Locale.ENGLISH) + "_leaves");
        this.setHardness(0.2F);
        this.setLightOpacity(1);
        this.setSoundType(SoundType.PLANT);
        this.setDefaultState(this.blockState.getBaseState().withProperty(CHECK_DECAY, true).withProperty(DECAYABLE, true));
        this.setCreativeTab(TabHandler.PLANTS);
    }

    public TreeType getTreeType() {
        return this.treeType;
    }

    protected int[] surroundings;

    /*
     * Here the leaf-decaying algorithm will be executed. The word 'log' is used as a replacement for all blocks which sustain leaf blocks. The
     * 'current leaf' is the leaf block for which the updateTick function is executed.
     */
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (!worldIn.isRemote) {
            if (state.getValue(CHECK_DECAY).booleanValue() && state.getValue(DECAYABLE).booleanValue()) {

                int maxBlocks = 7; // The maximum distance a log can be away from the leaf

                // The coords of the current leaf block
                int posX = pos.getX();
                int posY = pos.getY();
                int posZ = pos.getZ();

                /*
                 * The blocks around this leaf block are stored in an 3D array for faster and easier access. For performance reasons this 3D array
                 * isn't a multidimensional one but a flat (1D) array, which means that the x, y, and z coordinates of the blocks represented have to
                 * be computed into an index of the 1D array. Therefore some properties of the 3D array have to be stored. The 3D array represents a cube, the center of this cube is the current leaf block.
                 */
                int cubeLength = 32; // The side length of the cube. It's 32, the vanilla value. If maxBlocks is larger than 15, it has to be extended.
                int cubeLengthSq = cubeLength * cubeLength; // The squared cube length. For performance reasons, that this hasn't to be computed every time
                int halfCubeLength = cubeLength / 2; // For performance reasons

                if (this.surroundings == null) {
                    this.surroundings = new int[cubeLength * cubeLength * cubeLength]; // The array will be created once for performance reasons (and thus it's stored as a field)
                }

                if (!worldIn.isAreaLoaded(pos, 1)) return; // Forge: prevent decaying leaves from updating neighbors and loading unloaded chunks
                if (worldIn.isAreaLoaded(pos, maxBlocks+2)) // Forge: extend range from 5 to 6 to account for neighbor checks in world.markAndNotifyBlock -> world.updateObservingBlocksAt
                {
                    BlockPos.MutableBlockPos tempBlockPos = new BlockPos.MutableBlockPos(); // For performance reasons, objects can be reused

                    // Get the blocks around the current leaf block up to the maximum range
                    for (int offsetX = -maxBlocks; offsetX <= maxBlocks; ++offsetX) {
                        for (int offsetY = -maxBlocks; offsetY <= maxBlocks; ++offsetY) {
                            for (int offsetZ = -maxBlocks; offsetZ <= maxBlocks; ++offsetZ) {
                                IBlockState iblockstate = worldIn.getBlockState(tempBlockPos.setPos(posX + offsetX, posY + offsetY, posZ + offsetZ));
                                Block block = iblockstate.getBlock();

                                // Compute the index of the 1D array. The coords also are translated to make the current leaf block the center of the cube.
                                int index = (offsetX + halfCubeLength) * cubeLengthSq + (offsetY + halfCubeLength) * cubeLength + offsetZ + halfCubeLength;

                                // Store the surrounding blocks in the array
                                if (BlockHandler.ANCIENT_LOGS.values().contains(block) && ((AncientLogBlock) block).getType() == this.getTreeType()) { // Check if the block is a JC log (non-vanilla behavior) and corresponds to the current leaf type
                                    this.surroundings[index] = 0; // A log
                                } else {
                                    if (this == block) { // Non-Vanilla behavior: Only leaves of the same kind (same block instance) are relevant
                                        this.surroundings[index] = -2; // A leaf block
                                    } else {
                                        this.surroundings[index] = -1; // Every other block
                                    }
                                }
                            }
                        }
                    }

                    /*
                     * At the first iteration, the algorithm looks for log blocks (around the current leaf block) and, if it's surroundings
                     * (orthogonal and diagonal ones) are a leaf block, marks them with a number indicating the distance to the log block (a 1 for the
                     * first iteration). So all blocks marked with a 1 thus are leaf blocks which are next to a log block. For the second iteration the
                     * surroundings of all blocks marked with a 1 will be checked (whether they're a leaf block or not). If they're a leaf block, they're
                     * marked with a 2. For the next iterations the same thing will be done with all blocks marked with a 2, and so on, until
                     * maxBlocks. At the end it's known whether every leaf block around the center one is connected to a log, because then the number
                     * in the array is greater equal zero.
                     */

                    // Iterate as much times as maxBlocks is large
                    for (int i3 = 1; i3 <= maxBlocks; ++i3) {

                        // Iterate through the surroundings of the current leaf block
                        for (int offsetX = -maxBlocks; offsetX <= maxBlocks; ++offsetX) {
                            for (int offsetY = -maxBlocks; offsetY <= maxBlocks; ++offsetY) {
                                for (int offsetZ = -maxBlocks; offsetZ <= maxBlocks; ++offsetZ) {

                                    // If the block is a log or connected to one (so far)
                                    if (this.surroundings[(offsetX + halfCubeLength) * cubeLengthSq + (offsetY + halfCubeLength) * cubeLength + offsetZ
                                            + halfCubeLength] == i3 - 1) {

                                        // Get all direct (orthogonal and diagonal) surroundings of the current block
                                        for (int dirX = -1; dirX <= 1; dirX++) {
                                            for (int dirY = -1; dirY <= 1; dirY++) {
                                                for (int dirZ = -1; dirZ <= 1; dirZ++) {

                                                    // If all three numbers are zero, the current block would be checker, but it mustn't be
                                                    if (dirX != 0 || dirY != 0 || dirZ != 0) {

                                                        int index = (offsetX + halfCubeLength + dirX) * cubeLengthSq
                                                                + (offsetY + halfCubeLength + dirY) * cubeLength + offsetZ + dirZ + halfCubeLength;
                                                        if (this.surroundings[index] == -2) {// If the surroundings are a leaf block
                                                            this.surroundings[index] = i3; // Store the distance to the log
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Check whether the current leaf block is connected to a log (the >= 0 is vanilla behavior)
                if (this.surroundings[halfCubeLength * cubeLengthSq + halfCubeLength * cubeLength + halfCubeLength] >= 0) {
                    // The leaf is connected to a log, and won't decay
                    worldIn.setBlockState(pos, state.withProperty(CHECK_DECAY, Boolean.valueOf(false)), 4); // Don't check anymore whether the current leaf block can decay. New block updates will force these checks again. 
                } else {
                    // The leaf block isn't connected to a log
                    this.dropBlockAsItem(worldIn, pos, worldIn.getBlockState(pos), 0); // Drop the drops
                    worldIn.setBlockToAir(pos); // Decay
                }
            }
        }
    }

    /*
     * The following functions have to be overridden, because setGraphicsLevel cannot be called for this block without hacking into the MC code. These
     * ensure that the fast/fancy graphic setting is applied correctly. <<<START>>>
     */

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return Blocks.LEAVES.isOpaqueCube(state);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return Blocks.LEAVES.getBlockLayer();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
        return Blocks.LEAVES.shouldSideBeRendered(state, world, pos, side);
    }

    /*
     * <<<END>>>
     */

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        List<ItemStack> drops = new ArrayList<>();
        Random rand = world instanceof World ? ((World) world).rand : new Random();
        int chance = this.treeType.getDropChance();

        if (fortune > 0) {
            chance -= 2 << fortune;
            if (chance < 2) {
                chance = 2;
            }
        }

        if (rand.nextInt(chance) == 0) {
            ItemStack drop = this.treeType.getDrop();
            drops.add(new ItemStack(drop.getItem(), drop.getCount()));
        }

        this.captureDrops(true);
        drops.addAll(this.captureDrops(false));
        return drops;
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return this.treeType.getDrop().getItem();
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        /*
         * For compatibility reasons, we've to account for the old metadata
         * 
         * 0: DECAYABLE and CHECK_DECAY
         * 4: CHECK_DECAY
         * 8: DECAYABLE or none (we assume none)
         */
        
        return this.getDefaultState().withProperty(DECAYABLE, meta == 0 || meta == 1).withProperty(CHECK_DECAY, meta == 0 || meta == 4);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        // Because of compatibility reasons we've to create the metadata that ugly way
        boolean decayable = state.getValue(DECAYABLE);
        boolean checkDecay = state.getValue(CHECK_DECAY);
        
        if(decayable && checkDecay) {
            return 0;
        }else if(decayable) {
            return 1;
        }else if(checkDecay) {
            return 4;
        }else {
            return 8;
        }
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, CHECK_DECAY, DECAYABLE);
    }

    @Override
    public int damageDropped(IBlockState state) {
        return 0;
    }

    @Override
    public List<ItemStack> onSheared(ItemStack item, IBlockAccess world, BlockPos pos, int fortune) {
        return Lists.newArrayList(new ItemStack(this, 1, 0));
    }

    @Override
    public BlockPlanks.EnumType getWoodType(int meta) {
        return BlockPlanks.EnumType.BIRCH; // TODO: This is because we extend BlockLeaves. Probably in 1.13 we don't have this anymore
    }

    @Override
    public ItemBlock getItemBlock() {
        return new AncientItemLeaves(this);
    }
}

