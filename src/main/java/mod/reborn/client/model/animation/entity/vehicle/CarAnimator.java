package mod.reborn.client.model.animation.entity.vehicle;

import com.google.common.collect.Lists;
import mod.reborn.client.proxy.ClientProxy;
import mod.reborn.server.entity.vehicle.VehicleEntity;
import net.ilexiconn.llibrary.client.model.tabula.ITabulaModelAnimator;
import net.ilexiconn.llibrary.client.model.tabula.TabulaModel;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import mod.reborn.server.entity.ai.util.InterpValue;
import mod.reborn.server.entity.vehicle.HelicopterEntity;

import java.util.List;

public class CarAnimator implements ITabulaModelAnimator<VehicleEntity> {
    private final List<CarAnimator.Door> doorList = Lists.newArrayList();
    public float partialTicks;

    public CarAnimator addDoor(Door door) {
        this.doorList.add(door);
        return this;
    }

    @Override
    public void setRotationAngles(TabulaModel model, VehicleEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float rotationYaw, float rotationPitch, float scale) {
        if(!(entity instanceof HelicopterEntity)) {
            doorList.forEach(door -> {
                InterpValue value = door.getInterpValue(entity);
                VehicleEntity.Seat seat = door.getSeat(entity);
                VehicleEntity.Seat closestSeat = seat;
                EntityPlayer player = ClientProxy.MC.player;
                Vec3d playerPos = player.getPositionVector();
                for (Door door1 : this.doorList) {
                    if (door1.getSeat(entity).getPos().distanceTo(playerPos) <= closestSeat.getPos().distanceTo(playerPos)) {
                        closestSeat = door1.getSeat(entity);
                    }
                }
                value.setTarget(Math.toRadians(
                        player.isSpectator() || entity.getPassengers().contains(player) || entity.getEntityInSeat(door.getSeatIndex()) != null
                                || closestSeat != seat || closestSeat.getPos().distanceTo(playerPos) > 4D ? 0F
                                : door.isLeft() ? 60F : -60F));
                model.getCube(door.getName()).rotateAngleY = (float) value.getValueForRendering(partialTicks);

            });

            AdvancedModelRenderer wheelHolderFront = model.getCube("wheel holder front");
            AdvancedModelRenderer wheelHolderBack = model.getCube("wheel holder back");

            float wheelRotation = entity.prevWheelRotateAmount + (entity.wheelRotateAmount - entity.prevWheelRotateAmount) * partialTicks;
            float wheelRotationAmount = entity.wheelRotation - entity.wheelRotateAmount * (1.0F - partialTicks);

            if (entity.backward()) {
                wheelRotationAmount = -wheelRotationAmount;
            }

            wheelHolderFront.rotateAngleX = wheelRotationAmount * 0.5F;
            wheelHolderBack.rotateAngleX = wheelRotationAmount * 0.5F;

            entity.steerAmount.setTarget(Math.toRadians(entity.left() ? 40.0F : entity.right() ? -40.0F : 0.0F) * wheelRotation);

            float steerAmount = (float) entity.steerAmount.getValueForRendering(partialTicks);

            model.getCube("steering wheel main").rotateAngleZ = steerAmount;
            wheelHolderFront.rotateAngleY = -steerAmount * 0.15F;
        }

    }

    public static class Door {
        private final String name;
        private final int seatIndex;
        private final boolean isLeft;

        public Door(String name, int seatIndex, boolean isLeft) {
            this.name = name;
            this.seatIndex = seatIndex;
            this.isLeft = isLeft;
        }

        public InterpValue getInterpValue(VehicleEntity entity) {
            return getSeat(entity).getInterpValue();
        }

        public String getName() {
            return name;
        }

        public int getSeatIndex() {
            return this.seatIndex;
        }

        public VehicleEntity.Seat getSeat(VehicleEntity entity) {
            return entity.getSeat(seatIndex);
        }

        public boolean isLeft() {
            return isLeft;
        }

    }
}