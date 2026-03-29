package net.vit.jurassicreborn.common.blocks.entities.DNABlocks.DNASequencer;

import net.neoforged.neoforge.items.SlotItemHandler;
import net.vit.jurassicreborn.common.blocks.entities.MachineItemStackHandler;
import net.vit.jurassicreborn.common.blocks.entities.ModMenuTypes;
import net.vit.jurassicreborn.common.blocks.inventory.DNASequencerItemHandler;
import net.vit.jurassicreborn.common.util.api.SequencableItem;
import net.vit.jurassicreborn.common.util.slot.CustomSlot;
import net.vit.jurassicreborn.common.util.slot.StorageSlot;
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

import java.util.function.Function;

public class DNASequencerMenu extends AbstractContainerMenu {

    private final DNASequencerItemHandler dnaSequencer;
    private final ContainerData sequencerData;
    public DNASequencerMenu(int pContainerId, Inventory playerInventory) {
        this(pContainerId, DNASequencerItemHandler.instance(), new SimpleContainerData(3), playerInventory);
    }
    public DNASequencerMenu(int containerId, DNASequencerItemHandler dnaSequencer, ContainerData sequencerData, Inventory playerInventory){
        super(ModMenuTypes.DNA_SEQUENCER.get(), containerId);

        this.dnaSequencer = dnaSequencer;
        this.sequencerData = sequencerData;

        this.addSlot(new SlotItemHandler(this.dnaSequencer, 0, 44, 16));//im too tired to make a whole new class for this. this is not the code you need, it's the code you deserve.
        this.addSlot(new SlotItemHandler(this.dnaSequencer, 1, 66, 16));
        this.addSlot(new SlotItemHandler(this.dnaSequencer, 2, 44, 36));
        this.addSlot(new SlotItemHandler(this.dnaSequencer, 3, 66, 36));
        this.addSlot(new SlotItemHandler(this.dnaSequencer, 4, 44, 56));
        this.addSlot(new SlotItemHandler(this.dnaSequencer, 5, 66, 56));

        this.addSlot(new SlotItemHandler(this.dnaSequencer, 6, 113, 16));
        this.addSlot(new SlotItemHandler(this.dnaSequencer, 7, 113, 36));
        this.addSlot(new SlotItemHandler(this.dnaSequencer, 8, 113, 56));

        addPlayerInventory(playerInventory, this::addSlot);


        this.addDataSlots(this.sequencerData);


    }

    public int getField(int index){
        return this.sequencerData.get(index);
    }

    public void setField(int index, int value){
        this.sequencerData.set(index, value);
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

    public static void addPlayerInventory(Inventory pPlayerInventory, Function<Slot, Slot> adderFunc){

        for(int l = 0; l < 3; ++l) {
            for(int j1 = 0; j1 < 9; ++j1) {
                adderFunc.apply(new Slot(pPlayerInventory, j1 + l * 9 + 9, 8 + j1 * 18, 84 + l * 18));
            }
        }

        for(int i1 = 0; i1 < 9; ++i1) {
            adderFunc.apply((new Slot(pPlayerInventory, i1, 8 + i1 * 18, 142)));
        }
    }

    @Override
    public boolean stillValid(@NotNull Player pPlayer) {
        return true;//todo check block
    }
}
