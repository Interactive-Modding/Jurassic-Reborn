//package net.vit.jurassicreborn.common.entities.ai;
//
//import net.minecraft.core.BlockPos;
//import net.minecraft.util.Mth;
//import net.minecraft.world.entity.LivingEntity;
//import net.minecraft.world.entity.ai.goal.Goal;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.levelgen.Heightmap;
//import net.minecraft.world.phys.Vec3;
//import net.vit.jurassicreborn.common.entities.DinosaurEntity;
//import net.vit.jurassicreborn.common.entities.EntityUtils.FoodType;
//import net.vit.jurassicreborn.common.entities.FlyingDinosaurEntity;
//
//import java.util.Comparator;
//import java.util.EnumSet;
//import java.util.List;
//import java.util.UUID;
//
//public class FlyAI extends Goal {
//
//    public enum Phase {
//        ROOST, THERMAL, FLOCK_CRUISE, HUNT_SCAN, SWOOP, CARRY, WATER_DIVE, WATER_SWIM, ESCAPE
//    }
//
//    private static final double THERMAL_RADIUS = 14.0D;
//    private static final double THERMAL_ALTITUDE = 22.0D;
//    private static final float THERMAL_TURN_RATE = 0.032F;
//    private static final double CRUISE_ALTITUDE_MIN = 13.0D;
//    private static final double CRUISE_ALTITUDE_IDEAL = 24.0D;
//    private static final double ANCHOR_SPREAD = 20.0D;
//
//    private static final double FLOCK_RADIUS = 45.0D;
//    private static final double FLOCK_SEPARATION = 7.0D;
//    private static final double FLOCK_WEIGHT_SEP = 0.85D;
//    private static final double FLOCK_WEIGHT_ALIGN = 0.50D;
//    private static final double FLOCK_WEIGHT_COHESION = 0.30D;
//    private static final int FLOCK_CACHE_INTERVAL = 8;
//    private static final double HUNT_SCAN_RADIUS = 32.0D;
//    private static final double GRAB_MAX_WIDTH = 1.1D;
//    private static final double GRAB_MAX_HEIGHT = 2.1D;
//    private static final double EAT_MAX_WIDTH = 0.8D;
//    private static final double EAT_MAX_HEIGHT = 0.9D;
//    private static final double SWOOP_CONTACT_SQ = 2.5D * 2.5D;
//    private static final double SWOOP_ABORT_SQ = 55.0D * 55.0D;
//    private static final double DROP_ALTITUDE = 22.0D;
//    private static final int CARRY_MAX_TICKS = 100;
//
//    private static final double WATER_STRIKE_SCAN_RANGE = 18.0D;
//    private static final double MAX_WATER_STRIKE_DEPTH = 10.0D;
//    private static final double WATER_STRIKE_CONTACT_SQ = 2.8D * 2.8D;
//    private static final int MAX_WATER_ESCAPE_TICKS = 18;
//    private static final int WATER_STRIKE_COOLDOWN_TICKS = 80;
//
//    private static final double PLAYER_FEAR_RANGE = 24.0D;
//    private static final double ESCAPE_ALTITUDE = 30.0D;
//    private static final double ESCAPE_DONE_SQ = 12.0D * 12.0D;
//    private static final int ESCAPE_COOLDOWN_TICKS = 200;
//
//    private static final int ROOST_SCAN_H = 48;
//    private static final int MIN_ROOST_HEIGHT_GAIN = 8;
//    private static final double ROOST_ARRIVAL_SQ = 2.5D * 2.5D;
//    private static final int ROOST_IDLE_TICKS_MIN = 30;
//    private static final int ROOST_IDLE_TICKS_MAX = 120;
//
//    private static final float YAW_ROOST = 2.0F;
//    private static final float YAW_THERMAL = 1.5F;
//    private static final float YAW_FLOCK = 3.5F;
//    private static final float YAW_HUNT = 2.0F;
//    private static final float YAW_SWOOP = 7.0F;
//    private static final float YAW_ESCAPE = 8.0F;
//    private static final float PITCH_RATE = 2.5F;
//    private static final float MAX_PITCH_TILT = 35.0F;
//
//    private static final double BLEND_H = 0.055D;
//    private static final double BLEND_V = 0.045D;
//    private static final double BLEND_SWOOP = 0.10D;
//
//    private final FlyingDinosaurEntity entity;
//
//    public Phase phase = Phase.THERMAL;
//    private double baseSpeed = 0.0D;
//
//    private Vec3 thermalAnchor = null;
//    private float thermalAngle = 0.0F;
//    private int thermalTimer = 0;
//    private int roostIdleTarget = 0;
//
//    private Vec3 cachedFlockDir = null;
//    private int flockCacheAge = 0;
//
//    private LivingEntity preyTarget = null;
//    private int swoopTimer = 0;
//
//    private Vec3 waterDiveTarget = null;
//    private int waterEscapeTicks = 0;
//    private int waterStrikeCooldownTicks = 0;
//
//    private int carryTimer = 0;
//
//    private BlockPos roostPos = null;
//    private int roostIdleTimer = 0;
//    private int roostSearchCooldown = 0;
//
//    private Vec3 escapeTarget = null;
//    private int escapeCooldown = 0;
//
//    private Vec3 collisionRecoveryTarget = null;
//    private int collisionRecoveryTicks = 0;
//
//    private boolean alarmReceived = false;
//
//    public FlyAI(FlyingDinosaurEntity entity) {
//        this.entity = entity;
//        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
//    }
//
//    private double cruiseSpeed() {
//        return Math.max(0.09D, entity.getConfiguredMaxFlightSpeed() * 0.24D);
//    }
//
//    private double flockSpeed() {
//        return Math.max(0.10D, entity.getConfiguredMaxFlightSpeed() * 0.29D);
//    }
//
//    private double huntSpeed() {
//        return Math.max(0.11D, entity.getConfiguredMaxFlightSpeed() * 0.33D);
//    }
//
//    private double swoopSpeed() {
//        return Math.max(0.16D, entity.getConfiguredMaxFlightSpeed() * 0.48D);
//    }
//
//    private double carrySpeed() {
//        return Math.max(0.11D, entity.getConfiguredMaxFlightSpeed() * 0.27D);
//    }
//
//    private double escapeSpeed() {
//        return Math.max(0.18D, entity.getConfiguredMaxFlightSpeed() * 0.54D);
//    }
//
//    private double waterSpeed() {
//        return Math.max(0.08D, entity.getConfiguredMaxSwimmingSpeed() * 0.70D);
//    }
//
//    @Override
//    public boolean canUse() {
//        return entity.isAlive()
//                && !entity.isCarcass()
//                && !entity.isSleeping()
//                && !entity.tranqed
//                && entity.getOrder() != DinosaurEntity.Order.SIT
//                && !entity.isPassenger();
//    }
//
//    @Override
//    public boolean canContinueToUse() {
//        return this.canUse();
//    }
//
//    @Override
//    public void start() {
//        baseSpeed = entity.getConfiguredMaxFlightSpeed();
//        thermalAngle = entity.getRandom().nextFloat() * Mth.TWO_PI;
//        thermalAngle += (float) (entity.getId() % 20) * (Mth.TWO_PI / 20F);
//
//        thermalAnchor = null;
//        cachedFlockDir = null;
//        flockCacheAge = FLOCK_CACHE_INTERVAL;
//        preyTarget = null;
//        waterDiveTarget = null;
//        waterEscapeTicks = 0;
//        escapeTarget = null;
//        collisionRecoveryTarget = null;
//        collisionRecoveryTicks = 0;
//
//        if (entity.onGround() && !entity.isInWater()) {
//            phase = Phase.ROOST;
//            roostPos = entity.blockPosition();
//            roostIdleTimer = 0;
//            roostIdleTarget = ROOST_IDLE_TICKS_MIN
//                    + entity.getRandom().nextInt(ROOST_IDLE_TICKS_MAX - ROOST_IDLE_TICKS_MIN + 1);
//            entity.setFlying(false);
//            entity.setAquaticSwimming(false);
//        } else {
//            phase = Phase.THERMAL;
//            if (!entity.isFlightTakeoffBlocked()) {
//                entity.initiateFlying();
//            }
//        }
//    }
//
//    @Override
//    public void stop() {
//        if (entity.onGround() && !entity.isInWater()) {
//            entity.setFlying(false);
//        }
//        entity.setAquaticSwimming(false);
//        cachedFlockDir = null;
//        preyTarget = null;
//        waterDiveTarget = null;
//        waterEscapeTicks = 0;
//        escapeTarget = null;
//        roostPos = null;
//        collisionRecoveryTarget = null;
//        collisionRecoveryTicks = 0;
//    }
//
//    public Phase getPhase() {
//        return phase;
//    }
//
//    @Override
//    public void tick() {
//        if (entity.level().isClientSide) return;
//
//        if (entity.tickCount % 20 == 0) {
//            baseSpeed = entity.getConfiguredMaxFlightSpeed();
//        }
//
//        if (escapeCooldown > 0) {
//            escapeCooldown--;
//        }
//        if (waterStrikeCooldownTicks > 0) {
//            waterStrikeCooldownTicks--;
//        }
//
//        clearBadTargets();
//
//        if (collisionRecoveryTicks > 0
//                && collisionRecoveryTarget != null
//                && !entity.isInWater()
//                && !entity.isAquaticSwimming()) {
//            tickCollisionRecovery();
//            return;
//        }
//
//        if (entity.onGround() && entity.isFlightTakeoffBlocked()) {
//            phase = Phase.ROOST;
//            roostPos = entity.blockPosition();
//            roostIdleTimer = 0;
//            if (roostIdleTarget <= 0) {
//                roostIdleTarget = ROOST_IDLE_TICKS_MIN
//                        + entity.getRandom().nextInt(ROOST_IDLE_TICKS_MAX - ROOST_IDLE_TICKS_MIN + 1);
//            }
//            entity.setFlying(false);
//            entity.setAquaticSwimming(false);
//            preyTarget = null;
//            waterDiveTarget = null;
//            waterEscapeTicks = 0;
//            escapeTarget = null;
//            return;
//        }
//
//        updatePhase();
//        dispatchPhase();
//    }
//
//    private void clearBadTargets() {
//        LivingEntity current = entity.getTarget();
//        if (current instanceof DinosaurEntity d && d.isCarcass()) {
//            entity.setTarget(null);
//        }
//        if (preyTarget instanceof DinosaurEntity d && d.isCarcass()) {
//            preyTarget = null;
//        }
//    }
//
//    private void updatePhase() {
//        if (phase != Phase.ESCAPE && escapeCooldown <= 0) {
//            if (isPlayerTooClose() || alarmReceived) {
//                alarmReceived = false;
//                enterEscape();
//                return;
//            }
//        }
//
//        if (phase == Phase.ESCAPE) {
//            if (escapeTarget != null
//                    && entity.position().distanceToSqr(escapeTarget) < ESCAPE_DONE_SQ
//                    && !isPlayerTooClose()) {
//                escapeCooldown = ESCAPE_COOLDOWN_TICKS;
//                phase = Phase.THERMAL;
//                thermalAnchor = null;
//            }
//            return;
//        }
//
//        if (phase == Phase.WATER_SWIM) {
//            if (!entity.isInWater()) {
//                entity.setAquaticSwimming(false);
//                waterEscapeTicks = 0;
//                preyTarget = null;
//                waterStrikeCooldownTicks = WATER_STRIKE_COOLDOWN_TICKS;
//
//                if (!entity.isFlightTakeoffBlocked()) {
//                    entity.initiateFlying();
//                }
//
//                phase = Phase.THERMAL;
//                thermalAnchor = null;
//            }
//            return;
//        }
//
//        if (phase == Phase.WATER_DIVE) {
//            if (entity.isInWater()) {
//                entity.setFlying(false);
//                entity.setAquaticSwimming(true);
//                phase = Phase.WATER_SWIM;
//                waterEscapeTicks = 0;
//            }
//            return;
//        }
//
//        if (phase == Phase.CARRY) {
//            carryTimer++;
//            if (!entity.isVehicle() || carryTimer >= CARRY_MAX_TICKS || isAboveDropHeight()) {
//                entity.ejectPassengers();
//                carryTimer = 0;
//                preyTarget = null;
//                entity.setTarget(null);
//                phase = Phase.THERMAL;
//                thermalAnchor = null;
//            }
//            return;
//        }
//
//        if (phase == Phase.SWOOP) {
//            swoopTimer++;
//            if (preyTarget == null || !preyTarget.isAlive()) {
//                abortSwoop();
//                return;
//            }
//            if (entity.distanceToSqr(preyTarget) > SWOOP_ABORT_SQ) {
//                abortSwoop();
//                return;
//            }
//            return;
//        }
//
//        if (phase == Phase.ROOST) {
//            if (entity.onGround()) {
//                if (roostIdleTarget <= 0) {
//                    roostIdleTarget = ROOST_IDLE_TICKS_MIN
//                            + entity.getRandom().nextInt(ROOST_IDLE_TICKS_MAX - ROOST_IDLE_TICKS_MIN + 1);
//                }
//
//                roostIdleTimer++;
//                if (roostIdleTimer >= roostIdleTarget) {
//                    if (entity.isFlightTakeoffBlocked()) {
//                        roostIdleTimer = 0;
//                        roostIdleTarget = ROOST_IDLE_TICKS_MIN
//                                + entity.getRandom().nextInt(ROOST_IDLE_TICKS_MAX - ROOST_IDLE_TICKS_MIN + 1);
//                        entity.setFlying(false);
//                        return;
//                    }
//
//                    entity.initiateFlying();
//                    entity.setDeltaMovement(
//                            entity.getDeltaMovement().x,
//                            Math.max(entity.getDeltaMovement().y, 0.28D),
//                            entity.getDeltaMovement().z
//                    );
//                    entity.hasImpulse = true;
//
//                    phase = Phase.THERMAL;
//                    thermalAnchor = null;
//                    roostPos = null;
//                    roostIdleTimer = 0;
//                    roostIdleTarget = 0;
//                }
//            }
//            return;
//        }
//
//        boolean hasFlockmates = hasNearbyFlockmates();
//        if (hasFlockmates && phase != Phase.FLOCK_CRUISE) {
//            phase = Phase.FLOCK_CRUISE;
//            return;
//        }
//        if (phase == Phase.FLOCK_CRUISE && !hasFlockmates) {
//            phase = Phase.HUNT_SCAN;
//            return;
//        }
//
//        if (phase == Phase.HUNT_SCAN || phase == Phase.FLOCK_CRUISE) {
//            if (entity.tickCount % 20 == 0) {
//                LivingEntity candidate = findPreyTarget();
//                if (candidate != null) {
//                    preyTarget = candidate;
//                    swoopTimer = 0;
//                    phase = Phase.SWOOP;
//                    return;
//                }
//
//                if (waterStrikeCooldownTicks <= 0 && !entity.isInWater()) {
//                    LivingEntity waterTarget = findShallowAquaticPrey();
//                    if (waterTarget != null) {
//                        preyTarget = waterTarget;
//                        waterDiveTarget = waterTarget.position().add(
//                                0.0D,
//                                Math.min(0.6D, waterTarget.getBbHeight() * 0.35D),
//                                0.0D
//                        );
//                        waterEscapeTicks = 0;
//                        phase = Phase.WATER_DIVE;
//                        return;
//                    }
//                }
//            }
//        }
//
//        if (phase == Phase.THERMAL && entity.tickCount % 160 == 0
//                && entity.getRandom().nextInt(35) == 0) {
//            if (roostSearchCooldown <= 0) {
//                BlockPos rp = findRoostPosition();
//                if (rp != null) {
//                    roostPos = rp;
//                    phase = Phase.ROOST;
//                    roostIdleTimer = 0;
//                    roostIdleTarget = 0;
//                }
//                roostSearchCooldown = 200;
//            }
//        }
//
//        if (roostSearchCooldown > 0) {
//            roostSearchCooldown--;
//        }
//
//        thermalTimer++;
//        if (thermalTimer > 300) {
//            thermalTimer = 0;
//            if (phase == Phase.THERMAL) {
//                phase = Phase.HUNT_SCAN;
//            } else if (phase == Phase.HUNT_SCAN) {
//                phase = Phase.THERMAL;
//                thermalAnchor = null;
//            }
//        }
//    }
//
//    private boolean canHuntTarget(LivingEntity target, boolean aquatic) {
//        if (target == entity || !target.isAlive() || target.isSpectator()) return false;
//        if (target instanceof FlyingDinosaurEntity) return false;
//        if (!entity.canEatEntity(target)) return false;
//
//        UUID owner = entity.getOwner();
//        if (target instanceof Player player) {
//            if (player.getAbilities().instabuild) return false;
//            if (owner != null && owner.equals(player.getUUID())) return false;
//        }
//
//        if (target instanceof DinosaurEntity dino) {
//            if (dino.isCarcass()) return false;
//            if (entity.isEntityFreindly(dino)) return false;
//            if (entity.herd != null && dino.herd == entity.herd) return false;
//            if (entity.family != null && dino.family == entity.family) return false;
//        }
//
//        if (!entity.getAttackTargets().isEmpty()
//                && entity.getAttackTargets().stream().noneMatch(c -> c.isAssignableFrom(target.getClass()))) {
//            return false;
//        }
//
//        if (!aquatic) {
//            if (!entity.hasLineOfSight(target)) return false;
//            if (target.getY() >= entity.getY() - 2.0D) return false;
//        }
//
//        return true;
//    }
//
//    private void dispatchPhase() {
//        switch (phase) {
//            case ROOST -> tickRoost();
//            case THERMAL -> tickThermal();
//            case FLOCK_CRUISE -> tickFlockCruise();
//            case HUNT_SCAN -> tickHuntScan();
//            case SWOOP -> tickSwoop();
//            case CARRY -> tickCarry();
//            case WATER_DIVE -> tickWaterDive();
//            case WATER_SWIM -> tickWaterSwim();
//            case ESCAPE -> tickEscape();
//        }
//    }
//
//    private void tickRoost() {
//        if (roostPos == null) {
//            phase = Phase.THERMAL;
//            return;
//        }
//
//        Vec3 target = Vec3.atCenterOf(roostPos).add(0.0D, 0.1D, 0.0D);
//        double dist = entity.position().distanceToSqr(target);
//
//        if (dist < ROOST_ARRIVAL_SQ && entity.onGround()) {
//            entity.setFlying(false);
//            entity.setDeltaMovement(Vec3.ZERO);
//            return;
//        }
//
//        Vec3 dir = target.subtract(entity.position()).normalize();
//        float tYaw = yawTo(dir);
//        float tPitch = pitchTo(dir);
//
//        entity.setYRot(smoothAngle(entity.getYRot(), tYaw, YAW_ROOST));
//        entity.setXRot(smoothAngle(entity.getXRot(), tPitch, PITCH_RATE));
//        entity.yBodyRot = entity.getYRot();
//        entity.yHeadRot = entity.getYRot();
//
//        double speed = cruiseSpeed() * 0.65D;
//        Vec3 cur = entity.getDeltaMovement();
//        entity.setDeltaMovement(
//                Mth.lerp(BLEND_H, cur.x, dir.x * speed * 0.32D),
//                Mth.lerp(BLEND_V, cur.y, dir.y * speed * 0.22D),
//                Mth.lerp(BLEND_H, cur.z, dir.z * speed * 0.32D)
//        );
//    }
//
//    private void tickThermal() {
//        ensureThermalAnchor();
//        thermalAngle += THERMAL_TURN_RATE;
//
//        double tx = thermalAnchor.x + Math.cos(thermalAngle) * THERMAL_RADIUS;
//        double ty = thermalAnchor.y + Math.sin(thermalAngle * 0.5D) * 2.0D;
//        double tz = thermalAnchor.z + Math.sin(thermalAngle) * THERMAL_RADIUS;
//
//        double terrainY = getTerrainYBelow();
//        ty = Math.max(ty, terrainY + CRUISE_ALTITUDE_MIN + 4.0D);
//
//        steerToward(new Vec3(tx, ty, tz), cruiseSpeed(), YAW_THERMAL);
//    }
//
//    private void tickFlockCruise() {
//        ensureThermalAnchor();
//
//        if (cachedFlockDir == null || flockCacheAge >= FLOCK_CACHE_INTERVAL) {
//            cachedFlockDir = computeFlockVector();
//            flockCacheAge = 0;
//        }
//        flockCacheAge++;
//
//        Vec3 desiredDir = cachedFlockDir;
//        if (desiredDir.lengthSqr() < 1.0E-6D) {
//            tickThermal();
//            return;
//        }
//
//        double terrainY = getTerrainYBelow();
//        double altitudeTarget = terrainY + CRUISE_ALTITUDE_IDEAL;
//        double altCorrection = Mth.clamp((altitudeTarget - entity.getY()) * 0.04D, -0.15D, 0.15D);
//        desiredDir = new Vec3(desiredDir.x, desiredDir.y + altCorrection, desiredDir.z).normalize();
//
//        float tYaw = yawTo(desiredDir);
//        float tPitch = pitchTo(desiredDir);
//
//        entity.setYRot(smoothAngle(entity.getYRot(), tYaw, YAW_FLOCK));
//        entity.setXRot(smoothAngle(entity.getXRot(), tPitch, PITCH_RATE));
//        entity.yBodyRot = entity.getYRot();
//        entity.yHeadRot = entity.getYRot();
//
//        Vec3 cur = entity.getDeltaMovement();
//        double speed = flockSpeed();
//        entity.setDeltaMovement(
//                Mth.lerp(BLEND_H, cur.x, desiredDir.x * speed),
//                Mth.lerp(BLEND_V, cur.y, desiredDir.y * speed * 0.72D),
//                Mth.lerp(BLEND_H, cur.z, desiredDir.z * speed)
//        );
//    }
//
//    private void tickHuntScan() {
//        ensureThermalAnchor();
//        thermalAngle += THERMAL_TURN_RATE * 0.7F;
//
//        double tx = thermalAnchor.x + Math.cos(thermalAngle) * (THERMAL_RADIUS * 0.6D);
//        double ty = Math.max(thermalAnchor.y - 1.5D, getTerrainYBelow() + CRUISE_ALTITUDE_MIN + 2.0D);
//        double tz = thermalAnchor.z + Math.sin(thermalAngle) * (THERMAL_RADIUS * 0.6D);
//
//        steerToward(new Vec3(tx, ty, tz), huntSpeed(), YAW_HUNT);
//        entity.setXRot(smoothAngle(entity.getXRot(), 8.0F, PITCH_RATE * 0.5F));
//    }
//
//    private void tickSwoop() {
//        if (preyTarget == null || !preyTarget.isAlive()) {
//            abortSwoop();
//            return;
//        }
//
//        double distToPrey = entity.distanceTo(preyTarget);
//        double approachLift = distToPrey > 6.0D ? 1.1D : 0.15D;
//
//        Vec3 myPos = entity.position();
//        Vec3 preyPos = preyTarget.position().add(0.0D, preyTarget.getBbHeight() * 0.65D + approachLift, 0.0D);
//        Vec3 toPreyVec = preyPos.subtract(myPos);
//        double distSq = toPreyVec.lengthSqr();
//        Vec3 dir = toPreyVec.normalize();
//
//        float tYaw = yawTo(dir);
//        float tPitch = Mth.clamp(pitchTo(dir), -MAX_PITCH_TILT, MAX_PITCH_TILT);
//
//        entity.setYRot(smoothAngle(entity.getYRot(), tYaw, YAW_SWOOP));
//        entity.setXRot(smoothAngle(entity.getXRot(), tPitch, PITCH_RATE * 1.5F));
//        entity.yBodyRot = entity.getYRot();
//        entity.yHeadRot = entity.getYRot();
//
//        Vec3 cur = entity.getDeltaMovement();
//        double speed = swoopSpeed();
//        entity.setDeltaMovement(
//                Mth.lerp(BLEND_SWOOP, cur.x, dir.x * speed),
//                Mth.lerp(BLEND_SWOOP, cur.y, dir.y * speed * 0.96D),
//                Mth.lerp(BLEND_SWOOP, cur.z, dir.z * speed)
//        );
//
//        if (distSq < SWOOP_CONTACT_SQ) {
//            onSwoopContact();
//        }
//    }
//
//    private void onSwoopContact() {
//        if (preyTarget == null) return;
//
//        boolean isTinyPrey = preyTarget.getBbWidth() <= EAT_MAX_WIDTH
//                && preyTarget.getBbHeight() <= EAT_MAX_HEIGHT
//                && !(preyTarget instanceof Player);
//
//        if (isTinyPrey) {
//            preyTarget.hurt(entity.damageSources().mobAttack(entity), preyTarget.getMaxHealth() * 2.0F);
//            entity.heal(Math.min(preyTarget.getMaxHealth(), 8.0F));
//            entity.setTarget(null);
//            preyTarget = null;
//            thermalAnchor = null;
//            phase = Phase.THERMAL;
//            pullUpAfterStrike();
//        } else if (entity.tryGrab(preyTarget)) {
//            carryTimer = 0;
//            entity.setTarget(null);
//            phase = Phase.CARRY;
//            pullUpAfterStrike();
//        } else {
//            preyTarget.hurt(entity.damageSources().mobAttack(entity), (float) (baseSpeed * 40.0D));
//            entity.setTarget(null);
//            abortSwoop();
//            pullUpAfterStrike();
//        }
//    }
//
//    private void abortSwoop() {
//        preyTarget = null;
//        entity.setTarget(null);
//        phase = Phase.THERMAL;
//        thermalAnchor = null;
//        swoopTimer = 0;
//    }
//
//    private void tickCarry() {
//        if (!entity.isVehicle()) {
//            phase = Phase.THERMAL;
//            thermalAnchor = null;
//            return;
//        }
//
//        double terrainY = getTerrainYBelow();
//        double targetY = terrainY + DROP_ALTITUDE;
//
//        Vec3 cur = entity.getDeltaMovement();
//        Vec3 myPos = entity.position();
//        double yDiff = targetY - myPos.y;
//
//        float tPitch = Mth.clamp((float) (-yDiff * 2.0D), -MAX_PITCH_TILT, MAX_PITCH_TILT);
//        entity.setXRot(smoothAngle(entity.getXRot(), tPitch, PITCH_RATE));
//
//        double speed = carrySpeed();
//        double dir = Math.toRadians(entity.getYRot());
//
//        entity.setDeltaMovement(
//                Mth.lerp(BLEND_H, cur.x, -Math.sin(dir) * speed),
//                Mth.lerp(BLEND_V, cur.y, yDiff > 0.5D ? speed * 0.55D : -speed * 0.18D),
//                Mth.lerp(BLEND_H, cur.z, Math.cos(dir) * speed)
//        );
//    }
//
//    private void tickWaterDive() {
//        if (preyTarget == null || !preyTarget.isAlive() || !preyTarget.isInWaterOrBubble() || !isShallowEnoughForStrike(preyTarget)) {
//            abortWaterStrike();
//            return;
//        }
//
//        Vec3 target = preyTarget.position().add(
//                0.0D,
//                Math.min(0.6D, preyTarget.getBbHeight() * 0.35D),
//                0.0D
//        );
//
//        Vec3 dir = target.subtract(entity.position());
//        if (dir.lengthSqr() < 1.0E-6D) {
//            abortWaterStrike();
//            return;
//        }
//
//        dir = dir.normalize();
//
//        float tYaw = yawTo(dir);
//        float tPitch = Mth.clamp(pitchTo(dir), -MAX_PITCH_TILT, 10.0F);
//
//        entity.setYRot(smoothAngle(entity.getYRot(), tYaw, YAW_SWOOP));
//        entity.setXRot(smoothAngle(entity.getXRot(), tPitch, PITCH_RATE * 1.5F));
//        entity.yBodyRot = entity.getYRot();
//        entity.yHeadRot = entity.getYRot();
//
//        Vec3 cur = entity.getDeltaMovement();
//        double speed = swoopSpeed();
//        entity.setDeltaMovement(
//                Mth.lerp(BLEND_SWOOP, cur.x, dir.x * speed),
//                Mth.lerp(BLEND_SWOOP, cur.y, dir.y * speed),
//                Mth.lerp(BLEND_SWOOP, cur.z, dir.z * speed)
//        );
//
//        if (entity.distanceToSqr(preyTarget) <= WATER_STRIKE_CONTACT_SQ) {
//            performWaterStrike();
//            return;
//        }
//
//        if (entity.isInWater()) {
//            entity.setFlying(false);
//            entity.setAquaticSwimming(true);
//            waterEscapeTicks = 0;
//            phase = Phase.WATER_SWIM;
//        }
//    }
//
//    private void tickWaterSwim() {
//        waterEscapeTicks++;
//
//        if (entity.getAirSupply() < 120) {
//            preyTarget = null;
//        }
//
//        if (preyTarget != null
//                && preyTarget.isAlive()
//                && preyTarget.isInWaterOrBubble()
//                && isShallowEnoughForStrike(preyTarget)
//                && entity.distanceToSqr(preyTarget) <= WATER_STRIKE_CONTACT_SQ
//                && waterEscapeTicks <= 4) {
//            performWaterStrike();
//            return;
//        }
//
//        int x = Mth.floor(entity.getX());
//        int z = Mth.floor(entity.getZ());
//
//        double surfaceY = entity.level().hasChunk(x >> 4, z >> 4)
//                ? entity.level().getHeight(Heightmap.Types.WORLD_SURFACE, x, z)
//                : entity.getY() + 4.0D;
//
//        Vec3 escape = new Vec3(entity.getX(), surfaceY + 1.2D, entity.getZ());
//        Vec3 toTarget = escape.subtract(entity.position());
//
//        if (toTarget.lengthSqr() > 1.0E-6D) {
//            Vec3 dir = toTarget.normalize();
//
//            float tYaw = yawTo(dir);
//            float tPitch = Mth.clamp(pitchTo(dir), -MAX_PITCH_TILT, MAX_PITCH_TILT);
//
//            entity.setYRot(smoothAngle(entity.getYRot(), tYaw, YAW_ESCAPE));
//            entity.setXRot(smoothAngle(entity.getXRot(), tPitch, PITCH_RATE));
//            entity.yBodyRot = entity.getYRot();
//            entity.yHeadRot = entity.getYRot();
//
//            Vec3 cur = entity.getDeltaMovement();
//            entity.setDeltaMovement(
//                    Mth.lerp(0.18D, cur.x, dir.x * waterSpeed() * 0.45D),
//                    Mth.lerp(0.40D, cur.y, Math.max(dir.y * waterSpeed() * 2.1D, 0.28D)),
//                    Mth.lerp(0.18D, cur.z, dir.z * waterSpeed() * 0.45D)
//            );
//        }
//
//        if (waterEscapeTicks >= MAX_WATER_ESCAPE_TICKS) {
//            preyTarget = null;
//        }
//    }
//
//    private void performWaterStrike() {
//        if (preyTarget == null) {
//            abortWaterStrike();
//            return;
//        }
//
//        boolean tinyPrey = preyTarget.getBbWidth() <= EAT_MAX_WIDTH
//                && preyTarget.getBbHeight() <= EAT_MAX_HEIGHT
//                && !(preyTarget instanceof Player);
//
//        if (tinyPrey) {
//            preyTarget.hurt(entity.damageSources().mobAttack(entity), preyTarget.getMaxHealth() * 2.0F);
//            entity.heal(Math.min(preyTarget.getMaxHealth(), 8.0F));
//        } else if (!entity.tryGrab(preyTarget)) {
//            preyTarget.hurt(entity.damageSources().mobAttack(entity), (float) (baseSpeed * 40.0D));
//        }
//
//        entity.setTarget(null);
//        preyTarget = null;
//        waterDiveTarget = null;
//        waterEscapeTicks = 0;
//        waterStrikeCooldownTicks = WATER_STRIKE_COOLDOWN_TICKS;
//
//        if (entity.isInWater()) {
//            entity.setAquaticSwimming(true);
//            phase = Phase.WATER_SWIM;
//        } else {
//            entity.setAquaticSwimming(false);
//            if (!entity.isFlightTakeoffBlocked()) {
//                entity.initiateFlying();
//            }
//            phase = Phase.THERMAL;
//            thermalAnchor = null;
//        }
//    }
//
//    private void abortWaterStrike() {
//        entity.setTarget(null);
//        preyTarget = null;
//        waterDiveTarget = null;
//        waterEscapeTicks = 0;
//        waterStrikeCooldownTicks = WATER_STRIKE_COOLDOWN_TICKS;
//
//        if (entity.isInWater()) {
//            entity.setFlying(false);
//            entity.setAquaticSwimming(true);
//            phase = Phase.WATER_SWIM;
//        } else {
//            entity.setAquaticSwimming(false);
//            if (!entity.isFlightTakeoffBlocked()) {
//                entity.initiateFlying();
//            }
//            phase = Phase.THERMAL;
//            thermalAnchor = null;
//        }
//    }
//
//    private void tickEscape() {
//        if (escapeTarget == null) {
//            enterEscape();
//            return;
//        }
//
//        Vec3 toTarget = escapeTarget.subtract(entity.position());
//        double dist = toTarget.length();
//        if (dist < 0.5D) return;
//
//        Vec3 dir = toTarget.scale(1.0D / dist);
//        float tYaw = yawTo(dir);
//        float tPitch = Mth.clamp(pitchTo(dir), -MAX_PITCH_TILT, 5.0F);
//
//        entity.setYRot(smoothAngle(entity.getYRot(), tYaw, YAW_ESCAPE));
//        entity.setXRot(smoothAngle(entity.getXRot(), tPitch, PITCH_RATE * 2.0F));
//        entity.yBodyRot = entity.getYRot();
//        entity.yHeadRot = entity.getYRot();
//
//        Vec3 cur = entity.getDeltaMovement();
//        double speed = escapeSpeed();
//        entity.setDeltaMovement(
//                Mth.lerp(0.15D, cur.x, dir.x * speed),
//                Mth.lerp(0.12D, cur.y, dir.y * speed * 0.90D),
//                Mth.lerp(0.15D, cur.z, dir.z * speed)
//        );
//    }
//
//    private void enterEscape() {
//        collisionRecoveryTarget = null;
//        collisionRecoveryTicks = 0;
//
//        if (entity.onGround() && entity.isFlightTakeoffBlocked()) {
//            phase = Phase.ROOST;
//            roostPos = entity.blockPosition();
//            roostIdleTimer = 0;
//            roostIdleTarget = ROOST_IDLE_TICKS_MIN
//                    + entity.getRandom().nextInt(ROOST_IDLE_TICKS_MAX - ROOST_IDLE_TICKS_MIN + 1);
//            entity.setFlying(false);
//            entity.setAquaticSwimming(false);
//            preyTarget = null;
//            waterDiveTarget = null;
//            waterEscapeTicks = 0;
//            escapeTarget = null;
//            return;
//        }
//
//        phase = Phase.ESCAPE;
//        escapeTarget = computeEscapeTarget();
//        preyTarget = null;
//        waterDiveTarget = null;
//        waterEscapeTicks = 0;
//        entity.initiateFlying();
//        entity.ejectPassengers();
//
//        entity.level().getEntitiesOfClass(
//                entity.getClass(),
//                entity.getBoundingBox().inflate(FLOCK_RADIUS * 0.6D),
//                e -> e != entity
//        ).forEach(e -> ((FlyingDinosaurEntity) e).receiveFlockAlarm());
//    }
//
//    private Vec3 computeEscapeTarget() {
//        Player nearest = entity.level().getNearestPlayer(entity, PLAYER_FEAR_RANGE + 10.0D);
//        Vec3 away = nearest != null
//                ? entity.position().subtract(nearest.position()).normalize()
//                : new Vec3(
//                entity.getRandom().nextDouble() * 2.0D - 1.0D,
//                0.0D,
//                entity.getRandom().nextDouble() * 2.0D - 1.0D
//        ).normalize();
//
//        double terrainY = getTerrainYBelow();
//        return entity.position()
//                .add(away.scale(50.0D))
//                .add(0.0D, Math.max(ESCAPE_ALTITUDE, terrainY + ESCAPE_ALTITUDE - entity.getY()), 0.0D);
//    }
//
//    private Vec3 computeFlockVector() {
//        List<FlyingDinosaurEntity> nearby = entity.level().getEntitiesOfClass(
//                entity.getClass().asSubclass(FlyingDinosaurEntity.class),
//                entity.getBoundingBox().inflate(FLOCK_RADIUS),
//                e -> e != entity && e.isAlive()
//        );
//
//        if (nearby.isEmpty()) return Vec3.ZERO;
//
//        Vec3 sep = Vec3.ZERO;
//        Vec3 align = Vec3.ZERO;
//        Vec3 cohesion = Vec3.ZERO;
//        int sepCount = 0;
//
//        for (FlyingDinosaurEntity other : nearby) {
//            double d = entity.distanceTo(other);
//            if (d < FLOCK_SEPARATION) {
//                Vec3 away = entity.position().subtract(other.position());
//                if (away.lengthSqr() > 1.0E-10D) {
//                    sep = sep.add(away.normalize().scale(FLOCK_SEPARATION / Math.max(d, 0.1D)));
//                }
//                sepCount++;
//            }
//            align = align.add(other.getDeltaMovement());
//            cohesion = cohesion.add(other.position());
//        }
//
//        Vec3 result = Vec3.ZERO;
//        int n = nearby.size();
//
//        if (sepCount > 0 && sep.lengthSqr() > 1.0E-10D) {
//            result = result.add(sep.normalize().scale(FLOCK_WEIGHT_SEP));
//        }
//
//        Vec3 alignNorm = align.scale(1.0D / n);
//        if (alignNorm.lengthSqr() > 1.0E-10D) {
//            result = result.add(alignNorm.normalize().scale(FLOCK_WEIGHT_ALIGN));
//        }
//
//        Vec3 center = cohesion.scale(1.0D / n);
//        Vec3 toCohesion = center.subtract(entity.position());
//        if (toCohesion.lengthSqr() > 1.0E-10D) {
//            result = result.add(toCohesion.normalize().scale(FLOCK_WEIGHT_COHESION));
//        }
//
//        float phaseOffset = (entity.getId() % 12) * (Mth.TWO_PI / 12.0F);
//        double sideBlend = Math.sin(entity.tickCount * 0.015D + phaseOffset) * 0.18D;
//        result = result.add(new Vec3(
//                Math.cos(phaseOffset) * sideBlend,
//                0.0D,
//                Math.sin(phaseOffset) * sideBlend
//        ));
//
//        return result.lengthSqr() > 1.0E-10D ? result.normalize() : Vec3.ZERO;
//    }
//
//    private LivingEntity findPreyTarget() {
//        if (!entity.getDinosaur().getDiet().canEat(entity, FoodType.MEAT)) {
//            return null;
//        }
//
//        return entity.level().getEntitiesOfClass(
//                LivingEntity.class,
//                entity.getBoundingBox().inflate(HUNT_SCAN_RADIUS),
//                e -> canHuntTarget(e, false)
//                        && e.getBbWidth() <= GRAB_MAX_WIDTH
//                        && e.getBbHeight() <= GRAB_MAX_HEIGHT
//        ).stream().min(Comparator.comparingDouble(entity::distanceToSqr)).orElse(null);
//    }
//
//    private LivingEntity findShallowAquaticPrey() {
//        boolean canEatFish = entity.getDinosaur().getDiet().canEat(entity, FoodType.FISH)
//                || entity.getDinosaur().getDiet().canEat(entity, FoodType.MEAT);
//
//        if (!canEatFish) {
//            return null;
//        }
//
//        return entity.level().getEntitiesOfClass(
//                LivingEntity.class,
//                entity.getBoundingBox().inflate(WATER_STRIKE_SCAN_RANGE),
//                e -> canHuntTarget(e, true)
//                        && e.isInWaterOrBubble()
//                        && e.getBbWidth() <= GRAB_MAX_WIDTH
//                        && e.getBbHeight() <= GRAB_MAX_HEIGHT
//                        && isShallowEnoughForStrike(e)
//        ).stream().min(Comparator.comparingDouble(entity::distanceToSqr)).orElse(null);
//    }
//
//    private boolean isShallowEnoughForStrike(LivingEntity target) {
//        int x = Mth.floor(target.getX());
//        int z = Mth.floor(target.getZ());
//
//        if (!entity.level().hasChunk(x >> 4, z >> 4)) {
//            return false;
//        }
//
//        double surfaceY = entity.level().getHeight(Heightmap.Types.WORLD_SURFACE, x, z);
//        double depth = surfaceY - target.getY();
//
//        return depth >= 0.0D && depth <= MAX_WATER_STRIKE_DEPTH;
//    }
//
//    private BlockPos findRoostPosition() {
//        Level level = entity.level();
//        BlockPos origin = entity.blockPosition();
//        int bestY = Integer.MIN_VALUE;
//        BlockPos best = null;
//
//        for (int dx = -ROOST_SCAN_H; dx <= ROOST_SCAN_H; dx += 6) {
//            for (int dz = -ROOST_SCAN_H; dz <= ROOST_SCAN_H; dz += 6) {
//                int x = origin.getX() + dx;
//                int z = origin.getZ() + dz;
//                if (!level.hasChunk(x >> 4, z >> 4)) continue;
//
//                int y = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z);
//                if (y > bestY && y > origin.getY() + MIN_ROOST_HEIGHT_GAIN) {
//                    BlockPos candidate = new BlockPos(x, y, z);
//                    if (level.getBlockState(candidate.below()).isSolid()
//                            && level.getBlockState(candidate).isAir()) {
//                        bestY = y;
//                        best = candidate;
//                    }
//                }
//            }
//        }
//        return best;
//    }
//
//    private boolean isPlayerTooClose() {
//        if (escapeCooldown > 0) return false;
//        return !entity.level().getEntitiesOfClass(
//                Player.class,
//                entity.getBoundingBox().inflate(PLAYER_FEAR_RANGE),
//                p -> !p.isSpectator() && !p.getAbilities().instabuild
//        ).isEmpty();
//    }
//
//    private boolean hasNearbyFlockmates() {
//        return !entity.level().getEntitiesOfClass(
//                entity.getClass(),
//                entity.getBoundingBox().inflate(FLOCK_RADIUS),
//                e -> e != entity && e.isAlive() && ((FlyingDinosaurEntity) e).isFlying()
//        ).isEmpty();
//    }
//
//    private boolean isAboveDropHeight() {
//        return entity.getY() >= getTerrainYBelow() + DROP_ALTITUDE - 1.0D;
//    }
//
//    private double getTerrainYBelow() {
//        Level level = entity.level();
//        int x = (int) entity.getX();
//        int z = (int) entity.getZ();
//        if (!level.hasChunk(x >> 4, z >> 4)) return entity.getY() - CRUISE_ALTITUDE_IDEAL;
//        return level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z);
//    }
//
//    private void ensureThermalAnchor() {
//        if (thermalAnchor == null) {
//            double terrainY = getTerrainYBelow();
//            double offsetX = (entity.getRandom().nextDouble() * 2.0D - 1.0D) * ANCHOR_SPREAD;
//            double offsetZ = (entity.getRandom().nextDouble() * 2.0D - 1.0D) * ANCHOR_SPREAD;
//            thermalAnchor = new Vec3(
//                    entity.getX() + offsetX,
//                    Math.max(entity.getY(), terrainY + THERMAL_ALTITUDE),
//                    entity.getZ() + offsetZ
//            );
//        }
//    }
//
//    public void receiveAlarm() {
//        if (escapeCooldown <= 0) {
//            alarmReceived = true;
//        }
//    }
//
//    public void onFlightCollision(boolean horizontal, boolean vertical, boolean takeoff) {
//        if (entity.isInWater() || entity.isAquaticSwimming()) {
//            return;
//        }
//
//        Vec3 dm = entity.getDeltaMovement();
//        Vec3 away = new Vec3(
//                horizontal ? -dm.x : 0.0D,
//                vertical ? 0.75D : 0.35D,
//                horizontal ? -dm.z : 0.0D
//        );
//
//        if (away.lengthSqr() < 1.0E-6D) {
//            float yaw = (entity.getYRot() + 90.0F + entity.getRandom().nextInt(180)) * ((float) Math.PI / 180.0F);
//            away = new Vec3(-Mth.sin(yaw), 0.45D, Mth.cos(yaw));
//        }
//
//        away = away.normalize();
//
//        double terrainY = getTerrainYBelow();
//        double targetY = Math.max(
//                entity.getY() + (takeoff ? 6.0D : 3.5D),
//                terrainY + CRUISE_ALTITUDE_MIN + 2.0D
//        );
//
//        Vec3 lateral = entity.position().add(away.scale(takeoff ? 12.0D : 8.0D));
//        this.collisionRecoveryTarget = new Vec3(lateral.x, targetY, lateral.z);
//        this.collisionRecoveryTicks = takeoff ? 18 : 10;
//
//        if (phase == Phase.ROOST) {
//            phase = Phase.THERMAL;
//            roostPos = null;
//            roostIdleTimer = 0;
//            roostIdleTarget = 0;
//        }
//
//        if (!entity.isFlightTakeoffBlocked()) {
//            entity.setFlying(true);
//            entity.setAquaticSwimming(false);
//        }
//    }
//
//    private void tickCollisionRecovery() {
//        collisionRecoveryTicks--;
//
//        entity.setFlying(true);
//        entity.setAquaticSwimming(false);
//
//        steerToward(
//                collisionRecoveryTarget,
//                Math.max(cruiseSpeed(), entity.getConfiguredMaxFlightSpeed() * 0.30D),
//                YAW_ESCAPE
//        );
//
//        if (entity.position().distanceToSqr(collisionRecoveryTarget) < 4.0D || collisionRecoveryTicks <= 0) {
//            collisionRecoveryTarget = null;
//            collisionRecoveryTicks = 0;
//            thermalAnchor = null;
//
//            if (phase != Phase.ESCAPE
//                    && phase != Phase.SWOOP
//                    && phase != Phase.CARRY
//                    && phase != Phase.WATER_DIVE
//                    && phase != Phase.WATER_SWIM) {
//                phase = Phase.THERMAL;
//            }
//        }
//    }
//
//    private void pullUpAfterStrike() {
//        Vec3 dm = entity.getDeltaMovement();
//        entity.setDeltaMovement(dm.x, Math.max(dm.y, 0.22D), dm.z);
//        entity.hasImpulse = true;
//    }
//
//    private void steerToward(Vec3 target, double speed, float yawRate) {
//        Vec3 toTarget = target.subtract(entity.position());
//        double dist = toTarget.length();
//        if (dist < 0.5D) return;
//
//        Vec3 dir = toTarget.scale(1.0D / dist);
//        float tYaw = yawTo(dir);
//        float tPitch = Mth.clamp(pitchTo(dir), -MAX_PITCH_TILT, MAX_PITCH_TILT);
//
//        entity.setYRot(smoothAngle(entity.getYRot(), tYaw, yawRate));
//        entity.setXRot(smoothAngle(entity.getXRot(), tPitch, PITCH_RATE));
//        entity.yBodyRot = entity.getYRot();
//        entity.yHeadRot = entity.getYRot();
//
//        Vec3 cur = entity.getDeltaMovement();
//        double yDiff = target.y - entity.getY();
//
//        double verticalScale = yDiff > 1.0D ? 1.20D : (yDiff < -1.0D ? 0.55D : 0.85D);
//        double climbBias = yDiff > 2.0D ? Math.min(0.10D, yDiff * 0.02D) : 0.0D;
//        double verticalBlend = yDiff > 0.0D ? 0.09D : BLEND_V;
//
//        entity.setDeltaMovement(
//                Mth.lerp(BLEND_H, cur.x, dir.x * speed),
//                Mth.lerp(verticalBlend, cur.y, dir.y * speed * verticalScale + climbBias),
//                Mth.lerp(BLEND_H, cur.z, dir.z * speed)
//        );
//    }
//    private static float yawTo(Vec3 dir) {
//        return (float) Math.toDegrees(Math.atan2(-dir.x, dir.z));
//    }
//
//    private static float pitchTo(Vec3 dir) {
//        double h = Math.sqrt(dir.x * dir.x + dir.z * dir.z);
//        return h < 1.0E-5D ? 0.0F : (float) Math.toDegrees(Math.atan2(-dir.y, h));
//    }
//
//    public static float smoothAngle(float current, float target, float maxStep) {
//        float delta = Mth.wrapDegrees(target - current);
//        delta = Mth.clamp(delta, -maxStep, maxStep);
//        return current + delta;
//    }
//}