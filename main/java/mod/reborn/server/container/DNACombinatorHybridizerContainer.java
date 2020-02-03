package mod.reborn.server.container;

import mod.reborn.server.block.entity.DNACombinatorHybridizerBlockEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.tileentity.TileEntity;
import mod.reborn.server.container.slot.CustomSlot;
import mod.reborn.server.container.slot.StorageSlot;

public class DNACombinatorHybridizerContainer extends MachineContainer {
    private DNACombinatorHybridizerBlockEntity dnaHybridizer;
    private InventoryPlayer playerInventory;

    public DNACombinatorHybridizerContainer(InventoryPlayer playerInventory, TileEntity tileEntity) {
        super((IInventory) tileEntity);
        this.dnaHybridizer = (DNACombinatorHybridizerBlockEntity) tileEntity;
        this.playerInventory = playerInventory;
        this.updateSlots(this.dnaHybridizer.getMode());
    }

    public void updateSlots(boolean mode) {
        this.inventorySlots.clear();
        this.inventoryItemStacks.clear();

        if (mode) {
            this.addSlotToContainer(new StorageSlot(this.dnaHybridizer, 0, 10, 17, true));
            this.addSlotToContainer(new StorageSlot(this.dnaHybridizer, 1, 30, 17, true));
            this.addSlotToContainer(new StorageSlot(this.dnaHybridizer, 2, 50, 17, true));
            this.addSlotToContainer(new StorageSlot(this.dnaHybridizer, 3, 70, 17, true));
            this.addSlotToContainer(new StorageSlot(this.dnaHybridizer, 4, 90, 17, true));
            this.addSlotToContainer(new StorageSlot(this.dnaHybridizer, 5, 110, 17, true));
            this.addSlotToContainer(new StorageSlot(this.dnaHybridizer, 6, 130, 17, true));
            this.addSlotToContainer(new StorageSlot(this.dnaHybridizer, 7, 150, 17, true));
            this.addSlotToContainer(new CustomSlot(this.dnaHybridizer, 10, 80, 56, stack -> false));
        } else {
            this.addSlotToContainer(new StorageSlot(this.dnaHybridizer, 8, 55, 13, true));
            this.addSlotToContainer(new StorageSlot(this.dnaHybridizer, 9, 105, 13, true));
            this.addSlotToContainer(new CustomSlot(this.dnaHybridizer, 11, 81, 60, stack -> false));
        }

        int i;

        for (i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(this.playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (i = 0; i < 9; ++i) {
            this.addSlotToContainer(new Slot(this.playerInventory, i, 8 + i * 18, 142));
        }
    }

    @Override
    public void onContainerClosed(EntityPlayer player) {
        super.onContainerClosed(player);

        if (!player.world.isRemote) {
            this.dnaHybridizer.closeInventory(player);
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return this.dnaHybridizer.isUsableByPlayer(player);
    }
}
