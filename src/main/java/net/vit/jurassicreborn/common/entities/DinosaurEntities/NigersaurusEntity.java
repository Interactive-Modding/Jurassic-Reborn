package net.vit.jurassicreborn.common.entities.DinosaurEntities;

import com.github.alexthe666.citadel.animation.Animation;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimation;
import net.vit.jurassicreborn.client.sounds.SoundHandler;
import net.vit.jurassicreborn.common.entities.DinosaurEntity;
import net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurHandler;

import java.util.Locale;

public class NigersaurusEntity extends DinosaurEntity {
    private static final EntityDataAccessor<Integer> VARIANT= SynchedEntityData.defineId(NigersaurusEntity.class, EntityDataSerializers.INT);

    public NigersaurusEntity(EntityType<NigersaurusEntity> type, Level world) {
        super(world, type, DinosaurHandler.NIGERSAURUS);
        this.setVariant(this.getRandom().nextInt(3));
    }


    @Override
    public SoundEvent getSoundForAnimation(Animation animation)
    {
        switch (EntityAnimation.getAnimation(animation))
        {
            case SPEAK:
                return SoundHandler.NIGERSAURUS_LIVING;
            case DYING:
                return SoundHandler.NIGERSAURUS_DEATH;
            case INJURED:
                return SoundHandler.NIGERSAURUS_HURT;
            case CALLING:
                return SoundHandler.NIGERSAURUS_LIVING;
            case BEGGING:
                return SoundHandler.NIGERSAURUS_HURT;
            case ATTACKING:
                return SoundHandler.NIGERSAURUS_ATTACKING;
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
            case 0: default: return texture("green");
            case 1: return texture("brown");
            case 2: return texture("white");
        }
    }
    private ResourceLocation texture(String variant){
        String formattedName = this.dinosaur.getName().toLowerCase(Locale.ENGLISH).replaceAll(" ", "_");
        String baseTextures = "textures/entities/" + formattedName + "/";
        String texture = baseTextures + formattedName;
        return isMale()?new ResourceLocation(JurassicReborn.MODID, texture + "_male_" + "adult" + "_" + variant + ".png"):new ResourceLocation(JurassicReborn.MODID, texture + "_female_" + "adult" + "_" + variant +".png");
    }
}

