package mod.reborn.server.items;

import mod.reborn.server.tab.TabHandler;
import mod.reborn.server.util.LangUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class AmberItem extends Item {
    public AmberItem() {
        super(new Properties().group(TabHandler.ITEMS));
    }

    @Override
    public ITextComponent getDisplayName(ItemStack stack) {
        return new StringTextComponent(LangUtils.translate(this.getName() + ".name").replace("{stored}", LangUtils.translate("amber." + (stack.getDamage() == 0 ? "mosquito" : "aphid") + ".name")));
    }
}
