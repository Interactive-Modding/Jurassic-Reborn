package net.vit.jurassicreborn.client.render.entity.animation;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class FixedChainBuffer {
    private int   yawTimer;
    private float yawVariation, prevYawVariation;
    private float pitchVariation, prevPitchVariation;
    private float prevPartialTicks;

    // ensure we only integrate once per *tick* (not every render frame)
    private int lastAppliedTick = -1;

    /**
     * 1.19+ port of the old LLibrary logic.
     * Keeps timer/deadband behavior; computes input once per tick; renders with per-frame interpolation.
     *
     * @param maxAngle    max swing (degrees)
     * @param bufferTime  delay before decay starts (ticks)
     * @param angleDec    decrement per decay step (degrees)
     * @param divisor     scales input delta (higher = softer)
     */
    public void calculateChainSwingBuffer(float maxAngle, int bufferTime, float angleDec, float divisor, LivingEntity entity) {
        // save previous for smooth interpolation during apply*
        this.prevYawVariation = this.yawVariation;

        // Only process the driver once per game tick
        if (entity.tickCount != lastAppliedTick) {
            lastAppliedTick = entity.tickCount;

            // old: (prevRenderYawOffset - renderYawOffset); new: (yBodyRotO - yBodyRot)
            float delta = Mth.wrapDegrees(entity.yBodyRotO - entity.yBodyRot); // wrap like LLibrary avoided 0/360 jumps
            if (delta != 0.0F && Mth.abs(this.yawVariation) < maxAngle) {
                this.yawVariation += delta / (divisor == 0.0F ? 1.0F : divisor);
            }

            // original timer-gated decay (deadband +/- 0.7 * angleDec)
            if (this.yawVariation > 0.7F * angleDec) {
                if (this.yawTimer > bufferTime) {
                    this.yawVariation -= angleDec;
                    if (Mth.abs(this.yawVariation) < angleDec) {
                        this.yawVariation = 0.0F;
                        this.yawTimer = 0;
                    }
                } else {
                    this.yawTimer++;
                }
            } else if (this.yawVariation < -0.7F * angleDec) {
                if (this.yawTimer > bufferTime) {
                    this.yawVariation += angleDec;
                    if (Mth.abs(this.yawVariation) < angleDec) {
                        this.yawVariation = 0.0F;
                        this.yawTimer = 0;
                    }
                } else {
                    this.yawTimer++;
                }
            } else {
                // within deadband → slowly count down timer so it can decay again soon
                if (this.yawTimer > 0) this.yawTimer--;
            }
        }
    }

    public void calculateChainSwingBuffer(float maxAngle, int bufferTime, float angleDec, LivingEntity entity) {
        calculateChainSwingBuffer(maxAngle, bufferTime, angleDec, 1.0F, entity);
    }

    /** Apply on Y (swing) — per-frame interpolation, pause-friendly. */
    public void applyChainSwingBuffer(AdvancedModelBox... boxes) {
        if (boxes == null || boxes.length == 0) return;

        Minecraft mc = Minecraft.getInstance();
        float partial = mc.isPaused() ? this.prevPartialTicks : mc.getFrameTime();
        if (!mc.isPaused()) this.prevPartialTicks = partial;

        float rotateAmount = (float) Math.toRadians(Mth.lerp(partial, this.prevYawVariation, this.yawVariation)) / boxes.length;
        for (AdvancedModelBox box : boxes) {
            box.rotateAngleY += rotateAmount;
        }
    }

    /** Apply on X (wave) — mirrors the 1.12.2 pattern. */
    public void applyChainWaveBuffer(AdvancedModelBox... boxes) {
        if (boxes == null || boxes.length == 0) return;

        float partial = Minecraft.getInstance().getFrameTime();
        float rotateAmount = (float) Math.toRadians(Mth.lerp(partial, this.prevPitchVariation, this.pitchVariation)) / boxes.length;
        for (AdvancedModelBox box : boxes) {
            box.rotateAngleX += rotateAmount;
        }
    }
}
