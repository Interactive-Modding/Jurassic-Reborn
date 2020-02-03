package mod.reborn.server.container;

import mod.reborn.server.block.entity.DNASequencerBlockEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.tileentity.TileEntity;
import mod.reborn.server.container.slot.CustomSlot;
import mod.reborn.server.container.slot.SequencableItemSlot;
import mod.reborn.server.container.slot.StorageSlot;

public class DNASequencerContainer extends MachineContainer {
    private DNASequencerBlockEntity dnaSequencer;

    public DNASequencerContainer(InventoryPlayer playerInventory, TileEntity tileEntity) {
        super((IInventory) tileEntity);

        this.dnaSequencer = (DNASequencerBlockEntity) tileEntity;

        this.addSlotToContainer(new SequencableItemSlot(this.dnaSequencer, 0, 44, 16));
        this.addSlotToContainer(new StorageSlot(this.dnaSequencer, 1, 66, 16, false, 1));
        this.addSlotToContainer(new SequencableItemSlot(this.dnaSequencer, 2, 44, 36));
        this.addSlotToContainer(new StorageSlot(this.dnaSequencer, 3, 66, 36, false, 1));
        this.addSlotToContainer(new SequencableItemSlot(this.dnaSequencer, 4, 44, 56));
        this.addSlotToContainer(new StorageSlot(this.dnaSequencer, 5, 66, 56, false, 1));

        this.addSlotToContainer(new CustomSlot(this.dnaSequencer, 6, 113, 16, stack -> false, 1));
        this.addSlotToContainer(new CustomSlot(this.dnaSequencer, 7, 113, 36, stack -> false, 1));
        this.addSlotToContainer(new CustomSlot(this.dnaSequencer, 8, 113, 56, stack -> false, 1));

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int i = 0; i < 9; ++i) {
            this.addSlotToContainer(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }

    @Override
    public void onContainerClosed(EntityPlayer player) {
        super.onContainerClosed(player);

        if (!player.world.isRemote) {
            this.dnaSequencer.closeInventory(player);
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return this.dnaSequencer.isUsableByPlayer(player);
    }
}
