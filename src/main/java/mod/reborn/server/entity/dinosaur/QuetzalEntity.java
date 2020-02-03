package mod.reborn.server.entity.dinosaur;

import mod.reborn.client.model.animation.EntityAnimation;
import mod.reborn.client.sound.SoundHandler;
import mod.reborn.server.entity.FlyingDinosaurEntity;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class QuetzalEntity extends FlyingDinosaurEntity
{
    public QuetzalEntity(World world)
    {
        super(world);
    }

    @Override
    public SoundEvent getSoundForAnimation(Animation animation)
    {
        switch (EntityAnimation.getAnimation(animation))
        {
            case SPEAK:
                return SoundHandler.PTERANODON_LIVING;
            case CALLING:
                return SoundHandler.PTERANODON_CALL;
            case DYING:
                return SoundHandler.PTERANODON_DEATH;
            case INJURED:
                return SoundHandler.PTERANODON_HURT;
        }

        return null;
    }
}
