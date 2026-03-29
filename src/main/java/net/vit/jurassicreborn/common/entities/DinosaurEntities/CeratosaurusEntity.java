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
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;

import java.util.Locale;
public class CeratosaurusEntity extends DinosaurEntity implements IHasVariants {
    private static final EntityDataAccessor<Integer> VARIANT= SynchedEntityData.defineId(CeratosaurusEntity.class, EntityDataSerializers.INT);

    public CeratosaurusEntity(Level world, EntityType<CeratosaurusEntity> type) {
        super(world, type, DinosaurHandler.CERATOSAURUS);
        this.target(AchillobatorEntity.class, ApatosaurusEntity.class,  CamarasaurusEntity.class, AnkylodocusEntity.class, DiplodocusEntity.class, TitanisEntity.class, SmilodonEntity.class, MegatheriumEntity.class, DeinotheriumEntity.class, ArsinoitheriumEntity.class, SinoceratopsEntity.class, AlligatorGarEntity.class, AnkylosaurusEntity.class, AlvarezsaurusEntity.class, BaryonyxEntity.class, BeelzebufoEntity.class, CarcharodontosaurusEntity.class, CarnotaurusEntity.class, VelociraptorEntity.class, VelociraptorBlueEntity.class, VelociraptorCharlieEntity.class, VelociraptorDeltaEntity.class, VelociraptorEchoEntity.class, ChasmosaurusEntity.class, NigersaurusEntity.class, ChilesaurusEntity.class, KairukuEntity.class, CoelurusEntity.class, CorythosaurusEntity.class, MaiasauraEntity.class, CompsognathusEntity.class, DilophosaurusEntity.class, DimorphodonEntity.class, DodoEntity.class, EdmontosaurusEntity.class, GallimimusEntity.class, HerrerasaurusEntity.class, HyaenodonEntity.class, HypsilophodonEntity.class, LambeosaurusEntity.class, LeaellynasauraEntity.class, LeptictidiumEntity.class, LudodactylusEntity.class, MajungasaurusEntity.class, MammothEntity.class, MetriacanthosaurusEntity.class, MicroceratusEntity.class, MicroraptorEntity.class, MoganopterusEntity.class, MussaurusEntity.class, OrnithomimusEntity.class, OthnieliaEntity.class, OviraptorEntity.class, PachycephalosaurusEntity.class, ParasaurolophusEntity.class, PostosuchusEntity.class, DeinosuchusEntity.class, ProceratosaurusEntity.class, ProtoceratopsEntity.class, PteranodonEntity.class, RugopsEntity.class, SegisaurusEntity.class, StegosaurusEntity.class, StyracosaurusEntity.class, TherizinosaurusEntity.class, TriceratopsEntity.class, TroodonEntity.class, TropeognathusEntity.class, ZhenyuanopterusEntity.class, Player.class
, Animal.class, Villager.class);
        this.setVariant(this.getRandom().nextInt(5));
    }



    @Override
    public SoundEvent getSoundForAnimation(Animation animation) {
        switch (EntityAnimation.getAnimation(animation)) {
            case SPEAK:
                return SoundHandler.CERATOSAURUS_LIVING;
            case DYING:
                return SoundHandler.CERATOSAURUS_DEATH;
            case INJURED:
                return SoundHandler.CERATOSAURUS_HURT;
            case CALLING:
                return SoundHandler.CERATOSAURUS_CALL;
            case ROARING:
                return SoundHandler.CERATOSAURUS_ROAR;
            case BEGGING:
                return SoundHandler.CERATOSAURUS_THREAT;
        }

        return null;
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
            case 0: default: return texture("purple");
            case 1: return texture("red");
            case 2: return texture("brown");
            case 3: return texture("orange");
            case 4: return texture("blue");
        }
    }
    private ResourceLocation texture(String variant){
        String formattedName = this.dinosaur.getName().toLowerCase(Locale.ENGLISH).replaceAll(" ", "_");
        String baseTextures = "textures/entities/" + formattedName + "/";
        String texture = baseTextures + formattedName;
        return isMale()?ResourceLocation.parse(JurassicReborn.MODID + ":" + texture + "_male_" + "adult" + "_" + variant + ".png"):ResourceLocation.parse(JurassicReborn.MODID + ":" + texture + "_female_" + "adult" + "_" + variant +".png");
    }

}
