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
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;

import java.util.Locale;

public class MammothEntity extends DinosaurEntity implements IHasVariants {//is a mammoth really a dinosaur? - gamma_02
    private static final EntityDataAccessor<Integer> VARIANT= SynchedEntityData.defineId(MammothEntity.class, EntityDataSerializers.INT);

    public MammothEntity(EntityType<MammothEntity> type, Level world) {
        super(world, type, DinosaurHandler.MAMMOTH);
        this.setVariant(this.getRandom().nextInt(5));
    }

    public SoundEvent getSoundForAnimation(Animation animation) {
        switch (EntityAnimation.getAnimation(animation)) {
            case SPEAK:
                return SoundHandler.MAMMOTH_LIVING;
            case CALLING:
                return SoundHandler.MAMMOTH_ALARM_CALL;
            case MATING:
                return SoundHandler.MAMMOTH_MATE_CALL;
            case DYING:
                return SoundHandler.MAMMOTH_DEATH;
            case INJURED:
                return SoundHandler.MAMMOTH_HURT;
            case BEGGING:
                return SoundHandler.MAMMOTH_THREAT;
            default:
                return null;
        }
    }

    public void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(VARIANT, 0);
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
            case 0: default: return texture("blond");
            case 1: return texture("brunette");
            case 2: return texture("dark");
            case 3: return texture("red");
            case 4: return texture("white");
        }
    }
    private ResourceLocation texture(String variant){
        String formattedName = this.dinosaur.getName().toLowerCase(Locale.ENGLISH).replaceAll(" ", "_");
        String baseTextures = "textures/entities/" + formattedName + "/";
        String texture = baseTextures + formattedName;
        return isMale()?ResourceLocation.parse(JurassicReborn.MODID + ":" + texture + "_male_" + "adult" + "_" + variant + ".png"):ResourceLocation.parse(JurassicReborn.MODID + ":" + texture + "_female_" + "adult" + "_" + variant +".png");
    }
}
