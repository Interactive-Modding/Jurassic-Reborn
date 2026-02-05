package net.vit.jurassicreborn.common.network;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
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
            if (player == null) return;
            Level level = player.level();
            if (!(level instanceof ServerLevel serverLevel)) return;
            BlockEntity be = level.getBlockEntity(pkt.pos);
            if (!(be instanceof HologramBlockEntity hologram)) return;

            // ----------------------------
            // Apply settings
            // ----------------------------
            hologram.applySettings(pkt.dinosaurId, pkt.poseIndex, pkt.rotating, pkt.rotation, true);

            // ----------------------------
            // Force full save + sync
            // ----------------------------
            hologram.setChanged(); // Mark dirty
            serverLevel.sendBlockUpdated(pkt.pos, hologram.getBlockState(), hologram.getBlockState(), 3);
            serverLevel.getChunkSource().blockChanged(pkt.pos);

            try {
                var chunk = serverLevel.getChunkAt(pkt.pos);
                chunk.setUnsaved(true);

                CompoundTag tag = new CompoundTag();
                hologram.saveAdditional(tag);
                hologram.saveToItem(ItemStack.of(tag));
                hologram.saveWithFullMetadata();

                chunk.isUnsaved();
                serverLevel.getChunkSource().getDataStorage().save();

            } catch (Exception e) {
                System.err.println("[JurassicReborn] Failed to save HologramBlockEntity at " + pkt.pos + ": " + e);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}