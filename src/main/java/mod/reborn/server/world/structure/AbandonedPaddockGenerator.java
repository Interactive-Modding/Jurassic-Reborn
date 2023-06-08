package mod.reborn.server.world.structure;

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

import java.util.Map;
import java.util.Random;

public class AbandonedPaddockGenerator extends StructureGenerator {
    private static final ResourceLocation STRUCTURE = new ResourceLocation(RebornMod.MODID, "abandoned_paddock");

    public AbandonedPaddockGenerator (Random rand) {
        super(rand, 32, 24, 40);
    }

    @Override
    protected void generateStructure(World world, Random random, BlockPos position) {
        MinecraftServer server = world.getMinecraftServer();
        TemplateManager templateManager = world.getSaveHandler().getStructureTemplateManager();
        PlacementSettings settings = new PlacementSettings().setRotation(this.rotation).setMirror(this.mirror);
        Template template = templateManager.getTemplate(server, STRUCTURE);
        Map<BlockPos, String> dataBlocks = template.getDataBlocks(position, settings);
        template.addBlocksToWorldChunk(world, position, settings);
        for (Map.Entry<BlockPos, String> entry : dataBlocks.entrySet()) {
            String type = entry.getValue();
            BlockPos dataPos = entry.getKey();
            if (type.equals("Chest")) {
                world.setBlockState(dataPos, Blocks.AIR.getDefaultState(), 3);
                TileEntity tile = world.getTileEntity(dataPos.down());
                if (tile instanceof TileEntityChest) {
                    ((TileEntityChest) tile).setLootTable(LootTableList.CHESTS_VILLAGE_BLACKSMITH, random.nextLong()); //TODO Proper loottable
                }
            }
        }
    }
    @Override
    public int getOffsetY() {
        return -1;
    }
}
