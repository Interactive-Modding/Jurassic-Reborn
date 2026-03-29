package net.vit.jurassicreborn.common.blocks.entities.DNABlocks.DNAExtractor;

import net.neoforged.neoforge.items.SlotItemHandler;
import net.vit.jurassicreborn.common.blocks.entities.DNABlocks.DNASequencer.DNASequencerMenu;
import net.vit.jurassicreborn.common.blocks.entities.MachineItemStackHandler;
import net.vit.jurassicreborn.common.blocks.entities.ModMenuTypes;
import net.vit.jurassicreborn.common.blocks.inventory.DNAExtractorHandler;
import net.vit.jurassicreborn.common.items.Food.DinosaurMeatItem;
import net.vit.jurassicreborn.common.items.ModItems;
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

public class DNAExtractorMenu extends AbstractContainerMenu {

    private final MachineItemStackHandler dnaExtractor;
    private final ContainerData dnaExtractorData;
    public DNAExtractorMenu(int pContainerId, Inventory playerInventory) {
        this(pContainerId, DNAExtractorHandler.instance(), new SimpleContainerData(1), playerInventory);
    }
    public DNAExtractorMenu(int containerId, MachineItemStackHandler dnaSequencer, ContainerData sequencerData, Inventory playerInventory){
        super(ModMenuTypes.DNA_EXTRACTOR.get(), containerId);

        this.dnaExtractor = dnaSequencer;
        this.dnaExtractorData = sequencerData;

        this.addSlot(new SlotItemHandler(this.dnaExtractor, 0, 55, 26));
        this.addSlot(new SlotItemHandler(this.dnaExtractor, 1, 55, 47));
        this.addSlot(new SlotItemHandler(this.dnaExtractor, 2, 108, 28));
        this.addSlot(new SlotItemHandler(this.dnaExtractor, 3, 126, 28));
        this.addSlot(new SlotItemHandler(this.dnaExtractor, 4, 108, 46));
        this.addSlot(new SlotItemHandler(this.dnaExtractor, 5, 126, 46));

        DNASequencerMenu.addPlayerInventory(playerInventory, this::addSlot);


        this.addDataSlots(this.dnaExtractorData);


    }

    public int getField(int index){
        return this.dnaExtractorData.get(index);
    }

    public void setField(int index, int value){
        this.dnaExtractorData.set(index, value);
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
}
