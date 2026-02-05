package net.vit.jurassicreborn.common.blocks;

import net.vit.jurassicreborn.common.items.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;


public class GypsumStoneBlock extends Block {
    public GypsumStoneBlock() {
        super(BlockBehaviour.Properties.of(Material.STONE)
                .strength(2.0F, 2.0F)
                .requiresCorrectToolForDrops()); // Requires correct tool for drops
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        return new ItemStack(ModItems.GYPSUM_POWDER.get());
    }

//    @Override
//    public boolean canHarvestBlock(BlockState state, BlockGetter world, BlockPos pos, Player player) {
//        ItemStack itemstack = player.getMainHandItem();
////        return itemstack.isCorrectToolForDrops(state) && itemstack.getItem().getHarvestLevel(itemstack, net.minecraftforge.common.ToolType.PICKAXE, player, state) >= 1; // Stone level or higher
////    }
}
