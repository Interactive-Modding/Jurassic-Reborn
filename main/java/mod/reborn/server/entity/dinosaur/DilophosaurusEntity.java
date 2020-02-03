package mod.reborn.server.entity.dinosaur;

import mod.reborn.client.model.animation.EntityAnimation;
import mod.reborn.client.sound.SoundHandler;
import mod.reborn.server.entity.DinosaurEntity;
import mod.reborn.server.entity.animal.GoatEntity;
import mod.reborn.server.entity.VenomEntity;
import mod.reborn.server.entity.ai.DilophosaurusMeleeEntityAI;
import mod.reborn.server.entity.ai.DilophosaurusSpitEntityAI;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class DilophosaurusEntity extends DinosaurEntity implements IRangedAttackMob {

    private static final DataParameter<Boolean> WATCHER_HAS_TARGET = EntityDataManager.createKey(DinosaurEntity.class, DataSerializers.BOOLEAN);
    private int targetCooldown;

    public DilophosaurusEntity(World world) {
        super(world);
        this.target(GoatEntity.class, EntityPlayer.class, EntityVillager.class, EntityAnimal.class, GallimimusEntity.class, ParasaurolophusEntity.class, TriceratopsEntity.class, VelociraptorEntity.class, MussaurusEntity.class);
        this.tasks.addTask(1, new DilophosaurusMeleeEntityAI(this, this.dinosaur.getAttackSpeed()));
    }

    @Override
    public void attackEntityWithRangedAttack(EntityLivingBase target, float distance) {
	if(target instanceof EntityPlayer && ((EntityPlayer)target).isCreative()) {
	    return;
	}
        VenomEntity venom = new VenomEntity(this.world, this);
        double deltaX = target.posX - venom.posX;
        double deltaY = target.posY + (double) target.getEyeHeight() - 1.100000023841858D - venom.posY;
        double deltaZ = target.posZ - venom.posZ;
        float yOffset = MathHelper.sqrt(deltaX * deltaX + deltaZ * deltaZ) * 0.2F;
        venom.shoot(deltaX, deltaY + (double) yOffset, deltaZ, 1.5F, 0F);
        this.world.spawnEntity(venom);
    }


    //TODO: ????
    @Override
    public void setSwingingArms(boolean swingingArms)
    {

    }

    @Override
    public EntityAIBase getAttackAI() {
        return new DilophosaurusSpitEntityAI(this, this.dinosaur.getAttackSpeed(), 40, 10);
    }

    @Override
    public void entityInit() {
        super.entityInit();

        this.dataManager.register(WATCHER_HAS_TARGET, false);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if (!this.world.isRemote) {
            EntityLivingBase target = this.getAttackTarget();
            if (target != null && !target.isDead && this.targetCooldown < 50) {
                this.targetCooldown = 50 + this.getRNG().nextInt(30);
            } else if (this.targetCooldown > 0) {
                this.targetCooldown--;
            }

            this.dataManager.set(WATCHER_HAS_TARGET, this.hasTarget());
        }
    }

    @Override
    public boolean attackEntityAsMob(Entity entity) {
        if (super.attackEntityAsMob(entity)) {
            if (entity instanceof EntityLivingBase) {
                ((EntityLivingBase) entity).addPotionEffect(new PotionEffect(MobEffects.POISON, 300, 1, false, false));
            }

            return true;
        }

        return false;
    }

    public boolean hasTarget() {
        if (!this.isCarcass() && !this.isSleeping()) {
            if (this.world.isRemote) {
                return this.dataManager.get(WATCHER_HAS_TARGET);
            } else {
                EntityLivingBase target = this.getAttackTarget();
                return (target != null && !target.isDead) || this.targetCooldown > 0;
            }
        }
        return false;
    }

    @Override
    public SoundEvent getSoundForAnimation(Animation animation) {
        switch (EntityAnimation.getAnimation(animation)) {
            case SPEAK:
                return SoundHandler.DILOPHOSAURUS_LIVING;
            case CALLING:
                return SoundHandler.DILOPHOSAURUS_LIVING;
            case DYING:
                return SoundHandler.DILOPHOSAURUS_DEATH;
            case INJURED:
                return SoundHandler.DILOPHOSAURUS_HURT;
		default:
			break;
        }

        return null;
    }
}