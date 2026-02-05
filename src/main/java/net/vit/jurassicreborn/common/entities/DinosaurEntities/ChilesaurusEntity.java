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

public class ChilesaurusEntity extends DinosaurEntity {

    public ChilesaurusEntity(Level world, EntityType<ChilesaurusEntity> type) {
        super(world, type, DinosaurHandler.CHILESAURUS);
        this.addTask(1, new RaptorLeapEntityAI(this));
    }

    @Override
    public SoundEvent getSoundForAnimation(Animation animation)
    {
        switch (EntityAnimation.getAnimation(animation))
        {
            case SPEAK:
                return SoundHandler.CHILESAURUS_LIVING;
            case DYING:
                return SoundHandler.CHILESAURUS_DEATH;
            case INJURED:
                return SoundHandler.CHILESAURUS_HURT;
            case CALLING:
                return SoundHandler.CHILESAURUS_LIVING;
        }

        return null;
    }
}

