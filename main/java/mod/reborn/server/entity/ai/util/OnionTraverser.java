package mod.reborn.server.entity.ai.util;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Iterator;

/**
 * Copyright 2016 Timeless Mod Team.
 * <p>
 * This traverser iterates "layers" (like an onion) around the center point.  Being Minecraft,
 * the layers are boxes around the center point.
 */
public class OnionTraverser implements Iterable<BlockPos> {
    private static final Logger LOGGER = LogManager.getLogger();
    private final BlockPos _center;
    private final int _maxRadius;

    public OnionTraverser(BlockPos center, int radius) {
        this._center = center;
        this._maxRadius = radius;
    }

    @Override
    public Iterator<BlockPos> iterator() {
        return new OnionIterator();
    }

    public class OnionIterator implements Iterator<BlockPos> {
        private final int _maxCounter;
        // The max numbers are inclusive so <= rather than <
        private int _x, _minX, _maxX;
        private int _y;
        private int _z, _minZ, _maxZ;
        private int _currentRadius = 0;
        private int _yCounter = 1;
        private EnumFacing _facing;
        private boolean _returnedCenter = false;

        public OnionIterator() {
            this._maxCounter = OnionTraverser.this._maxRadius * 2 + 1;
            this._currentRadius = 1;
            this._y = OnionTraverser.this._center.getY();
            this.initRing();
        }

        @Override
        public boolean hasNext() {
            return this._currentRadius <= OnionTraverser.this._maxRadius;
        }

        @Override
        public BlockPos next() {
            if (!this._returnedCenter) {
                this._returnedCenter = true;
                return OnionTraverser.this._center;
            }

            // Construct the current one
            BlockPos retVal = new BlockPos(this._x, this._y, this._z);

            if (this._y == OnionTraverser.this._center.getY() + OnionTraverser.this._maxRadius || this._y == OnionTraverser.this._center.getY() - OnionTraverser.this._maxRadius) {
                this.walkSlab();
            } else {
                this.walkPerimeter();
            }

            //BlockPos nextVal = new BlockPos(_x, _y, _z);
            //LOGGER.info(_idx++ + ", facing=" + _facing + ", yCounter=" + _yCounter + "/" + _maxCounter + ", radius=" + _currentRadius + ", pos=" + retVal + ", nextVal=" + nextVal);

            return retVal;
        }

        @Override
        public void remove() {
            // We do nothing
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
                        this.nextLayer();
                    }
                    break;
			default:
				break;
            }
        }

        //private int _idx = 0;

        // We go back an forth (east, west) across the slab until we get past maxZ
        // then go to the next layer.
        private void walkSlab() {
            switch (this._facing) {
                case EAST:
                    if (this._x == this._maxX) {
                        ++this._z;
                        this._facing = EnumFacing.WEST;
                    } else {
                        ++this._x;
                    }
                    break;

                case WEST:
                    if (this._x == this._minX) {
                        ++this._z;
                        this._facing = EnumFacing.EAST;
                    } else {
                        --this._x;
                    }
                    break;
			default:
				break;
            }

            if (this._z > this._maxZ) {
                this.nextLayer();
            }
        }

        // Computes the next layer
        private void nextLayer() {
            // We are at the end.  We don't actually want thw return the end because
            // it has already been done as the start point.

            // Move up or down
            // 0,0,1,1,2,2,3,3,4,4,
            ++this._yCounter;

            if (this._yCounter > this._maxCounter) {
                ++this._currentRadius;
                this.initRing();
            }

            this._x = this._minX;
            this._z = this._minZ;
            if ((this._yCounter & 0x1) == 1) {
                this._y = OnionTraverser.this._center.getY() + this._yCounter / 2;
            } else {
                this._y = OnionTraverser.this._center.getY() - this._yCounter / 2;
            }

            this._facing = EnumFacing.EAST;

            //LOGGER.info("Layer. radius=" + _currentRadius + ", x=" + _x + ", y=" + _y + ", z=" + _z + ", facing=" + _facing);
        }

        // Compute the ring values, min, max, etc.
        private void initRing() {
            this._yCounter = 1;
            this._x = this._minX = OnionTraverser.this._center.getX() - this._currentRadius;
            this._z = this._minZ = OnionTraverser.this._center.getZ() - this._currentRadius;
            this._y = OnionTraverser.this._center.getY();
            this._maxX = OnionTraverser.this._center.getX() + this._currentRadius;
            this._maxZ = OnionTraverser.this._center.getZ() + this._currentRadius;
            this._facing = EnumFacing.EAST;

            //LOGGER.info("Ring. radius=" + _currentRadius + ", x=" + _minX + "-" + _maxX + ", z=" + _minZ + "-" + _maxZ );
        }
    }
}

