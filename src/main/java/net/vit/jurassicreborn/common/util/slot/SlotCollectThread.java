package net.vit.jurassicreborn.common.util.slot;

import net.minecraft.core.NonNullList;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class SlotCollectThread extends Thread{
    NonNullList<Slot> slots;

    ItemStack[] stacks;

    boolean isDone = false;

    public SlotCollectThread(NonNullList<Slot> slots){
        this.slots = slots;
    }

    @Override
    public void run() {
        super.run();
        stacks = new ItemStack[slots.size()];
        for (int i = 0; i < slots.size(); i++) {
            stacks[i] = slots.get(i).getItem();
        }
        isDone = true;
    }

    public boolean isDone() {
        return isDone;
    }

    public ItemStack[] getStacks() {
        return stacks;
    }
}
