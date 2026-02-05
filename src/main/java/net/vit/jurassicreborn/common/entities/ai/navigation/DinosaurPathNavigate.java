package net.vit.jurassicreborn.common.entities.ai.navigation;

import net.vit.jurassicreborn.common.entities.DinosaurEntity;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.NodeEvaluator;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.phys.Vec3;

public class DinosaurPathNavigate extends GroundPathNavigation {

    public DinosaurPathNavigate(DinosaurEntity entity, Level level) {
        super(entity, level);
    }

    @Override
    protected PathFinder createPathFinder(int maxVisitedNodes) {
        // mob is already set by PathNavigation ctor
        DinosaurEntity dino = (DinosaurEntity) this.mob;

        DinosaurWalkNodeEvaluator eval =
                new DinosaurWalkNodeEvaluator(() -> dino.getDinosaur()); // lazy, NPE-safe

        eval.setCanOpenDoors(false);
        eval.setCanPassDoors(false);
        this.nodeEvaluator = eval;

        return new PathFinder(eval, maxVisitedNodes);
    }


    @Override
    protected void followThePath() {
        final Path path = this.path;
        if (path == null) return;

        final Vec3 pos = this.getTempMobPos();

        // limit lookahead to nodes on same block Y
        int limit = path.getNodeCount();
        for (int i = path.getNextNodeIndex(); i < path.getNodeCount(); ++i) {
            if ((double) path.getNode(i).y != Math.floor(pos.y)) {
                limit = i;
                break;
            }
        }

        // next waypoint (center of node block)
        final Vec3 next = Vec3.atCenterOf(path.getNodePos(path.getNextNodeIndex()));

        final double dx = Math.abs(this.mob.getX() - next.x);
        final double dz = Math.abs(this.mob.getZ() - next.z);
        final double dy = Math.abs(this.mob.getY() - next.y);

        final double w  = this.mob.getBbWidth();
        final int iW    = Mth.ceil(w);
        final int iH    = Mth.ceil(this.mob.getBbHeight()); // kept for parity

        final double maxDist = (w > 0.75D) ? iW : (0.75D - w / 2.0D);

        if (dx < maxDist && dz < maxDist && dy < 2.0D) {
            path.advance(); // old incrementPathIndex()
        }

        // direct-path skipping across same-Y nodes
        for (int i = limit - 1; i >= path.getNextNodeIndex(); --i) {
            final Vec3 candidate = Vec3.atCenterOf(path.getNodePos(i));
            // parchment 2022.08.10: 2-arg helper name
            if (this.canMoveDirectly(pos, candidate)) {
                path.setNextNodeIndex(i);
                break;
            }
        }

        this.doStuckDetection(pos);
    }

    @Override
    protected boolean canUpdatePath() {
        // Avoid subclass field; use mob
        return !((DinosaurEntity) this.mob).isMovementBlocked() && super.canUpdatePath();
    }
}
