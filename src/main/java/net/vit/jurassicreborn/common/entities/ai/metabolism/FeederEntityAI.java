package net.vit.jurassicreborn.common.entities.ai.metabolism;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import net.vit.jurassicreborn.common.blocks.entities.feeder.FeederBlockEntity;
import net.vit.jurassicreborn.common.entities.DinosaurEntity;
import net.vit.jurassicreborn.common.entities.FlyingDinosaurEntity;
import net.vit.jurassicreborn.common.util.GameRuleHandler;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class FeederEntityAI extends Goal {
    private static final int MAX_TRY_TICKS = 240;
    private static final int FAILED_FEEDER_COOLDOWN = 200;
    private static final int MAX_NO_PROGRESS_TICKS = 60;

    private final DinosaurEntity dino;

    @Nullable
    private BlockPos feederPos;
    @Nullable
    private Vec3 feederTarget;
    @Nullable
    private Vec3 landingTarget;

    private int repathCooldown;
    private int tryTicks;

    private int noProgressTicks;
    private double lastDistSq = Double.MAX_VALUE;

    @Nullable
    private BlockPos rejectedFeeder;
    private int rejectedFeederUntilTick;

    public FeederEntityAI(DinosaurEntity dino) {
        this.dino = dino;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP));
    }

    @Override
    public boolean canUse() {
        if (this.dino == null || this.dino.isRemoved() || this.dino.isCarcass() || this.dino.isMovementBlocked()) {
            return false;
        }

        boolean metabolismEnabled = this.dino.level().getGameRules().getBoolean(GameRuleHandler.DINO_METABOLISM);
        if (!metabolismEnabled) {
            return false;
        }

        if (!this.dino.getMetabolism().isHungry()) {
            return false;
        }

        this.clearExpiredRejectedFeeder();

        BlockPos found = this.dino.getClosestFeeder();
        if (found == null) {
            return false;
        }

        if (this.isRejectedFeeder(found)) {
            this.dino.invalidateClosestFeeder();
            return false;
        }

        FeederBlockEntity feeder = this.getFeeder(found);
        if (feeder == null || !feeder.tryClaim(this.dino)) {
            this.rejectFeeder(found);
            return false;
        }

        this.feederPos = found.immutable();
        this.feederTarget = feeder.getFeedingPos(this.dino);
        this.landingTarget = this.dino instanceof FlyingDinosaurEntity ? this.getLandingTarget() : null;

        if (!this.canApproach(feeder)) {
            feeder.releaseClaim(this.dino);
            this.rejectFeeder(found);
            this.feederPos = null;
            this.feederTarget = null;
            this.landingTarget = null;
            return false;
        }

        return true;
    }

    @Override
    public void start() {
        this.repathCooldown = 0;
        this.tryTicks = 0;
        this.noProgressTicks = 0;
        this.lastDistSq = Double.MAX_VALUE;
        this.moveToFeeder();
    }

    @Override
    public boolean canContinueToUse() {
        if (this.dino == null || this.feederPos == null) return false;
        if (this.dino.isRemoved() || this.dino.isCarcass() || this.dino.isMovementBlocked()) return false;
        if (!this.dino.getMetabolism().isHungry()) return false;
        if (this.tryTicks >= MAX_TRY_TICKS) return false;
        if (this.isRejectedFeeder(this.feederPos)) return false;

        FeederBlockEntity feeder = this.getFeeder(this.feederPos);
        if (feeder == null) return false;
        if (!this.refreshTargets(feeder)) return false;
        if (!this.canApproach(feeder)) return false;

        return feeder.isClaimedBy(this.dino) || feeder.canServe(this.dino);
    }

    private Vec3 getApproachReferencePos() {
        if (this.dino.usesAquaticFeederLogic()) {
            return this.dino.getEyePosition();
        }
        return this.dino.position();
    }

    @Override
    public void tick() {
        this.tryTicks++;
        this.clearExpiredRejectedFeeder();

        FeederBlockEntity feeder = this.getFeeder(this.feederPos);
        if (feeder == null) {
            this.rejectCurrentFeederAndStop();
            return;
        }

        if (!feeder.isClaimedBy(this.dino) && !feeder.tryClaim(this.dino)) {
            this.rejectCurrentFeederAndStop();
            return;
        }

        feeder.keepClaimAlive(this.dino);

        if (!this.refreshTargets(feeder)) {
            this.rejectCurrentFeederAndStop();
            return;
        }

        if (!this.canApproach(feeder)) {
            this.rejectCurrentFeederAndStop();
            return;
        }

        double reach = feeder.getFeedReach(this.dino);
        double distSq = this.getApproachReferencePos().distanceToSqr(this.feederTarget);

        if (distSq + 0.25D < this.lastDistSq) {
            this.noProgressTicks = 0;
        } else {
            this.noProgressTicks++;
        }
        this.lastDistSq = distSq;

        if (this.noProgressTicks > MAX_NO_PROGRESS_TICKS) {
            this.rejectCurrentFeederAndStop();
            return;
        }

        if (this.dino instanceof FlyingDinosaurEntity flyer) {
            flyer.shouldLand = true;

            Vec3 approach = this.landingTarget != null ? this.landingTarget : this.feederTarget;
            if (!flyer.isTouchingGround() || flyer.position().distanceToSqr(approach) > 1.25D) {
                flyer.getMoveControl().setWantedPosition(
                        approach.x,
                        approach.y,
                        approach.z,
                        1.1D
                );
            }
        }

        if (distSq <= reach * reach
                && (!(this.dino instanceof FlyingDinosaurEntity flyer) || flyer.isTouchingGround())) {
            if (!this.dino.usesAquaticFeederLogic()) {
                this.dino.getNavigation().stop();
            }

            this.dino.getLookControl().setLookAt(
                    this.feederTarget.x,
                    this.feederTarget.y,
                    this.feederTarget.z,
                    30.0F,
                    30.0F
            );

            if (this.dino instanceof FlyingDinosaurEntity flyer) {
                flyer.setDeltaMovement(
                        flyer.getDeltaMovement().x * 0.2D,
                        0.0D,
                        flyer.getDeltaMovement().z * 0.2D
                );
                flyer.hasImpulse = true;
            }

            if (!this.dino.getMetabolism().isHungry() || !feeder.isClaimedBy(this.dino)) {
                this.stop();
            }
            return;
        }

        if (!(this.dino instanceof FlyingDinosaurEntity)) {
            if (this.dino.getNavigation().isDone() && distSq > reach * reach) {
                this.rejectCurrentFeederAndStop();
                return;
            }

            if (this.repathCooldown-- <= 0 || this.dino.getNavigation().isDone()) {
                this.moveToFeeder();
                this.repathCooldown = this.dino.usesAquaticFeederLogic() ? 10 : 15;
            }
        }
    }

    @Override
    public void stop() {
        FeederBlockEntity feeder = this.getFeeder(this.feederPos);
        if (feeder != null) {
            feeder.releaseClaim(this.dino);
        }

        if (this.dino != null) {
            this.dino.getNavigation().stop();
            this.dino.invalidateClosestFeeder();

            if (this.dino instanceof FlyingDinosaurEntity flyer) {
                flyer.shouldLand = false;
            }
        }

        this.feederPos = null;
        this.feederTarget = null;
        this.landingTarget = null;
        this.repathCooldown = 0;
        this.tryTicks = 0;
        this.noProgressTicks = 0;
        this.lastDistSq = Double.MAX_VALUE;
    }

    private boolean refreshTargets(FeederBlockEntity feeder) {
        this.feederTarget = feeder.getFeedingPos(this.dino);
        if (this.feederTarget == null) {
            return false;
        }

        if (this.dino instanceof FlyingDinosaurEntity) {
            this.landingTarget = this.getLandingTarget();
            return this.landingTarget != null;
        }

        return true;
    }

    private boolean canApproach(FeederBlockEntity feeder) {
        if (this.feederTarget == null) {
            return false;
        }

        if (this.dino instanceof FlyingDinosaurEntity) {
            return this.landingTarget != null;
        }

        if (this.dino.usesAquaticFeederLogic()) {
            return true;
        }

        return this.dino.getNavigation().createPath(
                BlockPos.containing(this.feederTarget.x, this.feederTarget.y, this.feederTarget.z),
                0
        ) != null;
    }

    private void moveToFeeder() {
        if (this.feederTarget == null) {
            return;
        }

        if (this.dino instanceof FlyingDinosaurEntity flyer) {
            if (this.landingTarget == null) {
                this.landingTarget = this.getLandingTarget();
                if (this.landingTarget == null) {
                    return;
                }
            }

            flyer.shouldLand = true;
            flyer.getMoveControl().setWantedPosition(
                    this.landingTarget.x,
                    this.landingTarget.y,
                    this.landingTarget.z,
                    1.1D
            );
            return;
        }

        this.dino.getNavigation().moveTo(
                this.feederTarget.x,
                this.feederTarget.y,
                this.feederTarget.z,
                1.0D
        );
    }

    @Nullable
    private FeederBlockEntity getFeeder(@Nullable BlockPos pos) {
        if (pos == null || this.dino == null || this.dino.level() == null || !this.dino.level().hasChunkAt(pos)) {
            return null;
        }

        BlockEntity be = this.dino.level().getBlockEntity(pos);
        return be instanceof FeederBlockEntity feeder ? feeder : null;
    }

    @Nullable
    private Vec3 getLandingTarget() {
        if (this.feederTarget == null || this.dino == null) {
            return null;
        }

        Level level = this.dino.level();
        BlockPos.MutableBlockPos probe = BlockPos.containing(
                this.feederTarget.x,
                this.feederTarget.y,
                this.feederTarget.z
        ).mutable();

        while (probe.getY() > level.getMinBuildHeight() && level.getBlockState(probe).isAir()) {
            probe.move(0, -1, 0);
        }

        if (level.getBlockState(probe).isAir()) {
            return null;
        }

        return new Vec3(
                this.feederTarget.x,
                probe.getY() + 0.2D,
                this.feederTarget.z
        );
    }

    private void rejectCurrentFeederAndStop() {
        this.rejectFeeder(this.feederPos);
        this.stop();
    }

    private void rejectFeeder(@Nullable BlockPos pos) {
        if (pos != null) {
            this.rejectedFeeder = pos.immutable();
            this.rejectedFeederUntilTick = this.dino.tickCount + FAILED_FEEDER_COOLDOWN;
        }
        this.dino.invalidateClosestFeeder();
    }

    private boolean isRejectedFeeder(@Nullable BlockPos pos) {
        if (pos == null || this.rejectedFeeder == null) {
            return false;
        }

        if (this.dino.tickCount >= this.rejectedFeederUntilTick) {
            this.rejectedFeeder = null;
            this.rejectedFeederUntilTick = 0;
            return false;
        }

        return this.rejectedFeeder.equals(pos);
    }

    private void clearExpiredRejectedFeeder() {
        if (this.rejectedFeeder != null && this.dino.tickCount >= this.rejectedFeederUntilTick) {
            this.rejectedFeeder = null;
            this.rejectedFeederUntilTick = 0;
        }
    }
}