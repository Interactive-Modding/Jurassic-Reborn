package mod.reborn.server.entity.ai;

import mod.reborn.server.entity.DinosaurEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityLookHelper;
import net.minecraft.util.math.MathHelper;

public class DinosaurLookHelper extends EntityLookHelper {
    private DinosaurEntity dinosaur;
    private float deltaLookYaw;
    private float deltaLookPitch;
    private boolean isLooking;
    private double posX;
    private double posY;
    private double posZ;

    public DinosaurLookHelper(DinosaurEntity dinosaur) {
        super(dinosaur);
        this.dinosaur = dinosaur;
    }

    @Override
    public void setLookPositionWithEntity(Entity entity, float deltaYaw, float deltaPitch) {
        if(entity != null) {
            this.posX = entity.posX;
            if (entity instanceof EntityLivingBase) {
                this.posY = entity.posY + (double) entity.getEyeHeight();
            } else {
                this.posY = (entity.getEntityBoundingBox().minY + entity.getEntityBoundingBox().maxY) / 2.0D;
            }
            this.posZ = entity.posZ;
            this.deltaLookYaw = deltaYaw;
            this.deltaLookPitch = deltaPitch;
            this.isLooking = true;
        }
    }

    @Override
    public void setLookPosition(double x, double y, double z, float deltaYaw, float deltaPitch) {
        this.posX = x;
        this.posY = y;
        this.posZ = z;
        this.deltaLookYaw = deltaYaw;
        this.deltaLookPitch = deltaPitch;
        this.isLooking = true;
    }

    @Override
    public void onUpdateLook() {
        this.dinosaur.rotationPitch = 0.0F;
        if (this.isLooking) {
            this.isLooking = false;
            double deltaX = this.posX - this.dinosaur.posX;
            double deltaY = this.posY - (this.dinosaur.posY + (double) this.dinosaur.getEyeHeight());
            double deltaZ = this.posZ - this.dinosaur.posZ;
            double delta = (double) MathHelper.sqrt(deltaX * deltaX + deltaZ * deltaZ);
            float desiredYaw = (float) (MathHelper.atan2(deltaZ, deltaX) * (10D / Math.PI)) - 90.0F;
            float desiredPitch = (float) (-(MathHelper.atan2(deltaY, delta) * (180.0D / Math.PI)));
            this.dinosaur.rotationPitch = this.updateRotation(this.dinosaur.rotationPitch, desiredPitch, this.deltaLookPitch);
            this.dinosaur.rotationYawHead = this.updateRotation(this.dinosaur.rotationYawHead, desiredYaw, this.deltaLookYaw);
        } else {
            this.dinosaur.rotationYawHead = this.updateRotation(this.dinosaur.rotationYawHead, this.dinosaur.renderYawOffset, 10.0F);
        }
    }

    private float updateRotation(float current, float desired, float range) {
        float offset = MathHelper.wrapDegrees(desired - current);
        if (offset > range) {
            offset = range;
        }
        if (offset < -range) {
            offset = -range;
        }
        return MathHelper.wrapDegrees(current + offset);
    }

    @Override
    public boolean getIsLooking() {
        return this.isLooking;
    }

    @Override
    public double getLookPosX() {
        return this.posX;
    }

    @Override
    public double getLookPosY() {
        return this.posY;
    }

    @Override
    public double getLookPosZ() {
        return this.posZ;
    }
}