package net.vit.jurassicreborn.common.entities.ai.metabolism;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimation;
import net.vit.jurassicreborn.common.entities.DinosaurEntity;
import net.vit.jurassicreborn.common.entities.EntityUtils.MetabolismContainer;
import net.vit.jurassicreborn.common.entities.FlyingDinosaurEntity;
import net.vit.jurassicreborn.common.entities.ai.util.AIUtils;

import java.util.EnumSet;

public class DrinkEntityAI extends Goal {
    private final DinosaurEntity dino;
    private Path path;
    private BlockPos shore;
    private int giveUpTicks;
    private int drinkCooldown;
    private BlockPos cachedShore;
    private int shoreCacheTick;
    private Vec3 flyerLandingTarget;

    public DrinkEntityAI(DinosaurEntity dino) {
        this.dino = dino;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (this.dino == null || this.dino.isCarcass() || !this.dino.isAlive()) {
            return false;
        }

        MetabolismContainer meta = this.dino.getMetabolism();
        if (!meta.isThirsty()) {
            return false;
        }

        Level level = this.dino.level();
        BlockPos origin = this.dino.blockPosition();

        if (((this.dino.tickCount + this.dino.getId()) & 7) != 0) {
            return false;
        }

        if (this.cachedShore == null
                || this.dino.distanceToSqr(Vec3.atCenterOf(this.cachedShore)) > 48 * 48
                || this.dino.tickCount - this.shoreCacheTick > 80) {
            this.cachedShore = AIUtils.findShore(level, origin);
            this.shoreCacheTick = this.dino.tickCount;
        }

        if (this.cachedShore == null || !level.isLoaded(this.cachedShore)) {
            return false;
        }

        this.shore = this.cachedShore.immutable();
        this.path = null;
        this.drinkCooldown = 0;

        if (this.dino instanceof FlyingDinosaurEntity) {
            this.flyerLandingTarget = this.findLandingTarget(this.shore);
            if (this.flyerLandingTarget == null) {
                return false;
            }

            this.giveUpTicks = 200;
            return true;
        }

        Path p = this.dino.getNavigation().createPath(this.cachedShore, 0);
        if (p == null) {
            return false;
        }

        this.path = p;
        this.dino.getNavigation().moveTo(p, meta.isDehydrated() ? 1.2D : 0.7D);

        int nodes = p.getNodeCount();
        this.giveUpTicks = Math.max(80, Math.min(400, nodes * 20));
        this.flyerLandingTarget = null;

        return true;
    }

    @Override
    public void start() {
        if (this.dino instanceof FlyingDinosaurEntity flyer && this.flyerLandingTarget != null) {
            flyer.shouldLand = true;
            flyer.getMoveControl().setWantedPosition(
                    this.flyerLandingTarget.x,
                    this.flyerLandingTarget.y,
                    this.flyerLandingTarget.z,
                    1.0D
            );
        }
    }

    @Override
    public boolean canContinueToUse() {
        if (this.dino == null || this.dino.isCarcass() || !this.dino.isAlive()) {
            return false;
        }
        if (this.shore == null) {
            return false;
        }

        MetabolismContainer m = this.dino.getMetabolism();
        boolean thirsty = m.getWater() < m.getMaxWater() * 0.9D;
        if (!thirsty || this.giveUpTicks <= 0) {
            return false;
        }

        if (this.dino instanceof FlyingDinosaurEntity) {
            return this.flyerLandingTarget != null;
        }

        boolean nearShore = this.dino.blockPosition().distSqr(this.shore) <= 6;
        boolean enRoute = this.path != null && !this.path.isDone();
        return enRoute || nearShore;
    }

    @Override
    public void tick() {
        if (this.shore == null) {
            return;
        }

        Vec3 shoreTarget = Vec3.atCenterOf(this.shore);

        this.dino.getLookControl().setLookAt(
                shoreTarget.x,
                shoreTarget.y,
                shoreTarget.z,
                30.0F,
                this.dino.getMaxHeadXRot()
        );

        if (this.dino instanceof FlyingDinosaurEntity flyer) {
            if (this.flyerLandingTarget == null) {
                this.flyerLandingTarget = this.findLandingTarget(this.shore);
                if (this.flyerLandingTarget == null) {
                    this.stop();
                    return;
                }
            }

            flyer.shouldLand = true;

            if (!flyer.isTouchingGround()
                    || flyer.position().distanceToSqr(this.flyerLandingTarget) > 1.25D) {
                flyer.getMoveControl().setWantedPosition(
                        this.flyerLandingTarget.x,
                        this.flyerLandingTarget.y,
                        this.flyerLandingTarget.z,
                        1.0D
                );
            }

            boolean nearShore = flyer.blockPosition().distSqr(this.shore) <= 6;
            if (nearShore && flyer.isTouchingGround()) {
                flyer.setDeltaMovement(
                        flyer.getDeltaMovement().x * 0.2D,
                        0.0D,
                        flyer.getDeltaMovement().z * 0.2D
                );

                if (!this.dino.level().isClientSide && this.drinkCooldown-- <= 0) {
                    this.dino.setAnimation(EntityAnimation.DRINKING.get());

                    MetabolismContainer meta = this.dino.getMetabolism();
                    int add = Math.max(50, meta.getMaxWater() / 8);
                    meta.setWater(Math.min(meta.getWater() + add, meta.getMaxWater()));

                    this.drinkCooldown = 20;
                }
            }

            if (--this.giveUpTicks <= 0) {
                this.stop();
            }
            return;
        }

        if (this.dino.blockPosition().distSqr(this.shore) <= 6) {
            this.dino.getNavigation().stop();
            this.dino.setDeltaMovement(Vec3.ZERO);

            if (!this.dino.level().isClientSide && this.drinkCooldown-- <= 0) {
                this.dino.setAnimation(EntityAnimation.DRINKING.get());
                MetabolismContainer meta = this.dino.getMetabolism();
                int add = Math.max(50, meta.getMaxWater() / 8);
                meta.setWater(Math.min(meta.getWater() + add, meta.getMaxWater()));
                this.drinkCooldown = 20;
            }
        }

        if (--this.giveUpTicks <= 0) {
            this.stop();
        }
    }

    @Override
    public void stop() {
        if (this.dino != null) {
            this.dino.getNavigation().stop();

            if (this.dino instanceof FlyingDinosaurEntity flyer) {
                flyer.shouldLand = false;
            }
        }

        this.path = null;
        this.shore = null;
        this.flyerLandingTarget = null;
        this.drinkCooldown = 0;
        this.giveUpTicks = 0;
    }

    @Override
    public boolean isInterruptable() {
        return false;
    }

    private Vec3 findLandingTarget(BlockPos shorePos) {
        if (shorePos == null || this.dino == null || this.dino.level() == null) {
            return null;
        }

        Level level = this.dino.level();
        BlockPos.MutableBlockPos probe = shorePos.mutable();

        while (probe.getY() > level.getMinBuildHeight() && level.getBlockState(probe).isAir()) {
            probe.move(0, -1, 0);
        }

        if (level.getBlockState(probe).isAir()) {
            return null;
        }

        return new Vec3(
                probe.getX() + 0.5D,
                probe.getY() + 0.2D,
                probe.getZ() + 0.5D
        );
    }
}