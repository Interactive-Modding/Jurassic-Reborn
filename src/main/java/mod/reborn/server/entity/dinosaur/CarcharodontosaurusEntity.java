package mod.reborn.server.entity.dinosaur;

import mod.reborn.client.model.animation.EntityAnimation;
import mod.reborn.client.sound.SoundHandler;
import mod.reborn.server.entity.DinosaurEntity;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class CarcharodontosaurusEntity extends DinosaurEntity {

    public CarcharodontosaurusEntity(World world) {
        super(world);
    }

    public SoundEvent getSoundForAnimation(Animation animation) {
        switch (EntityAnimation.getAnimation(animation)) {
            case SPEAK:
                return SoundHandler.CARCHARODONTOSAURUS_LIVING;
            case HISSING:
                return SoundHandler.CARCHARODONTOSAURUS_HISS;
            case DYING:
                return SoundHandler.CARCHARODONTOSAURUS_HURT;
            case INJURED:
                return SoundHandler.CARCHARODONTOSAURUS_HURT;
            case ATTACKING:
                return SoundHandler.CARCHARODONTOSAURUS_GROWL;
            case ROARING:
                return SoundHandler.CARCHARODONTOSAURUS_ROAR;
            default:
                return null;
        }
    }
}
