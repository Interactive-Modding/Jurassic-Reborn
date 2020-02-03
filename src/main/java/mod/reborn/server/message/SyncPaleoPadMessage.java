package mod.reborn.server.message;

import io.netty.buffer.ByteBuf;
import mod.reborn.RebornMod;
import mod.reborn.server.datafixers.PlayerData;
import net.ilexiconn.llibrary.server.network.AbstractMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SyncPaleoPadMessage extends AbstractMessage<SyncPaleoPadMessage>
{
    private NBTTagCompound nbt;

    public SyncPaleoPadMessage()
    {
    }

    public SyncPaleoPadMessage(EntityPlayer player)
    {
        nbt = new NBTTagCompound();
        PlayerData.get(player).saveNBTData(nbt);
    }

    @Override
    public void onClientReceived(Minecraft minecraft, SyncPaleoPadMessage message, EntityPlayer player, MessageContext messageContext)
    {
        PlayerData.get(player).loadNBTData(message.nbt);
        RebornMod.PROXY.openPaleoPad();
    }

    @Override
    public void onServerReceived(MinecraftServer minecraftServer, SyncPaleoPadMessage message, EntityPlayer player, MessageContext messageContext)
    {
        PlayerData.get(player).loadNBTData(message.nbt);
    }

    @Override
    public void toBytes(ByteBuf buffer)
    {
        ByteBufUtils.writeTag(buffer, nbt);
    }

    @Override
    public void fromBytes(ByteBuf buffer)
    {
        nbt = ByteBufUtils.readTag(buffer);
    }
}
