package mod.reborn.server.block;

import mod.reborn.server.api.IncubatorEnvironmentItem;
import mod.reborn.server.tab.TabHandler;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class PeatMossBlock extends Block implements IncubatorEnvironmentItem {
    public PeatMossBlock() {
        super(Material.GROUND);
        this.setSoundType(SoundType.GROUND);
        this.setHardness(0.5F);
        this.setCreativeTab(TabHandler.PLANTS);
    }
}
