package mod.reborn.server.message;

import io.netty.buffer.ByteBuf;
import mod.reborn.RebornMod;
import mod.reborn.server.entity.DinosaurEntity;
import net.ilexiconn.llibrary.server.network.AbstractMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class OpenPaleoPadEntityMessage extends AbstractMessage<OpenPaleoPadEntityMessage> {
    private DinosaurEntity entity;
    private int entityId;
    private boolean flocking;
    private boolean fleeing;

    public OpenPaleoPadEntityMessage() {
    }

    public OpenPaleoPadEntityMessage(DinosaurEntity entity) {
        this.entity = entity;
        this.entityId = entity.getEntityId();
    }

    @Override
    public void onClientReceived(Minecraft minecraft, OpenPaleoPadEntityMessage message, EntityPlayer player, MessageContext context) {
        Entity entity = player.world.getEntityByID(message.entityId);

        if (entity instanceof DinosaurEntity) {
            DinosaurEntity dinosaur = (DinosaurEntity) entity;

            RebornMod.PROXY.openPaleoDinosaurPad(dinosaur);
        }
    }

    @Override
    public void onServerReceived(MinecraftServer server, OpenPaleoPadEntityMessage message, EntityPlayer player, MessageContext context) {
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.entityId = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.entityId);
        DinosaurEntity.FieldGuideInfo.serialize(buf, this.entity);
    }
}
