package mod.reborn.server.entity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.World;

import javax.vecmathimpl.Vector3d;

public class LegSolver {
    public final Leg[] legs;

    public LegSolver(Leg... legs) {
        this.legs = legs;
    }

    public final void update(LivingEntity entity, float scale) {
        this.update(entity, entity.renderYawOffset, scale);
    }

    public final void update(Entity entity, float yaw, float scale) {
        double sideTheta = yaw / (180 / Math.PI);
        double sideX = Math.cos(sideTheta) * scale;
        double sideZ = Math.sin(sideTheta) * scale;
        double forwardTheta = sideTheta + Math.PI / 2;
        double forwardX = Math.cos(forwardTheta) * scale;
        double forwardZ = Math.sin(forwardTheta) * scale;
        for (Leg leg : this.legs) {
            leg.update(entity, sideX, sideZ, forwardX, forwardZ, scale);
        }
    }

    public static class Leg {
        public final float forward;

        public final float side;

        private final float range;

        private float height;

        private float prevHeight;

        public Leg(float forward, float side, float range) {
            this.forward = forward;
            this.side = side;
            this.range = range;
        }

        public final float getHeight(float delta) {
            return this.prevHeight + (this.height - this.prevHeight) * delta;
        }

        public void update(Entity entity, double sideX, double sideZ, double forwardX, double forwardZ, float scale) {
            this.prevHeight = this.height;
            float settledHeight = this.settle(entity, entity.getPosX() + sideX * this.side + forwardX * this.forward, entity.getPosY(), entity.getPosZ() + sideZ * this.side + forwardZ * this.forward, this.height);
            this.height = MathHelper.clamp(settledHeight, -this.range * scale, this.range * scale);
        }

        private float settle(Entity entity, double x, double y, double z, float height) {
            BlockPos pos = new BlockPos(x, y + 1e-3, z);
            float dist = this.getDistance(entity.world, pos);
            if (1 - dist < 1e-3) {
                dist = this.getDistance(entity.world, pos.down()) + (float) y % 1;
            } else {
                dist -= 1 - (y % 1);
            }
            if (entity.isOnGround() && height <= dist) {
                return height == dist ? height : Math.min(height + this.getFallSpeed(), dist);
            } else if (height > 0) {
                return Math.max(height - this.getRiseSpeed(), dist);
            }
            return height;
        }

        private float getDistance(World world, BlockPos pos) {
            BlockState state = world.getBlockState(pos);
            AxisAlignedBB aabb = state.getCollisionShape(world, pos).getBoundingBox();
            return 1 - Math.min((float) aabb.maxY, 1);
        }

        protected float getFallSpeed() {
            return 0.3F;
        }

        protected float getRiseSpeed() {
            return 0.5F;
        }
    }
}
