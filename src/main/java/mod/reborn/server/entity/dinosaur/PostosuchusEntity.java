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

public class PostosuchusEntity extends DinosaurEntity {

    public PostosuchusEntity(World world) {
        super(world);
        this.target(AchillobatorEntity.class, SpinoraptorEntity.class, TitanisEntity.class, SmilodonEntity.class, MegatheriumEntity.class, ArsinoitheriumEntity.class, AlligatorGarEntity.class, AlvarezsaurusEntity.class, BeelzebufoEntity.class, VelociraptorBlueEntity.class, VelociraptorCharlieEntity.class, ChilesaurusEntity.class, CoelurusEntity.class, CompsognathusEntity.class, CrassigyrinusEntity.class, DimorphodonEntity.class, DiplocaulusEntity.class, DodoEntity.class, HypsilophodonEntity.class, LeaellynasauraEntity.class, LeptictidiumEntity.class, MegapiranhaEntity.class, MicroceratusEntity.class, MicroraptorEntity.class, MussaurusEntity.class, OthnieliaEntity.class, OviraptorEntity.class, ProtoceratopsEntity.class, SegisaurusEntity.class, TroodonEntity.class, VelociraptorEchoEntity.class, DilophosaurusEntity.class, VelociraptorDeltaEntity.class, HyaenodonEntity.class, OrnithomimusEntity.class, GuanlongEntity.class, MetriacanthosaurusEntity.class, ProceratosaurusEntity.class, RugopsEntity.class, VelociraptorEntity.class,EntityPlayer.class, EntityAnimal.class, EntityVillager.class, GoatEntity.class);
    }

    @Override
    public SoundEvent getSoundForAnimation(Animation animation) {
        switch (EntityAnimation.getAnimation(animation)) {
            case CALLING:
                return SoundHandler.POSTOSUCHUS_CALL;
            case DYING:
                return SoundHandler.POSTOSUCHUS_DEATH;
            case INJURED:
                return SoundHandler.POSTOSUCHUS_ATTACK;
            case ATTACKING:
                return SoundHandler.POSTOSUCHUS_ATTACK;
            default:
                return null;
        }
    }
}
