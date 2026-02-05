package net.vit.jurassicreborn.common.network;

import net.vit.jurassicreborn.common.util.block.TemperatureControl;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

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
        if(Thread.currentThread() != ctx.get().getSender().getServer().getRunningThread())//stupid hackery trying to fix the dumb block entity fetching bug
            handleOnRenderThread(packet, ctx);
    }

    public static void handleOnRenderThread(SetIncubatorTempServerboundPacket packet, Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        ServerPlayer player = context.getSender();
        if (player == null) {
            return;
        }

        ServerLevel dim = player.getLevel();

        if(dim.isClientSide){
            return;
        }

        //this should pretty much always work unless the block entity was removed before this code could be run but the
        //      threading should make that pretty much impossible.
        //HOWEVER, I was encountering a stupid weird error where the world was returning null for no damn reason
        BlockEntity e = dim.getBlockEntity(packet.pos);

        //and then I added this and it worked.
        //my sanity is quickly leaving my body.

        // - gamma

        if (e == null) {
            e = dim.getChunkAt(packet.pos).getBlockEntity(packet.pos);
        }


        if(e instanceof TemperatureControl temperatureControl && dim.dimension().equals(packet.dimension)
                && packet.slotIndex < temperatureControl.getTemperatureCount()) {
            temperatureControl.setTemperature(packet.slotIndex, packet.temp);
        }


    }







    public void write(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(pos);
        buffer.writeInt(slotIndex);
        buffer.writeInt(temp);
        buffer.writeResourceKey(dimension);
    }

    public static SetIncubatorTempServerboundPacket read(FriendlyByteBuf buffer){
        var pos = buffer.readBlockPos();
        int slotIndex = buffer.readInt();
        int temp = buffer.readInt();
        ResourceKey<Level> dim = buffer.readResourceKey(Registries.DIMENSION);

        return new SetIncubatorTempServerboundPacket(pos, slotIndex, temp, dim);

    }
}
