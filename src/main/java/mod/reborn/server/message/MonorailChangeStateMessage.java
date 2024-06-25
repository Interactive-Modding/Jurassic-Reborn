package mod.reborn.server.message;

import io.netty.buffer.ByteBuf;
import mod.reborn.server.entity.vehicle.MonorailEntity;
import net.ilexiconn.llibrary.server.network.AbstractMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MonorailChangeStateMessage extends AbstractMessage<MonorailChangeStateMessage>
{
    private int entityId;
    
    private boolean onRails;

    public MonorailChangeStateMessage()
    {}

    public MonorailChangeStateMessage(MonorailEntity entity)
    {
        this.entityId = entity.getEntityId();
        this.onRails = entity.onRails;
    }

    @Override
    public void onClientReceived(Minecraft minecraft, MonorailChangeStateMessage message, EntityPlayer player, MessageContext context)
    {
	Entity entity = player.world.getEntityByID(message.entityId);
        if (entity instanceof MonorailEntity)
        {
            MonorailEntity car = (MonorailEntity) entity;
            car.onRails = message.onRails;
        }
    }

    @Override
    public void onServerReceived(MinecraftServer server, MonorailChangeStateMessage message, EntityPlayer player, MessageContext context)
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