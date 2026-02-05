package net.vit.jurassicreborn.common.entities.ai;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.util.Mth;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimation;
import net.vit.jurassicreborn.client.sounds.SoundHandler;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.DilophosaurusEntity;
import net.vit.jurassicreborn.common.entities.DinosaurEntity;

import java.util.EnumSet;

public class DilophosaurusSpitGoal extends Goal {

    private final DilophosaurusEntity dilo;
    private LivingEntity target;

    private final double speed;
    private final int attackInterval;
    private final int maxRangedAttackTime;
    private final float attackRadius;
    private final float maxAttackDistanceSq;

    private int rangedAttackTime = -1;
    private int seeTime = 0;
    private int animationTimer = -1;
    private int stuckCheckTime = 0;
    private double lastStuckCheckX;
    private double lastStuckCheckY;
    private double lastStuckCheckZ;

    public DilophosaurusSpitGoal(DilophosaurusEntity dilo, double speed, int attackInterval, int maxAttackTime, float maxAttackDistance) {
        this.dilo = dilo;
        this.speed = speed;
        this.attackInterval = attackInterval;
        this.maxRangedAttackTime = maxAttackTime;
        this.attackRadius = maxAttackDistance;
        this.maxAttackDistanceSq = maxAttackDistance * maxAttackDistance;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    public DilophosaurusSpitGoal(DilophosaurusEntity dilo, double speed, int maxAttackTime, float maxAttackDistance) {
        this(dilo, speed, maxAttackTime, maxAttackTime, maxAttackDistance);
    }

    @Override
    public boolean canUse() {
        LivingEntity candidate = this.dilo.getTarget();
        if (candidate == null) return false;
        if (!candidate.isAlive()) return false;
        if (candidate instanceof DinosaurEntity dino && dino.isCarcass()) return false;
        if (candidate.getHealth() < candidate.getMaxHealth() * 0.9F && candidate.hasEffect(MobEffects.BLINDNESS)) return false;
        if (this.dilo.isInWater()) return false;
        this.target = candidate;
        return true;
    }

    @Override
    public boolean canContinueToUse() {
        if (this.target == null || !this.target.isAlive()) return false;
        return !this.dilo.getNavigation().isDone()
                || this.dilo.distanceToSqr(this.target) <= (double) this.maxAttackDistanceSq
                || this.animationTimer > 0;
    }

    @Override
    public void start() {
        this.rangedAttackTime = -1;
        this.seeTime = 0;
        this.animationTimer = -1;
        this.stuckCheckTime = 0;
        this.lastStuckCheckX = this.dilo.getX();
        this.lastStuckCheckY = this.dilo.getY();
        this.lastStuckCheckZ = this.dilo.getZ();
    }

    @Override
    public void stop() {
        this.target = null;
        this.seeTime = 0;
        this.rangedAttackTime = -1;
        this.animationTimer = -1;
        this.stuckCheckTime = 0;
    }

    @Override
    public void tick() {
        if (this.target == null) return;

        double distSq = this.dilo.distanceToSqr(this.target.getX(), this.target.getY(), this.target.getZ());
        boolean canSee = this.dilo.getSensing().hasLineOfSight(this.target);
        if (canSee) ++this.seeTime; else this.seeTime = 0;

        double dx = this.target.getX() - this.dilo.getX();
        double dz = this.target.getZ() - this.dilo.getZ();
        double horizDistSq = dx * dx + dz * dz;
        double verticalGap = Math.abs(this.target.getY() - this.dilo.getY());

        boolean shouldHoldPosition = distSq <= this.maxAttackDistanceSq
                && this.seeTime >= 10
                && horizDistSq <= (double) (this.attackRadius * this.attackRadius * 0.75F)
                && verticalGap <= (double) (this.dilo.getStepHeight() + 0.75F);

        if (shouldHoldPosition) {
            this.dilo.getNavigation().stop();
        } else {
            this.dilo.getNavigation().moveTo(this.target, this.speed);
        }

        if (!shouldHoldPosition) {
            double deltaSq = this.dilo.distanceToSqr(this.lastStuckCheckX, this.lastStuckCheckY, this.lastStuckCheckZ);
            if (deltaSq < 0.0125D) {
                if (++this.stuckCheckTime > 12) {
                    this.dilo.getNavigation().recomputePath();
                    if (this.dilo.isOnGround()) this.dilo.getJumpControl().jump();
                    this.stuckCheckTime = 0;
                }
            } else {
                this.stuckCheckTime = 0;
                this.lastStuckCheckX = this.dilo.getX();
                this.lastStuckCheckY = this.dilo.getY();
                this.lastStuckCheckZ = this.dilo.getZ();
            }
        } else {
            this.stuckCheckTime = 0;
            this.lastStuckCheckX = this.dilo.getX();
            this.lastStuckCheckY = this.dilo.getY();
            this.lastStuckCheckZ = this.dilo.getZ();
        }

        this.dilo.getLookControl().setLookAt(this.target, 30.0F, 30.0F);

        if (--this.rangedAttackTime == 0) {
            if (distSq > this.maxAttackDistanceSq || !canSee) {
                this.resetCooldownByDistance(distSq);
                return;
            }
            if (SoundHandler.DILOPHOSAURUS_SPIT != null) {
                this.dilo.playSound(SoundHandler.DILOPHOSAURUS_SPIT, this.dilo.getSoundVolume(), this.dilo.getVoicePitch());
            }
            this.dilo.setAnimation(EntityAnimation.DILOPHOSAURUS_SPIT.get());
            this.animationTimer = 20;
        } else if (this.rangedAttackTime < 0) {
            this.resetCooldownByDistance(distSq);
        }

        if (this.animationTimer >= 0) {
            --this.animationTimer;
            if (this.animationTimer == 0) {
                boolean canSeeNow = this.dilo.getSensing().hasLineOfSight(this.target);
                if (distSq <= this.maxAttackDistanceSq && canSeeNow) {
                    float scaled = (float) (Mth.sqrt((float) distSq) / this.attackRadius);
                    scaled = Mth.clamp(scaled, 0.1F, 1.0F);
                    this.dilo.performRangedAttack(this.target, scaled);
                }
                this.resetCooldownByDistance(distSq);
            }
        }
    }

    private void resetCooldownByDistance(double distSq) {
        float scaled = (float) (Mth.sqrt((float) distSq) / this.attackRadius);
        int cd = Mth.floor(scaled * (float) (this.maxRangedAttackTime - this.attackInterval) + (float) this.attackInterval);
        this.rangedAttackTime = cd;
    }
}
