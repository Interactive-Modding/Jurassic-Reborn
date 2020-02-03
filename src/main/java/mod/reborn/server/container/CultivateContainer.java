package mod.reborn.server.container;

import mod.reborn.server.block.entity.CultivatorBlockEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.tileentity.TileEntity;
import mod.reborn.server.container.slot.CultivatorSyringeSlot;
import mod.reborn.server.container.slot.CustomSlot;
import mod.reborn.server.container.slot.NutrientSlot;
import mod.reborn.server.container.slot.WaterBucketSlot;

public class CultivateContainer extends MachineContainer {
    private CultivatorBlockEntity cultivator;

    public CultivateContainer(InventoryPlayer playerInventory, TileEntity tileEntity) {
        super((IInventory) tileEntity);
        this.cultivator = (CultivatorBlockEntity) tileEntity;
        this.addSlotToContainer(new CultivatorSyringeSlot(this.cultivator, 0, 122, 44));
        this.addSlotToContainer(new NutrientSlot(this.cultivator, 1, 208, 20));
        this.addSlotToContainer(new WaterBucketSlot(this.cultivator, 2, 12, 20));
        this.addSlotToContainer(new CustomSlot(this.cultivator, 3, 12, 68, stack -> false));

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 106 + i * 18));
            }
        }

        for (int i = 0; i < 9; ++i) {
            this.addSlotToContainer(new Slot(playerInventory, i, 8 + i * 18, 164));
        }
    }

    @Override
    public void onContainerClosed(EntityPlayer player) {
        super.onContainerClosed(player);

        if (!player.world.isRemote) {
            this.cultivator.closeInventory(player);
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return this.cultivator.isUsableByPlayer(player);
    }
}
