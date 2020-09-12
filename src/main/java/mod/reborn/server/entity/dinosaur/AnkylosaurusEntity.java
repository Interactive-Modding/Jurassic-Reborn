package mod.reborn.server.entity.dinosaur;

import mod.reborn.client.model.animation.EntityAnimation;
import mod.reborn.client.sound.SoundHandler;
import mod.reborn.server.entity.DinosaurEntity;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class AnkylosaurusEntity extends DinosaurEntity
{
    public AnkylosaurusEntity(World world)
    {
        super(world);
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
    }

    @Override
    public SoundEvent getSoundForAnimation(Animation animation) {
        switch (EntityAnimation.getAnimation(animation)) {
            case SPEAK:
                return SoundHandler.ANKYLOSAURUS_LIVING;
            case DYING:
                return SoundHandler.ANKYLOSAURUS_DEATH;
            case INJURED:
                return SoundHandler.ANKYLOSAURUS_HURT;
            case CALLING:
                return SoundHandler.ANKYLOSAURUS_CALL;
            case ATTACKING:
                return SoundHandler.ANKYLOSAURUS_ATTACK;
            case MATING:
                return SoundHandler.ANKYLOSAURUS_MATE_CALL;
            default:
                break;
        }

        return null;
    }
}
