package mod.reborn.server.entity.ai.metabolism;

import mod.reborn.client.model.animation.EntityAnimation;
import mod.reborn.server.entity.DinosaurEntity;
import mod.reborn.server.entity.MetabolismContainer;
import mod.reborn.server.entity.ai.BlockRingBuffer;
import mod.reborn.server.entity.ai.Mutex;
import mod.reborn.server.entity.ai.util.AIUtils;
import mod.reborn.server.entity.ai.util.OnionTraverser;
import mod.reborn.server.util.GameRuleHandler;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class DrinkEntityAI extends EntityAIBase {
    protected DinosaurEntity dinosaur;
    private static ThreadPoolExecutor tpe = new ThreadPoolExecutor(0, 3, 10, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
    protected volatile Path path;
    protected BlockPos pos;
    protected volatile boolean searched = false;
    protected int giveUpTime;
    protected volatile BlockPos water = null;
    protected final BlockRingBuffer unreachable = new BlockRingBuffer(10);//List<BlockPos> unreachable = new ArrayList<BlockPos>();
    //  protected volatile List<BlockPos> positive = new ArrayList<BlockPos>();
    protected volatile BlockPos waterPos = null;

    public DrinkEntityAI(DinosaurEntity dinosaur) {
        this.dinosaur = dinosaur;
        this.setMutexBits(Mutex.METABOLISM);
    }

    @Override
    public boolean shouldExecute() {
        if (this.dinosaur.isAlive() && GameRuleHandler.DINO_METABOLISM.getBoolean(this.dinosaur.world)) {
            if (this.dinosaur.getNavigator().noPath() || this.dinosaur.getMetabolism().isDehydrated()) {
                if (this.dinosaur.getMetabolism().isThirsty()) {
                    World world = this.dinosaur.world;

                    if (this.searched == false && tpe.getActiveCount() < 2) {

                        this.searched = true;
                        try {
                            tpe.execute(new ThreadRunnable(this, this.dinosaur) {

                                @Override
                                public void run() {
                                    synchronized (world) {

                                        OnionTraverser traverser = new OnionTraverser(this.entity.getPosition(), 32);

                                        HashMap<Float, BlockPos> distanceMap = new HashMap<>();

                                        for (BlockPos pos : traverser) {
                                            if (world.isBlockLoaded(pos)) {
                                                if (world.getBlockState(pos).getMaterial() == Material.WATER && !this.ai.unreachable.contains(pos)) {
                                                    float distance = (float) pos.distanceSq(this.entity.getPosition());
                                                    distanceMap.put(distance, pos);
                                                }
                                            }
                                        }

                                        List<Float> sortedList = new ArrayList<>(distanceMap.keySet());

                                        Collections.sort(sortedList);
                                        BlockPos pos = null;
                                        if(sortedList.size() != 0) pos = distanceMap.get(sortedList.get(0));

                                        //	for (BlockPos pos : traverser) {

                                        //if (world.getBlockState(pos).getMaterial() == Material.WAdTER && !this.ai.unreachable.contains(pos)) {
                                        this.ai.waterPos = pos;

                                        try {
                                            //BlockPos surface = AIUtils.findSurface(world, pos);
                                            //	BlockPos shore = AIUtils.findShore(world, surface.down());
                                            BlockPos shore = null;
                                            synchronized (this.ai.unreachable) {
                                                shore = AIUtils.findShore(world, pos);
                                            }
                                            if (shore != null) {

                                                IBlockState state = world.getBlockState(shore);

                                                if (state.isFullBlock()) {

                                                    synchronized (this.ai) {

                                                        Path path = this.entity.getNavigator().getPathToPos(shore);
                                                        if (path != null && path.getCurrentPathLength() != 0) {
                                                            this.ai.path = path;

                                                            this.ai.water = shore;
                                                            //break;
                                                        }
                                                    }
                                                }
                                            }
                                        } catch (Exception e) {

                                        }
                                    }
                                    this.ai.searched = false;
                                }
                            });
                        } catch (RejectedExecutionException e) {

                        }
                    }

                    if (this.water != null && this.path != null) {
                        this.pos = this.water;
                        this.giveUpTime = this.path.getCurrentPathLength() * 20;

                        return this.dinosaur.getNavigator().setPath(this.path, 1.0);
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void updateTask() {
        if (this.giveUpTime > 0) {
            this.giveUpTime--;
            if(this.giveUpTime <= 5 && this.dinosaur.getPosition().distanceSq(this.water) <= 6/* && !this.positive.contains(this.waterPos)*/) {
                this.unreachable.add(this.waterPos);
            }
        }
        if (this.path != null) {
            try {
                this.dinosaur.getNavigator().setPath(this.path, 1.0);

                //	if(this.path.isFinished() && this.dinosaur.getPosition().distanceSq(this.water) <= 6)
                //this.positive.add(this.water);


                if (this.path.isFinished() && this.dinosaur.getPosition().distanceSq(this.water) <= 6) {
                    this.dinosaur.getLookHelper().setLookPosition(this.pos.getX() + 0.5, this.pos.getY() + 0.5, this.pos.getZ() + 0.5, 10.0F, 10.0F);
                    this.dinosaur.setAnimation(EntityAnimation.DRINKING.get());
                    MetabolismContainer metabolism = this.dinosaur.getMetabolism();
                    metabolism.setWater(metabolism.getWater() + (metabolism.getMaxWater() / 8));
                }
            } catch (NullPointerException e) {

            }
        }
    }

    @Override
    public void resetTask() {
        super.resetTask();
        this.path = null;
        this.dinosaur.getNavigator().clearPath();
    }

    @Override
    public boolean shouldContinueExecuting() {
        return this.giveUpTime > 0 && this.dinosaur != null && this.dinosaur.isAlive() && this.path != null && this.dinosaur.getMetabolism().getWater() < this.dinosaur.getMetabolism().getMaxWater() * 0.9;
    }

    @Override
    public boolean isInterruptible() {
        return false;
    }

    abstract class ThreadRunnable implements Runnable {

        final DinosaurEntity entity;
        final DrinkEntityAI ai;

        ThreadRunnable(DrinkEntityAI drinkEntityAI, DinosaurEntity entity) {
            this.ai = drinkEntityAI;
            this.entity = entity;
        }
    }

}