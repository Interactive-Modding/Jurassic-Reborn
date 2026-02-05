package net.vit.jurassicreborn.common.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.RecordItem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.vit.jurassicreborn.common.entities.vehicle.VehicleEntity;
import java.util.function.Supplier;

public class CarEntityPlayRecord {
    private final int entityId;
    private final ItemStack record;

    public CarEntityPlayRecord(int entityId, ItemStack record) {
        this.entityId = entityId;
        this.record = record.copy();
    }

    public static void encode(CarEntityPlayRecord msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.entityId);
        buf.writeItem(msg.record);
    }

    public static CarEntityPlayRecord decode(FriendlyByteBuf buf) {
        return new CarEntityPlayRecord(buf.readInt(), buf.readItem());
    }


    public static void handle(CarEntityPlayRecord msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() ->
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> Client.handle(msg))
        );
        ctx.get().setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static class Client {
        static void handle(CarEntityPlayRecord msg) {
            var mc = net.minecraft.client.Minecraft.getInstance();
            if (mc.level == null) return;

            Entity e = mc.level.getEntity(msg.entityId);
            if (!(e instanceof VehicleEntity car)) return;

            if (!(msg.record.getItem() instanceof RecordItem rec)) return;

//            // stop previous loop
//            if (car.sound != null) car.sound.stop();
//
//            car.sound = new CarLoopSound(
//                    car,
//                    rec.getSound(),
//                    SoundSource.RECORDS,
//                    v -> v.getItem().is(rec)   // play only while the same disc is still inside
//            );
//            mc.getSoundManager().play(car.sound);
        }
    }
}
