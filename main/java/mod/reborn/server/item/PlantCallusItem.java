package mod.reborn.server.item;

import mod.reborn.server.tab.TabHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import mod.reborn.server.block.plant.DoublePlantBlock;
import mod.reborn.server.block.plant.RBBlockCropsBase;
import mod.reborn.server.plant.Plant;
import mod.reborn.server.plant.PlantHandler;
import mod.reborn.server.util.LangUtils;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class PlantCallusItem extends Item {
    public PlantCallusItem() {
        super();
        this.setCreativeTab(TabHandler.PLANTS);
        this.setHasSubtypes(true);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return LangUtils.translate(this.getUnlocalizedName() + ".name").replace("{plant}", LangUtils.getPlantName(stack.getItemDamage()));
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
    	ItemStack stack = player.getHeldItem(hand);
        if (side == EnumFacing.UP && player.canPlayerEdit(pos.offset(side), side, stack)) {
            if (world.isAirBlock(pos.offset(side)) && world.getBlockState(pos).getBlock() == Blocks.FARMLAND) {
                Plant plant = PlantHandler.getPlantById(stack.getItemDamage());

                if (plant != null) {
                    Block block = plant.getBlock();

                    if (!(block instanceof BlockCrops || block instanceof RBBlockCropsBase)) {
                        world.setBlockState(pos, Blocks.DIRT.getDefaultState());
                    }

                    if (block instanceof DoublePlantBlock) {
                        world.setBlockState(pos.up(), block.getDefaultState().withProperty(DoublePlantBlock.HALF, DoublePlantBlock.BlockHalf.LOWER));
                        world.setBlockState(pos.up(2), block.getDefaultState().withProperty(DoublePlantBlock.HALF, DoublePlantBlock.BlockHalf.UPPER));
                    } else {
                        world.setBlockState(pos.up(), block.getDefaultState());
                    }
                    stack.shrink(1);
                    return EnumActionResult.SUCCESS;
                }
            }
        }

        return EnumActionResult.PASS;
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems) {
        List<Plant> plants = new LinkedList<>(PlantHandler.getPrehistoricPlantsAndTrees());
        Collections.sort(plants);
        if(this.isInCreativeTab(tab))
        for (Plant plant : plants) {
            if (plant.shouldRegister()) {
                subItems.add(new ItemStack(this, 1, PlantHandler.getPlantId(plant)));
            }
        }
    }
}
