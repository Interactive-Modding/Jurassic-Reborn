package net.vit.jurassicreborn.common.entities.ai;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.AABB;
import net.vit.jurassicreborn.common.entities.DinosaurEntity;

import java.util.EnumSet;
import java.util.UUID;

public class FollowOwnerAI extends Goal {

    private static final double START_DIST_SQR = 6.0 * 6.0;   // start following if farther than 6 blocks
    private static final double STOP_DIST_SQR  = 3.0 * 3.0;   // stop when within 3 blocks
    private static final double TELEPORT_SQR   = 14.0 * 14.0; // teleport aide if beyond 14 blocks
    private static final double NAV_SPEED      = 0.8;

    private final DinosaurEntity dino;
    private net.minecraft.world.entity.player.Player owner;

    private int   recalcCooldown;     // ticks until next path recompute
    private float oldWaterMalus;

    public FollowOwnerAI(DinosaurEntity dino) {
        this.dino = dino;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (dino.isCarcass() || dino.isMovementBlocked() || !dino.isAlive()) return false;
        if (dino.getOrder() != DinosaurEntity.Order.FOLLOW)                  return false;

        UUID id = dino.getOwner();
        if (id == null) return false;

        owner = dino.level().getPlayerByUUID(id);
        if (owner == null || !owner.isAlive()) return false;

        // begin only if we’re a bit away
        return dino.distanceToSqr(owner) > START_DIST_SQR;
    }

    @Override
    public boolean canContinueToUse() {
        if (owner == null || !owner.isAlive())                 return false;
        if (dino.getOrder() != DinosaurEntity.Order.FOLLOW)    return false;
        if (dino.isCarcass() || dino.isMovementBlocked())      return false;

        // keep going until close enough
        return dino.distanceToSqr(owner) > STOP_DIST_SQR;
    }

    @Override
    public void start() {
        recalcCooldown = 0;
        oldWaterMalus = dino.getPathfindingMalus(BlockPathTypes.WATER);
        // Don’t fear water while following
        dino.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
    }

    @Override
    public void stop() {
        dino.getNavigation().stop();
        dino.setPathfindingMalus(BlockPathTypes.WATER, oldWaterMalus);
        owner = null;
    }

    @Override
    public void tick() {
        if (owner == null) return;

        dino.getLookControl().setLookAt(owner, 10.0F, dino.getMaxHeadXRot());

        // gentle teleport assist if we’re badly stuck/far
        if (dino.distanceToSqr(owner) > TELEPORT_SQR) {
            tryTeleportNearOwner();
        }

        if (recalcCooldown-- <= 0) {
            recalcCooldown = 10; // ~0.5s
            dino.getNavigation().moveTo(owner, NAV_SPEED);
        }
    }

    /* ------------------------------------------------------------------ */
    /* Teleport helper (short hop near owner if far & space is clear)     */
    /* ------------------------------------------------------------------ */
    private void tryTeleportNearOwner() {
        // Try a few offsets around the owner to find solid ground with headroom
        BlockPos ownerPos = owner.blockPosition();
        for (int dx = -2; dx <= 2; dx++) {
            for (int dz = -2; dz <= 2; dz++) {
                if (Math.abs(dx) < 2 && Math.abs(dz) < 2) continue; // prefer edge ring first
                BlockPos candidate = ownerPos.offset(dx, 0, dz);

                if (isTeleportFriendly(candidate)) {
                    dino.moveTo(candidate.getX() + 0.5, candidate.getY(), candidate.getZ() + 0.5,
                            dino.getYRot(), dino.getXRot());
                    dino.getNavigation().stop();
                    return;
                }
            }
        }
    }

    private boolean isTeleportFriendly(BlockPos pos) {
        // need solid-ish ground, no fluid at feet, and 2-block headroom
        BlockPos below = pos.below();
        BlockState ground = dino.level().getBlockState(below);

        if (!ground.getCollisionShape(dino.level(), below).isEmpty()) {
            if (dino.level().getFluidState(pos).is(FluidTags.WATER) || dino.level().getFluidState(pos).is(FluidTags.LAVA)) {
                return false;
            }
            // headroom: ensure two blocks above are passable/air
            AABB head = new AABB(pos).expandTowards(0, 2, 0);
            return dino.level().noCollision(dino, head);
        }
        return false;
    }
}
