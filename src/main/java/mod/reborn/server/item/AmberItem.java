package mod.reborn.server.item;

import mod.reborn.server.tab.TabHandler;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import mod.reborn.server.util.LangUtils;

public class AmberItem extends Item {
    public AmberItem() {
        super();
        this.setCreativeTab(TabHandler.ITEMS);
        this.setHasSubtypes(true);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return LangUtils.translate(this.getUnlocalizedName() + ".name").replace("{stored}", LangUtils.translate("amber." + (stack.getItemDamage() == 0 ? "mosquito" : "aphid") + ".name"));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems) {
        if(this.isInCreativeTab(tab))
        {
            subItems.add(new ItemStack(this, 1, 0));
            subItems.add(new ItemStack(this, 1, 1));
        }
    }
}
