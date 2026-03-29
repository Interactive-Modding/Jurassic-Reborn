package net.vit.jurassicreborn.common.entities.ai.navigation;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.vit.jurassicreborn.common.entities.SwimmingDinosaurEntity;

import java.util.EnumSet;

public class MoveUnderwaterEntityAI extends Goal {

    public enum Phase {
        IDLE,
        PATROL,
        CHASE
    }

    private static final float CRUISE_FACTOR = 2.10F;
    private static final float CHASE_FACTOR  = 0.5F;

    private static final double PATROL_H = 18.0D;
    private static final double PATROL_V = 7.0D;
    private static final double ARRIVAL_SQ = 3.25D * 3.25D;
    private static final int WAYPOINT_LIFE = 180;
    private static final int DRIFT_INTERVAL = 24;

    private static final double IDLE_H_TARGET = 0.050D;
    private static final double IDLE_BOB_AMP  = 0.008D;
    private static final double IDLE_BLEND    = 0.20D;

    private static final double CRUISE_BLEND_H = 0.34D;
    private static final double CRUISE_BLEND_V = 0.24D;

    private static final double CHASE_BLEND_H = 0.42D;
    private static final double CHASE_BLEND_V = 0.30D;

    private static final float YAW_IDLE   = 2.0F;
    private static final float YAW_CRUISE = 5.2F;
    private static final float YAW_CHASE  = 6.6F;
    private static final float PITCH_RATE = 3.1F;
    private static final float MAX_PITCH  = 30.0F;

    private static final double FLOOR_LIFT   = 0.070D;
    private static final double SURFACE_PUSH = 0.030D;

    private static final int TARGET_LOCK_TICKS = 18;

    // Only new constant: validate an actual swimmable corridor, not just the endpoint.
    private static final double PATH_PROBE_STEP = 0.75D;

    private final SwimmingDinosaurEntity entity;

    private Phase phase = Phase.IDLE;
    private Vec3 waypoint = null;
    private Vec3 driftDir = Vec3.ZERO;

    private float scanPhase = 0.0F;

    private int waypointTimer = 0;
    private int driftTimer = 0;
    private int biteCooldown = 0;

    private double baseSpeed = 0.0D;
    private boolean active = false;

    private LivingEntity lockedTarget = null;
    private int lockedTargetTicks = 0;

    public MoveUnderwaterEntityAI(SwimmingDinosaurEntity entity) {
        this.entity = entity;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    public boolean isActive() {
        return this.active;
    }

    @Override
    public boolean canUse() {
        return this.entity.shouldUseGenericUnderwaterAI()
                && this.entity.canDinoSwim()
                && !this.entity.isCarcass()
                && !this.entity.isMovementBlocked()
                && this.entity.isInWater();
    }

    @Override
    public boolean canContinueToUse() {
        return this.entity.shouldUseGenericUnderwaterAI()
                && this.entity.canDinoSwim()
                && !this.entity.isCarcass()
                && !this.entity.isMovementBlocked()
                && this.entity.isInWater();
    }

    @Override
    public void start() {
        this.active = true;
        this.baseSpeed = this.entity.getAttributeValue(Attributes.MOVEMENT_SPEED);
        this.phase = Phase.IDLE;
        this.waypoint = null;
        this.scanPhase = this.entity.getRandom().nextFloat() * Mth.TWO_PI;
        this.driftTimer = 0;
        this.biteCooldown = 0;
        this.lockedTarget = null;
        this.lockedTargetTicks = 0;
        this.pickDriftDir();
        this.entity.getNavigation().stop();
    }

    @Override
    public void stop() {
        this.active = false;
        this.entity.getNavigation().stop();
        this.waypoint = null;
        this.phase = Phase.IDLE;
        this.lockedTarget = null;
        this.lockedTargetTicks = 0;
    }

    @Override
    public void tick() {
        if (this.entity.level().isClientSide) {
            return;
        }

        if (this.entity.tickCount % 10 == 0) {
            this.baseSpeed = this.entity.getAttributeValue(Attributes.MOVEMENT_SPEED);
        }

        this.scanPhase += 0.018F;
        if (this.scanPhase > (float) (Math.PI * 2.0)) {
            this.scanPhase -= (float) (Math.PI * 2.0);
        }

        if (this.waypointTimer > 0) this.waypointTimer--;
        if (this.driftTimer > 0) this.driftTimer--;
        if (this.biteCooldown > 0) this.biteCooldown--;

        LivingEntity combatTarget = this.resolveCombatTarget();
        this.updatePhase(combatTarget);

        switch (this.phase) {
            case IDLE -> this.tickIdle();
            case PATROL -> this.tickPatrol();
            case CHASE -> this.tickChase(combatTarget);
        }
    }

    private void updatePhase(LivingEntity combatTarget) {
        if (combatTarget != null) {
            this.phase = Phase.CHASE;
            this.waypoint = null;
            return;
        }

        switch (this.phase) {
            case CHASE -> {
                this.phase = Phase.IDLE;
                this.driftTimer = 0;
                this.pickDriftDir();
            }
            case IDLE -> {
                if (this.entity.getRandom().nextInt(30) == 0) {
                    this.phase = Phase.PATROL;
                    this.waypoint = null;
                    this.waypointTimer = WAYPOINT_LIFE;
                }
            }
            case PATROL -> {
                if (this.waypointTimer <= 0 || this.waypoint == null || this.arrivedAt(this.waypoint)) {
                    this.phase = Phase.IDLE;
                    this.waypoint = null;
                    this.driftTimer = 0;
                    this.pickDriftDir();
                }
            }
        }
    }

    private void tickIdle() {
        if (this.driftTimer <= 0) {
            this.pickDriftDir();
            this.driftTimer = DRIFT_INTERVAL + this.entity.getRandom().nextInt(DRIFT_INTERVAL);
        }

        Vec3 cur = this.entity.getDeltaMovement();

        double targetX = this.driftDir.x * IDLE_H_TARGET;
        double targetY = Math.sin(this.entity.bodyBobPhase) * IDLE_BOB_AMP;
        double targetZ = this.driftDir.z * IDLE_H_TARGET;

        double nX = Mth.lerp(IDLE_BLEND, cur.x, targetX);
        double nY = Mth.lerp(IDLE_BLEND, cur.y, targetY);
        double nZ = Mth.lerp(IDLE_BLEND, cur.z, targetZ);

        if (this.isNearFloor()) {
            nY = Math.max(nY, FLOOR_LIFT);
        }
        if (this.isNearSurface() && nY > 0.0D) {
            nY -= SURFACE_PUSH;
        }

        this.entity.setDeltaMovement(nX, nY, nZ);

        if (this.driftDir.horizontalDistanceSqr() > 1.0E-5D) {
            float targetYaw = (float) Math.toDegrees(Math.atan2(-this.driftDir.x, this.driftDir.z));
            float scanOffset = (float) (Math.sin(this.scanPhase) * 10.0D);

            this.entity.setYRot(smoothAngle(this.entity.getYRot(), targetYaw + scanOffset, YAW_IDLE));
            this.entity.yBodyRot = this.entity.getYRot();
            this.entity.yHeadRot = this.entity.getYRot();
        }

        this.entity.setXRot(smoothAngle(this.entity.getXRot(), 0.0F, PITCH_RATE));
    }

    private void tickPatrol() {
        if (this.waypoint == null || !this.isWaypointUsable(this.waypoint)) {
            this.waypoint = this.pickWaypoint();
            if (this.waypoint == null) {
                this.phase = Phase.IDLE;
                this.driftTimer = 0;
                this.pickDriftDir();
                return;
            }
        }

        Vec3 pos = this.entity.position();
        Vec3 toWaypoint = this.waypoint.subtract(pos);
        double distSq = toWaypoint.lengthSqr();

        if (distSq < ARRIVAL_SQ) {
            this.phase = Phase.IDLE;
            this.waypoint = null;
            return;
        }

        Vec3 dir = toWaypoint.normalize();

        if (this.isNearSurface() && dir.y > 0.0D) {
            dir = new Vec3(dir.x, dir.y * 0.30D, dir.z).normalize();
        }
        if (this.isNearFloor() && dir.y < 0.0D) {
            dir = new Vec3(dir.x, Math.max(0.18D, -dir.y), dir.z).normalize();
        }

        float targetYaw = (float) Math.toDegrees(Math.atan2(-dir.x, dir.z));
        float targetPitch = Mth.clamp(
                (float) Math.toDegrees(Math.atan2(-dir.y, Math.sqrt(dir.x * dir.x + dir.z * dir.z))),
                -MAX_PITCH,
                MAX_PITCH
        );

        this.entity.setYRot(smoothAngle(this.entity.getYRot(), targetYaw, YAW_CRUISE));
        this.entity.setXRot(smoothAngle(this.entity.getXRot(), targetPitch, PITCH_RATE));
        this.entity.yBodyRot = this.entity.getYRot();
        this.entity.yHeadRot = this.entity.getYRot();

        double speed = Math.max(0.12D, this.baseSpeed * CRUISE_FACTOR);
        Vec3 cur = this.entity.getDeltaMovement();

        double targetVX = dir.x * speed * 0.90D;
        double targetVY = dir.y * speed * 0.52D;
        double targetVZ = dir.z * speed * 0.90D;

        this.entity.setDeltaMovement(
                Mth.lerp(CRUISE_BLEND_H, cur.x, targetVX),
                Mth.lerp(CRUISE_BLEND_V, cur.y, targetVY),
                Mth.lerp(CRUISE_BLEND_H, cur.z, targetVZ)
        );
    }

    private void tickChase(LivingEntity target) {
        if (target == null) {
            this.phase = Phase.IDLE;
            return;
        }

        if (!this.isUsableCombatTarget(target)) {
            this.clearCombatTarget(target);
            this.phase = Phase.IDLE;
            return;
        }

        Vec3 selfAim = this.entity.position().add(0.0D, this.entity.getBbHeight() * 0.35D, 0.0D);
        Vec3 targetAim = target.position().add(0.0D, target.getBbHeight() * 0.35D, 0.0D);
        Vec3 toTarget = targetAim.subtract(selfAim);

        double distSq = toTarget.lengthSqr();
        if (distSq < 1.0E-6D) {
            return;
        }

        double maxChase = this.entity.getUnderwaterChaseRange();
        if (distSq > maxChase * maxChase) {
            this.clearCombatTarget(target);
            this.phase = Phase.IDLE;
            return;
        }

        Vec3 dir = toTarget.normalize();

        boolean targetInWater = target.isInWaterOrBubble();

        if (this.isNearSurface() && dir.y > 0.0D && !targetInWater) {
            dir = new Vec3(dir.x, Math.min(0.0D, dir.y * 0.05D), dir.z).normalize();
        } else {
            if (this.isNearSurface() && dir.y > 0.0D) {
                dir = new Vec3(dir.x, dir.y * 0.28D, dir.z).normalize();
            }
        }

        if (this.isNearFloor() && dir.y < 0.0D) {
            dir = new Vec3(dir.x, Math.max(0.12D, -dir.y), dir.z).normalize();
        }

        float targetYaw = (float) Math.toDegrees(Math.atan2(-dir.x, dir.z));
        float targetPitch = Mth.clamp(
                (float) Math.toDegrees(Math.atan2(-dir.y, Math.sqrt(dir.x * dir.x + dir.z * dir.z))),
                -MAX_PITCH,
                MAX_PITCH
        );

        float yawStep = this.entity.hurtTime > 0 ? 4.0F : YAW_CHASE;

        this.entity.setYRot(smoothAngle(this.entity.getYRot(), targetYaw, yawStep));
        this.entity.setXRot(smoothAngle(this.entity.getXRot(), targetPitch, PITCH_RATE));
        this.entity.yBodyRot = this.entity.getYRot();
        this.entity.yHeadRot = this.entity.getYRot();

        Vec3 cur = this.entity.getDeltaMovement();
        double speed = Math.max(0.16D, this.baseSpeed * CHASE_FACTOR);

        if (this.shouldDoWaterBreach(target, dir)) {
            this.entity.setDeltaMovement(
                    Mth.lerp(0.45D, cur.x, dir.x * this.entity.getWaterBreachForwardBoost()),
                    Math.max(cur.y, this.entity.getWaterBreachVerticalBoost()),
                    Mth.lerp(0.45D, cur.z, dir.z * this.entity.getWaterBreachForwardBoost())
            );
        } else {
            double targetVX = dir.x * speed * 1.00D;
            double targetVY = dir.y * speed * 0.62D;
            double targetVZ = dir.z * speed * 1.00D;

            this.entity.setDeltaMovement(
                    Mth.lerp(CHASE_BLEND_H, cur.x, targetVX),
                    Mth.lerp(CHASE_BLEND_V, cur.y, targetVY),
                    Mth.lerp(CHASE_BLEND_H, cur.z, targetVZ)
            );
        }

        if (this.biteCooldown <= 0
                && this.entity.getAnimationTick() == 0
                && this.canMeleeHit(target)) {
            if (this.entity.doHurtTarget(target)) {
                this.biteCooldown = this.entity.getUnderwaterAttackIntervalTicks();
                this.entity.resetAttackCooldown();
            }
        }
    }

    private LivingEntity resolveCombatTarget() {
        LivingEntity raw = this.entity.getAttackTarget();

        if (this.isUsableCombatTarget(raw)) {
            this.lockedTarget = raw;
            this.lockedTargetTicks = TARGET_LOCK_TICKS;
            return raw;
        }

        if (raw != null && !this.isUsableCombatTarget(raw)) {
            this.clearCombatTarget(raw);
        }

        if (this.lockedTarget != null && this.lockedTargetTicks > 0) {
            this.lockedTargetTicks--;
            if (this.isUsableCombatTarget(this.lockedTarget)) {
                return this.lockedTarget;
            }
        }

        this.lockedTarget = null;
        this.lockedTargetTicks = 0;
        return null;
    }

    private void clearCombatTarget(LivingEntity target) {
        if (target != null && this.entity.getAttackTarget() == target) {
            this.entity.setTarget(null);
        }
        if (this.lockedTarget == target) {
            this.lockedTarget = null;
            this.lockedTargetTicks = 0;
        }
    }

    private boolean isUsableCombatTarget(LivingEntity target) {
        if (target == null || !target.isAlive() || target.isRemoved() || target.isSpectator()) {
            return false;
        }

        if (target instanceof Player player && (player.isCreative() || player.isSpectator())) {
            return false;
        }

        if (target.level() != this.entity.level()) {
            return false;
        }

        double maxChase = this.entity.getUnderwaterChaseRange();
        if (this.entity.distanceToSqr(target) > maxChase * maxChase) {
            return false;
        }

        if (target.isInWaterOrBubble() && !this.hasSwimCorridorTo(target.position())) {
            return false;
        }

        return true;
    }

    private boolean canMeleeHit(LivingEntity target) {
        double reach = 0.20D + this.entity.getBbWidth() * 1.25D + target.getBbWidth() * 0.70D;
        double reachSq = reach * reach;

        return this.entity.distanceToSqr(target) <= reachSq
                && Math.abs((target.getY() + target.getBbHeight() * 0.35D) - (this.entity.getY() + this.entity.getBbHeight() * 0.35D))
                <= (this.entity.getBbHeight() + 0.9D);
    }

    private boolean shouldDoWaterBreach(LivingEntity target, Vec3 dir) {
        if (!this.entity.canWaterBreachAttack()) {
            return false;
        }

        if (!this.isNearSurface()) {
            return false;
        }

        if (target == null || !target.isAlive()) {
            return false;
        }

        double horizontalDistSq = this.horizontalDistanceSqr(this.entity.position(), target.position());
        if (horizontalDistSq < 0.75D * 0.75D || horizontalDistSq > 3.0D * 3.0D) {
            return false;
        }

        if (target.getY() <= this.entity.getY()) {
            return false;
        }

        if (this.entity.getDeltaMovement().y > 0.12D) {
            return false;
        }

        return this.hasWaterLandingAhead(dir);
    }

    private boolean hasWaterLandingAhead(Vec3 dir) {
        double probe = this.entity.getWaterBreachProbeDistance();

        Vec3 landing = this.entity.position().add(dir.x * probe, 0.10D, dir.z * probe);

        AABB testBox = this.entity.getDimensions(this.entity.getPose())
                .makeBoundingBox(landing.x, landing.y, landing.z)
                .inflate(0.02D);

        if (!this.entity.level().noCollision(this.entity, testBox)) {
            return false;
        }

        return this.isWaterColumn(testBox);
    }

    private boolean isWaterColumn(AABB box) {
        double cx = (box.minX + box.maxX) * 0.5D;
        double cz = (box.minZ + box.maxZ) * 0.5D;

        BlockPos feet = BlockPos.containing(cx, box.minY + 0.05D, cz);
        BlockPos body = BlockPos.containing(cx, (box.minY + box.maxY) * 0.5D, cz);
        BlockPos head = BlockPos.containing(cx, box.maxY - 0.05D, cz);

        Level level = this.entity.level();
        return level.getFluidState(feet).is(FluidTags.WATER)
                && level.getFluidState(body).is(FluidTags.WATER)
                && level.getFluidState(head).is(FluidTags.WATER);
    }

    private Vec3 pickWaypoint() {
        Level level = this.entity.level();
        Vec3 origin = this.entity.position();

        for (int i = 0; i < 20; i++) {
            double dx = (this.entity.getRandom().nextDouble() * 2.0D - 1.0D) * PATROL_H;
            double dy = (this.entity.getRandom().nextDouble() * 2.0D - 1.0D) * PATROL_V;
            double dz = (this.entity.getRandom().nextDouble() * 2.0D - 1.0D) * PATROL_H;

            double tx = origin.x + dx;
            double ty = Mth.clamp(origin.y + dy, level.getMinBuildHeight() + 1.0D, level.getMaxBuildHeight() - 2.0D);
            double tz = origin.z + dz;

            Vec3 candidate = new Vec3(tx, ty, tz);
            if (this.isWaypointUsable(candidate)) {
                return candidate;
            }
        }

        return null;
    }

    private boolean isWaypointUsable(Vec3 target) {
        Level level = this.entity.level();
        BlockPos center = BlockPos.containing(target);

        if (!level.hasChunkAt(center)) {
            return false;
        }

        if (!level.getFluidState(center).is(FluidTags.WATER)) {
            return false;
        }

        AABB box = this.entity.getDimensions(this.entity.getPose())
                .makeBoundingBox(target.x, target.y, target.z)
                .inflate(0.05D);

        if (!level.noCollision(this.entity, box)) {
            return false;
        }

        BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
        int minY = Mth.floor(box.minY);
        int maxY = Mth.ceil(box.maxY);
        int tx = Mth.floor(target.x);
        int tz = Mth.floor(target.z);

        for (int y = minY; y <= maxY; y++) {
            cursor.set(tx, y, tz);
            if (!level.getFluidState(cursor).is(FluidTags.WATER)) {
                return false;
            }
        }

        return this.hasSwimCorridorTo(target);
    }

    private boolean hasSwimCorridorTo(Vec3 target) {
        Level level = this.entity.level;
        Vec3 start = this.entity.position();
        double dist = start.distanceTo(target);

        if (dist < 1.0E-4D) {
            return true;
        }

        int steps = Math.max(2, Mth.ceil(dist / PATH_PROBE_STEP));

        for (int i = 1; i <= steps; i++) {
            double t = i / (double) steps;
            double px = Mth.lerp(t, start.x, target.x);
            double py = Mth.lerp(t, start.y, target.y);
            double pz = Mth.lerp(t, start.z, target.z);

            BlockPos probePos = BlockPos.containing(px, py, pz);
            if (!level.hasChunkAt(probePos)) {
                return false;
            }

            AABB probeBox = this.entity.getDimensions(this.entity.getPose())
                    .makeBoundingBox(px, py, pz)
                    .inflate(0.02D);

            if (!level.noCollision(this.entity, probeBox)) {
                return false;
            }

            if (!this.isWaterColumn(probeBox)) {
                return false;
            }
        }

        return true;
    }

    private boolean isNearSurface() {
        Level level = this.entity.level();
        BlockPos.MutableBlockPos cursor = this.entity.blockPosition().mutable();

        for (int i = 1; i <= 2; i++) {
            cursor.move(0, 1, 0);
            if (!level.getFluidState(cursor).is(FluidTags.WATER)) {
                return true;
            }
        }

        return false;
    }

    private boolean isNearFloor() {
        if (this.entity.onGround()) {
            return true;
        }

        Level level = this.entity.level();
        BlockPos below = BlockPos.containing(
                this.entity.getX(),
                this.entity.getBoundingBox().minY - 0.15D,
                this.entity.getZ()
        );

        return !level.getFluidState(below).is(FluidTags.WATER);
    }

    private boolean arrivedAt(Vec3 target) {
        return this.entity.position().distanceToSqr(target) < ARRIVAL_SQ;
    }

    private void pickDriftDir() {
        double angle = this.entity.getRandom().nextDouble() * Math.PI * 2.0D;
        this.driftDir = new Vec3(Math.cos(angle), 0.0D, Math.sin(angle));
    }

    private double horizontalDistanceSqr(Vec3 a, Vec3 b) {
        double dx = a.x - b.x;
        double dz = a.z - b.z;
        return dx * dx + dz * dz;
    }

    public static float smoothAngle(float current, float target, float maxStep) {
        float delta = Mth.wrapDegrees(target - current);
        delta = Mth.clamp(delta, -maxStep, maxStep);
        return current + delta;
    }
}