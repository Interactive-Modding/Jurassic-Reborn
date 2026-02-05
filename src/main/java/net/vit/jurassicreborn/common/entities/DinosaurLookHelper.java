package net.vit.jurassicreborn.common.entities;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.control.LookControl;
import net.vit.jurassicreborn.common.entities.DinosaurEntity;

/**
 * Smooth head-tracking for dinosaurs (ported from the 1.12 EntityLookHelper),
 * updated for 1.19.2 parchment. Clamps yaw/pitch to the dino's allowed ranges.
 */
public class DinosaurLookHelper extends LookControl {

    private final DinosaurEntity dino;
    private float yawSpeedDeg;
    private float pitchSpeedDeg;
    private boolean hasTarget;
    private double lookX, lookY, lookZ;

    public DinosaurLookHelper(DinosaurEntity dino) {
        super(dino);
        this.dino = dino;
    }

    /* --------------------------------------------------------------------- */
    /*  Targets                                                              */
    /* --------------------------------------------------------------------- */

    @Override
    public void setLookAt(Entity target, float yawSpeed, float pitchSpeed) {
        if (target == null) return;

        this.lookX = target.getX();
        this.lookY = (target instanceof LivingEntity) ? target.getEyeY()
                : (target.getBoundingBox().minY + target.getBoundingBox().maxY) * 0.5D;
        this.lookZ = target.getZ();
        this.yawSpeedDeg   = yawSpeed;
        this.pitchSpeedDeg = pitchSpeed;
        this.hasTarget     = true;
    }

    @Override
    public void setLookAt(double x, double y, double z, float yawSpeed, float pitchSpeed) {
        this.lookX = x;
        this.lookY = y;
        this.lookZ = z;
        this.yawSpeedDeg   = yawSpeed;
        this.pitchSpeedDeg = pitchSpeed;
        this.hasTarget     = true;
    }

    /* --------------------------------------------------------------------- */
    /*  Per-tick update                                                      */
    /* --------------------------------------------------------------------- */

    @Override
    public void tick() {
        if (hasTarget) {
            hasTarget = false;

            // vector from head to target
            double dx = lookX - dino.getX();
            double dy = lookY - dino.getEyeY();
            double dz = lookZ - dino.getZ();
            double horiz = Math.sqrt(dx * dx + dz * dz);

            // desired angles (deg)
            float desiredYaw   = (float)(Mth.atan2(dz, dx) * (180F / Math.PI)) - 90.0F;
            float desiredPitch = (float)(-(Mth.atan2(dy, horiz) * (180F / Math.PI)));

            // rotate head toward target, clamped by per-tick speeds
            float newHeadYaw   = rotateToward(dino.getYHeadRot(), desiredYaw,   yawSpeedDeg);
            float newHeadPitch = rotateToward(dino.getXRot(),     desiredPitch, pitchSpeedDeg);

            // also clamp against the entity's allowed head limits vs body
            float maxHeadYaw   = dino.getMaxHeadYRot(); // how far head can twist from body
            float maxHeadPitch = dino.getMaxHeadXRot(); // up/down limit

            float clampedYaw   = clampHeadYawRelativeToBody(newHeadYaw, dino.yBodyRot, maxHeadYaw);
            float clampedPitch = Mth.clamp(newHeadPitch, -maxHeadPitch, maxHeadPitch);

            dino.setYHeadRot(clampedYaw);
            dino.setXRot(clampedPitch);
            // Let BodyRotationControl handle body follow; don't force yBodyRot here.
        } else {
            // relax head back toward body direction
            float eased = rotateToward(dino.getYHeadRot(), dino.yBodyRot, 10.0F);
            dino.setYHeadRot(eased);
        }
    }

    /* --------------------------------------------------------------------- */
    /*  Helpers                                                              */
    /* --------------------------------------------------------------------- */

    private static float rotateToward(float current, float target, float maxStep) {
        float delta = Mth.wrapDegrees(target - current);
        delta = Mth.clamp(delta, -maxStep, maxStep);
        return current + delta;
    }

    private static float clampHeadYawRelativeToBody(float headYaw, float bodyYaw, float maxDeltaFromBody) {
        float diff = Mth.wrapDegrees(headYaw - bodyYaw);
        diff = Mth.clamp(diff, -maxDeltaFromBody, maxDeltaFromBody);
        return bodyYaw + diff;
    }

    /* --------------------------------------------------------------------- */
    /*  Accessors (match LookControl’s getters)                               */
    /* --------------------------------------------------------------------- */

    @Override public double getWantedX() { return lookX; }
    @Override public double getWantedY() { return lookY; }
    @Override public double getWantedZ() { return lookZ; }
}
