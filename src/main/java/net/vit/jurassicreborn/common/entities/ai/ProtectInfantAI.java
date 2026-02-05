package net.vit.jurassicreborn.common.entities.ai;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.vit.jurassicreborn.common.entities.DinosaurEntity;

import java.util.EnumSet;
import java.util.List;

public class ProtectInfantAI<T extends DinosaurEntity> extends Goal {

    private static final double RANGE_XZ = 8.0D;
    private static final double RANGE_Y  = 3.0D;

    private final T adult;
    private final Class<T> dinoClass;
    private T infant; // cached youngster

    @SuppressWarnings("unchecked")
    public ProtectInfantAI(T adult) {
        this.adult = adult;
        this.dinoClass = (Class<T>) adult.getClass();
        this.setFlags(EnumSet.of(Flag.TARGET));
    }

    @Override
    public boolean canUse() {
        // Server-only; require a capable adult
        if (adult.level.isClientSide)             return false;
        if (adult.getAgePercentage() <= 75)       return false;
        if (adult.isCarcass() || adult.isSleeping()) return false;

        // Keep it lightweight: check every 10 ticks
        if ((adult.tickCount % 10) != 0)          return false;

        // If the adult already has a valid target, no need to pick up another
        LivingEntity current = adult.getTarget();
        if (current != null && current.isAlive()) return false;

        List<? extends T> nearby = adult.level.getEntitiesOfClass(
                dinoClass,
                adult.getBoundingBox().inflate(RANGE_XZ, RANGE_Y, RANGE_XZ),
                d -> d != adult
                        && d.isAlive()
                        && !d.isCarcass()
                        && !d.isSleeping()
                        && d.getAgePercentage() <= 50
                        && d.getTarget() != null
        );

        for (T baby : nearby) {
            LivingEntity attacker = baby.getTarget();
            if (attacker == null || !attacker.isAlive()) continue;

            // Ignore creative/spectator players
            if (attacker instanceof Player p && (p.isCreative() || p.isSpectator())) continue;

            this.infant = baby;
            return true;
        }

        this.infant = null;
        return false;
    }

    @Override
    public void start() {
        if (infant == null) return;

        LivingEntity attacker = infant.getTarget();
        if (attacker == null || !attacker.isAlive()) return;

        // Assign the threat directly
        adult.setTarget(attacker);
    }

    @Override
    public boolean canContinueToUse() {
        // One-shot goal; targeting logic continues via normal combat AI
        return false;
    }

    @Override
    public void stop() {
        infant = null;
    }
}
