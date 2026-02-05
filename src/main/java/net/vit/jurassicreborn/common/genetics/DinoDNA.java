package net.vit.jurassicreborn.common.genetics;

import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;
import net.vit.jurassicreborn.common.util.LangUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.List;

public class/* Bingo! */ DinoDNA extends DNA{
    private String genetics;
    private Dinosaur dinosaur;

    public DinoDNA(Dinosaur dinosaur, int quality, String genetics) {
        super(dinosaur.getFormattedName(), quality);
        this.genetics = genetics;
        this.dinosaur = dinosaur == null ? Dinosaur.EMPTY : dinosaur;
    }

    public static DinoDNA fromStack(ItemStack stack) {
        return readFromNBT(stack.getTag());
    }

    public static @Nullable DinoDNA readFromNBT(@Nullable CompoundTag tag) {

        if(tag == null)
            return null;
        if(!tag.contains("DNA"))
            return null;

        CompoundTag nbt = tag.getCompound("DNA");

        return new DinoDNA(Dinosaur.getDinosaurByName(nbt.getString("Dinosaur")), nbt.getInt("DNAQuality"), nbt.getString("Genetics"));
    }

    public void writeToNBT(CompoundTag tag) {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("DNAQuality", this.quality);
        nbt.putString("Genetics", this.genetics);
        nbt.putString("StorageId", "DinoDNA");
        nbt.putString("Dinosaur", this.dinosaur.getName());
        tag.put("DNA", nbt);
    }


    public String getGenetics() {
        return this.genetics;
    }

    public void addInformation(ItemStack stack, List<Component> tooltip) {
        tooltip.add(LangUtil.replaceWithDinoName(this.dinosaur, "lore.dinosaur").withStyle(ChatFormatting.DARK_AQUA));

        ChatFormatting colour;

        if (this.quality > 75) {
            colour = ChatFormatting.GREEN;
        } else if (this.quality > 50) {
            colour = ChatFormatting.YELLOW;
        } else if (this.quality > 25) {
            colour = ChatFormatting.GOLD;
        } else if(this.quality == -1) {
            colour = ChatFormatting.AQUA;
        } else {
            colour = ChatFormatting.RED;
        }
//        tooltip.add(colour + Component.translatable("lore.dna_quality.name").getString().replace("%1$s", LangUtils.getFormattedQuality(this.quality)));
        String qualityString = Component.translatable("lore.dna_quality").getString();
//        String[] splitQuality = qualityString.split("\\{[a-z]*\\}");//regex go bRRRRRRRRRRRRRR
        Component quality = LangUtil.getFormattedQuality(this.quality);
        qualityString = qualityString.formatted(quality.getString(), "%");
        tooltip.add(Component.literal(qualityString).withStyle(colour));

//        tooltip.add(ChatFormatting.BLUE + LangUtils.translate(LangUtils.LORE.get("genetic_code")).replace("%1$s", LangUtils.getFormattedGenetics(this.genetics)));

        String geneticString = Component.translatable("lore.genetic_code").getString();
//        String[] splitGenetics = geneticString.split("\\{[a-z]*\\}");
        Component genetics = LangUtil.getFormattedGenetics(this.genetics);
        geneticString = geneticString.formatted(genetics.getString());
        Component formattedQuality = Component.literal(geneticString).withStyle(ChatFormatting.BLUE);
//        if(splitGenetics.length > 1){
//            formattedQuality = Component.literal(splitQuality[0]).append(genetics).append(splitGenetics[1]).withStyle(ChatFormatting.BLUE);
//        }else{
//            formattedQuality = Component.literal(splitQuality[0]).append(genetics).withStyle(ChatFormatting.BLUE);
        //}
        tooltip.add(formattedQuality);



    }

    public Dinosaur getDinosaur() {
        return this.dinosaur;
    }

    public int getMetadata() {//fool! old system! do not use this!
        return -1;
    }

    public String getDinoName(){
        return this.dinosaur.getName();
    }
}