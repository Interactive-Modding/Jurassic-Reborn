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

public class StegosaurusEntity extends DinosaurEntity {
    public LegSolverQuadruped legSolver;

    public StegosaurusEntity(Level world, EntityType<StegosaurusEntity> type) {
        super(world, type, DinosaurHandler.STEGOSAURUS);
                this.addTask(1, new HurtByTargetGoal(this));
    }

    @Override
    protected LegSolverQuadruped createLegSolver() {
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


