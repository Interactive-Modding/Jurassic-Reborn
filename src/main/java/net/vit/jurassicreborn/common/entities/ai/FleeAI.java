package net.vit.jurassicreborn.common.entities.ai;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.AABB;
import net.vit.jurassicreborn.common.entities.DinosaurEntity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FleeAI extends Goal {
    private final DinosaurEntity dinosaur;
    private final List<LivingEntity> attackers = new ArrayList<>();

    public FleeAI(DinosaurEntity dinosaur) {
        this.dinosaur = dinosaur;
        // No MOVE/LOOK flags; this goal only toggles herd state/targets.
    }

    @Override
    public boolean canUse() {
        // Server-side only
        if (dinosaur.level.isClientSide) return false;

        // Run every 5 ticks; also skip if unable to act
        if ((dinosaur.tickCount % 5) != 0) return false;
        if (!dinosaur.isAlive() || dinosaur.isCarcass() || dinosaur.isMovementBlocked()) return false;

        attackers.clear();

        // Search a tall column around the dino
        AABB searchBox = dinosaur.getBoundingBox().inflate(10.0, 40.0, 10.0);
        List<DinosaurEntity> nearby = dinosaur.level.getEntitiesOfClass(DinosaurEntity.class, searchBox);

        for (DinosaurEntity other : nearby) {
            if (other == dinosaur || other.isCarcass()) continue;
            if (!dinosaur.hasLineOfSight(other)) continue;
            if (!dinosaur.closerThan(other, 20.0)) continue;
            // If 'other' has any attack target class that matches 'this' dino’s class, treat it as a threat
            for (Class<? extends LivingEntity> clazz : other.getAttackTargets()) {
                if (clazz.isAssignableFrom(dinosaur.getClass())) {
                    attackers.add(other);

                    // If they don’t already have a live target, point them at this dino
                    LivingEntity tgt = other.getTarget();
                    if (tgt == null || !tgt.isAlive()) {
                        other.setTarget(dinosaur);
                    }

                    // If they have a herd, mark our herd (or us) as enemies for theirs
                    if (other.herd != null) {
                        if (dinosaur.herd != null) {
                            other.herd.enemies.addAll(dinosaur.herd.members);
                        } else {
                            other.herd.enemies.add(dinosaur);
                        }
                    }
                    break; // one match is enough
                }
            }
        }
        return !attackers.isEmpty();
    }

    @Override
    public boolean canContinueToUse() {
        // One-shot goal: we just toggle herd state then end.
        return false;
    }

    @Override
    public void start() {
        if (dinosaur.herd == null || attackers.isEmpty()) return;

        // Deduplicate and register enemies; flip the herd to fleeing
        Set<LivingEntity> uniq = new HashSet<>(attackers);
        for (LivingEntity a : uniq) {
            if (!dinosaur.herd.enemies.contains(a)) {
                dinosaur.herd.enemies.add(a);
            }
        }
        dinosaur.herd.fleeing = true;
    }
}
