package mod.reborn.server.entity.ai.metabolism;

import mod.reborn.client.model.animation.EntityAnimation;
import mod.reborn.server.entity.DinosaurEntity;
import mod.reborn.server.entity.MetabolismContainer;
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

public class DrinkEntityAI extends EntityAIBase {
    protected DinosaurEntity dinosaur;

    protected Path path;
    protected BlockPos pos;

    protected int giveUpTime;

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
                    BlockPos water = null;
                    OnionTraverser traverser = new OnionTraverser(this.dinosaur.getPosition(), 32);
                    for (BlockPos pos : traverser) {
                        if (world.getBlockState(pos).getMaterial() == Material.WATER) {
                            BlockPos surface = AIUtils.findSurface(world, pos);
                            BlockPos shore = AIUtils.findShore(world, surface.down());
                            if (shore != null) {
                                IBlockState state = world.getBlockState(shore);
                                if (state.isFullBlock()) {
                                    Path path = this.dinosaur.getNavigator().getPathToPos(shore);
                                    if (path != null && path.getCurrentPathLength() != 0) {
                                        this.path = path;
                                        water = shore;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    if (water != null) {
                        this.pos = water;
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
        }
        if (this.path != null) {
            this.dinosaur.getNavigator().setPath(this.path, 1.0);
            Vec3d center = new Vec3d(this.pos.getX() + 0.5, this.pos.getY() + 0.5, this.pos.getZ() + 0.5);
            if (this.path.isFinished() || (this.dinosaur.getEntityBoundingBox().expand(2, 2, 2).intersectsWithXY(center) && this.giveUpTime < 10)) {
                this.dinosaur.getLookHelper().setLookPosition(this.pos.getX() + 0.5, this.pos.getY() + 0.5, this.pos.getZ() + 0.5, 10.0F, 10.0F);
                this.dinosaur.setAnimation(EntityAnimation.DRINKING.get());
                MetabolismContainer metabolism = this.dinosaur.getMetabolism();
                metabolism.setWater(metabolism.getWater() + (metabolism.getMaxWater() / 8));
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
}
