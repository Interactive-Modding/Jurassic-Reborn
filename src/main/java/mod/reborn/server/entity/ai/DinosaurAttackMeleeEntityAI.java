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
        if (this.attacker != null && super.shouldContinueExecuting()) {
            EntityLivingBase target = this.attacker.getAttackTarget();
            if (target == null) {
                return false;
            }
            if (target instanceof DinosaurEntity && ((DinosaurEntity) target).isCarcass()) {
                return false;
            }
            return true;
        }
        return false;
    }

    @Override
    public void startExecuting() {
        super.startExecuting();
    }

    @Override
    public void resetTask() {
        super.resetTask();
    }

    @Override
    public void updateTask() {
        if (this.attacker == null) {
            return;
        }

        EntityLivingBase attackTarget = this.attacker.getAttackTarget();
        if (attackTarget == null) {
            return;
        }

            super.updateTask();

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
