package mod.reborn.server.entity.animal.ai;

import mod.reborn.server.entity.ai.Mutex;
import mod.reborn.server.entity.animal.EntityCrab;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class EntityAIFindWater extends EntityAIBase
{
    protected EntityCreature entity;
    private double xPosition;
    private double yPosition;
    private double zPosition;
    private double speed;
    protected int executionChance;
    private boolean mustUpdate;
    private int walkradius;


    public EntityAIFindWater(EntityCreature creatureIn, double speedIn, int chance, int walkradius)
    {
        this.entity = creatureIn;
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
                    for (BlockPos pos : BlockPos.getAllInBox(new BlockPos(vec.addVector(0, 1, 0)), new BlockPos(vec.addVector(1, 1, 1)))) {
                        if (this.entity.world.getBlockState(pos).getBlock() != Blocks.WATER) {
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
        return this.entity.getRNG().nextInt(this.executionChance) != 0;
    }

    protected boolean outterShouldExecute() {
        return this.entity.getNavigator().noPath() && this.entity.getAttackTarget() == null;
    }

    protected Vec3d getWanderPosition() {
        return RandomPositionGenerator.findRandomTarget(this.entity, walkradius, walkradius);
    }

    @Override
    public boolean shouldContinueExecuting()
    {
        return !this.entity.getNavigator().noPath();
    }

    @Override
    public void startExecuting()
    {
        this.entity.getNavigator().tryMoveToXYZ(this.xPosition, this.yPosition, this.zPosition, this.speed);
    }

    @Override
    public boolean isInterruptible() {
        return false;
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