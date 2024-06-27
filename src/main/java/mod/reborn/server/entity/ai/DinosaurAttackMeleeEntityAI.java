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
        if(this.attacker != null && super.shouldContinueExecuting()) {
            EntityLivingBase target = this.attacker.getAttackTarget();
            if (target instanceof DinosaurEntity && ((DinosaurEntity) target).isCarcass()) {
                return false;
            }
            return true;
        }
        return false;
    }

    @Override
    protected double getAttackReachSqr(EntityLivingBase attackTarget) {
        if (attackTarget.getEntityBoundingBox().intersects(this.dinosaur.getEntityBoundingBox().expand(1.3, 1.3, 1.3))) {
            return 1024.0;
        }
        double grownWidth = this.attacker.width + 1.0;
        return grownWidth * grownWidth;
    }
}