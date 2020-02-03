package mod.reborn.server.damage;

import net.minecraft.util.DamageSource;

public class DamageSources {
    public static final DamageSource SHOCK = new ShockDamageSource();
    public static final DamageSource CAR = new MultipleNameDamageSource("reborn.car", 3);
    public static final DamageSource BULLET = new MultipleNameDamageSource("reborn.bullet", 4);
}
