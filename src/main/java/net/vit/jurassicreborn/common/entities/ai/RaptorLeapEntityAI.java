package net.vit.jurassicreborn.common.entities.ai;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimation;
import net.vit.jurassicreborn.common.entities.DinosaurEntity;

import java.util.EnumSet;

public class RaptorLeapEntityAI extends Goal {
    private final DinosaurEntity entity;
    private LivingEntity target;

    private int prevTick;
    private EntityAnimation animation;

    private double targetPrevPosX;
    private double targetPrevPosZ;

    private boolean ticked = false;

    public RaptorLeapEntityAI(DinosaurEntity entity) {
        this.entity = entity;
        this.setFlags(EnumSet.of(Flag.TARGET, Flag.LOOK, Flag.MOVE, Flag.JUMP));
    }

    @Override
    public boolean canUse() {
        if (this.entity.herd != null && this.entity.herd.fleeing) {
            return false;
        }

        // Use your 1.19 target accessor
        LivingEntity tgt = this.entity.getTarget(); // if you expose getAttackTarget(), swap here
        if (tgt == null || !tgt.isAlive()) return false;
        if (tgt instanceof DinosaurEntity d && d.isCarcass()) return false;

        float distance = this.entity.distanceTo(tgt);
        if (!(distance >= 5.0F && distance <= 6.0F)) return false;
        if (!this.entity.onGround()) return false;

        // LOS check: from our eyes to target position
        Vec3 from = new Vec3(this.entity.getX(), this.entity.getEyeY(), this.entity.getZ());
        HitResult hit = this.entity.level().clip(new ClipContext(
                from,
                tgt.position(),
                ClipContext.Block.OUTLINE,
                ClipContext.Fluid.ANY,
                this.entity
        ));
        if (hit != null && hit.getType() == HitResult.Type.BLOCK) return false;

        this.target = tgt;
        return true;
    }

    @Override
    public void start() {
        this.animation = EntityAnimation.PREPARE_LEAP;
        this.entity.getLookControl().setLookAt(this.target, 30.0F, 30.0F);
        this.entity.getNavigation().stop();
        if (!this.entity.level().isClientSide) {
            this.entity.setDeltaMovement(Vec3.ZERO);
        }
        this.ticked = false;
    }

    @Override
    public void tick() {
        int tick = this.entity.getAnimationTick();

        this.entity.getNavigation().stop();
        this.entity.getLookControl().setLookAt(this.target, 30.0F, 30.0F);

        if (this.animation == EntityAnimation.PREPARE_LEAP && tick < this.prevTick) {
            this.animation = EntityAnimation.LEAP;

            // Keep your sound accessors as defined on your entity
            this.entity.playSound(this.entity.getSoundForAnimation(EntityAnimation.ATTACKING.get()),
                    this.entity.getSoundVolume(), this.entity.getVoicePitch());

            // Previous-frame target motion (1.19 fields)
            double targetSpeedX = this.target.getX() - (!this.ticked ? this.target.xOld : this.targetPrevPosX);
            double targetSpeedZ = this.target.getZ() - (!this.ticked ? this.target.zOld : this.targetPrevPosZ);

            double length = this.entity.getBbWidth() * 6.0F;

            double destX = this.target.getX() + targetSpeedX * length;
            double destZ = this.target.getZ() + targetSpeedZ * length;

            double dx = destX - this.entity.getX();
            double dz = destZ - this.entity.getZ();
            double delta = Math.sqrt(dx * dx + dz * dz);
            double angle = Math.atan2(dz, dx);

            double mx = (delta / length) * Math.cos(angle);
            double mz = (delta / length) * Math.sin(angle);
            double my = Math.min(0.3, Math.max(0.0, (this.target.getY() - this.entity.getY()) * 0.1)) + 0.6;

            // Apply motion (server side authoritative)
            if (!this.entity.level().isClientSide) {
                this.entity.setDeltaMovement(mx, my, mz);
                this.entity.hasImpulse = true;
            }
        } else if (this.animation == EntityAnimation.LEAP && this.entity.getDeltaMovement().y < 0.0) {
            this.animation = EntityAnimation.LEAP_LAND;
        } else if (this.animation == EntityAnimation.LEAP_LAND && (this.entity.onGround() || this.entity.isSwimming())) {
            this.animation = EntityAnimation.IDLE;

            if (this.entity.getBoundingBox() != null && this.target.getBoundingBox() != null &&
                    this.entity.getBoundingBox().intersects(this.target.getBoundingBox().inflate(2.0, 2.0, 2.0))) {
                this.entity.doHurtTarget(this.target);
            }
        }

        this.targetPrevPosX = this.target.getX();
        this.targetPrevPosZ = this.target.getZ();
        this.ticked = true;

        if (this.entity.getAnimation() != this.animation.get()) {
            this.entity.setAnimation(this.animation.get());
            this.entity.setAnimationTick(this.prevTick + 1);
        }

        this.prevTick = tick;
    }

    @Override
    public void stop() {
        this.entity.setAnimation(EntityAnimation.IDLE.get());
    }

    @Override
    public boolean canContinueToUse() {
        return this.target != null
                && this.target.isAlive()
                && !(this.target instanceof DinosaurEntity d && d.isCarcass())
                && this.animation != EntityAnimation.IDLE;
    }
}
