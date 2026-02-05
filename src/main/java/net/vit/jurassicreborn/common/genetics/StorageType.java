package net.vit.jurassicreborn.common.genetics;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public interface StorageType {
    ItemStack createItem();

    void saveAdditional(CompoundTag nbt);

    DNA load(CompoundTag nbt);

    void addInformation(ItemStack stack, List<Component> tooltip);

}