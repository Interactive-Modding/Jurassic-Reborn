package net.vit.jurassicreborn.common.entities.ai.util;

import net.minecraft.core.BlockPos;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Iterates over the X-Z square ring at Chebyshev distance {@code radius} from {@code center}.
 * Order: EAST edge → SOUTH → WEST → NORTH (clockwise), starting at the NW corner.
 * Emits exactly {@code (radius == 0 ? 1 : 8 * radius)} positions.
 */
public class RingXZTraverser implements Iterable<BlockPos>, Iterator<BlockPos> {

    private final BlockPos center;
    private final int r;

    // walking state
    private int x, z, y;
    private int side = 0;            // 0:EAST, 1:SOUTH, 2:WEST, 3:NORTH
    private int stepsOnSide = 0;     // 0..(2r - 1)
    private int stepsRemaining;      // (r==0 ? 1 : 8r)
    private boolean started = false;

    public RingXZTraverser(BlockPos center, int radius) {
        this.center = center;
        this.r      = Math.max(0, radius);

        this.y = center.getY();

        if (r == 0) {
            // single position: the center itself
            this.x = center.getX();
            this.z = center.getZ();
            this.stepsRemaining = 1;
        } else {
            // start at NW corner (minX, minZ)
            this.x = center.getX() - r;
            this.z = center.getZ() - r;
            this.stepsRemaining = 8 * r;
        }
    }

    @Override
    public Iterator<BlockPos> iterator() { return this; }

    @Override
    public boolean hasNext() { return stepsRemaining > 0; }

    @Override
    public BlockPos next() {
        if (!hasNext()) throw new NoSuchElementException();

        if (started) advance();
        started = true;

        stepsRemaining--;
        return new BlockPos(x, y, z);
    }

    private void advance() {
        if (r == 0) return; // nothing to advance; single element already handled

        // edge lengths: 2r each
        final int edgeLen = 2 * r;

        switch (side) {
            case 0: // EAST edge, move +X
                x++;
                break;
            case 1: // SOUTH edge, move +Z
                z++;
                break;
            case 2: // WEST edge, move -X
                x--;
                break;
            case 3: // NORTH edge, move -Z
                z--;
                break;
        }

        stepsOnSide++;
        if (stepsOnSide >= edgeLen) {
            stepsOnSide = 0;
            side = (side + 1) & 3; // next side (wrap 0..3)
        }
    }
}
