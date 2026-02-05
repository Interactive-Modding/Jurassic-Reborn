package net.vit.jurassicreborn.common.entities.vehicle;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.vit.jurassicreborn.common.items.ModItems;

/**
 * Minimal concrete helicopter so the abstract base can be registered.
 */
public class HelicopterBaseEntity extends HelicopterEntity {

    public HelicopterBaseEntity(EntityType<? extends VehicleEntity> type, Level level) {
        super(level, 4.0F, 3.0F, 4.0F, 1500, 200, 1000, 5);
    }

    @Override
    protected void dropFromLootTable(boolean causedByPlayer) {
        if (!level.isClientSide) spawnAtLocation(ModItems.HELICOPTER.get());
    }

    @Override
    protected Seat[] createSeats() {
        Seat middle = new Seat(0F, -0.23F, 1.2F, 0.5F, 0.25F);
        Seat frontLeft = new Seat(-0.55F, -0.34F, 0.1F, 0.5F, 0.25F);
        Seat frontRight = new Seat(0.55F, -0.34F, 0.1F, 0.5F, 0.25F);
        Seat backLeft = new Seat(0.4F, 0.25F, -1F, 0.5F, 0.25F);
        Seat backReft = new Seat(-0.4F, 0.25F, -1F, 0.5F, 0.25F);
        return new Seat[] { middle, frontLeft, frontRight, backLeft, backReft };
    }

    @Override
    protected WheelData createWheels() {
        return new WheelData(1, 2, -1, -2.2);
    }

    @Override
    public void dropItems() {
        super.dropItems();
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this);
    }
}