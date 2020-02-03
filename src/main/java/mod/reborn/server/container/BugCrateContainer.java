package mod.reborn.server.container;

import mod.reborn.server.api.BreedableBug;
import mod.reborn.server.block.entity.BugCrateBlockEntity;
import mod.reborn.server.food.FoodHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import mod.reborn.server.container.slot.CustomSlot;

public class BugCrateContainer extends MachineContainer {
    private BugCrateBlockEntity crate;

    public BugCrateContainer(InventoryPlayer playerInventory, BugCrateBlockEntity crate) {
        super(crate);
        this.crate = crate;

        for (int column = 0; column < 3; column++) {
            this.addSlotToContainer(new CustomSlot(crate, column, 26 + (column * 18), 17, stack -> stack.getItem() instanceof BreedableBug));
        }

        for (int column = 0; column < 3; column++) {
            this.addSlotToContainer(new CustomSlot(crate, column + 3, 26 + (column * 18), 51, stack -> FoodHelper.isFood(stack.getItem())));
        }

        for (int row = 0; row < 3; row++) {
            this.addSlotToContainer(new CustomSlot(crate, row + 6, 126, 17 + (row * 18), stack -> false));
        }

        for (int row = 0; row < 3; ++row) {
            for (int column = 0; column < 9; ++column) {
                this.addSlotToContainer(new Slot(playerInventory, column + row * 9 + 9, 8 + column * 18, 84 + row * 18));
            }
        }

        for (int column = 0; column < 9; ++column) {
            this.addSlotToContainer(new Slot(playerInventory, column, 8 + column * 18, 142));
        }
    }

    @Override
    public void onContainerClosed(EntityPlayer player) {
        super.onContainerClosed(player);
        this.crate.closeInventory(player);
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return this.crate.isUsableByPlayer(player);
    }
}
