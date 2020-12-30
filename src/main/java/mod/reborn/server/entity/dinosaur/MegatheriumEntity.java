package mod.reborn.server.entity.dinosaur;

import mod.reborn.client.model.animation.EntityAnimation;
import mod.reborn.client.sound.SoundHandler;
import mod.reborn.server.entity.DinosaurEntity;
import mod.reborn.server.entity.animal.GoatEntity;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class MegatheriumEntity extends DinosaurEntity
{
    public MegatheriumEntity(World world) { super(world); }
    public SoundEvent getSoundForAnimation(Animation animation) {

        switch (EntityAnimation.getAnimation(animation)) {
            case SPEAK:
                return SoundHandler.MEGATHERIUM_LIVING;
            case CALLING:
                return SoundHandler.MEGATHERIUM_LIVING;
            case MATING:
                return SoundHandler.MEGATHERIUM_THREAT;
            case DYING:
                return SoundHandler.MEGATHERIUM_DEATH;
            case INJURED:
                return SoundHandler.MEGATHERIUM_HURT;
            case BEGGING:
                return SoundHandler.MEGATHERIUM_THREAT;
            default:
                return null;
        }
    }
}
