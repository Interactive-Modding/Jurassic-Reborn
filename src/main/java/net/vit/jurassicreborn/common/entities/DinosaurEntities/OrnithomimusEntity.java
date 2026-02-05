package net.vit.jurassicreborn.common.entities.DinosaurEntities;

import com.github.alexthe666.citadel.animation.Animation;
import net.vit.jurassicreborn.client.sounds.SoundHandler;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimation;
import net.vit.jurassicreborn.common.entities.DinosaurEntity;
import net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurHandler;
import net.vit.jurassicreborn.common.entities.ai.RaptorLeapEntityAI;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;
import net.vit.jurassicreborn.common.entities.ai.animations.PeckGroundAnimationAI;

public class OrnithomimusEntity extends DinosaurEntity
{
    public OrnithomimusEntity(Level world, EntityType<OrnithomimusEntity> type)
    {
        super(world, type, DinosaurHandler.ORNITHOMIMUS);
        this.addTask(3, new PeckGroundAnimationAI(this));
        this.addTask(1, new RaptorLeapEntityAI(this));
    }

//    @Override
//    public EntityAIBase getAttackAI() {TODO:AI
//        return new RaptorLeapEntityAI(this);
//    }

    @Override
    public int calculateFallDamage(float distance, float damageMultiplier) {
        if (this.getAnimation() != EntityAnimation.LEAP_LAND.get()) {
            return super.calculateFallDamage(distance, damageMultiplier);
        }
        return 0;
    }

    @Override
    public SoundEvent getSoundForAnimation(Animation animation) {
        switch (EntityAnimation.getAnimation(animation)) {
            case SPEAK:
                return SoundHandler.ORNITHOMIMUS_LIVING;
            case CALLING:
                return SoundHandler.ORNITHOMIMUS_LIVING;
            case DYING:
                return SoundHandler.ORNITHOMIMUS_DEATH;
            case INJURED:
                return SoundHandler.ORNITHOMIMUS_HURT;
            default:
                break;
        }

        return null;
    }
}

