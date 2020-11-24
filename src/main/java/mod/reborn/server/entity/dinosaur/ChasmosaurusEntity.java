package mod.reborn.server.entity.dinosaur;

import mod.reborn.client.model.animation.EntityAnimation;
import mod.reborn.client.sound.SoundHandler;
import mod.reborn.server.entity.DinosaurEntity;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class ChasmosaurusEntity extends DinosaurEntity
{
    public ChasmosaurusEntity(World world)
    {
        super(world);
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
    }

    @Override
    public SoundEvent getSoundForAnimation(Animation animation)
    {
        switch (EntityAnimation.getAnimation(animation))
        {
            case SPEAK:
                return SoundHandler.CHASMOSAURUS_LIVING;
            case DYING:
                return SoundHandler.CHASMOSAURUS_DEATH;
            case INJURED:
                return SoundHandler.CHASMOSAURUS_HURT;
            case BEGGING:
                return SoundHandler.CHASMOSAURUS_THREAT;
        }

        return null;
    }
}
