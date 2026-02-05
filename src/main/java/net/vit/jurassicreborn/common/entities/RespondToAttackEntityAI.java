package net.vit.jurassicreborn.common.entities;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

/**
 * When the dino already has an attack target and can see it, forward that
 * information to its herd logic so the whole group reacts.
 */
public class RespondToAttackEntityAI extends Goal {

    private final DinosaurEntity dino;
    private LivingEntity attacker;

    public RespondToAttackEntityAI(DinosaurEntity dino) {
        this.dino = dino;
        this.setFlags(EnumSet.noneOf(Flag.class));
    }

    /* ------------------------------------------------------------------ */
    /*  Goal                                                               */
    /* ------------------------------------------------------------------ */

    @Override
    public boolean canUse() {
        attacker = dino.getTarget();
        if (attacker == null) return false;

        boolean visible      = dino.hasLineOfSight(attacker);
        boolean deadOrCarcass = attacker.isDeadOrDying() ||
                attacker instanceof DinosaurEntity d &&
                        d.isCarcass();
        boolean creativePlayer = attacker instanceof Player p && p.isCreative();

        return visible && !deadOrCarcass && !creativePlayer;
    }

    @Override
    public void start() {
        dino.respondToAttack(attacker);
    }

    @Override
    public boolean canContinueToUse() {
        // one-shot – stop right after start()
        return false;
    }
}
