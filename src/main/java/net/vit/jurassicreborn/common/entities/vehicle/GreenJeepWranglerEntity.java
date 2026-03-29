package net.vit.jurassicreborn.common.entities.vehicle;


import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.vit.jurassicreborn.common.items.ModItems;

public class GreenJeepWranglerEntity extends VehicleEntity {

    public GreenJeepWranglerEntity(EntityType<? extends VehicleEntity> type, Level level) {

        super(type, level);
        this.speedModifier = 0f;
    }

        @Override
        protected void dropFromLootTable ( boolean causedByPlayer){
            if (!level().isClientSide) spawnAtLocation(ModItems.GREEN_JEEP_WRANGLER.get());
        }

    @Override
    protected void doBlockCollisions() {

    }

    @Override
        protected Seat[] createSeats () {
            Seat frontLeft = new Seat(0.563F, -0.15F, 0.0F, 0.5F, 0.25F);
            Seat frontRight = new Seat(-0.563F, -0.15F, 0.0F, 0.5F, 0.25F);
            Seat backLeft = new Seat(0.5F, 0.18F, -2.2F, 0.4F, 0.25F);
            Seat backRight = new Seat(-0.5F, 0.18F, -2.2F, 0.4F, 0.25F);
            return new Seat[]{frontLeft, frontRight, backLeft, backRight};
        }
        @Override
        protected WheelData createWheels () {
            return new WheelData(1.2, 1.5, -1.2, -2.65);
        }

    @Override
    public void dropItems() {
    }


}