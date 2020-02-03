package mod.reborn.server.world.structure;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.feature.WorldGenerator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

public abstract class StructureGenerator extends WorldGenerator {
    protected Rotation rotation;
    protected Mirror mirror;
    protected int horizontalPos;
    protected int sizeX;
    protected int sizeY;
    protected int sizeZ;
    private int offsetY;

    protected StructureGenerator(Random rand, int sizeX, int sizeY, int sizeZ) {
        this.horizontalPos = -1;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.sizeZ = sizeZ;
        Rotation[] rotations = Rotation.values();
        this.rotation = rotations[rand.nextInt(rotations.length)];
        Mirror[] mirrors = Mirror.values();
        this.mirror = mirrors[rand.nextInt(mirrors.length)];
    }

    public void setRotation(Rotation rotation) {
        this.rotation = rotation;
    }

    public void setMirror(Mirror mirror) {
        this.mirror = mirror;
    }

    protected BlockPos placeOnGround(World world, BlockPos pos, int yOffset) {
        if (this.horizontalPos >= 0) {
            return new BlockPos(pos.getX(), this.horizontalPos, pos.getZ());
        } else {
            int minHeight = Integer.MAX_VALUE;
            int maxHeight = Integer.MIN_VALUE;
            BlockPos.MutableBlockPos currentPos = new BlockPos.MutableBlockPos();
            BlockPos min = this.transformPos(new BlockPos(0, 0, 0), this.mirror, this.rotation).add(pos);
            BlockPos max = this.transformPos(new BlockPos(this.sizeX - 1, 0, this.sizeZ - 1), this.mirror, this.rotation).add(pos);
            int minX = min.getX();
            int minZ = min.getZ();
            int maxX = max.getX();
            int maxZ = max.getZ();
            if (maxZ < minZ) {
                int oldMax = maxZ;
                maxZ = minZ;
                minZ = oldMax;
            }
            if (maxX < minX) {
                int oldMax = maxX;
                maxX = minX;
                minX = oldMax;
            }
            for (int z = minZ; z <= maxZ; ++z) {
                for (int x = minX; x <= maxX; ++x) {
                    if (x == minX || x == maxX || z == minZ || z == maxZ) {
                        currentPos.setPos(x, 64, z);
                        BlockPos ground = this.getGround(world, currentPos);
                        int level = ground.getY();
                        if (level < minHeight) {
                            minHeight = level;
                        }
                        if (level > maxHeight) {
                            maxHeight = level;
                        }
                    }
                }
            }
            int average = (maxHeight + minHeight) / 2;
            if (average - minHeight > 8 && !this.canSpawnOnHills()) {
                return null;
            }
            this.horizontalPos = minHeight + yOffset;
            return new BlockPos(pos.getX(), this.horizontalPos, pos.getZ());
        }
    }

    protected BlockPos getGround(World world, BlockPos pos) {
        Chunk chunk = world.getChunkFromBlockCoords(pos);
        BlockPos currentPos;
        BlockPos ground;
        for (currentPos = new BlockPos(pos.getX(), chunk.getHeight(pos), pos.getZ()); currentPos.getY() >= world.provider.getAverageGroundLevel() - 16; currentPos = ground) {
            ground = currentPos.down();
            IBlockState state = chunk.getBlockState(ground);
            Material material = state.getMaterial();
            if (material == Material.GROUND || material == Material.SAND || material == Material.GRASS || material == Material.ROCK|| material.isLiquid()) {
                break;
            }
        }
        return currentPos;
    }

    @Override
    public boolean generate(World world, Random random, BlockPos position) {
        BlockPos levelPos = getLevelPosition();
        position = levelPos == null ? this.placeOnGround(world, position, this.getOffsetY()) : this.getGround(world, position).subtract(this.transformPos(levelPos, this.mirror, this.rotation));
        if (position != null) {
            this.generateStructure(world, random, position);
            this.generateFiller(world, position);
            return true;
        }
        return false;
    }

    protected void generateFiller(World world, BlockPos pos) {
        BlockPos min = this.transformPos(new BlockPos(0, 0, 0), this.mirror, this.rotation).add(pos);
        BlockPos max = this.transformPos(new BlockPos(this.sizeX - 1, 0, this.sizeZ - 1), this.mirror, this.rotation).add(pos);

        for(int x = Math.min(min.getX(), max.getX()); x <= Math.max(min.getX(), max.getX()); x ++) {
            for(int z = Math.min(min.getZ(), max.getZ()); z <= Math.max(min.getZ(), max.getZ()); z ++) {
                BlockPos blockPos = new BlockPos(x, pos.getY(), z);
                if(world.getBlockState(blockPos).getMaterial() != Material.AIR) {
                    BlockPos setpos = blockPos.down();
                    do {
                        world.setBlockState(setpos, this.getFillerState());
                        setpos = setpos.down();
                    } while (world.getBlockState(setpos).getBlock().isReplaceable(world, setpos));
                }
            }
        }
    }

    protected boolean canSpawnOnHills() {
        return false;
    }

    protected BlockPos transformPos(BlockPos pos, Mirror mirror, Rotation rotation) {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        boolean mirrored = true;
        switch (mirror) {
            case LEFT_RIGHT:
                z = -z;
                break;
            case FRONT_BACK:
                x = -x;
                break;
            default:
                mirrored = false;
        }
        switch (rotation) {
            case COUNTERCLOCKWISE_90:
                return new BlockPos(z, y, -x);
            case CLOCKWISE_90:
                return new BlockPos(-z, y, x);
            case CLOCKWISE_180:
                return new BlockPos(-x, y, -z);
            default:
                return mirrored ? new BlockPos(x, y, z) : pos;
        }
    }

    protected abstract void generateStructure(World world, Random random, BlockPos position);

    public int getOffsetY() {
        return -1;
    }

    @Nullable
    public BlockPos getLevelPosition() {
        return null;
    }

    @Nonnull
    public IBlockState getFillerState() {
        return Blocks.DIRT.getDefaultState();
    }
}
