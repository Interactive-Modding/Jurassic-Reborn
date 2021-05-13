package mod.reborn.server.tab;

import mod.reborn.server.items.ItemHandler;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class RebornDNATab extends ItemGroup {
    private ItemStack[] stacks = null;

    public RebornDNATab(String label) {
        super(label);
    }

    @Override
    public ItemStack createIcon() {
        return new ItemStack(ItemHandler.DNA);
    }
}
