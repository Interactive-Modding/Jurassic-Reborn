package mod.reborn.server.entity.dinosaur;

import javafx.animation.Animation;
import mod.reborn.server.entity.DinosaurEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class AchillobatorEntity extends DinosaurEntity {
    public AchillobatorEntity(World worldIn) {
        super(null, worldIn);
        //this.target(AlvarezsaurusEntity.class, TitanisEntity.class, MegatheriumEntity.class, SmilodonEntity.class, ArsinoitheriumEntity.class, BeelzebufoEntity.class, VelociraptorBlueEntity.class, CearadactylusEntity.class, VelociraptorCharlieEntity.class, ChilesaurusEntity.class, CoelurusEntity.class, ProceratosaurusEntity.class, CompsognathusEntity.class, VelociraptorDeltaEntity.class, DilophosaurusEntity.class, DimorphodonEntity.class, VelociraptorEchoEntity.class, GallimimusEntity.class, DodoEntity.class, HypsilophodonEntity.class, EntityPlayer.class, LeaellynasauraEntity.class, LeptictidiumEntity.class, MetriacanthosaurusEntity.class, MicroraptorEntity.class, MussaurusEntity.class, OrnithomimusEntity.class, OthnieliaEntity.class, OviraptorEntity.class, ProtoceratopsEntity.class, EntityAnimal.class, EntityVillager.class);
        //this.goalSelector.addGoal(1, new LeapingMeleeEntityAI(this, this.dinosaur.getAttackSpeed()));
        //this.goalSelector.addGoal(1, new RaptorLeapEntityAI(this));

        //this.goalSelector.addGoal(1, new EntityAIHurtByTarget(this, true, EntityPlayer.class, RaphusrexEntity.class, IndominusEntity.class, TyrannosaurusEntity.class, GiganotosaurusEntity.class, SpinosaurusEntity.class));
        //doesEatEggs(true);
    }

    /*
        @Override
        public Goal getAttackAI() {
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

    */
    protected void applyEntityAttributes() {
        //super.applyEntityAttributes();
        this.getAttribute(Attributes.FOLLOW_RANGE).setBaseValue(35D);
    }
}
