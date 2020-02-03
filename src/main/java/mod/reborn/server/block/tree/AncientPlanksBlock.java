package mod.reborn.server.block.tree;

import mod.reborn.server.tab.TabHandler;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

import java.util.Locale;

public class AncientPlanksBlock extends Block {
    public AncientPlanksBlock(TreeType treeType) {
        super(Material.WOOD);
        this.setHardness(2.0F);
        this.setResistance(5.0F);
        this.setSoundType(SoundType.WOOD);
        this.setUnlocalizedName(treeType.name().toLowerCase(Locale.ENGLISH) + "_planks");
        this.setCreativeTab(TabHandler.PLANTS);
    }
}
