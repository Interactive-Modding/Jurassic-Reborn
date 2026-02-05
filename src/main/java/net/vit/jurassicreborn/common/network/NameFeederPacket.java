package net.vit.jurassicreborn.common.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;
import net.vit.jurassicreborn.common.blocks.entities.feeder.FeederBlockEntity;
import net.vit.jurassicreborn.common.paleopad.FeederTrackerApp;

import java.util.function.Supplier;

public class NameFeederPacket {
    private final BlockPos pos;
    private final String name;

    public NameFeederPacket(BlockPos pos, String name) {
        this.pos = pos;
        this.name = name;
    }

    public static void encode(NameFeederPacket pkt, FriendlyByteBuf buf) {
        buf.writeBlockPos(pkt.pos);
        buf.writeUtf(pkt.name);
    }

    public static NameFeederPacket decode(FriendlyByteBuf buf) {
        return new NameFeederPacket(buf.readBlockPos(), buf.readUtf());
    }

    public static void handle(NameFeederPacket pkt, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player != null) {
                Level level = player.level();
                BlockEntity be = level.getBlockEntity(pkt.pos);
                if (be instanceof FeederBlockEntity feeder) {
                    Component nameComp = Component.literal(pkt.name);
                    feeder.setCustomName(nameComp);
                    FeederTrackerApp.addFeeder(player, pkt.pos, nameComp);
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
