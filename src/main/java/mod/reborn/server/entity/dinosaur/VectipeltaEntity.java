package mod.reborn.server.entity.dinosaur;

import mod.reborn.RebornMod;
import mod.reborn.client.model.animation.EntityAnimation;
import mod.reborn.client.sound.SoundHandler;
import mod.reborn.server.entity.DinosaurEntity;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

import java.util.Locale;

public class VectipeltaEntity extends DinosaurEntity {
    private static final DataParameter<Integer> VARIANT= EntityDataManager.createKey(VectipeltaEntity.class, DataSerializers.VARINT);

    public VectipeltaEntity(World world) {
        super(world);
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
        this.setVariant(this.getRNG().nextInt(2));
    }


    @Override
    public SoundEvent getSoundForAnimation(Animation animation) {
        switch (EntityAnimation.getAnimation(animation)) {
            case SPEAK:
                return SoundHandler.ANKYLOSAURUS_LIVING;
            case DYING:
                return SoundHandler.ANKYLOSAURUS_DEATH;
            case INJURED:
                return SoundHandler.ANKYLOSAURUS_HURT;
            case CALLING:
                return SoundHandler.ANKYLOSAURUS_CALL;
            case ROARING:
                return SoundHandler.ANKYLOSAURUS_CALL;
            case ATTACKING:
                return SoundHandler.ANKYLOSAURUS_ATTACK;
            case MATING:
                return SoundHandler.ANKYLOSAURUS_MATE_CALL;
            default:
                break;
        }

        return null;
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
            case 0: default: return texture("red");
            case 1: return texture("gray");
        }
    }
    private ResourceLocation texture(String variant){
        String formattedName = this.dinosaur.getName().toLowerCase(Locale.ENGLISH).replaceAll(" ", "_");
        String baseTextures = "textures/entities/" + formattedName + "/";
        String texture = baseTextures + formattedName;
        return isMale()?new ResourceLocation(RebornMod.MODID, texture + "_male_" + "adult" + "_" + variant + ".png"):new ResourceLocation(RebornMod.MODID, texture + "_female_" + "adult" + "_" + variant +".png");
    }
}