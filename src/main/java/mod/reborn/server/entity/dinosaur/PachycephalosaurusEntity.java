package mod.reborn.server.entity.dinosaur;

import mod.reborn.client.model.animation.EntityAnimation;
import mod.reborn.client.sound.SoundHandler;
import mod.reborn.server.entity.DinosaurEntity;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class PachycephalosaurusEntity extends DinosaurEntity
{
    public PachycephalosaurusEntity(World world)
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
                return SoundHandler.PACHYCEPHALOSAURUS_LIVING;
            case DYING:
                return SoundHandler.PACHYCEPHALOSAURUS_DEATH;
            case INJURED:
                return SoundHandler.PACHYCEPHALOSAURUS_HURT;
            case CALLING:
                return SoundHandler.PACHYCEPHALOSAURUS_CALL;
            case BEGGING:
                return SoundHandler.PACHYCEPHALOSAURUS_THREAT;
        }

        return null;
    }
}
