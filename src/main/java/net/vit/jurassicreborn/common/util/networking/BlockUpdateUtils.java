package net.vit.jurassicreborn.common.util.networking;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;
import org.checkerframework.checker.units.qual.A;

public class BlockUpdateUtils {
    public static void sendBlockEntityUpdate(Level level, BlockPos pos) {
        if (!(level instanceof ServerLevel server)) {
            return;
        }

        BlockEntity be = level.getBlockEntity(pos);
        if (be != null) {
            server.sendBlockUpdated(pos, be.getBlockState(), be.getBlockState(), 3);
        }
    }
}