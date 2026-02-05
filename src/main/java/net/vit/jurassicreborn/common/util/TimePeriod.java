package net.vit.jurassicreborn.common.util;

import net.minecraft.nbt.CompoundTag;

public enum TimePeriod {
    /**
     * it will prefer the higher period on the list when using {@link TimePeriod#byYValue(int)}
     */
    QUATERNARY("quaternary", 64, 64),
    NEOGENE("neogene", 58, 64),
    PALEOGENE("paleogene", 48 ,58),
    CRETACEOUS("cretaceous", 30, 48),
    JURASSIC("jurassic", 16, 30),
    TRIASSIC("triassic", 4, 16),
    PERMIAN("permian", -6, 4),
    CARBONIFEROUS("carboniferous", -20, -6),
    DEVONIAN("devonian", -36, -20),
    SILURIAN("silurian", -40, -36),
    ORDOVICIAN("ordovician", -50, -40),
    CAMBRIAN("cambrian", -64, -50),
    NONE("none", 0, 0, 0);


    public final String name;
    public final int startY, endY, range;
    TimePeriod(String name, int startY, int endY){
        this.name = name;
        this.startY = startY;
        this.endY = endY;
        this.range = startY - endY == 0 ? 1 : startY - endY;
    }

    TimePeriod(String name, int startY, int endY, int range){
        this.name = name;
        this.startY = startY;
        this.endY = endY;
        this.range = range;
    }

    public static TimePeriod byName(String name){
        for(TimePeriod period : values()){
            if(period.name.equals(name)){
                return period;
            }
        }
        return NONE;
    }

    public static TimePeriod byYValue(int val){
        if(val > 64 || val < -64){
            return NONE;
        }else if(val == 64){
            return QUATERNARY;
        }
        TimePeriod previous = QUATERNARY;
        for(TimePeriod p : values()){
            if(p == QUATERNARY) {
                continue;
            }
            if(val <= p.endY && val >= p.startY )
                return p;

        }
        return NONE;

    }

    public static TimePeriod fromNbt(CompoundTag nbt){
        if(nbt != null){
            return byYValue(nbt.getInt("TimePeriod"));
        }
        return NONE;
    }

}
