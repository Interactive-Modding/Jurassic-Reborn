package mod.reborn.server.entity.ai;

import mod.reborn.server.entity.DinosaurEntity;

import net.minecraft.block.material.Material;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class DinosaurWanderEntityAI extends EntityAIBase
{
    protected DinosaurEntity entity;
    private double xPosition;
    private double yPosition;
    private double zPosition;
    private final double speed;
    protected int executionChance;
    private boolean mustUpdate;
    private final int walkradius;
    private final Herd herd;


    public DinosaurWanderEntityAI(DinosaurEntity creatureIn, double speedIn, int chance, int walkradius)
    {
        this.entity = creatureIn;
        this.herd = entity.herd;
        this.speed = speedIn;
        this.executionChance = chance;
        this.walkradius = walkradius;
        this.setMutexBits(Mutex.MOVEMENT);
    }

    @Override
    public boolean shouldExecute()
    {
        if (!this.mustUpdate)
        {
            if (innerShouldStopExcecuting())
            {
                return false;
            }
        }

        if (this.outterShouldExecute())
        {
            overlist:
            for(int i = 0; i < 100; i++) {
        	Vec3d vec = getWanderPosition();
        	if (vec != null) {
                for (BlockPos pos : BlockPos.getAllInBox(new BlockPos(vec.addVector(0, 1, 0)), new BlockPos(vec.addVector(1, 2, 1)))) {
                    if (!this.entity.world.isBlockLoaded(pos)) {
                        continue overlist;
                    }
                    if (this.entity.world.getBlockState(pos).getMaterial() != Material.AIR) {
                        continue overlist;
                    }
                }
        	    this.xPosition = vec.x;
        	    this.yPosition = vec.y;
        	    this.zPosition = vec.z;
        	    this.mustUpdate = false;
        	    return true;
        	}
            }
        }

        return false;
    }
    
    protected boolean innerShouldStopExcecuting() { //TODO: merge into one
	return this.entity.getRNG().nextInt(this.executionChance) != 0 && !(this.entity.getOrder() == DinosaurEntity.Order.FOLLOW);
    }
    
    protected boolean outterShouldExecute() {
	    return this.entity.getNavigator().noPath() && this.entity.getAttackTarget() == null;
    }
    
    protected Vec3d getWanderPosition() {
        return RandomPositionGenerator.getLandPos(this.entity, walkradius + 5, walkradius);
    }

    @Override
    public boolean shouldContinueExecuting()
    {
        return !this.entity.getNavigator().noPath() && !this.entity.isInWater();
    }

    @Override
    public void startExecuting()
    {
        if(herd != null) {
            for(DinosaurEntity entity : herd.members) {
                double xPositionUpdated = this.xPosition + (entity.getRNG().nextDouble()*2);
                double zPositionUpdated = this.zPosition + (entity.getRNG().nextDouble()*2);
                if (!this.entity.world.isBlockLoaded(new BlockPos(xPositionUpdated,this.yPosition,zPositionUpdated))){
                    this.entity.getNavigator().tryMoveToXYZ(this.xPosition, this.yPosition, this.zPosition, this.speed);
                }
                entity.getNavigator().tryMoveToXYZ(xPositionUpdated, this.yPosition, zPositionUpdated, this.speed);
            }
        } else {
            this.entity.getNavigator().tryMoveToXYZ(this.xPosition, this.yPosition, this.zPosition, this.speed);
        }
    }

    public void makeUpdate()
    {
        this.mustUpdate = true;
    }

    public void setExecutionChance(int chance)
    {
        this.executionChance = chance;
    }
}