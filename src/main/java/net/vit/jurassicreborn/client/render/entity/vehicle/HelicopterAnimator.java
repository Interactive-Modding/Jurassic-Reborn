package net.vit.jurassicreborn.client.render.entity.vehicle;


import com.github.alexthe666.citadel.client.model.AdvancedModelBox;

import com.github.alexthe666.citadel.client.model.ITabulaModelAnimator;
import com.github.alexthe666.citadel.client.model.TabulaModel;
import net.vit.jurassicreborn.common.entities.vehicle.HelicopterEntity;

public class HelicopterAnimator implements ITabulaModelAnimator<HelicopterEntity> {

    @Override
    public void setRotationAngles(TabulaModel model, HelicopterEntity entity, float f, float f1, float rotation, float rotationYaw, float rotationPitch, float partialTicks) {
        AdvancedModelBox rotor = model.getCube("rotorbase_rotatehere");
        AdvancedModelBox tailrotor = model.getCube("tailrotor_rotatehere");

        rotor.rotateAngleY = (float) entity.rotAmount;
        tailrotor.rotateAngleX = (float) entity.rotAmount;
        AdvancedModelBox ctrl1 = model.getCube("controlstick1");
        AdvancedModelBox ctrl2 = model.getCube("controlstick2");
        AdvancedModelBox gearL1 = model.getCube("gearL1");
        AdvancedModelBox gearL2 = model.getCube("gearL2");
        AdvancedModelBox gearR1 = model.getCube("gearR1");
        AdvancedModelBox gearR2 = model.getCube("gearR2");
        AdvancedModelBox gearFront = model.getCube("gearFront1");
        AdvancedModelBox exhaustL = model.getCube("body79");
        AdvancedModelBox exhaustR = model.getCube("body80");

        // if (entity.isEngineRunning()) {
        // ctrl1.offsetY = 0.01F;
        // ctrl2.offsetY = -0.01F;
        // }
        if (entity.isFlying) {
            gearL1.offsetY = entity.gearLift;
            gearL2.offsetY = entity.gearLift;
            gearR1.offsetY = entity.gearLift;
            gearR2.offsetY = entity.gearLift;
            gearFront.offsetY = entity.gearLift;
        } else {
            gearL1.offsetY = 0;
            gearL2.offsetY = 0;
            gearR1.offsetY = 0;
            gearR2.offsetY = 0;
            gearFront.offsetY = 0;
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