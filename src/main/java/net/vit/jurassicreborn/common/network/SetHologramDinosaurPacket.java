package net.vit.jurassicreborn.common.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;
import net.vit.jurassicreborn.common.blocks.entities.HologramBlockEntity;

import java.util.function.Supplier;

public class SetHologramDinosaurPacket {
    private final BlockPos pos;
    private final int dinosaurId;
    private final int poseIndex;
    private final boolean rotating;
    private final int rotation;

    public SetHologramDinosaurPacket(BlockPos pos, int dinosaurId, int poseIndex, boolean rotating, int rotation) {
        this.pos = pos;
        this.dinosaurId = dinosaurId;
        this.poseIndex = poseIndex;
        this.rotating = rotating;
        this.rotation = rotation;
    }

    public static void encode(SetHologramDinosaurPacket pkt, FriendlyByteBuf buf) {
        buf.writeBlockPos(pkt.pos);
        buf.writeInt(pkt.dinosaurId);
        buf.writeInt(pkt.poseIndex);
        buf.writeBoolean(pkt.rotating);
        buf.writeInt(pkt.rotation);
    }

    public static SetHologramDinosaurPacket decode(FriendlyByteBuf buf) {
        BlockPos pos = buf.readBlockPos();
        int id = buf.readInt();
        int pose = buf.readInt();
        boolean rotating = buf.readBoolean();
        int rotation = buf.readInt();
        return new SetHologramDinosaurPacket(pos, id, pose, rotating, rotation);
    }

    public static void handle(SetHologramDinosaurPacket pkt, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null) {
//                System.out.println("[Hologram] Packet sender is null!");
                return;
            }

            Level level = player.level;
            if (!(level instanceof ServerLevel serverLevel)) {
//                System.out.println("[Hologram] Level is not ServerLevel!");
                return;
            }

            BlockEntity be = level.getBlockEntity(pkt.pos);
            if (!(be instanceof HologramBlockEntity hologram)) {
//                System.out.println("[Hologram] BlockEntity at " + pkt.pos + " is not HologramBlockEntity!");
                return;
            }

//            System.out.println("[Hologram] SERVER Applying settings: dino=" + pkt.dinosaurId +
//                    ", pose=" + pkt.poseIndex + ", rotating=" + pkt.rotating +
//                    ", rotation=" + pkt.rotation);

            // Apply settings on the SERVER
            // The 'true' parameter ensures it marks for saving and syncs to clients
            hologram.applySettings(pkt.dinosaurId, pkt.poseIndex, pkt.rotating, pkt.rotation, true);

            // Mark the chunk as needing to be saved
            serverLevel.getChunkAt(pkt.pos).setUnsaved(true);

//            System.out.println("[Hologram] SERVER Settings applied successfully");
        });
        ctx.get().setPacketHandled(true);
    }
}