package mod.reborn.server.entity.dinosaur;

import mod.reborn.RebornMod;
import mod.reborn.client.model.animation.EntityAnimation;
import mod.reborn.client.sound.SoundHandler;
import mod.reborn.server.entity.DinosaurEntity;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import java.util.Locale;

public class MammothEntity extends DinosaurEntity {

    private static final DataParameter<Integer> VARIANT = EntityDataManager.createKey(MammothEntity.class, DataSerializers.VARINT);
    private static boolean isKingSet = false;
    private boolean isKing = false;
    private MammothEntity king = null;

    public MammothEntity(World world) {
        super(world);
        this.setVariant(this.getRNG().nextInt(5));
        this.target(AchillobatorEntity.class, AlvarezsaurusEntity.class);
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
    }

    public void entityInit() {
        super.entityInit();
        this.dataManager.register(VARIANT, 0);
    }

    @Override
    public void onLivingUpdate() {
        double distance2 = 18.0D;
        Entity entityFound2 = null;
        double d4 = -1.0D;
        for (Entity currE : this.world.loadedEntityList) {
            if (currE instanceof MammothEntity) {
                double d5 = currE.getDistanceSq(this.posX, this.posY, this.posZ);
                if ((d5 < distance2 * distance2) && (d4 == -1.0D || d5 < d4)) {
                    d4 = d5;
                    entityFound2 = currE;
                }
            }
        }
        if (entityFound2 != null) {
            if (!isKingSet) {
                king = ((MammothEntity) entityFound2);
                king.isKing = true;
                isKingSet = true;
            }
        }
        if(king == null){
            isKingSet = false;
        }
        if(!isKing && isKingSet) {
            moveHelper.setMoveTo(king.posX, king.posY, king.posZ, 1.0D);
        }
        super.onLivingUpdate();
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
        return isMale()?new ResourceLocation(RebornMod.MODID, texture + "_male_" + variant + ".png"):new ResourceLocation(RebornMod.MODID, texture + "_female_" + variant +".png");
    }
}
