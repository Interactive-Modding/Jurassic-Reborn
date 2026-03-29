package net.vit.jurassicreborn.common.network;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.common.blocks.ModBlocks;
import net.vit.jurassicreborn.common.blocks.entities.DNABlocks.DNACombinatorHybridizer.DNACombinatorHybridizerBlock;

public record SwitchHybridizerCombinatorMode(boolean mode, BlockPos pos, ResourceKey<Level> dimension)
        implements CustomPacketPayload {
    public static final Type<SwitchHybridizerCombinatorMode> TYPE = new Type<>(JurassicReborn.resource("switch_hybridizer_combinator_mode"));
    public static final StreamCodec<RegistryFriendlyByteBuf, SwitchHybridizerCombinatorMode> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public SwitchHybridizerCombinatorMode decode(RegistryFriendlyByteBuf buf) {
            boolean mode = buf.readBoolean();
            BlockPos pos = buf.readBlockPos();
            ResourceKey<Level> dimension = buf.readResourceKey(Registries.DIMENSION);
            return new SwitchHybridizerCombinatorMode(mode, pos, dimension);
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, SwitchHybridizerCombinatorMode msg) {
            buf.writeBoolean(msg.mode());
            buf.writeBlockPos(msg.pos());
            buf.writeResourceKey(msg.dimension());
        }
    };

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(SwitchHybridizerCombinatorMode packet, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            if (!(ctx.player() instanceof net.minecraft.server.level.ServerPlayer player)) {
                return;
            }
            ServerLevel level = player.server.getLevel(packet.dimension());
            if (level != null && level.getBlockState(packet.pos()).is(ModBlocks.DNA_COMBINER_HYBRIDIZER.get())) {
                level.setBlock(packet.pos(), level.getBlockState(packet.pos())
                        .setValue(DNACombinatorHybridizerBlock.MODE, packet.mode()), 3);
            }
        });
    }
}
