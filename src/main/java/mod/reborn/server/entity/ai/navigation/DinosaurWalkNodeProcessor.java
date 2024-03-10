package mod.reborn.server.entity.ai.navigation;

import mod.reborn.server.block.entity.ElectricFenceWireBlockEntity;
import mod.reborn.server.block.fence.ElectricFenceBaseBlock;
import mod.reborn.server.block.fence.ElectricFencePoleBlock;
import mod.reborn.server.block.fence.ElectricFenceWireBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.BlockWall;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Blocks;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.pathfinding.WalkNodeProcessor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import mod.reborn.server.dinosaur.Dinosaur;

import javax.annotation.Nullable;

public class DinosaurWalkNodeProcessor extends WalkNodeProcessor {
    private Dinosaur dinosaur;

    public DinosaurWalkNodeProcessor(Dinosaur dinosaur) {
        this.dinosaur = dinosaur;
    }

    @Override
    public int findPathOptions(PathPoint[] pathOptions, PathPoint currentPoint, PathPoint targetPoint, float maxDistance) {
        int optionIndex = 0;
        int stepHeight = 0;
        PathNodeType type = this.getPathNodeType(this.entity, currentPoint.x, currentPoint.y + 1, currentPoint.z);

        if (this.entity.getPathPriority(type) >= 0.0F) {
            stepHeight = MathHelper.floor(Math.max(1.0F, this.entity.stepHeight));
        }

        int jumpHeight = this.dinosaur.getJumpHeight();
        if (!this.entity.isInWater() && !this.entity.isInLava() && this.entity.onGround && jumpHeight > 0 && jumpHeight > stepHeight) {
            stepHeight = jumpHeight + 1;
        }

        BlockPos ground = (new BlockPos(currentPoint.x, currentPoint.y, currentPoint.z)).down();
        double groundY = currentPoint.y - (1.0D - this.blockaccess.getBlockState(ground).getBoundingBox(this.blockaccess, ground).maxY);
        PathPoint south = this.getSafePoint(currentPoint.x, currentPoint.y, currentPoint.z + 1, stepHeight, groundY, EnumFacing.SOUTH);
        PathPoint west = this.getSafePoint(currentPoint.x - 1, currentPoint.y, currentPoint.z, stepHeight, groundY, EnumFacing.WEST);
        PathPoint east = this.getSafePoint(currentPoint.x + 1, currentPoint.y, currentPoint.z, stepHeight, groundY, EnumFacing.EAST);
        PathPoint north = this.getSafePoint(currentPoint.x, currentPoint.y, currentPoint.z - 1, stepHeight, groundY, EnumFacing.NORTH);

        if (south != null && !south.visited && south.distanceTo(targetPoint) < maxDistance) {
            pathOptions[optionIndex++] = south;
        }

        if (west != null && !west.visited && west.distanceTo(targetPoint) < maxDistance) {
            pathOptions[optionIndex++] = west;
        }

        if (east != null && !east.visited && east.distanceTo(targetPoint) < maxDistance) {
            pathOptions[optionIndex++] = east;
        }

        if (north != null && !north.visited && north.distanceTo(targetPoint) < maxDistance) {
            pathOptions[optionIndex++] = north;
        }

        boolean canMoveNorth = north == null || north.nodeType == PathNodeType.OPEN || north.costMalus != 0.0F;
        boolean canMoveSouth = south == null || south.nodeType == PathNodeType.OPEN || south.costMalus != 0.0F;
        boolean canMoveEast = east == null || east.nodeType == PathNodeType.OPEN || east.costMalus != 0.0F;
        boolean canMoveWest = west == null || west.nodeType == PathNodeType.OPEN || west.costMalus != 0.0F;

        if (canMoveNorth && canMoveWest) {
            PathPoint northWest = this.getSafePoint(currentPoint.x - 1, currentPoint.y, currentPoint.z - 1, stepHeight, groundY, EnumFacing.NORTH);

            if (northWest != null && !northWest.visited && northWest.distanceTo(targetPoint) < maxDistance) {
                pathOptions[optionIndex++] = northWest;
            }
        }

        if (canMoveNorth && canMoveEast) {
            PathPoint northEast = this.getSafePoint(currentPoint.x + 1, currentPoint.y, currentPoint.z - 1, stepHeight, groundY, EnumFacing.NORTH);

            if (northEast != null && !northEast.visited && northEast.distanceTo(targetPoint) < maxDistance) {
                pathOptions[optionIndex++] = northEast;
            }
        }

        if (canMoveSouth && canMoveWest) {
            PathPoint southWest = this.getSafePoint(currentPoint.x - 1, currentPoint.y, currentPoint.z + 1, stepHeight, groundY, EnumFacing.SOUTH);

            if (southWest != null && !southWest.visited && southWest.distanceTo(targetPoint) < maxDistance) {
                pathOptions[optionIndex++] = southWest;
            }
        }

        if (canMoveSouth && canMoveEast) {
            PathPoint southEast = this.getSafePoint(currentPoint.x + 1, currentPoint.y, currentPoint.z + 1, stepHeight, groundY, EnumFacing.SOUTH);

            if (southEast != null && !southEast.visited && southEast.distanceTo(targetPoint) < maxDistance) {
                pathOptions[optionIndex++] = southEast;
            }
        }

        return optionIndex;
    }

    @Nullable
    private PathPoint getSafePoint(int x, int y, int z, int stepHeight, double currentGroundY, EnumFacing facing) {
        PathPoint point = null;
        BlockPos pos = new BlockPos(x, y, z);
        BlockPos ground = pos.down();
        double groundY = y - (1.0 - this.blockaccess.getBlockState(ground).getBoundingBox(this.blockaccess, ground).maxY);

        if (groundY - currentGroundY > stepHeight + 0.125) {
            return null;
        } else {
            PathNodeType type = this.getPathNodeType(this.entity, x, y, z);
            float priority = this.entity.getPathPriority(type);
            double halfWidth = this.entity.width / 2.0;

            if (priority >= 0.0F) {
                point = this.openPoint(x, y, z);
                point.nodeType = type;
                point.costMalus = Math.max(point.costMalus, priority);
            }

            if (type == PathNodeType.WALKABLE) {
                return point;
            } else {
                if (point == null && stepHeight > 0 && type != PathNodeType.FENCE && type != PathNodeType.TRAPDOOR) {
                    point = this.getSafePoint(x, y + 1, z, stepHeight - 1, currentGroundY, facing);

                    if (point != null && (point.nodeType == PathNodeType.OPEN || point.nodeType == PathNodeType.WALKABLE) && this.entity.width < 1.0F) {
                        double pointX = (x - facing.getFrontOffsetX()) + 0.5;
                        double pointZ = (z - facing.getFrontOffsetZ()) + 0.5;
                        AxisAlignedBB boundsAtPoint = new AxisAlignedBB(x - halfWidth - 0.8, y + 0.001, z - halfWidth - 0.8, x + halfWidth + 0.8, (y + this.entity.height), z + halfWidth + 0.8);
                        AxisAlignedBB pointBlockBounds = this.blockaccess.getBlockState(pos).getBoundingBox(this.blockaccess, pos);
                        AxisAlignedBB boundsAtGroundPoint = boundsAtPoint.expand(1.8, pointBlockBounds.maxY - 0.002, 1.8);

                        if (this.entity.world.collidesWithAnyBlock(boundsAtGroundPoint)) {
                            point = null;
                        }
                    }
                }

                if (type == PathNodeType.OPEN) {
                    AxisAlignedBB boundsAtPoint = new AxisAlignedBB(x - halfWidth + 0.5, y + 0.001, z - halfWidth + 0.5, x + halfWidth + 0.5, (y + this.entity.height), z + halfWidth + 0.5);

                    if (this.entity.world.collidesWithAnyBlock(boundsAtPoint)) {
                        return null;
                    }

                    if (this.entity.width >= 1.0F) {
                        PathNodeType groundType = this.getPathNodeType(this.entity, x, y - 1, z);

                        if (groundType == PathNodeType.BLOCKED) {
                            point = this.openPoint(x, y, z);
                            point.nodeType = PathNodeType.WALKABLE;
                            point.costMalus = Math.max(point.costMalus, priority);
                            return point;
                        }
                    }

                    int i = 0;

                    while (y > 0 && type == PathNodeType.OPEN) {
                        --y;

                        if (i++ >= this.entity.getMaxFallHeight()) {
                            return null;
                        }

                        type = this.getPathNodeType(this.entity, x, y, z);
                        priority = this.entity.getPathPriority(type);

                        if (type != PathNodeType.OPEN && priority >= 0.0F) {
                            point = this.openPoint(x, y, z);
                            point.nodeType = type;
                            point.costMalus = Math.max(point.costMalus, priority);
                            break;
                        }

                        if (priority < 0.0F) {
                            return null;
                        }
                    }
                }

                return point;
            }
        }
    }

    @Override
    public PathNodeType getPathNodeType(IBlockAccess world, int x, int y, int z) {
        PathNodeType nodeType = this.getPathNodeTypeRaw(world, x, y, z);

        if (nodeType == PathNodeType.OPEN && y >= 1) {
            Block block = world.getBlockState(new BlockPos(x, y - 1, z)).getBlock();
            PathNodeType groundNodeType = this.getPathNodeTypeRaw(world, x, y - 1, z);
            nodeType = groundNodeType != PathNodeType.WALKABLE && groundNodeType != PathNodeType.OPEN && groundNodeType != PathNodeType.WATER && groundNodeType != PathNodeType.LAVA ? PathNodeType.WALKABLE : PathNodeType.OPEN;

            if (groundNodeType == PathNodeType.DAMAGE_FIRE || block == Blocks.MAGMA) {
                nodeType = PathNodeType.DAMAGE_FIRE;
            }

            if (groundNodeType == PathNodeType.DAMAGE_CACTUS) {
                nodeType = PathNodeType.DAMAGE_CACTUS;
            }
            if(groundNodeType == PathNodeType.WATER) {
                nodeType = PathNodeType.WATER;
            }
        }

        BlockPos.PooledMutableBlockPos pool = BlockPos.PooledMutableBlockPos.retain();

        if (nodeType == PathNodeType.WALKABLE) {
            for (int offsetX = -1; offsetX <= 1; offsetX++) {
                for (int offsetZ = -1; offsetZ <= 1; offsetZ++) {
                    if (offsetX != 0 || offsetZ != 0) {
                        if (!this.entity.world.isBlockLoaded(pool.setPos(x + offsetX, y, z + offsetZ))) {
                            nodeType = PathNodeType.BLOCKED;
                        }
                        else {
                            IBlockState state = world.getBlockState(pool.setPos(x + offsetX, y, z + offsetZ));
                            Block block = state.getBlock();
                            if (block == Blocks.CACTUS || block instanceof ElectricFenceBaseBlock || block instanceof ElectricFencePoleBlock) {
                                nodeType = PathNodeType.DANGER_CACTUS;
                            } else if (block == Blocks.FIRE) {
                                nodeType = PathNodeType.DANGER_FIRE;
                            } else if (block instanceof ElectricFenceWireBlock) {
                                TileEntity entity = world.getTileEntity(pool);
                                if (entity instanceof ElectricFenceWireBlockEntity && ((ElectricFenceWireBlockEntity) entity).isPowered()) {
                                    nodeType = PathNodeType.DAMAGE_CACTUS;
                                }
                            }
                        }
                    }
                }
            }
        }

        pool.release();
        return nodeType;
    }


    // TODO: getCanBreakDoors gone???
    private PathNodeType getPathNodeType(EntityLiving entity, int x, int y, int z) {
        return this.getPathNodeType(this.blockaccess, x, y, z, entity, this.entitySizeX, this.entitySizeY, this.entitySizeZ, this.canEnterDoors, this.getCanEnterDoors());
    }

    public PathNodeType getPathNodeTypeRaw(IBlockAccess access, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        IBlockState state = access.getBlockState(pos);
        Block block = state.getBlock();
        Material material = state.getMaterial();

        if (material == Material.AIR) {
            return PathNodeType.OPEN;
        }

        if (block == Blocks.TRAPDOOR || block == Blocks.IRON_TRAPDOOR || block == Blocks.WATERLILY) {
            return PathNodeType.TRAPDOOR;
        }
        if (block == Blocks.FIRE) {
            return PathNodeType.DAMAGE_FIRE;
        }

        if (block == Blocks.CACTUS || block instanceof ElectricFenceBaseBlock || block instanceof ElectricFencePoleBlock) {
            return PathNodeType.DAMAGE_CACTUS;
        } else if (block instanceof ElectricFenceWireBlock) {
            TileEntity entity = access.getTileEntity(pos);
            if (entity instanceof ElectricFenceWireBlockEntity && ((ElectricFenceWireBlockEntity) entity).isPowered()) {
                return PathNodeType.DAMAGE_CACTUS;
            }
        }

        if (block instanceof BlockDoor) {
            if (!state.getValue(BlockDoor.OPEN)) {
                return material == Material.WOOD ? PathNodeType.DOOR_WOOD_CLOSED : PathNodeType.DOOR_IRON_CLOSED;
            }
            return PathNodeType.DOOR_OPEN;
        }
        if (block instanceof BlockRailBase) {
            return PathNodeType.RAIL;
        }
        if (block instanceof BlockFence || block instanceof BlockWall || (block instanceof BlockFenceGate && !state.getValue(BlockFenceGate.OPEN))) {
            return PathNodeType.FENCE;
        }

        if (material.isLiquid()) {
            return material == Material.LAVA ? PathNodeType.LAVA : PathNodeType.WATER;
        }

        return block.isPassable(access, pos) ? PathNodeType.OPEN : PathNodeType.BLOCKED;
    }
}