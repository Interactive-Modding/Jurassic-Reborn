package mod.reborn.server.message;

import io.netty.buffer.ByteBuf;
import mod.reborn.RebornMod;
import mod.reborn.server.entity.dinosaur.MicroraptorEntity;
import net.ilexiconn.llibrary.server.network.AbstractMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Set;

public class MicroraptorDismountMessage extends AbstractMessage<MicroraptorDismountMessage> {
    private int entityId;

    public MicroraptorDismountMessage(int entityId) {
        this.entityId = entityId;
    }

    public MicroraptorDismountMessage() {
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.entityId = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.entityId);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onClientReceived(Minecraft client, MicroraptorDismountMessage message, EntityPlayer player, MessageContext messageContext) {
        Entity entity = client.world.getEntityByID(message.entityId);
        if (entity instanceof MicroraptorEntity) {
            entity.dismountRidingEntity();
        }
    }

    @Override
    public void onServerReceived(MinecraftServer server, MicroraptorDismountMessage message, EntityPlayer player, MessageContext messageContext) {
        Entity entity = player.world.getEntityByID(message.entityId);
        if (entity instanceof MicroraptorEntity) {
            MicroraptorEntity microraptor = (MicroraptorEntity) entity;
            if (microraptor.isOwner(player)) {
                microraptor.dismountRidingEntity();
                if (!player.world.isRemote) {
                    WorldServer worldServer = (WorldServer) player.world;
                    Set<? extends EntityPlayer> trackers = worldServer.getEntityTracker().getTrackingPlayers(microraptor);
                    for (EntityPlayer tracker : trackers) {
                        RebornMod.NETWORK_WRAPPER.sendTo(message, (EntityPlayerMP) tracker);
                    }
                }
            }
        }
    }
}