package mod.reborn.server.entity.dinosaur;

import mod.reborn.client.model.animation.EntityAnimation;
import mod.reborn.client.sound.SoundHandler;
import mod.reborn.server.entity.DinosaurEntity;
import mod.reborn.server.entity.ai.animations.PeckGroundAnimationAI;
import mod.reborn.server.entity.ai.animations.RearingUpAnimationAI;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class MussaurusEntity extends DinosaurEntity {
    public MussaurusEntity(World world){
        super(world);
        this.animationTasks.addTask(3, new PeckGroundAnimationAI(this));
        this.animationTasks.addTask(3, new RearingUpAnimationAI(this));
    }

    @Override
    public SoundEvent getSoundForAnimation(Animation animation) {
        switch (EntityAnimation.getAnimation(animation)) {
            case SPEAK:
                return SoundHandler.MUSSAURUS_LIVING;
            case DYING:
                return SoundHandler.MUSSAURUS_DEATH;
            case INJURED:
                return SoundHandler.MUSSAURUS_HURT;
            case ATTACKING:
                return SoundHandler.MUSSAURUS_ATTACK;
            case CALLING:
                return SoundHandler.MUSSAURUS_MATE_CALL;
		default:
			break;
        }

        return null;
    }
}
