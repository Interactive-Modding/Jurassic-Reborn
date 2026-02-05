package net.vit.jurassicreborn.common.util;

public enum DNACombinatorHybridizerMode {
    HYBRIDIZER(false, "HYBRIDIZER"),
    COMBINATOR(true, "COMBINATOR");

    public static final DNACombinatorHybridizerMode[] modes = {HYBRIDIZER, COMBINATOR};

    public final boolean mode;

    public final String name;
    DNACombinatorHybridizerMode(boolean mode, String name){
        this.mode = mode;
        this.name = name;
    }

    public DNACombinatorHybridizerMode fromString(String test){
        for(DNACombinatorHybridizerMode m : modes){
            if(m.name.equals(test)){
                return m;
            }
        }
        return null;
    }
}
