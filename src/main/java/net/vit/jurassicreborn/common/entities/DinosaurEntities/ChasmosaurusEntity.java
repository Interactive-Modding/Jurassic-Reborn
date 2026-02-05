package net.vit.jurassicreborn.common.entities.DinosaurEntities;

import com.github.alexthe666.citadel.animation.Animation;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.client.sounds.SoundHandler;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimation;
import net.vit.jurassicreborn.common.entities.DinosaurEntity;
import net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurHandler;
import net.vit.jurassicreborn.common.entities.IHasVariants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;

import java.util.Locale;

public class ChasmosaurusEntity extends DinosaurEntity implements IHasVariants
{
    private static final EntityDataAccessor<Integer> VARIANT= SynchedEntityData.defineId(ChasmosaurusEntity.class, EntityDataSerializers.INT);

    public ChasmosaurusEntity(Level world, EntityType<ChasmosaurusEntity> type)
    {
        super(world, type, DinosaurHandler.CHASMOSAURUS);
                this.addTask(1, new HurtByTargetGoal(this));
                this.setVariant(this.getRandom().nextInt(3));

    }

    @Override
    public SoundEvent getSoundForAnimation(Animation animation)
    {
        switch (EntityAnimation.getAnimation(animation))
        {
            case SPEAK:
                return SoundHandler.CHASMOSAURUS_LIVING;
            case DYING:
                return SoundHandler.CHASMOSAURUS_DEATH;
            case INJURED:
                return SoundHandler.CHASMOSAURUS_HURT;
            case BEGGING:
                return SoundHandler.CHASMOSAURUS_THREAT;
        }

        return null;
    }

    public void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(VARIANT, 0);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putInt("Variant", this.entityData.get(VARIANT));
    }


    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        this.entityData.set(VARIANT, nbt.getInt("Variant"));
    }

    public void setVariant(int value){
        this.entityData.set(VARIANT, value);
    }

    public int getVariant(){
        return this.entityData.get(VARIANT);
    }

    public ResourceLocation getTexture(){
        switch(getVariant()){
            case 0: default: return texture("blue");
            case 1: return texture("brown");
            case 2: return texture("jp");
        }
    }
    private ResourceLocation texture(String variant){
        String formattedName = this.dinosaur.getName().toLowerCase(Locale.ENGLISH).replaceAll(" ", "_");
        String baseTextures = "textures/entities/" + formattedName + "/";
        String texture = baseTextures + formattedName;
        return isMale()?new ResourceLocation(JurassicReborn.MODID, texture + "_male_" + "adult" + "_" + variant + ".png"):new ResourceLocation(JurassicReborn.MODID, texture + "_female_" + "adult" + "_" + variant +".png");
    }
}
