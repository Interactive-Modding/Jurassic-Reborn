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

public class DreadnoughtusEntity extends DinosaurEntity {
    
	private int stepCount = 0;

    public LegSolverQuadruped legSolver;

    public DreadnoughtusEntity (World world) {
        super(world);
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.onGround && !this.isInWater()) {
            if (this.moveForward > 0 && (this.posX - this.prevPosX > 0 || this.posZ - this.prevPosZ > 0) && this.stepCount <= 0) {
                this.playSound(SoundHandler.STOMP, (float) this.interpolate(0.1F, 1.0F), this.getSoundPitch());
                this.stepCount = 65;
            }
            this.stepCount -= this.moveForward * 9.5;
        }
    }
    @Override
    public SoundEvent getSoundForAnimation(Animation animation) {
        switch (EntityAnimation.getAnimation(animation)) {
            case SPEAK:
                return SoundHandler.DREADNOUGHTUS_LIVING;
            case CALLING:
                return SoundHandler.DREADNOUGHTUS_CALLING;
            case DYING:
                return SoundHandler.DREADNOUGHTUS_DEATH;
            case BEGGING:
                return SoundHandler.DREADNOUGHTUS_THREAT;
            case INJURED:
                return SoundHandler.DREADNOUGHTUS_HURT;
            case MATING:
                return SoundHandler.DREADNOUGHTUS_MATING;
            case WALKING:
                return SoundHandler.STOMP;
            default:
                return null;
        }
    }
}
