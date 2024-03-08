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
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
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

public class CompsognathusEntity extends DinosaurEntity {
    private static final DataParameter<Integer> VARIANT = EntityDataManager.createKey(CompsognathusEntity.class, DataSerializers.VARINT);

    public CompsognathusEntity(World world) {
        super(world);
        this.setVariant(this.getRNG().nextInt(6));
        this.doesEatEggs(true);
        this.target(DodoEntity.class, OthnieliaEntity.class, MicroceratusEntity.class, DimetrodonEntity.class, MicroraptorEntity.class, CrassigyrinusEntity.class, LeptictidiumEntity.class, EntityPlayer.class, EntityAnimal.class, EntityVillager.class, GoatEntity.class);
        this.tasks.addTask(0, new LeapingMeleeEntityAI(this, this.dinosaur.getAttackSpeed()));
        this.tasks.addTask(1, new RaptorLeapEntityAI(this));
        this.tasks.addTask(1, new CompyHurtByTarget());
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
    public SoundEvent getSoundForAnimation(Animation animation)
    {
        switch (EntityAnimation.getAnimation(animation))
        {
            case SPEAK:
                return SoundHandler.COMPSOGNATHUS_LIVING;
            case DYING:
                return SoundHandler.COMPSOGNATHUS_DEATH;
            case INJURED:
                return SoundHandler.COMPSOGNATHUS_HURT;
            case CALLING:
                return SoundHandler.COMPSOGNATHUS_CALL;
            case BEGGING:
                return SoundHandler.COMPSOGNATHUS_THREAT;
        }

        return null;
    }
    class CompyHurtByTarget extends EntityAIHurtByTarget {


        public CompyHurtByTarget() {
            super(CompsognathusEntity.this, true);
        }

        public void startExecuting() {
            if (CompsognathusEntity.this.herd.size() >= 3) {
                super.startExecuting();
                if (CompsognathusEntity.this.isChild()) {
                    this.alertOthers();
                    this.resetTask();
                }
            }
        }

        protected void setEntityAttackTarget(DinosaurEntity creatureIn, EntityLivingBase entityLivingBaseIn) {
            if (creatureIn instanceof CompsognathusEntity && !creatureIn.isChild() && creatureIn != null) {
                super.setEntityAttackTarget(creatureIn, entityLivingBaseIn);
            }


        }
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
            case 0: default: return texture("nostripes");
            case 1: return texture("stripes");
            case 2: return texture("lightstripes");
            case 3: return texture("orange");
            case 4: return texture("blue");
            case 5: return texture("purple");
        }
    }
    private ResourceLocation texture(String variant){
        String formattedName = this.dinosaur.getName().toLowerCase(Locale.ENGLISH).replaceAll(" ", "_");
        String baseTextures = "textures/entities/" + formattedName + "/";
        String texture = baseTextures + formattedName;
        return isMale()?new ResourceLocation(RebornMod.MODID, texture + "_male_" + "adult" + "_" + variant + ".png"):new ResourceLocation(RebornMod.MODID, texture + "_female_" + "adult" + "_" + variant +".png");
    }
}

