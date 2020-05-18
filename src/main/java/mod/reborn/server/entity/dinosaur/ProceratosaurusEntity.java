package mod.reborn.server.entity.dinosaur;

import mod.reborn.client.model.animation.EntityAnimation;
import mod.reborn.client.sound.SoundHandler;
import mod.reborn.server.entity.DinosaurEntity;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class ProceratosaurusEntity extends DinosaurEntity {

    public ProceratosaurusEntity(World world) {
        super(world);
        this.target(AlligatorGarEntity.class, AchillobatorEntity.class, AlvarezsaurusEntity.class, BeelzebufoEntity.class, VelociraptorBlueEntity.class, VelociraptorCharlieEntity.class, ChilesaurusEntity.class, CoelurusEntity.class, CompsognathusEntity.class, CrassigyrinusEntity.class, VelociraptorDeltaEntity.class, DilophosaurusEntity.class, DimorphodonEntity.class, DiplocaulusEntity.class, DodoEntity.class, VelociraptorEchoEntity.class, GuanlongEntity.class, HyaenodonEntity.class, HypsilophodonEntity.class, LeaellynasauraEntity.class, LeptictidiumEntity.class, MegapiranhaEntity.class, MicroceratusEntity.class, MicroraptorEntity.class, MussaurusEntity.class, OthnieliaEntity.class, OviraptorEntity.class, ProtoceratopsEntity.class, SegisaurusEntity.class, TroodonEntity.class, EntityPlayer.class, VelociraptorEntity.class);
    }

    public SoundEvent getSoundForAnimation(Animation animation) {
        switch (EntityAnimation.getAnimation(animation)) {
            case SPEAK:
                return SoundHandler.PROCERATOSAURUS_LIVING;
            case CALLING:
                return SoundHandler.PROCERATOSAURUS_MATE_CALL;
            case DYING:
                return SoundHandler.PROCERATOSAURUS_DEATH;
            case INJURED:
                return SoundHandler.PROCERATOSAURUS_HURT;
            case ATTACKING:
                return SoundHandler.PROCERATOSAURUS_ATTACK;
            case BEGGING:
                return SoundHandler.PROCERATOSAURUS_THREAT;
            default:
                return null;
        }
    }
}
