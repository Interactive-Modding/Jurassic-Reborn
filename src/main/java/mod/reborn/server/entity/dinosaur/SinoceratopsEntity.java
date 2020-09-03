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

public class SinoceratopsEntity extends DinosaurEntity
{
    public SinoceratopsEntity(World world) {
        super(world);
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
    }

        @Override
        public SoundEvent getSoundForAnimation(Animation animation) {
        switch (EntityAnimation.getAnimation(animation)) {
            case SPEAK:
                return SoundHandler.SINOCERATOPS_LIVING;
            case CALLING:
                return SoundHandler.SINOCERATOPS_CALLING;
            case DYING:
                return SoundHandler.SINOCERATOPS_DEATH;
            case BEGGING:
                return SoundHandler.SINOCERATOPS_THREAT;
            case INJURED:
                return SoundHandler.SINOCERATOPS_HURT;
            case MATING:
                return SoundHandler.SINOCERATOPS_MATING;
            default:
                return null;
        }
    }
}
