package net.vit.jurassicreborn.common.entities.ai;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.vit.jurassicreborn.common.entities.DinosaurEntity;

import java.util.EnumSet;
import java.util.UUID;

public class AssistOwnerAI extends Goal {

    private static final double MAX_DISTANCE_SQR = 48.0D * 48.0D; // stop if too far

    private final DinosaurEntity dino;
    private Player owner;
    private LivingEntity target;

    // track timestamps so we only react to recent events
    private int lastAttackedTs;    // owner attacked something
    private int lastHurtByTs;      // owner was hurt by something

    public AssistOwnerAI(DinosaurEntity dino) {
        this.dino = dino;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.TARGET));
    }

    @Override
    public boolean canUse() {
        if (dino.isCarcass() || !dino.isAlive() || dino.isMovementBlocked()) return false;
        if (dino.getAgePercentage() <= 50) return false; // too young
        if (dino.getOrder() != DinosaurEntity.Order.FOLLOW) return false;

        UUID id = dino.getOwner();
        if (id == null) return false;

        owner = dino.level.getPlayerByUUID(id);
        if (owner == null || owner.isSpectator()) return false;

        // prefer defending owner over helping attack, choose the most recent event
        LivingEntity hurtBy = owner.getLastHurtByMob();
        LivingEntity attacked = owner.getLastHurtMob();
        int hurtByTs = owner.getLastHurtByMobTimestamp();
        int attackedTs = owner.getLastHurtMobTimestamp();

        LivingEntity pick = null;
        int pickTs = -1;

        if (hurtBy != null && hurtBy != dino && hurtBy.isAlive()) {
            pick = hurtBy;
            pickTs = hurtByTs;
        }
        if (attacked != null && attacked != dino && attacked.isAlive()
                && attackedTs > pickTs) {
            pick = attacked;
            pickTs = attackedTs;
        }

        if (pick == null) return false;

        // cache
        target = pick;
        lastHurtByTs = hurtByTs;
        lastAttackedTs = attackedTs;

        // don’t chase super-distant fights
        if (dino.distanceToSqr(target) > MAX_DISTANCE_SQR) return false;

        return true;
    }

    @Override
    public void start() {
        dino.setTarget(target);
    }

    @Override
    public boolean canContinueToUse() {
        if (dino.isCarcass() || !dino.isAlive() || dino.isMovementBlocked()) return false;
        if (dino.getOrder() != DinosaurEntity.Order.FOLLOW) return false;
        if (owner == null || owner.isSpectator()) return false;

        // target must be valid and not too far
        return target != null && target.isAlive() && dino.distanceToSqr(target) <= MAX_DISTANCE_SQR;
    }

    @Override
    public void tick() {
        // keep helping the newest relevant entity (attack or defend)
        LivingEntity newHurtBy = owner.getLastHurtByMob();
        LivingEntity newAttacked = owner.getLastHurtMob();
        int newHurtByTs = owner.getLastHurtByMobTimestamp();
        int newAttackedTs = owner.getLastHurtMobTimestamp();

        LivingEntity best = target;
        int bestTs = Math.max(lastHurtByTs, lastAttackedTs);

        if (newHurtBy != null && newHurtBy.isAlive() && newHurtBy != dino && newHurtByTs > bestTs) {
            best = newHurtBy;
            bestTs = newHurtByTs;
        }
        if (newAttacked != null && newAttacked.isAlive() && newAttacked != dino && newAttackedTs > bestTs) {
            best = newAttacked;
            bestTs = newAttackedTs;
        }

        if (best != target) {
            target = best;
            dino.setTarget(target);
            lastHurtByTs = newHurtByTs;
            lastAttackedTs = newAttackedTs;
        } else if (dino.getTarget() != target) {
            dino.setTarget(target); // keep synced if something cleared it
        }
    }

    @Override
    public void stop() {
        dino.setTarget(null);
        owner = null;
        target = null;
        lastAttackedTs = 0;
        lastHurtByTs = 0;
    }
}
