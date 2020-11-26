package mod.reborn.server.entity.dinosaur;

import mod.reborn.client.model.animation.EntityAnimation;
import mod.reborn.client.sound.SoundHandler;
import mod.reborn.server.entity.DinosaurEntity;
import mod.reborn.server.entity.animal.GoatEntity;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class OviraptorEntity extends DinosaurEntity
{
    public OviraptorEntity(World world)
    {
        super(world);
        this.target(CompsognathusEntity.class, LeptictidiumEntity.class, OthnieliaEntity.class, EntityPlayer.class, EntityAnimal.class, EntityVillager.class, GoatEntity.class);
        doesEatEggs(true);
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
    }
    @Override
    public SoundEvent getSoundForAnimation(Animation animation)
    {
        switch (EntityAnimation.getAnimation(animation))
        {
            case SPEAK:
                return SoundHandler.OVIRAPTOR_LIVING;
            case DYING:
                return SoundHandler.OVIRAPTOR_DEATH;
            case INJURED:
                return SoundHandler.OVIRAPTOR_HURT;
            case BEGGING:
                return SoundHandler.OVIRAPTOR_THREAT;
        }

        return null;
    }
}
