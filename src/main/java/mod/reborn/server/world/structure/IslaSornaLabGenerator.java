package mod.reborn.server.world.structure;

import mod.reborn.server.world.loot.Loot;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;
import mod.reborn.RebornMod;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class IslaSornaLabGenerator extends StructureGenerator {
    private static final ResourceLocation STRUCTURE = new ResourceLocation(RebornMod.MODID, "isla_sorna_lab");
    private static final Map<String, ResourceLocation> LOOT_TABLES = new HashMap<>();

    static {
        LOOT_TABLES.put("GroundStorage", Loot.VISITOR_GROUND_STORAGE);
        LOOT_TABLES.put("ControlRoom", Loot.VISITOR_CONTROL_ROOM);
        LOOT_TABLES.put("Laboratory", Loot.VISITOR_LABORATORY);
        LOOT_TABLES.put("Kitchen", Loot.VISITOR_KITCHEN);
        LOOT_TABLES.put("DormTower", Loot.VISITOR_DORM_TOWER);
        LOOT_TABLES.put("DiningHall", Loot.VISITOR_DINING_HALL);
    }

    public IslaSornaLabGenerator (Random rand) {
        super(rand, 111, 50, 205);
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
        return new BlockPos(0, 4,0);
    }
}
