package net.vit.jurassicreborn.common.genetics;

import net.vit.jurassicreborn.common.items.ModItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import net.vit.jurassicreborn.common.util.ItemStackNbtUtil;

public class DinosaurDNAStorageType implements StorageType {
    private DinoDNA dna;

    @Override
    public ItemStack createItem() {
        if (dna == null) {
            return ItemStack.EMPTY;
        }

        CompoundTag compound = new CompoundTag();
        this.dna.writeToNBT(compound);

        net.neoforged.neoforge.registries.DeferredHolder<net.minecraft.world.item.Item, ? extends net.minecraft.world.item.Item> regObj =
                ModItems.DINOSAUR_DNA.get(dna.getDinosaur());
        if (regObj == null) {
            return ItemStack.EMPTY;
        }

        ItemStack output = new ItemStack(regObj.get(), 1);
        ItemStackNbtUtil.setTag(output, compound);
        return output;
    }

    @Override
    public void saveAdditional(CompoundTag nbt) {
        this.dna.writeToNBT(nbt);
    }

    @Override
    public DNA load(CompoundTag nbt) {
        this.dna = DinoDNA.readFromNBT(nbt);
        return this.dna;
    }

    @Override
    public void addInformation(ItemStack stack, List<Component> tooltip) {
        this.dna.addInformation(stack, tooltip);
    }

    public String getDinoName(){
        return this.dna.getDinoName();
    }
}
