package mod.reborn.server.item;

import com.google.common.collect.Lists;
import mod.reborn.server.api.SequencableItem;
import mod.reborn.server.tab.TabHandler;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;
import mod.reborn.server.dinosaur.Dinosaur;
import mod.reborn.server.entity.EntityHandler;
import mod.reborn.server.genetics.DinoDNA;
import mod.reborn.server.genetics.GeneticsHelper;
import mod.reborn.server.util.LangUtils;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class SoftTissueItem extends Item implements SequencableItem {
    public SoftTissueItem() {
        this.setHasSubtypes(true);

        this.setCreativeTab(TabHandler.DNA);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return LangUtils.translate(this.getUnlocalizedName() + ".name").replace("{dino}", LangUtils.getDinoName(this.getDinosaur(stack)));
    }

    public Dinosaur getDinosaur(ItemStack stack) {
        Dinosaur dinosaur = EntityHandler.getDinosaurById(stack.getItemDamage());

        if (dinosaur == null) {
            dinosaur = EntityHandler.VELOCIRAPTOR;
        }

        return dinosaur;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subtypes) {
        List<Dinosaur> dinosaurs = new LinkedList<>(EntityHandler.getDinosaurs().values());

        Collections.sort(dinosaurs);
        if(this.isInCreativeTab(tab))
        for (Dinosaur dinosaur : dinosaurs) {
            if (dinosaur.shouldRegister()) {
                subtypes.add(new ItemStack(this, 1, EntityHandler.getDinosaurId(dinosaur)));
            }
        }
    }

    @Override
    public boolean isSequencable(ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack getSequenceOutput(ItemStack stack, Random random) {
        NBTTagCompound nbt = stack.getTagCompound();

        if (nbt == null) {
            nbt = new NBTTagCompound();
            int quality = SequencableItem.randomQuality(random);
            DinoDNA dna = new DinoDNA(EntityHandler.getDinosaurById(stack.getItemDamage()), quality, GeneticsHelper.randomGenetics(random));
            dna.writeToNBT(nbt);
        } else if (!nbt.hasKey("Dinosaur")) {
            nbt.setInteger("Dinosaur", stack.getItemDamage());
        }

        ItemStack output = new ItemStack(ItemHandler.STORAGE_DISC);
        output.setTagCompound(nbt);

        return output;
    }

    @Override
    public List<ItemStack> getJEIRecipeTypes() {
        return getItemSubtypes(this);
    }

    @Override
    public List<Pair<Float, ItemStack>> getChancedOutputs(ItemStack inputItem) {
        List<Pair<Float, ItemStack>> list = Lists.newArrayList();
        NBTTagCompound nbt = new NBTTagCompound();
        DinoDNA dna = new DinoDNA(EntityHandler.getDinosaurById(inputItem.getItemDamage()), -1, "");
        dna.writeToNBT(nbt);
        ItemStack output = new ItemStack(ItemHandler.STORAGE_DISC);
        output.setTagCompound(nbt);
        list.add(Pair.of(100F, output));
        return list;
    }
}
