package mod.reborn.server.entity.ai.util;

import net.minecraft.util.math.BlockPos;

import java.util.Iterator;

/**
 * Copyright 2016 Andrew O. Mellinger
 */
public class FastCircleTraverser implements Iterable<BlockPos> {
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

    }

    @Override
    public Iterator<BlockPos> iterator() {
        return null;
    }
}
