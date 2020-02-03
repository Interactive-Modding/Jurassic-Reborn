package mod.reborn.server.tab;

import java.util.List;

import mod.reborn.server.dinosaur.Dinosaur;
import mod.reborn.server.entity.EntityHandler;
import mod.reborn.server.item.DisplayBlockItem;
import mod.reborn.server.item.ItemHandler;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class RebornDecorationsTab extends CreativeTabs {
    private int[] metas;

    public RebornDecorationsTab(String label) {
        super(label);

        List<Dinosaur> registeredDinosaurs = EntityHandler.getRegisteredDinosaurs();
        this.metas = new int[registeredDinosaurs.size()];

        for (int i = 0; i < registeredDinosaurs.size(); i++) {
            this.metas[i] = DisplayBlockItem.getMetadata(EntityHandler.getDinosaurId(registeredDinosaurs.get(i)), 0, false);
        }
    }

    @Override
    public ItemStack getTabIconItem() {
        return new ItemStack(ItemHandler.DISPLAY_BLOCK);
    }
}
