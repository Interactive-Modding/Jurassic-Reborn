package mod.reborn.server.item;

import mod.reborn.server.tab.TabHandler;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import mod.reborn.server.plant.Plant;
import mod.reborn.server.plant.PlantHandler;
import mod.reborn.server.util.LangUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PlantDNAItem extends Item {
    public PlantDNAItem() {
        super();
        this.setCreativeTab(TabHandler.PLANTS);
        this.setHasSubtypes(true);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        String plantName = this.getPlant(stack).getName().toLowerCase(Locale.ENGLISH).replaceAll(" ", "_");

        return LangUtils.translate(this.getUnlocalizedName() + ".name").replace("{plant}", LangUtils.translate(LangUtils.PLANTS.get(plantName)));
    }

    public Plant getPlant(ItemStack stack) {
        Plant plant = PlantHandler.getPlantById(stack.getItemDamage());

        if (plant == null) {
            plant = PlantHandler.SMALL_ROYAL_FERN;
        }

        return plant;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subtypes) {
        List<Plant> plants = new LinkedList<>(PlantHandler.getPrehistoricPlantsAndTrees());

        Map<Plant, Integer> ids = new HashMap<>();

        for (Plant plant : plants) {
            ids.put(plant, PlantHandler.getPlantId(plant));
        }

        Collections.sort(plants);
        if(this.isInCreativeTab(tab))
        for (Plant plant : plants) {
            if (plant.shouldRegister()) {
                subtypes.add(new ItemStack(this, 1, ids.get(plant)));
            }
        }
    }

    //INFO: use DNAContainerItem.getDNAQuality()
//    public int getDNAQuality(EntityPlayer player, ItemStack stack) {
//        int quality = player.capabilities.isCreativeMode ? 100 : 0;
//
//        NBTTagCompound nbt = stack.getTagCompound();
//
//        if (nbt == null) {
//            nbt = new NBTTagCompound();
//        }
//
//        if (nbt.hasKey("DNAQuality")) {
//            quality = nbt.getInteger("DNAQuality");
//        } else {
//            nbt.setInteger("DNAQuality", quality);
//        }
//
//        stack.setTagCompound(nbt);
//
//        return quality;
//    }

//    @Override
//    public void addInformation(ItemStack stack, World worldIn, List<String> lore, ITooltipFlag flagIn) {
//        int quality = this.getDNAQuality(player, stack);
//
//        TextFormatting formatting;
//
//        if (quality > 75) {
//            formatting = TextFormatting.GREEN;
//        } else if (quality > 50) {
//            formatting = TextFormatting.YELLOW;
//        } else if (quality > 25) {
//            formatting = TextFormatting.GOLD;
//        } else {
//            formatting = TextFormatting.RED;
//        }
//
//        lore.add(formatting + new LangHelper("lore.dna_quality.name").withProperty("quality", quality + "").build());
//    }
}
