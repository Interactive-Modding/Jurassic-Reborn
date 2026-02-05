package net.vit.jurassicreborn.common.worldgen.structure;

import net.minecraft.core.BlockPos;
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
import net.vit.jurassicreborn.JurassicReborn;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DesertDigsiteGenerator extends StructureGenerator {
    private static final ResourceLocation STRUCTURE = JurassicReborn.resource("desert_digsite");
    private static final Map<String, ResourceLocation> LOOT_TABLES = new HashMap<>();
    static {
        LOOT_TABLES.put("RaptorChest", new ResourceLocation(JurassicReborn.MODID, "structure/raptor_chest"));
        LOOT_TABLES.put("GroundStorage", new ResourceLocation(JurassicReborn.MODID, "structure/visitor_centre/ground_storage"));
        LOOT_TABLES.put("Kitchen", new ResourceLocation(JurassicReborn.MODID, "structure/visitor_centre/kitchen"));
        LOOT_TABLES.put("DiningHall", new ResourceLocation(JurassicReborn.MODID, "structure/visitor_centre/dining_hall"));
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
            String type = info.nbt.getString("metadata");
            ResourceLocation lootTable = LOOT_TABLES.get(type);
            if (lootTable != null) {
                level.setBlock(info.pos, Blocks.AIR.defaultBlockState(), 3);
                BlockEntity tile = level.getBlockEntity(info.pos.below());
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
