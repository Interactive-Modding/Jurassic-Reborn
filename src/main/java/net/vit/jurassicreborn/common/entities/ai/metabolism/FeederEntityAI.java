package net.vit.jurassicreborn.common.entities.ai.metabolism;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.vit.jurassicreborn.common.blocks.ModBlocks;
import net.vit.jurassicreborn.common.blocks.entities.feeder.FeederBlock;
import net.vit.jurassicreborn.common.blocks.entities.feeder.FeederBlockEntity;
import net.vit.jurassicreborn.common.entities.DinosaurEntity;
import net.vit.jurassicreborn.common.entities.FlyingDinosaurEntity;
import net.vit.jurassicreborn.common.util.GameRuleHandler;

import java.util.EnumSet;
import java.util.List;

public class FeederEntityAI extends Goal {
    private final DinosaurEntity dino;
    private Path path;
    private BlockPos feederPos;
    private Vec3 feederTarget;
    private int ticksTrying;
    private boolean waitingForFood;
    private int foodWaitTicks;
    private ItemEntity targetFoodItem;

    public FeederEntityAI(DinosaurEntity dino) {
        this.dino = dino;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (dino == null || dino.isRemoved() || dino.isCarcass() || dino.isMovementBlocked()) return false;
        if (!dino.level().getGameRules().getBoolean(GameRuleHandler.DINO_METABOLISM)) return false;
        int intervalMask = dino.isMarineCreature() ? 7 : 15;
        if ((dino.tickCount & intervalMask) != 0) return false;
        if (!dino.getMetabolism().isHungry()) return false;

        BlockPos found = dino.getClosestFeeder();
        if (found == null) return false;

        Level level = dino.level();
        if (level == null || !level.hasChunkAt(found)) return false;

        BlockState state = level.getBlockState(found);
        if (state.getBlock() != ModBlocks.FEEDER.get()) return false;

        BlockEntity be = level.getBlockEntity(found);
        if (!(be instanceof FeederBlockEntity feeder) || !feeder.isStockedFor(dino)) {
            return false;
        }

        Vec3 target = computeTarget(found, state);
        Path p;
        try {
            p = dino.getNavigation().createPath(new BlockPos((int)target.x, (int)target.y, (int)target.z), 0);
        } catch (Throwable t) {
            p = null;
        }
        boolean canDirectMove = dino.isMarineCreature() || dino instanceof FlyingDinosaurEntity;
        if (p == null && !canDirectMove) return false;

        this.feederPos = found.immutable();
        this.path = p;
        this.feederTarget = target;
        this.ticksTrying = 0;
        this.waitingForFood = false;
        this.foodWaitTicks = 0;
        this.targetFoodItem = null;
        return true;
    }

    @Override
    public void start() {
        if (this.path != null) {
            dino.getNavigation().moveTo(this.path, 1.0D);
        } else if (this.feederTarget != null) {
            if (shouldTakeOffForTarget()) {
                ((FlyingDinosaurEntity) dino).startTakeOff();
            }
            dino.getNavigation().moveTo(feederTarget.x, feederTarget.y, feederTarget.z, 1.0D);
        }
    }

    @Override
    public boolean canContinueToUse() {
        if (dino == null || feederPos == null) return false;
        if (dino.isCarcass() || dino.isMovementBlocked()) return false;
        if (!dino.getMetabolism().isHungry()) return false;

        // Continue if waiting for food or chasing food item
        if (waitingForFood && foodWaitTicks < 120) return true;
        if (targetFoodItem != null && !targetFoodItem.isRemoved()) return true;

        BlockState state = dino.level().getBlockState(feederPos);
        if (state.getBlock() != ModBlocks.FEEDER.get()) return false;

        this.feederTarget = computeTarget(feederPos, state);
        boolean directMove = (this.path == null) && (dino.isMarineCreature() || dino instanceof FlyingDinosaurEntity);
        if (directMove && this.feederTarget != null) {
            double dist = dino.position().distanceTo(feederTarget);
            double reach = Math.max(2.0D, dino.getBbWidth() * 3.0D);
            return dist > reach && ticksTrying < 300;
        }
        return !dino.getNavigation().isDone() && ticksTrying < 300;
    }

    @Override
    public void tick() {
        if (feederPos == null) return;

        // Priority 1: If we have a target food item, go eat it
        if (targetFoodItem != null && !targetFoodItem.isRemoved()) {
            if (tryEatSpecificItem(targetFoodItem)) {
                stop();
                return;
            }
            // Navigate to the food item
            dino.getNavigation().moveTo(targetFoodItem.getX(), targetFoodItem.getY(), targetFoodItem.getZ(), 1.2D);
            ticksTrying++;
            if (ticksTrying > 300) {
                stop();
            }
            return;
        }

        // Priority 2: If waiting for food, scan for it
        if (waitingForFood) {
            foodWaitTicks++;
            ItemEntity foundFood = scanForNearbyFood();
            if (foundFood != null) {
                this.targetFoodItem = foundFood;
                this.waitingForFood = false;
                return;
            }
            if (foodWaitTicks >= 120) {
                stop();
                return;
            }
            return;
        }

        // Priority 3: Navigate to feeder
        BlockState state = dino.level().getBlockState(feederPos);
        if (state.getBlock() != ModBlocks.FEEDER.get()) {
            stop();
            return;
        }

        this.feederTarget = computeTarget(feederPos, state);

        // Retry pathfinding periodically
        if ((++ticksTrying % 40) == 0 && (this.path == null || dino.getNavigation().isDone())) {
            BlockPos targetPos = new BlockPos((int)feederTarget.x, (int)feederTarget.y, (int)feederTarget.z);
            Path retry = dino.getNavigation().createPath(targetPos, 0);
            if (retry != null) {
                this.path = retry;
                dino.getNavigation().moveTo(retry, 1.0D);
            } else if (dino.isMarineCreature() || dino instanceof FlyingDinosaurEntity) {
                // Direct movement for creatures that can fly/swim
                if (shouldTakeOffForTarget()) {
                    ((FlyingDinosaurEntity) dino).startTakeOff();
                }
                dino.getNavigation().moveTo(feederTarget.x, feederTarget.y, feederTarget.z, 1.0D);
            }
        }

        // Check if we reached the feeder
        if (!dino.level().isClientSide) {
            Vec3 target = (this.feederTarget != null) ? this.feederTarget : Vec3.atCenterOf(feederPos);
            double dist = dino.position().distanceTo(target);
            double reach = Math.max(2.0D, dino.getBbWidth() * 3.0D);

            if (dist <= reach) {
                BlockEntity be = dino.level().getBlockEntity(feederPos);
                if (be instanceof FeederBlockEntity feeder) {
                    // Check if feeder is already feeding this dino
                    DinosaurEntity currentFeeding = feeder.getFeeding();
                    if (currentFeeding == null || currentFeeding == dino) {
                        feeder.setOpen(true);
                        feeder.setFeeding(dino);
                        waitingForFood = true;
                        foodWaitTicks = 0;
                        dino.getNavigation().stop();
                    } else {
                        // Feeder is busy with another dino, wait a bit
                        if (ticksTrying > 200) {
                            stop();
                        }
                    }
                }
            }
        }
    }

    @Override
    public void stop() {
        if (dino != null) {
            dino.getNavigation().stop();
        }
        this.path = null;
        this.feederPos = null;
        this.feederTarget = null;
        this.ticksTrying = 0;
        this.waitingForFood = false;
        this.foodWaitTicks = 0;
        this.targetFoodItem = null;
    }

    private ItemEntity scanForNearbyFood() {
        if (dino.level().isClientSide || feederPos == null) return null;

        // Larger search area for marine creatures
        double searchRadius = dino.isMarineCreature() ? 8.0D : 5.0D;
        AABB searchBox = new AABB(feederPos).inflate(searchRadius);
        List<ItemEntity> items = dino.level().getEntitiesOfClass(ItemEntity.class, searchBox);

        ItemEntity closest = null;
        double closestDist = Double.MAX_VALUE;

        for (ItemEntity itemEntity : items) {
            if (itemEntity.isRemoved() || itemEntity.getItem().isEmpty()) continue;

            // Check if dino can eat this item
            if (net.vit.jurassicreborn.common.items.Food.FoodHelper.isEdible(
                    dino, dino.getDinosaur().getDiet(), itemEntity.getItem().getItem())) {

                double dist = dino.position().distanceTo(itemEntity.position());
                if (dist < closestDist) {
                    closestDist = dist;
                    closest = itemEntity;
                }
            }
        }

        return closest;
    }

    private boolean tryEatSpecificItem(ItemEntity itemEntity) {
        if (dino.level().isClientSide || itemEntity == null || itemEntity.isRemoved()) return false;

        double distToItem = dino.position().distanceTo(itemEntity.position());
        double eatReach = Math.max(2.0D, dino.getBbWidth() * 2.0D);

        if (distToItem <= eatReach) {
            // Check one more time that it's edible
            if (net.vit.jurassicreborn.common.items.Food.FoodHelper.isEdible(
                    dino, dino.getDinosaur().getDiet(), itemEntity.getItem().getItem())) {

                // Eat the item
                int foodValue = net.vit.jurassicreborn.common.items.Food.FoodHelper.getHealAmount(
                        itemEntity.getItem().getItem());
                dino.getMetabolism().eat(foodValue);
                net.vit.jurassicreborn.common.items.Food.FoodHelper.applyEatEffects(
                        dino, itemEntity.getItem().getItem());

                itemEntity.getItem().shrink(1);
                if (itemEntity.getItem().isEmpty()) {
                    itemEntity.discard();
                }
                return true;
            }
        }
        return false;
    }

    private Vec3 computeTarget(BlockPos pos, BlockState state) {
        Vec3 center = Vec3.atCenterOf(pos);
        if (!state.hasProperty(FeederBlock.FACING)) {
            return center;
        }
        Direction facing = state.getValue(FeederBlock.FACING);
        double forward = 1.2D; // Increased from 0.6D
        double vertical = 0.0D;

        if (facing.getAxis().isHorizontal()) {
            if (dino.isMarineCreature()) {
                vertical = 0.1D;
                forward = 1.5D; // More distance for marine creatures
            } else {
                vertical = 0.3D;
            }
        } else if (facing == Direction.UP) {
            vertical = 1.0D; // Increased from 0.6D
        } else if (facing == Direction.DOWN) {
            vertical = -0.5D;
        }
        return center.add(
                facing.getStepX() * forward,
                facing.getStepY() * forward + vertical,
                facing.getStepZ() * forward
        );
    }

    private boolean shouldTakeOffForTarget() {
        if (!(dino instanceof FlyingDinosaurEntity) || this.feederTarget == null) return false;
        boolean needsAltitude = feederTarget.y - dino.getY() > 0.75D;
        return needsAltitude || this.path == null;
    }
}