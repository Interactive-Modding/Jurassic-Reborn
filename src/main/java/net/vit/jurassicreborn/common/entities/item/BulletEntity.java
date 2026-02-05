package net.vit.jurassicreborn.common.entities.item;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;
import net.vit.jurassicreborn.common.entities.ModEntities;
import net.vit.jurassicreborn.common.items.ModItems;
import net.vit.jurassicreborn.common.items.guns.Bullet;

public class BulletEntity extends AbstractArrow implements IEntityAdditionalSpawnData {
    private ItemStack ammoStack = ItemStack.EMPTY;
    private int damage = 5;

    /**
     * Called by Forge/Minecraft when spawning from packets, etc.
     */
    public BulletEntity(EntityType<? extends BulletEntity> type, Level world) {
        super(type, world);
        // Optional: keep the old 0.2×0.2 size
        this.setBoundingBox(this.getBoundingBox().inflate(0.1D));
    }

    public BulletEntity(Level world, LivingEntity shooter, ItemStack ammo) {
        super((EntityType<? extends AbstractArrow>) ModEntities.BULLET_ENTITY.get(), shooter, world);
        this.ammoStack = ammo.copy();
        // We no longer call setItem(...). Instead, override getItem() below.
    }

    public void setDamage(int dmg) {
        this.damage = dmg;
    }



    @Override
    protected ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (!this.level.isClientSide) {
            if (result.getEntity() instanceof LivingEntity target &&
                    this.ammoStack.getItem() instanceof Bullet) {

                DamageSource src = this.damageSources().arrow(this, this.getOwner());
                target.hurt(src, this.damage);
            }
            this.discard(); // remove from world
        }
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (!this.level.isClientSide) {
            this.discard();
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.level.isClientSide && this.level instanceof ServerLevel serverLevel) {
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

    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {
        buffer.writeItem(ammoStack);
        buffer.writeInt(damage);
    }

    @Override
    public void readSpawnData(FriendlyByteBuf additionalData) {
        this.ammoStack = additionalData.readItem();
        this.damage = additionalData.readInt();
        // No more setItem(...) here; getItem() will return ammoStack automatically.
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

}