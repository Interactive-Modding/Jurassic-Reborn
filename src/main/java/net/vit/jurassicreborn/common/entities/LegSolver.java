package net.vit.jurassicreborn.common.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.VoxelShape;

public class LegSolver {
    public final Leg[] legs;

    public LegSolver(Leg... legs) {
        this.legs = legs;
    }

    /** Convenience: uses the entity's body yaw (yBodyRot) like 1.12's renderYawOffset. */
    public final void update(LivingEntity entity, float scale) {
        this.update(entity, entity.yBodyRot, scale);
    }

    /** Core update using a provided yaw (degrees). */
    public final void update(Entity entity, float yaw, float scale) {
        double sideTheta = Math.toRadians(yaw);
        double sideX = Math.cos(sideTheta) * scale;
        double sideZ = Math.sin(sideTheta) * scale;

        double forwardTheta = sideTheta + Math.PI / 2.0;
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

            double x = entity.getX() + sideX * this.side + forwardX * this.forward;
            double y = entity.getY();
            double z = entity.getZ() + sideZ * this.side + forwardZ * this.forward;

            float settled = this.settle(entity, x, y, z, this.height);
            this.height = Mth.clamp(settled, -this.range * scale, this.range * scale);
        }

        private float settle(Entity entity, double x, double y, double z, float height) {
            Level level = entity.level;

            int ix = Mth.floor(x);
            int iy = Mth.floor(y + 1.0E-3D);
            int iz = Mth.floor(z);
            BlockPos pos = new BlockPos(ix, iy, iz);

            float dist = this.getDistance(level, pos);
            double fracY = y % 1.0;

            if (1 - dist < 1e-3) {
                dist = this.getDistance(level, pos.below()) + (float) fracY;
            } else {
                dist -= 1 - (float) fracY;
            }

            boolean onGround = entity.isOnGround();
            if (onGround && height <= dist) {
                return height == dist ? height : Math.min(height + this.getFallSpeed(), dist);
            } else if (height > 0) {
                return Math.max(height - this.getRiseSpeed(), dist);
            }
            return height;
        }


        /** Returns how far below the foot the solid surface is within this block column: 0..1 */
        private float getDistance(Level level, BlockPos pos) {
            BlockState state = level.getBlockState(pos);
            VoxelShape shape = state.getCollisionShape(level, pos);
            if (shape.isEmpty()) return 1.0F;

            // Using the bounding box of the shape as a reasonable approximation
            AABB aabb = shape.bounds();
            double top = Math.min(aabb.maxY, 1.0);
            return (float) (1.0 - top);
        }

        protected float getFallSpeed() {
            return 0.3F;
        }

        protected float getRiseSpeed() {
            return 0.5F;
        }
    }
}
