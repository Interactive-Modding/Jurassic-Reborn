package net.vit.jurassicreborn.common.entities.vehicle;


import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.vit.jurassicreborn.common.items.ModItems;

public class GyrosphereEntity extends VehicleEntity {

    public GyrosphereEntity(EntityType<? extends VehicleEntity> type, Level level) {

        super(type, level);
        this.speedModifier = 0f;
    }

    @Override
    protected void dropFromLootTable ( boolean causedByPlayer){
        if (!level.isClientSide) spawnAtLocation(ModItems.GYROSPHERE.get());
    }

    @Override
    protected void doBlockCollisions() {

    }

    @Override
    protected Seat[] createSeats () {
        Seat frontLeft = new Seat( 0.563F, 0.85F, 0.0F, 0.5F, 0.25F);
        Seat frontRight = new Seat( -0.563F, 0.85F, 0.0F, 0.5F, 0.25F);
        return new Seat[] { frontLeft, frontRight};
    }

    @Override
    protected WheelData createWheels() {
        return new WheelData(0.0000001, 0.0000002, 0.0000003, 0.0000004);
    }

    @Override
    public void dropItems() {
    }

    @Override public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this);
    }
}