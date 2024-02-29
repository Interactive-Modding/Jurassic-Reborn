package mod.reborn.server.item;

import mod.reborn.server.tab.TabHandler;
import mod.reborn.server.util.LangUtils;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class FrozenLeechItem extends Item {
    public FrozenLeechItem() {
        super();
        this.setCreativeTab(TabHandler.ITEMS);
        this.setHasSubtypes(true);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return LangUtils.translate(this.getUnlocalizedName() + ".name").replace("{stored}", LangUtils.translate("frozen." + "leech" + ".name"));
    }
}
