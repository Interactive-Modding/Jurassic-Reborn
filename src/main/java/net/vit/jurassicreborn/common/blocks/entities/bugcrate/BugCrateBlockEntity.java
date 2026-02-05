package net.vit.jurassicreborn.common.blocks.entities.bugcrate;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.vit.jurassicreborn.common.blocks.entities.ModBlockEntities;
import net.vit.jurassicreborn.common.items.Food.FoodHelper;
import net.vit.jurassicreborn.common.entities.EntityUtils.FoodType;
import net.vit.jurassicreborn.common.util.BreedableBug;

import javax.annotation.Nullable;

import java.util.stream.IntStream;

import static net.minecraft.world.ContainerHelper.loadAllItems;
import static net.minecraft.world.ContainerHelper.saveAllItems;

public class BugCrateBlockEntity extends RandomizableContainerBlockEntity implements WorldlyContainer {
    public static final int PLANT_SLOT_START = 0, PLANT_SLOT_END = 2;
    public static final int INSECT_SLOT_START = 3, INSECT_SLOT_END = 5;
    public static final int OUTPUT_SLOT_START = 6, OUTPUT_SLOT_END = 8;
    public static final int SIZE = 9; // Correct size

    private static final int BREED_TICKS = 200; // time to breed a bug

    private int breedProgress = 0;
    private int lastBugTypeSlot = -1;

    private NonNullList<ItemStack> items = NonNullList.withSize(SIZE, ItemStack.EMPTY);

    public BugCrateBlockEntity(BlockPos pos, BlockState state) {
        this(ModBlockEntities.BUG_CRATE.get(), pos, state);
    }

    public BugCrateBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    protected Component getDefaultName() {
        return Component.literal("Bug Crate");
    }

    @Override
    public int getContainerSize() {
        return SIZE;
    }
    private final ContainerData dataAccess = new ContainerData() {
        @Override public int get(int idx) {
            if (idx == 0) return breedProgress;
            if (idx == 1) return BREED_TICKS;
            return 0;
        }

        @Override public void set(int idx, int value) {
            if (idx == 0) breedProgress = value;
        }

        @Override public int getCount() {
            return 2;
        }
    };

    public ContainerData getDataAccess() {
        return dataAccess;
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> items) {
        this.items = items;
    }

    // Required by Forge for menu opening
    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory playerInv) {
        return new BugCrateMenu(id, playerInv, this, this.dataAccess);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.items = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
        loadAllItems(tag, this.items);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        saveAllItems(tag, this.items);
    }

    // Slot validation - use slot index
    public boolean isItemValidForSlot(int slot, ItemStack stack) {
        if (stack == null || stack.isEmpty()) return false;

        if (slot >= PLANT_SLOT_START && slot <= PLANT_SLOT_END)
            return FoodHelper.isFoodType(stack.getItem(), FoodType.PLANT);
        if (slot >= INSECT_SLOT_START && slot <= INSECT_SLOT_END)
            return BreedableBug.isBug(stack);

        // Output slots and anything else: deny
        return false;
    }

    // Tick logic
    public static void serverTick(Level level, BlockPos pos, BlockState state, BugCrateBlockEntity entity) {
        if (entity.canBreedBug()) {
            entity.breedProgress++;
            if (entity.breedProgress >= BREED_TICKS) {
                entity.tryBreedBug();
                entity.breedProgress = 0;
            }
        } else {
            entity.breedProgress = 0;
        }
    }

    private boolean canBreedBug() {
        // Plant in any top slot?
        boolean hasPlant = false;
        for (int i = PLANT_SLOT_START; i <= PLANT_SLOT_END; i++) {
            if (!getItem(i).isEmpty() && FoodHelper.isFoodType(getItem(i).getItem(), FoodType.PLANT)) {
                hasPlant = true;
                break;
            }
        }
        // At least one breedable bug in bottom slot?
        for (int i = INSECT_SLOT_START; i <= INSECT_SLOT_END; i++) {
            ItemStack bug = getItem(i);
            if (!bug.isEmpty() && BreedableBug.isBug(bug)) {
                // Space in output?
                if (hasPlant && getFreeOutputSlot() != -1) {
                    lastBugTypeSlot = i;
                    return true;
                }
            }
        }
        return false;
    }

    private void tryBreedBug() {
        if (lastBugTypeSlot == -1) return;

        ItemStack bugInSlot = getItem(lastBugTypeSlot);
        if (bugInSlot.isEmpty() || !BreedableBug.isBug(bugInSlot)) return;

        // 1) Find a valid plant slot (0–2)
        int plantSlot = getFirstPlantSlot();
        if (plantSlot == -1) return;          // no food → abort

        Item target = bugInSlot.getItem();
        int stackSlot = -1;
        for (int i = OUTPUT_SLOT_START; i <= OUTPUT_SLOT_END; i++) {
            ItemStack existing = getItem(i);
            if (!existing.isEmpty() && existing.getItem() == target && ItemStack.tagMatches(existing, bugInSlot)) {
                stackSlot = i;
                break;
            }
        }

        // 3) Consume one plant
        removeItem(plantSlot, 1);

        if (stackSlot != -1) {
            getItem(stackSlot).grow(1);
            return;
        }

        // 5) Otherwise find a truly empty output slot
        int emptySlot = getFreeOutputSlot();
        if (emptySlot != -1) {
            ItemStack newBug = new ItemStack(target, 1);
            setItem(emptySlot, newBug);
        } else {
            // 6) All output slots (6–8) are full: drop one bug in the world
            if (level != null && !level.isClientSide) {
                double x = worldPosition.getX() + 0.5;
                double y = worldPosition.getY() + 1.1;
                double z = worldPosition.getZ() + 0.5;
                ItemStack newBug = new ItemStack(target, 1);
                ItemEntity drop = new ItemEntity(level, x, y, z, newBug);
                level.addFreshEntity(drop);
            }
        }
    }


    @Override
    public int[] getSlotsForFace(Direction direction) {
        return IntStream.range(0, SIZE).toArray();
    }

    // 2) Only allow insertion into slots 0–5 (plants/bugs)
    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack stack, Direction direction) {
        return index >= PLANT_SLOT_START && index <= INSECT_SLOT_END;
    }

    // 3) Only allow extraction from slots 6–8 (output bugs)
    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        return index >= OUTPUT_SLOT_START && index <= OUTPUT_SLOT_END;
    }

    private int getFirstPlantSlot() {
        for (int i = PLANT_SLOT_START; i <= PLANT_SLOT_END; i++) {
            if (!getItem(i).isEmpty() && FoodHelper.isFoodType(getItem(i).getItem(), FoodType.PLANT)) {
                return i;
            }
        }
        return -1;
    }

    private int getFreeOutputSlot() {
        for (int i = OUTPUT_SLOT_START; i <= OUTPUT_SLOT_END; i++) {
            if (getItem(i).isEmpty()) return i;
        }
        return -1;
    }
}
