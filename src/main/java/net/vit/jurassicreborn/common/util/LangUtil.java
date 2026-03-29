package net.vit.jurassicreborn.common.util;

import net.minecraft.world.item.ItemStack;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.vit.jurassicreborn.common.entities.item.AttractionSignEntity;
import net.vit.jurassicreborn.common.items.misc.AttractionSignItem;

import java.util.Locale;
import java.util.function.Supplier;

/**
 * This is different than LangUtils!!!!!!! that is OLD CODE, built to work with raw string translation!<br>
 * <b>THIS</b> is built to work with the updated TranslatableContents/MutableComponent system! <br>
 * <b><i>USE THIS INSTEAD!!!!!!!!!</i></b>
 */
public class LangUtil {

    public static final String LORE = "lore.%s";
    public static final String GENDER_CHANGE = "%s.genderchange";

    // 🔷 These are what you are missing:
    public static final TranslateKey GUI = new TranslateKey("gui.%s.name");
    public static final TranslateKey STATUS = new TranslateKey("status.%s.name");
    public static final TranslateKey STAND_CHANGE = new TranslateKey("%s.standchange.name");


    public static MutableComponent getDinoName(Dinosaur dino){
        return Component.translatable(getEntityKey(dino.getName().replace(" ", "_").toLowerCase(Locale.ENGLISH)));
    }

    public static MutableComponent replaceInKey(Supplier<String> replacement, String replaceTarget, String key){
        return Component.literal(Component.translatable(key).getString().replace(replaceTarget, replacement.get()) );
    }

    public static Component replaceWithDinosaurName(Dinosaur name, String key){
        return replaceInKey(() -> name.getTranslatedName().getString(), "{dinosaur}", key);
    }
    public static MutableComponent replaceWithDinoName(Dinosaur name, String key){
        return replaceInKey(() -> name.getTranslatedName().getString(), "{dino}", key);
    }

    public static String getStandType(boolean type) {
        return type == true ? translate("stand.stand.name") : translate("stand.placed.name");
    }

    public static String getEntityKey(String key){
        return "entity." + JurassicReborn.MODID + "." + key;
    }

    public static Component getFormattedQuality(int quality){
        return quality == -1 ? Component.literal("??").withStyle(ChatFormatting.OBFUSCATED) : Component.literal(Integer.toString(quality));
    }
    public static String getDinoInfo(Dinosaur dinosaur) {
        String key = "info." + dinosaur.getName().replace(" ", "_").toLowerCase(Locale.ENGLISH) + ".name";
        return net.minecraft.network.chat.Component.translatable(key).getString();
    }
// Add these to LangUtil

    // 1. Direct string translation for simple keys
    public static String translate(String key) {
        return Component.translatable(key).getString();
    }

    // 2. Helper for gender mode as String (legacy-style)
    public static String getGenderMode(int gender) {
        return getGender(gender).getString();
    }

    // 3. (Optional) Helper for GUI/status keys, if you want
    public static String getGUIKey(String key) {
        return "gui." + JurassicReborn.MODID + "." + key;
    }
    public static String getStatusKey(String status) {
        return "status." + status;
    }


    public static Component getFormattedGenetics(String genetics) {
        return genetics.isEmpty() ? Component.literal("???").withStyle(ChatFormatting.OBFUSCATED) : Component.literal(genetics);
    }

    public static MutableComponent getGender(int gender){
        String genderKey = "";
        if(gender == 0){
            genderKey = "random";
        }else if(gender == 1){
            genderKey = "male";
        }else if(gender == 2){
            genderKey = "female";
        }

        return Component.translatable("gender." + genderKey);

    }
    public static class TranslateKey {
        private final String format;

        public TranslateKey(String format) {
            this.format = format;
        }

        public String get(String key) {
            return String.format(format, key);
        }
    }
    public static String getAttractionSignName(ItemStack stack) {
        if (stack.getItem() instanceof AttractionSignItem sign) {
            AttractionSignEntity.AttractionSignType type = sign.getType();
            String key = String.format(
                    "attraction_sign.%s.name",
                    type.name().toLowerCase(Locale.ROOT)
            );
            return translate(key);
        }
        return "";
    }
}
