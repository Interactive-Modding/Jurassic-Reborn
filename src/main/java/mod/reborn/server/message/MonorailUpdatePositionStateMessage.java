package mod.reborn.server.message;


import io.netty.buffer.ByteBuf;
import mod.reborn.server.entity.vehicle.MonorailEntity;
import net.ilexiconn.llibrary.server.network.AbstractMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MonorailUpdatePositionStateMessage extends AbstractMessage<MonorailUpdatePositionStateMessage>
{
    private int entityId;
    
    private long position;

    public MonorailUpdatePositionStateMessage()
    {}

    public MonorailUpdatePositionStateMessage(MonorailEntity entity, BlockPos railPos)
    {
        this.entityId = entity.getEntityId();
        this.position = railPos.toLong();
    }

    @Override
    public void onClientReceived(Minecraft minecraft, MonorailUpdatePositionStateMessage message, EntityPlayer player, MessageContext context)
    {
	Entity entity = player.world.getEntityByID(message.entityId);
        if (entity instanceof MonorailEntity)
        {
            MonorailEntity car = (MonorailEntity) entity;
            BlockPos prevRails = car.railTracks;
            car.railTracks = BlockPos.fromLong(message.position);
            car.prevPos = prevRails;
        }
    }

    @Override
    public void onServerReceived(MinecraftServer server, MonorailUpdatePositionStateMessage message, EntityPlayer player, MessageContext context)
    {}

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.entityId = buf.readInt();
        this.position = buf.readLong();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.entityId);
        buf.writeLong(this.position);
    }
}