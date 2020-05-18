package mod.reborn.server.entity.dinosaur;

import mod.reborn.client.model.animation.EntityAnimation;
import mod.reborn.client.sound.SoundHandler;
import mod.reborn.server.dinosaur.Dinosaur;
import mod.reborn.server.entity.DinosaurEntity;
import mod.reborn.server.entity.EntityHandler;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

public class IndominusEntity extends DinosaurEntity
{
    private static final DataParameter<Boolean> DATA_WATCHER_IS_CAMOUFLAGING = EntityDataManager.createKey(IndominusEntity.class, DataSerializers.BOOLEAN);
    private float[] newSkinColor = new float[3];
    private float[] skinColor = new float[3];
    private int stepCount = 0;
    private boolean isCamouflaging;
    public IndominusEntity(World world)
    {
        super(world);
        this.target(EntityLivingBase.class);
    }

    @Override
    public void entityInit()
    {
        super.entityInit();

        this.dataManager.register(DATA_WATCHER_IS_CAMOUFLAGING, false);
    }

    @Override
    public void setupDisplay(boolean isMale)
    {
        super.setupDisplay(isMale);
        isCamouflaging = true;
        skinColor = new float[] { 255, 255, 255 };
        newSkinColor = new float[] { 255, 255, 255 };
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();

        if (this.moveForward > 0 && this.stepCount <= 0)
        {
            this.playSound(SoundHandler.STOMP, (float) interpolate(0.1F, 1.0F), this.getSoundPitch());
            stepCount = 65;
        }

        this.stepCount -= this.moveForward * 9.5;

        if (world.isRemote)
        {
            isCamouflaging = this.dataManager.get(DATA_WATCHER_IS_CAMOUFLAGING);
            changeSkinColor();
        }
        else
        {
            this.dataManager.set(DATA_WATCHER_IS_CAMOUFLAGING, isCamouflaging);
        }
    }

    @Override
    public float getSoundVolume()
    {
        return (float) interpolate(0.9F, 1.6F) + ((rand.nextFloat() - 0.5F) * 0.125F);
    }

    public boolean isCamouflaging()
    {
        return isCamouflaging;
    }

    public void changeSkinColor()
    {
        BlockPos pos = new BlockPos(this).offset(EnumFacing.DOWN);
        IBlockState state = this.world.getBlockState(pos);

        int color;

        if (isCamouflaging())
        {
            color = Minecraft.getMinecraft().getBlockColors().colorMultiplier(state, world, pos, 0); //TODO

            if (color == 0xFFFFFF)
            {
                color = state.getMapColor(world, pos).colorValue;
            }
        }
        else
        {
            color = 0xFFFFFF;
        }

        if (color != 0)
        {
            this.newSkinColor[0] = color >> 16 & 255;
            this.newSkinColor[1] = color >> 8 & 255;
            this.newSkinColor[2] = color & 255;

            if (this.skinColor[0] == 0 && this.skinColor[1] == 0 && this.skinColor[2] == 0)
            {
                this.skinColor[0] = this.newSkinColor[0];
                this.skinColor[1] = this.newSkinColor[1];
                this.skinColor[2] = this.newSkinColor[2];
            }
        }

        for (int i = 0; i < 3; ++i)
        {
            if (this.skinColor[i] < this.newSkinColor[i])
            {
                ++this.skinColor[i];
            }

            if (this.skinColor[i] > this.newSkinColor[i])
            {
                --this.skinColor[i];
            }
        }
    }//Alvarezsaurus

    @SideOnly(Side.CLIENT)
    public float[] getSkinColor()
    {
        return new float[] { this.skinColor[0] / 255.0F, this.skinColor[1] / 255.0F, this.skinColor[2] / 255.0F };
    }

    @Override
    public SoundEvent getSoundForAnimation(Animation animation)
    {
        switch (EntityAnimation.getAnimation(animation))
        {
            case SPEAK:
                return SoundHandler.INDOMINUS_LIVING;
            case DYING:
                return SoundHandler.INDOMINUS_DEATH;
            case INJURED:
                return SoundHandler.INDOMINUS_HURT;
            case ROARING:
                return SoundHandler.INDOMINUS_ROAR;
            case CALLING:
                return SoundHandler.INDOMINUS_ROAR;
        }

        return null;
    }

    @Override
    public SoundEvent getBreathingSound()
    {
        return SoundHandler.INDOMINUS_BREATHING;
    }
}
