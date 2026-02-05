package net.vit.jurassicreborn.common.network;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;
import net.vit.jurassicreborn.common.util.block.TemperatureControl;

import java.util.function.Supplier;

public class SetIncubatorTempServerboundPacket{


    private BlockPos pos;
    private int slotIndex;
    private int temp;
    private ResourceKey<Level> dimension;

    public SetIncubatorTempServerboundPacket(BlockPos pos, int slotIndex, int temperature, ResourceKey<Level> dim){
        this.pos = pos;
        this.slotIndex = slotIndex;
        this.temp = temperature;
        this.dimension = dim;
    }

    public static void handle(SetIncubatorTempServerboundPacket packet, Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player == null) {
                return;
            }

            ServerLevel level = player.getServer().getLevel(packet.dimension);
            if (level == null || level.isClientSide) {
                return;
            }

            BlockEntity blockEntity = level.getBlockEntity(packet.pos);
            if (blockEntity == null) {
                blockEntity = level.getChunkAt(packet.pos).getBlockEntity(packet.pos);
            }

            if (blockEntity instanceof TemperatureControl temperatureControl
                    && packet.slotIndex < temperatureControl.getTemperatureCount()) {
                temperatureControl.setTemperature(packet.slotIndex, packet.temp);

                blockEntity.setChanged();
                level.sendBlockUpdated(packet.pos, blockEntity.getBlockState(), blockEntity.getBlockState(), 3);
            }
        });

        context.setPacketHandled(true);
    }






    public void write(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(pos);
        buffer.writeInt(slotIndex);
        buffer.writeInt(temp);
        buffer.writeResourceLocation(dimension.location());
    }

    public static SetIncubatorTempServerboundPacket read(FriendlyByteBuf buffer){
        var pos = buffer.readBlockPos();
        int slotIndex = buffer.readInt();
        int temp = buffer.readInt();
        ResourceLocation dimId = buffer.readResourceLocation();
        ResourceKey<Level> dim = ResourceKey.create(Registry.DIMENSION_REGISTRY, dimId);

        return new SetIncubatorTempServerboundPacket(pos, slotIndex, temp, dim);

    }
}
