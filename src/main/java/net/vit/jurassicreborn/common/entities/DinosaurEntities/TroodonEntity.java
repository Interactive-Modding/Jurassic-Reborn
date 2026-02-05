package net.vit.jurassicreborn.common.entities.DinosaurEntities;

import com.github.alexthe666.citadel.animation.Animation;
import net.vit.jurassicreborn.client.sounds.SoundHandler;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimation;
import net.vit.jurassicreborn.common.entities.DinosaurEntity;
import net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurHandler;
import net.vit.jurassicreborn.common.entities.ai.HurtByTargetGoal;
import net.vit.jurassicreborn.common.entities.ai.LeapingMeleeEntityAI;
import net.vit.jurassicreborn.common.entities.ai.RaptorLeapEntityAI;
import net.minecraft.world.entity.animal.goat.Goat;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;

public class TroodonEntity extends DinosaurEntity
{
    private static final Class[] targets = {CompsognathusEntity.class, HyaenodonEntity.class, Player.class
, DilophosaurusEntity.class, DimorphodonEntity.class, DodoEntity.class, LeaellynasauraEntity.class, HypsilophodonEntity.class, StegosaurusEntity.class, ProtoceratopsEntity.class, OthnieliaEntity.class, MicroceratusEntity.class};

    public TroodonEntity(Level world, EntityType<TroodonEntity> type)
    {
        super(world, type, DinosaurHandler.TROODON);
        this.target(AlligatorGarEntity.class, AchillobatorEntity.class, AlvarezsaurusEntity.class, BeelzebufoEntity.class, VelociraptorBlueEntity.class, VelociraptorCharlieEntity.class, VelociraptorDeltaEntity.class, VelociraptorEchoEntity.class, ChilesaurusEntity.class, KairukuEntity.class, CoelurusEntity.class, CompsognathusEntity.class, CrassigyrinusEntity.class, Player.class
, DilophosaurusEntity.class, DimorphodonEntity.class, DiplocaulusEntity.class, DodoEntity.class, GallimimusEntity.class, GuanlongEntity.class, HyaenodonEntity.class, HypsilophodonEntity.class, LeaellynasauraEntity.class, LeptictidiumEntity.class, MicroceratusEntity.class, MussaurusEntity.class, MicroraptorEntity.class, OrnithomimusEntity.class, OthnieliaEntity.class, OviraptorEntity.class, ProceratosaurusEntity.class, ProtoceratopsEntity.class, SegisaurusEntity.class, ZhenyuanopterusEntity.class, MoganopterusEntity.class, Player.class
, Animal.class, Villager.class, Goat.class);
        this.addTask(0, new LeapingMeleeEntityAI(this, this.dinosaur.getAttackSpeed()));
        this.addTask(1, new RaptorLeapEntityAI(this));
        this.target(targets);
        for(Class entity : targets) {
            this.addTask(0, new NearestAttackableTargetGoal<LivingEntity>(this, entity, true, false));
        }
        this.addTask(1, new HurtByTargetGoal(this, Player.class
                , TyrannosaurusEntity.class, GiganotosaurusEntity.class, SpinosaurusEntity.class));
    }
//    @Override
//    public EntityAIBase getAttackAI() {
//        return new RaptorLeapEntityAI(this);
//    }

    @Override
    public int calculateFallDamage(float distance, float damageMultiplier) {
        if (this.getAnimation() != EntityAnimation.LEAP_LAND.get()) {
            return super.calculateFallDamage(distance, damageMultiplier);
        }
        return 0;
    }

    public boolean doHurtTarget(Entity entity){
        float damage = (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE);
        if (entity.hurt(this.damageSources().mobAttack(this), damage)) {
            if (entity instanceof LivingEntity) {
                int i = random.nextInt(3);
                if(i == 0) {
                    ((LivingEntity) entity).addEffect(new MobEffectInstance(MobEffects.POISON, 15, 10));
                } else if(i == 1) {
                    ((LivingEntity) entity).addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 15, 10));
                }else if(i == 2) {
                    ((LivingEntity) entity).addEffect(new MobEffectInstance(MobEffects.WITHER, 15, 10));
                }
                return true;
            }
        }
        return super.doHurtTarget(entity);
    }
    @Override
    public SoundEvent getSoundForAnimation(Animation animation) {
        switch (EntityAnimation.getAnimation(animation)) {
            case SPEAK:
                return SoundHandler.TROODON_LIVING;
            case DYING:
                return SoundHandler.TROODON_DEATH;
            case INJURED:
                return SoundHandler.TROODON_HURT;
            case CALLING:
                return SoundHandler.TROODON_CALL;
            case BEGGING:
                return SoundHandler.TROODON_THREAT;
        }

        return null;
    }
}

