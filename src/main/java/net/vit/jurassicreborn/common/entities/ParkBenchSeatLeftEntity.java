package net.vit.jurassicreborn.common.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class ParkBenchSeatLeftEntity extends ParkBenchSeatBaseEntity {
    public ParkBenchSeatLeftEntity(EntityType<? extends ParkBenchSeatLeftEntity> type, Level level) {
        super(type, level);
    }

    public ParkBenchSeatLeftEntity(Level level, BlockPos benchPos, double xOffset, double zOffset) {
        super(ModEntities.PARK_BENCH_SEAT_LEFT.get(), level, benchPos, xOffset, zOffset);
    }
}
