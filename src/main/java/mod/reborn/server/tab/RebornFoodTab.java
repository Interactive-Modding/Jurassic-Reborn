package mod.reborn.server.tab;

import mod.reborn.server.dinosaur.Dinosaur;
import mod.reborn.server.entity.EntityHandler;
import mod.reborn.server.items.ItemHandler;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

import java.util.List;

public class RebornFoodTab extends ItemGroup {
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
    public ItemStack createIcon() {
        return new ItemStack(ItemHandler.DINOSAUR_MEAT);
    }
}
