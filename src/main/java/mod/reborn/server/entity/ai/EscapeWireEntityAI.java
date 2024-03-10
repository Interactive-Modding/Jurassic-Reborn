package mod.reborn.server.entity.ai;

import mod.reborn.server.block.BlockHandler;
import mod.reborn.server.entity.DinosaurEntity;
import mod.reborn.server.entity.ai.util.AIUtils;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.BlockPos;

public class EscapeWireEntityAI extends EntityAIBase {
    private final DinosaurEntity entity;

    public EscapeWireEntityAI(DinosaurEntity entity) {
        super();
        this.entity = entity;
        this.setMutexBits(Mutex.MOVEMENT);
    }

    @Override
    public boolean shouldExecute() {
        return this.entity.wireTicks > 0;
    }

    @Override
    public void startExecuting() {
        int searchAttempts = 0;
        this.entity.getNavigator().clearPath();
        search:
        while (searchAttempts++ < 20 && this.entity.getNavigator().noPath()) {
            int offsetX = this.entity.getRNG().nextInt(8) - this.entity.getRNG().nextInt(8);
            int offsetZ = this.entity.getRNG().nextInt(8) - this.entity.getRNG().nextInt(8);
            BlockPos pos = this.entity.getPosition().add(offsetX, 0, offsetZ);
            BlockPos surface = AIUtils.findSurface(this.entity.world, pos);
            if (surface != null) {
                Iterable<BlockPos.MutableBlockPos> surrounding = BlockPos.getAllInBoxMutable(surface.add(-1, -1, -1), surface.add(1, 1, 1));
                for (BlockPos p : surrounding) {
                    if (!this.entity.world.isBlockLoaded(pos)) {
                        continue search;
                    }
                    if (this.entity.world.getBlockState(p).getBlock() == BlockHandler.LOW_SECURITY_FENCE_WIRE) {
                        continue search;
                    }
                }
                this.entity.getNavigator().tryMoveToXYZ(surface.getX(), surface.getY(), surface.getZ(), 1.4);
            }
        }
    }

    @Override
    public boolean shouldContinueExecuting() {
        return !this.entity.getNavigator().noPath();
    }
}
