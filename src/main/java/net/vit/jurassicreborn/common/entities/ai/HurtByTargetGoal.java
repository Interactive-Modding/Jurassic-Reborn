package net.vit.jurassicreborn.common.entities.ai;

import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.entity.EntitySelector;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;

public class HurtByTargetGoal extends TargetGoal {
    private static final TargetingConditions HURT_BY_TARGETING =
            TargetingConditions.forCombat().ignoreLineOfSight().ignoreInvisibilityTesting();
    private static final int ALERT_RANGE_Y = 10;

    private boolean alertSameType;
    /** Previous revenge timer value */
    private int timestamp;
    private final Class<?>[] toIgnoreDamage;
    @Nullable private Class<?>[] toIgnoreAlert;

    public HurtByTargetGoal(PathfinderMob mob, Class<?>... toIgnoreDamage) {
        super(mob, true);
        this.toIgnoreDamage = toIgnoreDamage;
        this.setFlags(EnumSet.of(Goal.Flag.TARGET));
    }

    @Override
    public boolean canUse() {
        int last = this.mob.getLastHurtByMobTimestamp();
        LivingEntity attacker = this.mob.getLastHurtByMob();

        if (last == this.timestamp || attacker == null) return false;

        // Honor universal anger (don’t retaliate against players when enabled)
        if (attacker.getType() == EntityType.PLAYER &&
                this.mob.level().getGameRules().getBoolean(GameRules.RULE_UNIVERSAL_ANGER)) {
            return false;
        }

        // If attacker matches ANY ignored class, skip
        for (Class<?> c : this.toIgnoreDamage) {
            if (c.isAssignableFrom(attacker.getClass())) {
                return false;
            }
        }

        return this.canAttack(attacker, HURT_BY_TARGETING);
    }

    public HurtByTargetGoal setAlertOthers(Class<?>... reinforcementTypes) {
        this.alertSameType = true;
        this.toIgnoreAlert = reinforcementTypes;
        return this;
    }

    @Override
    public void start() {
        this.mob.setTarget(this.mob.getLastHurtByMob());
        this.targetMob = this.mob.getTarget();
        this.timestamp = this.mob.getLastHurtByMobTimestamp();
        this.unseenMemoryTicks = 300;
        if (this.alertSameType) {
            this.alertOthers();
        }
        super.start();
    }

    protected void alertOthers() {
        double follow = this.getFollowDistance();
        AABB box = AABB.unitCubeFromLowerCorner(this.mob.position()).inflate(follow, ALERT_RANGE_Y, follow);

        List<? extends Mob> allies = this.mob.level().getEntitiesOfClass(
                this.mob.getClass(), box, EntitySelector.NO_SPECTATORS);

        LivingEntity aggressor = this.mob.getLastHurtByMob();
        if (aggressor == null) return;

        for (Mob ally : allies) {
            if (ally == this.mob) continue;
            if (ally.getTarget() != null) continue;
            if (ally.isAlliedTo(aggressor)) continue;

            // If both are tamables, only alert litter-mates with same owner
            if (this.mob instanceof TamableAnimal tmA && ally instanceof TamableAnimal tmB) {
                if (tmA.getOwner() != tmB.getOwner()) continue;
            }

            // Skip allies whose class is in the "do not alert" list
            if (this.toIgnoreAlert != null) {
                boolean ignored = false;
                for (Class<?> c : this.toIgnoreAlert) {
                    if (ally.getClass() == c) {
                        ignored = true;
                        break;
                    }
                }
                if (ignored) continue;
            }

            this.alertOther(ally, aggressor);
        }
    }

    protected void alertOther(Mob mob, LivingEntity target) {
        mob.setTarget(target);
    }
}
