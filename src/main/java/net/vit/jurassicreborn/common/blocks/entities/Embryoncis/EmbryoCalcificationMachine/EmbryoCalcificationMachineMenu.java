package net.vit.jurassicreborn.common.blocks.entities.Embryoncis.EmbryoCalcificationMachine;

import net.neoforged.neoforge.items.SlotItemHandler;
import net.vit.jurassicreborn.common.blocks.entities.DNABlocks.DNASequencer.DNASequencerMenu;
import net.vit.jurassicreborn.common.blocks.entities.ModMenuTypes;
import net.vit.jurassicreborn.common.blocks.inventory.EmbryoCalcificationMachineItemHandler;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;
import net.vit.jurassicreborn.common.items.genetics.SyringeItem;
import net.vit.jurassicreborn.common.util.slot.CustomSlot;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class EmbryoCalcificationMachineMenu extends AbstractContainerMenu {

    private EmbryoCalcificationMachineItemHandler owner;

    private ContainerData ownerData;

    public EmbryoCalcificationMachineMenu(int containerID, Inventory playerInventory){
        this(containerID, EmbryoCalcificationMachineItemHandler.instance(), new SimpleContainerData(1), playerInventory);
    }

    public EmbryoCalcificationMachineMenu(int containerID, EmbryoCalcificationMachineItemHandler owner, ContainerData ownerData, Inventory playerInventory){
        super(ModMenuTypes.EMBRYO_CALCIFICATION_MACHINE.get(), containerID);

        this.owner = owner;

        this.ownerData = ownerData;

        this.addSlot(new SlotItemHandler(this.owner, 0, 34, 14));
        this.addSlot(new SlotItemHandler(this.owner, 1, 34, 50));
        this.addSlot(new SlotItemHandler(this.owner, 2, 97, 32));

        this.addDataSlots(this.ownerData);

        DNASequencerMenu.addPlayerInventory(playerInventory, this::addSlot);

    }

    public int getField(int index){
        return this.ownerData.get(index);
    }

    public void setField(int index, int v){
        this.ownerData.set(index, v);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack transferred = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        int otherSlots = this.slots.size() - 36;

        if (slot.hasItem()) {
            ItemStack current = slot.getItem();
            transferred = current.copy();

            if (index < otherSlots) {
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
    public boolean stillValid(Player pPlayer) {
        return true; //todo check block
    }
}
