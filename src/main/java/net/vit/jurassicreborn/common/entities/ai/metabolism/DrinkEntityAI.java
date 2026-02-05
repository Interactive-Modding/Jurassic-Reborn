package net.vit.jurassicreborn.common.entities.ai.metabolism;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimation;
import net.vit.jurassicreborn.common.entities.DinosaurEntity;
import net.vit.jurassicreborn.common.entities.EntityUtils.MetabolismContainer;
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

    public DrinkEntityAI(DinosaurEntity dino) {
        this.dino = dino;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (dino == null || dino.isCarcass() || !dino.isAlive()) return false;
        MetabolismContainer meta = dino.getMetabolism();
        if (!meta.isThirsty()) return false;
        if (!dino.getNavigation().isDone()) return false;
        Level level = dino.level;
        BlockPos origin = dino.blockPosition();
        if (((dino.tickCount + dino.getId()) & 7) != 0) return false;
        if (cachedShore == null
                || dino.distanceToSqr(cachedShore.getX() + 0.5, cachedShore.getY() + 0.5, cachedShore.getZ() + 0.5) > 48 * 48
                || dino.tickCount - shoreCacheTick > 80) {
            cachedShore = AIUtils.findShore(level, origin);
            shoreCacheTick = dino.tickCount;
            for (int i = 0; cachedShore == null && i < 6; i++) {
                int dx = dino.getRandom().nextInt(33) - 16;
                int dz = dino.getRandom().nextInt(33) - 16;
                BlockPos sample = origin.offset(dx, 0, dz);
                if (!level.isLoaded(sample)) continue;
                BlockPos s = AIUtils.findShore(level, sample);
                if (s != null && level.isLoaded(s)) cachedShore = s;
            }
        }
        if (cachedShore == null || !level.isLoaded(cachedShore)) return false;
        Path p = dino.getNavigation().createPath(cachedShore, 0);
        if (p == null) return false;
        shore = cachedShore.immutable();
        path = p;
        dino.getNavigation().moveTo(p, meta.isDehydrated() ? 1.2D : 0.7D);
        int nodes = p.getNodeCount();
        giveUpTicks = Math.max(80, Math.min(20 * 20, nodes * 20));
        drinkCooldown = 0;
        return true;
    }

    @Override
    public void start() {}

    @Override
    public boolean canContinueToUse() {
        if (dino == null || dino.isCarcass() || !dino.isAlive()) return false;
        if (shore == null) return false;
        boolean nearShore = dino.blockPosition().distSqr(shore) <= 6;
        MetabolismContainer m = dino.getMetabolism();
        boolean thirsty = m.getWater() < m.getMaxWater() * 0.9;
        boolean enRoute = path != null && !path.isDone();
        return (enRoute || (nearShore && thirsty)) && giveUpTicks > 0;
    }

    @Override
    public void tick() {
        if (shore == null) return;
        Vec3 look = Vec3.atCenterOf(shore);
        dino.getLookControl().setLookAt(look.x, look.y, look.z, 30.0F, dino.getMaxHeadXRot());
        if (dino.blockPosition().distSqr(shore) <= 6) {
            dino.getNavigation().stop();
            dino.setDeltaMovement(Vec3.ZERO);
            dino.setYRot(dino.getYHeadRot());
            dino.setYBodyRot(dino.getYHeadRot());
            dino.yRotO = dino.getYRot();
            dino.yBodyRotO = dino.yBodyRot;
            dino.yHeadRotO = dino.getYHeadRot();
            if (!dino.level.isClientSide) {
                if (drinkCooldown-- <= 0) {
                    dino.setAnimation(EntityAnimation.DRINKING.get());
                    MetabolismContainer meta = dino.getMetabolism();
                    int add = Math.max(50, meta.getMaxWater() / 8);
                    meta.setWater(Math.min(meta.getWater() + add, meta.getMaxWater()));
                    drinkCooldown = 20;
                }
            }
        }
        if (--giveUpTicks <= 0) {
            stop();
        }
    }

    @Override
    public void stop() {
        if (dino != null) dino.getNavigation().stop();
        path = null;
        shore = null;
        drinkCooldown = 0;
        giveUpTicks = 0;
    }

    @Override
    public boolean isInterruptable() {
        return false;
    }
}
