package net.vit.jurassicreborn.common.util.message;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;
import net.vit.jurassicreborn.client.JurassicClient;
import net.vit.jurassicreborn.common.entities.DinosaurEntity;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;

import java.util.function.Supplier;

public class OpenPaleoPadEntityMessage {
    private final int entityId;
    private final DinosaurEntity.FieldGuideInfo guideInfo;

    // Sending (manual construction)
    public OpenPaleoPadEntityMessage(int entityId, DinosaurEntity.FieldGuideInfo guideInfo) {
        this.entityId = entityId;
        this.guideInfo = guideInfo;
    }

    // Sending (from entity)
    public OpenPaleoPadEntityMessage(DinosaurEntity entity) {
        this(entity.getId(), entity.getFieldGuideInfo());
    }
    public static void write(OpenPaleoPadEntityMessage message, FriendlyByteBuf buf) {
        buf.writeInt(message.entityId);
        DinosaurEntity.FieldGuideInfo.write(buf, message.guideInfo); // Now this works, because write is static
    }

    public static OpenPaleoPadEntityMessage read(FriendlyByteBuf buf) {
        int entityId = buf.readInt();
        DinosaurEntity.FieldGuideInfo guideInfo = DinosaurEntity.FieldGuideInfo.read(buf);
        return new OpenPaleoPadEntityMessage(entityId, guideInfo);
    }

    // Receiving/Decoding
    public OpenPaleoPadEntityMessage(FriendlyByteBuf buf) {
        this.entityId = buf.readInt();
        this.guideInfo = DinosaurEntity.FieldGuideInfo.read(buf);
    }

    // --- Registration helpers ---

    // Forge registration expects static decode/read and instance encode/write:
    public static OpenPaleoPadEntityMessage decode(FriendlyByteBuf buf) {
        return new OpenPaleoPadEntityMessage(buf);
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(this.entityId);
        DinosaurEntity.FieldGuideInfo.write(buf, this.guideInfo);
    }

    // --- Handler ---

    public static void handle(OpenPaleoPadEntityMessage message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() ->
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> Client.handle(message))
        );
        ctx.get().setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static class Client {
        static void handle(OpenPaleoPadEntityMessage message) {
            LocalPlayer player = net.minecraft.client.Minecraft.getInstance().player;
            if (player == null) return;
            Entity entity = player.level.getEntity(message.entityId);
            if (entity instanceof DinosaurEntity dinosaur) {
                JurassicClient.openPaleoDinosaurPad(dinosaur, message.guideInfo);
            }
        }
    }
}
