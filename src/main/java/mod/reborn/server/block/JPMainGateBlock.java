package mod.reborn.server.block;

import mod.reborn.server.tab.TabHandler;

import net.minecraft.block.BlockDoor;
import net.minecraft.block.material.Material;

public class JPMainGateBlock extends BlockDoor {
    public JPMainGateBlock() {
        super(Material.ROCK);
        this.setCreativeTab(TabHandler.BLOCKS);
        this.setHardness(1.5F);
        this.setResistance(1.5F);
    }
}
