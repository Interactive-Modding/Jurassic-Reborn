package mod.reborn.server.message;

import io.netty.buffer.ByteBuf;
import mod.reborn.RebornMod;
import mod.reborn.server.entity.DinosaurEntity;
import net.ilexiconn.llibrary.server.network.AbstractMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class BiPacketOrder extends AbstractMessage<BiPacketOrder> {
    private int entityId;
    private DinosaurEntity.Order order;

    public BiPacketOrder() {
    }

    public BiPacketOrder(DinosaurEntity entity) {
        this.entityId = entity.getEntityId();
        this.order = DinosaurEntity.Order.SIT;
    }

    @Override
    public void onClientReceived(Minecraft minecraft, BiPacketOrder message, EntityPlayer player, MessageContext messageContext) {
    	
    	DinosaurEntity entity = (DinosaurEntity) player.world.getEntityByID(message.entityId);
       	entity.setFieldOrder(message.order);
        RebornMod.PROXY.openOrder(entity);
    	
    }

    @Override
    public void onServerReceived(MinecraftServer server, BiPacketOrder message, EntityPlayer player, MessageContext messageContext) {
    	 Entity entity = player.getEntityWorld().getEntityByID(message.entityId);

         if (entity instanceof DinosaurEntity) {
             DinosaurEntity dinosaur = (DinosaurEntity) entity;

             if (dinosaur.getOwner() != null && dinosaur.getOwner().equals(player.getUniqueID())) {
                 message.order = dinosaur.getOrder();
             }
         }
    	RebornMod.NETWORK_WRAPPER.sendTo(message, (EntityPlayerMP) player);
    	
    	
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.entityId = buf.readInt();
        this.order = DinosaurEntity.Order.values()[Math.max(0, buf.readByte() % DinosaurEntity.Order.values().length)];
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.entityId);
        buf.writeByte(this.order.ordinal());
    }
}