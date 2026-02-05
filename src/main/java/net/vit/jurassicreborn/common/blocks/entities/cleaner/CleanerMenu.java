package net.vit.jurassicreborn.common.blocks.entities.cleaner;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.items.SlotItemHandler;
import net.vit.jurassicreborn.common.blocks.entities.MachineItemStackHandler;
import net.vit.jurassicreborn.common.blocks.entities.ModMenuTypes;
import net.vit.jurassicreborn.common.blocks.inventory.CleanerItemHandler;
import net.vit.jurassicreborn.common.util.api.CleanableItem;
import net.vit.jurassicreborn.common.util.slot.CustomSlot;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class CleanerMenu extends AbstractContainerMenu {


    private Inventory playerInv;

    private CleanerBlockEntity instance;

    private final MachineItemStackHandler cleaningStation;
    private final ContainerData cleaningStationData;

    public static final int INPUT_SLOT = 0;
    public static final int BUCKET_SLOT = 1;
    public static final int OUTPUT_SLOT_END = 5;

    public CleanerMenu(int id, Inventory inventory) {
        this(id, inventory, CleanerItemHandler.instance(), new SimpleContainerData(2));
    }

    public CleanerMenu(int pContainerId, Inventory inventory, MachineItemStackHandler cleaningStation, ContainerData stationData) {

        super(ModMenuTypes.CLEANER.get(), pContainerId);


        this.cleaningStation = cleaningStation;
        this.cleaningStationData = stationData;

        this.addSlot(new SlotItemHandler(cleaningStation, 0, 56, 17));
        this.addSlot(new SlotItemHandler(cleaningStation, 1, 56, 53));

        int slotIncrement = 18;

        this.addSlot(new SlotItemHandler(cleaningStation, 2, 108, 26));
        this.addSlot(new SlotItemHandler(cleaningStation, 3, 108+slotIncrement, 26));
        this.addSlot(new SlotItemHandler(cleaningStation, 4, 108+slotIncrement*2, 26));
        this.addSlot(new SlotItemHandler(cleaningStation, 5, 108, 26+slotIncrement));
        this.addSlot(new SlotItemHandler(cleaningStation, 6, 108+slotIncrement, 26+slotIncrement));
        this.addSlot(new SlotItemHandler(cleaningStation, 7, 108+slotIncrement*2, 26+slotIncrement));



        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for(int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(inventory, k, 8 + k * 18, 142));
        }


        this.addDataSlots(this.cleaningStationData);

        this.playerInv = inventory;

    }

    public int getAmountOfFluid(){
        return this.cleaningStationData.get(0);

//        return 0;
    }
    public boolean isCleaning(){
        if(this.instance != null)
            return this.instance.isCleaning();
        else if(this.cleaningStationData.get(1) != 0){
            return true;
        }
        return false;
    }

    public int getProgress(){
        if(this.instance != null)
            return (int) Math.ceil((this.instance.getProgress() * ( 22.0 / 200 ) ));/*or 24 idk which but if it doesn't work that's why lol*/
        else{
            return (int) Math.ceil((this.cleaningStationData.get(1) * ( 22.0 / 200 ) ));
        }
    }


    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(pIndex);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (pIndex < this.cleaningStation.getSlots()) {
                if (!this.moveItemStackTo(itemstack1, this.cleaningStation.getSlots(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 0, this.cleaningStation.getSlots(), false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return itemstack;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return true;
    }

    public void setInstance(CleanerBlockEntity entity){
        this.instance = entity;
    }


    public static class EmptyFluidSlot extends Slot{

        public EmptyFluidSlot(Container pContainer, int pSlot, int pX, int pY) {
            super(pContainer, pSlot, pX, pY);
        }

        @Override
        public boolean mayPlace(ItemStack pStack) {
            return false;
        }


    }
}
