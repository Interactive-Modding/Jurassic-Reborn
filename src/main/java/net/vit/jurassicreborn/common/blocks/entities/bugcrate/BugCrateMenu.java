package net.vit.jurassicreborn.common.blocks.entities.bugcrate;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.vit.jurassicreborn.common.blocks.entities.ModMenuTypes;
import net.vit.jurassicreborn.common.items.Food.FoodHelper;
import net.vit.jurassicreborn.common.util.BreedableBug;
import net.vit.jurassicreborn.common.entities.EntityUtils.FoodType;

import java.util.function.Predicate;

public class BugCrateMenu extends AbstractContainerMenu {

    private final Container crate;
    public final ContainerData data; // <-- For progress

    // MAIN CONSTRUCTOR: Accepts ContainerData for progress sync
    public BugCrateMenu(int id, Inventory playerInv, Container crateInv, ContainerData dataAccess) {
        super(ModMenuTypes.BUG_CRATE.get(), id);
        this.crate = crateInv;
        this.data = dataAccess;
        this.addDataSlots(dataAccess); // This enables progress bar sync

        // === Slot coordinates (like FeederMenu) ===
        final int SLOT = 18, PLANT_X = 26, INSECT_X = 26, TOP_Y = 17, BOTTOM_Y = 51, OUTPUT_X = 126, OUTPUT_Y = 17;

        // Top: Plant input (FoodType.PLANT only)
        for (int col = 0; col < 3; col++) {
            final int slotIdx = col;
            this.addSlot(new FilteredSlot(crateInv, slotIdx, PLANT_X + col * SLOT, TOP_Y, stack ->
                    FoodHelper.isFoodType(stack.getItem(), FoodType.PLANT)
            ));
        }
        // Bottom: Insect input (FoodType.INSECT only)
        for (int col = 0; col < 3; col++) {
            final int slotIdx = col + 3;
            this.addSlot(new FilteredSlot(crateInv, slotIdx, INSECT_X + col * SLOT, BOTTOM_Y, stack ->
                    FoodHelper.isFoodType(stack.getItem(), FoodType.INSECT)
            ));
        }
        // Output slots (right side, cannot insert)
        for (int row = 0; row < 3; row++) {
            final int slotIdx = row + 6;
            this.addSlot(new FilteredSlot(crateInv, slotIdx, OUTPUT_X, TOP_Y + row * SLOT, stack -> false));
        }

        // === Player inventory (copy from FeederMenu) ===
        int INV_Y = 84;
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 9; c++) {
                this.addSlot(new Slot(playerInv, c + r * 9 + 9, 8 + c * SLOT, INV_Y + r * SLOT));
            }
        }
        // Hotbar (copy from FeederMenu)
        int HOTBAR_Y = 142;
        for (int c = 0; c < 9; c++) {
            this.addSlot(new Slot(playerInv, c, 8 + c * SLOT, HOTBAR_Y));
        }
    }
    public int getProgress() {
        // If using addDataSlots(new ContainerData()...) with progress/max
        return this.data.getCount() >= 2 ? this.data.get(0) : 0;
    }

    public int getMaxProgress() {
        return this.data.getCount() >= 2 ? this.data.get(1) : 0;
    }
    // CLIENT fallback for network desync (Forge requires this, but NO progress bar)
    public BugCrateMenu(int id, Inventory playerInv) {
        this(id, playerInv, new SimpleContainer(9), new ContainerData() {
            @Override public int get(int index) { return 0; }
            @Override public void set(int index, int value) {}
            @Override public int getCount() { return 2; }
        });
    }

    @Override
    public boolean stillValid(Player player) {
        return crate.stillValid(player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY;
    }

    /** Custom filtered slot for special behavior */
    public static class FilteredSlot extends Slot {
        private final Predicate<ItemStack> filter;
        public FilteredSlot(Container inv, int idx, int x, int y, Predicate<ItemStack> filter) {
            super(inv, idx, x, y);
            this.filter = filter;
        }
        @Override
        public boolean mayPlace(ItemStack stack) {
            return filter.test(stack);
        }
    }
}
