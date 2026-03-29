package net.vit.jurassicreborn.common.util.message;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.common.entities.DinosaurEntity;

public record OpenPaleoPadEntityMessage(int entityId, DinosaurEntity.FieldGuideInfo guideInfo)
        implements CustomPacketPayload {
    public static final Type<OpenPaleoPadEntityMessage> TYPE = new Type<>(JurassicReborn.resource("open_paleo_pad_entity"));
    public static final StreamCodec<RegistryFriendlyByteBuf, OpenPaleoPadEntityMessage> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public OpenPaleoPadEntityMessage decode(RegistryFriendlyByteBuf buf) {
            int entityId = buf.readInt();
            DinosaurEntity.FieldGuideInfo guideInfo = DinosaurEntity.FieldGuideInfo.read(buf);
            return new OpenPaleoPadEntityMessage(entityId, guideInfo);
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, OpenPaleoPadEntityMessage msg) {
            buf.writeInt(msg.entityId());
            DinosaurEntity.FieldGuideInfo.write(buf, msg.guideInfo());
        }
    };

    public OpenPaleoPadEntityMessage(DinosaurEntity entity) {
        this(entity.getId(), entity.getFieldGuideInfo());
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(OpenPaleoPadEntityMessage message, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            var player = ctx.player();
            if (player == null) {
                return;
            }
            Entity entity = player.level().getEntity(message.entityId());
            if (entity instanceof DinosaurEntity dinosaur) {
                openPaleoPadClientOnly(dinosaur, message.guideInfo());
            }
        });
    }
    private static void openPaleoPadClientOnly(DinosaurEntity dinosaur, DinosaurEntity.FieldGuideInfo guideInfo) {
        try {
            Class<?> clientClass = Class.forName("net.vit.jurassicreborn.client.JurassicClient");
            clientClass.getMethod("openPaleoDinosaurPad", DinosaurEntity.class, DinosaurEntity.FieldGuideInfo.class)
                    .invoke(null, dinosaur, guideInfo);
        } catch (ReflectiveOperationException ignored) {
        }
    }

}
