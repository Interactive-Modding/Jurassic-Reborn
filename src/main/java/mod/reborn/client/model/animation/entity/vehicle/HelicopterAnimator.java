package mod.reborn.client.model.animation.entity.vehicle;

import mod.reborn.server.entity.ai.util.MathUtils;
import net.ilexiconn.llibrary.client.model.tabula.ITabulaModelAnimator;
import net.ilexiconn.llibrary.client.model.tabula.TabulaModel;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import mod.reborn.server.entity.vehicle.HelicopterEntity;

public class HelicopterAnimator implements ITabulaModelAnimator<HelicopterEntity> {

    @Override
    public void setRotationAngles(TabulaModel model, HelicopterEntity entity, float f, float f1, float rotation, float rotationYaw, float rotationPitch, float partialTicks) {
        AdvancedModelRenderer rotor = model.getCube("rotorbase_rotatehere");
        AdvancedModelRenderer tailrotor = model.getCube("tailrotor_rotatehere");

        tailrotor.rotateAngleX  = rotor.rotateAngleY = (float) (entity.previousRotAmount + (entity.rotAmount - entity.previousRotAmount) * partialTicks);
        AdvancedModelRenderer ctrl1 = model.getCube("controlstick1");
        AdvancedModelRenderer ctrl2 = model.getCube("controlstick2");
        AdvancedModelRenderer gearL1 = model.getCube("gearL1");
        AdvancedModelRenderer gearL2 = model.getCube("gearL2");
        AdvancedModelRenderer gearR1 = model.getCube("gearR1");
        AdvancedModelRenderer gearR2 = model.getCube("gearR2");
        AdvancedModelRenderer gearFront = model.getCube("gearFront1");
        AdvancedModelRenderer exhaustL = model.getCube("body79");
        AdvancedModelRenderer exhaustR = model.getCube("body80");

        // if (entity.isEngineRunning()) {
        // ctrl1.offsetY = 0.01F;
        // ctrl2.offsetY = -0.01F;
        // }
        if (entity.isFlying) {
            gearL1.offsetY = entity.gearLift;
            gearR1.offsetY = entity.gearLift;
            gearFront.offsetY = entity.gearLift;
        }
        ctrl1.rotateAngleX = (float) Math.toRadians(entity.interpRotationPitch.getValueForRendering(partialTicks) * -1F);
        ctrl1.rotateAngleZ = (float) Math.toRadians(entity.interpRotationRoll.getValueForRendering(partialTicks) * 1F);

        float angleX = -15 + (entity.pitch / 25) * 15;
        if (entity.pitch > 25) {
            angleX = 0;
        } else if (entity.pitch < -25) {
            angleX = -30;
        }
        float angleZ = (entity.roll / 20) * 15;
        if (entity.roll > 20) {
            angleZ = 15;
        } else if (entity.roll < -20) {
            angleZ = -15;
        }
        ctrl1.rotateAngleX = (float) Math.toRadians(angleX);
        ctrl1.rotateAngleZ = (float) Math.toRadians(angleZ);
    }
}