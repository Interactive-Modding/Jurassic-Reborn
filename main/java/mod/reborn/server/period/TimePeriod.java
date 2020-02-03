package mod.reborn.server.period;

import net.minecraft.util.IStringSerializable;

public enum TimePeriod implements IStringSerializable {
    QUATERNARY("quaternary", 2.588F, 0.0F),
    NEOGENE("neogene", 23.03F, 2.589F),
    PALEOGENE("paleogene", 66.0F, 23.04F),
    CRETACEOUS("cretaceous", 145.5F, 66.1F),
    JURASSIC("jurassic", 201.3F, 145.6F),
    TRIASSIC("triassic", 252.17F, 201.4F),
    PERMIAN("permian", 298.9F, 252.18F),
    CARBONIFEROUS("carboniferous", 358.9F, 299.0F),
    DEVONIAN("devonian", 419.2F, 359.0F),
    SILURIAN("silurian", 443.4F, 419.3F),
    ORDOVICIAN("ordovician", 485.4F, 443.5F),
    CAMBRIAN("cambrian", 541.0F, 485.5F),
    NONE("none", 0F, 0F);

    private final String name;
    private final float startTime;
    private final float endTime;

    TimePeriod(String name, float startTime, float endTime) {
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public static int getStartYLevel(TimePeriod period) {
        return 64 - (int) (period.getStartTime() != 0 ? period.getStartTime() * 64.0F / 541.0F : 0);
    }

    public static int getEndYLevel(TimePeriod period) {
        return 64 - (int) (period.getEndTime() != 0 ? period.getEndTime() * 64.0F / 541.0F : 0);
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public float getStartTime() {
        return this.startTime;
    }

    public float getEndTime() {
        return this.endTime;
    }
}
