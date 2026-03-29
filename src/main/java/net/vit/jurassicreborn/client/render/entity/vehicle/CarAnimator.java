package net.vit.jurassicreborn.client.render.entity.vehicle;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;

import com.github.alexthe666.citadel.client.model.ITabulaModelAnimator;
import com.github.alexthe666.citadel.client.model.TabulaModel;
import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.phys.Vec3;
import net.vit.jurassicreborn.common.entities.vehicle.InterpValue;
import net.vit.jurassicreborn.common.entities.vehicle.VehicleEntity;

import java.util.List;

/**
 * Handles animated doors / steering wheel / wheels for a Tabula car model.
 * Logic is unchanged – only the 1.19 names & types were applied.
 */
public class CarAnimator implements ITabulaModelAnimator<VehicleEntity> {

    private final List<Door> doors = Lists.newArrayList();
    public  float partialTicks;

    /* --------------------------------------------------------------------- */
    /*  builder                                                              */
    /* --------------------------------------------------------------------- */
    public CarAnimator addDoor(Door d) {
        doors.add(d);
        return this;
    }

    /* --------------------------------------------------------------------- */
    /*  main entry from LLibrary                                             */
    /* --------------------------------------------------------------------- */
    @Override
    public void setRotationAngles(TabulaModel model,
                                  VehicleEntity car,
                                  float limbSwing, float limbSwingAmount,
                                  float ageInTicks, float yaw, float pitch, float scale) {

        /* ––– Helicopter has its own animator – skip ––– */
//        if (car instanceof HelicopterEntity) return;

        /* ---------------- doors ---------------- */
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            Vec3 playerPos = player.position();

            // find which seat is closest to the player _this frame_
            VehicleEntity.Seat closest = doors.stream()
                    .map(d -> d.getSeat(car))
                    .min((s1, s2) ->
                            Double.compare(s1.getPos(car).distanceTo(playerPos),
                                    s2.getPos(car).distanceTo(playerPos)))
                    .orElse(null);

            for (Door d : doors) {
                InterpValue value = d.getInterpValue(car);
                boolean open = !player.isSpectator()
                        && !car.getPassengers().contains(player)
                        && car.getEntityInSeat(d.seatIndex) == null
                        && closest == d.getSeat(car)
                        && closest.getPos(car).distanceTo(playerPos) <= 4.0;

                value.setTarget(Math.toRadians(open
                        ? (d.isLeft ? 60 : -60)
                        : 0));

                model.getCube(d.name).rotateAngleY =
                        (float) value.getValueForRendering(partialTicks);
            }
        }

        /* ---------------- wheels & steering ---------------- */
        AdvancedModelBox wheelHolderFront = model.getCube("wheel holder front");
        AdvancedModelBox wheelHolderBack  = model.getCube("wheel holder back");

        float rot       = car.prevWheelRotateAmount + (car.wheelRotateAmount - car.prevWheelRotateAmount) * partialTicks;
        float rotAmount = car.wheelRotation - car.wheelRotateAmount * (1.0F - partialTicks);

        if (car.backward()) rotAmount = -rotAmount;

        wheelHolderFront.rotateAngleX = rotAmount * 0.5F;
        wheelHolderBack .rotateAngleX = rotAmount * 0.5F;

        /* steering wheel + front-axle yaw */
        car.steerAmount.setTarget(Math.toRadians(
                car.left()  ?  40F :
                        car.right() ? -40F : 0F));

        float steer = (float) car.steerAmount.getValueForRendering(partialTicks);

        model.getCube("steering wheel main").rotateAngleZ = steer;
        wheelHolderFront.rotateAngleY = -steer * 0.15F;
    }

    /* --------------------------------------------------------------------- */
    /*  door helper                                                          */
    /* --------------------------------------------------------------------- */
    public static final class Door {
        private final String name;
        private final int    seatIndex;
        private final boolean isLeft;

        public Door(String boneName, int seatIndex, boolean leftSide) {
            this.name       = boneName;
            this.seatIndex  = seatIndex;
            this.isLeft     = leftSide;
        }

        public InterpValue getInterpValue(VehicleEntity car) { return getSeat(car).getInterpValue(); }
        public VehicleEntity.Seat getSeat(VehicleEntity car) { return car.getSeat(seatIndex); }
    }
}
