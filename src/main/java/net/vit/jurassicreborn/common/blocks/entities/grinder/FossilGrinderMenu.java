package net.vit.jurassicreborn.common.blocks.entities.grinder;

import net.minecraftforge.items.SlotItemHandler;
import net.vit.jurassicreborn.common.blocks.entities.MachineItemStackHandler;
import net.vit.jurassicreborn.common.blocks.entities.ModMenuTypes;
import net.vit.jurassicreborn.common.blocks.inventory.FossilGrinderItemHandler;
import net.vit.jurassicreborn.common.util.api.GrindableItem;
import net.vit.jurassicreborn.common.util.slot.CustomSlot;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class FossilGrinderMenu extends AbstractContainerMenu {

    private final MachineItemStackHandler fossilGrinder;

    private final ContainerData data;

    public FossilGrinderMenu(int pContainerId, Inventory playerInv) {
        this(pContainerId, playerInv, FossilGrinderItemHandler.instance(), new SimpleContainerData(1));
    }


    public FossilGrinderMenu(int pContainerId, Inventory playerInventory, MachineItemStackHandler fossilGrinder, ContainerData grinderData) {
        super(ModMenuTypes.FOSSIL_GRINDER.get(), pContainerId);

        this.fossilGrinder = fossilGrinder;
        this.data = grinderData;


        this.addSlot(new SlotItemHandler(fossilGrinder, 0, 23, 26));
        this.addSlot(new SlotItemHandler(fossilGrinder, 3, 23, 44));
        this.addSlot(new SlotItemHandler(fossilGrinder, 1, 41, 26));
        this.addSlot(new SlotItemHandler(fossilGrinder, 4, 41, 44));
        this.addSlot(new SlotItemHandler(fossilGrinder, 2, 59, 26));
        this.addSlot(new SlotItemHandler(fossilGrinder, 5, 59, 44));

        this.addSlot(new SlotItemHandler(fossilGrinder, 6, 108, 26));
        this.addSlot(new SlotItemHandler(fossilGrinder, 9, 108, 44));
        this.addSlot(new SlotItemHandler(fossilGrinder, 7, 126, 26));
        this.addSlot(new SlotItemHandler(fossilGrinder, 10, 126, 44));
        this.addSlot(new SlotItemHandler(fossilGrinder, 8, 144, 26));
        this.addSlot(new SlotItemHandler(fossilGrinder, 11, 144, 44));

        for (int row = 0; row < 3; ++row) {
            for (int column = 0; column < 9; ++column) {
                this.addSlot(new Slot(playerInventory, column + row * 9 + 9, 8 + column * 18, 84 + row * 18));
            }
        }

        for (int column = 0; column < 9; ++column) {
            this.addSlot(new Slot(playerInventory, column, 8 + column * 18, 142));
        }

        this.addDataSlots(grinderData);
    }

    public int getField(int index){
        return this.data.get(index);
    }
    public void setField(int index, int value){
        this.data.set(index, value);
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int slotIndex) {
        ItemStack transferred = ItemStack.EMPTY;
        Slot slot = this.slots.get(slotIndex);

        int otherSlots = this.slots.size() - 36;

        if (slot.hasItem()) {
            ItemStack current = slot.getItem();
            transferred = current.copy();

            if (slotIndex < otherSlots) {
                if (!this.moveItemStackTo(current, otherSlots, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(current, 0, otherSlots, false)) {
                return ItemStack.EMPTY;
            }

            if (current.getCount() == 0) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return transferred;
    }

    @Override
    public boolean stillValid(@NotNull Player pPlayer) {
        return true;//todo check block
    }



    public static class GrindableItemSlot extends Slot {
        public GrindableItemSlot(Container inventory, int slotIndex, int xPosition, int yPosition) {
            super(inventory, slotIndex, xPosition, yPosition);
        }


        @Override
        public boolean mayPlace(@NotNull ItemStack stack) {
            GrindableItem grindableItem = GrindableItem.getGrindableItem(stack);
            return grindableItem != null && grindableItem.isGrindable(stack);
        }

    }
}
