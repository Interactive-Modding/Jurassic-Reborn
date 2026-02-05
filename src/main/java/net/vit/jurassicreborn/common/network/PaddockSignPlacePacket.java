package net.vit.jurassicreborn.common.network;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import net.vit.jurassicreborn.common.entities.item.PaddockSignEntity;

import java.util.function.Supplier;

public class PaddockSignPlacePacket {
    private final BlockPos pos;
    private final Direction face;
    private final InteractionHand hand;
    private final int dinosaurId;

    public PaddockSignPlacePacket(BlockPos pos, Direction face, InteractionHand hand, int dinosaurId) {
        this.pos = pos;
        this.face = face;
        this.hand = hand;
        this.dinosaurId = dinosaurId;
    }

    public static void encode(PaddockSignPlacePacket pkt, FriendlyByteBuf buf) {
        buf.writeBlockPos(pkt.pos);
        buf.writeEnum(pkt.face);
        buf.writeEnum(pkt.hand);
        buf.writeInt(pkt.dinosaurId);
    }

    public static PaddockSignPlacePacket decode(FriendlyByteBuf buf) {
        BlockPos pos = buf.readBlockPos();
        Direction face = buf.readEnum(Direction.class);
        InteractionHand hand = buf.readEnum(InteractionHand.class);
        int id = buf.readInt();
        return new PaddockSignPlacePacket(pos, face, hand, id);
    }

    public static void handle(PaddockSignPlacePacket pkt, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer sender = ctx.get().getSender();
            if (sender == null) return;
            Level world = sender.level();
            BlockPos spawnPos = pkt.pos.relative(pkt.face);

            if (world.isClientSide) return;
            if (!world.getBlockState(spawnPos).isAir()) return;

            // spawn the hanging sign
            PaddockSignEntity sign = new PaddockSignEntity(
                    (net.minecraft.world.level.Level)world,
                    pkt.pos,
                    pkt.face,
                    pkt.dinosaurId
            );
            world.addFreshEntity(sign);

            // consume one item
            ItemStack held = sender.getItemInHand(pkt.hand);
            if (!sender.getAbilities().instabuild) {
                held.shrink(1);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
