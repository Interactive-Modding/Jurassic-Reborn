package mod.reborn.server.block.tree;

import mod.reborn.server.tab.TabHandler;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.SoundType;

import java.util.Locale;

public class AncientFenceGateBlock extends BlockFenceGate {
    public AncientFenceGateBlock(TreeType treeType) {
        super(BlockPlanks.EnumType.OAK);
        this.setHardness(2.0F);
        this.setResistance(0.5F);
        this.setSoundType(SoundType.WOOD);
        this.setUnlocalizedName(treeType.name().toLowerCase(Locale.ENGLISH) + "_fence_gate");
        this.setCreativeTab(TabHandler.PLANTS);
    }
}
