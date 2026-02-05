package net.vit.jurassicreborn.common.worldgen.structure;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.vit.jurassicreborn.JurassicReborn;

import java.util.List;

/**
 * Generates an abandoned paddock.
 */
public class AbandonedPaddockGenerator extends StructureGenerator {
    private static final ResourceLocation STRUCTURE = JurassicReborn.resource("abandoned_paddock");

    public AbandonedPaddockGenerator(RandomSource rand) {
        super(rand, 32, 24, 40);
    }

    @Override
    protected void generateStructure(ServerLevel level, RandomSource random, BlockPos position) {
        StructureTemplateManager manager = level.getStructureManager();
        StructurePlaceSettings settings = new StructurePlaceSettings().setRotation(this.rotation).setMirror(this.mirror);
        StructureTemplate template = manager.getOrCreate(STRUCTURE);
        List<StructureTemplate.StructureBlockInfo> dataBlocks = template.filterBlocks(position, settings, Blocks.STRUCTURE_BLOCK);
        template.placeInWorld(level, position, position, settings, random, 4);
        for (StructureTemplate.StructureBlockInfo info : dataBlocks) {
            String type = info.nbt.getString("metadata"); // Use 'nbt' as a field, not method!
            if ("Chest".equals(type)) {
                level.setBlock(info.pos, Blocks.AIR.defaultBlockState(), 3);
                BlockEntity tile = level.getBlockEntity(info.pos.below());
                if (tile instanceof ChestBlockEntity chest) {
                    chest.setLootTable(BuiltInLootTables.VILLAGE_WEAPONSMITH, random.nextLong());
                }
            }
        }
    }

    @Override
    public BlockPos getLevelPosition() {
        return new BlockPos(0, 3, 0);
    }
}
