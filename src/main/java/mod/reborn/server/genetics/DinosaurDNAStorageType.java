package mod.reborn.server.genetics;

import mod.reborn.server.item.ItemHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.List;

public class DinosaurDNAStorageType implements StorageType {
    private DinoDNA dna;

    @Override
    public ItemStack createItem() {
        ItemStack output = new ItemStack(ItemHandler.DNA, 1, this.getMetadata());
        NBTTagCompound compound = new NBTTagCompound();
        this.dna.writeToNBT(compound);
        output.setTagCompound(compound);
        return output;
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        this.dna.writeToNBT(nbt);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        this.dna = DinoDNA.readFromNBT(nbt);
    }

    @Override
    public void addInformation(ItemStack stack, List<String> tooltip) {
        this.dna.addInformation(stack, tooltip);
    }

    @Override
    public int getMetadata() {
        return this.dna.getMetadata();
    }
}
