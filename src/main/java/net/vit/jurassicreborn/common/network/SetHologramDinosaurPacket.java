package net.vit.jurassicreborn.common.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.common.blocks.entities.HologramBlockEntity;

public record SetHologramDinosaurPacket(BlockPos pos, int dinosaurId, int poseIndex, boolean rotating, int rotation)
        implements CustomPacketPayload {
    public static final Type<SetHologramDinosaurPacket> TYPE = new Type<>(JurassicReborn.resource("set_hologram_dinosaur"));
    public static final StreamCodec<RegistryFriendlyByteBuf, SetHologramDinosaurPacket> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public SetHologramDinosaurPacket decode(RegistryFriendlyByteBuf buf) {
            BlockPos pos = buf.readBlockPos();
            int id = buf.readInt();
            int pose = buf.readInt();
            boolean rotating = buf.readBoolean();
            int rotation = buf.readInt();
            return new SetHologramDinosaurPacket(pos, id, pose, rotating, rotation);
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, SetHologramDinosaurPacket msg) {
            buf.writeBlockPos(msg.pos());
            buf.writeInt(msg.dinosaurId());
            buf.writeInt(msg.poseIndex());
            buf.writeBoolean(msg.rotating());
            buf.writeInt(msg.rotation());
        }
    };

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(SetHologramDinosaurPacket pkt, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            if (!(ctx.player() instanceof ServerPlayer player)) {
                return;
            }
            Level level = player.level();
            if (!(level instanceof ServerLevel serverLevel)) {
                return;
            }
            BlockEntity be = level.getBlockEntity(pkt.pos());
            if (!(be instanceof HologramBlockEntity hologram)) {
                return;
            }

            hologram.applySettings(pkt.dinosaurId(), pkt.poseIndex(), pkt.rotating(), pkt.rotation(), true);

            hologram.setChanged();
            serverLevel.sendBlockUpdated(pkt.pos(), hologram.getBlockState(), hologram.getBlockState(), 3);
            serverLevel.getChunkSource().blockChanged(pkt.pos());

            try {
                var chunk = serverLevel.getChunkAt(pkt.pos());
                chunk.setUnsaved(true);

                ItemStack stack = new ItemStack(hologram.getBlockState().getBlock());
                hologram.saveToItem(stack, serverLevel.registryAccess());

                chunk.isUnsaved();
                serverLevel.getChunkSource().getDataStorage().save();
            } catch (Exception e) {
                System.err.println("[JurassicReborn] Failed to save HologramBlockEntity at " + pkt.pos() + ": " + e);
            }
        });
    }
}
