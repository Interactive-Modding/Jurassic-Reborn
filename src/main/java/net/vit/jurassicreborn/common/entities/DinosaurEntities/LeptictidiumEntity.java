package net.vit.jurassicreborn.common.entities.DinosaurEntities;

import com.github.alexthe666.citadel.animation.Animation;
import net.vit.jurassicreborn.client.sounds.SoundHandler;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimation;
import net.vit.jurassicreborn.common.entities.DinosaurEntity;
import net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurHandler;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;

public class LeptictidiumEntity extends DinosaurEntity
{
    public LeptictidiumEntity(Level world, EntityType<LeptictidiumEntity> type) { super(world, type, DinosaurHandler.LEPTICTIDIUM); }
    @Override
    public SoundEvent getSoundForAnimation(Animation animation)
    {
        switch (EntityAnimation.getAnimation(animation))
        {
            case SPEAK:
                return SoundHandler.LEPTICTIDIUM_LIVING;
            case DYING:
                return SoundHandler.LEPTICTIDIUM_DEATH;
            case INJURED:
                return SoundHandler.LEPTICTIDIUM_HURT;
        }

        return null;
    }
}

