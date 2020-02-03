package mod.reborn.server.block;

import mod.reborn.server.tab.TabHandler;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BasicBlock extends Block {
    public BasicBlock(Material material) {
        super(material);
        this.setCreativeTab(TabHandler.BLOCKS);
    }

    public BasicBlock(Material material, SoundType soundType) {
        super(material);
        this.setSoundType(soundType);
        this.setCreativeTab(TabHandler.BLOCKS);
    }
}
