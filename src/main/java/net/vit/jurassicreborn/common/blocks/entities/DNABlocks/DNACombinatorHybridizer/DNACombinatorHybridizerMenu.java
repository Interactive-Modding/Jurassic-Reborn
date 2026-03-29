package net.vit.jurassicreborn.common.blocks.entities.DNABlocks.DNACombinatorHybridizer;

import net.neoforged.neoforge.items.SlotItemHandler;
import net.vit.jurassicreborn.common.blocks.entities.MachineItemStackHandler;
import net.vit.jurassicreborn.common.blocks.entities.ModMenuTypes;
import net.vit.jurassicreborn.common.blocks.inventory.DNACombinatorHybridizerItemHandler;
import net.vit.jurassicreborn.common.util.slot.CustomSlot;
import net.vit.jurassicreborn.common.util.slot.StorageSlot;
import net.vit.jurassicreborn.common.util.slot.ToggleableSlot;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DNACombinatorHybridizerMenu extends AbstractContainerMenu {

    private final DNACombinatorHybridizerItemHandler owner;
    private final ContainerData ownerData;

    private final Inventory playerInventory;

    private final List<Slot> combinatorSlots = new ArrayList<>();
    private final List<Slot> hybridizerSlots = new ArrayList<>();

    public static int HYBRIDIZER_INPUT_Y = 17;

    public static int HYBRIDIZER_OUTPUT_Y = 52;

    public static int COMBINATOR_INPUT_Y = 13;

    public static int COMBINATOR_OUTPUT_Y = 60;

    public DNACombinatorHybridizerMenu(int pContainerId, Inventory playerInv) {
        this(pContainerId, playerInv, new DNACombinatorHybridizerItemHandler(DNACombinatorHybridizerBlockEntity.SLOTS,new int[]{0,1,2,3,4,5,6,7,8,9},new int[]{10,11}), new SimpleContainerData(6));
    }

    public DNACombinatorHybridizerMenu(int pContainerId, Inventory playerInv, DNACombinatorHybridizerItemHandler hybridizer, ContainerData hybridizerData){
        super(ModMenuTypes.COMBINATOR.get(), pContainerId);

        this.owner = hybridizer;
        this.ownerData = hybridizerData;
        boolean mode = ownerData.get(2) == 1;

        //todo active?

        this.hybridizerSlots.add(this.addSlot(new ToggleableSlotItemHandler(this.owner, 0, 10, 17)));
        this.hybridizerSlots.add(this.addSlot(new ToggleableSlotItemHandler(this.owner, 1, 30, 17)));
        this.hybridizerSlots.add(this.addSlot(new ToggleableSlotItemHandler(this.owner, 2, 50, 17)));
        this.hybridizerSlots.add(this.addSlot(new ToggleableSlotItemHandler(this.owner, 3, 70, 17)));
        this.hybridizerSlots.add(this.addSlot(new ToggleableSlotItemHandler(this.owner, 4, 90, 17)));
        this.hybridizerSlots.add(this.addSlot(new ToggleableSlotItemHandler(this.owner, 5, 110, 17)));
        this.hybridizerSlots.add(this.addSlot(new ToggleableSlotItemHandler(this.owner, 6, 130, 17)));
        this.hybridizerSlots.add(this.addSlot(new ToggleableSlotItemHandler(this.owner, 7, 150, 17)));
        this.hybridizerSlots.add(this.addSlot(new ToggleableSlotItemHandler(this.owner, 10, 80, 56)));
        this.combinatorSlots.add(this.addSlot(new ToggleableSlotItemHandler(this.owner, 8, 55, 13)));
        this.combinatorSlots.add(this.addSlot(new ToggleableSlotItemHandler(this.owner, 9, 105, 13)));
        this.combinatorSlots.add(this.addSlot(new ToggleableSlotItemHandler(this.owner, 11, 81, 60)));



        this.updateSlots(!mode);


        int i;

        for (i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInv, i, 8 + i * 18, 142));
        }

        this.addDataSlots(this.ownerData);

        this.playerInventory = playerInv;




    }




    public List<ToggleableSlotItemHandler> getHybridizerCombinatorSlots(boolean mode){
        List<Slot> source = mode ? this.combinatorSlots : this.hybridizerSlots;
        return source.stream()
                .filter(ToggleableSlotItemHandler.class::isInstance)
                .map(ToggleableSlotItemHandler.class::cast)
                .collect(Collectors.toList());
    }

    public List<Slot> getHybridizerSlots(){
        return this.hybridizerSlots;
    }

    public List<Slot> getCombinatorSlots() {
        return this.combinatorSlots;
    }




    public void updateSlots(boolean mode) {
//        this.slots.clear();

        List<ToggleableSlotItemHandler> slotsForMode = this.getHybridizerCombinatorSlots(mode);
        List<ToggleableSlotItemHandler> otherSlots = this.getHybridizerCombinatorSlots(!mode);

        slotsForMode.forEach(slot -> slot.setActive(true));
        otherSlots.forEach(slot -> slot.setActive(false));


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
    protected boolean moveItemStackTo(ItemStack pStack, int pStartIndex, int pEndIndex, boolean pReverseDirection) {
        boolean flag = false;
        int i = pStartIndex;
        if (pReverseDirection) {
            i = pEndIndex - 1;
        }

        if (pStack.isStackable()) {
            while(!pStack.isEmpty()) {
                if (pReverseDirection) {
                    if (i < pStartIndex) {
                        break;
                    }
                } else if (i >= pEndIndex) {
                    break;
                }

                Slot slot = this.slots.get(i);
                if (slot.isActive()) {
                    ItemStack itemstack = slot.getItem();
                    if (!itemstack.isEmpty() && ItemStack.isSameItemSameComponents(pStack, itemstack)) {
                        int j = itemstack.getCount() + pStack.getCount();
                        int maxSize = Math.min(slot.getMaxStackSize(), pStack.getMaxStackSize());
                        if (j <= maxSize) {
                            pStack.setCount(0);
                            itemstack.setCount(j);
                            slot.setChanged();
                            flag = true;
                        } else if (itemstack.getCount() < maxSize) {
                            pStack.shrink(maxSize - itemstack.getCount());
                            itemstack.setCount(maxSize);
                            slot.setChanged();
                            flag = true;
                        }
                    }
                }

                if (pReverseDirection) {
                    --i;
                } else {
                    ++i;
                }
            }
        }

        if (!pStack.isEmpty()) {
            if (pReverseDirection) {
                i = pEndIndex - 1;
            } else {
                i = pStartIndex;
            }

            while(true) {
                if (pReverseDirection) {
                    if (i < pStartIndex) {
                        break;
                    }
                } else if (i >= pEndIndex) {
                    break;
                }

                Slot slot1 = this.slots.get(i);
                if (slot1.isActive()) {
                    ItemStack itemstack1 = slot1.getItem();
                    if (itemstack1.isEmpty() && slot1.mayPlace(pStack)) {
                        if (pStack.getCount() > slot1.getMaxStackSize()) {
                            slot1.set(pStack.split(slot1.getMaxStackSize()));
                        } else {
                            slot1.set(pStack.split(pStack.getCount()));
                        }

                        slot1.setChanged();
                        flag = true;
                        break;
                    }
                }
                if (pReverseDirection) {
                    --i;
                } else {
                    ++i;
                }
            }
        }

        return flag;
    }

    public int getField(int index){
        return this.ownerData.get(index);
    }
    public void setField(int index, int value){
        this.ownerData.set(index, value);
    }



    @Override
    public boolean stillValid(@NotNull Player pPlayer) {
        return true;//todo
    }


    public boolean getMode() {
        return this.ownerData.get(2) == 1;
    }

    public void setMode(boolean mode) {
        this.ownerData.set(2, mode ? 1 : 0);
    }

}
