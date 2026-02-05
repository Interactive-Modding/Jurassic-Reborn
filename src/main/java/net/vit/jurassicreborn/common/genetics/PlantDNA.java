package net.vit.jurassicreborn.common.genetics;

import net.vit.jurassicreborn.common.plants.Plant;
import net.vit.jurassicreborn.common.plants.PlantHandler;
import net.vit.jurassicreborn.common.util.LangUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class PlantDNA extends DNA{
    private ResourceLocation plant;

    public PlantDNA(ResourceLocation plant, int quality) {
        super(plant.toString(), quality);
        this.plant = plant;
    }

    public static PlantDNA fromStack(ItemStack stack) {
        return readFromNBT(stack.getTag());
    }

    public static PlantDNA readFromNBT(CompoundTag tag) {
        if(tag == null)
            return null;
        if(!tag.contains("DNA"))
            return null;
        CompoundTag nbt = tag.getCompound("DNA");

        return new PlantDNA(new ResourceLocation(nbt.getString("Plant")), nbt.getInt("DNAQuality"));
    }

    public void writeToNBT(CompoundTag tag) {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("DNAQuality", this.quality);

//        CompoundTag plant = new CompoundTag();
//        plant.putString("id", );
        nbt.putString("Plant", this.plant.toString());
        nbt.putString("StorageId", "PlantDNA");
        tag.put("DNA", nbt);
    }


    public ResourceLocation getPlant() {
        return this.plant;
    }

    public void addInformation(ItemStack stack, List<Component> tooltip) {
        tooltip.add( Component.literal(Component.translatable("lore.plant").getString().replace("{plant}", PlantHandler.getPlantById(this.plant).getName())).withStyle(ChatFormatting.DARK_AQUA));

        ChatFormatting formatting;

        if (this.quality > 75) {
            formatting = ChatFormatting.GREEN;
        } else if (this.quality > 50) {
            formatting = ChatFormatting.YELLOW;
        } else if (this.quality > 25) {
            formatting = ChatFormatting.GOLD;
        } else if (this.quality == -1) {
            formatting = ChatFormatting.AQUA;
        } else {
            formatting = ChatFormatting.RED;
        }

        String qualityString = Component.translatable("lore.dna_quality").getString();
        Component quality = LangUtil.getFormattedQuality(this.quality);
        tooltip.add(Component.literal(qualityString.formatted(quality.getString(), "%")).withStyle(formatting));
    }

    public Plant getRealPlant(){
        return PlantHandler.getPlantById(this.plant);
    }

//    public int getMetadata() {
//        return this.plant;
//    }
}