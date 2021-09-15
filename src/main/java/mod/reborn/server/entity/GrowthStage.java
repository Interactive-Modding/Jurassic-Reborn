package mod.reborn.server.entity;

import mod.reborn.server.util.LangUtils;

public enum GrowthStage {
    ADULT, INFANT, JUVENILE, ADOLESCENT, SKELETON;

    public static final GrowthStage[] VALUES = GrowthStage.values();

    public String getLocalization() {
        return LangUtils.translate("growth_stage." + this.name().toLowerCase() + ".name");
    }
}
