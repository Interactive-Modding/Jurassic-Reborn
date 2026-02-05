package net.vit.jurassicreborn.common.items.genetics;

import net.vit.jurassicreborn.common.blocks.ancientplants.AncientCrop;
import net.vit.jurassicreborn.common.genetics.PlantDNA;
import net.vit.jurassicreborn.common.items.ModItems;
import net.vit.jurassicreborn.common.items.TabHandler;
import net.vit.jurassicreborn.common.plants.Plant;
import net.vit.jurassicreborn.common.plants.PlantHandler;
import net.vit.jurassicreborn.common.util.LangUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;

public class PlantCallusItem extends Item {


    public PlantCallusItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public Component getName(ItemStack pStack) {
        return LangUtil.replaceInKey(() -> this.getPlant(pStack).getName(), "{plant}", "item.JurassicReborn.plant_callus");
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        Direction side = context.getClickedFace();
        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        if(world.isClientSide)
            return InteractionResult.SUCCESS;
        if (side == Direction.UP /*&& player.canPlayerEdit(pos.offset(side), side, stack)*/) {
            if (world.getBlockState(pos.relative(side)).isAir() && world.getBlockState(pos).getBlock() == Blocks.FARMLAND) {
                PlantDNA dna = PlantDNA.fromStack(stack);

                if (dna == null) {
                    return InteractionResult.FAIL;
                }
                Plant plant = PlantHandler.getPlantById(dna.getPlant());

                if (plant != null) {
                    Block block = plant.getBlock();

                    if (!(block instanceof CropBlock || block instanceof AncientCrop)) {
                        world.setBlock(pos, Blocks.DIRT.defaultBlockState(), 3);
                    }

                    if (block instanceof DoublePlantBlock) {
                        world.setBlock(pos.above(), block.defaultBlockState().setValue(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER), 3);
                        world.setBlock(pos.above(2), block.defaultBlockState().setValue(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER), 3);
                    } else {
                        world.setBlock(pos.above(), block.defaultBlockState(), 3);
                    }
                    stack.shrink(1);
                    return InteractionResult.SUCCESS;
                }
            }
        }

        return InteractionResult.PASS;
    }


    public Plant getPlant(ItemStack stack){
        PlantDNA dna = PlantDNA.fromStack(stack);

        if(dna == null){
            return PlantHandler.EMPTY;
        }

        return PlantHandler.getPlantById(dna.getPlant());
    }

    @Override
    public void fillItemCategory(CreativeModeTab pCategory, NonNullList<ItemStack> pItems) {
        if(pCategory != TabHandler.PLANTS && pCategory != CreativeModeTab.TAB_SEARCH)
            return;

        for(Plant p : PlantHandler.getPrehistoricPlantsAndTrees()){
            PlantDNA dna = new PlantDNA(p.getId(), 100);
            CompoundTag plantData = new CompoundTag();
            dna.writeToNBT(plantData);
            ItemStack plant = ModItems.PLANT_CALLUS.get().getDefaultInstance();
            plant.setTag(plantData);
            pItems.add(plant);
        }
    }

    @Override
    public ItemStack getDefaultInstance() {
        ItemStack callus = new ItemStack(this);
        CompoundTag plantData = new CompoundTag();
        PlantDNA plantDNA = new PlantDNA(PlantHandler.EMPTY.getId(), 0);
        plantDNA.writeToNBT(plantData);
        callus.setTag(plantData);

        return callus;
    }

    public ItemStack getPlantInstance(PlantDNA dna){
        ItemStack plant = ModItems.PLANT_CALLUS.get().getDefaultInstance();
        CompoundTag plantData = new CompoundTag();
        dna.writeToNBT(plantData);
        plant.setTag(plantData);
        return plant;
    }

    public ItemStack getPlantInstance(Plant p, int quality){
        return getPlantInstance(new PlantDNA(p.getId(), quality));
    }
}
