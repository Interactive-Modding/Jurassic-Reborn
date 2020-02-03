package mod.reborn.server.entity.dinosaur;

import mod.reborn.client.model.animation.EntityAnimation;
import mod.reborn.client.sound.SoundHandler;
import mod.reborn.server.entity.DinosaurEntity;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class ProceratosaurusEntity extends DinosaurEntity {

    public ProceratosaurusEntity(World world) {
        super(world);
    }

    public SoundEvent getSoundForAnimation(Animation animation) {
        switch (EntityAnimation.getAnimation(animation)) {
            case SPEAK:
                return SoundHandler.PROCERATOSAURUS_LIVING;
            case CALLING:
                return SoundHandler.PROCERATOSAURUS_MATE_CALL;
            case DYING:
                return SoundHandler.PROCERATOSAURUS_DEATH;
            case INJURED:
                return SoundHandler.PROCERATOSAURUS_HURT;
            case ATTACKING:
                return SoundHandler.PROCERATOSAURUS_ATTACK;
            case BEGGING:
                return SoundHandler.PROCERATOSAURUS_THREAT;
            default:
                return null;
        }
    }
}
