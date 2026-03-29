package net.vit.jurassicreborn.common.network;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.common.entities.item.BlueprintPaintingEntity;
import net.vit.jurassicreborn.common.items.misc.BlueprintItem;

import java.util.Optional;

public record BlueprintPlacePacket(
        BlockPos pos,
        Direction face,
        InteractionHand hand,
        ResourceLocation variantId
) implements CustomPacketPayload {

    public static final Type<BlueprintPlacePacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(JurassicReborn.MODID, "blueprint_place"));

    private static final StreamCodec<RegistryFriendlyByteBuf, Direction> DIRECTION_STREAM_CODEC =
            StreamCodec.of(
                    (buf, value) -> buf.writeVarInt(value.get3DDataValue()),
                    buf -> Direction.from3DDataValue(buf.readVarInt())
            );

    private static final StreamCodec<RegistryFriendlyByteBuf, InteractionHand> HAND_STREAM_CODEC =
            StreamCodec.of(
                    (buf, value) -> buf.writeVarInt(value.ordinal()),
                    buf -> InteractionHand.values()[buf.readVarInt()]
            );

    private static final StreamCodec<RegistryFriendlyByteBuf, ResourceLocation> RL_STREAM_CODEC =
            StreamCodec.of(
                    (buf, value) -> buf.writeResourceLocation(value),
                    RegistryFriendlyByteBuf::readResourceLocation
            );

    public static final StreamCodec<RegistryFriendlyByteBuf, BlueprintPlacePacket> STREAM_CODEC =
            StreamCodec.composite(
                    BlockPos.STREAM_CODEC, BlueprintPlacePacket::pos,
                    DIRECTION_STREAM_CODEC, BlueprintPlacePacket::face,
                    HAND_STREAM_CODEC, BlueprintPlacePacket::hand,
                    RL_STREAM_CODEC, BlueprintPlacePacket::variantId,
                    BlueprintPlacePacket::new
            );

    @Override
    public Type<BlueprintPlacePacket> type() {
        return TYPE;
    }

    public static void handle(BlueprintPlacePacket pkt, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            if (!(ctx.player() instanceof ServerPlayer sender)) {
                return;
            }

            if (!pkt.face().getAxis().isHorizontal()) {
                return;
            }

            Level level = sender.level();
            ItemStack held = sender.getItemInHand(pkt.hand());
            if (!(held.getItem() instanceof BlueprintItem)) {
                return;
            }

            Registry<PaintingVariant> registry = level.registryAccess().registryOrThrow(Registries.PAINTING_VARIANT);
            Optional<Holder.Reference<PaintingVariant>> holderOpt = registry.getHolder(pkt.variantId());

            if (holderOpt.isEmpty()) {
                return;
            }

            Holder<PaintingVariant> holder = holderOpt.get();
            if (!holder.is(BlueprintItem.BLUEPRINT_VARIANTS_TAG)) {
                return;
            }

            BlockPos spawnPos = pkt.pos().relative(pkt.face());
            BlueprintPaintingEntity painting = new BlueprintPaintingEntity(level, spawnPos, pkt.face(), holder);

            if (!painting.survives()) {
                return;
            }

            painting.setBlueprintTexture(ResourceLocation.fromNamespaceAndPath(
                    JurassicReborn.MODID,
                    "textures/painting/" + pkt.variantId().getPath() + ".png"
            ));

            level.addFreshEntity(painting);
            painting.playPlacementSound();

            if (!sender.getAbilities().instabuild) {
                held.shrink(1);
            }
        });
    }
}