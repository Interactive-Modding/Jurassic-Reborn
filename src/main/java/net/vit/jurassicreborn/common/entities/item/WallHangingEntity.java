package net.vit.jurassicreborn.common.entities.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SupportType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.List;

/**
 * Small helper base class for wall mounted art (blueprints, murals, ...).
 *
 * <p>The vanilla {@link HangingEntity} bounding-box logic assumes the anchor
 * point is stable but in practice the custom entities swap their width/height
 * at runtime (when the server syncs the selected variant).  Without reapplying
 * the placement maths every time the dimensions change the client ends up with
 * a slightly different anchor, which manifests as the entity "sliding" a few
 * pixels after placement.</p>
 *
 * <p>The calculations below are derived from the painting implementation in
 * vanilla with a couple of helper hooks so both Blueprint and Mural entities
 * can share the fix.</p>
 */
public abstract class WallHangingEntity extends HangingEntity {

    protected WallHangingEntity(EntityType<? extends WallHangingEntity> type, Level level) {
        super(type, level);
    }

    protected WallHangingEntity(EntityType<? extends WallHangingEntity> type, Level level, BlockPos pos) {
        super(type, level, pos);
    }

    @Override
    public boolean survives() {
        if (!this.level.noCollision(this, this.getBoundingBox())) {
            return false;
        }

        Direction facing = this.direction;
        if (facing == null) {
            return false;
        }

        int widthBlocks = Math.max(1, this.getWidth() / 16);
        int heightBlocks = Math.max(1, this.getHeight() / 16);

        Direction right = facing.getCounterClockWise();
        BlockPos backingStart = this.pos.relative(facing.getOpposite())
                .relative(right, -(widthBlocks / 2) + 1);

        for (int x = 0; x < widthBlocks; x++) {
            for (int y = 0; y < heightBlocks; y++) {
                BlockPos check = backingStart.relative(right, x).below(y);
                BlockState state = this.level.getBlockState(check);
                if (!state.isFaceSturdy(this.level, check, facing, SupportType.FULL)) {
                    return false;
                }
            }
        }

        List<Entity> overlaps = this.level.getEntities(this, this.getBoundingBox(), this::isSameKind);
        return overlaps.isEmpty();
    }

    /** Allow subclasses to tighten the overlap test if required. */
    protected boolean isSameKind(Entity other) {
        return other.getType() == this.getType();
    }

    @Override
    protected void recalculateBoundingBox() {
        Direction facing = this.direction;
        if (facing == null) {
            return;
        }

        double anchorX = this.pos.getX() + 0.5D;
        double anchorY = this.pos.getY() + 0.5D;
        double anchorZ = this.pos.getZ() + 0.5D;

        double wallOffset = 0.46875D; // vanilla painting offset
        double offsetX = offsetForPixels(this.getWidth());
        double offsetY = offsetForPixels(this.getHeight());

        anchorX -= facing.getStepX() * wallOffset;
        anchorZ -= facing.getStepZ() * wallOffset;
        anchorY += offsetY;

        Direction right = facing.getCounterClockWise();
        anchorX += offsetX * right.getStepX();
        anchorZ += offsetX * right.getStepZ();

        double width = this.getWidth();
        double height = this.getHeight();
        double depth = this.getWidth();

        if (facing.getAxis() == Direction.Axis.Z) {
            depth = 1.0D;
        } else {
            width = 1.0D;
        }

        width /= 32.0D;
        height /= 32.0D;
        depth /= 32.0D;

        this.setPosRaw(anchorX, anchorY, anchorZ);
        this.setBoundingBox(new AABB(anchorX - width, anchorY - height, anchorZ - depth,
                anchorX + width, anchorY + height, anchorZ + depth));
    }

    private double offsetForPixels(int pixels) {
        return (pixels % 32 == 0) ? 0.5D : 0.0D;
    }

    @Override
    public void refreshDimensions() {
        Direction current = this.direction;
        BlockPos anchor = this.pos;
        double prevX = this.getX();
        double prevY = this.getY();
        double prevZ = this.getZ();

        super.refreshDimensions();

        this.setPosRaw(prevX, prevY, prevZ);
        this.pos = anchor;
        if (current != null) {
            this.setDirection(current);
        }
    }

    /** Apply a relative move to the hanging anchor, then rebuild the AABB. */
    public void moveToRelative(double x, double y, double z) {
        BlockPos delta = new BlockPos(x - this.getX(), y - this.getY(), z - this.getZ());
        this.pos = this.pos.offset(delta);
        this.recalculateBoundingBox();
    }
}
