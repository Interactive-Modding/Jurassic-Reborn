package mod.reborn.server.items;

import mod.reborn.server.tab.TabHandler;
import mod.reborn.server.util.LangUtils;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import java.util.Collections;
import java.util.LinkedList;

public class DNAItem extends DNAContainerItem {
    public DNAItem() {
        super(new Properties().group(TabHandler.DNA));
    }

    @Override
    public ITextComponent getDisplayName(ItemStack stack) {
        return new StringTextComponent(LangUtils.translate(this.getName() + ".name").replace("{dino}", LangUtils.getDinoName(this.getDinosaur(stack))));
    }

    public Dinosaur getDinosaur(ItemStack stack) {
        Dinosaur dinosaur = EntityHandler.getDinosaurById(stack.getDamage());

        if (dinosaur == null) {
            dinosaur = EntityHandler.VELOCIRAPTOR;
        }

        return dinosaur;
    }

    @Override
    public int getContainerId(ItemStack stack) {
        return EntityHandler.getDinosaurId(this.getDinosaur(stack));
    }

    public void getSubItems(ItemGroup tab, NonNullList<ItemStack> subtypes) {
        List<Dinosaur> dinosaurs = new LinkedList<>(EntityHandler.getDinosaurs().values());

        Collections.sort(dinosaurs);
        if(this.getCreativeTabs().contains(tab))
            for (Dinosaur dinosaur : dinosaurs) {
                if (dinosaur.shouldRegister()) {
                    subtypes.add(new ItemStack(this, 1, EntityHandler.getDinosaurId(dinosaur)));
                }
            }
    }
}
