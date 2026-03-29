package net.vit.jurassicreborn.common.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimation;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.CompsognathusEntity;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.HypsilophodonEntity;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.LeptictidiumEntity;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.MicroceratusEntity;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.MicroraptorEntity;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;
import net.vit.jurassicreborn.common.entities.ai.DinosaurAttackMeleeEntityAI;
import net.vit.jurassicreborn.common.entities.ai.DinosaurWanderEntityAI;
import net.vit.jurassicreborn.common.entities.ai.metabolism.DrinkEntityAI;
import net.vit.jurassicreborn.common.entities.ai.metabolism.EatFoodItemEntityAI;
import net.vit.jurassicreborn.common.entities.ai.metabolism.FeederEntityAI;
import net.vit.jurassicreborn.common.entities.ai.navigation.DinosaurMoveHelper;

import java.util.EnumSet;

public abstract class FlyingDinosaurEntity extends DinosaurEntity {

    private int ticksOnFloor;
    private int ticksInAir;
    private boolean blocked;

    private boolean takingOff;
    public boolean shouldLand;
    private int idleFlightTicks;
    private int takeoffSmoothTicks;
    private Vec3 takeoffTargetVelocity = Vec3.ZERO;

    private static final double FLIGHT_ACCEL = 0.05D;
    private static final double MAX_FLIGHT_SPEED = 0.55D;
    private static final double MAX_ASCENT = 0.45D;
    private static final double MAX_DESCENT = -0.45D;
    private static final double ARRIVE_SLOW_RADIUS = 4.0D;

    public FlyingDinosaurEntity(Level world, EntityType type, Dinosaur dino) {
        super(world, type, dino);
        this.blocked = false;

        this.moveControl = new FlyingMoveHelper();

        this.goalSelector.addGoal(0, new DinosaurAttackMeleeEntityAI(this, 1.0D, true));
        this.goalSelector.addGoal(1, new AIFlyLand());
        this.goalSelector.addGoal(0, new DrinkEntityAI(this));
        this.goalSelector.addGoal(0, new EatFoodItemEntityAI(this));
        this.goalSelector.addGoal(0, new FeederEntityAI(this));
        this.goalSelector.addGoal(2, new AIStartFlying());
        this.goalSelector.addGoal(3, new AILookAround());
        this.goalSelector.addGoal(4, new AIRandomFly());
        this.goalSelector.addGoal(5, new AIWander());
        this.goalSelector.addGoal(6, new AdvancedSwimEntityAI(this));

        this.doesEatEggs(true);
        this.doTarget();
    }

    @Override
    protected void updateWalkAnimation(float partialTicks) {
        if (!this.isTouchingGround()) {
            return;
        }
        super.updateWalkAnimation(partialTicks);
    }

    protected void doTarget() {
        this.target(
                LeptictidiumEntity.class,
                HypsilophodonEntity.class,
                MicroraptorEntity.class,
                MicroceratusEntity.class,
                CompsognathusEntity.class
        );
    }

    @Override
    public void tick() {
        if (!this.onGround() && this.getAnimation() == EntityAnimation.SLEEPING.get()) {
            this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.2D, 0.0D));
        }

        if (this.getMetabolism().isStarving() || this.getMetabolism().isThirsty()) {
            this.shouldLand = true;
        }

        if (this.isTouchingGround()) {
            this.ticksInAir = 0;
            this.ticksOnFloor++;
            this.idleFlightTicks = 0;
            this.shouldLand = false;
            this.takingOff = false;
            this.takeoffSmoothTicks = 0;
            this.takeoffTargetVelocity = Vec3.ZERO;
        } else {
            this.ticksInAir++;
            this.ticksOnFloor = 0;

            boolean moving = this.getDeltaMovement().lengthSqr() > 2.5E-3D;
            if (!moving || !this.getMoveControl().hasWanted()) {
                this.idleFlightTicks++;
            } else {
                this.idleFlightTicks = 0;
            }

            if (this.idleFlightTicks > 60) {
                this.shouldLand = true;
            }
        }

        if (this.ticksInAir > 150) {
            this.takingOff = false;
        }

        if (this.takingOff && this.takeoffSmoothTicks > 0) {
            Vec3 current = this.getDeltaMovement();
            Vec3 desired = this.takeoffTargetVelocity;
            Vec3 blended = current.lerp(desired, 0.45D);

            if (blended.y < desired.y * 0.6D) {
                blended = new Vec3(blended.x, desired.y * 0.6D, blended.z);
            }

            this.setDeltaMovement(blended);
            this.hasImpulse = true;
            this.hurtMarked = true;
            this.takeoffSmoothTicks--;

            if (this.takeoffSmoothTicks == 0) {
                this.takeoffTargetVelocity = Vec3.ZERO;
            }
        } else if (!this.takingOff) {
            this.takeoffSmoothTicks = 0;
            this.takeoffTargetVelocity = Vec3.ZERO;
        }

        boolean keepLift = !this.isTouchingGround() && (this.takingOff || !this.shouldLand);
        this.setNoGravity(keepLift);

        if (this.shouldLand && !this.isTouchingGround() && !this.getMoveControl().hasWanted()) {
            Vec3 current = this.getDeltaMovement();
            this.setDeltaMovement(current.x * 0.91D, Math.min(current.y, -0.08D), current.z * 0.91D);
        }

        super.tick();
    }

    @Override
    public boolean isImmobile() {
        return this.isCarcass() || this.isSleeping() || this.blocked;
    }

    public boolean isTouchingGround() {
        if (this.isDeadOrDying() || this.isCarcass() || this.isInWater()) {
            return true;
        }
        if (this.takingOff) {
            return false;
        }

        AABB box = this.getBoundingBox().inflate(0.24D);
        boolean anyCollision = !this.level().noCollision(this, box);
        return anyCollision || this.onGround();
    }

    public void startTakeOff() {
        Vec3 motion = this.getDeltaMovement();
        Vec3 boosted = new Vec3(motion.x * 0.4D, Math.max(motion.y, 0.35D), motion.z * 0.4D);

        this.setDeltaMovement(boosted);
        this.takeoffTargetVelocity = new Vec3(boosted.x, Math.max(boosted.y, 0.45D), boosted.z);
        this.takeoffSmoothTicks = 8;
        this.takingOff = true;
        this.shouldLand = false;
        this.hasImpulse = true;
        this.hurtMarked = true;
    }

    @Override
    public boolean causeFallDamage(float distance, float damageMultiplier, DamageSource source) {
        return false;
    }

    @Override
    public boolean onClimbable() {
        return false;
    }

    private boolean isCourseTraversable(Vec3 loc) {
        double distance = this.position().distanceTo(loc) + 1.0D;
        double dX = (loc.x - this.getX()) / distance;
        double dY = (loc.y - this.getY()) / distance;
        double dZ = (loc.z - this.getZ()) / distance;

        AABB box = this.getBoundingBox();
        for (double i = 1.0D; i < distance; i += 0.75D) {
            box = box.move(dX, dY, dZ);
            if (!this.level().noCollision(this, box)) {
                return false;
            }
        }
        return true;
    }

    protected void updateCustomFlightAnimation() {
        double dx = this.getX() - this.xo;
        double dz = this.getZ() - this.zo;
        float dist = Mth.sqrt((float) (dx * dx + dz * dz)) * 4.0F;

        if (dist > 1.0F) {
            dist = 1.0F;
        }

        this.walkAnimation.update(dist, 0.4F);
    }

    @Override
    public void travel(Vec3 travelVec) {
        if (this.tranqed || this.isTouchingGround()) {
            super.travel(travelVec);
            return;
        }

        if (this.isInWater()) {
            this.moveRelative(0.02F, travelVec);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.8D));
        } else if (this.isInLava()) {
            this.moveRelative(0.02F, travelVec);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.5D));
        } else {
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.91D));
        }

        this.updateCustomFlightAnimation();
    }

    class AIStartFlying extends Goal {
        private final FlyingDinosaurEntity dino = FlyingDinosaurEntity.this;

        public AIStartFlying() {
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return this.dino.ticksOnFloor >= 220
                    && this.dino.isTouchingGround()
                    && this.dino.getRandom().nextFloat() < 0.03F;
        }

        @Override
        public boolean canContinueToUse() {
            return false;
        }

        @Override
        public void start() {
            this.dino.startTakeOff();
            this.dino.setAnimation(EntityAnimation.FLYING.get());
            this.dino.shouldLand = false;
            this.dino.idleFlightTicks = 0;

            RandomSource random = this.dino.getRandom();
            double x = this.dino.getX() + (random.nextFloat() * 2.0F - 1.0F) * 4.0D;
            double y = this.dino.getY() + 3.0D + random.nextFloat() * 3.0D;
            double z = this.dino.getZ() + (random.nextFloat() * 2.0F - 1.0F) * 4.0D;

            this.dino.getMoveControl().setWantedPosition(x, y, z, 1.2D);
        }
    }

    class AIRandomFly extends Goal {
        private final FlyingDinosaurEntity dino = FlyingDinosaurEntity.this;

        @Override
        public boolean canUse() {
            if (this.dino.onGround()) {
                return false;
            }

            MoveControl ctl = this.dino.getMoveControl();
            if (!ctl.hasWanted()) {
                return true;
            }

            double dx = ctl.getWantedX() - this.dino.getX();
            double dy = ctl.getWantedY() - this.dino.getY();
            double dz = ctl.getWantedZ() - this.dino.getZ();
            double dist = dx * dx + dy * dy + dz * dz;
            return dist < 3.0D || dist > 3600.0D;
        }

        @Override
        public void start() {
            for (int i = 0; i < 100; i++) {
                double dx = this.dino.getX() + (this.dino.getRandom().nextFloat() * 2.0F - 1.0F) * 16.0F;
                double dy = this.dino.getY() + (this.dino.getRandom().nextFloat() * 2.0F - 1.0F) * 8.0F;
                double dz = this.dino.getZ() + (this.dino.getRandom().nextFloat() * 2.0F - 1.0F) * 16.0F;
                Vec3 pos = new Vec3(dx, dy, dz);

                if (this.dino.isCourseTraversable(pos)) {
                    this.dino.setAnimation(EntityAnimation.FLYING.get());
                    this.dino.getMoveControl().setWantedPosition(dx, dy, dz, 1.0D);
                    return;
                }
            }

            this.dino.shouldLand = true;
        }
    }

    private boolean isOverWater() {
        BlockPos posBelow = this.blockPosition().below();
        return this.level().getBlockState(posBelow).is(Blocks.WATER);
    }

    class AIFlyLand extends Goal {
        private final FlyingDinosaurEntity dino = FlyingDinosaurEntity.this;

        public AIFlyLand() {
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            if (this.dino.ticksInAir <= 150
                    && this.dino.isTouchingGround()
                    && this.dino.isInWater()
                    && this.dino.isOverWater()) {
                return false;
            }

            MoveControl ctrl = this.dino.getMoveControl();
            if ((!ctrl.hasWanted() && this.dino.getRandom().nextFloat() < 0.1F)
                    || (!ctrl.hasWanted() && this.dino.shouldLand)) {
                return true;
            }

            double dx = ctrl.getWantedX() - this.dino.getX();
            double dy = ctrl.getWantedY() - this.dino.getY();
            double dz = ctrl.getWantedZ() - this.dino.getZ();
            double d = dx * dx + dy * dy + dz * dz;

            if (d < 1.0D || d > 3600.0D) {
                BlockPos posBelow = this.dino.blockPosition().below();
                return this.dino.level().getBlockState(posBelow).isAir()
                        && this.dino.getRandom().nextFloat() < 0.01F;
            }

            return false;
        }

        @Override
        public boolean canContinueToUse() {
            return false;
        }

        @Override
        public void start() {
            RandomSource random = this.dino.getRandom();
            double dstX = this.dino.getX() + (random.nextFloat() * 2.0F - 1.0F) * 16.0F;
            double dstZ = this.dino.getZ() + (random.nextFloat() * 2.0F - 1.0F) * 16.0F;
            int topY = this.dino.level().getHeight(
                    Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    Mth.floor(dstX),
                    Mth.floor(dstZ)
            );
            double dstY = topY;

            BlockPos.MutableBlockPos probe = new BlockPos.MutableBlockPos(
                    Mth.floor(dstX),
                    Mth.floor(dstY - 1.0D),
                    Mth.floor(dstZ)
            );

            Level level = this.dino.level();
            while (probe.getY() > level.getMinBuildHeight() && level.getBlockState(probe).isAir()) {
                probe.move(0, -1, 0);
            }

            if (!level.getBlockState(probe).isAir()) {
                double landingY = probe.getY() + 0.2D;
                this.dino.getMoveControl().setWantedPosition(dstX, landingY, dstZ, 1.15D);
                this.dino.setAnimation(EntityAnimation.ON_LAND.get());
            } else {
                this.dino.shouldLand = false;
            }
        }
    }

    class FlyingMoveHelper extends DinosaurMoveHelper {
        private final FlyingDinosaurEntity parent = FlyingDinosaurEntity.this;

        public FlyingMoveHelper() {
            super(FlyingDinosaurEntity.this);
        }

        @Override
        public void tick() {
            if (this.parent.isTouchingGround()) {
                super.tick();
                return;
            }

            if (this.operation != MoveControl.Operation.MOVE_TO) {
                this.parent.setZza(0.0F);
                return;
            }

            double dx = this.wantedX - this.parent.getX();
            double dy = this.wantedY - this.parent.getY();
            double dz = this.wantedZ - this.parent.getZ();
            double distSqr = dx * dx + dy * dy + dz * dz;

            if (distSqr < 2.500000277905201E-7D) {
                this.operation = MoveControl.Operation.WAIT;
                this.parent.setZza(0.0F);
                return;
            }

            double dist = Math.sqrt(distSqr);
            if (!this.isNotColliding(this.wantedX, this.wantedY, this.wantedZ, dist)) {
                this.operation = MoveControl.Operation.WAIT;
                this.parent.shouldLand = true;
                return;
            }

            float yaw = (float) (Mth.atan2(dz, dx) * 57.2957763671875D) - 90.0F;
            this.parent.setYRot(this.rotlerp(this.parent.getYRot(), yaw, 90.0F));
            this.parent.setYBodyRot(this.parent.getYRot());

            double horizontal = Math.sqrt(dx * dx + dz * dz);
            if (Math.abs(dy) > 1.0E-5D || Math.abs(horizontal) > 1.0E-5D) {
                float pitch = (float) (-(Mth.atan2(dy, horizontal) * 57.2957763671875D));
                this.parent.setXRot(this.rotlerp(this.parent.getXRot(), pitch, 20.0F));
            }

            double accel = FLIGHT_ACCEL * this.speedModifier;
            if (dist < ARRIVE_SLOW_RADIUS) {
                accel *= dist / ARRIVE_SLOW_RADIUS;
            }

            Vec3 velocity = this.parent.getDeltaMovement().add(
                    dx / dist * accel,
                    dy / dist * accel,
                    dz / dist * accel
            );

            velocity = new Vec3(
                    velocity.x,
                    Mth.clamp(velocity.y, MAX_DESCENT, MAX_ASCENT),
                    velocity.z
            );

            double speed = velocity.length();
            if (speed > MAX_FLIGHT_SPEED) {
                velocity = velocity.scale(MAX_FLIGHT_SPEED / speed);
            }

            this.parent.setDeltaMovement(velocity);

            if (distSqr < 0.6D) {
                this.parent.setDeltaMovement(this.parent.getDeltaMovement().scale(0.6D));
                if (distSqr < 0.05D) {
                    this.operation = MoveControl.Operation.WAIT;
                }
            }
        }

        private boolean isNotColliding(double x, double y, double z, double distance) {
            double d0 = (x - this.parent.getX()) / distance;
            double d1 = (y - this.parent.getY()) / distance;
            double d2 = (z - this.parent.getZ()) / distance;
            AABB bounds = this.parent.getBoundingBox();

            for (double i = 1.0D; i < distance; i += 0.75D) {
                bounds = bounds.move(d0, d1, d2);
                if (!this.parent.level().noCollision(this.parent, bounds)) {
                    return false;
                }
            }

            return true;
        }
    }

    class AILookAround extends Goal {
        private final FlyingDinosaurEntity dino = FlyingDinosaurEntity.this;

        public AILookAround() {
            this.setFlags(EnumSet.of(Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            return true;
        }

        @Override
        public void tick() {
            Vec3 vel = this.dino.getDeltaMovement();
            boolean moving = vel.lengthSqr() > 1.0E-4D;

            if (!moving) {
                MoveControl mc = this.dino.getMoveControl();
                if (mc.hasWanted()) {
                    double dx = mc.getWantedX() - this.dino.getX();
                    double dz = mc.getWantedZ() - this.dino.getZ();
                    if (dx * dx + dz * dz > 1.0E-4D) {
                        float yaw = -(float) (Mth.atan2(dx, dz) * (180.0F / (float) Math.PI));
                        this.dino.setYRot(yaw);
                        this.dino.setYBodyRot(yaw);
                    }
                }
                return;
            }

            if (this.dino.getTarget() == null) {
                float yaw = -(float) (Mth.atan2(vel.x, vel.z) * (180.0F / (float) Math.PI));
                this.dino.setYRot(yaw);
                this.dino.setYBodyRot(yaw);
            } else {
                net.minecraft.world.entity.LivingEntity target = this.dino.getTarget();
                double max = 64.0D;
                if (this.dino.distanceToSqr(target) < max * max) {
                    double dx = target.getX() - this.dino.getX();
                    double dz = target.getZ() - this.dino.getZ();
                    float yaw = -(float) (Mth.atan2(dx, dz) * (180.0F / (float) Math.PI));
                    this.dino.setYRot(yaw);
                    this.dino.setYBodyRot(yaw);
                }
            }
        }
    }

    class AIWander extends DinosaurWanderEntityAI {
        private final FlyingDinosaurEntity dino = FlyingDinosaurEntity.this;

        public AIWander() {
            super(FlyingDinosaurEntity.this, 0.8D, 10, 10);
        }

        @Override
        public boolean canUse() {
            if (!this.dino.isTouchingGround()) {
                return false;
            }
            return super.canUse();
        }
    }
}