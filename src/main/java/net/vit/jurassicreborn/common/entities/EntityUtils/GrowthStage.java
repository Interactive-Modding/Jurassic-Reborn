package net.vit.jurassicreborn.common.entities.EntityUtils;

import net.minecraft.network.chat.Component;

public enum GrowthStage {
    ADULT, INFANT, JUVENILE, /*FLUORESCENT*/ADOLESCENT, SKELETON;

    // Enum#values() is not being cached for security reasons. DONT PERFORM CHANGES ON THIS ARRAY
    public static final GrowthStage[] VALUES = GrowthStage.values();

    public String getLocalization() {
        return Component.translatable("growth_stage" + this.name().toLowerCase() + ".name").getString();
    }
}