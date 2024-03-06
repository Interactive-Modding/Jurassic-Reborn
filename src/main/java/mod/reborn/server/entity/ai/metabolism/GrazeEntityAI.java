package mod.reborn.server.entity.ai.metabolism;

import mod.reborn.client.model.animation.EntityAnimation;
import mod.reborn.server.entity.DinosaurEntity;
import mod.reborn.server.entity.MetabolismContainer;
import mod.reborn.server.entity.ai.Mutex;
import mod.reborn.server.entity.ai.util.OnionTraverser;
import mod.reborn.server.food.FoodHelper;
import mod.reborn.server.util.GameRuleHandler;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class GrazeEntityAI extends EntityAIBase {
    public static final int EAT_RADIUS = 6;// was 25
    public static final int LOOK_RADIUS = 16;
    private static ThreadPoolExecutor tpe = new ThreadPoolExecutor(0, 3, 10, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
    private static final int GIVE_UP_TIME = 400;// 14*20 counter = 14 seconds (ish?)
    protected volatile boolean searched = false;

    protected DinosaurEntity dinosaur;
    protected volatile BlockPos target;
    protected volatile BlockPos moveTarget;
    protected volatile boolean feederExists = false;
    private int counter;
    private World world;
    private volatile BlockPos previousTarget;
    private volatile Vec3d targetVec;

    public GrazeEntityAI(DinosaurEntity dinosaur) {
        this.dinosaur = dinosaur;
        this.setMutexBits(Mutex.METABOLISM);
    }

    @Override
    public boolean shouldExecute() {
        if (!(this.dinosaur.isDead || this.dinosaur.isCarcass() || !GameRuleHandler.DINO_METABOLISM.getBoolean(this.dinosaur.world)) && this.dinosaur.getMetabolism().isHungry()) {
            if (!this.dinosaur.getMetabolism().isStarving() && feederExists()) {
                return false;
            }

            // This gets called once to initiate. Here's where we find the plant and start
            // movement
            Vec3d headPos = this.dinosaur.getHeadPos();
            BlockPos head = new BlockPos(headPos.x, headPos.y, headPos.z);

            // world the animal currently inhabits
            this.world = this.dinosaur.world;

            MetabolismContainer metabolism = this.dinosaur.getMetabolism();

            // Look in increasing layers (e.g. boxes) around the head. Traversers... are
            // like ogres?

            if (this.searched == false && tpe.getActiveCount() < 2) {

                this.searched = true;
                try {
                    tpe.execute(new ThreadRunnable(this, this.dinosaur) {
                        @Override
                        public void run() {
                            synchronized (world) {
                                OnionTraverser traverser = new OnionTraverser(head, LOOK_RADIUS);
                                this.ai.target = null;


                                // scans all blocks around the LOOK_RADIUS
                                for (BlockPos pos : traverser) {
                                    if (world.isBlockLoaded(pos)) {
                                        Block block = world.getBlockState(pos).getBlock();
                                        if (FoodHelper.isEdible(this.entity, this.entity.getDinosaur().getDiet(), block) && pos != this.ai.previousTarget) {
                                            this.ai.target = pos;
                                            for (int i = 0; i < 16; i++) {
                                                IBlockState state = world.getBlockState(pos);
                                                if (!state.getBlock().isLeaves(state, world, pos) && !state.getBlock().isAir(state, world, pos)) {
                                                    break;
                                                }
                                                pos = pos.down();
                                            }
                                            this.ai.moveTarget = pos;
                                            this.ai.targetVec = new Vec3d(this.ai.target.getX(), this.ai.target.getY(), this.ai.target.getZ());
                                            break;
                                        }
                                    }
                                }
                            }
                            this.ai.searched = false;
                        }
                    });
                } catch (RejectedExecutionException e) {

                }
            }

            if (this.moveTarget != null) {
                try {
                    if (metabolism.isStarving()) {
                        this.dinosaur.getNavigator().tryMoveToXYZ(this.moveTarget.getX(), this.moveTarget.getY(), this.moveTarget.getZ(), 1.2);
                    } else {
                        this.dinosaur.getNavigator().tryMoveToXYZ(this.moveTarget.getX(), this.moveTarget.getY(), this.moveTarget.getZ(), 0.7);
                    }
                }catch(Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
        }
        return false;
    }

    protected boolean feederExists() {

        if (tpe.getActiveCount() < 2) {
            World world = this.dinosaur.world;
            try {
                tpe.execute(new ThreadRunnable(this, this.dinosaur) {
                    @Override
                    public void run() {
                        synchronized (world) {
                            synchronized (this.ai) {
                                this.ai.feederExists = this.entity.getClosestFeeder() != null;
                            }
                        }
                    }
                });
            } catch (RejectedExecutionException e) {

            }
        }

        return this.feederExists;
    }

    @Override
    public boolean shouldContinueExecuting() {
        if (this.target != null && this.world.isAirBlock(this.target) && !this.dinosaur.getNavigator().noPath()) {
            this.terminateTask();
            return false;
        }

        return this.target != null;
    }

    @Override
    public void updateTask() {
        if (this.target != null) {
            try {
                Vec3d headPos = this.dinosaur.getHeadPos();
                Vec3d headVec = new Vec3d(headPos.x, this.target.getY(), headPos.z);

                if (headVec.squareDistanceTo(this.targetVec) < EAT_RADIUS) {
                    this.dinosaur.getNavigator().clearPath();

                    // TODO inadequate method for looking at block
                    this.dinosaur.getLookHelper().setLookPosition(this.target.getX(), this.target.getY(), this.target.getZ(), 30.0F, this.dinosaur.getVerticalFaceSpeed());

                    this.dinosaur.setAnimation(EntityAnimation.EATING.get());

                    if(this.target.getY() > 254)
                        return;

                    Item item = Item.getItemFromBlock(this.world.getBlockState(this.target).getBlock());

                    this.world.destroyBlock(this.target, false);

                    this.dinosaur.getMetabolism().eat(FoodHelper.getHealAmount(item));
                    FoodHelper.applyEatEffects(this.dinosaur, item);
                    this.dinosaur.heal(10.0F);

                    this.previousTarget = null;
                    this.terminateTask();
                } else {
                    this.counter++;

                    if (this.counter >= GIVE_UP_TIME) {
                        this.counter = 0;
                        this.previousTarget = this.target;
                        this.terminateTask();
                    }
                }
            }catch(NullPointerException e) {

            }
        }
    }

    private void terminateTask() {
        this.dinosaur.getNavigator().clearPath();
        this.target = null;
        this.dinosaur.setAnimation(EntityAnimation.IDLE.get());
    }

    abstract class ThreadRunnable implements Runnable {

        final DinosaurEntity entity;
        final GrazeEntityAI ai;

        ThreadRunnable(GrazeEntityAI grazeEntityAI, DinosaurEntity entity) {
            this.ai = grazeEntityAI;
            this.entity = entity;
        }
    }
}