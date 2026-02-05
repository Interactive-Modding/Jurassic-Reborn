package net.vit.jurassicreborn.common.entities.DinosaurEntities;

import com.github.alexthe666.citadel.animation.Animation;
import net.vit.jurassicreborn.client.sounds.SoundHandler;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimation;
import net.vit.jurassicreborn.common.entities.DinosaurEntity;
import net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurHandler;
import net.minecraft.world.entity.animal.goat.Goat;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;

public class PostosuchusEntity extends DinosaurEntity {

    public PostosuchusEntity(EntityType<PostosuchusEntity> type, Level world) {
        super(world, type, DinosaurHandler.POSTOSUCHUS);
        this.target(AchillobatorEntity.class, SpinoraptorEntity.class, TitanisEntity.class, SmilodonEntity.class, MegatheriumEntity.class, ArsinoitheriumEntity.class, AlligatorGarEntity.class, AlvarezsaurusEntity.class, BeelzebufoEntity.class, VelociraptorBlueEntity.class, VelociraptorCharlieEntity.class, ChilesaurusEntity.class, KairukuEntity.class, CoelurusEntity.class, CompsognathusEntity.class, CrassigyrinusEntity.class, DimorphodonEntity.class, DiplocaulusEntity.class, DodoEntity.class, HypsilophodonEntity.class, LeaellynasauraEntity.class, LeptictidiumEntity.class, MegapiranhaEntity.class, MicroceratusEntity.class, MicroraptorEntity.class, MussaurusEntity.class, OthnieliaEntity.class, OviraptorEntity.class, ProtoceratopsEntity.class, SegisaurusEntity.class, TroodonEntity.class, VelociraptorEchoEntity.class, DilophosaurusEntity.class, VelociraptorDeltaEntity.class, HyaenodonEntity.class, OrnithomimusEntity.class, GuanlongEntity.class, MetriacanthosaurusEntity.class, ProceratosaurusEntity.class, RugopsEntity.class, VelociraptorEntity.class, Player.class, Animal.class, Villager.class, Goat.class );
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

