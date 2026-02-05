package net.vit.jurassicreborn.common.entities.ai;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.vit.jurassicreborn.common.entities.DinosaurEntity;

import java.util.EnumSet;
import java.util.UUID;

public class DefendOwnerAI extends Goal {

    private static final double MAX_DISTANCE_SQR = 48.0D * 48.0D; // don’t chase very far

    private final DinosaurEntity dino;
    private Player owner;
    private LivingEntity attacker;
    private int lastHurtByTs;

    public DefendOwnerAI(DinosaurEntity dino) {
        this.dino = dino;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.TARGET));
    }

    @Override
    public boolean canUse() {
        if (dino.isCarcass() || !dino.isAlive() || dino.isMovementBlocked()) return false;
        if (dino.getAgePercentage() <= 50) return false;
        if (dino.getOrder() != DinosaurEntity.Order.FOLLOW) return false;

        UUID id = dino.getOwner();
        if (id == null) return false;

        owner = dino.level().getPlayerByUUID(id);
        if (owner == null || owner.isSpectator()) return false;

        LivingEntity hurtBy = owner.getLastHurtByMob();
        if (hurtBy == null || !hurtBy.isAlive() || hurtBy == dino) return false;

        attacker = hurtBy;
        lastHurtByTs = owner.getLastHurtByMobTimestamp();

        // don’t start if the fight is very far away
        return dino.distanceToSqr(attacker) <= MAX_DISTANCE_SQR;
    }

    @Override
    public void start() {
        dino.setTarget(attacker);
    }

    @Override
    public boolean canContinueToUse() {
        if (dino.isCarcass() || !dino.isAlive() || dino.isMovementBlocked()) return false;
        if (dino.getOrder() != DinosaurEntity.Order.FOLLOW) return false;
        if (owner == null || owner.isSpectator()) return false;
        return attacker != null && attacker.isAlive() &&
                dino.distanceToSqr(attacker) <= MAX_DISTANCE_SQR;
    }

    @Override
    public void tick() {
        // owner might be attacked by someone else now; switch to the newest attacker
        LivingEntity newAttacker = owner.getLastHurtByMob();
        int ts = owner.getLastHurtByMobTimestamp();
        if (newAttacker != null && newAttacker.isAlive() && newAttacker != dino && ts > lastHurtByTs) {
            attacker = newAttacker;
            lastHurtByTs = ts;
            dino.setTarget(attacker);
        } else if (dino.getTarget() != attacker) {
            dino.setTarget(attacker);
        }
    }

    @Override
    public void stop() {
        dino.setTarget(null);
        owner = null;
        attacker = null;
        lastHurtByTs = 0;
    }
}
