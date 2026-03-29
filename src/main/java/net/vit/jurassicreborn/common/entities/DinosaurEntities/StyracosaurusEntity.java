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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;

import java.util.Locale;

public class StyracosaurusEntity extends DinosaurEntity implements IHasVariants {
    private static final EntityDataAccessor<Integer> VARIANT= SynchedEntityData.defineId(StyracosaurusEntity.class, EntityDataSerializers.INT);


    private static boolean isKingSet = false;
    private boolean isKing = false;
    private StyracosaurusEntity king = null;

    public StyracosaurusEntity(EntityType<StyracosaurusEntity> type, Level world) {
        super(world, type, DinosaurHandler.STYRACOSAURUS);
                this.addTask(1, new HurtByTargetGoal(this));
                this.setVariant(this.getRandom().nextInt(3));

    }

    @Override
    public SoundEvent getSoundForAnimation(Animation animation)
    {
        switch (EntityAnimation.getAnimation(animation))
        {
            case SPEAK:
                return SoundHandler.STYRACOSAURUS_LIVING;
            case DYING:
                return SoundHandler.STYRACOSAURUS_DEATH;
            case INJURED:
                return SoundHandler.STYRACOSAURUS_HURT;
            case CALLING:
                return SoundHandler.STYRACOSAURUS_CALL;
            case ROARING:
                return SoundHandler.STYRACOSAURUS_ROAR;
            case BEGGING:
                return SoundHandler.STYRACOSAURUS_THREAT;
        }

        return null;
    }

    @Override
    public void aiStep() {
        double distance2 = 18.0D;
        Entity entityFound2 = null;
        double d4 = -1.0D;
//        for (Entity currE : this.world.loadedEntityList) { again, how ***much*** iteration over this??? no wonder it took a robust device to run this mod good greif - gamma_02
//            if (currE instanceof StyracosaurusEntity) {
//                double d5 = currE.getDistanceSq(this.getX(), this.posY, this.posZ);
//                if ((d5 < distance2 * distance2) && (d4 == -1.0D || d5 < d4)) {
//                    d4 = d5;
//                    entityFound2 = currE;
//                }
//            }
//        }
        for(Entity e : this.level().getEntitiesOfClass(StyracosaurusEntity.class, this.getBoundingBox().inflate(distance2*distance2))){
            if(e.distanceTo(this) < distance2 * distance2){
                entityFound2 = e;
                break;
            }
        }
        if (entityFound2 != null) {
            if (!isKingSet) {
                king = ((StyracosaurusEntity) entityFound2);
                king.isKing = true;
                isKingSet = true;
            }
        }
        if(king == null){
            isKingSet = false;
        }
        if(!isKing && isKingSet) {
            moveControl.setWantedPosition(king.getX(), king.getY(), king.getZ(), 1.0D);
        }
        super.aiStep();
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
            case 0: default: return texture("black");
            case 1: return texture("gray");
            case 2: return texture("jpog");
        }
    }
    private ResourceLocation texture(String variant){
        String formattedName = this.dinosaur.getName().toLowerCase(Locale.ENGLISH).replaceAll(" ", "_");
        String baseTextures = "textures/entities/" + formattedName + "/";
        String texture = baseTextures + formattedName;
        return isMale()?ResourceLocation.parse(JurassicReborn.MODID + ":" + texture + "_male_" + "adult" + "_" + variant + ".png"):ResourceLocation.parse(JurassicReborn.MODID + ":" + texture + "_female_" + "adult" + "_" + variant +".png");
    }
}
