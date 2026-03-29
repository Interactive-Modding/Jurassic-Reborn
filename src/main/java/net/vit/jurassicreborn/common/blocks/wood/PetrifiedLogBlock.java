package net.vit.jurassicreborn.common.blocks.wood;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.core.registries.BuiltInRegistries;
import net.vit.jurassicreborn.common.plants.Plant;
import net.vit.jurassicreborn.common.util.api.GrindableItem;

import java.util.List;
import java.util.Locale;
import java.util.Random;
import net.vit.jurassicreborn.common.util.ItemStackNbtUtil;

/**
 * Petrified log block that can be ground in the fossil grinder.
 */
public class PetrifiedLogBlock extends RotatedPillarBlock implements GrindableItem {
    private final Plant plant;

    public PetrifiedLogBlock(Plant plant, BlockBehaviour.Properties properties) {
        super(properties);
        this.plant = plant;
    }

    @Override
    public boolean isGrindable(ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack getGroundItem(ItemStack stack, Random random) {
        CompoundTag tag = ItemStackNbtUtil.getTag(stack);
        int outputType = random.nextInt(4);

        if (outputType == 3) {
            ItemStack tissue = getTissueItem();
            if (!tissue.isEmpty()) {
                ItemStackNbtUtil.setTag(tissue, tag);
                return tissue;
            }
        } else if (outputType < 2) {
            return new ItemStack(Items.BONE_MEAL);
        }
        return new ItemStack(Items.FLINT);
    }

    @Override
    public List<Pair<Float, ItemStack>> getChancedOutputs(ItemStack inputItem) {
        List<Pair<Float, ItemStack>> list = Lists.newArrayList();
        CompoundTag tag = ItemStackNbtUtil.getTag(inputItem);
        float single = 100F / 4F;

        ItemStack tissue = getTissueItem();
        if (!tissue.isEmpty()) {
            ItemStackNbtUtil.setTag(tissue, tag);
            list.add(Pair.of(single, tissue));
        }
        list.add(Pair.of(50f, new ItemStack(Items.BONE_MEAL)));
        list.add(Pair.of(single, new ItemStack(Items.FLINT)));
        return list;
    }

    private ItemStack getTissueItem() {
        String id = plant.getFormattedName().toLowerCase(Locale.ROOT).replace(" ", "_");
        ResourceLocation tissueId = ResourceLocation.fromNamespaceAndPath("jurassicreborn", "soft_tissue/plants/soft_tissue_" + id);
        Item item = BuiltInRegistries.ITEM.get(tissueId);
        return item == null || item == Items.AIR ? ItemStack.EMPTY : new ItemStack(item);
    }
}
