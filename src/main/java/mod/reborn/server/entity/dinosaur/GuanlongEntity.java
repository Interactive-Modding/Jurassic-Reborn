package mod.reborn.server.entity.dinosaur;

import mod.reborn.client.model.animation.EntityAnimation;
import mod.reborn.client.sound.SoundHandler;
import mod.reborn.server.entity.DinosaurEntity;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class GuanlongEntity extends DinosaurEntity {

    public GuanlongEntity(World world) {
        super(world);
    }

    public SoundEvent getSoundForAnimation(Animation animation) {
        switch (EntityAnimation.getAnimation(animation)) {
            case SPEAK:
                return SoundHandler.GUANLONG_LIVING;
            case DYING:
                return SoundHandler.GUANLONG_DEATH;
            case INJURED:
                return SoundHandler.GUANLONG_HURT;
            default:
                return null;
        }
    }
}
