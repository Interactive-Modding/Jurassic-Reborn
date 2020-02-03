package mod.reborn.server.block.tree;

import mod.reborn.server.tab.TabHandler;
import net.minecraft.block.BlockFence;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

import java.util.Locale;

public class AncientFenceBlock extends BlockFence {
    public AncientFenceBlock(TreeType treeType) {
        super(Material.WOOD, MapColor.BROWN);
        this.setHardness(2.0F);
        this.setResistance(5.0F);
        this.setSoundType(SoundType.WOOD);
        this.setUnlocalizedName(treeType.name().toLowerCase(Locale.ENGLISH) + "_fence");
        this.setCreativeTab(TabHandler.PLANTS);
    }
}
