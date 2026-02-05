package net.vit.jurassicreborn.common.worldgen.villager;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.saveddata.maps.MapDecoration;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.Random;

/**
 * Custom cartographer trade that mirrors the vanilla treasure map behaviour but targets JR structures.
 */
public class JurassicStructureMapForEmeralds implements VillagerTrades.ItemListing {
    private static final float PRICE_MULTIPLIER = 0.2F;

    private final int emeraldCost;
    private final net.minecraft.resources.ResourceKey<ConfiguredStructureFeature<?, ?>> structureKey;
    private final MapDecoration.Type markerType;
    private final int maxUses;
    private final int villagerXp;
    private final String translationKey;

    public JurassicStructureMapForEmeralds(int emeraldCost,
                                           net.minecraft.resources.ResourceKey<ConfiguredStructureFeature<?, ?>> structureKey,
                                           String translationKey) {
        this(emeraldCost, structureKey, MapDecoration.Type.RED_X, 12, 5, translationKey);
    }

    public JurassicStructureMapForEmeralds(int emeraldCost,
                                           net.minecraft.resources.ResourceKey<ConfiguredStructureFeature<?, ?>> structureKey,
                                           MapDecoration.Type markerType,
                                           int maxUses,
                                           int villagerXp,
                                           String translationKey) {
        this.emeraldCost = emeraldCost;
        this.structureKey = structureKey;
        this.markerType = markerType;
        this.maxUses = maxUses;
        this.villagerXp = villagerXp;
        this.translationKey = translationKey;
    }

    @Override
    public MerchantOffer getOffer(Entity trader, Random random) {
        if (!(trader.level instanceof ServerLevel serverLevel)) {
            return null;
        }

        Registry<ConfiguredStructureFeature<?, ?>> registry = serverLevel.registryAccess().registryOrThrow(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY);

        Optional<Holder<ConfiguredStructureFeature<?, ?>>> structureHolder = registry.getHolder(this.structureKey);
        if (structureHolder.isEmpty()) {
            return null;
        }

        HolderSet<ConfiguredStructureFeature<?, ?>> structures = HolderSet.direct(structureHolder.get());
        ChunkGenerator generator = serverLevel.getChunkSource().getGenerator();

        Optional<com.mojang.datafixers.util.Pair<BlockPos, Holder<ConfiguredStructureFeature<?, ?>>>> result = findNearestStructure(generator, serverLevel, structures, trader.blockPosition());

        if (result.isEmpty()) {
            return null;
        }

        BlockPos structurePos = result.get().getFirst();

        ItemStack map = MapItem.create(serverLevel, structurePos.getX(), structurePos.getZ(), (byte) 2, true, true);
        MapItem.renderBiomePreviewMap(serverLevel, map);
        MapItemSavedData.addTargetDecoration(map, structurePos, "+", this.markerType);
        map.setHoverName(new TranslatableComponent(this.translationKey));

        return new MerchantOffer(
                new ItemStack(Items.EMERALD, this.emeraldCost),
                new ItemStack(Items.COMPASS),
                map,
                this.maxUses,
                this.villagerXp,
                PRICE_MULTIPLIER
        );
}

    @SuppressWarnings("unchecked")
    private Optional<com.mojang.datafixers.util.Pair<BlockPos, Holder<ConfiguredStructureFeature<?, ?>>>> findNearestStructure(ChunkGenerator generator,
                                                                                                                             ServerLevel level,
                                                                                                                             HolderSet<ConfiguredStructureFeature<?, ?>> structures,
                                                                                                                             BlockPos origin) {
        try {
            Method method = generator.getClass().getMethod("findNearestMapStructure", ServerLevel.class, HolderSet.class, BlockPos.class, int.class, boolean.class);
            Object result = method.invoke(generator, level, structures, origin, 100, true);
            return Optional.ofNullable((com.mojang.datafixers.util.Pair<BlockPos, Holder<ConfiguredStructureFeature<?, ?>>>) result);
        } catch (NoSuchMethodException ignored) {
        } catch (IllegalAccessException | InvocationTargetException exception) {
            exception.printStackTrace();
            return Optional.empty();
        }

        try {
            Method method = generator.getClass().getMethod("findNearestMapStructure", ServerLevel.class, HolderSet.class, BlockPos.class, int.class, boolean.class, long.class);
            Object result = method.invoke(generator, level, structures, origin, 100, true, level.getSeed());
            return Optional.ofNullable((com.mojang.datafixers.util.Pair<BlockPos, Holder<ConfiguredStructureFeature<?, ?>>>) result);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException exception) {
            exception.printStackTrace();
        }

        return Optional.empty();
    }
}

