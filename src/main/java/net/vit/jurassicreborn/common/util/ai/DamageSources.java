package net.vit.jurassicreborn.common.util.ai;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.LivingEntity;

/**
 */
public class DamageSources {
    public static final DamageSource SHOCK = new ShockDamageSource();

    public static final DamageSource CAR = new MultipleNameDamageSource("reborn.car", 3.0F);
    public static final DamageSource BULLET = new MultipleNameDamageSource("reborn.bullet", 4.0F);

    /**
     */
    public static class ShockDamageSource extends DamageSource {
        public ShockDamageSource() {
            super("reborn.shock");
            this.setExplosion();        // example: bypasses armor like an explosion
        }
    }

    /**
     * A DamageSource with a custom name and setDamageBypassesArmor(flag).
     */
    public static class MultipleNameDamageSource extends DamageSource {
        public MultipleNameDamageSource(String damageTypeIn, float exhaustion) {
            super(damageTypeIn);
            this.setExplosion();
        }
    }
}
