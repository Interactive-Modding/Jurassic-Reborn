package net.vit.jurassicreborn.common.entities.DinosaurEntities;

import com.github.alexthe666.citadel.animation.Animation;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.level.Level;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimation;
import net.vit.jurassicreborn.client.sounds.SoundHandler;
import net.vit.jurassicreborn.common.entities.DinosaurEntity;
import net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurHandler;
import net.vit.jurassicreborn.common.entities.IHasVariants;
import net.vit.jurassicreborn.common.entities.LegSolverQuadruped;

import java.util.Locale;

public class PatagotitanEntity extends DinosaurEntity implements IHasVariants {
    private static final EntityDataAccessor<Integer> VARIANT= SynchedEntityData.defineId(PatagotitanEntity.class, EntityDataSerializers.INT);

	private int stepCount = 0;

    public LegSolverQuadruped legSolver;

    public PatagotitanEntity(EntityType<PatagotitanEntity> type, Level world) {
        super(world, type, DinosaurHandler.PATAGOTITAN);
        this.setVariant(this.getRandom().nextInt(3));
        this.addTask(1, new HurtByTargetGoal(this));

    }

    @Override
    public void tick() {
        super.tick();
        if (this.onGround() && !this.isInWater()) {
            if (this.zza > 0 && (this.getX() - this.xOld > 0 || this.getZ() - this.zOld > 0) && this.stepCount <= 0) {
                this.playSound(SoundHandler.STOMP, (float) this.interpolate(0.1F, 1.0F), this.getVoicePitch());
                this.stepCount = 65;
            }
            this.stepCount -= this.zza * 9.5;
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
    public SoundEvent getSoundForAnimation(Animation animation) {
        switch (EntityAnimation.getAnimation(animation)) {
            case SPEAK:
                return SoundHandler.PATAGOTITAN_LIVING;
            case CALLING:
                return SoundHandler.PATAGOTITAN_CALLING;
            case DYING:
                return SoundHandler.PATAGOTITAN_DEATH;
            case BEGGING:
                return SoundHandler.PATAGOTITAN_THREAT;
            case INJURED:
                return SoundHandler.PATAGOTITAN_HURT;
            case MATING:
                return SoundHandler.PATAGOTITAN_MATING;
            case WALKING:
                return SoundHandler.STOMP;
            default:
                return null;
        }
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
            case 0: default: return texture("brown");
            case 1: return texture("lbrown");
            case 2: return texture("blue");
        }
    }
    private ResourceLocation texture(String variant){
        String formattedName = this.dinosaur.getName().toLowerCase(Locale.ENGLISH).replaceAll(" ", "_");
        String baseTextures = "textures/entities/" + formattedName + "/";
        String texture = baseTextures + formattedName;
        return isMale()?ResourceLocation.parse(JurassicReborn.MODID + ":" + texture + "_male_" + "adult" + "_" + variant + ".png"):ResourceLocation.parse(JurassicReborn.MODID + ":" + texture + "_female_" + "adult" + "_" + variant +".png");
    }
}
