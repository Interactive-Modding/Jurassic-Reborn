package mod.reborn.server.entity.ai;

import mod.reborn.server.entity.DinosaurEntity;
import net.minecraft.entity.EntityBodyHelper;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.math.MathHelper;

/**
 * @author pau101
 */
public class SmartBodyHelper extends EntityBodyHelper {
    private static final float MAX_ROTATE = 75;

    private static final int HISTORY_SIZE = 10;

    private final EntityLiving entity;

    private int rotateTime;

    private float targetYawHead;

    private double[] histPosX = new double[HISTORY_SIZE];

    private double[] histPosZ = new double[HISTORY_SIZE];

    public SmartBodyHelper(EntityLiving entity) {
        super(entity);
        this.entity = entity;
    }

    @Override
    public void updateRenderAngles() {
        if (!this.entity.isDead && !(this.entity instanceof DinosaurEntity && ((DinosaurEntity) this.entity).isCarcass())) {
            System.arraycopy(this.histPosX, 0, this.histPosX, 1, this.histPosX.length - 1);
            System.arraycopy(this.histPosZ, 0, this.histPosZ, 1, this.histPosZ.length - 1);
            this.histPosX[0] = this.entity.posX;
            this.histPosZ[0] = this.entity.posZ;
            double dx = this.delta(this.histPosX);
            double dz = this.delta(this.histPosZ);
            double distSq = dx * dx + dz * dz;
            double moveAngle;
            if (distSq > 2.5e-7) {
                moveAngle = (float) MathHelper.atan2(dz, dx) * (180 / (float) Math.PI) - 90;
                this.entity.renderYawOffset += MathHelper.wrapDegrees(moveAngle - this.entity.renderYawOffset) * 0.6F;
                if (!this.entity.getNavigator().noPath()) {
                    this.entity.rotationYaw += MathHelper.wrapDegrees(moveAngle - this.entity.rotationYaw) * 0.4F;
                }
            } else if (this.entity.getPassengers().isEmpty() || !(this.entity.getPassengers().get(0) instanceof EntityLiving)) {
                float limit = MAX_ROTATE;
                if (Math.abs(this.entity.rotationYawHead - this.targetYawHead) > 15) {
                    this.rotateTime = 0;
                    this.targetYawHead = this.entity.rotationYawHead;
                } else {
                    this.rotateTime++;
                    final int speed = 30;
                    if (this.rotateTime > speed) {
                        limit = Math.max(1 - (this.rotateTime - speed) / (float) speed, 0) * MAX_ROTATE;
                    }
                }
                this.entity.renderYawOffset = this.approach(this.entity.rotationYawHead, this.entity.renderYawOffset, limit);
            }
        }
    }

    private float approach(float target, float current, float limit) {
        float delta = MathHelper.wrapDegrees(target - current);
        if (delta < -limit) {
            delta = -limit;
        } else if (delta >= limit) {
            delta = limit;
        }
        return target - delta * 0.67F;
    }

    private double delta(double[] arr) {
        return this.mean(arr, 0) - this.mean(arr, HISTORY_SIZE / 2);
    }

    private double mean(double[] arr, int start) {
        double mean = 0;
        for (int i = 0; i < HISTORY_SIZE / 2; i++) {
            mean += arr[i + start];
        }
        return mean / arr.length;
    }
}