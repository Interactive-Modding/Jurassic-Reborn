package mod.reborn.server.entity.dinosaur;

import mod.reborn.client.model.animation.EntityAnimation;
import mod.reborn.client.sound.SoundHandler;
import mod.reborn.server.entity.DinosaurEntity;
import net.ilexiconn.llibrary.server.animation.Animation;
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
        this.target(AlvarezsaurusEntity.class, GuanlongEntity.class, TitanisEntity.class, SmilodonEntity.class, ArsinoitheriumEntity.class, ChilesaurusEntity.class, CoelurusEntity.class, CompsognathusEntity.class, DodoEntity.class, GallimimusEntity.class, HyaenodonEntity.class, HypsilophodonEntity.class, LeaellynasauraEntity.class, LeptictidiumEntity.class, MicroceratusEntity.class, MicroraptorEntity.class, MussaurusEntity.class, OrnithomimusEntity.class, OthnieliaEntity.class, OviraptorEntity.class, SegisaurusEntity.class, TroodonEntity.class, EntityPlayer.class, ProtoceratopsEntity.class, EntityAnimal.class, EntityVillager.class);
        this.doesEatEggs(true);
    }
    @Override
    public SoundEvent getSoundForAnimation(Animation animation)
    {
        switch (EntityAnimation.getAnimation(animation))
        {
            case SPEAK:
                return SoundHandler.DIMETRODON_LIVING;
            case CALLING:
                return SoundHandler.DIMETRODON_LIVING;
            case DYING:
                return SoundHandler.DIMETRODON_ROAR;
            case INJURED:
                return SoundHandler.DIMETRODON_ROAR;
            case BEGGING:
                return SoundHandler.DIMETRODON_LIVING;
        }

        return null;
    }
}
