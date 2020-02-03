package mod.reborn.server.entity.dinosaur;

import mod.reborn.client.model.animation.EntityAnimation;
import mod.reborn.client.sound.SoundHandler;
import mod.reborn.server.entity.DinosaurEntity;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class SuchomimusEntity extends DinosaurEntity {

    public SuchomimusEntity(World world) {
        super(world);
    }

    public SoundEvent getSoundForAnimation(Animation animation) {
        switch (EntityAnimation.getAnimation(animation)) {
            case SPEAK:
                return SoundHandler.SUCHOMIMUS_LIVING;
            case CALLING:
                return SoundHandler.SUCHOMIMUS_MATE_CALL;
            case DYING:
                return SoundHandler.SUCHOMIMUS_DEATH;
            case INJURED:
                return SoundHandler.SUCHOMIMUS_HURT;
            case ROARING:
                return SoundHandler.SUCHOMIMUS_ROAR;
            default:
                return null;
        }
    }
}
