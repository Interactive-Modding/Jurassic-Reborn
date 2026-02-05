package net.vit.jurassicreborn.common.entities;

import net.minecraft.network.protocol.Packet;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import net.vit.jurassicreborn.client.JurassicClient;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.DilophosaurusEntity;

import java.util.UUID;

public class VenomEntity extends Projectile {

    public static final double GRAVITY = 0.0075D;
    public static final double DRAG = 0.99D;
    public static final int MAX_LIFETIME = 200;
    public static final int OWNER_GRACE_TICKS = 6;

    private UUID ownerIdCache;

    public VenomEntity(EntityType<? extends VenomEntity> type, Level level) {
        super(type, level);
        this.noCulling = true;
    }

    public VenomEntity(EntityType<? extends VenomEntity> type, Level level, LivingEntity owner) {
        this(type, level);
        this.setOwner(owner);
        if (owner != null) this.ownerIdCache = owner.getUUID();
    }

    @Override
    protected void defineSynchedData() {}

    @Override
    public void tick() {
        super.tick();
        if (!level.isClientSide && this.tickCount > MAX_LIFETIME) {
            this.discard();
            return;
        }

        Vec3 start = this.position();
        Vec3 motion = this.getDeltaMovement();
        Vec3 end = start.add(motion);

        HitResult blockHit = level.clip(new ClipContext(start, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
        double speed = motion.length();
        AABB aabb = this.getBoundingBox().expandTowards(motion).inflate(0.35D + speed * 0.75D);
        EntityHitResult entityHit = ProjectileUtil.getEntityHitResult(level, this, start, end, aabb, this::canHitEntity);

        HitResult chosen = null;
        if (entityHit != null) chosen = entityHit;
        if (blockHit != null && blockHit.getType() != HitResult.Type.MISS) {
            if (chosen == null) {
                chosen = blockHit;
            } else {
                double dBlock = start.distanceTo(blockHit.getLocation());
                double dEntity = start.distanceTo(((EntityHitResult) chosen).getLocation());
                if (dBlock + 0.15D < dEntity) chosen = blockHit;
            }
        }

        if (chosen != null && chosen.getType() != HitResult.Type.MISS) {
            this.onHit(chosen);
        }

        this.setPos(this.getX() + motion.x, this.getY() + motion.y, this.getZ() + motion.z);
        if (!this.isNoGravity()) motion = motion.add(0.0D, -GRAVITY, 0.0D);
        motion = motion.scale(DRAG);
        this.setDeltaMovement(motion);

        if (level.isClientSide) {
            JurassicClient.spawnVenomParticles(this);
        }

        Vec3 v = this.getDeltaMovement();
        double horiz = Math.sqrt(v.x * v.x + v.z * v.z);
        float yRot = (float) Math.toDegrees(Math.atan2(v.x, v.z));
        float xRot = (float) Math.toDegrees(Math.atan2(v.y, horiz));
        this.setYRot(yRot);
        this.setXRot(xRot);
        this.yRotO = yRot;
        this.xRotO = xRot;
    }

    @Override
    protected boolean canHitEntity(Entity e) {
        if (e == null || !e.isAlive() || e.isSpectator() || e.is(this)) return false;
        Entity owner = this.getOwner();
        if (owner != null) {
            if (this.tickCount <= OWNER_GRACE_TICKS) {
                if (e == owner) return false;
                if (ownerIdCache != null && ownerIdCache.equals(e.getUUID())) return false;
            }
            Entity eRoot = e.getRootVehicle();
            Entity oRoot = owner.getRootVehicle();
            if (eRoot != null && oRoot != null && eRoot == oRoot) return false;
            if (e.isPassenger() && e.getVehicle() == owner) return false;
            if (owner.isPassenger() && owner.getVehicle() == e) return false;
        }
        return super.canHitEntity(e);
    }

    @Override
    protected void onHit(HitResult hit) {
        super.onHit(hit);
        if (!this.level.isClientSide) this.discard();
    }

    @Override
    protected void onHitEntity(EntityHitResult ehr) {
        super.onHitEntity(ehr);
        Entity victim = ehr.getEntity();
        Entity shooter = this.getOwner();
        if (victim == shooter) return;
        if (shooter instanceof DilophosaurusEntity dilo) {
            if (victim instanceof DilophosaurusEntity && victim != dilo.getTarget()) return;
        }
        if (victim instanceof LivingEntity living) {
            DamageSource src = DamageSource.indirectMobAttack(this, shooter instanceof LivingEntity le ? le : null);
            living.hurt(src, 4.0F);
            living.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 20 * 15, 0, false, true));
            living.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 20 * 15, 0, false, true));
        }
        if (!this.level.isClientSide) this.discard();
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public void makeLaserStraight(boolean straight) {
        this.setNoGravity(straight);
    }
}
