package mod.reborn.server.item.block;

import mod.reborn.server.tab.TabHandler;
import net.minecraft.block.Block;
import net.minecraft.item.ItemDoor;

public class AncientDoorItem extends ItemDoor {
    public AncientDoorItem(Block block) {
        super(block);
        this.setCreativeTab(TabHandler.PLANTS);
    }
}
