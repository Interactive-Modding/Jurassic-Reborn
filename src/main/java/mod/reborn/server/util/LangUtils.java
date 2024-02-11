package mod.reborn.server.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import mod.reborn.server.dinosaur.Dinosaur;
import mod.reborn.server.entity.item.AttractionSignEntity;
import mod.reborn.server.plant.Plant;
import mod.reborn.server.plant.PlantHandler;

import java.util.Locale;

@SuppressWarnings("unused")
public final class LangUtils
{
    public static final TranslateKey COLORS = new TranslateKey("color.%s.name");
    public static final TranslateKey PLANTS = new TranslateKey("plants.%s.name");
    public static final TranslateKey LORE = new TranslateKey("lore.%s.name");
    public static final TranslateKey TAME = new TranslateKey("message.tame.name");
    public static final TranslateKey SET_ORDER = new TranslateKey("message.set_order.name");
    public static final TranslateKey ORDER_VALUE = new TranslateKey("order.%s.name");
    public static final TranslateKey STAND_CHANGE = new TranslateKey("%s.standchange.name");
    public static final TranslateKey GENDER_CHANGE = new TranslateKey("%s.genderchange.name");
    public static final TranslateKey GUI = new TranslateKey("gui.%s.name");
    public static final TranslateKey STATUS = new TranslateKey("status.%s.name");
    public static final TranslateKey ENTITY_NAME   = new TranslateKey("entity.%s.name");
    public static final TranslateKey ENTITY_DESC   = new TranslateKey("entity.%s.desc");
    public static final TranslateKey CONTAINER_INV = new TranslateKey("container.inventory");

    @SuppressWarnings("deprecation")
    public static String translate(String langKey, Object... args) {
        return net.minecraft.util.text.translation.I18n.canTranslate(langKey)
                       ? net.minecraft.util.text.translation.I18n.translateToLocalFormatted(langKey, args)
                       : langKey;
    }

    public static String translate(TranslateKey langKey, Object... args) {
        return translate(langKey.key, args);
    }

    @SuppressWarnings("deprecation")
    public static String translateOrDefault(String langKey, String defaultVal) {
        return net.minecraft.util.text.translation.I18n.canTranslate(langKey) ? translate(langKey) : defaultVal;
    }

    public static String translateOrDefault(TranslateKey langKey, String defaultVal) {
        return translateOrDefault(langKey.key, defaultVal);
    }

    public static String translateEntityCls(Class<? extends Entity> eClass) {
        String namedEntry = EntityList.getTranslationName(EntityList.getKey(eClass));
        if( namedEntry != null ) {
            return translate(ENTITY_NAME.get(namedEntry));
        }

        return "[UNKNOWN] " + eClass.getName();
    }

    public static String translateEntityClsDesc(Class<? extends Entity> eClass) {
        String namedEntry = EntityList.getTranslationName(EntityList.getKey(eClass));
        if( namedEntry != null ) {
            return translate(ENTITY_DESC.get(namedEntry));
        }

        return "";
    }

    public static String getFormattedQuality(int quality) {
        return (quality == -1 ? TextFormatting.OBFUSCATED : "") + Integer.toString(quality);
    }

    public static String getFormattedGenetics(String genetics) {
        return genetics.isEmpty() ? TextFormatting.OBFUSCATED + "aaa" : genetics;
    }

    public static String getDinoName(Dinosaur dinosaur) {
        return translate(LangUtils.ENTITY_NAME.get("rebornmod." + dinosaur.getName().replace(" ", "_").toLowerCase(Locale.ENGLISH)));
    }

    public static String getDinoInfo(Dinosaur dinosaur) {
        return translate("info." + dinosaur.getName().replace(" ", "_").toLowerCase(Locale.ENGLISH) + ".name");
    }

    public static String getAttractionSignName(ItemStack stack) {
        return translate("attraction_sign." + (AttractionSignEntity.AttractionSignType.values()[stack.getItemDamage()].name().toLowerCase(Locale.ENGLISH)) + ".name");
    }

    public static String getGenderMode(int mode) {
        String modeString = "";
        switch( mode ) {
            case 0: modeString = "random"; break;
            case 1: modeString = "male"; break;
            case 2: modeString = "female"; break;
        }

        return translate("gender." + modeString + ".name");
    }
    public static String getPlantName(int plantId) {
        return getPlantName(PlantHandler.getPlantById(plantId));
    }

    public static String getPlantName(Plant plant) {
        return translate("plants." + (plant == null ? "null" : plant.getName().toLowerCase(Locale.ENGLISH).replaceAll(" ", "_")) + ".name");
    }

    public static String getStandType(boolean type) {
        return type == true ? translate("stand.stand.name") : translate("stand.placed.name");
    }

    public static final class TranslateKey
    {
        private final String key;

        public TranslateKey(String key) {
            this.key = key;
        }

        public TranslateKey(String key, Object... args) {
            this(String.format(key, args));
        }

        public String get() {
            return this.key;
        }

        public String get(Object... args) {
            return String.format(this.key, args);
        }
    }
}