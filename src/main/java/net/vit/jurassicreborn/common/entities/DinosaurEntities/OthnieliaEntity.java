package net.vit.jurassicreborn.common.entities.DinosaurEntities;

import com.github.alexthe666.citadel.animation.Animation;
import net.vit.jurassicreborn.client.sounds.SoundHandler;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimation;
import net.vit.jurassicreborn.common.entities.DinosaurEntity;
import net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurHandler;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;

public class OthnieliaEntity extends DinosaurEntity
{
    public OthnieliaEntity(EntityType<OthnieliaEntity> type, Level world)
    {
        super(world, type, DinosaurHandler.OTHNIELIA);
    }
    @Override
    public SoundEvent getSoundForAnimation(Animation animation)
    {
        switch (EntityAnimation.getAnimation(animation))
        {
            case SPEAK:
                return SoundHandler.OTHNIELIA_LIVING;
            case DYING:
                return SoundHandler.OTHNIELIA_DEATH;
            case INJURED:
                return SoundHandler.OTHNIELIA_HURT;
            case CALLING:
                return SoundHandler.OTHNIELIA_LIVING;
        }

        return null;
    }
}

