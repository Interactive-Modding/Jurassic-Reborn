package net.vit.jurassicreborn.common.entities.DinosaurEntities;

import com.github.alexthe666.citadel.animation.Animation;
import net.vit.jurassicreborn.client.sounds.SoundHandler;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimation;
import net.vit.jurassicreborn.common.entities.DinosaurEntity;
import net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurHandler;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;

public class HypsilophodonEntity extends DinosaurEntity
{
    public HypsilophodonEntity(EntityType<HypsilophodonEntity> type, Level world)
    {
        super(world, type, DinosaurHandler.HYPSILOPHODON);
    }

    @Override
    public SoundEvent getSoundForAnimation(Animation animation)
    {
        switch (EntityAnimation.getAnimation(animation))
        {
            case SPEAK:
                return SoundHandler.HYPSILOPHODON_LIVING;
            case DYING:
                return SoundHandler.HYPSILOPHODON_HURT;
            case INJURED:
                return SoundHandler.HYPSILOPHODON_HURT;
        }

        return null;
    }

    @Override
    protected float getJumpUpwardsMotion() {
        return 0.62F;
    }
}

