package net.vit.jurassicreborn.common.entities.DinosaurEntities;

import com.github.alexthe666.citadel.animation.Animation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.goat.Goat;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimation;
import net.vit.jurassicreborn.client.sounds.SoundHandler;
import net.vit.jurassicreborn.common.entities.CrocodileDinosaurEntity;
import net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurHandler;

public class DeinosuchusEntity extends CrocodileDinosaurEntity {

    public DeinosuchusEntity(EntityType<DeinosuchusEntity> type, Level world) {
        super(world, type, DinosaurHandler.DEINOSUCHUS);
        this.target(AchillobatorEntity.class, SpinoraptorEntity.class, TitanisEntity.class, SmilodonEntity.class, MegatheriumEntity.class, ArsinoitheriumEntity.class, AlligatorGarEntity.class, AlvarezsaurusEntity.class, BeelzebufoEntity.class, VelociraptorBlueEntity.class, VelociraptorCharlieEntity.class, ChilesaurusEntity.class, KairukuEntity.class, CoelurusEntity.class, CompsognathusEntity.class, CrassigyrinusEntity.class, DimorphodonEntity.class, DiplocaulusEntity.class, DodoEntity.class, HypsilophodonEntity.class, LeaellynasauraEntity.class, LeptictidiumEntity.class, MegapiranhaEntity.class, MicroceratusEntity.class, MicroraptorEntity.class, MussaurusEntity.class, OthnieliaEntity.class, OviraptorEntity.class, ProtoceratopsEntity.class, SegisaurusEntity.class, TroodonEntity.class, VelociraptorEchoEntity.class, DilophosaurusEntity.class, VelociraptorDeltaEntity.class, HyaenodonEntity.class, OrnithomimusEntity.class, GuanlongEntity.class, MetriacanthosaurusEntity.class, ProceratosaurusEntity.class, RugopsEntity.class, VelociraptorEntity.class, Player.class, Animal.class, Villager.class, Goat.class );
    }

    @Override
    public SoundEvent getSoundForAnimation(Animation animation) {
        switch (EntityAnimation.getAnimation(animation)) {
            case CALLING:
                return SoundHandler.DEINOSUCHUS_CALL;
            case SPEAK:
                return SoundHandler.DEINOSUCHUS_LIVING;
            case DYING:
                return SoundHandler.DEINOSUCHUS_DEATH;
            case INJURED:
                return SoundHandler.DEINOSUCHUS_INJURED;
            case ATTACKING:
                return SoundHandler.DEINOSUCHUS_ATTACK;
            case DEATH_ROLL:
                return SoundHandler.DEINOSUCHUS_DEATH_ROLL;
            default:
                return null;
        }
    }
}

