package mod.reborn.server.item;

import mod.reborn.server.api.BreedableBug;
import mod.reborn.server.tab.TabHandler;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.function.Function;

public class BugItem extends Item implements BreedableBug {
    private Function<ItemStack, Integer> breedings;

    public BugItem(Function<ItemStack, Integer> breedings) {
        super();
        this.setCreativeTab(TabHandler.ITEMS);
        this.breedings = breedings;
    }

    @Override
    public int getBreedings(ItemStack stack) {
        return this.breedings.apply(stack);
    }
}
