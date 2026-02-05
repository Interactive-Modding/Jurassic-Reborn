package net.vit.jurassicreborn.common.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.RandomSource;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;
import net.vit.jurassicreborn.common.entities.ai.navigation.DinosaurMoveHelper;
import net.vit.jurassicreborn.common.entities.ai.navigation.DinosaurPathNavigate;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.Random;

/**
 * Amphibian that swaps between land and water navigation.
 * Only tries to exit water when land is actually available.
 */
public abstract class AmphibianDinosaurEntity extends DinosaurEntity {
    private boolean getOut = false;     // desire to leave water (only when land found)
    private boolean getInWater = false; // desire to find water
    private boolean blocked;

    // Two navigators and swap them according to medium
    protected final PathNavigation navigationSwimmer;
    protected final PathNavigation navigationLand;

    // Swap-able controls
    private final SmoothSwimmingMoveControl waterMoveControl;
    private final SmoothSwimmingLookControl waterLookControl;
    private final DinosaurMoveHelper        landMoveControl;
    private final LookControl               landLookControl;

    private int waterTicks;
    private int landTicks;
    private int landCheckCooldown = 0; // Cooldown before checking for land again

    // Debounce medium switches
    private int mediumSwitchCooldown = 0;
    private static final int MEDIUM_SWITCH_CD = 10;

    public AmphibianDinosaurEntity(Level world, EntityType type, Dinosaur dino) {
        super(world, type, dino);

        // WATER: smooth swimming controls + navigator
        this.waterMoveControl = new SmoothSwimmingMoveControl(this, 85, 10, 0.02F, 0.1F, true);
        this.waterLookControl = new SmoothSwimmingLookControl(this, 10);
        this.navigationSwimmer = new WaterBoundPathNavigation(this, world);

        this.landMoveControl = new DinosaurMoveHelper(this);
        this.landLookControl = new LookControl(this);
        this.navigationLand  = new DinosaurPathNavigate(this, world);
        this.navigationLand.setCanFloat(true);

        // Start on land by default
        this.moveControl = this.landMoveControl;
        this.lookControl = this.landLookControl;
        this.navigation  = this.navigationLand;

        this.blocked = false;

        // Prefer both media
        this.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
        this.setPathfindingMalus(BlockPathTypes.WATER_BORDER, 0.0F);

        // Help clambering out of banks
        this.maxUpStep = 1.5F;

        // Goals
        this.goalSelector.addGoal(5,  new MoveUnderwaterGoal());
        this.goalSelector.addGoal(10, new FindWaterGoal());
        this.goalSelector.addGoal(10, new WanderGoal());
    }

    @Override
    public boolean isMovementBlocked() {
        return this.isCarcass() || this.isSleeping() || blocked;
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if (mediumSwitchCooldown > 0) mediumSwitchCooldown--;
        if (landCheckCooldown > 0) landCheckCooldown--;

        if (!this.level.isClientSide && this.isAlive()) {
            final boolean inWaterNow = this.isInWater();

            if (inWaterNow) {
                waterTicks++;
                landTicks = 0;

                this.setAirSupply(300);

                // Only set getOut if we can actually find land nearby
                if (waterTicks > 200 && landCheckCooldown == 0) {
                    Vec3 nearbyLand = findNearbyLandQuick(this, 16, 6);
                    if (nearbyLand != null) {
                        getOut = true;
                        landCheckCooldown = 100; // Don't spam land checks
                    } else {
                        getOut = false;
                        landCheckCooldown = 40; // Check again soon
                        waterTicks = 150; // Reset timer partially
                    }
                } else if (waterTicks <= 200) {
                    getOut = false;
                }

                getInWater = false;

                // Use water navigation when swimming normally
                if (!getOut && mediumSwitchCooldown == 0 && !(this.navigation instanceof WaterBoundPathNavigation)) {
                    this.navigation = navigationSwimmer;
                    mediumSwitchCooldown = MEDIUM_SWITCH_CD;
                }
                // Switch to land navigation only when actively trying to exit
                else if (getOut && mediumSwitchCooldown == 0 && !(this.navigation instanceof GroundPathNavigation)) {
                    this.navigation = navigationLand;
                    mediumSwitchCooldown = MEDIUM_SWITCH_CD;
                }
            } else {
                landTicks++;
                waterTicks = 0;
                landCheckCooldown = 0; // Reset when on land

                int air = this.getAirSupply() - 1;
                this.setAirSupply(air);
                if (air <= -20) {
                    this.setAirSupply(0);
                    this.hurt(DamageSource.DROWN, 2.0F);
                }

                getInWater = air < 40;
                getOut = false;

                if (mediumSwitchCooldown == 0 && !(this.navigation instanceof GroundPathNavigation)) {
                    this.navigation = navigationLand;
                    mediumSwitchCooldown = MEDIUM_SWITCH_CD;
                }
            }
        }

        // Swap controls based on medium
        if (this.isInWater()) {
            if (this.moveControl != this.waterMoveControl) {
                this.moveControl = this.waterMoveControl;
                this.lookControl = this.waterLookControl;
            }
        } else {
            if (this.moveControl != this.landMoveControl) {
                this.moveControl = this.landMoveControl;
                this.lookControl = this.landLookControl;
            }
        }
    }

    @Override
    public void travel(Vec3 vec) {
        float strafe   = (float) vec.x;
        float vertical = (float) vec.y;
        float forward  = (float) vec.z;
        boolean noInput = strafe == 0 && vertical == 0 && forward == 0;

        if (!this.level.isClientSide && this.isInWater() && !this.isCarcass()) {
            this.moveRelative(0.15F, new Vec3(strafe, vertical, forward));
            this.move(MoverType.SELF, this.getDeltaMovement());
            Vec3 movement = this.getDeltaMovement().multiply(0.8, 0.8, 0.8);

            // Gentle sink when idle to prevent floating at surface
            if (noInput && !getOut) {
                movement = movement.add(0.0D, -0.01D, 0.0D);
            }

            this.setDeltaMovement(movement);
        } else {
            super.travel(vec);
        }
    }

    // Quick land check used in aiStep
    @Nullable
    private Vec3 findNearbyLandQuick(Mob mob, int hr, int vr) {
        BlockPos origin = mob.blockPosition();

        for (int attempts = 0; attempts < 8; attempts++) {
            int dx = mob.getRandom().nextInt(hr * 2 + 1) - hr;
            int dy = mob.getRandom().nextInt(vr * 2 + 1) - vr;
            int dz = mob.getRandom().nextInt(hr * 2 + 1) - hr;

            BlockPos pos = origin.offset(dx, dy, dz);

            if (mob.level.getBlockState(pos).getMaterial().isSolid()) {
                BlockPos above = pos.above();
                if (mob.level.getBlockState(above).getMaterial().isLiquid() ||
                        hasAdjacentWater(mob, pos)) {
                    return Vec3.atBottomCenterOf(above);
                }
            }
        }
        return null;
    }

    private boolean hasAdjacentWater(Mob mob, BlockPos pos) {
        for (Direction dir : Direction.Plane.HORIZONTAL) {
            if (mob.level.getBlockState(pos.relative(dir)).getMaterial().isLiquid()) {
                return true;
            }
        }
        return false;
    }

    class MoveUnderwaterGoal extends Goal {
        private double x, y, z;
        private int retryCd = 0;
        private static final int TRY_EVERY = 20;
        private boolean failedToFindLand = false;

        public MoveUnderwaterGoal() {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            if (retryCd > 0) { retryCd--; return false; }

            if (!AmphibianDinosaurEntity.this.isInWater()) return false;
            if (AmphibianDinosaurEntity.this.getTarget() != null) return false;

            Vec3 target;

            if (getOut) {
                // Try to find shore to exit
                target = findNearbyLand(AmphibianDinosaurEntity.this, 16, 6);

                if (target == null) {
                    // No land found - cancel getOut and stay underwater
                    getOut = false;
                    waterTicks = 0; // Reset so we stay in water longer
                    failedToFindLand = true;
                    retryCd = 40;
                    return false;
                }
                failedToFindLand = false;
            } else {
                Random rng = AmphibianDinosaurEntity.this.getRandom();
                if (rng.nextFloat() < 0.50F && AmphibianDinosaurEntity.this.isBusy()) return false;

                // Find underwater position (not surface)
                target = getRandomDeepWaterPos(AmphibianDinosaurEntity.this, 8, 4);
                if (target == null) {
                    retryCd = TRY_EVERY;
                    return false;
                }
            }

            this.x = target.x; this.y = target.y; this.z = target.z;
            return true;
        }

        @Override
        public boolean canContinueToUse() {
            if (AmphibianDinosaurEntity.this.getTarget() != null) return false;

            // If we're trying to exit but still in water, keep trying
            if (getOut && AmphibianDinosaurEntity.this.isInWater()) {
                return !AmphibianDinosaurEntity.this.getNavigation().isDone();
            }

            // If we're just swimming and left water, stop
            if (!getOut && !AmphibianDinosaurEntity.this.isInWater()) {
                return false;
            }

            return !AmphibianDinosaurEntity.this.getNavigation().isDone();
        }

        @Override
        public void start() {
            AmphibianDinosaurEntity.this.getNavigation().moveTo(
                    this.x, this.y, this.z,
                    getOut ? 1.2D : 0.8D
            );
            retryCd = TRY_EVERY;
        }

        @Override
        public void stop() {
            // If we failed to find land while trying to exit, go back underwater
            if (failedToFindLand && AmphibianDinosaurEntity.this.isInWater()) {
                getOut = false;
                waterTicks = 0;
                failedToFindLand = false;
            }
        }

        @Override
        public void tick() {
            // Only add boost when trying to exit and colliding with shore
            if (getOut && AmphibianDinosaurEntity.this.horizontalCollision) {
                double distToTarget = AmphibianDinosaurEntity.this.distanceToSqr(this.x, this.y, this.z);

                if (distToTarget < 4.0D) {
                    Vec3 dm = AmphibianDinosaurEntity.this.getDeltaMovement();
                    AmphibianDinosaurEntity.this.setDeltaMovement(
                            dm.x,
                            Math.max(dm.y, 0.15D),
                            dm.z
                    );
                }
            }
        }

        @Override
        public boolean isInterruptable() { return true; }

        // Find deep water positions to avoid surface
        @Nullable
        private Vec3 getRandomDeepWaterPos(Mob mob, int hr, int vr) {
            BlockPos mobPos = mob.blockPosition();

            for (int i = 0; i < 15; i++) {
                int dx = mob.getRandom().nextInt(hr * 2 + 1) - hr;
                int dy = mob.getRandom().nextInt(vr * 2 + 1) - vr;
                int dz = mob.getRandom().nextInt(hr * 2 + 1) - hr;

                BlockPos pos = mobPos.offset(dx, dy, dz);

                // Check if this position is underwater
                if (!mob.level.getBlockState(pos).getMaterial().isLiquid()) continue;

                // Make sure there's water above (we're not at surface)
                boolean isDeep = false;
                for (int up = 1; up <= 2; up++) {
                    if (mob.level.getBlockState(pos.above(up)).getMaterial().isLiquid()) {
                        isDeep = true;
                        break;
                    }
                }

                if (isDeep || dy < 0) { // Prefer positions below current Y or with water above
                    return Vec3.atCenterOf(pos);
                }
            }

            // Fallback: any water position with downward bias
            for (int i = 0; i < 8; i++) {
                int dx = mob.getRandom().nextInt(hr * 2 + 1) - hr;
                int dy = -Math.abs(mob.getRandom().nextInt(vr + 1)); // Force downward bias
                int dz = mob.getRandom().nextInt(hr * 2 + 1) - hr;

                BlockPos pos = mobPos.offset(dx, dy, dz);
                if (mob.level.getBlockState(pos).getMaterial().isLiquid()) {
                    return Vec3.atCenterOf(pos);
                }
            }

            return null;
        }

        @Nullable
        private Vec3 findNearbyLand(Mob mob, int hr, int vr) {
            BlockPos origin = mob.blockPosition();

            for (int attempts = 0; attempts < 25; attempts++) {
                int dx = mob.getRandom().nextInt(hr * 2 + 1) - hr;
                int dy = mob.getRandom().nextInt(vr * 2 + 1) - vr;
                int dz = mob.getRandom().nextInt(hr * 2 + 1) - hr;

                BlockPos pos = origin.offset(dx, dy, dz);

                // Check if this is solid ground with water above or adjacent
                if (mob.level.getBlockState(pos).getMaterial().isSolid()) {
                    BlockPos above = pos.above();
                    // Must have passable space above and be adjacent to water
                    if (!mob.level.getBlockState(above).getMaterial().isSolid() &&
                            !mob.level.getBlockState(above.above()).getMaterial().isSolid()) {

                        if (mob.level.getBlockState(above).getMaterial().isLiquid() ||
                                hasAdjacentWater(mob, pos)) {
                            return Vec3.atBottomCenterOf(above);
                        }
                    }
                }
            }
            return null;
        }

        private boolean hasAdjacentWater(Mob mob, BlockPos pos) {
            for (Direction dir : Direction.Plane.HORIZONTAL) {
                if (mob.level.getBlockState(pos.relative(dir)).getMaterial().isLiquid()) {
                    return true;
                }
            }
            return false;
        }
    }

    class FindWaterGoal extends Goal {
        private int cd = 0;

        public FindWaterGoal() {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }
        @Override
        public boolean canUse() {
            if (cd > 0) { cd--; return false; }
            return false;
        }
    }

    class WanderGoal extends RandomStrollGoal {
        public WanderGoal() {
            super(AmphibianDinosaurEntity.this, 1.0D, 10);
        }
        @Override
        public boolean canUse() {
            if (getInWater) return false;
            return !AmphibianDinosaurEntity.this.isInWater() && super.canUse();
        }
    }
}