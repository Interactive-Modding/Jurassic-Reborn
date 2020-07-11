package mod.reborn.server.message;

import io.netty.buffer.ByteBuf;
import mod.reborn.server.entity.vehicle.HelicopterEntityNew;
import net.ilexiconn.llibrary.server.network.AbstractMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class HelicopterEngineMessage extends AbstractMessage<HelicopterEngineMessage> {
    private int heliID;
    private boolean engineState;
    private float engineMovement;

    public HelicopterEngineMessage() {
    }

    public HelicopterEngineMessage(int heliID, boolean engineState, float engineMovement) {
        this.heliID = heliID;
        this.engineState = engineState;
        this.engineMovement = engineMovement;
    }

    @Override
    public void onClientReceived(Minecraft minecraft, HelicopterEngineMessage message, EntityPlayer player, MessageContext messageContext) {
        HelicopterEntityNew helicopter = getHeli(player.world, message.heliID);
        if (helicopter != null) {
            helicopter.setEngineRunning(message.engineState);
            helicopter.setEngineMovement(message.engineMovement);
        }
    }

    @Override
    public void onServerReceived(MinecraftServer minecraftServer, HelicopterEngineMessage message, EntityPlayer player, MessageContext messageContext) {
        HelicopterEntityNew helicopter = getHeli(player.world, message.heliID);
        if (helicopter != null) {
            helicopter.setEngineRunning(message.engineState);
            helicopter.setEngineMovement(message.engineMovement);
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
        this.engineState = buf.readBoolean();
        this.engineMovement = buf.readFloat();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.heliID);
        buf.writeBoolean(this.engineState);
        buf.writeFloat(this.engineMovement);
    }
}
