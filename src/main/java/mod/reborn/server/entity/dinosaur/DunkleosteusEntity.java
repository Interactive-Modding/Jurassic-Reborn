package mod.reborn.server.entity.dinosaur;

import mod.reborn.client.model.animation.EntityAnimation;
import mod.reborn.client.sound.SoundHandler;
import mod.reborn.server.entity.SwimmingDinosaurEntity;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class DunkleosteusEntity extends SwimmingDinosaurEntity
{
    public DunkleosteusEntity(World world)
    {
        super(world);
        this.target(AlligatorGarEntity.class, MawsoniaEntity.class, EntitySquid.class, BeelzebufoEntity.class, CrassigyrinusEntity.class, DiplocaulusEntity.class, EntityPlayer.class, MegapiranhaEntity.class, EntityVillager.class, EntityAnimal.class, EntityMob.class);
    }
    @Override
    public SoundEvent getSoundForAnimation(Animation animation)
    {
        switch (EntityAnimation.getAnimation(animation))
        {
            case SPEAK:
                return SoundHandler.DUNKLEOSTEUS_LIVING;
            case DYING:
                return SoundHandler.DUNKLEOSTEUS_DEATH;
            case INJURED:
                return SoundHandler.DUNKLEOSTEUS_HURT;
            case BEGGING:
                return SoundHandler.DUNKLEOSTEUS_THREAT;
        }

        return null;
    }
}
