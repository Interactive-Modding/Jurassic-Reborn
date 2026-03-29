package net.vit.jurassicreborn.common.network;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.common.util.block.TemperatureControl;

public record SetIncubatorTempServerboundPacket(BlockPos pos, int slotIndex, int temp, ResourceKey<Level> dimension)
        implements CustomPacketPayload {
    public static final Type<SetIncubatorTempServerboundPacket> TYPE = new Type<>(JurassicReborn.resource("set_incubator_temp"));
    public static final StreamCodec<RegistryFriendlyByteBuf, SetIncubatorTempServerboundPacket> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public SetIncubatorTempServerboundPacket decode(RegistryFriendlyByteBuf buf) {
            BlockPos pos = buf.readBlockPos();
            int slotIndex = buf.readInt();
            int temp = buf.readInt();
            ResourceKey<Level> dim = buf.readResourceKey(Registries.DIMENSION);
            return new SetIncubatorTempServerboundPacket(pos, slotIndex, temp, dim);
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, SetIncubatorTempServerboundPacket msg) {
            buf.writeBlockPos(msg.pos());
            buf.writeInt(msg.slotIndex());
            buf.writeInt(msg.temp());
            buf.writeResourceKey(msg.dimension());
        }
    };

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(SetIncubatorTempServerboundPacket packet, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            if (!(ctx.player() instanceof ServerPlayer player)) {
                return;
            }

            ServerLevel level = player.serverLevel();
            if (level.isClientSide) {
                return;
            }

            BlockEntity entity = level.getBlockEntity(packet.pos());
            if (entity == null) {
                entity = level.getChunkAt(packet.pos()).getBlockEntity(packet.pos());
            }

            if (entity instanceof TemperatureControl temperatureControl
                    && level.dimension().equals(packet.dimension())
                    && packet.slotIndex() < temperatureControl.getTemperatureCount()) {
                temperatureControl.setTemperature(packet.slotIndex(), packet.temp());
            }
        });
    }
}
