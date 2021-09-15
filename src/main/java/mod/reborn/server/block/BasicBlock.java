package mod.reborn.server.block;

import mod.reborn.server.tab.TabHandler;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;

public class BasicBlock extends Block {
    public BasicBlock(Properties properties) {
        super(properties);
    }

    public BasicBlock(Properties properties, SoundType soundType) {
        super(properties.sound(soundType));
    }

    public static class BlockItem extends net.minecraft.item.BlockItem {

        public BlockItem(Block blockIn) {
            super(blockIn, new Properties().group(TabHandler.BLOCKS));
        }
    }
}
