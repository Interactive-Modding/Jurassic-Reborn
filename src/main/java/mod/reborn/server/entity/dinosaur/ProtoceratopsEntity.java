package mod.reborn.server.entity.dinosaur;

import mod.reborn.client.model.animation.EntityAnimation;
import mod.reborn.client.sound.SoundHandler;
import mod.reborn.server.entity.DinosaurEntity;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class ProtoceratopsEntity extends DinosaurEntity
{
    public ProtoceratopsEntity(World world)
    {
        super(world);
    }
    @Override
    public SoundEvent getSoundForAnimation(Animation animation)
    {
        switch (EntityAnimation.getAnimation(animation))
        {
            case SPEAK:
                return SoundHandler.PROTOCERATOPS_LIVING;
            case DYING:
                return SoundHandler.PROTOCERATOPS_DEATH;
            case INJURED:
                return SoundHandler.PROTOCERATOPS_HURT;
            case BEGGING:
                return SoundHandler.PROTOCERATOPS_THREAT;
        }

        return null;
    }
}
