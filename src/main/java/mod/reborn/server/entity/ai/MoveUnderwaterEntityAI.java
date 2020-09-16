package mod.reborn.server.entity.ai;

import mod.reborn.server.entity.DinosaurEntity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.util.math.Vec3d;

import java.util.Random;

public class MoveUnderwaterEntityAI extends EntityAIBase {
    private DinosaurEntity swimmingEntity;
    private double xPosition;
    private double yPosition;
    private double zPosition;

    public MoveUnderwaterEntityAI(DinosaurEntity entity) {
        this.swimmingEntity = entity;
        this.setMutexBits(Mutex.MOVEMENT);
    }

    @Override
    public boolean shouldExecute() {
        if (this.swimmingEntity.getRNG().nextFloat() < 0.50 && this.swimmingEntity.isBusy()) {
            return false;
        }
        Vec3d target = RandomPositionGenerator.findRandomTarget(this.swimmingEntity, 6, 6);
        double x;
        double y;
        double z;
        if (target == null) {
            return false;
        } else {
            this.xPosition = target.x;
            this.yPosition = target.y;
            this.zPosition = target.z;
            return true;
        }
    }

    @Override
    public boolean shouldContinueExecuting() {
        return !this.swimmingEntity.getNavigator().noPath();
    }

    @Override
    public void startExecuting() {
        this.swimmingEntity.getNavigator().tryMoveToXYZ(this.xPosition, this.yPosition, this.zPosition, 1.0D);
    }

    @Override
    public boolean isInterruptible() {
        return false;
    }
}
