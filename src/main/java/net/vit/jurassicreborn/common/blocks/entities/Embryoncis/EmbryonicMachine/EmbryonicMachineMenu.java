package net.vit.jurassicreborn.common.blocks.entities.Embryoncis.EmbryonicMachine;

import net.neoforged.neoforge.items.SlotItemHandler;
import net.vit.jurassicreborn.common.blocks.entities.DNABlocks.DNASequencer.DNASequencerMenu;
import net.vit.jurassicreborn.common.blocks.entities.MachineItemStackHandler;
import net.vit.jurassicreborn.common.blocks.entities.ModMenuTypes;
import net.vit.jurassicreborn.common.blocks.inventory.EmbryonicMachineItemHandler;
import net.vit.jurassicreborn.common.items.ModItems;
import net.vit.jurassicreborn.common.items.genetics.DNAItem;
import net.vit.jurassicreborn.common.items.genetics.PlantDNAItem;
import net.vit.jurassicreborn.common.util.slot.CustomSlot;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;

public class EmbryonicMachineMenu extends AbstractContainerMenu {
    private MachineItemStackHandler owner;
    private ContainerData ownerData;
    public EmbryonicMachineMenu(int pContainerId, Inventory playerInv) {
        this(pContainerId, EmbryonicMachineItemHandler.instance(), new SimpleContainerData(1), playerInv);
    }

    public EmbryonicMachineMenu(int containerId, MachineItemStackHandler owner, ContainerData ownerData, Inventory playerInv){
        super(ModMenuTypes.EMBRYONIC_MACHINE.get(), containerId);
        this.owner = owner;
        this.ownerData = ownerData;

        this.addSlot(new SlotItemHandler(this.owner, 0, 24, 49));
        this.addSlot(new SlotItemHandler(this.owner, 1, 50, 49));
        this.addSlot(new SlotItemHandler(this.owner, 2, 50, 13));

        this.addSlot(new SlotItemHandler(this.owner, 3, 119, 26));
        this.addSlot(new SlotItemHandler(this.owner, 5, 119, 44));
        this.addSlot(new SlotItemHandler(this.owner, 4, 137, 26));
        this.addSlot(new SlotItemHandler(this.owner, 6, 137, 44));

        DNASequencerMenu.addPlayerInventory(playerInv, this::addSlot);

        this.addDataSlots(this.ownerData);

    }

    public int getField(int i){
        return this.ownerData.get(i);
    }

    public void setFiled(int i, int v){
        this.ownerData.set(i, v);
    }



    @Override
    public boolean stillValid(Player pPlayer) {
        return true;//todo check distance
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
}
