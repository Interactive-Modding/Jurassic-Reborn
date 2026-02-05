package net.vit.jurassicreborn.common.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;

public class FossilUtil {

    public static final String BLOCK_ENTITY_TAG = "BlockEntityTag";
    public static final String DINO_KEY = "Dinosaur";

    public static ItemStack setDino(ItemStack stack, Dinosaur dino) {
        CompoundTag tag = stack.getOrCreateTag();
        CompoundTag blockEntityTag = tag.getCompound(BLOCK_ENTITY_TAG);
        blockEntityTag.putString(DINO_KEY, dino.getName());
        tag.put(BLOCK_ENTITY_TAG, blockEntityTag);
        stack.setTag(tag);
        return stack;
    }

    public static Dinosaur getDino(ItemStack stack) {
        if (stack.getTag() != null && stack.getTag().contains(BLOCK_ENTITY_TAG)) {
            CompoundTag blockEntityTag = stack.getTag().getCompound(BLOCK_ENTITY_TAG);
            String dinoName = blockEntityTag.getString(DINO_KEY);
            return Dinosaur.getDinosaurByName(dinoName);
        }
        return Dinosaur.EMPTY;
    }

    public static boolean hasDino(ItemStack stack) {
        return !getDino(stack).equals(Dinosaur.EMPTY);
    }
}
