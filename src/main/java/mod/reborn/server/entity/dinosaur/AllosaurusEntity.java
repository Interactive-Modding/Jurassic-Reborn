package mod.reborn.server.entity.dinosaur;

import mod.reborn.client.model.animation.EntityAnimation;
import mod.reborn.client.sound.SoundHandler;
import mod.reborn.server.entity.DinosaurEntity;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class AllosaurusEntity extends DinosaurEntity {

    public AllosaurusEntity(World world) {
        super(world);
    }

    @Override
    public SoundEvent getSoundForAnimation(Animation animation) {
        switch (EntityAnimation.getAnimation(animation)) {
            case SPEAK:
                return SoundHandler.ALLOSAURUS_LIVING;
            case CALLING:
                return SoundHandler.ALLOSAURUS_MATE_CALL;
            case DYING:
                return SoundHandler.ALLOSAURUS_DEATH;
            case INJURED:
                return SoundHandler.ALLOSAURUS_HURT;
            case ATTACKING:
                return !isMale()?SoundHandler.ALLOSAURUS_FIGHT_FEMALE:null;
            case ROARING:
                return SoundHandler.ALLOSAURUS_ROAR;
            case BEGGING:
                return SoundHandler.ALLOSAURUS_THREAT;
            default:
                return null;
        }
    }
}
