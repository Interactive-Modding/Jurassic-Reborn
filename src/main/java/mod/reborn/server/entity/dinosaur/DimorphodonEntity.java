package mod.reborn.server.entity.dinosaur;

import mod.reborn.client.model.animation.EntityAnimation;
import mod.reborn.client.sound.SoundHandler;
import mod.reborn.server.entity.FlyingDinosaurEntity;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class DimorphodonEntity extends FlyingDinosaurEntity
{
    public DimorphodonEntity(World world)
    {
        super(world);
        this.target(AlvarezsaurusEntity.class, EntityPlayer.class, DimetrodonEntity.class, EntityAnimal.class, EntityVillager.class, CompsognathusEntity.class, DodoEntity.class, LeaellynasauraEntity.class, LeptictidiumEntity.class, MicroceratusEntity.class, MicroraptorEntity.class, OthnieliaEntity.class, SegisaurusEntity.class);
    }

    @Override
    protected void doTarget(){
        this.target(LeptictidiumEntity.class, MicroraptorEntity.class, MicroceratusEntity.class, CompsognathusEntity.class);
    }
    @Override
    public SoundEvent getSoundForAnimation(Animation animation)
    {
        switch (EntityAnimation.getAnimation(animation))
        {
            case SPEAK:
                return SoundHandler.DIMORPHODON_LIVING;
            case DYING:
                return SoundHandler.DIMORPHODON_DEATH;
            case INJURED:
                return SoundHandler.DIMORPHODON_HURT;
            case CALLING:
                return SoundHandler.DIMORPHODON_CALL;
            case BEGGING:
                return SoundHandler.DIMORPHODON_THREAT;
        }

        return null;
    }
}
