package net.vit.jurassicreborn.common.entities.ai.metabolism;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimation;
import net.vit.jurassicreborn.common.entities.DinosaurEntity;
import net.vit.jurassicreborn.common.entities.EntityUtils.MetabolismContainer;
import net.vit.jurassicreborn.common.entities.ai.util.OnionTraverser;
import net.vit.jurassicreborn.common.items.Food.FoodHelper;

import java.util.EnumSet;
import java.util.Iterator;

public class GrazeEntityAI extends Goal {

    private static final int LOOK_RADIUS    = 16;
    private static final int EAT_RADIUS_SQ  = 36;  // 6 * 6
    private static final int GIVE_UP_TICKS  = 400;

    private final DinosaurEntity dino;

    private Iterator<BlockPos> searchIter = null;
    private BlockPos           target;      // block to eat
    private BlockPos           moveTarget;  // solid pos to stand on near target
    private int                giveUpCounter;

    public GrazeEntityAI(DinosaurEntity dino) {
        this.dino = dino;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (dino.isDeadOrDying() || dino.isCarcass()) return false;

        // NEW: don’t graze if we already have a combat target
        if (dino.getTarget() != null) return false;

        if (!dino.getMetabolism().isHungry()) return false;

        // Prefer feeder if one exists and we're not starving
        if (!dino.getMetabolism().isStarving() && dino.getClosestFeeder() != null) return false;

        // (Re)start a search if we don't have a move target yet
        if (moveTarget == null) {
            ensureSearchIterator();
            advanceSearch(64);
        }

        if (moveTarget != null) {
            double speed = dino.getMetabolism().isStarving() ? 1.2 : 0.7;
            dino.getNavigation().moveTo(
                    moveTarget.getX() + 0.5,
                    moveTarget.getY(),
                    moveTarget.getZ() + 0.5,
                    speed
            );
            giveUpCounter = GIVE_UP_TICKS;
            return true;
        }
        return false;
    }

    @Override
    public boolean canContinueToUse() {
        // stop if the target block is gone OR we enter combat
        if (dino.getTarget() != null) { abort(); return false; }
        if (target != null && dino.level.isEmptyBlock(target)) { abort(); return false; }
        return target != null;
    }

    @Override
    public void stop() {
        abort();
    }

    @Override
    public void tick() {
        if (target == null) return;

        // stop if combat begins mid-graze
        if (dino.getTarget() != null) { abort(); return; }

        Vec3 eye = dino.getEyePosition(1.0F);
        Vec3 tgt = Vec3.atCenterOf(target);

        if (eye.distanceToSqr(tgt) <= EAT_RADIUS_SQ) {
            dino.getNavigation().stop();
            dino.getLookControl().setLookAt(tgt.x, tgt.y, tgt.z, 30F, dino.getMaxHeadXRot());

            dino.setAnimation(EntityAnimation.EATING.get());

            BlockState state = dino.level.getBlockState(target);
            Item item = state.getBlock().asItem();
            if (item != Items.AIR) {
                dino.level.destroyBlock(target, false);

                MetabolismContainer meta = dino.getMetabolism();
                meta.eat(FoodHelper.getHealAmount(item));
                FoodHelper.applyEatEffects(dino, item);
                dino.heal(10F);
            }

            abort();
            return;
        }

        if (moveTarget == null && searchIter != null) {
            advanceSearch(32);
        }

        if (--giveUpCounter <= 0) {
            abort();
        }
    }

    private void abort() {
        dino.getNavigation().stop();
        target      = null;
        moveTarget  = null;
        searchIter  = null;
        dino.setAnimation(EntityAnimation.IDLE.get());
    }

    private void ensureSearchIterator() {
        if (searchIter == null) {
            Vec3 eye = dino.getEyePosition(1.0F);
            BlockPos origin = new BlockPos(eye.x, eye.y, eye.z);
            searchIter = new OnionTraverser(origin, LOOK_RADIUS).iterator();
        }
    }

    private void advanceSearch(int maxSteps) {
        if (searchIter == null) return;

        for (int i = 0; i < maxSteps && searchIter.hasNext(); i++) {
            BlockPos pos = searchIter.next();
            if (!dino.level.isLoaded(pos)) continue;

            BlockState state = dino.level.getBlockState(pos);
            if (state.isAir() || state.is(BlockTags.LEAVES) || state.getBlock() instanceof LeavesBlock) continue;

            Item item = state.getBlock().asItem();
            if (item == net.minecraft.world.item.Items.AIR) continue;

            if (FoodHelper.isEdible(dino, dino.getDinosaur().getDiet(), item)) {
                BlockPos stand = lowestSolidBelow(pos);
                if (stand != null) {
                    target     = pos;
                    moveTarget = stand;
                    return;
                }
            }
        }

        if (!searchIter.hasNext() && moveTarget == null) {
            searchIter = null;
        }
    }

    private BlockPos lowestSolidBelow(BlockPos pos) {
        BlockPos p = pos;
        for (int i = 0; i < 16; i++) {
            BlockState s = dino.level.getBlockState(p);
            if (!(s.getBlock() instanceof LeavesBlock) && !s.isAir()) break;
            p = p.below();
            if (p.getY() <= dino.level.getMinBuildHeight()) break;
        }
        BlockState ground = dino.level.getBlockState(p);
        if (ground.isAir() || ground.getBlock() instanceof LeavesBlock) return null;
        return p;
    }
}
