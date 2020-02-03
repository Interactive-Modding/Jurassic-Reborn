package mod.reborn.server.genetics;

import mod.reborn.server.dinosaur.Dinosaur;
import mod.reborn.server.entity.EntityHandler;
import mod.reborn.server.util.LangUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;

import java.util.List;

public class/* Bingo! */ DinoDNA {
    private int quality;
    private String genetics;
    private Dinosaur dinosaur;

    public DinoDNA(Dinosaur dinosaur, int quality, String genetics) {
        this.quality = quality;
        this.genetics = genetics;
        this.dinosaur = dinosaur;
    }

    public static DinoDNA fromStack(ItemStack stack) {
        return readFromNBT(stack.getTagCompound());
    }

    public static DinoDNA readFromNBT(NBTTagCompound nbt) {
        return nbt == null ? null : new DinoDNA(EntityHandler.getDinosaurById(nbt.getInteger("Dinosaur")), nbt.getInteger("DNAQuality"), nbt.getString("Genetics"));
    }

    public void writeToNBT(NBTTagCompound nbt) {
        nbt.setInteger("DNAQuality", this.quality);
        nbt.setString("Genetics", this.genetics);
        nbt.setString("StorageId", "DinoDNA");
        nbt.setInteger("Dinosaur", EntityHandler.getDinosaurId(this.dinosaur));
    }

    public int getDNAQuality() {
        return this.quality;
    }

    public String getGenetics() {
        return this.genetics;
    }

    public void addInformation(ItemStack stack, List<String> tooltip) {
        tooltip.add(TextFormatting.DARK_AQUA + LangUtils.translate(LangUtils.LORE.get("dinosaur")).replace("{dino}", LangUtils.getDinoName(this.dinosaur)));

        TextFormatting colour;

        if (this.quality > 75) {
            colour = TextFormatting.GREEN;
        } else if (this.quality > 50) {
            colour = TextFormatting.YELLOW;
        } else if (this.quality > 25) {
            colour = TextFormatting.GOLD;
        } else if(this.quality == -1) {
            colour = TextFormatting.AQUA;
        } else {
            colour = TextFormatting.RED;
        }
        tooltip.add(colour + LangUtils.translate(LangUtils.LORE.get("dna_quality")).replace("{quality}", LangUtils.getFormattedQuality(this.quality)));
        tooltip.add(TextFormatting.BLUE + LangUtils.translate(LangUtils.LORE.get("genetic_code")).replace("{code}", LangUtils.getFormattedGenetics(this.genetics)));
    }

    public Dinosaur getDinosaur() {
        return this.dinosaur;
    }

    public int getMetadata() {
        return EntityHandler.getDinosaurId(this.dinosaur);
    }
}
