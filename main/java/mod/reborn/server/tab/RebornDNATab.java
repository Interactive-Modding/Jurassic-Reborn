package mod.reborn.server.tab;

import java.util.List;

import mod.reborn.RebornMod;
import mod.reborn.server.dinosaur.Dinosaur;
import mod.reborn.server.entity.EntityHandler;
import mod.reborn.server.item.ItemHandler;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class RebornDNATab extends CreativeTabs {
    private ItemStack[] stacks = null;

    public RebornDNATab(String label) {
        super(label);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ItemStack getIconItemStack() {
        if (this.stacks == null) {
            List<Dinosaur> registeredDinosaurs = EntityHandler.getRegisteredDinosaurs();

            int dinosaurs = registeredDinosaurs.size();
            this.stacks = new ItemStack[dinosaurs * 3];

            int i = 0;

            for (Dinosaur dino : registeredDinosaurs) {
                int id = EntityHandler.getDinosaurId(dino);

                this.stacks[i] = new ItemStack(ItemHandler.DNA, 1, id);
                this.stacks[i + dinosaurs] = new ItemStack(ItemHandler.SOFT_TISSUE, 1, id);
                this.stacks[i + (dinosaurs * 2)] = new ItemStack(ItemHandler.SYRINGE, 1, id);

                i++;
            }
        }

        return this.stacks[(int) ((RebornMod.timerTicks / 20) % this.stacks.length)];
    }

    @Override
    public ItemStack getTabIconItem() {
        return new ItemStack(ItemHandler.DNA);
    }
}
