package net.vit.jurassicreborn.common.network;

import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.JukeboxSong;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.client.sounds.CarLoopSound;
import net.vit.jurassicreborn.common.entities.vehicle.VehicleEntity;

import java.util.Optional;

public record CarEntityPlayRecord(int entityId, ItemStack record) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<CarEntityPlayRecord> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(JurassicReborn.MODID, "car_play_record"));

    public static final StreamCodec<RegistryFriendlyByteBuf, CarEntityPlayRecord> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public CarEntityPlayRecord decode(RegistryFriendlyByteBuf buf) {
            int entityId = buf.readInt();
            boolean hasRecord = buf.readBoolean();
            ItemStack record = hasRecord ? ItemStack.STREAM_CODEC.decode(buf) : ItemStack.EMPTY;
            return new CarEntityPlayRecord(entityId, record);
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, CarEntityPlayRecord msg) {
            buf.writeInt(msg.entityId());
            boolean hasRecord = !msg.record().isEmpty();
            buf.writeBoolean(hasRecord);
            if (hasRecord) {
                ItemStack.STREAM_CODEC.encode(buf, msg.record());
            }
        }
    };

    @Override
    public CustomPacketPayload.Type<CarEntityPlayRecord> type() {
        return TYPE;
    }

    public static void handle(CarEntityPlayRecord msg, IPayloadContext ctx) {
        ctx.enqueueWork(() -> Client.handle(msg));
    }

    @OnlyIn(Dist.CLIENT)
    private static class Client {
        static void handle(CarEntityPlayRecord msg) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.level == null) return;

            Entity e = mc.level.getEntity(msg.entityId());
            if (!(e instanceof VehicleEntity car)) return;

            car.setStationItem(msg.record());

            if (msg.record().isEmpty() || car.getControllingPassenger() == null) {
                if (car.stationSound != null) {
                    mc.getSoundManager().stop(car.stationSound);
                    car.stationSound = null;
                }
                return;
            }

            Optional<Holder<JukeboxSong>> songHolder = JukeboxSong.fromStack(
                    mc.level.registryAccess(), msg.record()
            );
            if (songHolder.isEmpty()) return;

            JukeboxSong song = songHolder.get().value();

            if (car.stationSound != null) {
                mc.getSoundManager().stop(car.stationSound);
                car.stationSound = null;
            }

            car.stationSound = new CarLoopSound(
                    car,
                    song.soundEvent().value(),
                    SoundSource.RECORDS,
                    v -> !v.isRemoved() && !v.getStationItem().isEmpty() && v.getControllingPassenger() != null,
                    true
            );
            mc.getSoundManager().play(car.stationSound);

            if (mc.player != null && mc.player.getVehicle() == car) {
                mc.player.displayClientMessage(
                        Component.translatable(
                                "message.jurassicreborn.vehicle.station",
                                song.description()
                        ),
                        true
                );
            }
        }
    }
}