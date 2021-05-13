package mod.reborn.server.tab;

import mod.reborn.server.items.ItemHandler;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;

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
        return new ItemStack(ItemHandler.FOSSILS.get("skull"));
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
}
