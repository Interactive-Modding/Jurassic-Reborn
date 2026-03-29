package net.vit.jurassicreborn.common.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;
import net.vit.jurassicreborn.common.entities.ai.navigation.MoveUnderwaterEntityAI;

/**
 * SwimmingDinosaurEntity
 *
 * Underwater locomotion is owned by MoveUnderwaterEntityAI.
 * The move/look controllers only handle stranded-on-land steering.
 *
 * This version fixes:
 * - slow underwater movement
 * - movement/controller conflicts
 * - prey not being attacked underwater
 * - collision/suffocation on load
 * - violent shake after being attacked
 *
 * Also adds stranded-on-land behavior:
 * - body wobbles/shakes while out of water
 * - entity lurches toward nearest water periodically
 * - flops randomly if no water is nearby
 */
public abstract class SwimmingDinosaurEntity extends DinosaurEntity {

    /** Shared oscillator used by AI and rendering. */
    public float bodyBobPhase = 0.0F;

    private final MoveUnderwaterEntityAI underwaterAI;
    private int aquaticUnstuckCooldown = 0;

    // Stranded-on-land state
    private int strandedLurchCooldown = 0;
    private int strandedWobblePhase   = 0;

    public SwimmingDinosaurEntity(Level level, EntityType<? extends DinosaurEntity> type, Dinosaur dino) {
        super(level, type, dino);

        // Replace the generic marine controls from DinosaurEntity with our own.
        this.navigation = new WaterBoundPathNavigation(this, level);
        this.moveControl = new AquaticMoveControl(this);
        this.lookControl = new AquaticLookControl(this);

        this.underwaterAI = new MoveUnderwaterEntityAI(this);
        this.goalSelector.addGoal(1, this.underwaterAI);
    }

    public MoveUnderwaterEntityAI getUnderwaterAI() {
        return this.underwaterAI;
    }

    public int getUnderwaterAttackIntervalTicks() {
        return 32; // slower bite tempo
    }

    public double getUnderwaterChaseRange() {
        return 10.0D; // stop hard-locking from too far away
    }

    public boolean canWaterBreachAttack() {
        return false;
    }

    public double getWaterBreachForwardBoost() {
        return 0.72D;
    }

    public double getWaterBreachVerticalBoost() {
        return 0.34D;
    }

    public double getWaterBreachProbeDistance() {
        return Math.max(1.10D, this.getBbWidth() * 1.4D + 0.8D);
    }

    /**
     * Client cannot reliably know the server-side goal scheduler state,
     * so while submerged we suppress move/look control locally too.
     */
    public boolean isUnderwaterAutopilotActive() {
        return this.shouldUseGenericUnderwaterAI()
                && this.canDinoSwim()
                && !this.isCarcass()
                && this.isInWater()
                && (this.level().isClientSide || (this.underwaterAI != null && this.underwaterAI.isActive()));
    }

    public boolean shouldUseGenericUnderwaterAI() {
        return true;
    }

    @Override
    protected void onPostLoadFixup() {
        if (this.level().isClientSide) {
            return;
        }

        if (this.isInWater()
                && (this.isInWall() || !this.level().noCollision(this, this.getBoundingBox().deflate(1.0E-4D)))) {
            if (this.tryResolveAquaticCollision()) {
                this.aquaticUnstuckCooldown = 10;
            }
        }
    }

    @Override
    public void tick() {
        this.bodyBobPhase += 0.055F;
        if (this.bodyBobPhase > (float) (Math.PI * 2.0)) {
            this.bodyBobPhase -= (float) (Math.PI * 2.0);
        }

        super.tick();

        if (!this.level().isClientSide && this.isAlive()) {

            // ----- stranded-on-land behavior -----
            if (!this.isInWater() && !this.isCarcass() && this.onGround()) {
                this.handleStrandedOnLand();
            } else {
                // Reset wobble state as soon as we re-enter water or leave the ground
                this.strandedWobblePhase   = 0;
                this.strandedLurchCooldown = 0;
            }

            // ----- existing aquatic unstuck logic -----
            if (this.aquaticUnstuckCooldown > 0) {
                this.aquaticUnstuckCooldown--;
            }

            if (this.canDinoSwim()
                    && this.isInWater()
                    && this.aquaticUnstuckCooldown <= 0
                    && (this.isInWall() || !this.level().noCollision(this, this.getBoundingBox().deflate(1.0E-4D)))) {
                if (this.tryResolveAquaticCollision()) {
                    this.aquaticUnstuckCooldown = 8;
                }
            }

            this.handleAquaticAirSupply();
        }
    }

    // -------------------------------------------------------------------------
    // Stranded behavior
    // -------------------------------------------------------------------------

    /**
     * Called every server tick while the entity is on dry land.
     * Produces a visible body-shake and periodic lurches toward the nearest water.
     */
    private void handleStrandedOnLand() {
        this.strandedWobblePhase++;

        // Oscillate yaw to produce a flopping/shaking look
        float shake = (float) (Math.sin(this.strandedWobblePhase * 0.75) * 9.0);
        this.setYRot(this.getYRot() + shake);
        this.yBodyRot = this.getYRot();
        this.yHeadRot = this.getYRot();

        // Periodic lurch toward water
        if (this.strandedLurchCooldown-- <= 0) {
            this.strandedLurchCooldown = 15 + this.random.nextInt(10);

            BlockPos water = this.findNearestWaterPos(14);
            if (water != null) {
                double dx = (water.getX() + 0.5) - this.getX();
                double dz = (water.getZ() + 0.5) - this.getZ();
                double dist = Math.sqrt(dx * dx + dz * dz);
                if (dist > 0.1) {
                    dx /= dist;
                    dz /= dist;
                }

                double speed = 0.22 + this.random.nextDouble() * 0.14;
                this.setDeltaMovement(dx * speed, 0.24, dz * speed);
            } else {
                // No water in range — flop in a random direction
                double angle = this.random.nextDouble() * Math.PI * 2.0;
                this.setDeltaMovement(
                        Math.cos(angle) * 0.18,
                        0.20,
                        Math.sin(angle) * 0.18
                );
            }
            this.hasImpulse = true;
        }
    }

    /**
     * Scans a cube of radius {@code radius} around the entity for any water block.
     * Returns the closest one, or {@code null} if none is found.
     */
    private BlockPos findNearestWaterPos(int radius) {
        BlockPos origin    = this.blockPosition();
        BlockPos best      = null;
        double bestDistSq  = Double.MAX_VALUE;

        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                for (int y = -3; y <= 3; y++) {
                    BlockPos candidate = origin.offset(x, y, z);
                    if (this.level().getFluidState(candidate).is(FluidTags.WATER)) {
                        double d = candidate.distSqr(origin);
                        if (d < bestDistSq) {
                            bestDistSq = d;
                            best = candidate;
                        }
                    }
                }
            }
        }
        return best;
    }

    // -------------------------------------------------------------------------
    // Existing methods (unchanged)
    // -------------------------------------------------------------------------

    /**
     * Marine creatures refill air in water and lose it while stranded.
     */
    protected void handleAquaticAirSupply() {
        if (this.isInWaterOrBubble()) {
            int maxAir = this.getMaxAirSupply();
            this.setAirSupply(maxAir > 0 ? maxAir : 300);
            return;
        }

        if (this.isCarcass()) {
            return;
        }

        int air = this.getAirSupply() - 1;
        this.setAirSupply(air);

        if (air <= -20) {
            this.setAirSupply(0);
            this.hurt(this.damageSources().drown(), 2.0F);
        }
    }

    @Override
    public void travel(Vec3 vec) {
        float strafe   = (float) vec.x;
        float vertical = (float) vec.y;
        float forward  = (float) vec.z;
        boolean noInput = strafe == 0 && vertical == 0 && forward == 0;

        if (!this.level().isClientSide && this.isInWater() && !this.isCarcass()) {
            this.moveRelative(0.1F, new Vec3(strafe, vertical, forward));
            this.move(MoverType.SELF, this.getDeltaMovement());
            Vec3 movement = this.getDeltaMovement().multiply(0.7, 0.7, 0.7);
            if (noInput && !this.isUnderwaterAutopilotActive()) {
                movement = movement.add(0.0D, -0.005D, 0.0D);
            }
            if (!this.isUnderWater()) {
                movement = movement.add(0.0D, -0.02D, 0.0D);
            }
            this.setDeltaMovement(movement);
        } else {
            super.travel(vec);
        }
    }

    protected void spawnInk() {
        this.playSound(SoundEvents.SQUID_SQUIRT, this.getSoundVolume(), this.getVoicePitch());
        Vec3 view = this.getViewVector(0.0F);

        for (int i = 0; i < 30; ++i) {
            this.level().addParticle(
                    ParticleTypes.SQUID_INK,
                    this.getX() + view.x * 0.1D + (this.random.nextDouble() - 0.5D) * 0.6D,
                    this.getY() + view.y * 0.1D + (this.random.nextDouble() - 0.5D) * 0.6D,
                    this.getZ() + view.z * 0.1D + (this.random.nextDouble() - 0.5D) * 0.6D,
                    this.random.nextGaussian() * 0.02D,
                    this.random.nextGaussian() * 0.02D,
                    this.random.nextGaussian() * 0.02D
            );
        }
    }

    private boolean tryResolveAquaticCollision() {
        AABB currentBox = this.getBoundingBox().deflate(1.0E-4D);
        if (this.level().noCollision(this, currentBox)) {
            return false;
        }

        double[][] offsets = new double[][]{
                {0.0D,  0.0D},
                {0.4D,  0.0D}, {-0.4D,  0.0D}, {0.0D,  0.4D}, {0.0D, -0.4D},
                {0.4D,  0.4D}, { 0.4D, -0.4D}, {-0.4D, 0.4D}, {-0.4D, -0.4D},
                {0.8D,  0.0D}, {-0.8D,  0.0D}, {0.0D,  0.8D}, {0.0D, -0.8D}
        };

        for (int up = 0; up <= 6; up++) {
            double yOff = up * 0.5D;

            for (double[] off : offsets) {
                double nx = this.getX() + off[0];
                double ny = this.getY() + yOff;
                double nz = this.getZ() + off[1];

                AABB testBox = this.getDimensions(this.getPose()).makeBoundingBox(nx, ny, nz).deflate(1.0E-4D);

                if (!this.level().noCollision(this, testBox)) {
                    continue;
                }

                if (!this.isWaterColumn(testBox)) {
                    continue;
                }

                this.setPos(nx, ny, nz);
                Vec3 motion = this.getDeltaMovement();
                this.setDeltaMovement(motion.x * 0.5D, Math.max(motion.y, 0.04D), motion.z * 0.5D);
                this.hasImpulse = true;
                return true;
            }
        }

        return false;
    }

    private boolean isWaterColumn(AABB box) {
        double cx = (box.minX + box.maxX) * 0.5D;
        double cz = (box.minZ + box.maxZ) * 0.5D;

        BlockPos feet = BlockPos.containing(cx, box.minY + 0.05D, cz);
        BlockPos body = BlockPos.containing(cx, (box.minY + box.maxY) * 0.5D, cz);
        BlockPos head = BlockPos.containing(cx, box.maxY - 0.05D, cz);

        return this.level().getFluidState(feet).is(FluidTags.WATER)
                && this.level().getFluidState(body).is(FluidTags.WATER)
                && this.level().getFluidState(head).is(FluidTags.WATER);
    }

    // -------------------------------------------------------------------------
    // Inner classes (unchanged)
    // -------------------------------------------------------------------------

    static final class AquaticMoveControl extends MoveControl {
        private final SwimmingDinosaurEntity swim;

        AquaticMoveControl(SwimmingDinosaurEntity entity) {
            super(entity);
            this.swim = entity;
        }

        @Override
        public void tick() {
            if (this.swim.isUnderwaterAutopilotActive()) {
                this.operation = Operation.WAIT;
                return;
            }

            if (this.operation != Operation.MOVE_TO) {
                this.swim.setSpeed(0.0F);
                return;
            }

            double dx = this.wantedX - this.swim.getX();
            double dy = this.wantedY - this.swim.getY();
            double dz = this.wantedZ - this.swim.getZ();
            double distSq = dx * dx + dy * dy + dz * dz;

            if (distSq < 1.0E-4D) {
                this.swim.setSpeed(0.0F);
                this.operation = Operation.WAIT;
                return;
            }

            double dist = Math.sqrt(distSq);
            float targetYaw = (float) Math.toDegrees(Math.atan2(-dx / dist, dz / dist));

            this.swim.setYRot(MoveUnderwaterEntityAI.smoothAngle(this.swim.getYRot(), targetYaw, 10.0F));
            this.swim.yBodyRot = this.swim.getYRot();
            this.swim.yHeadRot = this.swim.getYRot();

            float landSpeed = (float) (this.swim.getAttributeValue(Attributes.MOVEMENT_SPEED) * 0.35F);
            this.swim.setSpeed(landSpeed);
        }
    }

    static final class AquaticLookControl extends LookControl {
        private final SwimmingDinosaurEntity swim;

        AquaticLookControl(SwimmingDinosaurEntity entity) {
            super(entity);
            this.swim = entity;
        }

        @Override
        public void tick() {
            if (this.swim.isUnderwaterAutopilotActive()) {
                return;
            }
            super.tick();
        }
    }
}