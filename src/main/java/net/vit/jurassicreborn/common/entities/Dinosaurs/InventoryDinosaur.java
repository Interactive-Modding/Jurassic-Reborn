package net.vit.jurassicreborn.common.entities.Dinosaurs;

import net.minecraft.world.level.levelgen.RandomSource;
import net.vit.jurassicreborn.common.entities.DinosaurEntity;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.Random;

public class InventoryDinosaur implements Container {
    private DinosaurEntity entity;

    private NonNullList<ItemStack> inventory;
    private ArrayList<Item> ITEMS = new ArrayList<>();

    public InventoryDinosaur(DinosaurEntity entity) {
        this.entity = entity;
        this.inventory = NonNullList.withSize(entity.getDinosaur().getStorage(), ItemStack.EMPTY);
    }

    public void writeToNBT(CompoundTag nbt) {
         ListTag nbttaglist = new ListTag();

        for (int i = 0; i < this.inventory.size(); ++i) {
            CompoundTag slotTag = new CompoundTag();
            slotTag.putByte("Slot", (byte) i);
            this.inventory.get(i).save(slotTag);
            nbttaglist.add(slotTag);
        }

        nbt.put("Items", nbttaglist);
    }

    public void readFromNBT(CompoundTag nbt) {
        ListTag nbttaglist = nbt.getList("Items", 10);
        for (int i = 0; i < nbttaglist.size(); ++i) {
            CompoundTag slotTag = nbttaglist.getCompound(i);
            ItemStack stack = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(slotTag.getString("id"))), (int)slotTag.getByte("Count"), slotTag.contains("tag") ? slotTag.getCompound("tag") : new CompoundTag());
            int j = slotTag.getByte("Slot") & 255;

            if (j >= 0 && j < this.inventory.size()) {
                this.setItem(j, stack);
            }
        }
    }

    @Override
    public int getContainerSize() {
        return this.inventory.size();
    }

    @Override
    public ItemStack getItem(int index) {
        if(!this.inventory.isEmpty() && index < this.inventory.size()) {
            return this.inventory.get(index);
        }else{
            return ItemStack.EMPTY;
        }
    }

    @Override
    public ItemStack removeItem(int index, int count) {
        if (!this.inventory.get(index).isEmpty()) {
            ItemStack itemstack;

            if (this.inventory.get(index).getCount() <= count) {
                itemstack = this.inventory.get(index);
                this.setItem(index, ItemStack.EMPTY);
                return itemstack;
            } else {
                itemstack = this.inventory.get(index).split(count);

                if (this.inventory.get(index).getCount() == 0) {
                    this.setItem(index, ItemStack.EMPTY);
                }

                return itemstack;
            }
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        ItemStack itemstack = this.inventory.get(index);
        this.setItem(index, ItemStack.EMPTY);
        return itemstack;
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        this.inventory.set(index, stack);
        this.ITEMS.add(stack.getItem());

        if (!stack.isEmpty() && stack.getCount() > this.getInventorySize()) {
            stack.setCount(this.getInventorySize());
        }
    }

    public boolean contains(Item item) {
        if(ITEMS.isEmpty()) {
            return false;
        }
        return ITEMS.contains(item);
    }




    public int getInventorySize() {
        return 64;
    }

    @Override
    public void setChanged() {
    }

    @Override
    public boolean stillValid(Player player) {
        return !this.entity.isDeadOrDying() && player.distanceToSqr(this.entity) <= 64.0D;
    }


    public void openInventory(Player player) {
    }


    public void closeInventory(Player player) {
    }


    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return true;
    }



    @Override
    public void clearContent() {
        for (int i = 0; i < this.getContainerSize(); i++) {
            this.setItem(i, ItemStack.EMPTY);
        }
    }


    public String getName() {
        return this.entity.getName().getString();
    }


    public boolean hasCustomName() {
        return this.entity.hasCustomName();
    }


    public Component getDisplayName() {
        return this.entity.getDisplayName();
    }

    public void dropItems(Level world, Random rand) {
        for (int i = 0; i < this.getInventorySize(); ++i) {

            ItemStack itemstack = this.getItem(i);

            if (!itemstack.isEmpty()) {
                float offsetX = rand.nextFloat() * 0.8F + 0.1F;
                float offsetY = rand.nextFloat() * 0.8F + 0.1F;
                float offsetZ = rand.nextFloat() * 0.8F + 0.1F;
                ItemEntity itemEntity = new ItemEntity(world, this.entity.getX() + offsetX, this.entity.getY() + offsetY, this.entity.getZ() + offsetZ, new ItemStack(itemstack.getItem(), itemstack.getCount(), itemstack.getTag()));
                float multiplier = 0.05F;
                itemEntity.setDeltaMovement((float) rand.nextGaussian() * multiplier, (float) rand.nextGaussian() * multiplier + 0.2F,(float) rand.nextGaussian() * multiplier );
                world.addFreshEntity(itemEntity);
                itemstack.shrink(itemstack.getCount());

            }
        }
    }
    public void dropItems(Level world, RandomSource rand) {
        for (int i = 0; i < this.getInventorySize(); ++i) {

            ItemStack itemstack = this.getItem(i);

            if (!itemstack.isEmpty()) {
                float offsetX = rand.nextFloat() * 0.8F + 0.1F;
                float offsetY = rand.nextFloat() * 0.8F + 0.1F;
                float offsetZ = rand.nextFloat() * 0.8F + 0.1F;
                ItemEntity itemEntity = new ItemEntity(world, this.entity.getX() + offsetX, this.entity.getY() + offsetY, this.entity.getZ() + offsetZ, new ItemStack(itemstack.getItem(), itemstack.getCount(), itemstack.getTag()));
                float multiplier = 0.05F;
                itemEntity.setDeltaMovement((float) rand.nextGaussian() * multiplier, (float) rand.nextGaussian() * multiplier + 0.2F,(float) rand.nextGaussian() * multiplier );
                world.addFreshEntity(itemEntity);
                itemstack.shrink(itemstack.getCount());

            }
        }
    }

    public NonNullList<ItemStack> getInventory() {
        return inventory;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}