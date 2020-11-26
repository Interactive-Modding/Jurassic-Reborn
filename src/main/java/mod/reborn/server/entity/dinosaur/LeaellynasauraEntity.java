package mod.reborn.server.entity.dinosaur;

import mod.reborn.client.model.animation.EntityAnimation;
import mod.reborn.client.sound.SoundHandler;
import mod.reborn.server.entity.DinosaurEntity;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class LeaellynasauraEntity extends DinosaurEntity
{
    public LeaellynasauraEntity(World world)
    {
        super(world);
    }
    @Override
    public SoundEvent getSoundForAnimation(Animation animation)
    {
        switch (EntityAnimation.getAnimation(animation))
        {
            case SPEAK:
                return SoundHandler.LEAELLYNASAURA_LIVING;
            case DYING:
                return SoundHandler.LEAELLYNASAURA_DEATH;
            case INJURED:
                return SoundHandler.LEAELLYNASAURA_HURT;
        }

        return null;
    }
}
