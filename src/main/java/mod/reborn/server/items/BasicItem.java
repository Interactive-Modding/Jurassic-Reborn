package mod.reborn.server.items;

import mod.reborn.server.tab.TabHandler;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class BasicItem extends Item {

    protected BasicItem() {
        super(new Properties().group(TabHandler.ITEMS));
    }

    public BasicItem(ItemGroup tab) {
        super(new Properties().group(tab));
    }
}
