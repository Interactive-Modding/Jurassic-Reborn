package net.vit.jurassicreborn.client.render.item;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.client.multiplayer.ClientLevel; // <-- use ClientLevel
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.model.BakedModelWrapper;
import org.jetbrains.annotations.Nullable;

/**
 * Wraps the generated storage disc model so we can swap baked variants based on the disc's CustomModelData.
 */
public final class StorageDiscBakedModel extends BakedModelWrapper<BakedModel> {
    private final Int2ObjectMap<BakedModel> variantModels;
    private final ItemOverrides overrides;

    public StorageDiscBakedModel(BakedModel base, Int2ObjectMap<BakedModel> variantModels) {
        super(base);
        this.variantModels = variantModels;
        this.overrides = new Overrides(base.getOverrides());
    }

    @Override
    public ItemOverrides getOverrides() {
        return overrides;
    }

    private BakedModel resolveModel(ItemStack stack, @Nullable ClientLevel level,
                                    @Nullable LivingEntity entity, int seed) {
        int modelId = stack.hasTag() ? stack.getTag().getInt("CustomModelData") : 0;
        if (modelId > 0) {
            BakedModel model = variantModels.get(modelId);
            if (model != null) {
                return model;
            }
        }
        return originalModel;
    }

    private final class Overrides extends ItemOverrides {
        private final ItemOverrides parent;

        private Overrides(ItemOverrides parent) {
            this.parent = parent;
        }

        @Override
        public BakedModel resolve(BakedModel originalModel, ItemStack stack,
                                  @Nullable ClientLevel level, @Nullable LivingEntity livingEntity, int seed) {
            // Defer to parent first (handles predicates from JSON etc.)
            BakedModel resolved = parent.resolve(StorageDiscBakedModel.this.originalModel, stack, level, livingEntity, seed);
            if (resolved != StorageDiscBakedModel.this.originalModel) {
                return resolved;
            }
            // Then apply our variant swap by CustomModelData
            return resolveModel(stack, level, livingEntity, seed);
        }
    }
}
