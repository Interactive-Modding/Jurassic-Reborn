package mod.reborn.server.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemFood;

public class BasicFoodItem extends ItemFood {
    public BasicFoodItem(int amount, float saturation, boolean isWolfFood, CreativeTabs tab) {
        super(amount, saturation, isWolfFood);
        this.setCreativeTab(tab);
    }
}
