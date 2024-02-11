package mod.reborn.server.entity.dinosaur;

import mod.reborn.client.model.animation.EntityAnimation;
import mod.reborn.client.sound.SoundHandler;
import mod.reborn.server.entity.DinosaurEntity;
import mod.reborn.server.entity.animal.GoatEntity;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class DimetrodonEntity extends DinosaurEntity
{
    public DimetrodonEntity(World world)
    {
        super(world);
        this.target(AlvarezsaurusEntity.class, DimetrodonEntity.class, TitanisEntity.class, SmilodonEntity.class, ArsinoitheriumEntity.class, ChilesaurusEntity.class, CoelurusEntity.class, CompsognathusEntity.class, DodoEntity.class, GallimimusEntity.class, HyaenodonEntity.class, HypsilophodonEntity.class, LeaellynasauraEntity.class, LeptictidiumEntity.class, MicroceratusEntity.class, MicroraptorEntity.class, MussaurusEntity.class, OrnithomimusEntity.class, OthnieliaEntity.class, OviraptorEntity.class, SegisaurusEntity.class, TroodonEntity.class, EntityPlayer.class, ProtoceratopsEntity.class, EntityAnimal.class, EntityVillager.class);
        this.doesEatEggs(true);
    }
    @Override
    public SoundEvent getSoundForAnimation(Animation animation)
    {
        switch (EntityAnimation.getAnimation(animation))
        {
            case SPEAK:
                return SoundHandler.SEGISAURUS_LIVING;
            case DYING:
                return SoundHandler.SEGISAURUS_DEATH;
            case INJURED:
                return SoundHandler.SEGISAURUS_HURT;
            case BEGGING:
                return SoundHandler.SEGISAURUS_THREAT;
        }

        return null;
    }
}
