package mod.reborn.server.entity.dinosaur;

import mod.reborn.client.model.animation.EntityAnimation;
import mod.reborn.client.sound.SoundHandler;
import mod.reborn.server.entity.DinosaurEntity;
import mod.reborn.server.entity.animal.GoatEntity;
import mod.reborn.server.entity.ai.LeapingMeleeEntityAI;
import mod.reborn.server.entity.ai.RaptorLeapEntityAI;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class VelociraptorEntity extends DinosaurEntity {

    private static final Class[] targets = {CompsognathusEntity.class,  VectipeltaEntity.class, DimetrodonEntity.class, TitanisEntity.class, SmilodonEntity.class, MegatheriumEntity.class, ElasmotheriumEntity.class, ArsinoitheriumEntity.class, EntityPlayer.class, DilophosaurusEntity.class, DimorphodonEntity.class, DodoEntity.class, LeaellynasauraEntity.class, HypsilophodonEntity.class, StegosaurusEntity.class, ProtoceratopsEntity.class, OthnieliaEntity.class, MicroceratusEntity.class};
    private static final Class[] nontargets = {VelociraptorEntity.class, VelociraptorBlueEntity.class, VelociraptorCharlieEntity.class, VelociraptorDeltaEntity.class, VelociraptorEchoEntity.class};


    public VelociraptorEntity(World world) {
        super(world);
        this.target(GoatEntity.class, TitanisEntity.class, SpinoraptorEntity.class, EntityPlayer.class, EntityAnimal.class, EntityVillager.class, AchillobatorEntity.class, AlligatorGarEntity.class, AlvarezsaurusEntity.class, BeelzebufoEntity.class, CearadactylusEntity.class, ChilesaurusEntity.class, CoelurusEntity.class, CompsognathusEntity.class, DilophosaurusEntity.class, DimorphodonEntity.class, GallimimusEntity.class, ProceratosaurusEntity.class, GuanlongEntity.class, DodoEntity.class, HypsilophodonEntity.class, LeaellynasauraEntity.class, LeptictidiumEntity.class, MicroraptorEntity.class, MussaurusEntity.class, MicroceratusEntity.class, OrnithomimusEntity.class, OthnieliaEntity.class, OviraptorEntity.class, PostosuchusEntity.class, ProtoceratopsEntity.class, SegisaurusEntity.class, TroodonEntity.class);
        this.tasks.addTask(0, new LeapingMeleeEntityAI(this, this.dinosaur.getAttackSpeed()));
        this.tasks.addTask(1, new RaptorLeapEntityAI(this));
        this.target(targets);
        for(Class entity : targets) {
            this.tasks.addTask(0, new EntityAINearestAttackableTarget<EntityLivingBase>(this, entity, true, false));
            this.targetTasks.addTask(0, new EntityAINearestAttackableTarget<EntityLivingBase>(this, entity, false));
        }
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, EntityPlayer.class, RaphusrexEntity.class, TyrannosaurusEntity.class, GiganotosaurusEntity.class, SpinosaurusEntity.class, IndominusEntity.class));
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
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(35.0D);
    }


    @Override
    public SoundEvent getSoundForAnimation(Animation animation) {
        switch (EntityAnimation.getAnimation(animation)) {
            case SPEAK:
                return SoundHandler.VELOCIRAPTOR_LIVING;
            case DYING:
                return SoundHandler.VELOCIRAPTOR_DEATH;
            case INJURED:
                return SoundHandler.VELOCIRAPTOR_HURT;
            case CALLING:
                return SoundHandler.VELOCIRAPTOR_CALL;
            case ATTACKING:
                return SoundHandler.VELOCIRAPTOR_ATTACK;
		default:
			break;
        }

        return null;
    }

    @Override
    public SoundEvent getBreathingSound() {
        return SoundHandler.VELOCIRAPTOR_BREATHING;
    }
}
