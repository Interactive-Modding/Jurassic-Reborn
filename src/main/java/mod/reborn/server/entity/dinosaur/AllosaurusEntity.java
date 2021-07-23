package mod.reborn.server.entity.dinosaur;

import mod.reborn.RebornMod;
import mod.reborn.client.model.animation.EntityAnimation;
import mod.reborn.client.sound.SoundHandler;
import mod.reborn.server.entity.DinosaurEntity;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundEvent;
import net.minecraft.entity.EntityCreature;

import net.minecraft.world.World;
import scala.tools.nsc.doc.model.Class;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ResourceLocation;
import java.util.Locale;

public class AllosaurusEntity extends DinosaurEntity {
    private static final DataParameter<Integer> VARIANT= EntityDataManager.createKey(AllosaurusEntity.class, DataSerializers.VARINT);

    public AllosaurusEntity(World world) {
        super(world);
        this.setVariant(this.getRNG().nextInt(2));
        this.target(AchillobatorEntity.class, AnkylodocusEntity.class, DiplodocusEntity.class, TitanisEntity.class, SmilodonEntity.class, RaphusrexEntity.class, MegatheriumEntity.class, ElasmotheriumEntity.class, ArsinoitheriumEntity.class, DeinotheriumEntity.class, SinoceratopsEntity.class, AlvarezsaurusEntity.class, BaryonyxEntity.class, BeelzebufoEntity.class, VelociraptorBlueEntity.class, ProceratosaurusEntity.class, CarnotaurusEntity.class, CearadactylusEntity.class, CeratosaurusEntity.class, VelociraptorCharlieEntity.class, ChasmosaurusEntity.class, ChilesaurusEntity.class, CompsognathusEntity.class, CorythosaurusEntity.class, VelociraptorDeltaEntity.class, DilophosaurusEntity.class, DimorphodonEntity.class, HyaenodonEntity.class, VelociraptorEchoEntity.class, EdmontosaurusEntity.class, GallimimusEntity.class, GiganotosaurusEntity.class, GuanlongEntity.class, HerrerasaurusEntity.class, DodoEntity.class, HypsilophodonEntity.class, EntityPlayer.class, LambeosaurusEntity.class, LeaellynasauraEntity.class, LeptictidiumEntity.class, SpinoraptorEntity.class, LudodactylusEntity.class, MajungasaurusEntity.class, MammothEntity.class, MetriacanthosaurusEntity.class, MicroceratusEntity.class, MicroraptorEntity.class, MoganopterusEntity.class, MussaurusEntity.class, OrnithomimusEntity.class, OthnieliaEntity.class, OviraptorEntity.class, PachycephalosaurusEntity.class, ParasaurolophusEntity.class, PostosuchusEntity.class, ProtoceratopsEntity.class, PteranodonEntity.class, QuetzalEntity.class, RugopsEntity.class, SegisaurusEntity.class, StegosaurusEntity.class, StyracosaurusEntity.class, TherizinosaurusEntity.class, TriceratopsEntity.class, TroodonEntity.class, TropeognathusEntity.class, TyrannosaurusEntity.class, ZhenyuanopterusEntity.class, EntityAnimal.class, EntityVillager.class);
        doesEatEggs(true);
    }


    @Override
    public SoundEvent getSoundForAnimation(Animation animation) {
        switch (EntityAnimation.getAnimation(animation)) {
            case SPEAK:
                return SoundHandler.ALLOSAURUS_LIVING;
            case CALLING:
                return SoundHandler.ALLOSAURUS_MATE_CALL;
            case DYING:
                return SoundHandler.ALLOSAURUS_DEATH;
            case INJURED:
                return SoundHandler.ALLOSAURUS_HURT;
            case ATTACKING:
                return !isMale()?SoundHandler.ALLOSAURUS_FIGHT_FEMALE:null;
            case ROARING:
                return SoundHandler.ALLOSAURUS_ROAR;
            case BEGGING:
                return SoundHandler.ALLOSAURUS_THREAT;
            default:
                return null;
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
            case 0: default: return texture("arid");
            case 1: return texture("jw2");
        }
    }
    private ResourceLocation texture(String variant){
        String formattedName = this.dinosaur.getName().toLowerCase(Locale.ENGLISH).replaceAll(" ", "_");
        String baseTextures = "textures/entities/" + formattedName + "/";
        String texture = baseTextures + formattedName;
        return isMale()?new ResourceLocation(RebornMod.MODID, texture + "_male_" + "adult" + "_" + variant + ".png"):new ResourceLocation(RebornMod.MODID, texture + "_female_" + "adult" + "_" + variant +".png");
    }
}