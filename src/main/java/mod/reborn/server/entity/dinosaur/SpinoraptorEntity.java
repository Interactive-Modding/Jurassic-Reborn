package mod.reborn.server.entity.dinosaur;

import mod.reborn.RebornMod;
import mod.reborn.client.model.animation.EntityAnimation;
import mod.reborn.client.sound.SoundHandler;
import mod.reborn.server.entity.DinosaurEntity;
import mod.reborn.server.entity.ai.LeapingMeleeEntityAI;
import mod.reborn.server.entity.ai.RaptorLeapEntityAI;
import mod.reborn.server.entity.animal.GoatEntity;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

import java.util.Locale;

public class SpinoraptorEntity extends DinosaurEntity {
    private static final DataParameter<Integer> VARIANT = EntityDataManager.createKey(SmilodonEntity.class, DataSerializers.VARINT);
    private static final Class[] targets = {CompsognathusEntity.class, MammothEntity.class, VelociraptorEntity.class, VelociraptorBlueEntity.class, VelociraptorCharlieEntity.class, VelociraptorDeltaEntity.class, VelociraptorEchoEntity.class, MegatheriumEntity.class, ElasmotheriumEntity.class, ArsinoitheriumEntity.class, EntityPlayer.class, DilophosaurusEntity.class, DimorphodonEntity.class, DodoEntity.class, LeaellynasauraEntity.class, HypsilophodonEntity.class, StegosaurusEntity.class, ProtoceratopsEntity.class, OthnieliaEntity.class, MicroceratusEntity.class};

    public SpinoraptorEntity (World world) {
        super(world);
        this.setVariant(this.getRNG().nextInt(5));
        this.target(AchillobatorEntity.class, MetriacanthosaurusEntity.class, VectipeltaEntity.class, DimetrodonEntity.class, TitanisEntity.class, SmilodonEntity.class, MegatheriumEntity.class, ArsinoitheriumEntity.class, SinoceratopsEntity.class, AlligatorGarEntity.class, AlvarezsaurusEntity.class, BeelzebufoEntity.class, VelociraptorBlueEntity.class, CarnotaurusEntity.class, CeratosaurusEntity.class, VelociraptorCharlieEntity.class, ChasmosaurusEntity.class, ChilesaurusEntity.class, CoelurusEntity.class, CompsognathusEntity.class, CorythosaurusEntity.class, CrassigyrinusEntity.class, VelociraptorDeltaEntity.class, DilophosaurusEntity.class, DimorphodonEntity.class, DiplocaulusEntity.class, DodoEntity.class, VelociraptorEchoEntity.class, GallimimusEntity.class, GuanlongEntity.class, HyaenodonEntity.class, HypsilophodonEntity.class, LeaellynasauraEntity.class, LeptictidiumEntity.class, MegapiranhaEntity.class, MicroceratusEntity.class, MicroraptorEntity.class, MussaurusEntity.class, OrnithomimusEntity.class, OthnieliaEntity.class, OviraptorEntity.class, PachycephalosaurusEntity.class, ParasaurolophusEntity.class, PostosuchusEntity.class, ProceratosaurusEntity.class, ProtoceratopsEntity.class, RugopsEntity.class, SegisaurusEntity.class, StyracosaurusEntity.class, TriceratopsEntity.class, TroodonEntity.class, VelociraptorEntity.class, EntityPlayer.class, EntityAnimal.class, EntityVillager.class, GoatEntity.class);
        this.tasks.addTask(0, new LeapingMeleeEntityAI(this, this.dinosaur.getAttackSpeed()));
        this.tasks.addTask(1, new RaptorLeapEntityAI(this));
        this.target(targets);
        for(Class entity : targets) {
            this.tasks.addTask(0, new EntityAINearestAttackableTarget<EntityLivingBase>(this, entity, true, false));
            this.targetTasks.addTask(0, new EntityAINearestAttackableTarget<EntityLivingBase>(this, entity, false));
        }
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, EntityPlayer.class, TyrannosaurusEntity.class, GiganotosaurusEntity.class, SpinosaurusEntity.class));
    }

    @Override
    public EntityAIBase getAttackAI() {
        return new RaptorLeapEntityAI(this);
    }
    @Override
    public SoundEvent getSoundForAnimation(Animation animation)
    {
        switch (EntityAnimation.getAnimation(animation))
        {
            case SPEAK:
                return SoundHandler.SPINORAPTOR_LIVING;
            case DYING:
                return SoundHandler.SPINORAPTOR_DEATH;
            case INJURED:
                return SoundHandler.SPINORAPTOR_HURT;
            case CALLING:
                return SoundHandler.SPINORAPTOR_CALL;
            case ROARING:
                return SoundHandler.SPINORAPTOR_CALL;
            case BEGGING:
                return SoundHandler.SPINOSAURUS_THREAT;
        }

        return null;
    }
    @Override
    public SoundEvent getBreathingSound()
    {
        return SoundHandler.SPINORAPTOR_BREATHING;
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

    public void entityInit() {
        super.entityInit();
        this.dataManager.register(VARIANT, 0);
    }

    public void writeEntityToNBT(NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setInteger("Variant", this.getVariant());
    }

    public void readEntityFromNBT(NBTTagCompound tagCompound) {
        super.readEntityFromNBT(tagCompound);
        this.setVariant(tagCompound.getInteger("Variant"));
    }

    public void setVariant(int value){
        this.dataManager.set(VARIANT, value);
    }

    public int getVariant(){
        return this.dataManager.get(VARIANT);
    }


    public ResourceLocation getTexture(){
        switch(getVariant()){
            case 0: default: return texture("black");
            case 1: return texture("green");
            case 2: return texture("grey");
            case 3: return texture("orange");
            case 4: return texture("vivid");
        }
    }
    private ResourceLocation texture(String variant){
        String formattedName = this.dinosaur.getName().toLowerCase(Locale.ENGLISH).replaceAll(" ", "_");
        String baseTextures = "textures/entities/" + formattedName + "/";
        String texture = baseTextures + formattedName;
        return isMale()?new ResourceLocation(RebornMod.MODID, texture + "_male_" + "adult" + "_" + variant + ".png"):new ResourceLocation(RebornMod.MODID, texture + "_female_" + "adult" + "_" + variant +".png");
    }
}

