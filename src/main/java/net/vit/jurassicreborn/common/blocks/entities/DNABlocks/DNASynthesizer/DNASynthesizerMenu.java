package net.vit.jurassicreborn.common.blocks.entities.DNABlocks.DNASynthesizer;

import net.minecraftforge.items.SlotItemHandler;
import net.vit.jurassicreborn.common.blocks.entities.MachineItemStackHandler;
import net.vit.jurassicreborn.common.blocks.entities.ModMenuTypes;
import net.vit.jurassicreborn.common.blocks.inventory.DNASynthesizerHandler;
import net.vit.jurassicreborn.common.items.ModItems;
import net.vit.jurassicreborn.common.util.api.SynthesizableItem;
import net.vit.jurassicreborn.common.util.slot.CustomSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import static net.vit.jurassicreborn.common.blocks.entities.DNABlocks.DNASequencer.DNASequencerMenu.addPlayerInventory;

public class DNASynthesizerMenu extends AbstractContainerMenu {

    MachineItemStackHandler handler;
    ContainerData data;

    public DNASynthesizerMenu(int pContainerId, Inventory playerInventory) {
        this(pContainerId, new DNASynthesizerHandler(DNASynthesizerBlockEntity.SLOTS,DNASynthesizerBlockEntity.INPUTS,DNASynthesizerBlockEntity.OUTPUTS), new SimpleContainerData(1), playerInventory);
    }
    public DNASynthesizerMenu(int containerId, DNASynthesizerHandler dnaSequencer, ContainerData sequencerData, Inventory playerInventory){
        super(ModMenuTypes.DNA_SYNTHESIZER.get(), containerId);

        this.handler = dnaSequencer;
        this.data = sequencerData;


        this.addSlot(new SlotItemHandler(this.handler, 0, 38, 22));
        this.addSlot(new SlotItemHandler(this.handler, 1, 24, 49));
        this.addSlot(new SlotItemHandler(this.handler, 2, 50, 49));


        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                this.addSlot(new SlotItemHandler(this.handler, i + (j * 2) + 3, i * 18 + 119, j * 18 + 26));
            }
        }

        addPlayerInventory(playerInventory, this::addSlot);
        this.addDataSlots(this.data);
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
}
