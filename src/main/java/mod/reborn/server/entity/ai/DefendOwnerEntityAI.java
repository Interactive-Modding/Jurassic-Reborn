package mod.reborn.server.entity.ai;

import mod.reborn.server.entity.DinosaurEntity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;

import java.util.UUID;

public class DefendOwnerEntityAI extends EntityAIBase {
    private DinosaurEntity entity;
    private EntityPlayer owner;
    private EntityLivingBase attacker;

    public DefendOwnerEntityAI(DinosaurEntity entity) {
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
                    this.attacker = this.owner.getRevengeTarget();

                    return this.attacker != null && this.entity.getOrder() == DinosaurEntity.Order.FOLLOW;
                }
            }
        }

        return false;
    }

    @Override
    public boolean shouldContinueExecuting() {
        return !this.isDead(this.attacker) && this.entity.getOrder() == DinosaurEntity.Order.FOLLOW;
    }

    private boolean isDead(EntityLivingBase attacker) {
        return !attacker.isEntityAlive() || (attacker instanceof DinosaurEntity && ((DinosaurEntity) attacker).isCarcass());
    }

    @Override
    public void updateTask() {
        if (this.entity.getAttackTarget() != this.attacker) {
            this.entity.setAttackTarget(this.attacker);
        }
    }

    @Override
    public void resetTask() {
        this.entity.setAttackTarget(null);
    }
}
