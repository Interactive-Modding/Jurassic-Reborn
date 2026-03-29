package net.vit.jurassicreborn.common.items.genetics;

import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;
import net.vit.jurassicreborn.common.items.ModItems;
import net.vit.jurassicreborn.common.util.LangUtil;
import net.vit.jurassicreborn.common.util.ItemStackNbtUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import java.util.List;

public class DNAItem extends DNAContainerItem {

    public final Dinosaur dinosaur;

    public DNAItem(Properties pProperties, Dinosaur dino) {
        super(pProperties);
        this.dinosaur = dino;
    }

    @Override
    public Component getName(ItemStack pStack) {
        return LangUtil.replaceWithDinoName(this.dinosaur, "item.JurassicReborn.dna");
    }

    public Dinosaur getDinosaur(ItemStack stack) {
        Dinosaur dinosaur = null;

        if(stack.getItem() instanceof DNAItem dna){
            dinosaur = dna.dinosaur;
        }

        if (dinosaur == null) {
            dinosaur = Dinosaur.EMPTY;
        }

        return dinosaur;
    }

    @Override
    public int getContainerId(ItemStack stack) {
        return Dinosaur.DINOS.indexOf(this.getDinosaur(stack));
    }

    @Override
    public void appendHoverText(ItemStack pStack, Item.TooltipContext context, List<Component> lore, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, context, lore, pIsAdvanced);
    }

    @Override
    public ItemStack getDefaultInstance() {
        ItemStack stack = super.getDefaultInstance();
        String genetics = "ACGTACGTACGTACGTACGTACGTACGTAC"; // length 30, adjust if needed
        var tag = ItemStackNbtUtil.getOrCreateTag(stack);
        new net.vit.jurassicreborn.common.genetics.DinoDNA(dinosaur, 100, genetics).writeToNBT(tag);
        ItemStackNbtUtil.setTag(stack, tag);
        return stack;
    }

}
