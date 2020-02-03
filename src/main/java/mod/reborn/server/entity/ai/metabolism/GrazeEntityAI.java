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

public class GrazeEntityAI extends EntityAIBase {
    public static final int EAT_RADIUS = 6;// was 25
    public static final int LOOK_RADIUS = 16;
    private static final int GIVE_UP_TIME = 400;// 14*20 counter = 14 seconds (ish?)

    protected DinosaurEntity dinosaur;
    protected BlockPos target;
    protected BlockPos moveTarget;

    private int counter;
    private World world;
    private BlockPos previousTarget;
    private Vec3d targetVec;

    public GrazeEntityAI(DinosaurEntity dinosaur) {
        this.dinosaur = dinosaur;
        this.setMutexBits(Mutex.METABOLISM);
    }

    @Override
    public boolean shouldExecute() {
        if (!(this.dinosaur.isDead || this.dinosaur.isCarcass() || !GameRuleHandler.DINO_METABOLISM.getBoolean(this.dinosaur.world)) && this.dinosaur.getMetabolism().isHungry()) {
            if (!this.dinosaur.getMetabolism().isStarving() && this.dinosaur.getClosestFeeder() != null) {
                return false;
            }

            // This gets called once to initiate.  Here's where we find the plant and start movement
            Vec3d headPos = this.dinosaur.getHeadPos();
            BlockPos head = new BlockPos(headPos.x, headPos.y, headPos.z);

            //world the animal currently inhabits
            this.world = this.dinosaur.world;

            MetabolismContainer metabolism = this.dinosaur.getMetabolism();

            // Look in increasing layers (e.g. boxes) around the head. Traversers... are like ogres?
            OnionTraverser traverser = new OnionTraverser(head, LOOK_RADIUS);
            this.target = null;

            //scans all blocks around the LOOK_RADIUS
            for (BlockPos pos : traverser) {
                Block block = this.world.getBlockState(pos).getBlock();
                if (FoodHelper.isEdible(this.dinosaur, this.dinosaur.getDinosaur().getDiet(), block) && pos != this.previousTarget) {
                    this.target = pos;
                    for (int i = 0; i < 16; i++) {
                        IBlockState state = this.world.getBlockState(pos);
                        if (!state.getBlock().isLeaves(state, this.world, pos) && !state.getBlock().isAir(state, this.world, pos)) {
                            break;
                        }
                        pos = pos.down();
                    }
                    this.moveTarget = pos;
                    this.targetVec = new Vec3d(this.target.getX(), this.target.getY(), this.target.getZ());
                    break;
                }
            }

            if (this.moveTarget != null) {
                if (metabolism.isStarving()) {
                    this.dinosaur.getNavigator().tryMoveToXYZ(this.moveTarget.getX(), this.moveTarget.getY(), this.moveTarget.getZ(), 1.2);
                } else {
                    this.dinosaur.getNavigator().tryMoveToXYZ(this.moveTarget.getX(), this.moveTarget.getY(), this.moveTarget.getZ(), 0.7);
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public void startExecuting() {
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
            Vec3d headPos = this.dinosaur.getHeadPos();
            Vec3d headVec = new Vec3d(headPos.x, this.target.getY(), headPos.z);

            if (headVec.squareDistanceTo(this.targetVec) < EAT_RADIUS) {
                this.dinosaur.getNavigator().clearPath();

                // TODO inadequate method for looking at block
                this.dinosaur.getLookHelper().setLookPosition(this.target.getX(), this.target.getY(), this.target.getZ(), 30.0F, this.dinosaur.getVerticalFaceSpeed());

                this.dinosaur.setAnimation(EntityAnimation.EATING.get());

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
        }
    }

    private void terminateTask() {
        this.dinosaur.getNavigator().clearPath();
        this.target = null;
        this.dinosaur.setAnimation(EntityAnimation.IDLE.get());
    }
}
