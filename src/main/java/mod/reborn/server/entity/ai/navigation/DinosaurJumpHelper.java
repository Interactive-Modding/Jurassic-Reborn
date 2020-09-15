package mod.reborn.server.entity.ai.navigation;

import mod.reborn.client.model.animation.EntityAnimation;
import mod.reborn.server.entity.DinosaurEntity;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.minecraft.entity.ai.EntityJumpHelper;

public class DinosaurJumpHelper extends EntityJumpHelper {
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
    public void setJumping() {
        this.jump(1);
    }

    public void jump(int height) {
        this.isJumping = true;
        this.jumped = true;
        this.jumpHeight = Math.min(entity.getDinosaur().getJumpHeight(), height);
        this.yaw = this.entity.rotationYaw;
        this.speed = this.entity.getAIMoveSpeed();
        Animation animation = this.entity.getAnimation();
        if (this.jumpHeight > 1 && animation != EntityAnimation.PREPARE_LEAP.get() && animation != EntityAnimation.LEAP.get() && animation != EntityAnimation.LEAP_LAND.get()) {
            if (!this.entity.isInWater() && !this.entity.inLava() && this.entity.onGround) {
                this.entity.setAnimation(EntityAnimation.PREPARE_LEAP.get());
            }
            this.sounded = false;
        } else {
            this.sounded = true;
        }
    }

    @Override
    public void doJump() {
        if (!this.sounded && this.jumpHeight > 1 && this.entity.getAnimation() == EntityAnimation.LEAP.get()) {
            this.entity.playSound(this.entity.getSoundForAnimation(EntityAnimation.ATTACKING.get()), this.entity.getSoundVolume(), this.entity.getSoundPitch());
            this.sounded = true;
        }
        if (this.isJumping && (this.jumpHeight <= 1 || this.entity.getAnimation() == EntityAnimation.LEAP.get())) {
            this.entity.setJumping(this.isJumping);
            this.entity.setJumpHeight(this.jumpHeight);
            this.isJumping = false;
            this.jumpHeight = 0;
        } else {
            if (this.jumped) {
                if (this.entity.getAnimation() == EntityAnimation.LEAP.get() || this.entity.getAnimation() == EntityAnimation.LEAP_LAND.get()) {
                    this.entity.rotationYaw = this.yaw;
                    /*TODO: Make sure works */this.entity.travel(0.0F, (float) this.speed * 0.2F, 0F);
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
