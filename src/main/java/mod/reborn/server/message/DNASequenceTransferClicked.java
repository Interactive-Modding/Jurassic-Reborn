package mod.reborn.server.message;

import com.google.common.collect.Lists;
import io.netty.buffer.ByteBuf;
import mod.reborn.server.plugin.jei.category.dnasynthesizer.DNASequencerTransferHandler;
import net.ilexiconn.llibrary.server.network.AbstractMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class DNASequenceTransferClicked extends AbstractMessage<DNASequenceTransferClicked> {

    private List<Pair<Integer, Integer>> linkedList;

    public DNASequenceTransferClicked() {

    }

    public DNASequenceTransferClicked(List<Pair<Integer, Integer>> linkedList) {
        this.linkedList = linkedList;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(linkedList.size());
        linkedList.forEach(pair -> {
            buf.writeInt(pair.getKey());
            buf.writeInt((pair.getRight()));
        });
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        linkedList = Lists.newArrayList();
        int size = buf.readInt();
        for(int i = 0; i < size; i++) {
            linkedList.add(Pair.of(buf.readInt(), buf.readInt()));
        }
    }

    @Override
    public void onClientReceived(Minecraft client, DNASequenceTransferClicked message, EntityPlayer player, MessageContext messageContext) {

    }

    @Override
    public void onServerReceived(MinecraftServer server, DNASequenceTransferClicked message, EntityPlayer player, MessageContext messageContext) {
        DNASequencerTransferHandler.setItemServer(player, message.linkedList);
    }
}
