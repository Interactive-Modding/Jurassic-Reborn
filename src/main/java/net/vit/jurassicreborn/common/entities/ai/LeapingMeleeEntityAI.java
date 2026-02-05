package net.vit.jurassicreborn.common.entities.ai;

import net.minecraft.world.entity.LivingEntity;
import net.vit.jurassicreborn.common.entities.DinosaurEntity;

import java.util.EnumSet;

public class LeapingMeleeEntityAI extends DinosaurAttackMeleeEntityAI {
    public LeapingMeleeEntityAI(DinosaurEntity entity, double speed) {
        super(entity, speed, false);
        this.setFlags(EnumSet.of(Flag.TARGET));
    }

    @Override
    public boolean canUse() {
        LivingEntity target = this.getAttacker().getAttackTarget();
        return super.canUse() && target != null && this.isInRange(target);
    }

    @Override
    public boolean canContinueToUse() {
        return this.canUse() && super.canContinueToUse();
    }

    private boolean isInRange(LivingEntity target) {
        float distance = this.getAttacker().distanceTo(target);
        float maxRange = this.getAttacker().getBbWidth() * 6.0F;
        float minRange = this.getAttacker().getBbWidth() * 1.5F;
        return distance >= minRange && distance <= maxRange;
    }
}
