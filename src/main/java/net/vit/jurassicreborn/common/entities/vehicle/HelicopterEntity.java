package net.vit.jurassicreborn.common.entities.vehicle;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Entity.MoveFunction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.client.input.VehicleKeyHandler;
import net.vit.jurassicreborn.client.render.RenderingHandler;
import net.vit.jurassicreborn.common.entities.ModEntities;
import net.vit.jurassicreborn.common.util.math.MutableVec3;
import net.vit.jurassicreborn.common.util.math.Vec3d;
import net.vit.jurassicreborn.common.util.particles.ModParticles;

import javax.annotation.Nonnull;
import java.util.List;

public abstract class HelicopterEntity extends VehicleEntity {

    private static final byte UPWARD = 0b010000;
    private static final byte DOWNWARD = 0b100000;

    private int rotationControl = 0;
    public float gearLift;
    public boolean shouldGearLift = true;
    private final InterpValue rotationYawInterp = new InterpValue(this, 4f);
    public boolean isFlying;
    public final InterpValue interpRotationPitch = new InterpValue(this, 0.25D);
    public final InterpValue interpRotationRoll = new InterpValue(this, 0.25D);
    protected MutableVec3 direction;

    private static final int MAX_MOVEMENT_ROTATION = 15;

    private boolean shouldFallDamage;
    public double rotAmount = 0D;
    private Vec3d prevInAirPos;
    private float damageAmount;
    private final BlockPos.MutableBlockPos mb = new BlockPos.MutableBlockPos();
    protected boolean lockOn;
    protected int blastHeight = 6;

    private static final float TAKEOFF_THRESHOLD_RATIO = 0.15f;
    private float currentEngineSpeed = 0;
    protected float torque;

    // Renamed to fix the typo
    protected float yawRotationAcceleration = 0;

    private float shakingDirection = 0;
    protected ResourceLocation warningSoundResource;
    private int warningDelay = 0;

    // Technical specifications
    protected final int enginePower;  // W (converted from PS)
    protected final int engineSpeed;  // RPM
    protected final int rotorLength;  // blocks
    protected final int weight;       // kg
    private final float physicalWidth;   // blocks
    protected final float physicalHeight; // blocks
    private final float physicalDepth;    // blocks
    protected final float qualityGrade = 0.75f;
    protected boolean simpleControle;

    // Death guard
    private boolean didDieOnce = false;

    public HelicopterEntity(Level worldIn, float widthIn, float heightIn, float depthIn,
                            int enginePowerIn, int engineSpeedIn, int weightIn, int rotorLengthIn) {
        super(ModEntities.HELICOPTER.get(), worldIn);

        this.physicalWidth = widthIn;
        this.physicalHeight = heightIn;
        this.physicalDepth = depthIn;
        this.setBoundingBox(new AABB(0, 0, 0, this.physicalWidth, this.physicalHeight, this.physicalDepth));

        this.enginePower = (int) ((float) enginePowerIn * 735.5f);
        this.engineSpeed = engineSpeedIn;
        this.weight = weightIn;
        this.rotorLength = rotorLengthIn;

        this.torque = this.computeTorque();
        this.speedModifier = 1.5f;
        this.isFlying = false;
        this.direction = new MutableVec3(0, 1, 0);
        this.simpleControle = true;
        this.lockOn = true;
        this.warningSoundResource = new ResourceLocation(JurassicReborn.MODID, "helicopter_warning");
    }

    public boolean upward() { return this.getStateBit(UPWARD); }
    public boolean downward() { return this.getStateBit(DOWNWARD); }
    public void upward(boolean upward) { this.setStateBit(UPWARD, upward); }
    public void downward(boolean downward) { this.setStateBit(DOWNWARD, downward); }

    @Override
    public void startSound() { super.startSound(); }

    protected boolean shouldStopUpdates() { return false; }

    @Override
    protected void doBlockCollisions() {}

    public boolean isController(Player e) {
        if (e == null || e.getVehicle() != this) return false;
        String seat = this.getIfExists(0, false);
        return !seat.isEmpty() && seat.equals(Integer.toString(e.getId()));
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        if (hand != InteractionHand.MAIN_HAND) return InteractionResult.PASS;

        if (!level().isClientSide) {
            if (player.getVehicle() == this) return InteractionResult.CONSUME;

            int seat = getSeatForEntity(player);
            if (seat == -1) {
                for (int i = 0; i < seats.length; i++) {
                    if (getIfExists(i, false).equals("")) { seat = i; break; }
                }
            }
            if (seat != -1 && tryPutInSeat(player, seat, false)) {
                player.startRiding(this, true);
            }
        }
        return InteractionResult.sidedSuccess(level().isClientSide);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    protected void handleControl() {
        if (this.isController(Minecraft.getInstance().player)) {
            if (this.isInWater()) {
                this.upward(false);
                this.downward(false);
            } else {
                this.upward(VehicleKeyHandler.HELICOPTER_UP.isDown());
                this.downward(VehicleKeyHandler.HELICOPTER_DOWN.isDown());
                this.handleKeyEnableAutoPilot(VehicleKeyHandler.HELICOPTER_AUTOPILOT.consumeClick());
                this.handleKeyLock(VehicleKeyHandler.HELICOPTER_LOCK.consumeClick());
                this.rotateLeft(VehicleKeyHandler.HELICOPTER_ROTATE_LEFT.isDown());
                this.rotateRight(VehicleKeyHandler.HELICOPTER_ROTATE_RIGHT.isDown());
            }
            super.handleControl();
        }
        if (this.hasPassenger(Minecraft.getInstance().player)) {
            this.increaseThirdPersonViewDistance(VehicleKeyHandler.HELICOPTER_THIRD_PERSON_VIEW_ZOOM_OUT.isDown());
            this.decreaseThirdPersonViewDistance(VehicleKeyHandler.HELICOPTER_THIRD_PERSON_VIEW_ZOOM_IN.isDown());
        }
    }

    public void fall(float distance, float damageMultiplier) {
        if (!level().isClientSide && !isFlying) {
            float damage = Mth.ceil((distance - 3F) * damageMultiplier);
            if (damage > 0) {
                this.setHealth(this.getHealth() - (float)(damage * 1.25F));
                this.checkAndHandleDeath();
            }
        }
        if (this.level().isClientSide && !isFlying) {
            float damage = Mth.ceil((distance - 3F) * damageMultiplier);
            if (damage > 0) {
                float tmp = this.getHealth() - (float) (damage * 1.25F);
                if (tmp <= 0) this.playHelicopterExplosion();
            }
        }
    }

    @Override
    protected void removePassenger(Entity passenger) {
        super.removePassenger(passenger);
        if (this.level().isClientSide) resetThirdPersonViewDistance();
    }

    @Override
    public void tick() {
        if (this.level().isClientSide) this.isFlying = this.isNoGravity();
        super.tick();

        if (!this.isInWater()) {
            final float dist = this.getDistanceToGround();

            if (this.level().isClientSide) {
                for (int i = 0; i < this.seats.length; i++) {
                    final Entity e = this.getEntityInSeat(i);
                    if (e != null) e.fallDistance = 0;
                }
            }

            // Manual pitch
            if (forward() && this.isFlying) this.pitch -= this.computeThrottleUpDown() / 2;
            else if (this.backward() && this.isFlying) this.pitch += this.computeThrottleUpDown() / 2;

            // Manual roll
            if (this.left() && !this.right() && this.isFlying) this.roll -= this.computeThrottleUpDown();
            else if (this.right() && !this.left() && this.isFlying) this.roll += this.computeThrottleUpDown();

            // Manual yaw
            if (this.rotateLeft() && !this.left() && !this.right() && this.isFlying && dist > 0.1f && this.getCurrentEngineSpeed() > 10) {
                this.yawRotationAcceleration += 0.08f;
            } else if (this.rotateRight() && !this.left() && !this.right() && this.isFlying && dist > 0.1f && this.getCurrentEngineSpeed() > 10) {
                this.yawRotationAcceleration -= 0.08f;
            }

            // Clamp pitch wraparound
            if (this.pitch > 180) this.pitch = -180 + (this.pitch - 180);
            else if (this.pitch < -180) this.pitch = this.pitch + 360;

            // Apply max rotation limits
            float maxRot = this.computeMaxMovementRotation(dist);
            this.pitch = Mth.clamp(this.pitch, -maxRot, maxRot);
            this.roll  = Mth.clamp(this.roll,  -maxRot, maxRot);

            // Clamp yaw acceleration
            if (!this.isLowHealth()) {
                float maxYawAccel = this.computeThrottleUpDown() * 2;
                this.yawRotationAcceleration = Mth.clamp(this.yawRotationAcceleration, -maxYawAccel, maxYawAccel);
            }
            if (dist <= 0.1f) this.yawRotationAcceleration = 0;

            // Autopilot stabilization & hover
            final float requiredSpeedForHovering = this.computeRequiredEngineSpeedForHover();
            this.updateAutopilot(requiredSpeedForHovering, dist);

            // Crash dynamics
            this.updateHelicopterCrash(dist);
            this.setYRot(this.getYRot() + this.yawRotationAcceleration);

            // Interp targets
            rotationYawInterp.reset(this.getYRot() - 180D);
            this.interpRotationPitch.setTarget(this.direction.zCoord * -30D);
            this.interpRotationRoll.setTarget(this.direction.xCoord * 20D);

            // Engine speed control
            if ((this.getControllingPassenger() != null) && !this.isLowHealth()) {
                if (this.upward()) {
                    this.changeCurrentEngineSpeed(this.computeThrottleUpDown());
                    if (!this.isFlying && this.getCurrentEngineSpeed() >= this.engineSpeed * TAKEOFF_THRESHOLD_RATIO) {
                        this.setFlying();
                    }
                } else if (this.downward() && this.isFlying) {
                    this.shouldFallDamage = false;
                    this.changeCurrentEngineSpeed(-this.computeThrottleUpDown());
                } else {
                    if (!this.isFlying) {
                        this.setNoGravity(false);
                        for (int i = 0; i < this.seats.length; i++) {
                            Entity e = this.getEntityInSeat(i);
                            if (e != null) e.setNoGravity(false);
                        }
                        if (this.simpleControle && this.getCurrentEngineSpeed() > 0) {
                            this.changeCurrentEngineSpeed(-1);
                        }
                    }
                    // Hover engine adjustment handled in updateAutopilot()
                }
                this.updateHelicopterTakeoffShaking(dist);
            } else if (this.getCurrentEngineSpeed() > 0) {
                this.changeCurrentEngineSpeed(-1);
            }

            // Landing logic
            if (this.onGround()) {
                boolean wasFlying = this.isFlying;
                this.isFlying = false;
                if (wasFlying && this.shouldCrashOnLanding()) {
                    if (!this.level().isClientSide) {
                        this.setHealth(0);
                        this.checkAndHandleDeath(); // unified drop+discard
                    } else {
                        this.playHelicopterExplosion();
                    }
                }
                this.lockOn = true;
                this.pitch = 0;
                this.roll = 0;
                this.yawRotationAcceleration = 0;
            }

            // Landing gear animation
            if (this.level().isClientSide) {
                if (!this.shouldGearLift) this.gearLift += 0.02f; else this.gearLift -= 0.02f;
                this.shouldGearLift = !(dist < 10);
                if (this.gearLift < -0.5f) this.gearLift = -0.5f;
                if (this.gearLift > 0f)    this.gearLift = 0f;
            }

            if (this.getControllingPassenger() == null) this.setNoGravity(false);

            // Fall damage when landing hard (server-side)
            if (this.onGround() && this.shouldFallDamage) {
                this.damageAmount = (float) this.prevInAirPos.y - (float) this.getPositionVector().y;
                this.setHealth(this.getHealth() - (float) Math.floor((double) (this.damageAmount / 3)));
                this.shouldFallDamage = false;
                this.checkAndHandleDeath();
            }

            // Rotor animation
            this.rotAmount += this.getCurrentEngineSpeed() * (Math.PI / 250d);

            // Rotor collision damage
            if (this.getCurrentEngineSpeed() >= 1 && !this.isRotorAreaFree()) {
                this.setHealth(this.getHealth() - (this.getCurrentEngineSpeed() / this.engineSpeed * 2f));
                if (this.getHealth() <= 0f) {
                    if (!this.level().isClientSide) {
                        this.checkAndHandleDeath();
                    } else {
                        this.playHelicopterExplosion();
                    }
                }
            }
        } else {
            // In water
            this.setNoGravity(false);
            this.wheelRotateAmount = 0;
            this.setCurrentEngineSpeed(this.getCurrentEngineSpeed() / 8);
        }

        // Client-side effects
        if (this.level().isClientSide) {
            this.spawnHoveringParticle();
            this.spawnEngineRunningParticle();
            this.spawnCrashingParticle();
            this.playWarningSound();
        }

        this.blastItems();
        this.checkAndHandleDeath(); // final safety net
    }

    @Override
    protected void applyMovement() {
        float moveAmount;

        float surfaceFront = this.physicalWidth * this.physicalHeight;
        float surfaceTop   = this.physicalWidth * this.physicalDepth;

        Vec3 delta = this.getDeltaMovement();
        final double vx = delta.x;
        final double vy = delta.y;
        final double vz = delta.z;

        final float horizontalSpeed = (float) Math.abs(Math.sqrt(vx*vx + vz*vz) * 20);
        final float verticalSpeed   = (float) Math.abs(vy * 20);

        final float flowResistanceFront = (float) (2 * surfaceFront * 0.5f * 1.2f * Math.pow(horizontalSpeed, 2));
        final float flowResistanceTop   = (float) (2 * surfaceTop   * 0.5f * 1.2f * Math.pow(verticalSpeed,   2));

        // Compute thrust forces
        float horizontalThrust = this.computeHorizontalForceFrontBack() / this.weight / 20;
        float verticalThrust   = (float) ((this.computeVerticalForce() / this.weight - 9.81) / 20);

        moveAmount  = horizontalThrust;
        moveAmount *= Math.abs(((this.roll <= 45) ? this.roll / 45 : 2 - this.roll / 45)
                * 2
                * ((this.pitch <= 90) ? this.pitch / 90 : 2 - this.pitch / 90));

        double newY = vy + verticalThrust - (flowResistanceTop / this.weight / 20);

        if ((this.roll > 0 && this.roll < 90 && this.pitch != 0)) {
            this.rotationDelta -= 20.0F * moveAmount;
        } else if ((this.roll < 0 && this.roll > -90 && this.pitch != 0)) {
            this.rotationDelta += 20.0F * moveAmount;
        }

        this.rotationDelta = Mth.clamp(this.rotationDelta, -3F, 3F);

        final float yawRad = this.getYRot() * 0.017453292F;

        double newZ = vz
                - Math.cos(yawRad) * horizontalThrust
                + Math.cos((this.getYRot() - 90) * 0.017453292F) * (this.computeHorizontalForceLeftRight()) / this.weight / 20;

        double newX = vx
                - Math.sin(-yawRad) * horizontalThrust
                + Math.sin(-(this.getYRot() - 90) * 0.017453292F) * (this.computeHorizontalForceLeftRight()) / this.weight / 20;

        // Velocity-proportional drag
        final double drag = 0.04;
        newX -= vx * drag;
        newZ -= vz * drag;
        newY -= vy * drag;

        double max = 0.8D;
        newX = Mth.clamp(newX, -max, max);
        newY = Mth.clamp(newY, -max, max);
        newZ = Mth.clamp(newZ, -max, max);
        this.setDeltaMovement(newX, newY, newZ);

        // Improved yaw damping
        this.rotationDelta *= 0.85f;
        this.setYRot(this.getYRot() + this.rotationDelta);
    }

    // Smooth, progressive crash behavior when low health & airborne
    private void updateHelicopterCrash(float dist) {
        if (this.isLowHealth() && this.isFlying && dist > 1.5f) {
            float healthRatio = this.getHealth() / this.MAX_HEALTH;
            float instability = (1.0f - healthRatio) * 0.5f; // Max 0.5 when health is 0

            // Random yaw drift
            this.yawRotationAcceleration += (float)((Math.random() - 0.5) * instability * 1.5f);
            this.yawRotationAcceleration = Mth.clamp(this.yawRotationAcceleration, -7f, 7f);

            // Forward drift
            Vec3 deltaCrash = this.getDeltaMovement();
            float driftAmount = instability * 0.03f;
            deltaCrash = deltaCrash.add(
                    Math.sin(Math.toRadians(this.getYRot())) * driftAmount,
                    0,
                    Math.cos(Math.toRadians(this.getYRot())) * driftAmount
            );
            this.setDeltaMovement(deltaCrash);

            // Engine power loss
            this.changeCurrentEngineSpeed(-0.5f * instability);

            // Gradual pitch forward
            float targetPitch = 15 + (instability * 25);
            if (this.pitch < targetPitch) this.pitch += 0.15f * instability;

            // Gradual roll oscillation
            float targetRoll = (float) (Math.sin(this.tickCount * 0.1) * 30 * instability);
            if (Math.abs(this.roll - targetRoll) > 1) {
                this.roll += (targetRoll - this.roll) * 0.05f;
            }
        }
    }

    // Unified, idempotent death handlers
    private void handleDeath() {
        if (this.level().isClientSide || this.didDieOnce || this.isRemoved()) return;
        this.didDieOnce = true;
        if (this.level().getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
            this.dropItems();
        }
        this.discard();
    }

    private void checkAndHandleDeath() {
        if (!this.level().isClientSide && this.getHealth() <= 0f) {
            handleDeath();
        }
    }

    // Hover autopilot with vertical-velocity damping
    private void updateAutopilot(float requiredSpeedForHovering, float dist) {
        if (!this.simpleControle || !this.isFlying || this.getControllingPassenger() == null || this.isLowHealth()) {
            return;
        }

        final boolean hasHorizontalInput = this.forward() || this.backward() || this.left() || this.right();
        final boolean hasVerticalInput   = this.upward()  || this.downward();

        // Stabilize pitch & roll when no horizontal input
        if (!hasHorizontalInput && this.lockOn) {
            this.pitch = Math.abs(this.pitch) < 0.6f ? 0f : this.pitch * 0.94f;
            this.roll  = Math.abs(this.roll)  < 0.6f ? 0f : this.roll  * 0.94f;
        }

        // Dampen yaw spin when airborne
        if (dist > 0.1f && this.getCurrentEngineSpeed() > 10) {
            this.yawRotationAcceleration = Math.abs(this.yawRotationAcceleration) < 0.03f
                    ? 0f
                    : this.yawRotationAcceleration * 0.90f;
        }

        // If no vertical input, hold hover using a simple PV controller
        if (!hasVerticalInput) {
            final float current = this.getCurrentEngineSpeed();
            final float vy      = (float) this.getDeltaMovement().y;

            final float kP = 0.25f; // pull toward required hover speed
            final float kV = 40f;   // counter vertical drift

            float target = requiredSpeedForHovering - kV * vy;
            target = Mth.clamp(target, 0f, (float) this.engineSpeed);

            float delta = (target - current) * kP;
            if (Math.abs(delta) < 0.05f) delta = 0f;

            this.changeCurrentEngineSpeed(delta);
        }
    }

    protected boolean isLowHealth() { return this.getHealth() <= 2; }

    private void updateHelicopterTakeoffShaking(float dist) {}

    protected void updateShakingRotation() { this.roll += this.shakingDirection; }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) { super.addAdditionalSaveData(compound); }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) { super.readAdditionalSaveData(compound); }

    @Override
    public float getSoundVolume() {
        return this.getCurrentEngineSpeed() > 0
                ? (Math.abs(this.getCurrentEngineSpeed() / 150) + 0.001F)
                / (this.engineSound == null || this.engineSound.isStopped() ? 2f : 4f)
                : (Math.abs(this.wheelRotateAmount) + 0.001F)
                / (this.engineSound == null || this.engineSound.isStopped() ? 2f : 4f);
    }

    @Nonnull
    public Direction getAdjustedHorizontalFacing() { return Direction.fromYRot(this.getYRot()); }

    @Override
    protected void positionRider(Entity passenger, MoveFunction moveFunction) {
        if (this.hasPassenger(passenger)) {
            Seat seat = null;
            if (getSeatForEntity(passenger) != -1) seat = this.seats[getSeatForEntity(passenger)];

            Vec3d pos;
            if (seat == null) {
                pos = new Vec3d(this.getX(), this.getY() + this.physicalHeight, this.getZ());
            } else {
                Vec3 seatPos = seat.getPos(this);
                pos = new Vec3d(seatPos.x, seatPos.y, seatPos.z);
            }
            moveFunction.accept(passenger, pos.x, pos.y + this.interpRotationPitch.getCurrent() / 75D, pos.z);
            passenger.setYRot(passenger.getYRot() + this.rotationDelta);
            passenger.setYHeadRot(passenger.getYHeadRot() + this.rotationDelta);
            if (passenger instanceof LivingEntity living) {
                living.yBodyRot += (living.getYRot() - living.yBodyRot) * 0.6F;
            }
        }
    }

    private void playHelicopterExplosion() {
        this.level().addParticle(ParticleTypes.EXPLOSION, this.getX(), this.getY(), this.getZ(), 0.1, 0.1, 0.1);
        this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
                SoundEvents.GENERIC_EXPLODE, SoundSource.NEUTRAL, 4.0F,
                (1.0F + (this.level().random.nextFloat() - this.level().random.nextFloat()) * 0.2F) * 0.7F);
    }

    private float computeMaxMovementRotation(float dist) {
        return (dist <= 3)
                ? ((dist / 3.0f) * (this.lockOn ? MAX_MOVEMENT_ROTATION : 90))
                : (this.lockOn ? MAX_MOVEMENT_ROTATION : 180f);
    }

    public float getDistanceToGround() {
        Level level = this.level();
        boolean found = false;
        float dist = -1;
        mb.set(this.blockPosition());
        while (!found && this.getY() >= 0 && mb.getY() >= 0) {
            if (level.isEmptyBlock(mb)) {
                mb.move(0, -1, 0);
            } else {
                found = true;
                dist = (float) (this.getY() - mb.getY() - 1);
            }
        }
        return dist;
    }

    protected BlockState getGroundBlock() {
        Level level = this.level();
        boolean found = false;
        BlockState groundBlock = null;
        mb.set(this.blockPosition());
        while (!found && this.getY() >= 0 && mb.getY() >= 0) {
            if (level.isEmptyBlock(mb)) {
                mb.move(0, -1, 0);
            } else {
                found = true;
                groundBlock = this.level().getBlockState(mb);
            }
        }
        return groundBlock;
    }

    protected void updateHeal() {}

    @OnlyIn(Dist.CLIENT)
    private void increaseThirdPersonViewDistance(boolean shouldIncrease) {
        if (shouldIncrease) {
            RenderingHandler.INSTANCE.setThirdPersonViewDistance(RenderingHandler.INSTANCE.getThirdPersonViewDistance() + 1);
        }
        ;
    }

    @OnlyIn(Dist.CLIENT)
    private void decreaseThirdPersonViewDistance(boolean shouldDecrease) {
        if (shouldDecrease) {
            RenderingHandler.INSTANCE.setThirdPersonViewDistance(RenderingHandler.INSTANCE.getThirdPersonViewDistance() - 1);
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void resetThirdPersonViewDistance() { RenderingHandler.INSTANCE.resetThirdPersonViewDistance(); }

    public void setDead() {
        if (this.level().isClientSide) this.playHelicopterExplosion();
        super.discard();
    }

    public float getCollisionBorderSize() { return 2.25f; }

    private boolean isRotorAreaFree() {
        Level level = this.level();
        boolean isFree = true;
        for (int x = -this.rotorLength; x < this.rotorLength && isFree; x++) {
            for (int z = -this.rotorLength; z < this.rotorLength && isFree; z++) {
                if (!level.getBlockState(BlockPos.containing(this.getX() + x, this.getY() + this.physicalHeight, this.getZ() + z)).isAir()) {
                    isFree = false;
                }
            }
        }
        return isFree;
    }

    protected Vec3d getPositionVector() { return new Vec3d(this.getX(), this.getY(), this.getZ()); }

    protected void setFlying() {
        this.isFlying = true;
        this.shouldFallDamage = true;
        this.prevInAirPos = this.getPositionVector();
        this.setNoGravity(true);
        for (int i = 0; i < this.seats.length; i++) {
            Entity e = this.getEntityInSeat(i);
            if (e != null) e.setNoGravity(false);
        }
    }

    public int getPositionLightFrequency() {
        return 30 - (int) ((this.getCurrentEngineSpeed() / this.engineSpeed) * 20);
    }

    protected void blastItems() {
        float dist = this.getDistanceToGround();
        if (dist >= 0) {
            List<Entity> items = this.level().getEntitiesOfClass(Entity.class,
                    new AABB(this.getX() - this.rotorLength * 2, this.getY() - dist - 1, this.getZ() - this.rotorLength * 2,
                            this.getX() + this.rotorLength * 2, this.getY() + 1, this.getZ() + this.rotorLength * 2));
            for (Entity item : items) {
                float itemDist = (float) item.distanceTo(this);
                if (itemDist <= this.rotorLength * 2 && item.getBbWidth() <= 0.55) {
                    float moveAmount = (float) ((1 - (item.distanceTo(this) / (this.rotorLength * 2)))
                            * (1 - (dist / this.blastHeight))
                            * ((1 / Math.pow(this.engineSpeed, 3)) * Math.pow(this.getCurrentEngineSpeed(), 3)));
                    float x = (float) ((item.getX() - this.getX()) / itemDist);
                    float z = (float) ((item.getZ() - this.getZ()) / itemDist);
                    item.push(x * moveAmount, 0, z * moveAmount);
                }
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    protected void spawnHoveringParticle() {
        float dist = this.getDistanceToGround();
        BlockState groundBlock = this.getGroundBlock();
        if (groundBlock == null) return; // null-safe
        if (dist <= blastHeight && dist >= 0) {
            for (int i = 0; i < 360; i++) {
                if (Math.random() * 100 < ((1 - (dist / blastHeight))
                        * ((groundBlock.getBlock().equals(Blocks.WATER)) ? 80 : 20))
                        * (((1 / Math.pow(this.engineSpeed, 3)) * Math.pow(this.getCurrentEngineSpeed(), 3)) * 1.0)) {
                    float x = (float) ((Math.cos(Math.toRadians(i)) * (this.rotorLength / 1.8f)) * ((Math.random() * 0.2) + 1));
                    float y = (float) (this.getY() - dist);
                    float z = (float) ((Math.sin(Math.toRadians(i)) * (this.rotorLength / 1.8f)) * ((Math.random() * 0.2) + 1));
                    if (groundBlock.getBlock().equals(Blocks.WATER)) {
                        this.level().addParticle(
                                ModParticles.WASHING_DROPLET.get(),
                                this.getX() + x, y + 0.5f, this.getZ() + z,
                                x / 5, 0.001f, z / 5
                        );
                        this.level().addParticle(ParticleTypes.BUBBLE, this.getX() + x, y + 0.5f, this.getZ() + z, x / 5, 0.001f, z / 5);
                    } else if (!groundBlock.getFluidState().is(FluidTags.LAVA)) {
                        if (this.isBlockDusty(groundBlock.getBlock()) && Math.random() < (this.level().isRaining() ? 0.1 : 0.4)) {
                            this.level().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, groundBlock),
                                    this.getX() + x, y + 0.1, this.getZ() + z,
                                    x / 5, 0.001f + Math.random() * (this.level().isRaining() ? 0 : 0.5), z / 5);
                        }
                        this.level().addParticle(
                                ModParticles.HELICOPTER_GROUND.get(),
                                this.getX() + x, y, this.getZ() + z,
                                x / 5, 0.001f, z / 5
                        );
                    }
                }
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    protected void spawnEngineRunningParticle() {
        float[] offsetBack = this.computeEngineOutletPosition(0.675f, -0.675f, 2.1f, 3.0625f);
        float[] directionBack1 = this.computeEngineExhaustParticleDirection(15);
        float[] directionBack2 = this.computeEngineExhaustParticleDirection(-15);
        for (int i = 0; i < 5; i++) {
            if (Math.random() < this.getCurrentEngineSpeed() / this.engineSpeed) {
                this.level().addParticle(
                        ModParticles.HELICOPTER_ENGINE.get(),
                        this.getX() + offsetBack[0] + Math.random() * 0.3,
                        this.getY() + offsetBack[2] + Math.random() * 0.3,
                        this.getZ() - offsetBack[4] + Math.random() * 0.3,
                        directionBack1[0] * (this.getCurrentEngineSpeed() * 3 / this.engineSpeed),
                        0.001f,
                        directionBack1[2] * (this.getCurrentEngineSpeed() * 3 / this.engineSpeed)
                );
                this.level().addParticle(
                        ModParticles.HELICOPTER_ENGINE.get(),
                        this.getX() + offsetBack[1] + Math.random() * 0.3,
                        this.getY() + offsetBack[3] + Math.random() * 0.3,
                        this.getZ() - offsetBack[5] + Math.random() * 0.3,
                        directionBack2[0] * (this.getCurrentEngineSpeed() * 3 / this.engineSpeed),
                        0.001f,
                        directionBack2[2] * (this.getCurrentEngineSpeed() * 3 / this.engineSpeed)
                );
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    protected void spawnCrashingParticle() {
        if (Math.random() < (-(this.getHealth() * 1.6f - MAX_HEALTH) / MAX_HEALTH)) {
            float[] offsetFront = this.computeEngineOutletPosition(0.675f, -0.675f, 2.1f, 0.9375f);
            float[] offsetBack  = this.computeEngineOutletPosition(0.675f, -0.675f, 2.1f, 3.0625f);
            float[] directionFront1 = this.computeEngineFrontSmokeParticleDirection(-20);
            float[] directionFront2 = this.computeEngineFrontSmokeParticleDirection(20);
            float[] directionBack   = this.computeEngineExhaustParticleDirection(0);
            this.level().addParticle(ParticleTypes.LARGE_SMOKE, this.getX() + offsetBack[0],  this.getY() + offsetBack[2],  this.getZ() - offsetBack[4],  directionBack[0],   directionBack[1],   directionBack[2]);
            this.level().addParticle(ParticleTypes.LARGE_SMOKE, this.getX() + offsetBack[1],  this.getY() + offsetBack[3],  this.getZ() - offsetBack[5],  directionBack[0],   directionBack[1],   directionBack[2]);

            if (this.isInWater() && ((int) this.level().getChunk(this.blockPosition()).getHeight(Heightmap.Types.MOTION_BLOCKING, this.blockPosition().getX(), this.blockPosition().getZ()) - this.getY()) > 2) {
                if (Math.random() < (1 - (this.getHealth() * 2 - MAX_HEALTH) / MAX_HEALTH)) {
                    this.level().addParticle(ParticleTypes.BUBBLE, this.getX() + offsetBack[0],  this.getY() + offsetBack[2],  this.getZ() - offsetBack[4], 0, 0, 0);
                    this.level().addParticle(ParticleTypes.BUBBLE, this.getX() + offsetBack[1],  this.getY() + offsetBack[3],  this.getZ() - offsetBack[5], 0, 0, 0);
                }

                if (Math.random() < (1.0f - (this.getCurrentEngineSpeed() * 1.6f / (float) (this.engineSpeed)))) {
                    this.level().addParticle(ParticleTypes.SMOKE, this.getX() + offsetFront[0], this.getY() + offsetFront[2], this.getZ() - offsetFront[4], directionFront1[0] * 0.5, directionFront1[1] * 0.5, directionFront1[2] * 0.5);
                    this.level().addParticle(ParticleTypes.SMOKE, this.getX() + offsetFront[1], this.getY() + offsetFront[3], this.getZ() - offsetFront[5], directionFront2[0] * 0.5, directionFront2[1] * 0.5, directionFront2[2] * 0.5);
                }
            } else {
                if (Math.random() < (1 - (this.getHealth() * 2 - MAX_HEALTH) / MAX_HEALTH)) {
                    this.level().addParticle(ParticleTypes.FLAME, this.getX() + offsetBack[0],  this.getY() + offsetBack[2],  this.getZ() - offsetBack[4],  directionBack[0],   directionBack[1],   directionBack[2]);
                    this.level().addParticle(ParticleTypes.FLAME, this.getX() + offsetBack[1],  this.getY() + offsetBack[3],  this.getZ() - offsetBack[5],  directionBack[0],   directionBack[1],   directionBack[2]);
                }

                if (Math.random() < (1.0f - (this.getCurrentEngineSpeed() * 1.6f / (float) (this.engineSpeed)))) {
                    this.level().addParticle(ParticleTypes.LARGE_SMOKE, this.getX() + offsetFront[0], this.getY() + offsetFront[2], this.getZ() - offsetFront[4], directionFront1[0], directionFront1[1], directionFront1[2]);
                    this.level().addParticle(ParticleTypes.LARGE_SMOKE, this.getX() + offsetFront[1], this.getY() + offsetFront[3], this.getZ() - offsetFront[5], directionFront2[0], directionFront2[1], directionFront2[2]);
                }
            }
        }
    }

    protected float[] computeEngineOutletPosition(float xPos1, float xPos2, float yPos, float zPos) {
        final float radiantRotationYaw = (float) Math.toRadians(this.getYRot());
        final float radiantPitch = (float) Math.toRadians(this.pitch);
        final float radiantRoll  = (float) Math.toRadians(this.roll);

        float[] offsets = new float[6];
        float[] tmpOffsets;

        offsets[2] = yPos;
        offsets[3] = yPos;
        offsets[4] = zPos;
        offsets[5] = zPos;

        offsets[0] = xPos1;
        offsets[1] = xPos2;
        offsets[2] = (float) (Math.cos(-radiantPitch) * yPos - Math.sin(-radiantPitch) * zPos);
        offsets[3] = (float) (Math.cos(-radiantPitch) * yPos - Math.sin(-radiantPitch) * zPos);
        offsets[4] = (float) (Math.sin(-radiantPitch) * yPos + Math.cos(-radiantPitch) * zPos);
        offsets[5] = (float) (Math.sin(-radiantPitch) * yPos + Math.cos(-radiantPitch) * zPos);

        tmpOffsets = offsets.clone();
        offsets[0] = (float) (Math.cos(-radiantRoll) * tmpOffsets[0] - Math.sin(-radiantRoll) * tmpOffsets[2]);
        offsets[1] = (float) (Math.cos(-radiantRoll) * tmpOffsets[1] - Math.sin(-radiantRoll) * tmpOffsets[3]);
        offsets[2] = (float) (Math.sin(-radiantRoll) * tmpOffsets[0] + Math.cos(-radiantRoll) * tmpOffsets[2]);
        offsets[3] = (float) (Math.sin(-radiantRoll) * tmpOffsets[1] + Math.cos(-radiantRoll) * tmpOffsets[3]);

        tmpOffsets = offsets.clone();
        offsets[0] = (float) (Math.cos(radiantRotationYaw) * tmpOffsets[0] + Math.sin(radiantRotationYaw) * tmpOffsets[4]);
        offsets[1] = (float) (Math.cos(radiantRotationYaw) * tmpOffsets[1] + Math.sin(radiantRotationYaw) * tmpOffsets[5]);
        offsets[4] = (float) (-Math.sin(radiantRotationYaw) * tmpOffsets[0] + Math.cos(radiantRotationYaw) * tmpOffsets[4]);
        offsets[5] = (float) (-Math.sin(radiantRotationYaw) * tmpOffsets[1] + Math.cos(radiantRotationYaw) * tmpOffsets[5]);
        return offsets;
    }

    protected float[] computeEngineExhaustParticleDirection(float rotOffset) {
        float[] directions = new float[3];
        float engineBlast = (this.getCurrentEngineSpeed() / this.engineSpeed) * 0.2f;
        directions[0] = (float) (Math.sin(-Math.toRadians(this.getYRot() + rotOffset)) * (-0.05f - engineBlast) + ((Math.random() - 0.5) * 0.1));
        directions[1] = 0.01f;
        directions[2] = (float) (Math.cos(Math.toRadians(this.getYRot() + rotOffset)) * (-0.05f - engineBlast) + ((Math.random() - 0.5) * 0.1));
        return directions;
    }

    protected float[] computeEngineFrontSmokeParticleDirection(float rotOffset) {
        float[] directions = new float[3];
        float engineBlast = (this.getCurrentEngineSpeed() / this.engineSpeed) * 0.2f;
        directions[0] = (float) (Math.sin(-Math.toRadians(this.getYRot() + rotOffset)) * (0.05f + engineBlast) + ((Math.random() - 0.5) * 0.1));
        directions[1] = 0.0f;
        directions[2] = (float) (Math.cos(Math.toRadians(this.getYRot() + rotOffset)) * (0.05f + engineBlast) + ((Math.random() - 0.5) * 0.1));
        return directions;
    }

    @OnlyIn(Dist.CLIENT)
    protected void playWarningSound() {
        if (this.getControllingPassenger() == Minecraft.getInstance().player) {
            if (this.getHealth() / this.MAX_HEALTH < 0.3 && this.warningDelay <= 0) {
                this.level().playSound(Minecraft.getInstance().player, Minecraft.getInstance().player.blockPosition(),
                        SoundEvent.createVariableRangeEvent(this.warningSoundResource), SoundSource.BLOCKS,
                        (float) 0.1 * (1 - (this.getHealth() / this.MAX_HEALTH)), 1);
                this.warningDelay = 17;
            } else if (this.getHealth() / this.MAX_HEALTH < 0.3) {
                this.warningDelay--;
            }
        }
    }

    private boolean shouldAdjustEngineSpeedByHorizontalControls(float requiredSpeedForHovering) {
        return (this.forward() || this.backward() || this.left() || this.right())
                && !(this.getCurrentEngineSpeed() > requiredSpeedForHovering && this.upward())
                && !(this.getCurrentEngineSpeed() < requiredSpeedForHovering && this.downward());
    }

    private boolean shouldAdjustEngineSpeedWithoutHorizontalControls(float requiredSpeedForHovering) {
        return !(this.forward() || this.backward() || this.left() || this.right())
                && !(this.getCurrentEngineSpeed() > requiredSpeedForHovering && this.upward())
                && !(this.getCurrentEngineSpeed() < requiredSpeedForHovering && this.downward());
    }

    protected void handleKeyEnableAutoPilot(boolean shouldChange) {
        if (shouldChange) this.simpleControle = !this.simpleControle;
    }

    protected void handleKeyLock(boolean shouldChange) {
        if (shouldChange) this.lockOn = !this.lockOn;
    }

    private void rotateLeft(boolean keyPressed) {
        if (keyPressed) this.rotationControl = 1;
        else if (this.rotationControl == 1) { if (!rotateRight()) this.rotationControl = 0; }
    }

    private void rotateRight(boolean keyPressed) {
        if (keyPressed) {
            if (!rotateLeft()) this.rotationControl = 2;
            else this.rotationControl = 0;
        } else if (this.rotationControl == 2) {
            if (!rotateLeft()) this.rotationControl = 0;
        }
    }

    protected boolean rotateLeft()  { return this.rotationControl == 1; }
    protected boolean rotateRight() { return this.rotationControl == 2; }

    protected boolean isBlockDusty(Block block) {
        return block.equals(Blocks.SAND) || block.equals(Blocks.SOUL_SAND) || block.equals(Blocks.GRAVEL);
    }

    @Override
    public void dropItems() {
        this.dropRecordedItemOrLoot(false);
    }

    private boolean shouldCrashOnLanding() {
        Vec3 movement = this.getDeltaMovement();
        double horizontalSpeed = movement.horizontalDistance();
        double verticalSpeed = Math.abs(movement.y);
        boolean isSevereTilt = Math.abs(this.pitch) > 40 || Math.abs(this.roll) > 40;
        boolean hasHardVerticalImpact = verticalSpeed > 0.45;
        boolean hasHardHorizontalImpact = horizontalSpeed > 0.55;
        boolean isSpinningViolently = Math.abs(this.yawRotationAcceleration) > 0.6f && (verticalSpeed > 0.35 || horizontalSpeed > 0.35);

        if (isSevereTilt && (hasHardVerticalImpact || hasHardHorizontalImpact)) return true;
        return isSpinningViolently;
    }

    // Physics helpers
    protected void changeCurrentEngineSpeed(float changeSpeed) { this.setCurrentEngineSpeed(this.getCurrentEngineSpeed() + changeSpeed); }

    protected void setCurrentEngineSpeed(float speed) {
        this.currentEngineSpeed = speed;
        if (this.currentEngineSpeed > this.engineSpeed) this.currentEngineSpeed = this.engineSpeed;
        else if (this.currentEngineSpeed < 0) this.currentEngineSpeed = 0;
    }

    protected float computeRotorSweptArea() { return (float) (Math.PI * Math.pow(this.rotorLength, 2)); }
    protected float computeShaftPower() { return (float) (this.torque * this.getCurrentEngineSpeed() * (Math.PI / 30)); }
    protected float computeRotorForce() { return (float) Math.pow(2 * 1.2f * this.computeRotorSweptArea() * Math.pow(this.qualityGrade * this.computeShaftPower(), 2), (1.0 / 3.0)); }
    protected float computeVerticalForce() { return ((1.0f - Math.abs(this.pitch) / 90) * (1 - Math.abs(this.roll) / 90)) * this.computeRotorForce(); }

    protected float computeHorizontalForceFrontBack() {
        if (this.pitch <= 90.0f && this.pitch >= -90.0f) {
            return ((this.pitch / 90.0f) * (1.0f - Math.abs(this.roll) / 90.0f))
                    * (((1.0f - Math.abs(this.pitch) / 90.0f) * (1.0f - Math.abs(this.roll) / 90.0f)) * 5f)
                    * this.computeRotorForce();
        } else {
            if (this.pitch < 0) {
                return ((1.0f + (this.pitch + 90.0f) / 90.0f) * (1.0f - Math.abs(this.roll) / 90.0f))
                        * (((Math.abs(this.pitch + 90) / 90.0f) * (1.0f - Math.abs(this.roll) / 90.0f)) * 5f)
                        * this.computeRotorForce();
            } else {
                return ((1.0f - (this.pitch - 90.0f) / 90.0f) * (1.0f - Math.abs(this.roll) / 90.0f))
                        * (((Math.abs(this.pitch - 90) / 90.0f) * (1.0f - Math.abs(this.roll) / 90.0f)) * 5f)
                        * this.computeRotorForce();
            }
        }
    }

    protected float computeHorizontalForceLeftRight() {
        return ((this.roll / 90.0f) * (1.0f - this.pitch / 90.0f)) * this.computeRotorForce();
    }

    public float getCurrentEngineSpeed() { return this.currentEngineSpeed; }

    protected float computeRequiredEngineSpeedForHover() {
        float S = (9.81f * this.weight) / ((1 - Math.abs(this.pitch) / 90.0f) * (1.0f - Math.abs(this.roll) / 90.0f));
        return (float) (((1 / this.qualityGrade) * Math.sqrt(Math.pow(S, 3) / (2 * 1.2f * this.computeRotorSweptArea())))
                / (this.torque * (Math.PI / 30)));
    }

    protected float computeThrottleUpDown() { return ((float) this.enginePower / 735.5f) * 0.00029481132f; }

    protected float computeTorque() {
        float denom = (float) (engineSpeed * (Math.PI / 30));
        if (denom <= 0f) return 0f;
        return (float) (enginePower / denom);
    }
}
