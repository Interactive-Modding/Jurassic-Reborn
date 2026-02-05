package net.vit.jurassicreborn.common.entities.ai.navigation;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.JumpControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.NodeEvaluator;
import net.vit.jurassicreborn.common.entities.DinosaurEntity;

public class DinosaurMoveHelper extends MoveControl {
    private final DinosaurEntity dinosaur;

    public DinosaurMoveHelper(DinosaurEntity entity) {
        super(entity);
        this.dinosaur = entity;
    }

    @Override
    public void tick() {
        final float baseSpeed = (float) this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED);
        final PathNavigation navigator = this.mob.getNavigation();
        final NodeEvaluator nodeEvaluator = navigator != null ? navigator.getNodeEvaluator() : null;

        switch (this.operation) {
            case STRAFE -> {
                float moveSpeed = (float) (this.speedModifier * baseSpeed);
                float forward = this.strafeForwards;
                float strafe  = this.strafeRight;
                float len = Mth.sqrt(forward * forward + strafe * strafe);

                if (len < 1.0F) {
                    // match old behavior (don’t let it drop to 0 and stall)
                    len = 0.8F;
                }

                // normalize then scale to our target move speed
                float scale = moveSpeed / len;
                forward *= scale;
                strafe  *= scale;

                // rotate local (forward, strafe) into world-space
                float yawRad = this.mob.getYRot() * ((float)Math.PI / 180F);
                float sin = Mth.sin(yawRad);
                float cos = Mth.cos(yawRad);
                float mx = forward * cos - strafe * sin;
                float mz = strafe  * cos + forward * sin;

                // if the next tile isn't walkable, bias to forward only
                if (nodeEvaluator != null) {
                    BlockPathTypes type = nodeEvaluator.getBlockPathType(
                            this.mob.level(),
                            Mth.floor(this.mob.getX() + (double) mx),
                            this.mob.getBlockY(),
                            Mth.floor(this.mob.getZ() + (double) mz)
                    );
                    if (type != BlockPathTypes.WALKABLE) {
                        this.strafeForwards = 0.9F;
                        this.strafeRight = 0.0F;
                        moveSpeed = baseSpeed; // fall back to base
                    }
                }

                this.mob.setSpeed(moveSpeed);
                this.mob.setZza(this.strafeForwards);
                this.mob.setXxa(this.strafeRight);
                this.operation = Operation.WAIT;
            }

            case MOVE_TO -> {
                this.operation = Operation.WAIT;

                double dx = this.wantedX - this.mob.getX();
                double dz = this.wantedZ - this.mob.getZ();
                double dy = this.wantedY - this.mob.getY();
                double d2 = dx * dx + dy * dy + dz * dz;

                if (d2 < 2.5000003E-7D) {
                    this.mob.setZza(0.0F);
                    return;
                }

                float desiredYaw = (float)(Mth.atan2(dz, dx) * (180F / (float)Math.PI)) - 90.0F;
                this.mob.setYRot(this.rotlerp(this.mob.getYRot(), desiredYaw, 60.0F));
                this.mob.setSpeed((float) (this.speedModifier * baseSpeed));
                float stepHeight = this.mob.getStepHeight();
                if (dy > (double) stepHeight && (dx * dx + dz * dz) < (double) Math.max(1.0F, this.mob.getBbWidth() + (dy * dy))) {
                    JumpControl jumpHelper = this.mob.getJumpControl();
                    if (jumpHelper instanceof DinosaurJumpHelper && !this.mob.isInLava() && !this.mob.isInWater()) {
                        ((DinosaurJumpHelper) jumpHelper).jump((int) Math.ceil(dy));
                    } else {
                        jumpHelper.jump();
                    }
                }
            }

            case JUMPING -> {
                // keep speed while in vanilla jumping state
                this.mob.setSpeed((float) (this.speedModifier * baseSpeed));
                if (this.mob.onGround()) {
                    this.operation = Operation.WAIT;
                }
            }

            case WAIT -> {
                // Clear forward input to avoid creeping
                this.mob.setZza(0.0F);
                this.mob.setXxa(0.0F);
            }
        }
    }


    private boolean isWalkableRelative(float relX, float relZ) {
        PathNavigation nav = this.mob.getNavigation();
        if (nav == null) return true;
        NodeEvaluator eval = nav.getNodeEvaluator();
        if (eval == null) return true;

        BlockPos probe = new BlockPos(
                Mth.floor(this.mob.getX() + (double) relX),
                this.mob.getBlockY(),
                Mth.floor(this.mob.getZ() + (double) relZ)
        );
        return eval.getBlockPathType(this.mob.level(), probe.getX(), probe.getY(), probe.getZ()) == BlockPathTypes.WALKABLE;
    }
}
