package net.vit.jurassicreborn.common.entities.DinosaurEntities;

import com.github.alexthe666.citadel.animation.Animation;
import net.vit.jurassicreborn.client.sounds.SoundHandler;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimation;
import net.vit.jurassicreborn.common.entities.DinosaurEntity;
import net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class IndominusEntity extends DinosaurEntity
{
    private static final EntityDataAccessor<Boolean> DATA_WATCHER_IS_CAMOUFLAGING = SynchedEntityData.defineId(IndominusEntity.class, EntityDataSerializers.BOOLEAN);
    private float[] newSkinColor = new float[3];
    private float[] skinColor = new float[3];
    private int stepCount = 0;
    private boolean isCamouflaging;
    public IndominusEntity(Level world, EntityType<IndominusEntity> type)
    {
        super(world, type, DinosaurHandler.INDOMINUS);
        this.target(LivingEntity.class, Player.class
);
    }

    @Override
    public void defineSynchedData()
    {
        super.defineSynchedData();

        this.entityData.define(DATA_WATCHER_IS_CAMOUFLAGING, false);
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
    public void tick()
    {
        super.tick();

        if (this.zza > 0 && this.stepCount <= 0)
        {
            this.playSound(SoundHandler.STOMP, (float) interpolate(0.1F, 1.0F), this.getVoicePitch());
            stepCount = 65;
        }

        this.stepCount -= this.zza * 9.5;

        if (level.isClientSide)
        {
            isCamouflaging = this.entityData.get(DATA_WATCHER_IS_CAMOUFLAGING);
            changeSkinColor();
        }
        else
        {
            this.entityData.set(DATA_WATCHER_IS_CAMOUFLAGING, isCamouflaging);
        }
    }

    @Override
    public float getSoundVolume()
    {
        return (float) interpolate(0.9F, 1.6F) + ((random.nextFloat() - 0.5F) * 0.125F);
    }

    public boolean isCamouflaging()
    {
        return isCamouflaging;
    }

    @OnlyIn(Dist.CLIENT)
    public void changeSkinColor()
    {
        BlockPos pos = new BlockPos(this.getOnPos()).relative(Direction.DOWN);
        BlockState state = this.level.getBlockState(pos);

        int color;

        if (isCamouflaging())
        {
            color = Minecraft.getInstance().getBlockColors().getColor(state, level, pos, 0); /*TODO*/ // uh oh... - gamma_02

            if (color == 0xFFFFFF)
            {
                color = state.getMapColor(level, pos).col;
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

    @OnlyIn(Dist.CLIENT)
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
        }

        return null;
    }

    @Override
    public SoundEvent getBreathingSound()
    {
        return SoundHandler.INDOMINUS_BREATHING;
    }
}

