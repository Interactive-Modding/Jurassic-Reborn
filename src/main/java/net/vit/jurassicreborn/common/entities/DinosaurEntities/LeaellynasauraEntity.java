package net.vit.jurassicreborn.common.entities.DinosaurEntities;

import com.github.alexthe666.citadel.animation.Animation;
import net.vit.jurassicreborn.client.sounds.SoundHandler;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimation;
import net.vit.jurassicreborn.common.entities.DinosaurEntity;
import net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurHandler;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;

public class LeaellynasauraEntity extends DinosaurEntity
{
    public LeaellynasauraEntity(EntityType<LeaellynasauraEntity> type, Level world)
    {
        super(world, type, DinosaurHandler.LEAELLYNASAURA);
    }
    @Override
    public SoundEvent getSoundForAnimation(Animation animation)
    {
        switch (EntityAnimation.getAnimation(animation))
        {
            case SPEAK:
                return SoundHandler.LEAELLYNASAURA_LIVING;
            case DYING:
                return SoundHandler.LEAELLYNASAURA_DEATH;
            case INJURED:
                return SoundHandler.LEAELLYNASAURA_HURT;
        }

        return null;
    }
}

