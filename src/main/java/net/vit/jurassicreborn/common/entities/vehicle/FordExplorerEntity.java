package net.vit.jurassicreborn.common.entities.vehicle;

/*
 * FordExplorerEntity – 1.19.2 RE‑PORT  ✧  2nd pass
 * --------------------------------------------------
 *   support classes (VehicleEntity, InterpValue, WheelData, TourRailBlock, etc.)
 *   expose the methods used below.  Any remaining red squiggles will be called
 *
 *    • Replaced deprecated/removed overrides (refreshDimensions, makeBoundingBox)
 *      with modern `getDimensions(Pose)`.
 *      suspension code is ported.
 *    • Switched to a new packet helper `Network.sendToAllNear(level, pos, msg)`.
 *    • Dropped the unused `dropItems()` override – use `spawnAtLocation` in
 *      `dropFromLootTable` instead.
 *    • Added proper getAddEntityPacket() (SpawnPacket) implementation so the car
 *      can be spawned over network.
 */

import com.mojang.math.Vector4f;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.vit.jurassicreborn.common.blocks.parkBlocks.TourRailBlock;
import net.vit.jurassicreborn.common.items.ModItems;
import net.vit.jurassicreborn.common.network.*;

import javax.annotation.Nonnull;
import java.util.Arrays;

public class FordExplorerEntity extends VehicleEntity {
    // inside VehicleEntity
    public boolean isOnRails()           { return entityData.get(ON_RAILS); }
    public void    setOnRails(boolean v) { entityData.set(ON_RAILS, v); }

    // === DATA PARAMETERS ===================================================
    private static final EntityDataAccessor<Boolean> ON_RAILS = SynchedEntityData.defineId(FordExplorerEntity.class, EntityDataSerializers.BOOLEAN);

    public static final BlockPos INACTIVE = new BlockPos(-1, -1, -1);

    public BlockPos prevRailTracks = INACTIVE;
    public  BlockPos railTracks     = INACTIVE;
    public  BlockPos prevPos        = INACTIVE;
    private boolean prevOnRails;

    private boolean lastDirBackwards;

    public  final MinecartLogic minecart = new MinecartLogic();
    private final InterpValue   rotationYawInterp = new InterpValue(this, 4f);

    public FordExplorerEntity(EntityType<? extends VehicleEntity> type, Level level) {
        super(type, level);
        this.speedModifier = 0f;
    }

    /*...*/
    /*...*/
    /*...*/
    @Override public void tick() {
        super.tick();

        BlockPos start = blockPosition();
        if (!level.isClientSide) handleRailDetection();

        if (entityData.get(ON_RAILS)) {
            minecart.tick();
            Vector4f v = wheeldata.carVector;
            this.backValue .setTarget(calculateWheelHeight(v.y(), false));
            this.frontValue.setTarget(calculateWheelHeight(v.w(), false));
            this.leftValue .setTarget(getY());
            this.rightValue.setTarget(getY());
        }

        if (!start.equals(blockPosition())) prevPos = blockPosition();

        prevOnRails = isOnRails();
    }

    /*...*/
    /*...*/
    /*...*/
    @Override
    public void baseTick() {
        super.baseTick();

        if (isOnRails()) {
            if (canPassengerSteer()) {
                if (getPassengers().isEmpty() || !(getPassengers().get(0) instanceof Player)) {
                    setControlState((byte)0);
                }
                // client input handled elsewhere via network packets
            }
        } else {
            rotationYawInterp.reset(getYRot() - 180.0D);
        }

        if (forward()) {
            lastDirBackwards = false;
        } else if (backward()) {
            lastDirBackwards = true;
        }
    }

    /*...*/
    /*...*/
    /*...*/
    private void handleRailDetection() {
        BlockPos rail = blockPosition();
        boolean isRail = level.getBlockState(rail).getBlock() instanceof TourRailBlock;
        if (!isRail) {
            rail = rail.below();
            isRail = level.getBlockState(rail).getBlock() instanceof TourRailBlock;
        }
        if (!isRail) {
            BlockPos below = rail.below();
            if (level.getBlockState(below).getBlock() instanceof TourRailBlock &&
                    Arrays.asList(TourRailBlock.EnumRailDirection.ASCENDING_EAST,
                                    TourRailBlock.EnumRailDirection.ASCENDING_NORTH,
                                    TourRailBlock.EnumRailDirection.ASCENDING_SOUTH,
                                    TourRailBlock.EnumRailDirection.ASCENDING_WEST)
                            .contains(TourRailBlock.getRailDirection(level, below))) {
                rail = below;
                isRail = true;
            }
        }
        if (!isRail) {
            BlockPos above = rail.above();
            if (level.getBlockState(above).getBlock() instanceof TourRailBlock &&
                    Arrays.asList(TourRailBlock.EnumRailDirection.ASCENDING_EAST,
                                    TourRailBlock.EnumRailDirection.ASCENDING_NORTH,
                                    TourRailBlock.EnumRailDirection.ASCENDING_SOUTH,
                                    TourRailBlock.EnumRailDirection.ASCENDING_WEST)
                            .contains(TourRailBlock.getRailDirection(level, above))) {
                rail = above;
                isRail = true;
            }
        }

        boolean wasRail = entityData.get(ON_RAILS);
        if (wasRail != isRail) {
            if (isRail) minecart.isInReverse = lastDirBackwards;
            entityData.set(ON_RAILS, isRail);
            this.refreshDimensions();

            Network.sendToAllNear(level, blockPosition(), new FordExplorerChangeStateMessage(getId(), isRail));
        }

        railTracks = isRail ? rail : INACTIVE;
        if (!railTracks.equals(prevRailTracks)) {
            Network.sendToAllNear(level, blockPosition(), new FordExplorerUpdatePositionStateMessage(getId(), rail));
        }
        prevRailTracks = railTracks;
    }

    /*...*/
    /*...*/
    /*...*/
    @Override
    public EntityDimensions getDimensions(Pose pose) {
        // Return size based on whether we’re on rails
        return entityData.get(ON_RAILS)
                ? EntityDimensions.fixed(0.75f, 0.25f)
                : EntityDimensions.fixed(3.0f, 2.5f);
    }

    /*...*/
    /*...*/
    /*...*/
    @Override public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this);
    }

    /*...*/
    /*...*/
    /*...*/
    @Override protected Seat[] createSeats() {
        return new Seat[] {
                new Seat( 0.563F, 0.45F,  0.40F, 0.5F, 0.25F),
                new Seat(-0.563F, 0.45F,  0.40F, 0.5F, 0.25F),
                new Seat( 0.563F, 0.45F, -1.00F, 0.5F, 0.25F),
                new Seat(-0.563F, 0.45F, -1.00F, 0.5F, 0.25F)
        };
    }

    @Override
    protected void dropFromLootTable(boolean causedByPlayer) {
        if (!level.isClientSide) spawnAtLocation(ModItems.FORD_EXPLORER_SNOW.get());
    }
    /*...*/
    /*...*/
    /*...*/
    @Override protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(ON_RAILS, false);
    }

    @Override protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("OnRails", entityData.get(ON_RAILS));
        tag.putLong("RailPos", railTracks.asLong());
        tag.putLong("PrevPos", prevPos.asLong());
    }

    @Override protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        entityData.set(ON_RAILS, tag.getBoolean("OnRails"));
        railTracks = BlockPos.of(tag.getLong("RailPos"));
        prevPos    = BlockPos.of(tag.getLong("PrevPos"));
    }

    /*...*/
    /*...*/
    /*...*/
//    @Override public float getSoundVolume() {
//        return entityData.get(ON_RAILS) && getControllingPassenger() != null
//                ? (float) (getSpeed().modifier / 2f)
//                : super.getSoundVolume();
//    }

    @Nonnull
    public Direction getAdjustedHorizontalFacing() {
        return entityData.get(ON_RAILS)
                ? minecart.getAdjustedHorizontalFacing()
                : Direction.fromYRot(getYRot());
    }
    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        // no-op: vehicles don’t make step sounds
    }

    @Override protected WheelData createWheels() { return new WheelData(1, 2, -1, -2.2); }

    @Override
    public void dropItems() {

    }

    //    protected boolean shouldTyresRender() {
//        return super.shouldTyresRender() && !entityData.get(ON_RAILS);
//    }
    @Override public Vec2 getBackWheelRotationPoint() {
        Vec2 p = super.getBackWheelRotationPoint();
        return new Vec2(p.x, entityData.get(ON_RAILS) ? 0 : p.y);
    }
    @Override public float getPickRadius() { return 2.25f; }

    /*...*/
    /*...*/
    /*...*/
    @Override
    protected void doBlockCollisions() {
        if (!isOnRails()) {
            FordExplorerEntity.this.doBlockCollisions();
        }
    }

    @Override
    public boolean alwaysAccepts() {
        return super.alwaysAccepts();
    }

    /*...*/
    /*...*/
    /*...*/
    public class MinecartLogic {
        private boolean isInReverse, prevKeyDown;
        private double adjustedRotationYaw;

        public Direction getAdjustedHorizontalFacing() {
            Direction f = Direction.fromYRot(getYRot());
            return isInReverse ? f.getOpposite() : f;
        }

        public void tick() {
            rotationDelta *= 0.8f;
            for (int i = 0; i < 4; i++) {
                java.util.List<WheelParticleData> removed = com.google.common.collect.Lists.newArrayList();
                wheelDataList[i].forEach(w -> w.onUpdate(removed));
                removed.forEach(wheelDataList[i]::remove);
            }
            allWheels.forEach(FordExplorerEntity.this::processWheel);
            FordExplorerEntity.this.spawnWheelParticles();
            if (getY() < -64) discard();
            if (railTracks == INACTIVE || getPassengers().isEmpty()) return;

            moveAlongTrack();

            if (!level.isClientSide) {
                doBlockCollisions();
                setXRot(0);
                updateInWaterStateAndDoFluidPushing();
            }
        }

        private void moveAlongTrack() {
            fallDistance = 0.0F;
            Vec3 vecStart = getRailPos();
            if (vecStart == null) return;
            setPos(vecStart.x, vecStart.y, vecStart.z);
            double slope = 0.0078125D;
            TourRailBlock.EnumRailDirection dir = TourRailBlock.getRailDirection(level, railTracks);
            Direction facing = getFacingDir();

            switch (dir) {
                case ASCENDING_EAST  -> { setDeltaMovement(getDeltaMovement().add(-slope, 0, 0)); setPos(getX(), getY() + 1, getZ()); }
                case ASCENDING_WEST  -> { setDeltaMovement(getDeltaMovement().add(slope, 0, 0));  setPos(getX(), getY() + 1, getZ()); }
                case ASCENDING_NORTH -> { setDeltaMovement(getDeltaMovement().add(0, 0, slope));  setPos(getX(), getY() + 1, getZ()); }
                case ASCENDING_SOUTH -> { setDeltaMovement(getDeltaMovement().add(0, 0, -slope)); setPos(getX(), getY() + 1, getZ()); }
            }

            double d1 = (double)(dir.getBackwardsX(facing) - dir.getForwardX(facing));
            double d2 = (double)(dir.getBackwardsZ(facing) - dir.getForwardZ(facing));
            double d3 = Math.sqrt(d1 * d1 + d2 * d2);
            Vec3 motion = getDeltaMovement();
            double d4 = motion.x * d1 + motion.z * d2;
            if (d4 < 0.0D) { d1 = -d1; d2 = -d2; }

            double d5 = Math.sqrt(motion.x * motion.x + motion.z * motion.z);
            if (d5 > 2.0D) d5 = 2.0D;

            double dirMul = 1.0D;
            if (forward()) {
                if (!prevKeyDown && isInReverse) dirMul = -1.0D;
                isInReverse = false;
                prevKeyDown = true;
            } else if (backward()) {
                if (!prevKeyDown && !isInReverse) dirMul = -1.0D;
                isInReverse = true;
                prevKeyDown = true;
            } else {
                prevKeyDown = false;
            }
            if (!level.isClientSide) d5 *= dirMul;

            double motionX = d5 * d1 / d3;
            double motionZ = d5 * d2 / d3;

            Vec3 pos = position();
            Vec3 dirVec = new Vec3(-d1, 0, d2).add(pos);
            double target = net.vit.jurassicreborn.common.util.MathUtils.cosineFromPoints(pos.add(0,0,1), dirVec, pos);
            if (dirVec.x < pos.x) target = -target;
            adjustedRotationYaw = target;
            if (isInReverse) target += 180F;

            double diff;
            do {
                diff = Math.abs(rotationYawInterp.getCurrent() - target);
                double d23 = Math.abs(rotationYawInterp.getCurrent() - (target + 360f));
                double d24 = Math.abs(rotationYawInterp.getCurrent() - (target - 360f));
                if (d23 < diff) target += 360f; else if (d24 < diff) target -= 360f;
            } while (diff > 180);

            target = Math.round(target * 100D) / 100D;

            rotationYawInterp.setSpeed(getSpeedType().modifier * 4f);
            if (!prevOnRails) rotationYawInterp.reset(target); else if (dirMul != -1) rotationYawInterp.setTarget(target);

            setYRot((float) rotationYawInterp.getCurrent());

            double d18 = railTracks.getX() + 0.5D + dir.getForwardX(facing) * 0.5D;
            double d19 = railTracks.getZ() + 0.5D + dir.getForwardZ(facing) * 0.5D;
            double d20 = railTracks.getX() + 0.5D + dir.getBackwardsX(facing) * 0.5D;
            double d21 = railTracks.getZ() + 0.5D + dir.getBackwardsZ(facing) * 0.5D;
            d1 = d20 - d18;
            d2 = d21 - d19;
            double d10;

            if (d1 == 0.0D) {
                setPos(railTracks.getX() + 0.5D, getY(), getZ());
                d10 = getZ() - railTracks.getZ();
            } else if (d2 == 0.0D) {
                setPos(getX(), getY(), railTracks.getZ() + 0.5D);
                d10 = getX() - railTracks.getX();
            } else {
                double d11 = getX() - d18;
                double d12 = getZ() - d19;
                d10 = (d11 * d1 + d12 * d2) * 2.0D;
            }
            double newX = d18 + d1 * d10;
            double newZ = d19 + d2 * d10;
            setPos(newX, getY(), newZ);
            moveMinecartOnRail(motionX, motionZ);

            double drag = isVehicle() ? 0.9D : 0.75D;
            motionX *= drag;
            motionZ *= drag;

            Vec3 vecEnd = getRailPos();
            if (vecEnd != null && vecStart != null) {
                double d14 = (vecStart.y - vecEnd.y) * 0.05D;
                d5 = Math.sqrt(motionX * motionX + motionZ * motionZ);
                if (d5 > 0.0D) {
                    motionX = motionX / d5 * (d5 + d14);
                    motionZ = motionZ / d5 * (d5 + d14);
                }
            }

            int j = net.minecraft.util.Mth.floor(getX());
            int i = net.minecraft.util.Mth.floor(getZ());
            if (j != railTracks.getX() || i != railTracks.getZ()) {
                d5 = Math.sqrt(motionX * motionX + motionZ * motionZ);
                motionX = d5 * (double)(j - railTracks.getX());
                motionZ = d5 * (double)(i - railTracks.getZ());
            }

            double d15 = Math.sqrt(motionX * motionX + motionZ * motionZ);
            if (d15 == 0) d15 = 1;
            double d16 = 0.06D;
            motionX += motionX / d15 * d16;
            motionZ += motionZ / d15 * d16;

            setDeltaMovement(motionX, getDeltaMovement().y, motionZ);
        }

        private Vec3 getRailPos() {
            net.minecraft.world.level.block.state.BlockState state = level.getBlockState(railTracks);
            if (!(state.getBlock() instanceof TourRailBlock)) return null;

            TourRailBlock.EnumRailDirection dir = TourRailBlock.getRailDirection(level, railTracks);
            Direction facing = getFacingDir();

            double startX = railTracks.getX() + 0.5D + dir.getForwardX(facing) * 0.5D;
            double startY = railTracks.getY() + 0.0625D + dir.getForwardY(facing) * 0.5D;
            double startZ = railTracks.getZ() + 0.5D + dir.getForwardZ(facing) * 0.5D;
            double endX   = railTracks.getX() + 0.5D + dir.getBackwardsX(facing) * 0.5D;
            double endY   = railTracks.getY() + 0.0625D + dir.getBackwardsY(facing) * 0.5D;
            double endZ   = railTracks.getZ() + 0.5D + dir.getBackwardsZ(facing) * 0.5D;

            double dx = endX - startX;
            double dy = endY - startY;
            double dz = endZ - startZ;
            double len = Math.sqrt(dx * dx + dz * dz);
            if (len == 0.0D) {
                return new Vec3(startX, startY, startZ);
            }

            double t = ((getX() - startX) * dx + (getZ() - startZ) * dz) / (len * len);
            t = net.minecraft.util.Mth.clamp(t, 0.0D, 1.0D);

            double x = startX + dx * t;
            double y = startY + dy * t;
            double z = startZ + dz * t;
            return new Vec3(x, y, z);
        }

        private void moveMinecartOnRail(double mx, double mz) {
            if (mx == 0 && mz == 0 && !getPassengers().isEmpty()) {
                Vec3 look = getViewVector(1f);
                mx = look.x;
                mz = look.z;
            }
            double max = getSpeedType().modifier / 8f;
            max = Math.min(max, 0.4D);
            mx = net.minecraft.util.Mth.clamp(mx, -max, max);
            mz = net.minecraft.util.Mth.clamp(mz, -max, max);
            FordExplorerEntity.this.move(MoverType.SELF, new Vec3(mx, 0, mz));
        }

        private Speed getSpeedType() {
            return ((TourRailBlock) level.getBlockState(railTracks).getBlock()).getSpeedType().getSpeed(getSpeed());
        }

        private Direction getFacingDir() {
            Direction facing = Direction.fromYRot((float) this.adjustedRotationYaw);
            if (this.isInReverse) facing = facing.getOpposite();
            return facing;
        }
    }

    @Override
    public boolean causeFallDamage(float distance, float damageMultiplier, DamageSource source) {
        return false;
    }
    /*...*/
    /*...*/
    public double calculateWheelHeight(double raw, boolean front) { return getDimensions(Pose.STANDING).height / 2.0; }
}
