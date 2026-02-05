package net.vit.jurassicreborn.common.entities.DinosaurEntities;


import com.github.alexthe666.citadel.animation.Animation;
import net.vit.jurassicreborn.client.sounds.SoundHandler;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimation;
import net.vit.jurassicreborn.common.entities.DinosaurEntity;
import net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurHandler;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;

public class ElasmotheriumEntity extends DinosaurEntity
{
    public ElasmotheriumEntity(EntityType<ElasmotheriumEntity> type, Level world) { super(world, type, DinosaurHandler.ELASMOTHERIUM); }
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

