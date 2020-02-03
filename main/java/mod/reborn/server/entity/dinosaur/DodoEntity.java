package mod.reborn.server.entity.dinosaur;

import mod.reborn.client.model.animation.EntityAnimation;
import mod.reborn.client.sound.SoundHandler;
import mod.reborn.server.entity.DinosaurEntity;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class DodoEntity extends DinosaurEntity
{
    public DodoEntity(World world)
    {
        super(world);
    }

    @Override
    public SoundEvent getSoundForAnimation(Animation animation)
    {
        switch (EntityAnimation.getAnimation(animation))
        {
            case SPEAK:
                return SoundHandler.DODO_LIVING;
            case DYING:
                return SoundHandler.DODO_DEATH;
            case INJURED:
                return SoundHandler.DODO_HURT;
        }

        return null;
    }
}
