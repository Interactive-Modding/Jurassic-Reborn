package mod.reborn.server.entity.ai.util;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Copyright 2016 Andrew O. Mellinger
 */
public class AIUtils {
    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * Finds the point on the radius edge where we'll intersect if we travel in a
     * straight line.
     *
     * @param center Center of target area.
     * @param radius Radius of target area.
     * @param start Our starting (or current) location.
     * @return BlockPos on intersect or null if already within center
     */
    public static BlockPos findIntersect(BlockPos center, int radius, BlockPos start) {
        int deltaX = start.getX() - center.getX();
        int deltaZ = start.getZ() - center.getZ();
        double distance = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);

        if (distance < radius) {
            return start;
        }

        double scale = radius / distance;
        return new BlockPos(center.getX() + Math.round(deltaX * scale),
                center.getY(),
                center.getZ() + Math.round(deltaZ * scale));
    }

    //=============================================================================================

    public static BlockPos computePosToward(BlockPos current, BlockPos target, int move) {
        int deltaX = target.getX() - current.getX();
        int deltaZ = target.getZ() - current.getZ();

        double distance = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);

        if (distance < move) {
            return target;
        }

        double scale = move / distance;
        return new BlockPos(current.getX() + Math.round(deltaX * scale),
                current.getY(),
                current.getZ() + Math.round(deltaZ * scale));
    }

    public static BlockPos findSurface(EntityLiving entity) {
        if (!entity.isInWater()) {
            return null;
        }
        World world = entity.getEntityWorld();
        BlockPos pos = entity.getPosition();
        return findSurface(world, pos.down(MathHelper.floor(entity.height / 2.0F)));
    }

    public static BlockPos findSurface(World world, BlockPos pos) {
        while (!world.isAirBlock(pos)) {
            pos = pos.up();
        }
        return pos;
    }

    //=============================================================================================

    public static int getWaterDepth(EntityLiving entity) {
        if (!entity.isInWater()) {
            return 0;
        }

        World world = entity.getEntityWorld();
        BlockPos pos = entity.getPosition();

        // Move up
        int depth = 0;
        while (world.getBlockState(pos).getBlock() instanceof BlockLiquid) {
            pos = pos.up();
            ++depth;
        }

        // We are now above the water.  Start at below water level.
        pos = entity.getPosition().down();

        // Move down
        while (world.getBlockState(pos).getBlock() instanceof BlockLiquid) {
            pos = pos.down();
            ++depth;
        }

        return depth;
    }
    
    public static int getWaterDepth(EntityLiving entity, boolean fromEntity) {
        if (!entity.isInWater()) {
            return 0;
        }

        World world = entity.getEntityWorld();
        BlockPos pos = entity.getPosition();

        // Move up
        int depth = 0;
        while (world.getBlockState(pos).getBlock() instanceof BlockLiquid) {
            pos = pos.up();
            ++depth;
        }

        // We are now above the water.  Start at below water level.
        pos = entity.getPosition().down();
        if(fromEntity)
            depth = 0;
        // Move down
        while (world.getBlockState(pos).getBlock() instanceof BlockLiquid) {
            pos = pos.down();
            ++depth;
        }

        return depth + 1;
    }
    
    public static BlockPos getBottom(EntityLiving entity) {
        return new BlockPos(entity.posX, getWaterDepth(entity, true), entity.posZ);
    }


    //=============================================================================================

    public static BlockPos findShore(World world, BlockPos center) {
        int radius = 1;
        // MAX RADIUS
        while (radius < 32) {
            RingXZTraverser traverser = new RingXZTraverser(center, radius);

            for (BlockPos pos : traverser) {
                IBlockState state = world.getBlockState(pos);
                Block block = state.getBlock();
                if (!(block instanceof BlockLiquid)) {
                    return pos;
                }
            }
            ++radius;
        }

        return null;
    }

    static void plotCircle(int x0, int y0, int radius) {
        // Bresenham circle from: https://en.wikipedia.org/wiki/Midpoint_circle_algorithm
        // This is fast.
        int x = radius;
        int y = 0;
        int decisionOver2 = 1 - x;   // Decision criterion divided by 2 evaluated at x=r, y=0

        while (y <= x) {
            drawPixel(x + x0, y + y0); // Octant 1
            drawPixel(y + x0, x + y0); // Octant 2
            drawPixel(-x + x0, y + y0); // Octant 4
            drawPixel(-y + x0, x + y0); // Octant 3
            drawPixel(-x + x0, -y + y0); // Octant 5
            drawPixel(-y + x0, -x + y0); // Octant 6
            drawPixel(x + x0, -y + y0); // Octant 8
            drawPixel(y + x0, -x + y0); // Octant 7
            y++;
            if (decisionOver2 <= 0) {
                decisionOver2 += 2 * y + 1;   // Change in decision criterion for y -> y+1
            } else {
                x--;
                decisionOver2 += 2 * (y - x) + 1;   // Change for y -> y+1, x -> x-1
            }
        }
    }

    private static void drawPixel(int x, int y) {
        LOGGER.info("x=" + x + ", y=" + y);
    }
}

