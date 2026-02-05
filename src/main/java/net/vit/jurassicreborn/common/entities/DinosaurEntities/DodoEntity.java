package net.vit.jurassicreborn.common.entities.DinosaurEntities;

import com.github.alexthe666.citadel.animation.Animation;
import net.vit.jurassicreborn.client.sounds.SoundHandler;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimation;
import net.vit.jurassicreborn.common.entities.DinosaurEntity;
import net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurHandler;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;

public class DodoEntity extends DinosaurEntity
{
    public DodoEntity(EntityType<DodoEntity> type, Level world)
    {
        super(world, type, DinosaurHandler.DODO);
    }

    @Override
    public SoundEvent getSoundForAnimation(Animation animation)
    {
        switch (EntityAnimation.getAnimation(animation))
        {
            case SPEAK:
                return SoundHandler.DODO_LIVING;
            case DYING:
                return SoundHandler.DODO_DEATH;
            case INJURED:
                return SoundHandler.DODO_HURT;
        }

        return null;
    }
}

