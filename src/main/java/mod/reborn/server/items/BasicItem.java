package mod.reborn.server.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class BasicItem extends Item {
    public BasicItem(ItemGroup tab) {
        super(new Properties().group(tab));
    }
}
