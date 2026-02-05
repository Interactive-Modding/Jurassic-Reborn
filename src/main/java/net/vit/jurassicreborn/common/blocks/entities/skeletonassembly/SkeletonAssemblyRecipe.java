/* SkeletonAssemblyRecipe.java */
package net.vit.jurassicreborn.common.blocks.entities.skeletonassembly;

import net.minecraft.world.item.ItemStack;
import java.util.List;

public record SkeletonAssemblyRecipe(List<ItemStack> inputs,
                                     ItemStack       output) {}
