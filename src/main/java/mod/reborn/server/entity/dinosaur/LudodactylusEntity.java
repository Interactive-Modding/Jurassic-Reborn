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

public class LudodactylusEntity extends FlyingDinosaurEntity
{
    public LudodactylusEntity(World world)
    {
        super(world);
        this.target(AlligatorGarEntity.class, AlvarezsaurusEntity.class, BeelzebufoEntity.class, CearadactylusEntity.class, ChilesaurusEntity.class, CoelacanthEntity.class, CoelurusEntity.class, CompsognathusEntity.class, CrassigyrinusEntity.class, DimorphodonEntity.class, DiplocaulusEntity.class, DodoEntity.class, HypsilophodonEntity.class, LeaellynasauraEntity.class, LeptictidiumEntity.class, MegapiranhaEntity.class, MicroceratusEntity.class, MicroraptorEntity.class, MoganopterusEntity.class, MussaurusEntity.class, OthnieliaEntity.class, OviraptorEntity.class, ProtoceratopsEntity.class, SegisaurusEntity.class, TroodonEntity.class, TropeognathusEntity.class, ZhenyuanopterusEntity.class, EntityPlayer.class, EntityAnimal.class, EntityVillager.class);
    }
    @Override
    public SoundEvent getSoundForAnimation(Animation animation)
    {
        switch (EntityAnimation.getAnimation(animation))
        {
            case SPEAK:
                return SoundHandler.LUDODACTYLUS_LIVING;
            case DYING:
                return SoundHandler.LUDODACTYLUS_DEATH;
            case INJURED:
                return SoundHandler.LUDODACTYLUS_HURT;
            case CALLING:
                return SoundHandler.LUDODACTYLUS_CALL;
            case BEGGING:
                return SoundHandler.LUDODACTYLUS_THREAT;
        }

        return null;
    }
}
