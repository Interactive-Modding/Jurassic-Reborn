package net.vit.jurassicreborn.common.entities.DinosaurEntities;

import net.vit.jurassicreborn.client.sounds.SoundHandler;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimation;
import net.vit.jurassicreborn.common.entities.DinosaurEntity;
import net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurHandler;
import net.minecraft.world.entity.animal.goat.Goat;
import com.github.alexthe666.citadel.animation.Animation;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;

public class TyrannosaurusEntity extends DinosaurEntity {
    private int stepCount = 0;
    public TyrannosaurusEntity(Level world, EntityType<TyrannosaurusEntity> type) {
        super(world, type, DinosaurHandler.TYRANNOSAURUS);
        this.target(AchillobatorEntity.class, ApatosaurusEntity.class, CamarasaurusEntity.class, AnkylodocusEntity.class, DiplodocusEntity.class, SpinoraptorEntity.class, TitanisEntity.class, SmilodonEntity.class, MegatheriumEntity.class, ElasmotheriumEntity.class, DeinotheriumEntity.class, ArsinoitheriumEntity.class, SinoceratopsEntity.class, AlligatorGarEntity.class, AllosaurusEntity.class, AlvarezsaurusEntity.class, AnkylosaurusEntity.class, ApatosaurusEntity.class, BaryonyxEntity.class, BeelzebufoEntity.class, VelociraptorBlueEntity.class, CarnotaurusEntity.class, CearadactylusEntity.class, CeratosaurusEntity.class, VelociraptorCharlieEntity.class, ChasmosaurusEntity.class, NigersaurusEntity.class, ChilesaurusEntity.class, KairukuEntity.class, CoelurusEntity.class, CompsognathusEntity.class, CorythosaurusEntity.class, MaiasauraEntity.class, CrassigyrinusEntity.class, VelociraptorDeltaEntity.class, DilophosaurusEntity.class, DimorphodonEntity.class, DiplocaulusEntity.class, DodoEntity.class, VelociraptorEchoEntity.class, EdmontosaurusEntity.class, GallimimusEntity.class, GiganotosaurusEntity.class, GuanlongEntity.class, HerrerasaurusEntity.class, HyaenodonEntity.class, HypsilophodonEntity.class, IndominusEntity.class, LambeosaurusEntity.class, LeaellynasauraEntity.class, LeptictidiumEntity.class, LudodactylusEntity.class, MajungasaurusEntity.class, MamenchisaurusEntity.class, MammothEntity.class, MegapiranhaEntity.class, MetriacanthosaurusEntity.class, MicroceratusEntity.class, MicroraptorEntity.class, MoganopterusEntity.class, MussaurusEntity.class, OrnithomimusEntity.class, OthnieliaEntity.class, OviraptorEntity.class, PachycephalosaurusEntity.class, ParasaurolophusEntity.class, PostosuchusEntity.class, DeinosuchusEntity.class, ProceratosaurusEntity.class, ProtoceratopsEntity.class, PteranodonEntity.class, QuetzalEntity.class, RugopsEntity.class, SegisaurusEntity.class, SpinosaurusEntity.class, StegosaurusEntity.class, StyracosaurusEntity.class, SuchomimusEntity.class, TherizinosaurusEntity.class, TriceratopsEntity.class, TroodonEntity.class, TropeognathusEntity.class , VelociraptorEntity.class, ZhenyuanopterusEntity.class, Goat.class, Player.class
, Animal.class, Villager.class);
                this.addTask(1, new HurtByTargetGoal(this));

    }
    @Override
    public void tick() {
        super.tick();

        if (this.onGround() && this.zza > 0 && this.stepCount <= 0) {
            this.playSound(SoundHandler.STOMP, (float) interpolate(0.1F, 1.0F), this.getVoicePitch());
            this.stepCount = 65;
        }

        this.stepCount -= this.zza * 9.5;
    }

    @Override
    public SoundEvent getSoundForAnimation(Animation animation) {
        switch (EntityAnimation.getAnimation(animation)) {
            case SPEAK:
                return SoundHandler.TYRANNOSAURUS_LIVING;
            case CALLING:
                return SoundHandler.TYRANNOSAURUS_ROAR;
            case ROARING:
                return SoundHandler.TYRANNOSAURUS_ROAR;
            case DYING:
                return SoundHandler.TYRANNOSAURUS_DEATH;
            case INJURED:
                return SoundHandler.TYRANNOSAURUS_HURT;
            default:
                return null;
        }
    }
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createLivingAttributes().add(Attributes.FOLLOW_RANGE, 35.0D);
    }



    @Override
    public SoundEvent getBreathingSound() {
        return SoundHandler.TYRANNOSAURUS_BREATHING;
    }

}

