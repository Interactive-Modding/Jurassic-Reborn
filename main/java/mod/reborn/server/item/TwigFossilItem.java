package mod.reborn.server.item;

import com.google.common.collect.Lists;
import mod.reborn.server.api.GrindableItem;
import mod.reborn.server.tab.TabHandler;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.apache.commons.lang3.tuple.Pair;
import mod.reborn.server.block.tree.TreeType;
import mod.reborn.server.plant.PlantHandler;

import java.util.List;
import java.util.Random;

public class TwigFossilItem extends Item implements GrindableItem {
    public TwigFossilItem() {
        super();
        this.setCreativeTab(TabHandler.PLANTS);
    }

    @Override
    public boolean isGrindable(ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack getGroundItem(ItemStack stack, Random random) {
        NBTTagCompound tag = stack.getTagCompound();

        int outputType = random.nextInt(4);

        if (outputType == 3) {
            ItemStack output = new ItemStack(ItemHandler.PLANT_SOFT_TISSUE, 1, PlantHandler.getPlantId(TreeType.values()[random.nextInt(TreeType.values().length)].getPlant()));
            output.setTagCompound(tag);
            return output;
        } else if (outputType < 2) {
            return new ItemStack(Items.DYE, 1, 15);
        }

        return new ItemStack(Items.FLINT);
    }

    @Override
    public List<Pair<Float, ItemStack>> getChancedOutputs(ItemStack inputItem) {
        List<Pair<Float, ItemStack>> list = Lists.newArrayList();
        NBTTagCompound tag = inputItem.getTagCompound();
        float single = 100F/4F;
        float treeSingle = single / TreeType.values().length;
        for(TreeType treeType : TreeType.values()) {
            ItemStack output = new ItemStack(ItemHandler.PLANT_SOFT_TISSUE, 1, PlantHandler.getPlantId(treeType.getPlant()));
            output.setTagCompound(tag);
            list.add(Pair.of(treeSingle, output));
        }
        list.add(Pair.of(50f, new ItemStack(Items.DYE, 1, 15)));
        list.add(Pair.of(single, new ItemStack(Items.FLINT)));
        return list;
    }
}
