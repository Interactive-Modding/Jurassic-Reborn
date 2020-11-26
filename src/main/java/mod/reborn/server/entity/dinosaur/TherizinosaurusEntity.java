package mod.reborn.server.entity.dinosaur;

import mod.reborn.client.model.animation.EntityAnimation;
import mod.reborn.client.sound.SoundHandler;
import mod.reborn.server.entity.DinosaurEntity;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class TherizinosaurusEntity extends DinosaurEntity
{
    public TherizinosaurusEntity(World world)
    {
        super(world);
        this.target(TyrannosaurusEntity.class);
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
    }
    @Override
    public SoundEvent getSoundForAnimation(Animation animation)
    {
        switch (EntityAnimation.getAnimation(animation))
        {
            case SPEAK:
                return SoundHandler.THERIZINOSAURUS_LIVING;
            case DYING:
                return SoundHandler.THERIZINOSAURUS_DEATH;
            case INJURED:
                return SoundHandler.THERIZINOSAURUS_HURT;
            case CALLING:
                return SoundHandler.THERIZINOSAURUS_LIVING;
            case ROARING:
                return SoundHandler.THERIZINOSAURUS_ROAR;
            case BEGGING:
                return SoundHandler.THERIZINOSAURUS_THREAT;
        }

        return null;
    }
}
