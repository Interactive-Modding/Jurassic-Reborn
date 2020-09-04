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
public class IndoraptorEntity extends DinosaurEntity
{
    public IndoraptorEntity(World world)
    {
        super(world);
        this.target(EntityLivingBase.class, EntityPlayer.class);
        doesEatEggs(true);
    }
    @Override
    public SoundEvent getSoundForAnimation(Animation animation) {
        switch (EntityAnimation.getAnimation(animation)) {
            case SPEAK:
                return SoundHandler.INDORAPTOR_LIVING;
            case CALLING:
                return SoundHandler.INDORAPTOR_CALLING;
            case DYING:
                return SoundHandler.INDORAPTOR_DEATH;
            case BEGGING:
                return SoundHandler.INDORAPTOR_THREAT;
            case INJURED:
                return SoundHandler.INDORAPTOR_HURT;
            case ROARING:
                return SoundHandler.INDORAPTOR_ROAR;
            case MATING:
                return SoundHandler.INDORAPTOR_MATING;
            default:
                return null;
        }
    }
    @Override
    public SoundEvent getBreathingSound()
    {
        return SoundHandler.INDORAPTOR_BREATHING;
    }
}
