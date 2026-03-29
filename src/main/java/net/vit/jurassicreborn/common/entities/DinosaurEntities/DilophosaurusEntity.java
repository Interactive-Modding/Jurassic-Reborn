package net.vit.jurassicreborn.common.entities.DinosaurEntities;

import com.github.alexthe666.citadel.animation.Animation;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.goat.Goat;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimation;
import net.vit.jurassicreborn.client.sounds.SoundHandler;
import net.vit.jurassicreborn.common.entities.DinosaurEntity;
import net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurHandler;
import net.vit.jurassicreborn.common.entities.ModEntities;
import net.vit.jurassicreborn.common.entities.VenomEntity;
import net.vit.jurassicreborn.common.entities.ai.DilophosaurusSpitGoal;
import net.vit.jurassicreborn.common.entities.ai.DinosaurAttackMeleeEntityAI;

import java.util.EnumSet;

public class DilophosaurusEntity extends DinosaurEntity implements RangedAttackMob {

    private static final EntityDataAccessor<Boolean> WATCHER_HAS_TARGET =
            SynchedEntityData.defineId(DilophosaurusEntity.class, EntityDataSerializers.BOOLEAN);

    private int targetCooldown;

    public DilophosaurusEntity(EntityType<DilophosaurusEntity> type, Level world) {
        super(world, type, DinosaurHandler.DILOPHOSAURUS);
        this.target(Goat.class, SmilodonEntity.class, MegatheriumEntity.class, ArsinoitheriumEntity.class,
                SpinoraptorEntity.class, Player.class, Villager.class, Animal.class, AchillobatorEntity.class,
                AlligatorGarEntity.class, AlvarezsaurusEntity.class, BeelzebufoEntity.class,
                VelociraptorBlueEntity.class, VelociraptorCharlieEntity.class, ChasmosaurusEntity.class,
                NigersaurusEntity.class, ChilesaurusEntity.class, KairukuEntity.class, CoelurusEntity.class,
                CompsognathusEntity.class, CrassigyrinusEntity.class, VelociraptorDeltaEntity.class, DodoEntity.class,
                DiplocaulusEntity.class, VelociraptorEchoEntity.class, GallimimusEntity.class, GuanlongEntity.class,
                HyaenodonEntity.class, HypsilophodonEntity.class, LeaellynasauraEntity.class, LeptictidiumEntity.class,
                MegapiranhaEntity.class, MetriacanthosaurusEntity.class, MicroceratusEntity.class, MicroraptorEntity.class,
                MussaurusEntity.class, OrnithomimusEntity.class, OthnieliaEntity.class, OviraptorEntity.class,
                PostosuchusEntity.class, DeinosuchusEntity.class, ProceratosaurusEntity.class, ProtoceratopsEntity.class,
                SegisaurusEntity.class, TroodonEntity.class, VelociraptorEntity.class, PachycephalosaurusEntity.class);
        this.addTask(1, new DilophosaurusMeleeEntityAI(this, this.dinosaur.getAttackSpeed()));
        this.goalSelector.addGoal(0, new DilophosaurusSpitGoal(this, 1.2D, 25, 45, 14.0F));
    }

    @Override
    public void performRangedAttack(LivingEntity target, float power) {
        if (target instanceof Player p && p.isCreative()) return;
        if (this.level().isClientSide) return;

        final double baseX = this.getX();
        final double baseY = this.getEyeY() - 0.12D;
        final double baseZ = this.getZ();

        final float bodyYaw = this.getYRot() * ((float)Math.PI / 180F);
        final float sin = Mth.sin(bodyYaw);
        final float cos = Mth.cos(bodyYaw);
        final double forward = 0.8D;
        final double mouthDrop = 0.10D;

        final double spawnX = baseX + (-sin * forward);
        final double spawnY = baseY - mouthDrop;
        final double spawnZ = baseZ + ( cos * forward);

        final double dx = target.getX() - spawnX;
        final double dz = target.getZ() - spawnZ;
        final double horiz = Math.sqrt(dx*dx + dz*dz);
        final double dy = (target.getEyeY() - spawnY) + (horiz * 0.12D);

        final VenomEntity venom = new VenomEntity(ModEntities.VENOM.get(), this.level(), this);
        venom.setOwner(this);
        venom.setPos(spawnX, spawnY, spawnZ);

        final float velocity = 1.85F + 0.25F * Mth.clamp(power, 0.1F, 1.0F);
        final float inaccuracy = 0.08F;

        venom.shoot(dx, dy, dz, velocity, inaccuracy);
        venom.setNoGravity(false);

        this.level().addFreshEntity(venom);

//        if (this.level() instanceof ServerLevel sl) {
//            sl.sendParticles(
//                    ParticleTypes.ITEM_SLIME,
//                    spawnX, spawnY, spawnZ,
//                    10, 0.05D, 0.02D, 0.05D, 0.01D
//            );
//        }
    }

    @Override
    public void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(WATCHER_HAS_TARGET, false);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide) {
            final LivingEntity current = this.getTarget();
            if (current != null && current.isAlive() && this.targetCooldown < 50) {
                this.targetCooldown = 50 + this.getRandom().nextInt(30);
            } else if (this.targetCooldown > 0) {
                this.targetCooldown--;
            }
            this.entityData.set(WATCHER_HAS_TARGET, this.hasTarget());
        }
    }

    @Override
    public boolean doHurtTarget(@NotNull Entity entity) {
        if (super.doHurtTarget(entity)) {
            if (entity instanceof LivingEntity living) {
                living.addEffect(new MobEffectInstance(MobEffects.POISON, 20 * 15, 1, false, false));
            }
            return true;
        }
        return false;
    }

    public boolean hasTarget() {
        if (this.isCarcass() || this.isSleeping()) return false;
        if (this.level().isClientSide) {
            return this.entityData.get(WATCHER_HAS_TARGET);
        } else {
            final LivingEntity t = this.getTarget();
            return (t != null && t.isAlive()) || this.targetCooldown > 0;
        }
    }

    @Override
    public SoundEvent getSoundForAnimation(Animation animation) {
        switch (EntityAnimation.getAnimation(animation)) {
            case SPEAK:
            case CALLING:
                return SoundHandler.DILOPHOSAURUS_LIVING;
            case DYING:
                return SoundHandler.DILOPHOSAURUS_DEATH;
            case INJURED:
                return SoundHandler.DILOPHOSAURUS_HURT;
            case ATTACKING:
                return SoundHandler.DILOPHOSAURUS_SPIT;
            default:
                return null;
        }
    }

    public class DilophosaurusMeleeEntityAI extends DinosaurAttackMeleeEntityAI {
        public DilophosaurusMeleeEntityAI(DilophosaurusEntity entity, double speed) {
            super(entity, speed, false);
            this.setFlags(EnumSet.of(Flag.TARGET));
        }

        @Override
        public boolean canUse() {
            LivingEntity target = DilophosaurusEntity.this.getTarget();
            return super.canUse()
                    && target != null
                    && target.isAlive()
                    && target.getHealth() < target.getMaxHealth() * 0.9F
                    && target.hasEffect(MobEffects.BLINDNESS);
        }
    }
}
