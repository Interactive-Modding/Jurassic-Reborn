package mod.reborn.server.entity.ai;

import mod.reborn.server.entity.DinosaurEntity;
import mod.reborn.server.entity.ai.util.AIUtils;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.math.BlockPos;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class AdvancedSwimEntityAI extends EntityAIBase {
    private final DinosaurEntity entity;
    private volatile BlockPos shore = null;
    private static ThreadPoolExecutor tpeSwimming = new ThreadPoolExecutor(0, 10, 10, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
    private volatile Path path;
    private volatile boolean seti;
    private volatile boolean started = false;

    public AdvancedSwimEntityAI(DinosaurEntity entity) {
        this.entity = entity;
        this.setMutexBits(0);
    }

    @Override
    public boolean shouldExecute() {
        return this.entity.isSwimming() && this.entity.getNavigator().noPath() && (this.entity.getAttackTarget() == null || this.entity.getAttackTarget().isDead) && this.entity.canDinoSwim();
    }

    @Override
    public void startExecuting() {

        if (this.started == true)
            return;

        if (tpeSwimming.getActiveCount() < 9) {

            try {
                tpeSwimming.execute(new ThreadRunnable(this, this.entity) {

                    @Override
                    public void run() {
                        synchronized (this.entity.world) {
                            this.ai.started = true;

                            BlockPos surface = AIUtils.findSurface(this.entity);

                            if (surface != null) {
                                this.ai.shore = AIUtils.findShore(this.entity.getEntityWorld(), surface.down());

                                if (this.ai.shore != null) {

                                    this.ai.seti = true;
                                }
                            }
                        }
                        this.ai.started = false;
                    }
                });
            } catch (RejectedExecutionException e) {

            }
        }
    }

    @Override
    public boolean shouldContinueExecuting() {
        Path currentPath = this.entity.getNavigator().getPath();
        return this.shore != null && this.path != null && (currentPath == null || !currentPath.isFinished());
    }

    @Override
    public void updateTask() {

        try {
            if (this.seti) {
                this.path = this.entity.getNavigator().getPathToPos(this.shore.up());
                if (!this.entity.getNavigator().setPath(this.path, 1.5)) {
                    this.shore = null;
                }
                this.seti = false;
            }
            Path currentPath = this.entity.getNavigator().getPath();

            if (this.shore != null && this.path != null && !this.path.isSamePath(currentPath)) {
                synchronized (this.path) {
                    synchronized (this.shore) {
                        Path path = this.entity.getNavigator().getPathToPos(this.shore);
                        if (path != null && !path.isSamePath(currentPath)) {
                            this.path = path;
                            this.entity.getNavigator().setPath(path, 1.5);
                        }
                    }
                }
            }
        } catch (NullPointerException e) {

        }
    }

    abstract class ThreadRunnable implements Runnable {

        final DinosaurEntity entity;
        final AdvancedSwimEntityAI ai;

        ThreadRunnable(AdvancedSwimEntityAI wanderAI, DinosaurEntity entity) {
            this.ai = wanderAI;
            this.entity = entity;
        }
    }
}