package mod.reborn.server.entity.dinosaur;

import mod.reborn.client.model.animation.EntityAnimation;
import mod.reborn.client.sound.SoundHandler;
import mod.reborn.server.entity.DinosaurEntity;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class LambeosaurusEntity extends DinosaurEntity
{
    public LambeosaurusEntity(World world)
    {
        super(world);
    }
    @Override
    public SoundEvent getSoundForAnimation(Animation animation)
    {
        switch (EntityAnimation.getAnimation(animation))
        {
            case SPEAK:
                return SoundHandler.LAMBEOSAURUS_LIVING;
            case DYING:
                return SoundHandler.LAMBEOSAURUS_DEATH;
            case INJURED:
                return SoundHandler.LAMBEOSAURUS_HURT;
            case CALLING:
                return SoundHandler.LAMBEOSAURUS_CALL;
            case BEGGING:
                return SoundHandler.LAMBEOSAURUS_THREAT;
        }

        return null;
    }
}
