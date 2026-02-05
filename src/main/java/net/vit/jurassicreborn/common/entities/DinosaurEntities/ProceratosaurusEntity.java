package net.vit.jurassicreborn.common.entities.DinosaurEntities;

import com.github.alexthe666.citadel.animation.Animation;
import net.minecraft.world.entity.animal.goat.Goat;
import net.vit.jurassicreborn.client.sounds.SoundHandler;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimation;
import net.vit.jurassicreborn.common.entities.DinosaurEntity;
import net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurHandler;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;

public class ProceratosaurusEntity extends DinosaurEntity {

    public ProceratosaurusEntity(EntityType<ProceratosaurusEntity> type, Level world) {
        super(world, type, DinosaurHandler.PROCERATOSAURUS);
        this.target(AlligatorGarEntity.class, SmilodonEntity.class, OrnithomimusEntity.class, AchillobatorEntity.class, AlvarezsaurusEntity.class, BeelzebufoEntity.class, VelociraptorBlueEntity.class, VelociraptorCharlieEntity.class, ChilesaurusEntity.class, KairukuEntity.class, CoelurusEntity.class, CompsognathusEntity.class, CrassigyrinusEntity.class, VelociraptorDeltaEntity.class, DilophosaurusEntity.class, DimorphodonEntity.class, DiplocaulusEntity.class, DodoEntity.class, VelociraptorEchoEntity.class, GuanlongEntity.class, HyaenodonEntity.class, HypsilophodonEntity.class, LeaellynasauraEntity.class, LeptictidiumEntity.class, MegapiranhaEntity.class, MicroceratusEntity.class, MicroraptorEntity.class, MussaurusEntity.class, OthnieliaEntity.class, OviraptorEntity.class, ProtoceratopsEntity.class, SegisaurusEntity.class, TroodonEntity.class, Player.class
, Animal.class, Villager.class, Goat.class, VelociraptorEntity.class);
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

