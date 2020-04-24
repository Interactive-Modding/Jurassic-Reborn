package mod.reborn.server.message;

import io.netty.buffer.ByteBuf;
import mod.reborn.RebornMod;
import mod.reborn.server.entity.vehicle.VehicleEntity;
import mod.reborn.server.entity.vehicle.MultiSeatedEntity;
import net.ilexiconn.llibrary.server.network.AbstractMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class AttemptMoveToSeatMessage extends AbstractMessage<AttemptMoveToSeatMessage> {

    private int fromSeat;
    private int toSeat;
    private int entityID;

    public AttemptMoveToSeatMessage(){}

    public AttemptMoveToSeatMessage(Entity entity, int fromSeat, int toSeat){
        this.toSeat = toSeat;
        this.entityID = entity.getEntityId();
        this.fromSeat = fromSeat;
    }

    private AttemptMoveToSeatMessage(AttemptMoveToSeatMessage message) {
        this.toSeat = message.toSeat;
        this.fromSeat = message.fromSeat;
        this.entityID = message.entityID;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(toSeat);
        buf.writeInt(fromSeat);
        buf.writeInt(entityID);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        toSeat = buf.readInt();
        fromSeat = buf.readInt();
        entityID = buf.readInt();

    }

    @Override
    public void onClientReceived(Minecraft client, AttemptMoveToSeatMessage message, EntityPlayer player, MessageContext messageContext) {
        putEntityInSeat(player.world, message, player);
    }

    @Override
    public void onServerReceived(MinecraftServer server, AttemptMoveToSeatMessage message, EntityPlayer player, MessageContext messageContext) {
        if(putEntityInSeat(player.world, message, player)) {
            RebornMod.NETWORK_WRAPPER.sendToDimension(new AttemptMoveToSeatMessage(message), player.dimension);
        }
    }

    private boolean putEntityInSeat(World world, AttemptMoveToSeatMessage message, EntityPlayer player) {
        Entity entity = world.getEntityByID(message.entityID);
        if(entity instanceof MultiSeatedEntity) {
            MultiSeatedEntity multiSeatedEntity = ((MultiSeatedEntity)entity);
            VehicleEntity e = (VehicleEntity) entity;
            multiSeatedEntity.tryPutInSeat(world.getEntityByID(player.getEntityId()), message.toSeat, true);
            return true;
        }
        return false;
    }
}