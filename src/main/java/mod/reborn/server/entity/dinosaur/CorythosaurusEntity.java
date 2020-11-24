package mod.reborn.server.entity.dinosaur;

import mod.reborn.client.model.animation.EntityAnimation;
import mod.reborn.client.sound.SoundHandler;
import mod.reborn.server.entity.DinosaurEntity;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class CorythosaurusEntity extends DinosaurEntity
{
    public CorythosaurusEntity(World world)
    {
        super(world);
    }
    @Override
    public SoundEvent getSoundForAnimation(Animation animation)
    {
        switch (EntityAnimation.getAnimation(animation))
        {
            case SPEAK:
                return SoundHandler.CORYTHOSAURUS_LIVING;
            case DYING:
                return SoundHandler.CORYTHOSAURUS_DEATH;
            case INJURED:
                return SoundHandler.CORYTHOSAURUS_HURT;
            case CALLING:
                return SoundHandler.CORYTHOSAURUS_CALL;
            case BEGGING:
                return SoundHandler.CORYTHOSAURUS_THREAT;
        }

        return null;
    }
}

