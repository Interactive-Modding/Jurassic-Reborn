package mod.reborn.server.entity.dinosaur;

import mod.reborn.client.model.animation.EntityAnimation;
import mod.reborn.client.sound.SoundHandler;
import mod.reborn.server.entity.DinosaurEntity;
import mod.reborn.server.entity.LegSolverBiped;
import mod.reborn.server.entity.ai.RaptorLeapEntityAI;
import mod.reborn.server.entity.ai.animations.PeckGroundAnimationAI;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class OrnithomimusEntity extends DinosaurEntity
{
//    public LegSolverBiped legSolver;

    public OrnithomimusEntity(World world)
    {
        super(world);
        this.animationTasks.addTask(3, new PeckGroundAnimationAI(this));
        this.tasks.addTask(1, new RaptorLeapEntityAI(this));
    }

    @Override
    public EntityAIBase getAttackAI() {
        return new RaptorLeapEntityAI(this);
    }

    @Override
    public void fall(float distance, float damageMultiplier) {
        if (this.getAnimation() != EntityAnimation.LEAP_LAND.get()) {
            super.fall(distance, damageMultiplier);
        }
    }
//    @Override
//    protected LegSolverBiped createLegSolver() {
//        return this.legSolver = new LegSolverBiped(-0.05F, 0.25F, 0.5F);
//    }
    @Override
    public SoundEvent getSoundForAnimation(Animation animation) {
        switch (EntityAnimation.getAnimation(animation)) {
            case SPEAK:
                return SoundHandler.ORNITHOMIMUS_LIVING;
            case CALLING:
                return SoundHandler.ORNITHOMIMUS_LIVING;
            case DYING:
                return SoundHandler.ORNITHOMIMUS_DEATH;
            case INJURED:
                return SoundHandler.ORNITHOMIMUS_HURT;
            default:
                break;
        }

        return null;
    }
}
