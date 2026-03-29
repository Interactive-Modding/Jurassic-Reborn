package net.vit.jurassicreborn.common.network;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.common.entities.item.PaddockSignEntity;

public record PaddockSignPlacePacket(BlockPos pos, Direction face, InteractionHand hand, int dinosaurId)
        implements CustomPacketPayload {
    public static final Type<PaddockSignPlacePacket> TYPE = new Type<>(JurassicReborn.resource("paddock_sign_place"));
    public static final StreamCodec<RegistryFriendlyByteBuf, PaddockSignPlacePacket> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public PaddockSignPlacePacket decode(RegistryFriendlyByteBuf buf) {
            BlockPos pos = buf.readBlockPos();
            Direction face = buf.readEnum(Direction.class);
            InteractionHand hand = buf.readEnum(InteractionHand.class);
            int id = buf.readInt();
            return new PaddockSignPlacePacket(pos, face, hand, id);
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, PaddockSignPlacePacket msg) {
            buf.writeBlockPos(msg.pos());
            buf.writeEnum(msg.face());
            buf.writeEnum(msg.hand());
            buf.writeInt(msg.dinosaurId());
        }
    };

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(PaddockSignPlacePacket pkt, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            if (!(ctx.player() instanceof ServerPlayer sender)) {
                return;
            }
            Level world = sender.level();
            BlockPos spawnPos = pkt.pos().relative(pkt.face());

            if (world.isClientSide) {
                return;
            }
            if (!world.getBlockState(spawnPos).isAir()) {
                return;
            }

            PaddockSignEntity sign = new PaddockSignEntity(
                    world,
                    pkt.pos(),
                    pkt.face(),
                    pkt.dinosaurId()
            );
            world.addFreshEntity(sign);

            ItemStack held = sender.getItemInHand(pkt.hand());
            if (!sender.getAbilities().instabuild) {
                held.shrink(1);
            }
        });
    }
}
