package mod.reborn.server.tab;

import mod.reborn.server.dinosaur.Dinosaur;
import mod.reborn.server.entity.EntityHandler;
import mod.reborn.server.items.ItemHandler;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.util.ArrayList;
import java.util.List;

public class RebornFossilTab extends ItemGroup {
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

    @Override
    public ItemStack createIcon() {
        //return new ItemStack(ItemHandler.FOSSILS.get("skull"));
        return new ItemStack(Items.PLAYER_HEAD);
    }

    public List<Dinosaur> getFossilDinosaurs() {
        List<Dinosaur> fossilDinosaurs = new ArrayList<>();

        //for (Dinosaur dino : FossilItem.fossilDinosaurs.get("skull")) {
            //if (dino.shouldRegister()) {
                //fossilDinosaurs.add(dino);
            //}
        //}

        return fossilDinosaurs;
    }
}
