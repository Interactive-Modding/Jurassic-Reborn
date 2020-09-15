package mod.reborn.server.entity.ai;

import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;
import java.util.AbstractCollection;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class BlockRingBuffer extends AbstractCollection<BlockPos> {

    protected final BlockPos[] entry;

    protected int start = 0;

    protected int end = 0;

    protected boolean full = false;

    protected final int size;

    public BlockRingBuffer(final int size) {
        this.size = size;
        entry = new BlockPos[size];
    }

    @Override
    public int size() {
        int size = 0;

        if (end < start) {
            size = size - start + end;
        } else if (end == start) {
            size = full ? size : 0;
        } else {
            size = end - start;
        }

        return size;
    }

    @Override
    public boolean add(@Nonnull BlockPos element) {

        if (size() == size) {
            remove();
        }

        entry[end++] = element;

        if (end >= size) {
            end = 0;
        }

        if (end == start) {
            full = true;
        }

        return true;
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    public BlockPos remove() {
        if (isEmpty())
            throw new NoSuchElementException("Empty buffer");


        BlockPos element = entry[start];
        if (null != element) {
            entry[start++] = null;

            if (start >= size) {
                start = 0;
            }
            full = false;
        }
        return element;
    }

    private int increase(int index) {
        index++;
        if (index >= size)
            index = 0;

        return index;
    }

    @Override
    public Iterator<BlockPos> iterator() {
        return new Iterator<BlockPos>() {

            private int index = start;
            private int preIndex = -1;
            private boolean isFull = full;

            public boolean hasNext() {
                return isFull || index != end;
            }

            public BlockPos next() {
                if (!hasNext()) {
                    throw new NoSuchElementException("Wrongly indexed");
                }
                isFull = false;
                preIndex = index;
                index = increase(index);
                return entry[preIndex];
            }

        };
    }

}
