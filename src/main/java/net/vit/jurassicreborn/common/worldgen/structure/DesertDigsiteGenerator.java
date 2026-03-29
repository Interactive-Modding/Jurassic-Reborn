package net.vit.jurassicreborn.common.worldgen.structure;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraft.world.level.storage.loot.LootTable;
import net.vit.jurassicreborn.JurassicReborn;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DesertDigsiteGenerator extends StructureGenerator {
    private static final ResourceLocation STRUCTURE = JurassicReborn.resource("desert_digsite");
    private static final Map<String, ResourceKey<LootTable>> LOOT_TABLES = new HashMap<>();
    static {
        LOOT_TABLES.put("RaptorChest", ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.parse(JurassicReborn.MODID + ":" + "structure/raptor_chest")));
        LOOT_TABLES.put("GroundStorage", ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.parse(JurassicReborn.MODID + ":" + "structure/visitor_centre/ground_storage")));
        LOOT_TABLES.put("Kitchen", ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.parse(JurassicReborn.MODID + ":" + "structure/visitor_centre/kitchen")));
        LOOT_TABLES.put("DiningHall", ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.parse(JurassicReborn.MODID + ":" + "structure/visitor_centre/dining_hall")));
    }

    public DesertDigsiteGenerator(RandomSource rand) {
        super(rand, 47, 20, 33);
    }

    @Override
    protected void generateStructure(ServerLevel level, RandomSource random, BlockPos position) {
        StructureTemplateManager manager = level.getStructureManager();
        StructurePlaceSettings settings = new StructurePlaceSettings();
        settings.setRotation(this.rotation);
        settings.setMirror(this.mirror);
        settings.setRandom(random);
        StructureTemplate template = manager.getOrCreate(STRUCTURE);
        template.placeInWorld(level, position, position, settings, random, 4);
        List<StructureTemplate.StructureBlockInfo> dataBlocks = template.filterBlocks(position, settings, Blocks.STRUCTURE_BLOCK);
        for (StructureTemplate.StructureBlockInfo info : dataBlocks) {
            if (info.nbt() == null) continue;
            String type = info.nbt().getString("metadata");
            ResourceKey<LootTable> lootTable = LOOT_TABLES.get(type);
            if (lootTable != null) {
                BlockPos infoPos = info.pos();
                level.setBlock(infoPos, Blocks.AIR.defaultBlockState(), 3);
                BlockEntity tile = level.getBlockEntity(infoPos.below());
                if (tile instanceof ChestBlockEntity chest) {
                    chest.setLootTable(lootTable, random.nextLong());
                }
            }
        }
    }

    @Override
    public BlockPos getLevelPosition() {
        return new BlockPos(0, 14, 0);
    }
}
