package net.vit.jurassicreborn.common.blocks.entities.skeletonassembly;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

import static net.vit.jurassicreborn.common.blocks.entities.skeletonassembly.SkeletonAssemblerBlockEntity.GRID_W;

/**
 * Result slot: when the player takes the assembled skeleton,
 * clear the 5×5 fossil grid.
 */
public class SkeletonResultSlot extends Slot {

    private final Container craftGrid;          // the 5×5 matrix

    public SkeletonResultSlot(Player player,
                              Container craftGrid,
                              Container resultInv,
                              int index, int x, int y) {
        super(resultInv, index, x, y);
        this.craftGrid = craftGrid;
    }

    /** Player took the finished display block → clear inputs. */
    @Override
    public void onTake(Player player, ItemStack stack) {
        if (craftGrid instanceof IItemHandler handler) {
            SkeletonRecipeHelper.Result res = SkeletonRecipeHelper.tryMatch(handler);
            if (res.success()) {
                int[] b = res.bounds();
                for (int y = b[1]; y <= b[3]; y++)
                    for (int x = b[0]; x <= b[2]; x++)
                        craftGrid.setItem(x + y * GRID_W, ItemStack.EMPTY);
            } else {
                for (int i = 0; i < craftGrid.getContainerSize(); i++)
                    craftGrid.setItem(i, ItemStack.EMPTY);
            }
        } else {
            for (int i = 0; i < craftGrid.getContainerSize(); i++)
                craftGrid.setItem(i, ItemStack.EMPTY);
        }
        super.onTake(player, stack);
    }

    /* Nothing can ever be placed directly in the output slot */
    @Override
    public boolean mayPlace(ItemStack stack) {
        return false;
    }
}
