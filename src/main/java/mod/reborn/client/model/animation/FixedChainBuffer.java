package mod.reborn.client.model.animation;


import mod.reborn.client.proxy.ClientProxy;
import net.ilexiconn.llibrary.LLibrary;
import net.ilexiconn.llibrary.client.util.ClientUtils;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author LLibrary
 * @version 1.0
 */
@SideOnly(Side.CLIENT)
public class FixedChainBuffer {
    private int yawTimer;
    private float yawVariation;
    private float pitchVariation;
    private float prevYawVariation;
    private float prevPitchVariation;
    private float prevPartialTicks;

    /**
     * Calculates the swing amounts for the given entity (Y axis)
     *
     * @param maxAngle       the furthest this ChainBuffer can swing
     * @param bufferTime     the time it takes to swing this buffer in ticks
     * @param angleDecrement the angle to decrement by for each model piece
     * @param entity         the entity with this ChainBuffer
     */
    public void calculateChainSwingBuffer(float maxAngle, int bufferTime, float angleDecrement, float divisor, EntityLivingBase entity) {
        this.prevYawVariation = this.yawVariation;
        if (entity.renderYawOffset != entity.prevRenderYawOffset && MathHelper.abs(this.yawVariation) < maxAngle) {
            this.yawVariation += (entity.prevRenderYawOffset - entity.renderYawOffset) / divisor;
        }
        if (this.yawVariation > 0.7F * angleDecrement) {
            if (this.yawTimer > bufferTime) {
                this.yawVariation -= angleDecrement;
                if (MathHelper.abs(this.yawVariation) < angleDecrement) {
                    this.yawVariation = 0.0F;
                    this.yawTimer = 0;
                }
            } else {
                this.yawTimer++;
            }
        } else if (this.yawVariation < -0.7F * angleDecrement) {
            if (this.yawTimer > bufferTime) {
                this.yawVariation += angleDecrement;
                if (MathHelper.abs(this.yawVariation) < angleDecrement) {
                    this.yawVariation = 0.0F;
                    this.yawTimer = 0;
                }
            } else {
                this.yawTimer++;
            }
        }
    }

    /**
     * Calculates the wave amounts for the given entity (X axis)
     *
     * @param maxAngle       the furthest this ChainBuffer can wave
     * @param bufferTime     the time it takes to wave this buffer in ticks
     * @param angleDecrement the angle to decrement by for each model piece
     * @param entity         the entity with this ChainBuffer
     */
    public void calculateChainSwingBuffer(float maxAngle, int bufferTime, float angleDecrement, EntityLivingBase entity) {
        this.calculateChainSwingBuffer(maxAngle, bufferTime, angleDecrement, 1.0F, entity);
    }

    /**
     * Applies this buffer on the Y axis to the given array of model boxes.
     *
     * @param boxes the box array
     */
    public void applyChainSwingBuffer(ModelRenderer... boxes) {
        float rotateAmount;
        if(ClientProxy.MC.isGamePaused()) {
            rotateAmount = 0.01745329251F * ClientUtils.interpolate(this.prevYawVariation, this.yawVariation, this.prevPartialTicks) / boxes.length;
        }else {
            rotateAmount = 0.01745329251F * ClientUtils.interpolate(this.prevYawVariation, this.yawVariation, LLibrary.PROXY.getPartialTicks()) / boxes.length;
            this.prevPartialTicks = LLibrary.PROXY.getPartialTicks();
        }
        for (ModelRenderer box : boxes) {
            box.rotateAngleY += rotateAmount;
        }
    }

    /**
     * Applies this buffer on the X axis to the given array of model boxes.
     *
     * @param boxes the box array
     */
    public void applyChainWaveBuffer(ModelRenderer... boxes) {
        float rotateAmount = 0.01745329251F * ClientUtils.interpolate(this.prevPitchVariation, this.pitchVariation, LLibrary.PROXY.getPartialTicks()) / boxes.length;
        for (ModelRenderer box : boxes) {
            box.rotateAngleX += rotateAmount;
        }
    }

}
