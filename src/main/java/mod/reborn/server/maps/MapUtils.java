package mod.reborn.server.maps;

import com.google.common.collect.Lists;
import net.minecraft.init.Biomes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import mod.reborn.server.world.structure.StructureUtils;

import java.util.Random;

public class MapUtils {

    private static final int MIN_DISTANCE =  5000;
    private static final int MAX_DISTANCE = 10000;


    public static BlockPos getVisitorCenterPosition() {
        StructureUtils.StructureData data = StructureUtils.getStructureData();
        BlockPos pos = data.getVisitorCenterPosition();
        if(pos ==  null) {
            pos = generatePosition();
            data.setVisitorCenterPosition(pos);
        }
        return pos;
    }
    public static BlockPos getIslaSornaLabPosition() {
        StructureUtils.StructureData data = StructureUtils.getStructureData();
        BlockPos pos = data.getIslaSornaLabPosition();
        if(pos ==  null) {
            pos = generateLabPosition();
            data.setIslaSornaLabPosition(pos);
        }
        return pos;
    }

    private static BlockPos generatePosition() {
        World world = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(0);
        Random worldRandom = new Random(world.getSeed());
        int range = MAX_DISTANCE - MIN_DISTANCE;
        BlockPos pos = null;
        for(int i = 0; i < 10000; i++) {
            pos = new BlockPos(worldRandom.nextInt(range) + MIN_DISTANCE * (worldRandom.nextBoolean() ? 1 : -1), 0, worldRandom.nextInt(range) + MIN_DISTANCE * (worldRandom.nextBoolean() ? 1 : -1));
            if(Lists.newArrayList(Biomes.JUNGLE, Biomes.MUTATED_JUNGLE, Biomes.JUNGLE_EDGE, Biomes.MUTATED_JUNGLE_EDGE, Biomes.SAVANNA, Biomes.MUTATED_SAVANNA).contains(world.getBiome(pos))) {
                return pos;
            }
        }
        return pos;
    }
    private static BlockPos generateLabPosition() {
        World world = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(0);
        Random worldRandom = new Random(world.getSeed()-409283);
        int range = MAX_DISTANCE - MIN_DISTANCE;
        BlockPos pos = null;
        for(int i = 0; i < 10000; i++) {
            pos = new BlockPos(worldRandom.nextInt(range) + MIN_DISTANCE * (worldRandom.nextBoolean() ? 1 : -1), 0, worldRandom.nextInt(range) + MIN_DISTANCE * (worldRandom.nextBoolean() ? 1 : -1));
            if(Lists.newArrayList(Biomes.JUNGLE, Biomes.MUTATED_JUNGLE, Biomes.JUNGLE_EDGE, Biomes.MUTATED_JUNGLE_EDGE, Biomes.SAVANNA, Biomes.MUTATED_SAVANNA).contains(world.getBiome(pos))) {
                return pos;
            }
        }
        return pos;
    }
}