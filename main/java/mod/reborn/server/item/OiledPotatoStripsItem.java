package mod.reborn.server.item;

import mod.reborn.server.tab.TabHandler;

import net.minecraft.item.ItemFood;

public class OiledPotatoStripsItem extends ItemFood {
	public OiledPotatoStripsItem() {
	 super(0, false);
     this.setCreativeTab(TabHandler.FOODS);
	}
}
