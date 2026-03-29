package net.vit.jurassicreborn.common.network;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.MicroraptorEntity;
import net.vit.jurassicreborn.mixin.accessors.PlayerShoulderAccessor;

public record MicroraptorDismountMessage(int entityId) implements CustomPacketPayload {

    private static final int SHOULDER_DISMOUNT = -1;

    public static final CustomPacketPayload.Type<MicroraptorDismountMessage> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(JurassicReborn.MODID, "microraptor_dismount"));

    public static final StreamCodec<RegistryFriendlyByteBuf, MicroraptorDismountMessage> STREAM_CODEC =
            ByteBufCodecs.INT.map(MicroraptorDismountMessage::new, MicroraptorDismountMessage::entityId)
                    .cast();

    @Override
    public CustomPacketPayload.Type<MicroraptorDismountMessage> type() {
        return TYPE;
    }

    public static void handle(MicroraptorDismountMessage msg, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            // Server-side: sender is a real player
            if (ctx.player() instanceof ServerPlayer serverPlayer) {
                if (msg.entityId == SHOULDER_DISMOUNT) {
                    spawnMicroraptorFromShoulder(serverPlayer, true);
                    spawnMicroraptorFromShoulder(serverPlayer, false);
                    return;
                }

                Entity entity = serverPlayer.level().getEntity(msg.entityId);
                if (entity instanceof MicroraptorEntity microraptor && microraptor.isOwner(serverPlayer)) {
                    microraptor.stopRiding();
                    Network.sendToAllNear(serverPlayer.level(), microraptor.blockPosition(), 64.0d, msg);
                }
            } else {
                // Client-side
                Client.handle(msg, ctx);
            }
        });
    }

    private static void spawnMicroraptorFromShoulder(ServerPlayer player, boolean left) {
        CompoundTag shoulderTag = left
                ? player.getShoulderEntityLeft().copy()
                : player.getShoulderEntityRight().copy();

        if (shoulderTag.isEmpty() || !shoulderTag.getString("id").contains("microraptor")) {
            return;
        }

        PlayerShoulderAccessor accessor = (PlayerShoulderAccessor) player;
        if (left) {
            accessor.jurassicreborn$setShoulderEntityLeft(new CompoundTag());
        } else {
            accessor.jurassicreborn$setShoulderEntityRight(new CompoundTag());
        }

        EntityType.create(shoulderTag, player.level()).ifPresent(entity -> {
            float yaw = player.getYRot();
            float yawRad = yaw * ((float) Math.PI / 180F);

            double forwardX = -Mth.sin(yawRad) * 0.7D;
            double forwardZ =  Mth.cos(yawRad) * 0.7D;

            entity.setPos(player.getX() + forwardX, player.getY() + 0.2D, player.getZ() + forwardZ);

            if (entity instanceof MicroraptorEntity microraptor) {
                microraptor.setYRot(yaw);
                microraptor.setYBodyRot(yaw);
                microraptor.yRotO = yaw;
                microraptor.yBodyRotO = yaw;
                microraptor.setInvisible(false);
                microraptor.getEntityData().set(MicroraptorEntity.ON_SHOULDER, false);
            }

            player.level().addFreshEntity(entity);
        });
    }

    @OnlyIn(Dist.CLIENT)
    private static class Client {
        static void handle(MicroraptorDismountMessage msg, IPayloadContext ctx) {
            Level level = net.minecraft.client.Minecraft.getInstance().level;
            if (level == null) return;
            Entity entity = level.getEntity(msg.entityId);
            if (entity != null) {
                entity.stopRiding();
            }
        }
    }
}