package net.vit.jurassicreborn.common.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

public class BlockPosUtil {

    public static Vec3 blockPosToVec(BlockPos pos){
        return new Vec3(pos.getX(), pos.getY(), pos.getZ());
    }

    public static BlockPos vecToBlockPos(Vec3 vec){
        return new BlockPos(vec.x, vec.y, vec.z);
    }
}
