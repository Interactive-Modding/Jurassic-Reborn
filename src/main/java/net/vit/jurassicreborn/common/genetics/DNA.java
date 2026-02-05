package net.vit.jurassicreborn.common.genetics;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public abstract class DNA {

    protected int quality;
    public DNA(String id, int dnaQuality){

        this.quality = dnaQuality;
    }


    public abstract void writeToNBT(CompoundTag tag);

    public int getDNAQuality(){
        return this.quality;
    }

    public abstract void addInformation(ItemStack stack, List<Component> tooltip);
}
