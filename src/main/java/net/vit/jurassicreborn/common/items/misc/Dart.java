package net.vit.jurassicreborn.common.items.misc;

import net.vit.jurassicreborn.common.entities.DinosaurEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.function.BiConsumer;

public class Dart extends Item {
    private final BiConsumer<DinosaurEntity, ItemStack> consumer;
    private final int dartColor;

    public Dart(BiConsumer<DinosaurEntity, ItemStack> consumer, int dartColor) {
        super(new Item.Properties());
        this.consumer = consumer;
        this.dartColor = dartColor;
    }

    public int getDartColor(ItemStack stack) {
        return dartColor;
    }

    public BiConsumer<DinosaurEntity, ItemStack> getConsumer() {
        return consumer;
    }
}
