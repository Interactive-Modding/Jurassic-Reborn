package mod.reborn.server.entity.ai;

import mod.reborn.server.entity.DinosaurEntity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class ProtectInfantEntityAI<T extends DinosaurEntity> extends EntityAIBase {
    private T dinosaur;
    private Class<T> dinosaurClazz;
    private T infant;

    public ProtectInfantEntityAI(T dinosaur) {
        this.dinosaur = dinosaur;
        this.dinosaurClazz = (Class<T>) dinosaur.getClass();
        this.setMutexBits(Mutex.ATTACK);
    }

    @Override
    public boolean shouldExecute() {
        if (this.dinosaur.getAgePercentage() > 75 && this.dinosaur.ticksExisted % 10 == 0) {
            this.infant = null;

            List<T> dinosaurs = this.dinosaur.world.getEntitiesWithinAABB(this.dinosaurClazz, this.dinosaur.getEntityBoundingBox().expand(8, 3, 8));

            for (T entity : dinosaurs) {
                if (entity.getAttackTarget() != null && entity.getAgePercentage() <= 50) {
                    this.infant = entity;
                    break;
                }
            }

            if (this.infant != null) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void startExecuting() {
        EntityLivingBase attacker = this.infant.getAttackTarget();

        if (attacker != null && !(attacker instanceof EntityPlayer && ((EntityPlayer) attacker).capabilities.isCreativeMode)) {
            this.dinosaur.setAttackTarget(attacker);
        }
    }

    @Override
    public boolean shouldContinueExecuting() {
        return false;
    }
}
