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

public class QuetzalEntity extends FlyingDinosaurEntity
{
    public QuetzalEntity(World world)
    {
        super(world);
        this.target(AlligatorGarEntity.class, DimetrodonEntity.class, SmilodonEntity.class, TitanisEntity.class, MegatheriumEntity.class, ElasmotheriumEntity.class, DeinotheriumEntity.class, ArsinoitheriumEntity.class, MawsoniaEntity.class, AlvarezsaurusEntity.class, BeelzebufoEntity.class, CearadactylusEntity.class, CoelacanthEntity.class, CoelurusEntity.class, CompsognathusEntity.class, CrassigyrinusEntity.class, DimorphodonEntity.class, DiplocaulusEntity.class, DodoEntity.class, GuanlongEntity.class, HypsilophodonEntity.class, LeaellynasauraEntity.class, LeptictidiumEntity.class, LudodactylusEntity.class, MicroceratusEntity.class, MicroraptorEntity.class, MoganopterusEntity.class, MussaurusEntity.class, OthnieliaEntity.class, OviraptorEntity.class, ProceratosaurusEntity.class, ProtoceratopsEntity.class, TroodonEntity.class, SegisaurusEntity.class, ZhenyuanopterusEntity.class, PteranodonEntity.class, TropeognathusEntity.class, EntityPlayer.class, EntityAnimal.class, EntityVillager.class);
    }

    @Override
    public SoundEvent getSoundForAnimation(Animation animation)
    {
        switch (EntityAnimation.getAnimation(animation))
        {
            case SPEAK:
                return SoundHandler.QUETZALCOATLUS_LIVING;
            case CALLING:
                return SoundHandler.QUETZALCOATLUS_LIVING;
            case DYING:
                return SoundHandler.QUETZALCOATLUS_DEATH;
            case INJURED:
                return SoundHandler.QUETZALCOATLUS_HURT;
            case BEGGING:
                return SoundHandler.QUETZALCOATLUS_THREAT;
        }

        return null;
    }
}
