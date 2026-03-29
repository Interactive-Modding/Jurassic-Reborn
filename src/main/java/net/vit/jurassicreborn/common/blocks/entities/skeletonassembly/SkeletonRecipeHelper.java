package net.vit.jurassicreborn.common.blocks.entities.skeletonassembly;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;
import net.vit.jurassicreborn.common.items.Fossils.FossilItem;
import net.vit.jurassicreborn.common.items.ModItems;

/** Helper for matching skeleton assembly recipes on the server side. */
public class SkeletonRecipeHelper {
    private static final int GRID_W = SkeletonAssemblerBlockEntity.GRID_W;
    private static final int GRID_H = SkeletonAssemblerBlockEntity.GRID_H;

    /** Result record returned from {@link #tryMatch}. */
    public record Result(boolean success, ItemStack output, int[] bounds) {}

    /**
     * Try to match the items in the handler against any skeleton recipe.
     * @param items handler containing the 5×5 fossil grid
     * @return matching result information or a failed result
     */
    public static Result tryMatch(IItemHandler items) {
        AssemblyData data = null;
        int minX = GRID_W, minY = GRID_H, maxX = -1, maxY = -1;

        for (int y = 0; y < GRID_H; y++) {
            for (int x = 0; x < GRID_W; x++) {
                ItemStack stack = items.getStackInSlot(x + y * GRID_W);
                AssemblyData d = assemblyOf(stack);
                if (d != null) {
                    if (data != null && !data.equals(d)) {
                        return new Result(false, ItemStack.EMPTY, new int[4]);
                    }
                    data = d;
                    minX = Math.min(minX, x);
                    maxX = Math.max(maxX, x);
                    minY = Math.min(minY, y);
                    maxY = Math.max(maxY, y);
                }
            }
        }

        if (data == null || data.dino.getRecipe() == null) {
            return new Result(false, ItemStack.EMPTY, new int[4]);
        }

        String[][] recipe = data.dino.getRecipe();
        if (maxX - minX + 1 != recipe[0].length || maxY - minY + 1 != recipe.length) {
            return new Result(false, ItemStack.EMPTY, new int[4]);
        }

        for (int y = 0; y < recipe.length; y++) {
            for (int x = 0; x < recipe[0].length; x++) {
                ItemStack in = items.getStackInSlot((x + minX) + (y + minY) * GRID_W);
                if (!recipe[y][x].equals(boneType(in))) {
                    return new Result(false, ItemStack.EMPTY, new int[4]);
                }
            }
        }

        int[] bounds = new int[] {minX, minY, maxX, maxY};
        return new Result(true, data.makeResult(), bounds);
    }

    private static AssemblyData assemblyOf(ItemStack stack) {
        if (stack.getItem() instanceof FossilItem fi) {
            return new AssemblyData(fi.getDinosaur(stack), fi.isFresh());
        }
        return null;
    }

    private static String boneType(ItemStack stack) {
        return stack.getItem() instanceof FossilItem fi ? fi.getBoneType() : "";
    }

    private record AssemblyData(Dinosaur dino, boolean fresh) {
        ItemStack makeResult() {
            if (fresh) {
                return new ItemStack(ModItems.FRESH_SKELETONS.get(dino).get());
            }
            return new ItemStack(ModItems.FOSSIL_SKELETONS.get(dino).get());
        }
    }
}
