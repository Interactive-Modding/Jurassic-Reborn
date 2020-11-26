package mod.reborn.server.entity.dinosaur;

import mod.reborn.client.model.animation.EntityAnimation;
import mod.reborn.client.sound.SoundHandler;
import mod.reborn.server.entity.FlyingDinosaurEntity;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class ZhenyuanopterusEntity extends FlyingDinosaurEntity
{
    public ZhenyuanopterusEntity(World world)
    {
        super(world);
        this.target(AlligatorGarEntity.class, EntitySquid.class, AlvarezsaurusEntity.class, BeelzebufoEntity.class, CoelurusEntity.class, CompsognathusEntity.class, CoelacanthEntity.class, GuanlongEntity.class, CrassigyrinusEntity.class, DimorphodonEntity.class, DodoEntity.class, DiplocaulusEntity.class, MoganopterusEntity.class, HypsilophodonEntity.class, LeaellynasauraEntity.class, LeptictidiumEntity.class, LudodactylusEntity.class, MegapiranhaEntity.class, MicroceratusEntity.class, MicroraptorEntity.class, MussaurusEntity.class, OthnieliaEntity.class, OviraptorEntity.class, ProtoceratopsEntity.class, SegisaurusEntity.class, TroodonEntity.class, EntityPlayer.class, EntityAnimal.class, EntityVillager.class);
    }
    @Override
    public SoundEvent getSoundForAnimation(Animation animation)
    {
        switch (EntityAnimation.getAnimation(animation))
        {
            case SPEAK:
                return SoundHandler.ZHENYUANOPTERUS_LIVING;
            case DYING:
                return SoundHandler.ZHENYUANOPTERUS_DEATH;
            case INJURED:
                return SoundHandler.ZHENYUANOPTERUS_HURT;
            case ROARING:
                return SoundHandler.ZHENYUANOPTERUS_ROAR;
            case BEGGING:
                return SoundHandler.ZHENYUANOPTERUS_THREAT;
        }

        return null;
    }
}
