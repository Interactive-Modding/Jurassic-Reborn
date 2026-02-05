package net.vit.jurassicreborn.common;

import com.google.common.collect.ImmutableMap;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;

import java.util.*;

public class CommonRegistries {

    public static List<OreConfiguration.TargetBlockState> ORE_FAUNA_FOSSIL_LIST;

    public static List<OreConfiguration.TargetBlockState> ORE_AMBER_LIST;

    public static List<OreConfiguration.TargetBlockState> ORE_ICE_SHARD_LIST;

    public static List<OreConfiguration.TargetBlockState> ORE_FLORA_FOSSIL_LIST;

    public static List<OreConfiguration.TargetBlockState> ORE_GYPSUM_STONE_LIST;
    public static void addBlocksToBlockEntity(BlockEntityType<?> type, List<Block> blocks) {
        Set<Block> typeBlocks = type.validBlocks;
        List<Block> mutable = new ArrayList<>(typeBlocks);

        for (Block block : blocks) {
            if (!mutable.contains(block))
                mutable.add(block);
        }

        type.validBlocks = new HashSet<>(mutable);
    }

    public static void addLogsToStrippables(HashMap<Block, Block> logStrippedMap){
        Map<Block, Block> initialMap = AxeItem.STRIPPABLES;
        ImmutableMap.Builder<Block, Block> builder = new ImmutableMap.Builder<>();
        for(Block b : initialMap.keySet()){
            builder.put(b, initialMap.get(b));
        }
        for(Block b : logStrippedMap.keySet()){
            builder.put(b, logStrippedMap.get(b));
        }
        ImmutableMap<Block, Block> map = null;
        try{
            map = builder.build();
        }catch(IllegalArgumentException e){
            System.out.println(e);
        }
        if(map != null) {
            AxeItem.STRIPPABLES = builder.build();
        }

    }

    public static void init(){}

}
