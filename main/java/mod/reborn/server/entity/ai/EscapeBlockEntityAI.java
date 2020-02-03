package mod.reborn.server.entity.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import mod.reborn.server.entity.ai.util.AIUtils;

/**
 * Copyright 2016 Andrew O. Mellinger
 */
public class EscapeBlockEntityAI extends EntityAIBase {
    private final EntityLiving _entity;

    public EscapeBlockEntityAI(EntityLiving entity) {
        super();
        this._entity = entity;
        this.setMutexBits(Mutex.MOVEMENT);
    }

    @Override
    public boolean shouldExecute() {
        World world = this._entity.getEntityWorld();
        return world.isAirBlock(this._entity.getPosition());
    }

    @Override
    public void startExecuting() {
        BlockPos surface = AIUtils.findSurface(this._entity);
        if (surface != null) {
            this._entity.getNavigator().tryMoveToXYZ(surface.getX(), surface.getY(), surface.getZ(), 1.0);
        }
    }

    @Override
    public boolean shouldContinueExecuting() {
        return (!this._entity.getNavigator().noPath());
    }
}
