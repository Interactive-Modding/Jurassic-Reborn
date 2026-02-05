package net.vit.jurassicreborn.common.entities.ai;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.AABB;
import net.vit.jurassicreborn.common.entities.DinosaurEntity;
import net.vit.jurassicreborn.common.entities.FlyingDinosaurEntity;
import net.vit.jurassicreborn.common.entities.ai.util.AIUtils;

import java.util.EnumSet;

public class SleepEntityAI extends Goal {
    private final DinosaurEntity dino;
    private Path path;
    private int giveUpTicks;

    public SleepEntityAI(DinosaurEntity dino) {
        this.dino = dino;
        // Blocks movement, jumping, and look while running
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP, Flag.LOOK));
    }

    /* --------------------------------------------------------------------- */
    /*  start condition                                                      */
    /* --------------------------------------------------------------------- */

    @Override
    public boolean canUse() {
        Level level = dino.level;
        if (dino.getMetabolism().isHungry()) {
            return false;
        }
        boolean marine = dino.getDinosaur().isMarineCreature();
        if (!dino.isAlive() || dino.isSleeping() || !dino.shouldSleep() || dino.getStayAwakeTime() > 0) {
            return false;
        }
        if (dino instanceof FlyingDinosaurEntity flying && !flying.isOnGround()) {
            flying.shouldLand = true;
            return false;
        }
        // Marine creatures: allow immediately (we’ll just stop moving and sleep)
        if (marine) return true;

        // Look for a nearby sheltered, dry block to sleep on
        final int RANGE = 8;
        int ox = (int) dino.getX();
        int oz = (int) dino.getZ();

        for (int x = ox - RANGE; x <= ox + RANGE; ++x) {
            for (int z = oz - RANGE; z <= oz + RANGE; ++z) {
                BlockPos top = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, new BlockPos(x, 0, z));
                if (!level.isLoaded(top)) continue;
                if (!level.isEmptyBlock(top)) continue; // head space must be free
                if (level.getFluidState(top.below()).is(FluidTags.WATER)) continue; // don’t sleep over water
                if (level.canSeeSky(top)) continue; // wants cover
                if (!canFit(top)) continue;

                // try to path to this spot
                if (dino.setSleepLocation(top, true)) {
                    this.path = dino.getNavigation().getPath();
                    return true;
                }
            }
        }

        // If standing in water, try to path to nearest shore
        if (dino.isInWater()) {
            BlockPos shore = AIUtils.findShore(level, dino.blockPosition());
            if (shore != null && dino.getNavigation().moveTo(
                    shore.getX() + 0.5, shore.getY(), shore.getZ() + 0.5, 1.0
            )) {
                this.path = dino.getNavigation().getPath();
                return true;
            }
        }

        BlockPos here = dino.blockPosition();
        if (level.isLoaded(here)) {
            dino.setSleepLocation(here, false); // don’t move; just use current
            this.path = null;
            return true;
        }

        return false;
    }

    private boolean canFit(BlockPos pos) {
        double x = pos.getX() + 0.5;
        double y = pos.getY();
        double z = pos.getZ() + 0.5;
        double halfWidth = dino.getBbWidth() / 2.0;
        AABB box = new AABB(
                x - halfWidth, y,
                z - halfWidth,
                x + halfWidth, y + dino.getBbHeight(),
                z + halfWidth
        );
        // use entity-aware collision test so we don’t clip into blocks/ents
        return dino.level.noCollision(dino, box);
    }

    /* --------------------------------------------------------------------- */
    /*  behaviour                                                            */
    /* --------------------------------------------------------------------- */

    @Override
    public void start() {
        giveUpTicks = 400; // ~20s max attempt before just sleeping
    }

    @Override
    public void tick() {
        PathNavigation nav = dino.getNavigation();

        // If we had a planned path, ensure we keep heading for its end node
        if (this.path != null) {
            Node end = this.path.getEndNode();
            Path current = nav.getPath();

            // If the path vanished or points somewhere else, rebuild it
            if (end != null && (current == null || current.getEndNode() == null
                    || !sameNode(end, current.getEndNode()))) {
                Path newPath = nav.createPath(end.x, end.y, end.z, 1);
                if (newPath != null) {
                    nav.moveTo(newPath, 1.0);
                    this.path = newPath;
                }
            }
        }

        // When we arrive (or time out), go to sleep
        if (--giveUpTicks <= 0 || (dino.getStayAwakeTime() <= 0 && (this.path == null || this.path.isDone()))) {
            nav.stop();
            dino.setSleeping(true);
        }
    }

    private static boolean sameNode(Node a, Node b) {
        return a.x == b.x && a.y == b.y && a.z == b.z;
    }

    @Override
    public boolean canContinueToUse() {
        // Keep running until we actually toggle sleeping, or sleeping becomes invalid
        return !dino.isCarcass() && !dino.isSleeping() && dino.shouldSleep();
    }

    @Override
    public void stop() {
        dino.getNavigation().stop();
        dino.setSleeping(true);
        this.path = null;
    }
}
