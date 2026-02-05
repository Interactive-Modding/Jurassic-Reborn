package net.vit.jurassicreborn.common.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class ParkBenchSeatRightEntity extends ParkBenchSeatBaseEntity {
    public ParkBenchSeatRightEntity(EntityType<? extends ParkBenchSeatRightEntity> type, Level level) {
        super(type, level);
    }

    public ParkBenchSeatRightEntity(Level level, BlockPos benchPos, double xOffset, double zOffset) {
        super(ModEntities.PARK_BENCH_SEAT_RIGHT.get(), level, benchPos, xOffset, zOffset);
    }
}
