package mod.reborn.server.entity.dinosaur;

import mod.reborn.client.model.animation.EntityAnimation;
import mod.reborn.client.sound.SoundHandler;
import mod.reborn.server.entity.DinosaurEntity;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class CeratosaurusEntity extends DinosaurEntity {

    public CeratosaurusEntity(World world) {
        super(world);
    }

    public SoundEvent getSoundForAnimation(Animation animation) {
        switch (EntityAnimation.getAnimation(animation)) {
            case SPEAK:
                return SoundHandler.CERATOSAURUS_LIVING;
            case DYING:
            case INJURED:
                return SoundHandler.CERATOSAURUS_HURT;
            default:
                return null;
        }
    }
}
