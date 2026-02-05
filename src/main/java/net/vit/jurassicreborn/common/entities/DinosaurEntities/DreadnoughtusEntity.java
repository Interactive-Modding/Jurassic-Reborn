package net.vit.jurassicreborn.common.entities.DinosaurEntities;

import com.github.alexthe666.citadel.animation.Animation;
import net.vit.jurassicreborn.client.sounds.SoundHandler;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimation;
import net.vit.jurassicreborn.common.entities.DinosaurEntity;
import net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurHandler;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;
import net.vit.jurassicreborn.common.entities.LegSolverQuadruped;

public class DreadnoughtusEntity extends DinosaurEntity {
    
	private int stepCount = 0;

    public LegSolverQuadruped legSolver;

    public DreadnoughtusEntity(EntityType<DreadnoughtusEntity> type, Level world) {
        super(world, type, DinosaurHandler.DREADNOUGHTUS);
                this.addTask(1, new HurtByTargetGoal(this));
    }

    @Override
    public void tick() {
        super.tick();
        if (this.onGround && !this.isInWater()) {
            if (this.zza > 0 && (this.getX() - this.xOld > 0 || this.getZ() - this.zOld > 0) && this.stepCount <= 0) {
                this.playSound(SoundHandler.STOMP, (float) this.interpolate(0.1F, 1.0F), this.getVoicePitch());
                this.stepCount = 65;
            }
            this.stepCount -= this.zza * 9.5;
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

