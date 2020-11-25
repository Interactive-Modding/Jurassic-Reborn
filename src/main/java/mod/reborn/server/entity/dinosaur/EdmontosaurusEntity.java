package mod.reborn.server.entity.dinosaur;

import mod.reborn.client.model.animation.EntityAnimation;
import mod.reborn.client.sound.SoundHandler;
import mod.reborn.server.entity.DinosaurEntity;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class EdmontosaurusEntity extends DinosaurEntity
{
    public EdmontosaurusEntity(World world)
    {
        super(world);
    }
    @Override
    public SoundEvent getSoundForAnimation(Animation animation)
    {
        switch (EntityAnimation.getAnimation(animation))
        {
            case SPEAK:
                return SoundHandler.EDMONTOSAURUS_LIVING;
            case DYING:
                return SoundHandler.EDMONTOSAURUS_DEATH;
            case INJURED:
                return SoundHandler.EDMONTOSAURUS_HURT;
            case CALLING:
                return SoundHandler.EDMONTOSAURUS_CALL;
            case BEGGING:
                return SoundHandler.EDMONTOSAURUS_THREAT;
        }

        return null;
    }
}
