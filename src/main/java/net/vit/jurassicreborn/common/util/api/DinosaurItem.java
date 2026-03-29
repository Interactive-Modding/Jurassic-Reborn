package net.vit.jurassicreborn.common.util.api;

import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.vit.jurassicreborn.common.util.ItemStackNbtUtil;

public interface DinosaurItem {
    default Dinosaur getDinosaur(ItemStack stack) {
        return Dinosaur.getDinosaurByName(ItemStackNbtUtil.getTag(stack).getString("Dinosaur"));
    }
     static ItemStack setDino(ItemStack pStack, Dinosaur dino){
        CompoundTag tag = ItemStackNbtUtil.getOrCreateTag(pStack);
        tag.putString("Dinosaur", dino.getName());
        ItemStackNbtUtil.setTag(pStack, tag);
        return pStack;
    }
}
