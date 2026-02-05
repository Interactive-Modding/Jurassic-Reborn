package net.vit.jurassicreborn.common.entities.DinosaurEntities;

import com.github.alexthe666.citadel.animation.Animation;
import net.vit.jurassicreborn.client.sounds.SoundHandler;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimation;
import net.vit.jurassicreborn.common.entities.DinosaurEntity;
import net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurHandler;
import net.vit.jurassicreborn.common.entities.ai.HurtByTargetGoal;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;

public class HerrerasaurusEntity extends DinosaurEntity
{
    public HerrerasaurusEntity(EntityType<HerrerasaurusEntity> type, Level world)
    {
        super(world, type, DinosaurHandler.HERRERASAURUS);
        this.target(AchillobatorEntity.class, SpinoraptorEntity.class, SmilodonEntity.class, TitanisEntity.class, MegatheriumEntity.class, ArsinoitheriumEntity.class, AlligatorGarEntity.class, AlvarezsaurusEntity.class, BeelzebufoEntity.class, VelociraptorBlueEntity.class, VelociraptorCharlieEntity.class, ChasmosaurusEntity.class, NigersaurusEntity.class, ChilesaurusEntity.class, KairukuEntity.class, CoelurusEntity.class, CompsognathusEntity.class, CrassigyrinusEntity.class, VelociraptorDeltaEntity.class, DilophosaurusEntity.class, DodoEntity.class, DiplocaulusEntity.class, VelociraptorEchoEntity.class, GallimimusEntity.class, GuanlongEntity.class, HyaenodonEntity.class, HypsilophodonEntity.class, LeaellynasauraEntity.class, LeptictidiumEntity.class, MegapiranhaEntity.class, MetriacanthosaurusEntity.class, MicroceratusEntity.class, MicroraptorEntity.class, MussaurusEntity.class, OrnithomimusEntity.class, OthnieliaEntity.class, OviraptorEntity.class, PostosuchusEntity.class, DeinosuchusEntity.class, ProtoceratopsEntity.class, SegisaurusEntity.class, TroodonEntity.class, VelociraptorEntity.class, PachycephalosaurusEntity.class, Player.class
, Animal.class, Villager.class);
        this.addTask(1, new HurtByTargetGoal(this,  Player.class
, TyrannosaurusEntity.class, GiganotosaurusEntity.class, SpinosaurusEntity.class));

    }

    @Override
    public float getSoundVolume()
    {
        return (float) interpolate(1.3F, 2.0F);
    }

    @Override
    public SoundEvent getSoundForAnimation(Animation animation)
    {
        switch (EntityAnimation.getAnimation(animation))
        {
            case SPEAK:
                return SoundHandler.HERRERASAURUS_LIVING;
            case DYING:
                return SoundHandler.HERRERASAURUS_DEATH;
            case INJURED:
                return SoundHandler.HERRERASAURUS_HURT;
            case CALLING:
                return SoundHandler.HERRERASAURUS_CALL;
            case ROARING:
                return SoundHandler.HERRERASAURUS_ROAR;
            case BEGGING:
                return SoundHandler.HERRERASAURUS_THREAT;
        }

        return null;
    }
}

