package mod.reborn.server.entity.ai;

import mod.reborn.server.entity.DinosaurEntity;

import net.minecraft.block.material.Material;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class DinosaurWanderEntityAI extends EntityAIBase
{
    protected DinosaurEntity entity;
    private volatile double xPosition = 0;
    private volatile double yPosition = 0;
    private volatile double zPosition = 0;
    private double speed;
    private static ThreadPoolExecutor tpeWander = new ThreadPoolExecutor(0, 15, 10, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
    protected int executionChance;
    private boolean mustUpdate;
    private volatile boolean should = false;
    private volatile boolean started = false;
    private final int walkradius;
    private final Herd herd;


    public DinosaurWanderEntityAI(DinosaurEntity creatureIn, double speedIn, int chance, int walkradius)
    {
        this.entity = creatureIn;
        this.speed = speedIn;
        this.executionChance = chance;
        this.walkradius = walkradius;
        this.herd = entity.herd;
        this.setMutexBits(Mutex.MOVEMENT);
    }

    @Override
    public boolean shouldExecute() {
        if (!this.mustUpdate) {
            if (innerShouldStopExcecuting()) {
                return false;
            }
        }

        if (this.outterShouldExecute()) {
            if (this.started == true) //|| this.should == true)
                return false;
            if (tpeWander.getActiveCount() < 14) {
                try {
                    tpeWander.execute(new ThreadRunnable(this, this.entity) {

                        @Override
                        public void run() {
                            synchronized (this.entity.world) {
                                this.ai.started = true;

                                overlist: for (int i = 0; i < 100; i++) {
                                    Vec3d vec = getWanderPosition();
                                    if (vec != null) {

                                        for (BlockPos pos : BlockPos.getAllInBox(new BlockPos(vec.addVector(0, 1, 0)),
                                                new BlockPos(vec.addVector(1, 1, 1)))) {
                                            if (this.entity.world.getBlockState(pos).getMaterial() != Material.AIR) {
                                                continue overlist;
                                            }
                                        }
                                        this.ai.xPosition = vec.x;
                                        this.ai.yPosition = vec.y;
                                        this.ai.zPosition = vec.z;
                                        this.ai.mustUpdate = false;
                                        this.ai.should = true;


                                    }
                                }
                                this.ai.started = false;

                            }
                        }
                    });
                } catch (RejectedExecutionException e) {

                }
            }
        }

        return this.should;
    }

    protected boolean innerShouldStopExcecuting() { //TODO: merge into one
        return this.entity.getRNG().nextInt(this.executionChance) != 0;
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
        try {
            this.entity.getNavigator().tryMoveToXYZ(this.xPosition, this.yPosition, this.zPosition, this.speed);
        }catch(Exception e) {

        }
        this.should = false;
    }

    public void makeUpdate()
    {
        this.mustUpdate = true;
    }

    public void setExecutionChance(int chance)
    {
        this.executionChance = chance;
    }

    abstract class ThreadRunnable implements Runnable {

        final DinosaurEntity entity;
        final DinosaurWanderEntityAI ai;

        ThreadRunnable(DinosaurWanderEntityAI wanderAI, DinosaurEntity entity) {
            this.ai = wanderAI;
            this.entity = entity;
        }
    }
}