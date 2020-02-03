package mod.reborn.server.genetics;

import mod.reborn.server.util.LangUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;

import java.util.List;

public class PlantDNA {
    private int plant;
    private int quality;

    public PlantDNA(int plant, int quality) {
        this.plant = plant;
        this.quality = quality;
    }

    public static PlantDNA fromStack(ItemStack stack) {
        return readFromNBT(stack.getTagCompound());
    }

    public static PlantDNA readFromNBT(NBTTagCompound nbt) {
        return new PlantDNA(nbt.getInteger("Plant"), nbt.getInteger("DNAQuality"));
    }

    public void writeToNBT(NBTTagCompound nbt) {
        nbt.setInteger("DNAQuality", this.quality);
        nbt.setInteger("Plant", this.plant);
        nbt.setString("StorageId", "PlantDNA");
    }

    public int getDNAQuality() {
        return this.quality;
    }

    public int getPlant() {
        return this.plant;
    }

    public void addInformation(ItemStack stack, List<String> tooltip) {
        tooltip.add(TextFormatting.DARK_AQUA + LangUtils.translate(LangUtils.LORE.get("plant")).replace("{plant}", LangUtils.getPlantName(this.plant)));

        TextFormatting formatting;

        if (this.quality > 75) {
            formatting = TextFormatting.GREEN;
        } else if (this.quality > 50) {
            formatting = TextFormatting.YELLOW;
        } else if (this.quality > 25) {
            formatting = TextFormatting.GOLD;
        } else if (this.quality == -1) {
            formatting = TextFormatting.AQUA;
        } else {
            formatting = TextFormatting.RED;
        }

        tooltip.add(formatting + LangUtils.translate(LangUtils.LORE.get("dna_quality")).replace("{quality}", LangUtils.getFormattedQuality(this.quality)));
    }

    public int getMetadata() {
        return this.plant;
    }
}
