package net.vit.jurassicreborn.common.entities.DinosaurEntities;

import com.github.alexthe666.citadel.animation.Animation;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimation;
import net.vit.jurassicreborn.client.sounds.SoundHandler;
import net.vit.jurassicreborn.common.entities.AmphibianDinosaurEntity;
import net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurHandler;
import net.vit.jurassicreborn.common.entities.ai.DinosaurWanderEntityAI;
import net.vit.jurassicreborn.common.entities.ai.LeapingMeleeEntityAI;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;

;

public class BeelzebufoEntity extends AmphibianDinosaurEntity {

    public BeelzebufoEntity(Level world, EntityType<BeelzebufoEntity> type) {
        super(world, type, DinosaurHandler.BEELZEBUFO);
        this.target(AlvarezsaurusEntity.class, CompsognathusEntity.class, LeptictidiumEntity.class, OthnieliaEntity.class, MicroraptorEntity.class, MussaurusEntity.class, GuanlongEntity.class, GallimimusEntity.class);
        this.addTask(0, new LeapingMeleeEntityAI(this, this.getSpeed()));
        this.addTask(0, new DinosaurWanderEntityAI(this, getSpeed(), 10, /*RebornConfig.ENTITIES.dinosaurWalkingRadius todo: config*/16));
    }

    @Override
    public SoundEvent getSoundForAnimation(Animation animation) {
        switch (EntityAnimation.getAnimation(animation)) {
            case SPEAK:
                return SoundHandler.BEELZEBUFO_CROAK;
            case CALLING:
                return SoundHandler.BEELZEBUFO_LONG_DISTANCE_CROAK;
            case DYING:
                return SoundHandler.BEELZEBUFO_HURT_THREAT;
            case INJURED:
                return SoundHandler.BEELZEBUFO_HURT;
            case BEGGING:
                return SoundHandler.BEELZEBUFO_THREAT;
            default:
                return null;
        }
    }

//    @Override
//    public EntityAIBase getAttackAI() {
//        return new RaptorLeapEntityAI(this);
//    }

    @Override
    public int calculateFallDamage(float distance, float damageMultiplier) {
        if (this.getAnimation() != EntityAnimation.LEAP_LAND.get()) {
            return super.calculateFallDamage(distance, damageMultiplier);
        }
        else
            return 0;
    }
    protected void applyEntityAttributes()
    {
//        super.applyEntityAttributes();
        this.getAttribute(Attributes.FOLLOW_RANGE).setBaseValue(35.0D);
    }

}


