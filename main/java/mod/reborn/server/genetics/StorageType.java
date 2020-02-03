package mod.reborn.server.genetics;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.List;

public interface StorageType {
    ItemStack createItem();

    void writeToNBT(NBTTagCompound nbt);

    void readFromNBT(NBTTagCompound nbt);

    void addInformation(ItemStack stack, List<String> tooltip);

    int getMetadata();
}
