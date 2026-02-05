package net.vit.jurassicreborn.common.entities.ai;

import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimation;
import net.vit.jurassicreborn.common.entities.DinosaurEntity;
import net.vit.jurassicreborn.common.util.GameRuleHandler;

import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

public class MateEntityAI extends Goal {

    private static final double SEARCH_RANGE     = 16.0D;
    private static final double START_SPEED      = 1.0D;
    private static final double TOUCH_GROWTH     = 0.5D;
    private static final int    MAX_TICKS        = 20 * 20;  // ~20s before giving up
    private static final int    RECALC_COOLDOWN  = 10;       // repath every 10 ticks

    private final DinosaurEntity self;
    private DinosaurEntity partner;
    private int life;                 // ticks since start
    private int nextRecalc;           // path recalc timer

    public MateEntityAI(DinosaurEntity self) {
        this.self = self;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.TARGET));
    }

    @Override
    public boolean canUse() {
        // server-side only
        if (self.level().isClientSide) return false;

        if (!self.level().getGameRules().getRule(GameRuleHandler.DINO_BREEDING).get()) return false;

        // basic preconditions
        if (self.isBusy() || self.isCarcass() || self.isSleeping()) return false;
        if (self.getAgePercentage() < 90) return false;
        if (self.getBreedCooldown() > 0) return false;
        if (self.isPregnant()) return false;

        // type we will search for (same concrete class)
        final Class<? extends DinosaurEntity> cls = self.getClass().asSubclass(DinosaurEntity.class);
        final AABB box = self.getBoundingBox().inflate(SEARCH_RANGE);

        Predicate<DinosaurEntity> candidate = d -> d != self
                && d.isAlive()
                && !d.isCarcass()
                && !d.isSleeping()
                && d.getClass() == cls
                && d.isMale() != self.isMale()
                && d.getAgePercentage() >= 90
                && d.getBreedCooldown() <= 0
                && !d.isPregnant();

        // NOTE: method returns List<? extends DinosaurEntity>, so keep wildcard
        List<? extends DinosaurEntity> list =
                self.level().getEntitiesOfClass(cls, box, candidate::test);

        partner = list.stream()
                .min(Comparator.comparingDouble(self::distanceToSqr))
                .orElse(null);

        return partner != null;
    }

    @Override
    public boolean canContinueToUse() {
        if (self.level().isClientSide) return false;
        if (partner == null || !partner.isAlive() || partner.isCarcass() || partner.isSleeping()) return false;
        if (self.isBusy() || self.isCarcass() || self.isSleeping()) return false;
        if (life >= MAX_TICKS) return false;

        return true;
    }

    @Override
    public void start() {
        life = 0;
        nextRecalc = 0;

        if (partner != null && partner.getBreedCooldown() <= 0) {
            // mutual pairing so entity logic can proceed (pregnancy/eggs handled in DinosaurEntity)
            self.breed(partner);
            partner.breed(self);
        }

        if (partner != null) {
            self.getNavigation().moveTo(partner, START_SPEED);
        }
    }

    @Override
    public void tick() {
        if (partner == null) return;

        life++;

        // repath periodically to keep up if partner moves
        if (--nextRecalc <= 0) {
            nextRecalc = RECALC_COOLDOWN;
            self.getNavigation().moveTo(partner, START_SPEED);
        }

        if (self.getBoundingBox().inflate(TOUCH_GROWTH).intersects(partner.getBoundingBox())) {
            self.setAnimation(EntityAnimation.MATING.get());
            self.getMetabolism().decreaseEnergy(1000);
            self.getNavigation().stop();


            stop();
        }
    }

    @Override
    public void stop() {
        self.getNavigation().stop();
        partner = null;
        life = 0;
        nextRecalc = 0;
    }
}
