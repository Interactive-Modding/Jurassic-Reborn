package mod.reborn.server.message;

import mod.reborn.server.entity.vehicle.FordExplorerEntity;

import io.netty.buffer.ByteBuf;
import mod.reborn.server.entity.vehicle.FordExplorerSnowEntity;
import net.ilexiconn.llibrary.server.network.AbstractMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class FordExplorerSnowChangeStateMessage extends AbstractMessage<FordExplorerSnowChangeStateMessage>
{
    private int entityId;

    private boolean onRails;

    public FordExplorerSnowChangeStateMessage()
    {}

    public FordExplorerSnowChangeStateMessage(FordExplorerSnowEntity entity)
    {
        this.entityId = entity.getEntityId();
        this.onRails = entity.onRails;
    }

    @Override
    public void onClientReceived(Minecraft minecraft, FordExplorerSnowChangeStateMessage message, EntityPlayer player, MessageContext context)
    {
        Entity entity = player.world.getEntityByID(message.entityId);
        if (entity instanceof FordExplorerSnowEntity)
        {
            FordExplorerSnowEntity car = (FordExplorerSnowEntity) entity;
            car.onRails = message.onRails;
        }
    }

    @Override
    public void onServerReceived(MinecraftServer server, FordExplorerSnowChangeStateMessage message, EntityPlayer player, MessageContext context)
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