package mod.reborn.server.entity.dinosaur;

import mod.reborn.client.model.animation.EntityAnimation;
import mod.reborn.client.sound.SoundHandler;
import mod.reborn.server.entity.DinosaurEntity;
import mod.reborn.server.entity.LegSolver;
import mod.reborn.server.entity.LegSolverQuadruped;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class StegosaurusEntity extends DinosaurEntity {
    public LegSolverQuadruped legSolver;

    public StegosaurusEntity(World world) {
        super(world);
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
    }

    @Override
    protected LegSolver createLegSolver() {
        return this.legSolver = new LegSolverQuadruped(0.2F, 1.2F, 1.0F, 1.0F, 1.0F);
    }

    @Override
    public SoundEvent getSoundForAnimation(Animation animation) {
        switch (EntityAnimation.getAnimation(animation)) {
            case SPEAK:
                return SoundHandler.STEGOSAURUS_LIVING;
            case CALLING:
                return SoundHandler.STEGOSAURUS_LIVING;
            case DYING:
                return SoundHandler.STEGOSAURUS_DEATH;
            case INJURED:
                return SoundHandler.STEGOSAURUS_HURT;
		default:
			break;
        }

        return null;
    }
}

