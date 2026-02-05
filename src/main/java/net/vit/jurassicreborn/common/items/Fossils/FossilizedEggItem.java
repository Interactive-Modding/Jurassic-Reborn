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
import net.vit.jurassicreborn.common.items.TabHandler;
import net.vit.jurassicreborn.common.util.api.GrindableItem;

import java.util.List;
import java.util.Random;

import net.vit.jurassicreborn.common.items.ModItems;

public class FossilizedEggItem extends Item implements GrindableItem {

    public FossilizedEggItem() {
        super(new Item.Properties().tab(TabHandler.FOSSILS));
    }


    @Override
    public boolean isGrindable(ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack getGroundItem(ItemStack stack, Random random) {
        CompoundTag tag = stack.getTag();

        int outputType = random.nextInt(3);

        if (outputType == 0) {
            List<Dinosaur> dinosaurs = DinosaurHandler.getDinosaursFromAmber();
            dinosaurs.removeIf(dino -> dino.isMammal() || dino.isHybrid());

            Dinosaur selected = dinosaurs.get(random.nextInt(dinosaurs.size()));
            ItemStack tissue = new ItemStack(ModItems.SOFT_TISSUE.get(selected).get());
            tissue.setTag(tag);
            return tissue;
        } else if (outputType == 1) {
            return new ItemStack(Items.BONE_MEAL);
        }

        return new ItemStack(Items.FLINT);
    }

    @Override
    public List<Pair<Float, ItemStack>> getChancedOutputs(ItemStack inputItem) {
        List<Pair<Float, ItemStack>> list = Lists.newArrayList();
        CompoundTag tag = inputItem.getTag();
        List<Dinosaur> dinosaurs = DinosaurHandler.getDinosaursFromAmber();
        float single = 100F / 3F;
        float dinoSingle = single / dinosaurs.size();

        for (Dinosaur dino : dinosaurs) {
            net.minecraftforge.registries.RegistryObject<? extends Item> regObj = ModItems.SOFT_TISSUE.get(dino);
            if (regObj != null) {
                ItemStack output = new ItemStack(regObj.get());
                output.setTag(tag);
                list.add(Pair.of(dinoSingle, output));
            }
        }

        list.add(Pair.of(single, new ItemStack(Items.BONE_MEAL)));
        list.add(Pair.of(single, new ItemStack(Items.FLINT)));

        return list;
    }
}
