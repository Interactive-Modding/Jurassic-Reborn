package mod.reborn.server.entity.dinosaur;

import mod.reborn.RebornMod;
import mod.reborn.client.model.animation.EntityAnimation;
import mod.reborn.client.sound.SoundHandler;
import mod.reborn.server.entity.DinosaurEntity;
import mod.reborn.server.entity.ai.DinosaurWanderEntityAI;
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

public class BaryonyxEntity extends DinosaurEntity {

    private static final DataParameter<Integer> VARIANT= EntityDataManager.createKey(BaryonyxEntity.class, DataSerializers.VARINT);

    public BaryonyxEntity(World world) {
        super(world);
        this.target(AchillobatorEntity.class, SpinoraptorEntity.class, TitanisEntity.class, SmilodonEntity.class, MegatheriumEntity.class, ArsinoitheriumEntity.class, SinoceratopsEntity.class, AllosaurusEntity.class, AlvarezsaurusEntity.class, BeelzebufoEntity.class, VelociraptorBlueEntity.class, CarnotaurusEntity.class, CearadactylusEntity.class, CeratosaurusEntity.class, VelociraptorCharlieEntity.class, ChasmosaurusEntity.class, ChilesaurusEntity.class, CoelurusEntity.class, CompsognathusEntity.class, CorythosaurusEntity.class, VelociraptorDeltaEntity.class, DilophosaurusEntity.class, DimorphodonEntity.class, HyaenodonEntity.class, VelociraptorEchoEntity.class, EdmontosaurusEntity.class, GallimimusEntity.class, GiganotosaurusEntity.class, GuanlongEntity.class, HerrerasaurusEntity.class, DodoEntity.class, HypsilophodonEntity.class, EntityPlayer.class, LambeosaurusEntity.class, LeaellynasauraEntity.class, LeptictidiumEntity.class, LudodactylusEntity.class, MajungasaurusEntity.class, MammothEntity.class, MetriacanthosaurusEntity.class, MicroceratusEntity.class, MicroraptorEntity.class, MoganopterusEntity.class, MussaurusEntity.class, OrnithomimusEntity.class, OthnieliaEntity.class, OviraptorEntity.class, PachycephalosaurusEntity.class, ParasaurolophusEntity.class, ProceratosaurusEntity.class, PostosuchusEntity.class, ProtoceratopsEntity.class, PteranodonEntity.class, QuetzalEntity.class, RugopsEntity.class, SegisaurusEntity.class, StegosaurusEntity.class, StyracosaurusEntity.class, TherizinosaurusEntity.class, TriceratopsEntity.class, TroodonEntity.class, TropeognathusEntity.class, TyrannosaurusEntity.class, VelociraptorEntity.class, ZhenyuanopterusEntity.class, CoelacanthEntity.class, MegapiranhaEntity.class, AlligatorGarEntity.class, EntityAnimal.class, EntityVillager.class);
        doesEatEggs(true);
        this.setVariant(this.getRNG().nextInt(5));
    }

    public SoundEvent getSoundForAnimation(Animation animation)
    {
        switch (EntityAnimation.getAnimation(animation))
        {
            case SPEAK:
                return SoundHandler.BARYONYX_LIVING;
            case DYING:
                return SoundHandler.BARYONYX_DEATH;
            case INJURED:
                return SoundHandler.BARYONYX_HURT;
            case CALLING:
                return SoundHandler.BARYONYX_CALL;
            case ROARING:
                return SoundHandler.BARYONYX_ROAR;
            case BEGGING:
                return SoundHandler.BARYONYX_THREAT;
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
            case 0: default: return texture("chaos");
            case 1: return texture("green");
            case 2: return texture("grim");
            case 3: return texture("jw");
            case 4: return texture("limbo");
        }
    }
    private ResourceLocation texture(String variant){
        String formattedName = this.dinosaur.getName().toLowerCase(Locale.ENGLISH).replaceAll(" ", "_");
        String baseTextures = "textures/entities/" + formattedName + "/";
        String texture = baseTextures + formattedName;
        return isMale()?new ResourceLocation(RebornMod.MODID, texture + "_male_" + "adult" + "_" + variant + ".png"):new ResourceLocation(RebornMod.MODID, texture + "_female_" + "adult" + "_" + variant +".png");
    }
}