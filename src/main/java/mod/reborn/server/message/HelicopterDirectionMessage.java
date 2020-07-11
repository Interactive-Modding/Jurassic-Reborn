package mod.reborn.server.message;

import io.netty.buffer.ByteBuf;
import mod.reborn.server.entity.vehicle.HelicopterEntityNew;
import mod.reborn.server.util.MutableVec3;
import net.ilexiconn.llibrary.server.network.AbstractMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class HelicopterDirectionMessage extends AbstractMessage<HelicopterDirectionMessage> {
    private int heliID;
    private MutableVec3 direction;

    public HelicopterDirectionMessage() {
        this.direction = new MutableVec3(0, 0, 0);
    }

    public HelicopterDirectionMessage(int heliID, MutableVec3 direction) {
        this.heliID = heliID;
        this.direction = direction;
    }

    @Override
    public void onClientReceived(Minecraft minecraft, HelicopterDirectionMessage message, EntityPlayer entityPlayer, MessageContext messageContext) {
        HelicopterEntityNew helicopter = getHeli(entityPlayer.world, message.heliID);
        if (helicopter != null) {
            helicopter.setDirection(message.direction);
        }
    }

    @Override
    public void onServerReceived(MinecraftServer minecraftServer, HelicopterDirectionMessage message, EntityPlayer entityPlayer, MessageContext messageContext) {
        HelicopterEntityNew helicopter = getHeli(entityPlayer.world, message.heliID);
        if (helicopter != null) {
            helicopter.setDirection(message.direction);
        }
    }

    public static HelicopterEntityNew getHeli(World world, int heliID) {
        try {
            return (HelicopterEntityNew) world.getEntityByID(heliID);
        } catch (NullPointerException e) {
        }
        return null;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.heliID = buf.readInt();
        double x = buf.readDouble();
        double y = buf.readDouble();
        double z = buf.readDouble();
        this.direction.set(x, y, z);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.heliID);
        buf.writeDouble(this.direction.xCoord);
        buf.writeDouble(this.direction.yCoord);
        buf.writeDouble(this.direction.zCoord);
    }
}