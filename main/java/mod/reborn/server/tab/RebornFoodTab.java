package mod.reborn.server.tab;

import java.util.List;

import mod.reborn.server.dinosaur.Dinosaur;
import mod.reborn.server.entity.EntityHandler;
import mod.reborn.server.item.ItemHandler;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class RebornFoodTab extends CreativeTabs {
    private int[] metas;

    public RebornFoodTab(String label) {
        super(label);

        List<Dinosaur> registeredDinosaurs = EntityHandler.getRegisteredDinosaurs();
        this.metas = new int[registeredDinosaurs.size()];

        int i = 0;

        for (Dinosaur dino : registeredDinosaurs) {
            if (dino.shouldRegister()) {
                this.metas[i] = EntityHandler.getDinosaurId(dino);

                i++;
            }
        }
    }


    @Override
    public ItemStack getTabIconItem() {
        return new ItemStack(ItemHandler.DINOSAUR_MEAT);
    }
}
