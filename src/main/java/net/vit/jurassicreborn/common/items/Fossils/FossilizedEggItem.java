package net.vit.jurassicreborn.common.items.Fossils;

import com.google.common.collect.Lists;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.*;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;
import net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurHandler;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.vit.jurassicreborn.common.util.ItemStackNbtUtil;
import net.vit.jurassicreborn.common.util.api.GrindableItem;

import java.util.List;
import java.util.Random;

import net.vit.jurassicreborn.common.items.ModItems;

public class FossilizedEggItem extends Item implements GrindableItem {

    public FossilizedEggItem(Item.Properties properties) {
        super(properties);
    }


    @Override
    public boolean isGrindable(ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack getGroundItem(ItemStack stack, Random random) {
        CompoundTag tag = ItemStackNbtUtil.getTag(stack);

        int outputType = random.nextInt(3);

        if (outputType == 0) {
            List<Dinosaur> dinosaurs = DinosaurHandler.getDinosaursFromAmber();
            dinosaurs.removeIf(dino -> dino.isMammal() || dino.isHybrid());

            Dinosaur selected = dinosaurs.get(random.nextInt(dinosaurs.size()));
            ItemStack tissue = new ItemStack(ModItems.SOFT_TISSUE.get(selected).get());
            ItemStackNbtUtil.setTag(tissue, tag);
            return tissue;
        } else if (outputType == 1) {
            return new ItemStack(Items.BONE_MEAL);
        }

        return new ItemStack(Items.FLINT);
    }

    @Override
    public List<Pair<Float, ItemStack>> getChancedOutputs(ItemStack inputItem) {
        List<Pair<Float, ItemStack>> list = Lists.newArrayList();
        CompoundTag tag = ItemStackNbtUtil.getTag(inputItem);
        List<Dinosaur> dinosaurs = DinosaurHandler.getDinosaursFromAmber();
        float single = 100F / 3F;
        float dinoSingle = single / dinosaurs.size();

        for (Dinosaur dino : dinosaurs) {
            net.neoforged.neoforge.registries.DeferredHolder<Item, ? extends Item> regObj = ModItems.SOFT_TISSUE.get(dino);
            if (regObj != null) {
                ItemStack output = new ItemStack(regObj.get());
                ItemStackNbtUtil.setTag(output, tag);
                list.add(Pair.of(dinoSingle, output));
            }
        }

        list.add(Pair.of(single, new ItemStack(Items.BONE_MEAL)));
        list.add(Pair.of(single, new ItemStack(Items.FLINT)));

        return list;
    }
}
