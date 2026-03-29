package net.vit.jurassicreborn.common.items.genetics;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;
import net.vit.jurassicreborn.common.genetics.DinoDNA;
import net.vit.jurassicreborn.common.genetics.GeneticsHelper;
import net.vit.jurassicreborn.common.items.ModItems;
import net.vit.jurassicreborn.common.util.LangUtil;
import net.vit.jurassicreborn.common.util.api.DinosaurItem;
import net.vit.jurassicreborn.common.util.api.SequencableItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import net.vit.jurassicreborn.common.util.ItemStackNbtUtil;

public class SoftTissueItem extends Item implements SequencableItem, DinosaurItem {
    protected final Dinosaur dino;
    public SoftTissueItem(Properties pProperties, Dinosaur dino) {
        super(pProperties);
        this.dino = dino;
    }

    @Override
    public Component getName(ItemStack pStack) {
        return LangUtil.replaceWithDinoName(this.dino, "item.JurassicReborn.soft_tissue");
    }

    @Override
    public Dinosaur getDinosaur(ItemStack stack) {
        if(stack.getItem() == this) {
            return this.dino;
        }
        if(stack.getItem() instanceof SoftTissueItem i) {
            return i.getDinosaur(stack);
        }
        if(stack.getItem() instanceof DinosaurItem i){
            return i.getDinosaur(stack);
        }

        return Dinosaur.EMPTY;
    }



    @Override
    public List<Pair<Float, ItemStack>> getChancedOutputs(ItemStack inputItem) {

        List<Pair<Float, ItemStack>> list = Lists.newArrayList();
        CompoundTag nbt = new CompoundTag();

        DinoDNA dna = new DinoDNA(getDinosaur(inputItem), -1, "");
        dna.writeToNBT(nbt);

        ItemStack output = new ItemStack(ModItems.STORAGE_DISC.get());
        ItemStackNbtUtil.setTag(output, nbt);
        StorageDiscItem.applyCustomModelData(output);
        list.add(Pair.of(100F, output));
        return list;
    }

    @Override
    public boolean isSequencable(ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack getSequenceOutput(ItemStack stack, RandomSource random) {
        CompoundTag tag = ItemStackNbtUtil.getTag(stack);
        DinoDNA dna = tag != null ? DinoDNA.readFromNBT(tag) : null;

        int quality   = (dna != null) ? dna.getDNAQuality() : Math.abs(SequencableItem.randomQuality(random) / 2);
        String genes  = (dna != null) ? dna.getGenetics()    : GeneticsHelper.randomGenetics(random);

        ItemStack out = ModItems.STORAGE_DISC.get().getDefaultInstance(); // or whatever your storage disc item is
        CompoundTag outTag = ItemStackNbtUtil.getOrCreateTag(out);
        new DinoDNA(getDinosaur(stack), quality, genes).writeToNBT(outTag);
        ItemStackNbtUtil.setTag(out, outTag);
        StorageDiscItem.applyCustomModelData(out);
        return out;
    }


//    @Override
//    public void fillItemCategory(CreativeModeTab pCategory, NonNullList<ItemStack> pItems) {
//        DinosaurItem.setDino(ModItems.SOFT_TISSUE.get(dino).get().getDefaultInstance(), dino);
//        super.fillItemCategory(pCategory, pItems);
//    }

    private void initDnaCompound(ItemStack stack, RandomSource random, CompoundTag nbt) {
        int quality = Math.abs((SequencableItem.randomQuality(random))/2);
        DinoDNA dna = new DinoDNA(getDinosaur(stack), quality, GeneticsHelper.randomGenetics(random));
        dna.writeToNBT(nbt);
    }
}
