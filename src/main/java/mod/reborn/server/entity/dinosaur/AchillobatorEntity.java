package mod.reborn.server.entity.dinosaur;

import mod.reborn.client.model.animation.EntityAnimation;
import mod.reborn.client.sound.SoundHandler;
import mod.reborn.server.entity.DinosaurEntity;
import mod.reborn.server.entity.ai.LeapingMeleeEntityAI;
import mod.reborn.server.entity.ai.RaptorLeapEntityAI;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class AchillobatorEntity extends DinosaurEntity
{
    public AchillobatorEntity(World world)
    {
        super(world);
        this.target(AlvarezsaurusEntity.class, MegatheriumEntity.class, SmilodonEntity.class, ArsinoitheriumEntity.class, BeelzebufoEntity.class, VelociraptorBlueEntity.class, CearadactylusEntity.class, VelociraptorCharlieEntity.class, ChilesaurusEntity.class, CoelurusEntity.class, ProceratosaurusEntity.class, CompsognathusEntity.class, VelociraptorDeltaEntity.class, DilophosaurusEntity.class, DimorphodonEntity.class, VelociraptorEchoEntity.class, GallimimusEntity.class, DodoEntity.class, HypsilophodonEntity.class, EntityPlayer.class, LeaellynasauraEntity.class, LeptictidiumEntity.class, MetriacanthosaurusEntity.class, MicroraptorEntity.class, MussaurusEntity.class, OrnithomimusEntity.class, OthnieliaEntity.class, OviraptorEntity.class, ProtoceratopsEntity.class, EntityAnimal.class, EntityVillager.class);
        this.tasks.addTask(1, new LeapingMeleeEntityAI(this, this.dinosaur.getAttackSpeed()));
        this.tasks.addTask(1, new RaptorLeapEntityAI(this));

        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, EntityPlayer.class, TyrannosaurusEntity.class, GiganotosaurusEntity.class, SpinosaurusEntity.class));
        doesEatEggs(true);
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
    @Override
    public SoundEvent getSoundForAnimation(Animation animation) {
        switch (EntityAnimation.getAnimation(animation)) {
            case SPEAK:
                return SoundHandler.ACHILLOBATOR_LIVING;
            case DYING:
                return SoundHandler.ACHILLOBATOR_DEATH;
            case INJURED:
                return SoundHandler.ACHILLOBATOR_HURT;
            case CALLING:
                return SoundHandler.ACHILLOBATOR_CALL;
            case ATTACKING:
                return SoundHandler.ACHILLOBATOR_ATTACK;
            case MATING:
                return SoundHandler.ACHILLOBATOR_MATE_CALL;
            default:
                break;
        }

        return null;
    }


    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(35.0D);
    }
}
