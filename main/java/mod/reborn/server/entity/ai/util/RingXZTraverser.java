package mod.reborn.server.entity.ai.util;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import java.util.Iterator;

/**
 * Copyright 2016 Andrew O. Mellinger
 */
public class RingXZTraverser implements Iterable<BlockPos> {
    private final int _radius;
    private BlockPos _center;

    public RingXZTraverser(BlockPos center, int radius) {
        this._center = center;
        this._radius = radius;
    }

    @Override
    public Iterator<BlockPos> iterator() {
        return new RingIter(this._radius);
    }

    private class RingIter implements Iterator<BlockPos> {
        private EnumFacing _facing = EnumFacing.EAST;
        private int _x, _minX, _maxX;
        private int _y;
        private int _z, _minZ, _maxZ;
        private boolean _done = false;

        public RingIter(int radius) {
            this._x = this._minX = RingXZTraverser.this._center.getX() - radius;
            this._z = this._minZ = RingXZTraverser.this._center.getZ() - radius;
            this._y = RingXZTraverser.this._center.getY();
            this._maxX = RingXZTraverser.this._center.getX() + radius;
            this._maxZ = RingXZTraverser.this._center.getZ() + radius;
        }

        @Override
        public boolean hasNext() {
            return !this._done;
        }

        @Override
        public BlockPos next() {
            BlockPos pos = new BlockPos(this._x, this._y, this._z);
            this.walkPerimeter();
            return pos;
        }

        @Override
        public void remove() {
            // Nothing
        }

        // Walk around the outside perimeter
        private void walkPerimeter() {
            switch (this._facing) {
                case EAST:
                    if (this._x == this._maxX) {
                        ++this._z;
                        this._facing = EnumFacing.SOUTH;
                    } else {
                        ++this._x;
                    }
                    break;

                case SOUTH:
                    if (this._z == this._maxZ) {
                        --this._x;
                        this._facing = EnumFacing.WEST;
                    } else {
                        ++this._z;
                    }
                    break;

                case WEST:
                    if (this._x == this._minX) {
                        --this._z;
                        this._facing = EnumFacing.NORTH;
                    } else {
                        --this._x;
                    }
                    break;

                case NORTH:
                    --this._z;
                    if (this._z == this._minZ) {
                        this._done = true;
                    }
                    break;
			default:
				break;
            }
        }
    }
}
