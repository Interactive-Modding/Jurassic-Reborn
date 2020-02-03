package mod.reborn.server.entity.vehicle;

import net.minecraft.entity.Entity;

import javax.annotation.Nullable;

public interface MultiSeatedEntity {
    boolean tryPutInSeat(Entity passenger, int seatID, boolean isPacket);

    @Nullable Entity getEntityInSeat(int seatID);

    int getSeatForEntity(Entity entity);
}