package net.vit.jurassicreborn.common.blocks.entities.bugcrate;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
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

import org.jetbrains.annotations.Nullable;

import java.util.stream.IntStream;

import static net.minecraft.world.ContainerHelper.loadAllItems;
import static net.minecraft.world.ContainerHelper.saveAllItems;

public class BugCrateBlockEntity extends RandomizableContainerBlockEntity implements WorldlyContainer {

    public static final int PLANT_SLOT_START = 0, PLANT_SLOT_END = 2;
    public static final int INSECT_SLOT_START = 3, INSECT_SLOT_END = 5;
    public static final int OUTPUT_SLOT_START = 6, OUTPUT_SLOT_END = 8;
    public static final int SIZE = 9;

    private static final int BREED_TICKS = 200;

    private int breedProgress = 0;
    private int lastBugTypeSlot = -1;

    private NonNullList<ItemStack> items = NonNullList.withSize(SIZE, ItemStack.EMPTY);

    public BugCrateBlockEntity(BlockPos pos, BlockState state) {
        this(ModBlockEntities.BUG_CRATE.value(), pos, state);
    }

    public BugCrateBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    /* ---------------------------------------------------------------------
       NAME / SIZE
       --------------------------------------------------------------------- */
    @Override
    protected Component getDefaultName() {
        return Component.literal("Bug Crate");
    }

    @Override
    public int getContainerSize() {
        return SIZE;
    }

    /* ---------------------------------------------------------------------
       DATA SYNC
       --------------------------------------------------------------------- */
    private final ContainerData dataAccess = new ContainerData() {
        @Override
        public int get(int index) {
            return index == 0 ? breedProgress : BREED_TICKS;
        }

        @Override
        public void set(int index, int value) {
            if (index == 0) breedProgress = value;
        }

        @Override
        public int getCount() {
            return 2;
        }
    };

    public ContainerData getDataAccess() {
        return dataAccess;
    }

    /* ---------------------------------------------------------------------
       INVENTORY
       --------------------------------------------------------------------- */
    @Override
    protected NonNullList<ItemStack> getItems() {
        return items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> items) {
        this.items = items;
    }

    /* ---------------------------------------------------------------------
       MENU
       --------------------------------------------------------------------- */
    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory playerInv) {
        return new BugCrateMenu(id, playerInv, this, this.dataAccess);
    }

    /* ---------------------------------------------------------------------
       SAVE / LOAD (1.21 SIGNATURES)
       --------------------------------------------------------------------- */
    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.items = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
        loadAllItems(tag, this.items, registries);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        saveAllItems(tag, this.items, registries);
    }

    /* ---------------------------------------------------------------------
       SLOT VALIDATION
       --------------------------------------------------------------------- */
    public boolean isItemValidForSlot(int slot, ItemStack stack) {
        if (stack.isEmpty()) return false;

        if (slot >= PLANT_SLOT_START && slot <= PLANT_SLOT_END)
            return FoodHelper.isFoodType(stack.getItem(), FoodType.PLANT);

        if (slot >= INSECT_SLOT_START && slot <= INSECT_SLOT_END)
            return BreedableBug.isBug(stack);

        return false;
    }

    /* ---------------------------------------------------------------------
       TICKING
       --------------------------------------------------------------------- */
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
        boolean hasPlant = false;
        for (int i = PLANT_SLOT_START; i <= PLANT_SLOT_END; i++) {
            if (!getItem(i).isEmpty()
                    && FoodHelper.isFoodType(getItem(i).getItem(), FoodType.PLANT)) {
                hasPlant = true;
                break;
            }
        }

        for (int i = INSECT_SLOT_START; i <= INSECT_SLOT_END; i++) {
            ItemStack bug = getItem(i);
            if (!bug.isEmpty() && BreedableBug.isBug(bug)) {
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

        int plantSlot = getFirstPlantSlot();
        if (plantSlot == -1) return;

        Item target = bugInSlot.getItem();
        int stackSlot = -1;

        for (int i = OUTPUT_SLOT_START; i <= OUTPUT_SLOT_END; i++) {
            ItemStack existing = getItem(i);
            if (!existing.isEmpty()
                    && ItemStack.isSameItemSameComponents(existing, bugInSlot)) {
                stackSlot = i;
                break;
            }
        }

        removeItem(plantSlot, 1);

        if (stackSlot != -1) {
            getItem(stackSlot).grow(1);
            return;
        }

        int emptySlot = getFreeOutputSlot();
        if (emptySlot != -1) {
            setItem(emptySlot, new ItemStack(target, 1));
        } else if (level != null && !level.isClientSide) {
            ItemEntity drop = new ItemEntity(
                    level,
                    worldPosition.getX() + 0.5,
                    worldPosition.getY() + 1.1,
                    worldPosition.getZ() + 0.5,
                    new ItemStack(target, 1)
            );
            level.addFreshEntity(drop);
        }
    }

    /* ---------------------------------------------------------------------
       WORLDLY CONTAINER
       --------------------------------------------------------------------- */
    @Override
    public int[] getSlotsForFace(Direction direction) {
        return IntStream.range(0, SIZE).toArray();
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack stack, Direction direction) {
        return index >= PLANT_SLOT_START && index <= INSECT_SLOT_END;
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        return index >= OUTPUT_SLOT_START && index <= OUTPUT_SLOT_END;
    }

    private int getFirstPlantSlot() {
        for (int i = PLANT_SLOT_START; i <= PLANT_SLOT_END; i++) {
            if (!getItem(i).isEmpty()
                    && FoodHelper.isFoodType(getItem(i).getItem(), FoodType.PLANT)) {
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
