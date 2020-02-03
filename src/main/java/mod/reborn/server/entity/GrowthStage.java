package mod.reborn.server.entity;

import mod.reborn.server.util.LangUtils;

public enum GrowthStage {
        ADULT, INFANT, JUVENILE, /*FLUORESCENT*/ADOLESCENT, SKELETON;

    // Enum#values() is not being cached for security reasons. DONT PERFORM CHANGES ON THIS ARRAY
    public static final GrowthStage[] VALUES = GrowthStage.values();

    public String getLocalization() {
        return LangUtils.translate("growth_stage." + this.name().toLowerCase() + ".name");
    }
}
