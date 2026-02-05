package net.vit.jurassicreborn.common.entities.DinosaurEntities;

import com.github.alexthe666.citadel.animation.Animation;
import net.vit.jurassicreborn.client.sounds.SoundHandler;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimation;
import net.vit.jurassicreborn.common.entities.DinosaurEntity;
import net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurHandler;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;

public class LambeosaurusEntity extends DinosaurEntity
{
    public LambeosaurusEntity(EntityType<LambeosaurusEntity> type, Level world)
    {
        super(world, type, DinosaurHandler.LAMBEOSAURUS);
    }
    @Override
    public SoundEvent getSoundForAnimation(Animation animation)
    {
        switch (EntityAnimation.getAnimation(animation))
        {
            case SPEAK:
                return SoundHandler.LAMBEOSAURUS_LIVING;
            case DYING:
                return SoundHandler.LAMBEOSAURUS_DEATH;
            case INJURED:
                return SoundHandler.LAMBEOSAURUS_HURT;
            case CALLING:
                return SoundHandler.LAMBEOSAURUS_CALL;
            case BEGGING:
                return SoundHandler.LAMBEOSAURUS_THREAT;
        }

        return null;
    }
}

