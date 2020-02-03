package mod.reborn.server.entity;

import mod.reborn.client.proxy.ClientProxy;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import mod.reborn.server.entity.dinosaur.DilophosaurusEntity;

public class VenomEntity extends EntityThrowable {
    public VenomEntity(World world) {
        super(world);

        if (world.isRemote) {
            this.spawnParticles();
        }
    }

    public VenomEntity(World world, DilophosaurusEntity entity) {
        super(world, entity);

        if (world.isRemote) {
            this.spawnParticles();
        }
    }

    @Override
    protected void onImpact(RayTraceResult result) {
        EntityLivingBase thrower = this.getThrower();

        if (thrower instanceof DilophosaurusEntity) {
            DilophosaurusEntity spitter = (DilophosaurusEntity) thrower;

            if (result.entityHit != null && result.entityHit instanceof EntityLivingBase && result.entityHit != spitter && (result.entityHit == spitter.getAttackTarget() || !(result.entityHit instanceof DilophosaurusEntity))) {
                EntityLivingBase entityHit = (EntityLivingBase) result.entityHit;

                entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), 4.0F);
                entityHit.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 300, 1, false, false));
                entityHit.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 300, 1, false, false));

                if (!this.world.isRemote) {
                    this.setDead();
                }
            }
        }
    }

    private void spawnParticles() {
        ClientProxy.spawnVenomParticles(this);
    }
}
