package net.vit.jurassicreborn.common.entities.ai.util;

import net.minecraft.core.BlockPos;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Iterates outward from {@code center} in 3D "shells" (Chebyshev rings).
 * Order: r=0 (center), then all positions with max(|dx|,|dy|,|dz|)==1,
 * then 2, ... up to {@code maxRadius}.
 */
public class OnionTraverser implements Iterable<BlockPos> {

    private final BlockPos center;
    private final int maxRadius;

    public OnionTraverser(BlockPos center, int maxRadius) {
        this.center = center;
        this.maxRadius = Math.max(0, maxRadius);
    }

    @Override
    public Iterator<BlockPos> iterator() {
        return new ShellIterator();
    }

    private final class ShellIterator implements Iterator<BlockPos> {
        private boolean centerEmitted = false;

        // shell state
        private boolean shellStarted = false; // becomes true after center
        private int r = 0;                    // current radius
        private int dx = 0, dy = 0, dz = 0;   // loop cursors

        private BlockPos next;                // next cached value

        @Override
        public boolean hasNext() {
            if (next != null) return true;
            next = computeNext();
            return next != null;
        }

        @Override
        public BlockPos next() {
            if (!hasNext()) throw new NoSuchElementException();
            BlockPos out = next;
            next = null;
            return out;
        }

        private BlockPos computeNext() {
            // r = 0 (center) once
            if (!centerEmitted) {
                centerEmitted = true;
                return center;
            }

            // initialize first shell
            if (!shellStarted) {
                shellStarted = true;
                r = 1;
                dx = dy = dz = -1;
            }

            while (r <= maxRadius) {
                while (dy <= r) {
                    while (dx <= r) {
                        while (dz <= r) {
                            // emit only the outer shell: max(|dx|,|dy|,|dz|) == r
                            if (maxAbs(dx, dy, dz) == r) {
                                BlockPos out = center.offset(dx, dy, dz);
                                dz++;
                                return out;
                            }
                            dz++;
                        }
                        dz = -r;
                        dx++;
                    }
                    dx = -r;
                    dy++;
                }
                // next radius
                r++;
                if (r > maxRadius) break;
                dx = dy = dz = -r;
            }
            return null;
        }

        private int maxAbs(int a, int b, int c) {
            a = Math.abs(a); b = Math.abs(b); c = Math.abs(c);
            return Math.max(a, Math.max(b, c));
        }
    }
}
