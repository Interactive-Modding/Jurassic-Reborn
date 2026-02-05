package net.vit.jurassicreborn.common.worldgen.structure;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.resources.ResourceLocation;

import java.util.Random;
import java.lang.reflect.Method;
import javax.annotation.Nullable;

/**
 * Basic structure generator ported for 1.19.2.
 */
public abstract class StructureGenerator {
    protected Rotation rotation;
    protected Mirror mirror;
    protected int horizontalPos;
    protected final int sizeX;
    protected final int sizeY;
    protected final int sizeZ;

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

    protected BlockPos placeOnGround(ServerLevel level, BlockPos pos, int yOffset) {
        if (this.horizontalPos >= 0) {
            return new BlockPos(pos.getX(), this.horizontalPos, pos.getZ());
        } else {
            int minHeight = Integer.MAX_VALUE;
            int maxHeight = Integer.MIN_VALUE;
            BlockPos min = this.transformPos(new BlockPos(0, 0, 0), this.mirror, this.rotation).offset(pos);
            BlockPos max = this.transformPos(new BlockPos(this.sizeX - 1, 0, this.sizeZ - 1), this.mirror, this.rotation).offset(pos);
            int minX = Math.min(min.getX(), max.getX());
            int minZ = Math.min(min.getZ(), max.getZ());
            int maxX = Math.max(min.getX(), max.getX());
            int maxZ = Math.max(min.getZ(), max.getZ());
            for (int z = minZ; z <= maxZ; ++z) {
                for (int x = minX; x <= maxX; ++x) {
                    if (x == minX || x == maxX || z == minZ || z == maxZ) {
                        BlockPos ground = this.getGround(level, new BlockPos(x, 64, z));
                        int levelY = ground.getY();
                        if (levelY < minHeight) {
                            minHeight = levelY;
                        }
                        if (levelY > maxHeight) {
                            maxHeight = levelY;
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

    protected BlockPos getGround(ServerLevel level, BlockPos pos) {
        int y = level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, pos.getX(), pos.getZ());
        BlockPos current = new BlockPos(pos.getX(), y, pos.getZ());
        while (current.getY() > level.getMinBuildHeight()) {
            BlockPos below = current.below();
            BlockState state = level.getBlockState(below);
            Material material = state.getMaterial();
            if (material == Material.DIRT || material == Material.SAND || material == Material.GRASS || material == Material.STONE || material.isLiquid()) {
                break;
            }
            current = below;
        }
        return current;
    }

    public boolean generate(ServerLevel level, Random random, BlockPos position) {
        BlockPos levelPos = getLevelPosition();
        BlockPos placePos = levelPos == null ? this.placeOnGround(level, position, this.getOffsetY()) : this.getGround(level, position).subtract(this.transformPos(levelPos, this.mirror, this.rotation));
        if (placePos != null) {
            this.loadChunks(level, placePos);
            this.generateStructure(level, random, placePos);
            this.generateFiller(level, placePos);
            return true;
        }
        return false;
    }
    private void loadChunks(ServerLevel level, BlockPos pos) {
        BlockPos min = this.transformPos(new BlockPos(0, 0, 0), this.mirror, this.rotation).offset(pos);
        BlockPos max = this.transformPos(new BlockPos(this.sizeX - 1, 0, this.sizeZ - 1), this.mirror, this.rotation).offset(pos);
        int minChunkX = Math.min(min.getX(), max.getX()) >> 4;
        int maxChunkX = Math.max(min.getX(), max.getX()) >> 4;
        int minChunkZ = Math.min(min.getZ(), max.getZ()) >> 4;
        int maxChunkZ = Math.max(min.getZ(), max.getZ()) >> 4;
        for (int cx = minChunkX; cx <= maxChunkX; cx++) {
            for (int cz = minChunkZ; cz <= maxChunkZ; cz++) {
                level.getChunk(cx, cz);
            }
        }
    }
    protected void generateFiller(Level level, BlockPos pos) {
        BlockPos min = this.transformPos(new BlockPos(0, 0, 0), this.mirror, this.rotation).offset(pos);
        BlockPos max = this.transformPos(new BlockPos(this.sizeX - 1, 0, this.sizeZ - 1), this.mirror, this.rotation).offset(pos);
        for (int x = Math.min(min.getX(), max.getX()); x <= Math.max(min.getX(), max.getX()); x++) {
            for (int z = Math.min(min.getZ(), max.getZ()); z <= Math.max(min.getZ(), max.getZ()); z++) {
                BlockPos blockPos = new BlockPos(x, pos.getY(), z);
                if (!level.getBlockState(blockPos).isAir()) {
                    BlockPos setPos = blockPos.below();
                    do {
                        level.setBlock(setPos, this.getFillerState(), 2);
                        setPos = setPos.below();
                    } while (level.getBlockState(setPos).getMaterial().isReplaceable());
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
            case FRONT_BACK:
                x = -x;
                break;
            case LEFT_RIGHT:
                z = -z;
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

    protected abstract void generateStructure(ServerLevel level, Random random, BlockPos position);

    public int getOffsetY() {
        return -1;
    }

    public BlockPos getLevelPosition() {
        return null;
    }

    public BlockState getFillerState() {
        return Blocks.DIRT.defaultBlockState();
    }

    @Nullable
    protected StructureTemplate loadTemplate(ServerLevel level, ResourceLocation id) {
        try {
            Object manager = level.getStructureManager();
            Method method = manager.getClass().getMethod("getOrCreate", ResourceLocation.class);
            return (StructureTemplate) method.invoke(manager, id);
        } catch (ReflectiveOperationException e) {
            return null;
        }
    }
}
