package net.vit.jurassicreborn.common.entities.ai;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.vit.jurassicreborn.common.entities.DinosaurEntity;

import java.util.EnumSet;

public class DinosaurAttackMeleeEntityAI extends MeleeAttackGoal {

    protected final DinosaurEntity dinosaur;
    private final double attackSpeed; // local copy of the constructor's speedModifier

    // Repath controls
    private static final int REPTH_MIN_TICKS = 10; // 0.5s
    private static final int REPTH_MAX_TICKS = 20;
    private static final double REPATH_MOVE_DIST_SQR = 4.0; // target moved > 2 blocks

    private int repathTime = 0;
    private Vec3 lastTargetPos = Vec3.ZERO;

    public DinosaurAttackMeleeEntityAI(DinosaurEntity dinosaur, double speedModifier, boolean useLongMemory) {
        super(dinosaur, speedModifier, useLongMemory);
        this.dinosaur = dinosaur;
        this.attackSpeed = speedModifier; // keep our own copy
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    protected DinosaurEntity getAttacker() { return dinosaur; }

    @Override
    public boolean canUse() {
        LivingEntity target = this.dinosaur.getTarget();
        if (target == null || !target.isAlive()) {
            this.dinosaur.setTarget(null);
            return false;
        }
        return super.canUse();
    }

    @Override
    public boolean canContinueToUse() {
        LivingEntity target = this.dinosaur.getTarget();
        if (target == null || !target.isAlive()) {
            this.dinosaur.setTarget(null);
            return false;
        }
        return super.canContinueToUse();
    }

    @Override
    public void start() {
        super.start();
        repathTime = 0;
        LivingEntity t = dinosaur.getTarget();
        lastTargetPos = (t != null) ? t.position() : dinosaur.position();
        this.dinosaur.getNavigation().setMaxVisitedNodesMultiplier(1.0F);
    }

    @Override
    public void stop() {
        super.stop();
        this.dinosaur.getNavigation().stop();
    }

    @Override
    public void tick() {
        LivingEntity target = this.dinosaur.getTarget();
        if (target == null || !target.isAlive()) return;

        boolean waterNav = this.dinosaur.getNavigation() instanceof WaterBoundPathNavigation;
        this.dinosaur.getLookControl().setLookAt(target, 30.0F, this.dinosaur.getMaxHeadXRot());

        double reach = getCustomAttackReachSqr(target);
        double distSqr = this.dinosaur.distanceToSqr(target);
        if (distSqr <= reach) {
            this.dinosaur.getNavigation().stop();
        } else {
            if (--repathTime <= 0) {
                Vec3 cur = target.position();
                if (cur.distanceToSqr(lastTargetPos) > REPATH_MOVE_DIST_SQR || this.dinosaur.getRandom().nextInt(4) == 0) {
                    this.dinosaur.getNavigation().moveTo(target, this.attackSpeed);
                    lastTargetPos = cur;
                    repathTime = REPTH_MIN_TICKS + this.dinosaur.getRandom().nextInt(REPTH_MAX_TICKS - REPTH_MIN_TICKS + 1);
                }
            }
        }

        if (waterNav && this.dinosaur.getNavigation().isDone()) {
            this.dinosaur.getMoveControl().setWantedPosition(
                    target.getX(),
                    target.getY(target.getBbHeight() * 0.5),
                    target.getZ(),
                    this.attackSpeed
            );
        }

        super.tick();
    }

    protected double getCustomAttackReachSqr(LivingEntity target) {
        AABB myBox = this.dinosaur.getBoundingBox().inflate(0.25, 0.25, 0.25);
        if (myBox.intersects(target.getBoundingBox())) {
            return 0.25;
        }
        double grownWidth = this.dinosaur.getBbWidth() * 0.9 + target.getBbWidth() + 0.5;
        return grownWidth * grownWidth;
    }
}
