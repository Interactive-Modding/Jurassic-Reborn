//package mod.reborn.client.model.animation.entity.vehicle;
//
//import mod.reborn.server.entity.vehicle.HelicopterEntityNew;
//import net.ilexiconn.llibrary.client.model.tabula.ITabulaModelAnimator;
//import net.ilexiconn.llibrary.client.model.tabula.TabulaModel;
//import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
//
//public class HelicopterNewAnimator implements ITabulaModelAnimator<HelicopterEntityNew> {
//
//
//    @Override
//    public void setRotationAngles(TabulaModel model, HelicopterEntityNew entity, float f, float f1, float rotation, float rotationYaw, float rotationPitch, float partialTicks) {
//        AdvancedModelRenderer rotor = model.getCube("rotorbase_rotatehere");
//        AdvancedModelRenderer tailrotor = model.getCube("tailrotor_rotatehere");
//        entity.rotAmount += (entity.rotorRotationAmount.getCurrent()) / 2D;
//        rotor.rotateAngleY = entity.rotAmount;
//        tailrotor.rotateAngleX = entity.rotAmount;
//
//        AdvancedModelRenderer ctrl1 = model.getCube("controlstick1");
//        AdvancedModelRenderer ctrl2 = model.getCube("controlstick2");
//
//        if(entity.isEngineRunning()) {
//            ctrl1.offsetY = 0.01F;
//            ctrl2.offsetY = -0.01F;
//        }
//
//        ctrl1.rotateAngleX = (float) Math.toRadians(entity.interpRotationPitch.getValueForRendering(partialTicks) * -1F);
//        ctrl1.rotateAngleZ = (float) Math.toRadians(entity.interpRotationRoll.getValueForRendering(partialTicks) * 1F);
//    }
//}