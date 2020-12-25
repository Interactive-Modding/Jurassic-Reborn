package mod.reborn.server.entity.dinosaur;


import mod.reborn.client.model.animation.EntityAnimation;
import mod.reborn.client.sound.SoundHandler;
import mod.reborn.server.entity.DinosaurEntity;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class ElasmotheriumEntity extends DinosaurEntity
{
    public ElasmotheriumEntity(World world) { super(world); }
    public SoundEvent getSoundForAnimation(Animation animation) {

        switch (EntityAnimation.getAnimation(animation)) {
            case SPEAK:
                return SoundHandler.ELASMOTHERIUM_LIVING;
            case CALLING:
                return SoundHandler.ELASMOTHERIUM_LIVING;
            case MATING:
                return SoundHandler.ELASMOTHERIUM_THREAT;
            case DYING:
                return SoundHandler.ELASMOTHERIUM_DEATH;
            case INJURED:
                return SoundHandler.ELASMOTHERIUM_HURT;
            case BEGGING:
                return SoundHandler.ELASMOTHERIUM_THREAT;
            default:
                return null;
        }
    }
}
