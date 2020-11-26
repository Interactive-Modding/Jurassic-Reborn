package mod.reborn.server.entity.dinosaur;

import mod.reborn.client.model.animation.EntityAnimation;
import mod.reborn.client.sound.SoundHandler;
import mod.reborn.server.entity.DinosaurEntity;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class OthnieliaEntity extends DinosaurEntity
{
    public OthnieliaEntity(World world)
    {
        super(world);
    }
    @Override
    public SoundEvent getSoundForAnimation(Animation animation)
    {
        switch (EntityAnimation.getAnimation(animation))
        {
            case SPEAK:
                return SoundHandler.OTHNIELIA_LIVING;
            case DYING:
                return SoundHandler.OTHNIELIA_DEATH;
            case INJURED:
                return SoundHandler.OTHNIELIA_HURT;
            case CALLING:
                return SoundHandler.OTHNIELIA_LIVING;
        }

        return null;
    }
}
