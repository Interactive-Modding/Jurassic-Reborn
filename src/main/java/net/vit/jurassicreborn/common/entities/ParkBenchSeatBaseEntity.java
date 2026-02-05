package net.vit.jurassicreborn.common.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import net.vit.jurassicreborn.common.blocks.ModBlocks;

public abstract class ParkBenchSeatBaseEntity extends Entity {
    protected BlockPos benchPos;
    private int age;
    private static final int EMPTY_GRACE_TICKS = 6;

    public ParkBenchSeatBaseEntity(EntityType<?> type, Level level) {
        super(type, level);
        this.noPhysics = true;
        this.setNoGravity(true);
    }

    public ParkBenchSeatBaseEntity(EntityType<?> type, Level level, BlockPos benchPos, double xOffset, double zOffset) {
        this(type, level);
        this.benchPos = benchPos.immutable();
        this.setPos(benchPos.getX() + 0.5 + xOffset,
                benchPos.getY() + 0.30,
                benchPos.getZ() + 0.5 + zOffset);
    }

    @Override protected void defineSynchedData() {}

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        if (tag.contains("BenchX")) {
            this.benchPos = new BlockPos(tag.getInt("BenchX"), tag.getInt("BenchY"), tag.getInt("BenchZ"));
        } else {
            this.benchPos = null;
        }
        this.age = tag.getInt("Age");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        if (this.benchPos != null) {
            tag.putInt("BenchX", this.benchPos.getX());
            tag.putInt("BenchY", this.benchPos.getY());
            tag.putInt("BenchZ", this.benchPos.getZ());
        }
        tag.putInt("Age", this.age);
    }

    @Override public boolean isPickable() { return false; }
    @Override public boolean isPushable() { return false; }

    @Override
    public void tick() {
        super.tick();
        if (!level.isClientSide) {
            age++;

            boolean invalidBench = this.benchPos == null
                    || level.getBlockState(this.benchPos).getBlock() != ModBlocks.PARK_BENCH.get();

            if ((getPassengers().isEmpty() && age > EMPTY_GRACE_TICKS) || invalidBench) {
                discard();
                return;
            }

            Entity rider = this.getFirstPassenger();
            if (rider instanceof LivingEntity living) {
                this.setYRot(living.getYRot());
                this.setXRot(living.getXRot());
                this.yRotO = this.getYRot();
                this.xRotO = this.getXRot();
            }
        }
    }

    @Override
    protected void removePassenger(Entity passenger) {
        super.removePassenger(passenger);
        if (!level.isClientSide) discard();
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
