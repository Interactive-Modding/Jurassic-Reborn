package net.vit.jurassicreborn.mixin.accessors;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(CreativeModeTab.class)
public interface CreativeModeTabAccessor {

    @Invoker("fillItemList")
    void jurassicreborn$callFillItemList(NonNullList<ItemStack> items);
}
