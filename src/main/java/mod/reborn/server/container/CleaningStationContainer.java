package mod.reborn.server.container;

import mod.reborn.server.block.entity.CleaningStationBlockEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.tileentity.TileEntity;

import mod.reborn.server.container.slot.CleanableItemSlot;
import mod.reborn.server.container.slot.CustomSlot;
import mod.reborn.server.container.slot.WaterBucketSlot;

public class CleaningStationContainer extends MachineContainer {
    private final CleaningStationBlockEntity tileCleaningStation;

    public CleaningStationContainer(InventoryPlayer invPlayer, TileEntity tileEntity) {
        super((IInventory) tileEntity);
        this.tileCleaningStation = (CleaningStationBlockEntity) tileEntity;
        this.addSlotToContainer(new CleanableItemSlot(tileCleaningStation, 0, 56, 17));
        this.addSlotToContainer(new WaterBucketSlot(tileCleaningStation, 1, 56, 53));

        int i;

        for (i = 0; i < 3; i++) {
            for (int j = 0; j < 2; j++) {
                this.addSlotToContainer(new CustomSlot(tileCleaningStation, i + (j * 3) + 2, i * 18 + 93 + 15, j * 18 + 26, stack -> false));
            }
        }

        for (i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(invPlayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (i = 0; i < 9; ++i) {
            this.addSlotToContainer(new Slot(invPlayer, i, 8 + i * 18, 142));
        }
    }
    
    public void addListener(IContainerListener listener)
    {
        super.addListener(listener);
        listener.sendAllWindowProperties(this, this.tileCleaningStation);
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return this.tileCleaningStation.isUsableByPlayer(player);
    }
}
