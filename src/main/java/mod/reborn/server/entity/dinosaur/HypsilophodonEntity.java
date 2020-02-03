package mod.reborn.server.entity.dinosaur;

import mod.reborn.client.model.animation.EntityAnimation;
import mod.reborn.client.sound.SoundHandler;
import mod.reborn.server.entity.DinosaurEntity;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class HypsilophodonEntity extends DinosaurEntity
{
    public HypsilophodonEntity(World world)
    {
        super(world);
    }

    @Override
    public SoundEvent getSoundForAnimation(Animation animation)
    {
        switch (EntityAnimation.getAnimation(animation))
        {
            case SPEAK:
                return SoundHandler.HYPSILOPHODON_LIVING;
            case DYING:
                return SoundHandler.HYPSILOPHODON_HURT;
            case INJURED:
                return SoundHandler.HYPSILOPHODON_HURT;
        }

        return null;
    }

    @Override
    protected float getJumpUpwardsMotion() {
        return 0.62F;
    }
}
