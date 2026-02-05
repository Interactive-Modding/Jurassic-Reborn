package net.vit.jurassicreborn.common.entities.ai;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimation;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.MicroraptorEntity;

import java.util.EnumSet;

public class RaptorClimbTreeAI extends Goal {
    private static final int CLIMB_INTERVAL = 1200;
    private static final int MAX_TREE_HEIGHT = 14;

    private final MicroraptorEntity entity;
    private final double movementSpeed;
    private final Level level;

    private Path path;

    private BlockPos targetTrunk;
    private Direction approachSide;

    private double targetX;
    private double targetY;
    private double targetZ;

    private boolean gliding;
    private boolean active;
    private boolean reachedTarget;

    private int lastActive = -CLIMB_INTERVAL;

    // glide steering
    private Vec3 glideTarget;

    public RaptorClimbTreeAI(MicroraptorEntity entity, double speed) {
        this.entity = entity;
        this.movementSpeed = speed;
        this.level = entity.level;
        this.setFlags(EnumSet.noneOf(Flag.class));
    }

    @Override
    public boolean canUse() {
        if (this.active || (this.entity.tickCount - this.lastActive) < CLIMB_INTERVAL) return false;
        if (this.entity.getRandom().nextFloat() > 0.12f) return false;

        BlockPos.MutableBlockPos base = new BlockPos.MutableBlockPos()
                .set(this.entity.getX(), this.entity.getY(), this.entity.getZ());
        RandomSource random = this.entity.getRandom();

        for (int i = 0; i < 20; ++i) {
            BlockPos target = base.offset(random.nextInt(14) - 7, -5, random.nextInt(14) - 7);

            for (int iteration = 0; iteration <= 15; iteration++) {
                target = target.above();
                if (!level.hasChunkAt(target)) return false;

                BlockState state = this.level.getBlockState(target);
                if (isLeaves(state) || isWood(state)) {
                    for (Direction dir : Direction.Plane.HORIZONTAL) {
                        BlockPos offsetTarget = target.relative(dir);

                        if (!this.level.getBlockState(offsetTarget).isFaceSturdy(this.level, offsetTarget, Direction.DOWN)) {
                            boolean canTravel = true;
                            boolean woodFound = false;

                            for (int h = 0; h < MAX_TREE_HEIGHT; h++) {
                                BlockPos trunkPos = target.above(h);
                                BlockPos climbPos = offsetTarget.above(h);

                                BlockState climbState = this.level.getBlockState(climbPos);
                                boolean passable = climbState.isAir() || isLeaves(climbState);

                                // require 1-block headroom above leaf cells in the lane
                                if (isLeaves(climbState)) {
                                    BlockPos above = climbPos.above();
                                    if (!this.level.getBlockState(above).isAir()) {
                                        canTravel = false;
                                        break;
                                    }
                                }

                                if (!passable) {
                                    canTravel = false;
                                    break;
                                }

                                BlockState trunkState = this.level.getBlockState(trunkPos);
                                if (!isWood(trunkState)) break; // trunk ended
                                woodFound = true;
                            }

                            if (canTravel && woodFound) {
                                float add = this.entity.getBbWidth() + 0.25F;
                                float offX = dir.getStepX() * add + 0.1F;
                                float offZ = dir.getStepZ() * add + 0.1F;

                                this.targetTrunk = target;
                                this.targetX = target.getX() + 0.5F + offX;
                                this.targetY = target.getY() + 0.5F;
                                this.targetZ = target.getZ() + 0.5F + offZ;
                                this.approachSide = dir;

                                AABB bounds = getBoundsAtPos(this.targetX, this.targetY, this.targetZ);
                                if (noCollisionExceptLeaves(bounds)) {
                                    PathNavigation nav = this.entity.getNavigation();
                                    this.path = nav.createPath(
                                            new BlockPos(Mth.floor(this.targetX), Mth.floor(this.targetY), Mth.floor(this.targetZ)),
                                            0
                                    );
                                    if (this.path != null) {
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                } else if (state.isAir()) {
                    break;
                }
            }
        }
        return false;
    }

    private static boolean isLeaves(BlockState state) {
        return state.is(BlockTags.LEAVES);
    }

    private static boolean isWood(BlockState state) {
        return state.is(BlockTags.LOGS);
    }

    private boolean noCollisionExceptLeaves(AABB box) {
        int minX = Mth.floor(box.minX);
        int minY = Mth.floor(box.minY);
        int minZ = Mth.floor(box.minZ);
        int maxX = Mth.floor(box.maxX);
        int maxY = Mth.floor(box.maxY);
        int maxZ = Mth.floor(box.maxZ);

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    BlockPos pos = new BlockPos(x, y, z);
                    BlockState bs = level.getBlockState(pos);
                    if (bs.isAir()) continue;
                    if (isLeaves(bs)) continue;
                    if (!bs.getCollisionShape(level, pos).isEmpty()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private AABB getBoundsAtPos(double x, double y, double z) {
        float half = this.entity.getBbWidth() / 2.0F;
        double inset = 0.05;
        return new AABB(
                x - half + inset, y, z - half + inset,
                x + half - inset, y + this.entity.getBbHeight(), z + half - inset
        );
    }

    @Override
    public void start() {
        if (this.path != null) {
            this.entity.getNavigation().moveTo(this.path, this.movementSpeed);
        }
        this.active = true;
        this.gliding = false;
        this.reachedTarget = false;
        this.glideTarget = null;
        this.entity.noPhysics = false;
    }

    @Override
    public void tick() {
        // glide steering
        if (this.gliding && this.entity.getAnimation() == EntityAnimation.GLIDING.get() && this.glideTarget != null) {
            Vec3 to = new Vec3(glideTarget.x - this.entity.getX(), 0.0, glideTarget.z - this.entity.getZ());
            if (to.lengthSqr() > 1e-4) {
                this.entity.setDeltaMovement(this.entity.getDeltaMovement().add(to.normalize().scale(0.03)));
            }
        }

        if (this.reachedTarget) {
            BlockPos currentTrunk = new BlockPos(
                    this.targetTrunk.getX(),
                    Mth.floor(this.entity.getBoundingBox().minY),
                    this.targetTrunk.getZ()
            );

            // trunk under feet turned to air: glide or hop away
            if (!this.gliding && this.level.isEmptyBlock(currentTrunk)) {
                RandomSource random = this.entity.getRandom();
                if (random.nextFloat() < 0.3f) {
                    Vec3 push = new Vec3(-this.approachSide.getStepX() * 0.1F, 0.22F, -this.approachSide.getStepZ() * 0.1F);
                    this.entity.setDeltaMovement(this.entity.getDeltaMovement().add(push));
                } else {
                    Vec3 pos = findGlideTarget(random, true);
                    if (pos == null) pos = findGlideTarget(random, false);
                    if (pos != null) {
                        this.glideTarget = pos;
                        this.entity.setGlidingTo(pos);
                        Vec3 dir0 = new Vec3(pos.x - this.entity.getX(), 0.0, pos.z - this.entity.getZ()).normalize();
                        this.entity.setDeltaMovement(this.entity.getDeltaMovement().add(dir0.scale(0.12)).add(0.0, 0.28, 0.0));
                        this.gliding = true;
                        this.entity.setAnimation(EntityAnimation.GLIDING.get());
                    }
                }
                this.active = false;
                this.entity.noPhysics = false;
                return;
            }

            // validate trunk slice
            BlockState trunkState = level.getBlockState(currentTrunk);
            if (!(isLeaves(trunkState) || isWood(trunkState))) {
                this.active = false;
                this.entity.noPhysics = false;
                return;
            }

            // steer toward the climb slot and climb
            this.entity.getMoveControl().setWantedPosition(this.targetX, this.entity.getBoundingBox().minY, this.targetZ, this.movementSpeed);
            this.entity.setAnimation(EntityAnimation.CLIMBING.get());

            // === NEW: broaden noclip condition to include "leaf wall" directly in front ===
            BlockPos facePos = currentTrunk.relative(this.approachSide);
            boolean insideLeaves =
                    isLeaves(level.getBlockState(this.entity.blockPosition())) ||
                            isLeaves(level.getBlockState(this.entity.blockPosition().above()));
            boolean leafWallAhead = isLeafyFrontColumn(facePos);

            // only allow noclip while we are actively climbing the chosen lane
            this.entity.noPhysics = insideLeaves || leafWallAhead;

            // if leaves are immediately in front, bias up & into canopy
            if (isLeaves(this.level.getBlockState(facePos))) {
                BlockPos step = facePos;
                int up = 0;
                while (up < 3 && isLeaves(level.getBlockState(step))) {
                    step = step.above();
                    up++;
                }
                if (level.isEmptyBlock(step)) {
                    this.entity.setPos(this.targetX, step.getY() + 0.05, this.targetZ);
                    this.entity.setDeltaMovement(this.entity.getDeltaMovement().x, 0.42, this.entity.getDeltaMovement().z);
                } else {
                    this.active = false;
                    this.entity.noPhysics = false;
                    return;
                }
            }

            // horizontal nudge + vertical push when hugging trunk
            Vec3 toward = new Vec3(this.targetX - this.entity.getX(), 0.0, this.targetZ - this.entity.getZ());
            double d2 = toward.lengthSqr();
            if (d2 > 1e-4) {
                this.entity.setDeltaMovement(this.entity.getDeltaMovement().add(toward.normalize().scale(0.08)));
            }

            boolean atTrunkFace =
                    this.entity.horizontalCollision || level.getBlockState(currentTrunk).isFaceSturdy(level, currentTrunk, this.approachSide);
            if (atTrunkFace) {
                this.entity.setDeltaMovement(
                        this.entity.getDeltaMovement().x,
                        0.42,
                        this.entity.getDeltaMovement().z
                );
                this.entity.setDeltaMovement(this.entity.getDeltaMovement().add(
                        this.approachSide.getStepX() * 0.06, 0.0, this.approachSide.getStepZ() * 0.06));

                if (d2 < (0.75 * 0.75)) {
                    this.entity.setPos(this.targetX, this.entity.getY(), this.targetZ);
                }
                if (this.entity.distanceToSqr(Vec3.atCenterOf(currentTrunk)) > 2.0) {
                    this.active = false;
                    this.entity.noPhysics = false;
                }
            }

            // bumped head into leaves: step onto canopy
            if (this.entity.verticalCollision && !this.gliding) {
                BlockPos top = BlockPos.containing(
                        this.entity.getX(),
                        this.entity.getBoundingBox().maxY + 0.1,
                        this.entity.getZ()
                );
                if (isLeaves(this.level.getBlockState(top))) {
                    if (this.level.isEmptyBlock(top.above())) {
                        this.entity.setPos(this.targetX, Mth.ceil(this.entity.getY() + 1) + 0.2, this.targetZ);
                    } else {
                        this.entity.setPos(this.targetX, this.entity.getY() + 0.2, this.targetZ);
                    }
                }
            }
        } else {
            if (this.path == null) return;

            if (this.path.isDone()) {
                this.entity.setAnimation(EntityAnimation.START_CLIMBING.get());
                this.reachedTarget = true;

                // LOS check
                Vec3 origin = this.entity.position().add(0.0, this.entity.getEyeHeight(), 0.0);
                HitResult hit = this.level.clip(new ClipContext(
                        origin,
                        new Vec3(this.targetX, this.targetY, this.targetZ),
                        ClipContext.Block.OUTLINE,
                        ClipContext.Fluid.ANY,
                        this.entity
                ));
                if (hit != null && hit.getType() == HitResult.Type.BLOCK) {
                    this.path = null;
                }
            } else {
                this.entity.getNavigation().moveTo(this.path, this.movementSpeed);
            }

            this.entity.noPhysics = false;
        }
    }

    // Checks a short column in front of the trunk lane for any leaves so we can noclip before we actually collide.
    private boolean isLeafyFrontColumn(BlockPos facePos) {
        // look one block in front of the trunk slice (facePos), scan from one below feet to two above head
        int baseY = Mth.floor(this.entity.getBoundingBox().minY);
        for (int dy = -1; dy <= 2; dy++) {
            BlockPos p = new BlockPos(facePos.getX(), baseY + dy, facePos.getZ());
            BlockState s = level.getBlockState(p);
            if (isLeaves(s)) return true;
        }
        // also check half-step forward (approach direction) to catch corner grazing
        BlockPos halfForward = facePos.relative(this.approachSide);
        for (int dy = -1; dy <= 2; dy++) {
            BlockPos p = new BlockPos(halfForward.getX(), baseY + dy, halfForward.getZ());
            BlockState s = level.getBlockState(p);
            if (isLeaves(s)) return true;
        }
        return false;
    }

    private Vec3 findGlideTarget(RandomSource random, boolean preferLeaves) {
        for (int i = 0; i < 100; i++) {
            double x = (random.nextFloat() - 0.5) * 45.0;
            double z = (random.nextFloat() - 0.5) * 45.0;

            Vec3 base = this.entity.position().add(x, 0.0, z);
            BlockPos p = BlockPos.containing(base.x, this.entity.getY(), base.z);

            int y = level.getHeight(
                    preferLeaves ? Heightmap.Types.MOTION_BLOCKING_NO_LEAVES : Heightmap.Types.MOTION_BLOCKING,
                    p.getX(), p.getZ()
            );
            Vec3 candidate = new Vec3(Mth.floor(base.x) + 0.5, y + 0.5, Mth.floor(base.z) + 0.5);

            BlockState s = level.getBlockState(BlockPos.containing(candidate));
            boolean ok = this.entity.position().distanceTo(candidate) > 20.0 &&
                    (preferLeaves ? isLeaves(s) : !isLiquid(s));

            if (ok) return candidate;
        }
        return null;
    }

    private static boolean isLiquid(BlockState s) {
        Material m = s.getMaterial();
        return m.isLiquid();
    }

    @Override
    public boolean canContinueToUse() {
        if (!this.reachedTarget) {
            Path current = this.entity.getNavigation().getPath();
            if (current == null) return false;
            if (this.path != null && current != this.path && !current.sameAs(this.path)) return false;
        } else {
            // If we’re colliding but the collision is leaves in front of our lane, keep going.
            if (!this.level.noCollision(this.entity, this.entity.getBoundingBox())) {
                BlockPos pos = this.entity.blockPosition();
                if (!isLeaves(level.getBlockState(pos)) && !isLeafyFrontColumn(this.targetTrunk.relative(this.approachSide))) {
                    return false;
                }
            }
        }
        return this.active && !this.gliding;
    }

    @Override
    public void stop() {
        this.lastActive = this.entity.tickCount;
        this.path = null;
        this.targetTrunk = null;
        this.active = false;
        this.reachedTarget = false;
        this.glideTarget = null;
        this.entity.noPhysics = false; // restore
    }
}
