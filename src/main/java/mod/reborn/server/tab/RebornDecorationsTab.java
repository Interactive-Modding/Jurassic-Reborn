package mod.reborn.server.tab;

import mod.reborn.server.dinosaur.Dinosaur;
import mod.reborn.server.entity.EntityHandler;
import mod.reborn.server.item.DisplayBlockItem;
import mod.reborn.server.items.ItemHandler;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

import java.util.List;

public class RebornDecorationsTab extends ItemGroup {
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
    public ItemStack createIcon() {
        return new ItemStack(ItemHandler.DISPLAY_BLOCK);
    }
}
