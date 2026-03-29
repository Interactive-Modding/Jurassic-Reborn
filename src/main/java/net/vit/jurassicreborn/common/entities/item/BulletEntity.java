package net.vit.jurassicreborn.common.entities.item;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.server.level.ServerLevel;
import net.vit.jurassicreborn.common.entities.ModEntities;
import net.vit.jurassicreborn.common.items.ModItems;
import net.vit.jurassicreborn.common.items.guns.Bullet;

public class BulletEntity extends AbstractArrow {
    private static final EntityDataAccessor<ItemStack> AMMO_STACK =
            SynchedEntityData.defineId(BulletEntity.class, EntityDataSerializers.ITEM_STACK);
    private static final EntityDataAccessor<Integer> BULLET_DAMAGE =
            SynchedEntityData.defineId(BulletEntity.class, EntityDataSerializers.INT);

    /**
     * Called by Forge/Minecraft when spawning from packets, etc.
     */
    public BulletEntity(EntityType<? extends BulletEntity> type, Level world) {
        super(type, world);
        this.setBoundingBox(this.getBoundingBox().inflate(0.1D));
    }

    public BulletEntity(Level world, LivingEntity shooter, ItemStack ammo) {
        super(ModEntities.BULLET_ENTITY.get(), world);
        this.setOwner(shooter);
        this.setAmmoStack(ammo.copy());
    }


    /** If your Gun wants to change the damage: */
    public void setDamage(int dmg) {
        this.entityData.set(BULLET_DAMAGE, dmg);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(AMMO_STACK, ItemStack.EMPTY);
        builder.define(BULLET_DAMAGE, 5);
    }


    @Override
    protected ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return ItemStack.EMPTY;
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (!this.level().isClientSide) {
            if (result.getEntity() instanceof LivingEntity target &&
                    this.getAmmoStack().getItem() instanceof Bullet) {

                DamageSource src = this.damageSources().arrow(this, this.getOwner());
                target.hurt(src, this.entityData.get(BULLET_DAMAGE));
            }
            this.discard(); // remove from world
        }
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (!this.level().isClientSide) {
            this.discard();
        }
    }

    @Override
    public void tick() {
        super.tick();

        Level level = this.level();
        if (!this.level().isClientSide && level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(
                    ParticleTypes.SMOKE,
                    this.getX(),
                    this.getY(),
                    this.getZ(),
                    1,      // count
                    0.0,    // dx
                    0.0,    // dy
                    0.0,    // dz
                    0.0     // speed
            );
        }
    }



    private ItemStack getAmmoStack() {
        return this.entityData.get(AMMO_STACK);
    }

    private void setAmmoStack(ItemStack stack) {
        this.entityData.set(AMMO_STACK, stack);
    }
}
