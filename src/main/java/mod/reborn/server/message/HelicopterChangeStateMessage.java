package mod.reborn.server.message;

import mod.reborn.server.entity.vehicle.FordExplorerEntity;
import mod.reborn.server.entity.vehicle.HelicopterEntity;

import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.server.network.AbstractMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class HelicopterChangeStateMessage extends AbstractMessage<HelicopterChangeStateMessage>
{
    private int entityId;

    private boolean onRails;

    public HelicopterChangeStateMessage()
    {}

    public HelicopterChangeStateMessage(HelicopterEntity entity)
    {
        this.entityId = entity.getEntityId();
    }

    @Override
    public void onClientReceived(Minecraft minecraft, HelicopterChangeStateMessage message, EntityPlayer player, MessageContext context)
    {
        Entity entity = player.world.getEntityByID(message.entityId);
        if (entity instanceof FordExplorerEntity)
        {
            FordExplorerEntity car = (FordExplorerEntity) entity;
            car.onRails = message.onRails;
        }
    }

    @Override
    public void onServerReceived(MinecraftServer server, HelicopterChangeStateMessage message, EntityPlayer player, MessageContext context)
    {}

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.entityId = buf.readInt();
        this.onRails = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.entityId);
        buf.writeBoolean(this.onRails);
    }
}