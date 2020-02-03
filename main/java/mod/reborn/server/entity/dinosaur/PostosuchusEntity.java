package mod.reborn.server.entity.dinosaur;

import mod.reborn.client.model.animation.EntityAnimation;
import mod.reborn.client.sound.SoundHandler;
import mod.reborn.server.entity.DinosaurEntity;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class PostosuchusEntity extends DinosaurEntity {

    public PostosuchusEntity(World world) {
        super(world);
    }

    @Override
    public SoundEvent getSoundForAnimation(Animation animation) {
        switch (EntityAnimation.getAnimation(animation)) {
            case CALLING:
                return SoundHandler.POSTOSUCHUS_CALL;
            case DYING:
                return SoundHandler.POSTOSUCHUS_DEATH;
            case INJURED:
                return SoundHandler.POSTOSUCHUS_ATTACK;
            case ATTACKING:
                return SoundHandler.POSTOSUCHUS_ATTACK;
            default:
                return null;
        }
    }
}
