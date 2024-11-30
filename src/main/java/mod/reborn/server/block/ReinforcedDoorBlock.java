package mod.reborn.server.block;

import mod.reborn.server.item.ItemHandler;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class ReinforcedDoorBlock extends BlockDoor {

    public ReinforcedDoorBlock() {
        super(Material.IRON);
        this.setHardness(3.0F);
        this.setResistance(5.0F);
        this.setSoundType(SoundType.STONE);
        this.setUnlocalizedName("reinforced_door");
        this.setHarvestLevel("pickaxe", 2);

    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        if (state.getValue(HALF) == EnumDoorHalf.UPPER) {
            return null;
        } else {
            return this.getItem();
        }
    }

    @Override
    public ItemStack getItem(World world, BlockPos pos, IBlockState state) {
        return new ItemStack(this.getItem());
    }

    private Item getItem() {
        return ItemHandler.REINFORCED_DOOR_ITEM;
    }

    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }
}