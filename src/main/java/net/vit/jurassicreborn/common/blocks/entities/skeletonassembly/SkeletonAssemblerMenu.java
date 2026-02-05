package net.vit.jurassicreborn.common.blocks.entities.skeletonassembly;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.vit.jurassicreborn.common.blocks.entities.skeletonassembly.SkeletonAssemblerBlockEntity;
import net.vit.jurassicreborn.common.blocks.ModBlocks;
import net.vit.jurassicreborn.common.blocks.entities.ModMenuTypes;
import net.vit.jurassicreborn.common.items.Fossils.FossilItem;
import net.vit.jurassicreborn.common.items.ModItems;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;
import org.jetbrains.annotations.NotNull;

/**
 * 5 × 5 fossil grid + 1 result slot menu for the Skeleton Assembler block.
 */
public class SkeletonAssemblerMenu extends AbstractContainerMenu {

    /* ---------- registry helper -------------------------------------- */

    /* ---------- slot indices ------------------------------------------ */
    private static final int RESULT_SLOT  = SkeletonAssemblerBlockEntity.RESULT_SLOT;
    private static final int RESULT_IDX   = 0;
    private static final int GRID_FIRST   = 1;   // menu index of first grid slot
    private static final int GRID_LAST    = GRID_FIRST + SkeletonAssemblerBlockEntity.GRID_W * SkeletonAssemblerBlockEntity.GRID_H - 1;
    private static final int INV_FIRST    = GRID_LAST + 1;
    private static final int HOTBAR_FIRST = INV_FIRST + 27;  // 53-61

    /* ---------- grid size --------------------------------------------- */
    private static final int GRID_W = 5;
    private static final int GRID_H = 5;

    /* ---------- inventories ------------------------------------------ */
    private final Container grid;       // 25 fossil slots + result slot
    private final Container result;     // alias for output slot
    private final ContainerLevelAccess access;

    /* ================================================================== */
    /*  CONSTRUCTORS                                                      */
    /* ================================================================== */

    /** Server-side constructor */
    public SkeletonAssemblerMenu(int id,
                                 Inventory playerInv,
                                 IItemHandlerModifiable handler,
                                 ContainerData syncData,
                                 BlockPos pos) {

        super(ModMenuTypes.SKELETON_ASSEMBLER.get(), id);
        this.access = ContainerLevelAccess.create(playerInv.player.level, pos);

        /* handler provides both grid and result slot */
        this.grid   = (Container) handler;
        this.result = (Container) handler;
        if (handler instanceof SkeletonAssemblerItemHandler sa)
            sa.setChangeListener(() -> this.slotsChanged(sa));

        this.addDataSlots(syncData);

        /* result slot (index 0) */
        this.addSlot(new SkeletonResultSlot(playerInv.player,
                grid, result,
                RESULT_SLOT, 140, 52));

        /* 5 × 5 fossil input grid (indices 1-25) */
        for (int y = 0; y < GRID_H; y++) {
            for (int x = 0; x < GRID_W; x++) {
                int idx = x + y * GRID_W; // handler slot index
                this.addSlot(new FossilCraftSlot(grid, idx,
                        16 + x * 18,
                        16 + y * 18));
            }
        }

        /* player inventory (26-52) */
        for (int row = 0; row < 3; row++)
            for (int col = 0; col < 9; col++)
                this.addSlot(new Slot(playerInv,
                        col + row * 9 + 9,
                        8 + col * 18,
                        119 + row * 18));

        /* hot-bar (53-61) */
        for (int col = 0; col < 9; col++)
            this.addSlot(new Slot(playerInv, col,
                    8 + col * 18, 177));

        /* prime result slot */
        if (grid.getItem(RESULT_SLOT).isEmpty()) {
            ItemStack result = computeResult();
            if (!result.isEmpty()) grid.setItem(RESULT_SLOT, result);
        }
    }

    /** Client-side constructor – receives BlockPos via buffer */
    public SkeletonAssemblerMenu(int id, Inventory inv, FriendlyByteBuf buf) {
        /* create dummy handler/data for client; real inventory syncs via net */
        this(id,
                inv,
                new SkeletonAssemblerItemHandler(),
                new SimpleContainerData(2),
                buf.readBlockPos());
    }

    /* ================================================================== */
    /*  RECIPE / RESULT LOGIC                                             */
    /* ================================================================== */

    @Override
    public void slotsChanged(Container changed) {
        if (changed == grid) {
            ItemStack current = grid.getItem(RESULT_SLOT);
            ItemStack result  = computeResult();

            if (current.isEmpty()) {
                if (!result.isEmpty()) grid.setItem(RESULT_SLOT, result);
            } else if (result.isEmpty()) {
                grid.setItem(RESULT_SLOT, ItemStack.EMPTY);
            }
        }
        super.slotsChanged(changed);
    }

    /** Build the finished skeleton item if the grid matches a recipe. */
    private ItemStack computeResult() {
        AssemblyData data = findAssemblyData();
        if (data == null || data.dino.getRecipe() == null) return ItemStack.EMPTY;

        String[][] recipe = data.dino.getRecipe();
        Bounds b = boundsOfBones();
        if (b.width() + 1 != recipe[0].length || b.height() + 1 != recipe.length)
            return ItemStack.EMPTY;

        for (int y = 0; y < recipe.length; y++)
            for (int x = 0; x < recipe[0].length; x++) {
                ItemStack in = grid.getItem((x + b.minX)
                        + (y + b.minY) * GRID_W);
                if (!recipe[y][x].equals(boneType(in))) return ItemStack.EMPTY;
            }
        return data.makeResult();
    }

    /** detect which dinosaur (and fresh/fossil) is being assembled */
    private AssemblyData findAssemblyData() {
        AssemblyData found = null;
        for (int i = 0; i < GRID_W * GRID_H; i++) {
            ItemStack stack = grid.getItem(i);
            AssemblyData d = assemblyOf(stack);
            if (d != null) {
                if (found != null && !found.equals(d)) return null; // mixed bones
                found = d;
            }
        }
        return found;
    }

    private AssemblyData assemblyOf(ItemStack stack) {
        if (stack.getItem() instanceof FossilItem fi)
            return new AssemblyData(fi.getDinosaur(stack), fi.isFresh());
        return null;
    }

    private String boneType(ItemStack stack) {
        return stack.getItem() instanceof FossilItem fi ? fi.getBoneType() : "";
    }

    private Bounds boundsOfBones() {
        int minX = GRID_W, minY = GRID_H, maxX = -1, maxY = -1;
        for (int y = 0; y < GRID_H; y++)
            for (int x = 0; x < GRID_W; x++) {
                if (assemblyOf(grid.getItem(x + y * GRID_W)) != null) {
                    minX = Math.min(minX, x); maxX = Math.max(maxX, x);
                    minY = Math.min(minY, y); maxY = Math.max(maxY, y);
                }
            }
        return new Bounds(minX, minY, maxX, maxY);
    }

    /* ================================================================== */
    /*  SHIFT-CLICK HANDLING                                              */
    /* ================================================================== */

    @Override
    public @NotNull ItemStack quickMoveStack(Player player, int idx) {
        Slot slot = this.slots.get(idx);
        if (!slot.hasItem()) return ItemStack.EMPTY;

        ItemStack stack = slot.getItem();
        ItemStack copy  = stack.copy();

        if (idx == RESULT_IDX) {                          // result → player
            if (!moveItemStackTo(stack, INV_FIRST, HOTBAR_FIRST + 9, true))
                return ItemStack.EMPTY;
            slot.onQuickCraft(stack, copy);
            slot.onTake(player, stack);                    // clear grid inputs
        } else if (idx >= INV_FIRST) {                    // player → grid
            if (!moveItemStackTo(stack, GRID_FIRST, GRID_LAST + 1, false))
                return ItemStack.EMPTY;
        } else {                                          // grid → player
            if (!moveItemStackTo(stack, INV_FIRST, HOTBAR_FIRST + 9, false))
                return ItemStack.EMPTY;
        }

        if (stack.isEmpty()) slot.set(ItemStack.EMPTY);
        else slot.setChanged();

        return copy;
    }

    /* ================================================================== */
    /*  VALIDITY CHECK                                                    */
    /* ================================================================== */

    @Override
    public boolean stillValid(@NotNull Player p) {
        return access.evaluate((lvl, pos) ->
                        lvl.getBlockState(pos).is(ModBlocks.SKELETON_ASSEMBLY.get())
                                && p.distanceToSqr(pos.getX() + 0.5D,
                                pos.getY() + 0.5D,
                                pos.getZ() + 0.5D) <= 64D,
                true);
    }

    /* ================================================================== */
    /*  HELPER RECORDS                                                    */
    /* ================================================================== */

    private record Bounds(int minX, int minY, int maxX, int maxY) {
        int width()  { return maxX - minX; }
        int height() { return maxY - minY; }
    }

    private record AssemblyData(Dinosaur dino, boolean fresh) {
        ItemStack makeResult() {
            return new ItemStack(
                    fresh ? ModItems.FRESH_SKELETONS.get(dino).get()
                            : ModItems.FOSSIL_SKELETONS.get(dino).get());
        }
    }
}
