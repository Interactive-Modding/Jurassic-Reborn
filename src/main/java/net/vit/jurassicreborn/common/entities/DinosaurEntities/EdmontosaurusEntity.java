package net.vit.jurassicreborn.common.entities.DinosaurEntities;

import com.github.alexthe666.citadel.animation.Animation;
import net.vit.jurassicreborn.client.sounds.SoundHandler;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimation;
import net.vit.jurassicreborn.common.entities.DinosaurEntity;
import net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurHandler;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;

public class EdmontosaurusEntity extends DinosaurEntity
{
    public EdmontosaurusEntity(EntityType<EdmontosaurusEntity> type, Level world)
    {
        super(world, type, DinosaurHandler.EDMONTOSAURUS);
    }
    @Override
    public SoundEvent getSoundForAnimation(Animation animation)
    {
        switch (EntityAnimation.getAnimation(animation))
        {
            case SPEAK:
                return SoundHandler.EDMONTOSAURUS_LIVING;
            case DYING:
                return SoundHandler.EDMONTOSAURUS_DEATH;
            case INJURED:
                return SoundHandler.EDMONTOSAURUS_HURT;
            case CALLING:
                return SoundHandler.EDMONTOSAURUS_CALL;
            case BEGGING:
                return SoundHandler.EDMONTOSAURUS_THREAT;
        }

        return null;
    }
}

