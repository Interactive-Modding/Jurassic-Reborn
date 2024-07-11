package mod.reborn.server.entity.vehicle;

import mod.reborn.server.item.ItemHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class GyrosphereEntity extends VehicleEntity {
    public GyrosphereEntity(World world) {
        super(world);
        this.getYOffset();
        this.speedModifier = 0f;
    }

    public double getYOffset() {
        return 3.0f;
    }

    @Override
    public void dropItems() {
        this.entityDropItem(new ItemStack(ItemHandler.VEHICLE_ITEM, 1, 11), 0);
    }
    @Override
    protected Seat[] createSeats() {
        Seat frontLeft = new Seat( 0.563F, 0.85F, 0.0F, 0.5F, 0.25F);
        Seat frontRight = new Seat( -0.563F, 0.85F, 0.0F, 0.5F, 0.25F);
        return new Seat[] { frontLeft, frontRight};
    }

    @Override
    protected WheelData createWheels() {
	return new WheelData(0.0000001, 0.0000002, 0.0000003, 0.0000004);
    }

}
