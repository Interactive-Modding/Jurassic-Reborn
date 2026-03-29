package net.vit.jurassicreborn.common.worldgen.villager;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraft.world.level.saveddata.maps.MapDecorationType;
import net.minecraft.world.level.saveddata.maps.MapDecorationTypes;

import java.util.Optional;

/**
 * Custom cartographer trade that mirrors the vanilla treasure map behaviour but targets JR structures.
 */
public class JurassicStructureMapForEmeralds implements VillagerTrades.ItemListing {
    private static final float PRICE_MULTIPLIER = 0.2F;

    private final int emeraldCost;
    private final ResourceKey<Structure> structureKey;
    private final Holder<MapDecorationType> markerType;
    private final int maxUses;
    private final int villagerXp;
    private final String translationKey;

    public JurassicStructureMapForEmeralds(int emeraldCost,
                                           ResourceKey<Structure> structureKey,
                                           String translationKey) {
        this(emeraldCost, structureKey, MapDecorationTypes.TARGET_X, 12, 5, translationKey);
    }

    public JurassicStructureMapForEmeralds(int emeraldCost,
                                           ResourceKey<Structure> structureKey,
                                           Holder<MapDecorationType> markerType,
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
    public MerchantOffer getOffer(Entity trader, RandomSource random) {
        if (!(trader.level() instanceof ServerLevel serverLevel)) {
            return null;
        }

        var registry = serverLevel.registryAccess().registryOrThrow(Registries.STRUCTURE);
        var structureHolder = registry.getHolder(this.structureKey);
        if (structureHolder.isEmpty()) return null;

        HolderSet<Structure> structures = HolderSet.direct(structureHolder.get());
        ChunkGenerator generator = serverLevel.getChunkSource().getGenerator();

        var result = Optional.ofNullable(
                generator.findNearestMapStructure(
                        serverLevel,
                        structures,
                        trader.blockPosition(),
                        100,
                        true
                )
        );
        if (result.isEmpty()) return null;

        BlockPos structurePos = result.get().getFirst();

        ItemStack map = MapItem.create(serverLevel, structurePos.getX(), structurePos.getZ(), (byte) 2, true, true);
        MapItemSavedData mapData = MapItem.getSavedData(map, serverLevel);
        if (mapData == null) return null;

        MapItem.renderBiomePreviewMap(serverLevel, map);
        MapItemSavedData.addTargetDecoration(map, structurePos, "+", this.markerType);
        mapData.setDirty();
        map.set(DataComponents.CUSTOM_NAME, Component.translatable(this.translationKey));

        return new MerchantOffer(
                new ItemCost(Items.EMERALD, this.emeraldCost),
                Optional.of(new ItemCost(Items.COMPASS, 1)),
                map,
                this.maxUses,
                this.villagerXp,
                PRICE_MULTIPLIER
        );
    }
}
