package mod.reborn.server.item.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import mod.reborn.server.block.tree.AncientDoubleSlabBlock;
import mod.reborn.server.block.tree.AncientSlabBlock;
import mod.reborn.server.block.tree.AncientSlabHalfBlock;

public class AncientSlabItemBlock extends ItemBlock {
    private final BlockSlab singleSlab;
    private final BlockSlab doubleSlab;

    public AncientSlabItemBlock(Block block, AncientSlabHalfBlock singleSlab, AncientDoubleSlabBlock doubleSlab) {
        super(block);
        this.singleSlab = singleSlab;
        this.doubleSlab = doubleSlab;
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    @Override
    public int getMetadata(int damage) {
        return damage;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return this.singleSlab.getUnlocalizedName(stack.getMetadata());
    }

    
    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand,
    		EnumFacing facing, float hitX, float hitY, float hitZ) {
    	ItemStack stack = player.getHeldItem(hand);
        if (stack.getCount() != 0 && player.canPlayerEdit(pos.offset(facing), facing, stack)) {
            IBlockState state = world.getBlockState(pos);
            if (state.getBlock() == this.singleSlab) {
                AncientSlabBlock.EnumBlockHalf half = state.getValue(BlockSlab.HALF);

                if ((facing == EnumFacing.UP && half == BlockSlab.EnumBlockHalf.BOTTOM || facing == EnumFacing.DOWN && half == BlockSlab.EnumBlockHalf.TOP)) {
                    AxisAlignedBB collisionBox = state.getSelectedBoundingBox(world, pos);
                    IBlockState doubleSlabState = this.doubleSlab.getDefaultState();

                    if (collisionBox != Block.NULL_AABB && world.checkNoEntityCollision(collisionBox.offset(pos)) && world.setBlockState(pos, doubleSlabState, 11)) {
                        SoundType sound = this.doubleSlab.getSoundType();
                        world.playSound(player, pos, sound.getPlaceSound(), SoundCategory.BLOCKS, (sound.getVolume() + 1.0F) / 2.0F, sound.getPitch() * 0.8F);
                        stack.shrink(1);
                    }

                    return EnumActionResult.SUCCESS;
                }
            }

            return this.tryPlace(player, stack, world, pos.offset(facing)) ? EnumActionResult.SUCCESS : super.onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);
        } else {
            return EnumActionResult.FAIL;
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean canPlaceBlockOnSide(World world, BlockPos pos, EnumFacing side, EntityPlayer player, ItemStack stack) {
        BlockPos placePos = pos;
        IBlockState state = world.getBlockState(pos);

        if (state.getBlock() == this.singleSlab) {
            boolean flag = state.getValue(BlockSlab.HALF) == BlockSlab.EnumBlockHalf.TOP;

            if ((side == EnumFacing.UP && !flag || side == EnumFacing.DOWN && flag)) {
                return true;
            }
        }

        pos = pos.offset(side);
        IBlockState iblockstate1 = world.getBlockState(pos);
        return iblockstate1.getBlock() == this.singleSlab || super.canPlaceBlockOnSide(world, placePos, side, player, stack);
    }

    private boolean tryPlace(EntityPlayer player, ItemStack stack, World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);

        if (state.getBlock() == this.singleSlab) {
            AxisAlignedBB collisionBounds = state.getSelectedBoundingBox(world, pos);

            if (collisionBounds != Block.NULL_AABB && world.checkNoEntityCollision(collisionBounds.offset(pos)) && world.setBlockState(pos, state, 11)) {
                SoundType soundtype = this.doubleSlab.getSoundType();
                world.playSound(player, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
                stack.shrink(1);;
            }

            return true;
        }

        return false;
    }
}
