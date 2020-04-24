package mod.reborn.server.entity.ai.metabolism;

import mod.reborn.server.block.entity.FeederBlockEntity;
import mod.reborn.server.entity.DinosaurEntity;
import mod.reborn.server.entity.ai.Mutex;
import mod.reborn.server.util.GameRuleHandler;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.Path;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public class FeederEntityAI extends EntityAIBase {
    protected DinosaurEntity dinosaur;
    protected Path path;
    protected BlockPos feederPosition;

    public FeederEntityAI(DinosaurEntity dinosaur) {
        this.dinosaur = dinosaur;
        this.setMutexBits(Mutex.METABOLISM);
    }

    @Override
    public boolean shouldExecute() {
        if (!this.dinosaur.isCarcass() && !this.dinosaur.isMovementBlocked() && GameRuleHandler.DINO_METABOLISM.getBoolean(this.dinosaur.world)) {
            if (this.dinosaur.getMetabolism().isHungry()) {
                BlockPos feeder = this.dinosaur.getClosestFeeder();
                if (feeder != null) {
                    this.feederPosition = feeder;
                    this.path = this.dinosaur.getNavigator().getPathToPos(this.feederPosition);
                    if (this.path != null) {
                        return this.dinosaur.getNavigator().setPath(this.path, 1.0);
                    }
                }
            }
        }

        return false;
    }

    @Override
    public void updateTask() {
        if (this.path == null)
            return;
        try {
            this.dinosaur.getNavigator().setPath(this.path, 1.0);

            if (!this.dinosaur.world.isRemote && (this.dinosaur.getDistance(this.feederPosition.getX(), this.feederPosition.getY(), this.feederPosition.getZ()) <= this.dinosaur.width + this.dinosaur.width || this.path.isFinished())) {
                TileEntity tile = this.dinosaur.world.getTileEntity(this.feederPosition);
                if (tile instanceof FeederBlockEntity) {
                    FeederBlockEntity feeder = (FeederBlockEntity) tile;
                    if (feeder.canFeedDinosaur(this.dinosaur)) {
                        feeder.setOpen(true);
                        feeder.setFeeding(this.dinosaur);
                    }
                }
                this.resetTask();
            }
        } catch (NullPointerException e) {

        }
    }

    @Override
    public void resetTask() {
        this.path = null;
        this.dinosaur.getNavigator().clearPath();
    }

    @Override
    public boolean shouldContinueExecuting() {
        return this.dinosaur != null && this.path != null && this.dinosaur.getMetabolism().isHungry();
    }
}
