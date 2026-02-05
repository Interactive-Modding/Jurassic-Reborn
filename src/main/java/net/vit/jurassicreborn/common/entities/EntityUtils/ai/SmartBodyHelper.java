package net.vit.jurassicreborn.common.entities.EntityUtils.ai;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.vit.jurassicreborn.common.entities.DinosaurEntity;

public class SmartBodyHelper extends BodyRotationControl {
    private static final float MAX_ROTATE   = 75.0F;
    private static final int   HISTORY_SIZE = 10;

    private final Mob entity;

    private int   rotateTime;
    private float targetYawHead;

    private final double[] histPosX = new double[HISTORY_SIZE];
    private final double[] histPosZ = new double[HISTORY_SIZE];

    public SmartBodyHelper(Mob entity) {
        super(entity);
        this.entity = entity;
    }

    @Override
    public void clientTick() {
        if (this.entity.isDeadOrDying() || (this.entity instanceof DinosaurEntity d && d.isCarcass())) {
            return;
        }

        // push history back one, record current position
        System.arraycopy(this.histPosX, 0, this.histPosX, 1, this.histPosX.length - 1);
        System.arraycopy(this.histPosZ, 0, this.histPosZ, 1, this.histPosZ.length - 1);
        this.histPosX[0] = this.entity.getX();
        this.histPosZ[0] = this.entity.getZ();

        double dx = delta(this.histPosX);
        double dz = delta(this.histPosZ);
        double distSq = dx * dx + dz * dz;

        if (distSq > 2.5e-7) {
            // entity is moving → align body toward travel direction
            float moveAngle = (float) (Mth.atan2(dz, dx) * (180.0D / Math.PI)) - 90.0F;

            // Smoothly rotate body toward movement
            this.entity.yBodyRot += Mth.wrapDegrees(moveAngle - this.entity.yBodyRot) * 0.6F;

            // If pathing, nudge facing toward movement as well (ABSOLUTE yaw!)
            if (!this.entity.getNavigation().isDone()) {
                float yawDelta = (float) Mth.wrapDegrees(moveAngle - this.entity.getYRot()) * 0.4F;
                this.entity.setYRot(this.entity.getYRot() + yawDelta);
            }

            // Reset head tracking cooldown baseline
            this.rotateTime = 0;
            this.targetYawHead = this.entity.yHeadRot;
            return;
        }

        // Not moving: try to realign body toward head over time
        if (this.entity.getPassengers().isEmpty() || !(this.entity.getPassengers().get(0) instanceof LivingEntity)) {
            float limit = MAX_ROTATE;

            if (Math.abs(this.entity.yHeadRot - this.targetYawHead) > 15.0F) {
                // head moved abruptly → reset timer and target baseline
                this.rotateTime = 0;
                this.targetYawHead = this.entity.yHeadRot;
            } else {
                // Gradually reduce the max rotation as we linger
                this.rotateTime++;
                final int speed = 30;
                if (this.rotateTime > speed) {
                    float t = Math.max(1.0F - (this.rotateTime - speed) / (float) speed, 0.0F);
                    limit = t * MAX_ROTATE;
                }
            }

            this.entity.yBodyRot = approach(this.entity.yHeadRot, this.entity.yBodyRot, limit);
        }
    }

    private float approach(float target, float current, float limit) {
        float delta = Mth.wrapDegrees(target - current);
        if (delta < -limit) delta = -limit;
        else if (delta > limit) delta = limit;
        // Ease-in toward target
        return target - delta * 0.67F; // equivalent to current + 0.33*(target-current) after clamping
    }

    private double delta(double[] arr) {
        // difference between means of recent half vs. older half
        return mean(arr, 0) - mean(arr, HISTORY_SIZE / 2);
    }

    private double mean(double[] arr, int start) {
        double sum = 0.0;
        int half = HISTORY_SIZE / 2;
        for (int i = 0; i < half; i++) {
            sum += arr[i + start];
        }
        return sum / half; // <-- correct divisor
    }
}
