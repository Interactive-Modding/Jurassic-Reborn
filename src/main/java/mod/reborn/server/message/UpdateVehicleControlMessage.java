package mod.reborn.server.message;

import mod.reborn.server.entity.vehicle.VehicleEntity;

import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.server.network.AbstractMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class UpdateVehicleControlMessage extends AbstractMessage<UpdateVehicleControlMessage>
{
    private int entityId;

    private byte state;

    public UpdateVehicleControlMessage()
    {}

    public UpdateVehicleControlMessage(VehicleEntity entity)
    {
        this.entityId = entity.getEntityId();
        this.state = entity.getControlState();
    }

    @Override
    public void onClientReceived(Minecraft minecraft, UpdateVehicleControlMessage message, EntityPlayer player, MessageContext context)
    {
    	
    }

    @Override
    public void onServerReceived(MinecraftServer server, UpdateVehicleControlMessage message, EntityPlayer player, MessageContext context)
    {
        Entity entity = player.world.getEntityByID(message.entityId);
        if (entity instanceof VehicleEntity)
        {
            VehicleEntity car = (VehicleEntity) entity;
            if (car.getControllingPassenger() == player)
            {
                car.setControlState(message.state);
            }
        }
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.entityId = buf.readInt();
        this.state = buf.readByte();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.entityId);
        buf.writeByte(this.state);

    }
}
