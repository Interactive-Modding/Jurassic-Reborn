package mod.reborn.server.entity.dinosaur;

import mod.reborn.client.model.animation.EntityAnimation;
import mod.reborn.client.sound.SoundHandler;
import mod.reborn.server.conf.RebornConfig;
import mod.reborn.server.entity.DinosaurEntity;
import mod.reborn.server.entity.SwimmingDinosaurEntity;
import mod.reborn.server.entity.ai.DinosaurWanderEntityAI;
import mod.reborn.server.entity.ai.LeapingMeleeEntityAI;
import mod.reborn.server.entity.ai.RaptorLeapEntityAI;
import mod.reborn.server.entity.animal.ai.EntityAIWanderNearWater;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class BeelzebufoEntity extends SwimmingDinosaurEntity {

    public BeelzebufoEntity(World world) {
        super(world);
        this.tasks.addTask(0, new LeapingMeleeEntityAI(this, getAIMoveSpeed()));
        this.tasks.addTask(0, new DinosaurWanderEntityAI(this, getAIMoveSpeed(), 10, RebornConfig.ENTITIES.dinosaurWalkingRadius));
    }
    @Override
    public void onEntityUpdate() {
        int air = this.getAir();
        super.onEntityUpdate();

        if (this.isEntityAlive() && this.isInWater()) {
            if (this.getAir() == -20) {
                this.setAir(0);
            }
        } else {
            this.setAir(300);
        }
    }

    @Override
    protected boolean canTriggerWalking() {
        return true;
    }

    @Override
    public SoundEvent getSoundForAnimation(Animation animation) {
        switch (EntityAnimation.getAnimation(animation)) {
            case SPEAK:
                return SoundHandler.BEELZEBUFO_CROAK;
            case CALLING:
                return SoundHandler.BEELZEBUFO_LONG_DISTANCE_CROAK;
            case DYING:
                return SoundHandler.BEELZEBUFO_HURT_THREAT;
            case INJURED:
                return SoundHandler.BEELZEBUFO_HURT;
            case BEGGING:
                return SoundHandler.BEELZEBUFO_THREAT;
            default:
                return null;
        }
    }

    @Override
    public EntityAIBase getAttackAI() {
        return new RaptorLeapEntityAI(this);
    }
    }

    @Override
    public void fall(float distance, float damageMultiplier) {
        if (this.getAnimation() != EntityAnimation.LEAP_LAND.get()) {
            super.fall(distance, damageMultiplier);
        }
    }
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(35.0D);
    }

}
