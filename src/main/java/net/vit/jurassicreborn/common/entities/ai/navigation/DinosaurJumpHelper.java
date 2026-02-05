package net.vit.jurassicreborn.common.entities.ai.navigation;

import com.github.alexthe666.citadel.animation.Animation;
import net.minecraft.world.entity.ai.control.JumpControl;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimation;
import net.vit.jurassicreborn.common.entities.DinosaurEntity;

public class DinosaurJumpHelper extends JumpControl {
    private final DinosaurEntity entity;
    private boolean sounded;
    private boolean jumped;
    private float yaw;
    private double speed;
    private int jumpHeight;

    public DinosaurJumpHelper(DinosaurEntity entity) {
        super(entity);
        this.entity = entity;
    }

    @Override
    public void jump() {
        this.jump(1);
    }

    public void jump(int height) {
        this.jump = true;
        this.jumped = true;
        this.jumpHeight = Math.min(entity.getDinosaur().getJumpHeight(), height);
        this.yaw = this.entity.getYRot();
        this.speed = this.entity.getSpeed();
        Animation animation = this.entity.getAnimation();
        if (this.jumpHeight > 1 && animation != EntityAnimation.PREPARE_LEAP.get() && animation != EntityAnimation.LEAP.get() && animation != EntityAnimation.LEAP_LAND.get()) {
            if (!this.entity.isInWater() && !this.entity.inLava() && this.entity.isOnGround()) {
                this.entity.setAnimation(EntityAnimation.PREPARE_LEAP.get());
            }
            this.sounded = false;
        } else {
            this.sounded = true;
        }
    }

    @Override
    public void tick() {
        if (!this.sounded && this.jumpHeight > 1 && this.entity.getAnimation() == EntityAnimation.LEAP.get()) {
            this.entity.playSound(this.entity.getSoundForAnimation(EntityAnimation.ATTACKING.get()), this.entity.getSoundVolume(), this.entity.getVoicePitch());
            this.sounded = true;
        }
        if (this.jump && (this.jumpHeight <= 1 || this.entity.getAnimation() == EntityAnimation.LEAP.get())) {
            this.entity.setJumping(this.jump);
            this.entity.setJumpHeight(this.jumpHeight);
            this.jump = false;
            this.jumpHeight = 0;
        } else {
            if (this.jumped) {
                if (this.entity.getAnimation() == EntityAnimation.LEAP.get() || this.entity.getAnimation() == EntityAnimation.LEAP_LAND.get()) {
                    this.entity.setYRot(this.yaw);
                    float forwardSpeed = (float) (this.speed * 0.2F);
                    float yawRadians = this.entity.getYRot() * ((float) Math.PI / 180F);
                    double impulseX = -Math.sin(yawRadians) * forwardSpeed;
                    double impulseZ = Math.cos(yawRadians) * forwardSpeed;

                    this.entity.setDeltaMovement(this.entity.getDeltaMovement().add(impulseX, 0.0D, impulseZ));
                }
            }
            if (this.entity.getAnimation() == EntityAnimation.LEAP_LAND.get()) {
                this.jumped = false;
                this.sounded = false;
            }
            this.entity.setJumping(false);
            this.entity.setJumpHeight(0);
        }
    }
}
