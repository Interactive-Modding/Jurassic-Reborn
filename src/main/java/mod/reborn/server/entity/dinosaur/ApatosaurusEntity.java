package mod.reborn.server.entity.dinosaur;

import mod.reborn.client.model.animation.EntityAnimation;
import mod.reborn.client.sound.SoundHandler;
import mod.reborn.server.entity.DinosaurEntity;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class ApatosaurusEntity extends DinosaurEntity
{
    private int stepCount = 0;

    public ApatosaurusEntity(World world)
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
                return SoundHandler.APATOSAURUS_LIVING;
            case DYING:
                return SoundHandler.APATOSAURUS_DEATH;
            case INJURED:
                return SoundHandler.APATOSAURUS_HURT;
            case CALLING:
                return SoundHandler.APATOSAURUS_CALL;
            case BEGGING:
                return SoundHandler.APATOSAURUS_THREAT;
            case WALKING:
                return SoundHandler.STOMP;
        }

        return null;
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();

        if (this.moveForward > 0 && this.stepCount <= 0)
        {
            this.playSound(SoundHandler.STOMP, (float) interpolate(0.1F, 1.0F), this.getSoundPitch());
            stepCount = 65;
        }

        this.stepCount -= this.moveForward * 9.5;
    }
}
