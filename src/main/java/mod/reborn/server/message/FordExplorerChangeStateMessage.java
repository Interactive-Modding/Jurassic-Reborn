package mod.reborn.server.message;

import mod.reborn.server.entity.vehicle.FordExplorerEntity;

import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.server.network.AbstractMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class FordExplorerChangeStateMessage extends AbstractMessage<FordExplorerChangeStateMessage>
{
    private int entityId;
    
    private boolean onRails;

    public FordExplorerChangeStateMessage()
    {}

    public FordExplorerChangeStateMessage(FordExplorerEntity entity)
    {
        this.entityId = entity.getEntityId();
        this.onRails = entity.onRails;
    }

    @Override
    public void onClientReceived(Minecraft minecraft, FordExplorerChangeStateMessage message, EntityPlayer player, MessageContext context)
    {
	Entity entity = player.world.getEntityByID(message.entityId);
        if (entity instanceof FordExplorerEntity)
        {
            FordExplorerEntity car = (FordExplorerEntity) entity;
            car.onRails = message.onRails;
        }
    }

    @Override
    public void onServerReceived(MinecraftServer server, FordExplorerChangeStateMessage message, EntityPlayer player, MessageContext context)
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