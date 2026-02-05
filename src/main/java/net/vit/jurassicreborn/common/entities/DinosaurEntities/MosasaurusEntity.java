package net.vit.jurassicreborn.common.entities.DinosaurEntities;

import com.github.alexthe666.citadel.animation.Animation;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimation;
import net.vit.jurassicreborn.client.sounds.SoundHandler;
import net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurHandler;
import net.vit.jurassicreborn.common.entities.SwimmingDinosaurEntity;import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;

public class MosasaurusEntity extends SwimmingDinosaurEntity {

    public MosasaurusEntity(Level world, EntityType<MosasaurusEntity> type) {
        super(world, type, DinosaurHandler.MOSASAURUS);
        this.target(LivingEntity.class);
    }

    @Override
    public SoundEvent getSoundForAnimation(Animation animation) {
        switch (EntityAnimation.getAnimation(animation)) {
            case SPEAK:
                return SoundHandler.MOSASAURUS_LIVING;
            case CALLING:
                return SoundHandler.MOSASAURUS_MATE_CALL;
            case INJURED:
                return SoundHandler.MOSASAURUS_HURT;
            case DYING:
                return SoundHandler.MOSASAURUS_DEATH;
            case ROARING:
                return SoundHandler.MOSASAURUS_ROAR;
            case ATTACKING:
                return SoundHandler.MOSASAURUS_ATTACK;
            default:
                return null;
        }
    }
}

