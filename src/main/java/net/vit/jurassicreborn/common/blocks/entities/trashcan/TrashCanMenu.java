package net.vit.jurassicreborn.common.blocks.entities.trashcan;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.vit.jurassicreborn.common.blocks.entities.ModMenuTypes;

public class TrashCanMenu extends AbstractContainerMenu {
    private final Container inventory;

    public TrashCanMenu(int id, Inventory playerInv) {
        this(id, playerInv, new SimpleContainer(9));
    }

    public TrashCanMenu(int id, Inventory playerInv, Container container) {
        super(ModMenuTypes.TRASH_CAN.get(), id);
        checkContainerSize(container, 9);
        this.inventory = container;

        // Trash slots 3x3 grid
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                this.addSlot(new Slot(container, col + row * 3, 62 + col * 18, 17 + row * 18));
            }
        }

        // Player inventory
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new Slot(playerInv, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }

        // Hotbar
        for (int col = 0; col < 9; col++) {
            this.addSlot(new Slot(playerInv, col, 8 + col * 18, 142));
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        inventory.clearContent();
    }
}
