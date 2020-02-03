package mod.reborn.server.entity.ai.navigation;

import mod.reborn.server.entity.DinosaurEntity;
import mod.reborn.server.entity.ai.Mutex;
import mod.reborn.server.entity.ai.util.AIUtils;
import mod.reborn.server.entity.ai.util.MovementType;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.math.Vec3d;

public class MoveUnderwaterEntityAI extends EntityAIBase
{
    private DinosaurEntity swimmingEntity;
    private double xPosition;
    private double yPosition;
    private double zPosition;

    public MoveUnderwaterEntityAI(DinosaurEntity entity)
    {
        this.swimmingEntity = entity;
        this.setMutexBits(Mutex.MOVEMENT);
    }

    @Override
    public boolean shouldExecute()
    {
        if(!this.swimmingEntity.isInWater())
            return false;
        if (this.swimmingEntity.getRNG().nextFloat() < 0.50 && !this.swimmingEntity.getNavigator().noPath())
            return false;
        Vec3d target = RandomPositionGenerator.findRandomTarget(this.swimmingEntity, 6, 2);

        if (target == null)
        {
            return false;
        }
        if(getType().equals(MovementType.DEEP_WATER))
        {
            this.xPosition = target.x;
            this.yPosition = AIUtils.findSurface(swimmingEntity).getY() - (AIUtils.getBottom(swimmingEntity).getY());
            this.zPosition = target.z;
            return true;
        }
        else if(getType().equals(MovementType.NEAR_SURFACE))
        {
            this.xPosition = target.x;
            this.yPosition = target.y;
            this.zPosition = target.z;
            return true;
        }
        this.xPosition = target.x;
        this.yPosition = target.y;
        this.zPosition = target.z;
        return true;
    }

    @Override
    public boolean shouldContinueExecuting()
    {
        return !this.swimmingEntity.getNavigator().noPath();
    }

    @Override
    public void updateTask()
    {
        if(!swimmingEntity.getLookHelper().getIsLooking())
        this.swimmingEntity.getLookHelper().setLookPosition(this.xPosition * .75, this.yPosition * .75, this.zPosition * .75, -5F, swimmingEntity.getVerticalFaceSpeed());
    }

    @Override
    public void startExecuting()
    {
        this.swimmingEntity.getNavigator().tryMoveToXYZ(this.xPosition, this.yPosition, this.zPosition, 1.0D);
    }

    public MovementType getType()
    {
        return swimmingEntity.getDinosaur().getMovementType();
    }
}