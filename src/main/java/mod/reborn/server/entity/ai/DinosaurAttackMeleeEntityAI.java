package mod.reborn.server.entity.ai;

import mod.reborn.server.entity.DinosaurEntity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIAttackMelee;

public class DinosaurAttackMeleeEntityAI extends EntityAIAttackMelee {
    protected DinosaurEntity dinosaur;

    public DinosaurAttackMeleeEntityAI(DinosaurEntity dinosaur, double speed, boolean useLongMemory) {
        super(dinosaur, speed, useLongMemory);
        this.dinosaur = dinosaur;
        this.setMutexBits(Mutex.ATTACK);
    }

    @Override
    public boolean shouldContinueExecuting() {
        if(this.dinosaur.getOrder() == DinosaurEntity.Order.FOLLOW) {
            return false;
        }
        if(this.attacker != null && super.shouldContinueExecuting()) {
            EntityLivingBase target = this.attacker.getAttackTarget();
            assert target != null;
            if (target instanceof DinosaurEntity && ((DinosaurEntity) target).isCarcass() && target.isDead) {
                return false;
            }
            return true;
        }
        return false;
    }

    @Override
    protected double getAttackReachSqr(EntityLivingBase attackTarget) {
        if (attackTarget.getEntityBoundingBox().intersects(this.dinosaur.getEntityBoundingBox().expand(1, 1, 1))) {
            return 1024.0;
        }
        double grownWidth = this.attacker.width + 1.0;
        return grownWidth * grownWidth;
    }
}