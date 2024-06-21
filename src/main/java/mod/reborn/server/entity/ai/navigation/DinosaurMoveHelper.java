package mod.reborn.server.entity.ai.navigation;

import mod.reborn.server.dinosaur.Dinosaur;
import mod.reborn.server.entity.DinosaurEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityJumpHelper;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.pathfinding.NodeProcessor;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.math.MathHelper;

public class DinosaurMoveHelper extends EntityMoveHelper {
    private DinosaurEntity dinosaur;

    public DinosaurMoveHelper(DinosaurEntity entity) {
        super(entity);
        this.dinosaur = entity;
    }

    @Override
    public void onUpdateMoveHelper() {
        float speedAttribute = (float) this.entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue();

        if (this.action == EntityMoveHelper.Action.STRAFE) {
            float moveSpeed = (float) (this.speed * speedAttribute);
            float forward = this.moveForward;
            float strafe = this.moveStrafe;
            float moveDistance = MathHelper.sqrt(forward * forward + strafe * strafe);

            if (moveDistance < 1.0F) {
                moveDistance = 0.8F;
            }

            moveDistance = moveSpeed / moveDistance;
            forward = forward * moveDistance;
            strafe = strafe * moveDistance;
            float rotationX = MathHelper.sin(this.entity.rotationYaw * 0.017453292F);
            float rotationZ = MathHelper.cos(this.entity.rotationYaw * 0.017453292F);
            float moveX = forward * rotationZ - strafe * rotationX;
            float moveZ = strafe * rotationZ + forward * rotationX;
            PathNavigate navigator = this.entity.getNavigator();

            if (navigator != null) {
                NodeProcessor nodeProcessor = navigator.getNodeProcessor();

                if (nodeProcessor != null && nodeProcessor.getPathNodeType(this.entity.world, MathHelper.floor(this.entity.posX + moveX), MathHelper.floor(this.entity.posY), MathHelper.floor(this.entity.posZ + moveZ)) != PathNodeType.WALKABLE) {
                    this.moveForward = 0.9F;
                    this.moveStrafe = 0.0F;
                    moveSpeed = speedAttribute;
                }
            }

            this.entity.setAIMoveSpeed(moveSpeed);
            this.entity.setMoveForward(this.moveForward);
            this.entity.setMoveStrafing(this.moveStrafe);
            this.action = EntityMoveHelper.Action.WAIT;
        } else if (this.action == EntityMoveHelper.Action.MOVE_TO) {
            this.action = EntityMoveHelper.Action.WAIT;
            double deltaX = this.posX - this.entity.posX;
            double deltaZ = this.posZ - this.entity.posZ;
            double deltaY = this.posY - this.entity.posY;
            double delta = deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ;

            if (delta < 2.5E-07) {
                this.entity.setMoveForward(0.0F);
                return;
            }

            float movementDirection = (float) ((MathHelper.atan2(deltaZ, deltaX) * (180D / Math.PI)) - 90.0F);
            this.entity.rotationYaw = this.limitAngle(this.entity.rotationYaw, movementDirection, 60);
            this.entity.setAIMoveSpeed((float) (this.speed * speedAttribute));

            if (deltaY > this.entity.stepHeight && deltaX * deltaX + deltaZ * deltaZ < Math.max(1.0F, this.entity.width + (deltaY * deltaY))) {
                EntityJumpHelper jumpHelper = this.entity.getJumpHelper();
                if (jumpHelper instanceof DinosaurJumpHelper && !this.entity.isInLava() && !this.entity.isInWater()) {
                    ((DinosaurJumpHelper) jumpHelper).jump((int) Math.ceil(deltaY));
                } else {
                    jumpHelper.setJumping();
                }
            }
        } else {
            this.entity.setMoveForward(0.0F);
        }
    }
}