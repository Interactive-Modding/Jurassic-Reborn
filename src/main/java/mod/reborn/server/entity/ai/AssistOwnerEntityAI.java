package mod.reborn.server.entity.ai;

import mod.reborn.server.entity.DinosaurEntity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;

import java.util.UUID;

public class AssistOwnerEntityAI extends EntityAIBase {
    private DinosaurEntity entity;
    private EntityPlayer owner;
    private EntityLivingBase target;

    public AssistOwnerEntityAI(DinosaurEntity entity) {
        this.entity = entity;
        this.setMutexBits(Mutex.ATTACK | Mutex.MOVEMENT);
    }

    @Override
    public boolean shouldExecute() {
        if (this.entity.getAgePercentage() > 50) {
            UUID ownerId = this.entity.getOwner();

            if (ownerId != null) {
                this.owner = this.entity.world.getPlayerEntityByUUID(ownerId);

                if (this.owner != null) {
                    this.target = this.owner.getLastAttackedEntity();
                    return this.target != null && this.target != this.entity && this.entity.getOrder() == DinosaurEntity.Order.FOLLOW;
                }
            }
        }

        return false;
    }

    @Override
    public boolean shouldContinueExecuting() {
        return !this.isDead(this.target) && this.entity.getOrder() == DinosaurEntity.Order.FOLLOW;
    }

    private boolean isDead(EntityLivingBase attacker) {
        return !attacker.isEntityAlive() || (attacker instanceof DinosaurEntity && ((DinosaurEntity) attacker).isCarcass());
    }

    @Override
    public void updateTask() {
        if (this.entity.getAttackTarget() != this.target) {
            this.entity.setAttackTarget(this.target);
        }
    }

    @Override
    public void resetTask() {
        this.entity.setAttackTarget(null);
    }
}
