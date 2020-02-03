package mod.reborn.server.tab;

import java.util.ArrayList;
import java.util.List;

import mod.reborn.server.dinosaur.Dinosaur;
import mod.reborn.server.entity.EntityHandler;
import mod.reborn.server.item.FossilItem;
import mod.reborn.server.item.ItemHandler;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class RebornFossilTab extends CreativeTabs {
    private int[] metas;

    public RebornFossilTab(String label) {
        super(label);

        List<Dinosaur> fossilDinosaurs = this.getFossilDinosaurs();
        this.metas = new int[fossilDinosaurs.size()];

        int i = 0;

        for (Dinosaur dino : fossilDinosaurs) {
            this.metas[i] = EntityHandler.getDinosaurId(dino);

            i++;
        }
    }

    public List<Dinosaur> getFossilDinosaurs() {
        List<Dinosaur> fossilDinosaurs = new ArrayList<>();

        for (Dinosaur dino : FossilItem.fossilDinosaurs.get("skull")) {
            if (dino.shouldRegister()) {
                fossilDinosaurs.add(dino);	
            }
        }

        return fossilDinosaurs;
    }


    @Override
    public ItemStack getTabIconItem() {
        return new ItemStack(ItemHandler.FOSSILS.get("skull"));
    }
}
