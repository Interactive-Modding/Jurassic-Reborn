package net.vit.jurassicreborn.common.entities;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.Vec3;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimation;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;

/**
 * Crocodile-like dinosaur base.
 * Adds: death-roll w/ cooldown + DoT, ambush lunge in water, idle basking on land, and tail sweep on land.
 * FIX: During BASKING on land, entity is fully immobilized (no creeping).
 */
public abstract class CrocodileDinosaurEntity extends AmphibianDinosaurEntity {
    // --- Tunables ---
    private static final int DEATH_ROLL_COOLDOWN_T = 100; // ~5s
    private static final float DEATH_ROLL_DOT_DAMAGE = 2.0F; // per second while rolling
    private static final int BASKING_RAND = 500; // 1/500 chance per tick to start basking while idle
    private static final float AMBUSH_STEALTH_MULT = 0.6F; // slow approach before strike
    private static final float AMBUSH_LUNGE_MULT = 1.6F;   // burst when close
    private static final double AMBUSH_TRIGGER_DIST = 4.0D;
    private static final float TAIL_SWEEP_KNOCKBACK = 1.2F;
    private static final float TAIL_SWEEP_CHANCE = 0.25F;
    private static final int TAIL_SWEEP_COOLDOWN_T = 60; // ~3s

    // --- State ---
    private int deathRollCooldown = 0;
    private int tailSweepCooldown = 0;

    public CrocodileDinosaurEntity(Level world, EntityType<?> type, Dinosaur dino) {
        super(world, (EntityType) type, dino);
    }

    // --- Helpers ---
    /** True when we should be "pinned" while basking (server authoritative; client for pose only). */
    private boolean isBaskingNow() {
        return !this.level.isClientSide
                ? this.getAnimation() == EntityAnimation.BASKING.get()
                && !this.isInWater() && this.onGround
                && this.getTarget() == null
                : this.getAnimation() == EntityAnimation.BASKING.get();
    }

    // Cooldowns tick
    @Override
    public void tick() {
        super.tick();
        if (deathRollCooldown > 0) deathRollCooldown--;
        if (tailSweepCooldown > 0) tailSweepCooldown--;

        // Subtle land ambience while basking
        if (!this.level.isClientSide
                && this.getAnimation() == EntityAnimation.BASKING.get()
                && this.tickCount % 80 == 0) {
            this.playSound(SoundEvents.TURTLE_AMBIENT_LAND, 0.15F, 0.8F + this.random.nextFloat() * 0.4F);
        }
    }

    // Extra effects during active animations + basking trigger
    @Override
    public void aiStep() {
        super.aiStep();

        // --- Maintain/exit basking state & immobilize ---
        if (this.getAnimation() == EntityAnimation.BASKING.get()) {
            // Abort basking if situation changes
            if (this.getTarget() != null || this.isInWater() || !this.onGround || this.hurtTime > 0) {
                this.setAnimation(EntityAnimation.IDLE.get());
            }
        }

        if (isBaskingNow()) {
            // Stop navigation and player/AI inputs
            this.getNavigation().stop();
            this.getMoveControl().setWantedPosition(this.getX(), this.getY(), this.getZ(), 0.0D);
            this.setSpeed(0.0F);
            this.setZza(0.0F); // forward
            this.setXxa(0.0F); // strafe
            this.setYya(0.0F); // vertical input
            this.setSprinting(false);
            this.jumping = false;

            // Kill residual horizontal drift; keep gravity Y
            Vec3 v = this.getDeltaMovement();
            this.setDeltaMovement(v.x * 0.0D, v.y, v.z * 0.0D);
            this.hasImpulse = false;

            // Keep head still-ish
            this.getLookControl().setLookAt(this.getX(), this.getEyeY(), this.getZ());
        }

        // --- Death roll DoT while latched in water ---
        if (!this.level.isClientSide
                && this.isInWater()
                && this.getAnimation() == EntityAnimation.DEATH_ROLL.get()
                && this.getTarget() != null
                && this.getTarget().isAlive()) {
            if (this.tickCount % 20 == 0) {
                this.getTarget().hurt(this.damageSources().mobAttack(this), DEATH_ROLL_DOT_DAMAGE);
                Vec3 push = this.getLookAngle().scale(0.15D).add(0, 0.02D, 0);
                this.getTarget().setDeltaMovement(this.getTarget().getDeltaMovement().add(push));
            }
        }

        // --- Idle basking trigger on land, sunny, idle (server-only) ---
        if (!this.level.isClientSide
                && this.getTarget() == null
                && !this.isInWater() && this.onGround
                && this.getAnimation() == EntityAnimation.IDLE.get()) {

            boolean canSeeSky = this.level.canSeeSky(this.blockPosition());
            int sky = this.level.getBrightness(LightLayer.SKY, this.blockPosition());
            boolean bright = sky > 10;

            if (canSeeSky && bright && this.random.nextInt(BASKING_RAND) == 0) {
                this.setAnimation(EntityAnimation.BASKING.get()); // swap to RESTING if BASKING not present
                this.playSound(SoundEvents.TURTLE_LAY_EGG, 0.2F, 0.9F + this.random.nextFloat() * 0.2F);
            }
        }
    }

    // Ambush lunge behavior while swimming
    @Override
    public void travel(Vec3 input) {
        // Ignore movement entirely while basking on land
        if (isBaskingNow()) {
            // Absolute stillness except gravity: x/z zeroed above in aiStep; just don't call super
            return;
        }

        if (this.isInWater() && this.getTarget() != null) {
            double distance = this.distanceTo(this.getTarget());
            double base = this.getAttribute(Attributes.MOVEMENT_SPEED) != null
                    ? this.getAttribute(Attributes.MOVEMENT_SPEED).getValue() : 0.1D;

            if (distance < AMBUSH_TRIGGER_DIST) {
                this.setSpeed((float)(base * AMBUSH_LUNGE_MULT));
                if (this.level.isClientSide && this.tickCount % 10 == 0) {
                    this.playSound(SoundEvents.PLAYER_SWIM, 0.25F, 0.9F + this.random.nextFloat() * 0.2F);
                }
            } else {
                this.setSpeed((float)(base * AMBUSH_STEALTH_MULT));
            }
        }

        super.travel(input);
    }

    // Trigger animations on hits with cooldowns
    @Override
    public boolean doHurtTarget(Entity target) {
        boolean flag = super.doHurtTarget(target);
        if (!flag) return false;

        if (this.isInWater() && deathRollCooldown == 0) {
            this.setAnimation(EntityAnimation.DEATH_ROLL.get());
            deathRollCooldown = DEATH_ROLL_COOLDOWN_T;
            this.playSound(SoundEvents.DOLPHIN_ATTACK, 0.6F, 0.7F + this.random.nextFloat() * 0.4F);
        } else if (!this.isInWater() && tailSweepCooldown == 0 && this.random.nextFloat() < TAIL_SWEEP_CHANCE) {
            this.setAnimation(EntityAnimation.TAIL_SWEEP.get()); // map to an existing anim if needed
            tailSweepCooldown = TAIL_SWEEP_COOLDOWN_T;

            // Knockback using normalized push so it works on any Entity
            double dx = this.getX() - target.getX();
            double dz = this.getZ() - target.getZ();
            double n = Math.sqrt(dx * dx + dz * dz);
            if (n > 0.0001D) {
                dx /= n;
                dz /= n;
                target.push(dx * TAIL_SWEEP_KNOCKBACK, 0.1D, dz * TAIL_SWEEP_KNOCKBACK);
            }

            this.playSound(SoundEvents.WITHER_SKELETON_STEP, 0.4F, 0.8F + this.random.nextFloat() * 0.3F);
        }
        return true;
    }

    // Prevent external nudges while basking
    @Override
    public void push(double x, double y, double z) {
        if (isBaskingNow()) return;
        super.push(x, y, z);
    }

    @Override
    public boolean isPushable() {
        return !isBaskingNow() && super.isPushable();
    }
}
