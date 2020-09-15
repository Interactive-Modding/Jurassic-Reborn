package mod.reborn.server.entity.ai;

import mod.reborn.server.entity.DinosaurEntity;
import net.minecraft.block.material.Material;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.Random;

public class DinosaurHerdWanderEntityAI extends EntityAIBase
{
    private double xPosition;
    private double yPosition;
    private double zPosition;
    private final double speed;
    protected int executionChance;
    private boolean mustUpdate;
    private final int walkradius;
    private final Herd herd;
    private Random random = new Random();


    public DinosaurHerdWanderEntityAI(Herd herd, double speedIn, int chance, int walkradius)
    {
        this.herd = herd;
        this.speed = speedIn;
        this.executionChance = chance;
        this.walkradius = walkradius;
        this.setMutexBits(Mutex.MOVEMENT);
    }

    @Override
    public boolean shouldExecute()
    {
        if(herd != null && !herd.fleeing) {
            if (!this.mustUpdate) {
                if (innerShouldStopExcecuting()) {
                    return false;
                }
            }

            if (this.outterShouldExecute()) {
                overlist:
                for (int i = 0; i < 100; i++) {
                    Vec3d vec = getWanderPosition();
                    if (vec != null) {
                        for (BlockPos pos : BlockPos.getAllInBox(new BlockPos(vec.addVector(0, 1, 0)), new BlockPos(vec.addVector(1, 2, 1)))) {
                            if (this.herd.leader.world.getBlockState(pos).getMaterial() != Material.AIR) {
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

        }
        return false;
    }
    
    protected boolean innerShouldStopExcecuting() { //TODO: merge into one
        if(herd != null && herd.leader != null)
    	return this.random.nextInt(this.executionChance) != 0;
        return false;
    }
    
    protected boolean outterShouldExecute() {
        if(herd != null && herd.leader != null)
            return this.herd.leader.getNavigator().noPath() && this.herd.leader.getAttackTarget() == null;
        return false;
    }
    
    protected Vec3d getWanderPosition() {
        return RandomPositionGenerator.getLandPos(this.herd.leader, walkradius + 5, walkradius);
    }

    @Override
    public boolean shouldContinueExecuting()
    {
        if(herd != null && herd.leader != null) return !this.herd.leader.getNavigator().noPath() && !this.herd.leader.isInWater();
        else return false;
    }

    @Override
    public void startExecuting()
    {
        if(herd != null) {
            for(DinosaurEntity entity : herd.members) {
                entity.getNavigator().tryMoveToXYZ(this.xPosition + (entity.getRNG().nextDouble()*2), this.yPosition, this.zPosition + (entity.getRNG().nextDouble()*2), this.speed);
            }
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