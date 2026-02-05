package net.vit.jurassicreborn.common.blocks.entities.feeder;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.vit.jurassicreborn.common.blocks.entities.ModMenuTypes;
import net.vit.jurassicreborn.common.entities.EntityUtils.FoodType;
import net.vit.jurassicreborn.common.items.Food.FoodHelper;

import java.util.function.Predicate;

public class FeederMenu extends AbstractContainerMenu {

    private final Container feeder;

    public FeederMenu(int id, Inventory playerInv, Container feederInv, ContainerData dataAccess) {
        super(ModMenuTypes.FEEDER.get(), id);
        this.feeder = feederInv;
        this.addDataSlots(dataAccess);

        final int SLOT = 18, MEAT_X = 26, PLANT_X = 98, TOP_Y = 18;
        int idx = 0;

        boolean isReal = feederInv instanceof FeederBlockEntity;
        FeederBlockEntity feederEntity = isReal ? (FeederBlockEntity) feederInv : null;

// meat slots 0–8
        for (int index = 0; index < 9; index++) {
            int row = index / 3;
            int col = index % 3;
            final int slotIndex = index;
            this.addSlot(new FilteredSlot(
                    feederInv,
                    slotIndex,
                    MEAT_X + col * SLOT,
                    TOP_Y + row * SLOT,
                    stack -> {
                        if (feederEntity != null) {
                            return feederEntity.isItemValidForSlot(slotIndex, stack);
                        }
                        return true;
                    }
            ));
        }

// plant slots 9–17
        for (int index = 9; index < 18; index++) {
            int row = (index - 9) / 3;
            int col = (index - 9) % 3;
            final int slotIndex = index;
            this.addSlot(new FilteredSlot(
                    feederInv,
                    slotIndex,
                    PLANT_X + col * SLOT,
                    TOP_Y + row * SLOT,
                    stack -> {
                        if (feederEntity != null) {
                            return feederEntity.isItemValidForSlot(slotIndex, stack);
                        }
                        return true;
                    }
            ));
        }



        // Player inventory (9x3)
        int INV_Y = 84;
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 9; c++) {
                this.addSlot(new Slot(playerInv, c + r * 9 + 9, 8 + c * SLOT, INV_Y + r * SLOT));
            }
        }

        // Hotbar
        int HOTBAR_Y = 142;
        for (int c = 0; c < 9; c++) {
            this.addSlot(new Slot(playerInv, c, 8 + c * SLOT, HOTBAR_Y));
        }
    }

    // Client-side fallback constructor
    public FeederMenu(int id, Inventory playerInv) {
        this(id, playerInv, new SimpleContainer(18), new ContainerData() {
            @Override public int get(int index) { return 0; }
            @Override public void set(int index, int value) {}
            @Override public int getCount() { return 0; }
        });
    }

    @Override
    public boolean stillValid(Player player) {
        return feeder.stillValid(player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY; // Shift-clicking not handled
    }

    public static class FilteredSlot extends Slot {
        private final Predicate<ItemStack> filter;

        public FilteredSlot(Container inv, int index, int x, int y, Predicate<ItemStack> filter) {
            super(inv, index, x, y);
            this.filter = filter;
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return filter.test(stack);
        }
    }
}
