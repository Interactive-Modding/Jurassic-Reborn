package mod.reborn.server.world.structure;

import mod.reborn.server.world.loot.Loot;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;
import net.minecraft.world.storage.loot.LootTableList;
import mod.reborn.RebornMod;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class DesertDigsiteGenerator extends StructureGenerator {
    private static final ResourceLocation STRUCTURE = new ResourceLocation(RebornMod.MODID, "desert_digsite");

    public DesertDigsiteGenerator(Random rand) {
        super(rand, 47, 20, 33);
    }

    private static final Map<String, ResourceLocation> LOOT_TABLES = new HashMap<>();

    static {
        LOOT_TABLES.put("RaptorChest", Loot.RAPTOR_CHEST);
        LOOT_TABLES.put("GroundStorage", Loot.VISITOR_GROUND_STORAGE);
        LOOT_TABLES.put("Kitchen", Loot.VISITOR_KITCHEN);
        LOOT_TABLES.put("DiningHall", Loot.VISITOR_DINING_HALL);
    }
    @Override
    protected void generateStructure(World world, Random random, BlockPos position) {
        MinecraftServer server = world.getMinecraftServer();
        TemplateManager templateManager = world.getSaveHandler().getStructureTemplateManager();
        PlacementSettings settings = new PlacementSettings();
        settings.setRotation(this.rotation);
        settings.setMirror(this.mirror);
        settings.setRandom(random);
        Template template = templateManager.getTemplate(server, STRUCTURE);
        template.addBlocksToWorldChunk(world, position, settings);
        Map<BlockPos, String> dataBlocks = template.getDataBlocks(position, settings);
        for (Map.Entry<BlockPos, String> entry : dataBlocks.entrySet()) {
            String type = entry.getValue();
            BlockPos dataPos = entry.getKey();
            ResourceLocation lootTable = LOOT_TABLES.get(type);
            if (lootTable != null) {
                world.setBlockToAir(dataPos);
                TileEntity tile = world.getTileEntity(dataPos.down());
                if (tile instanceof TileEntityChest) {
                    ((TileEntityChest) tile).setLootTable(lootTable, random.nextLong());
                }
            }
        }
    }
    @Nullable
    @Override
    public BlockPos getLevelPosition() {
        return new BlockPos(0, 14,0);
    }
}
