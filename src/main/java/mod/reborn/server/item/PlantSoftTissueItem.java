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
import mod.reborn.server.genetics.PlantDNA;
import mod.reborn.server.plant.Plant;
import mod.reborn.server.plant.PlantHandler;
import mod.reborn.server.util.LangUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class PlantSoftTissueItem extends Item implements SequencableItem {
    public PlantSoftTissueItem() {
        super();
        this.setHasSubtypes(true);
        this.setCreativeTab(TabHandler.PLANTS);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        getItemSubtypes(this);

        return LangUtils.translate(this.getUnlocalizedName() + ".name").replace("{plant}", LangUtils.getPlantName(this.getPlant(stack)));
    }

    public Plant getPlant(ItemStack stack) {
        Plant plant = PlantHandler.getPlantById(stack.getItemDamage());

        if (plant == null) {
            plant = PlantHandler.SMALL_ROYAL_FERN;
        }

        return plant;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subtypes) {
        List<Plant> plants = new LinkedList<>(PlantHandler.getPrehistoricPlantsAndTrees());

        Map<Plant, Integer> ids = new HashMap<>();

        for (Plant plant : plants) {
            ids.put(plant, PlantHandler.getPlantId(plant));
        }
        Collections.sort(plants);
        if(this.isInCreativeTab(tab)) {
            for (Plant plant : plants) {
                subtypes.add(new ItemStack(this, 1, ids.get(plant)));
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
            PlantDNA dna = new PlantDNA(stack.getItemDamage(), SequencableItem.randomQuality(random));
            dna.writeToNBT(nbt);
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
        NBTTagCompound nbt = new NBTTagCompound();
        PlantDNA dna = new PlantDNA(inputItem.getItemDamage(), -1);
        dna.writeToNBT(nbt);
        ItemStack output = new ItemStack(ItemHandler.STORAGE_DISC);
        output.setTagCompound(nbt);
        return Lists.newArrayList(Pair.of(100F, output));
    }
}
