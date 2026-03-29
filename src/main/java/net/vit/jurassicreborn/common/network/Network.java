package net.vit.jurassicreborn.common.network;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.common.util.message.OpenPaleoPadEntityMessage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Network {
    private Network() {
    }

    public static void registerPayloadHandlers(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(JurassicReborn.MODID);

        registrar.playToServer(SwitchHybridizerCombinatorMode.TYPE, SwitchHybridizerCombinatorMode.STREAM_CODEC, SwitchHybridizerCombinatorMode::handle);
        registrar.playToServer(SetIncubatorTempServerboundPacket.TYPE, SetIncubatorTempServerboundPacket.STREAM_CODEC, SetIncubatorTempServerboundPacket::handle);
        registrar.playToClient(OpenPaleoPadEntityMessage.TYPE, OpenPaleoPadEntityMessage.STREAM_CODEC, OpenPaleoPadEntityMessage::handle);
        registrar.playToServer(PaddockSignPlacePacket.TYPE, PaddockSignPlacePacket.STREAM_CODEC, PaddockSignPlacePacket::handle);
        registrar.playToServer(SetHologramDinosaurPacket.TYPE, SetHologramDinosaurPacket.STREAM_CODEC, SetHologramDinosaurPacket::handle);
        registrar.playToServer(TrackDinosaurPacket.TYPE, TrackDinosaurPacket.STREAM_CODEC, TrackDinosaurPacket::handle);
        registrar.playToServer(SetOrderPacket.TYPE, SetOrderPacket.STREAM_CODEC, SetOrderPacket::handle);
        registrar.playToServer(NameFeederPacket.TYPE, NameFeederPacket.STREAM_CODEC, NameFeederPacket::handle);
        registrar.playToClient(SyncFeederTrackerPacket.TYPE, SyncFeederTrackerPacket.STREAM_CODEC, SyncFeederTrackerPacket::handle);
        registrar.playToClient(FordExplorerChangeStateMessage.TYPE, FordExplorerChangeStateMessage.STREAM_CODEC, FordExplorerChangeStateMessage::handle);
        registrar.playToClient(FordExplorerSnowChangeStateMessage.TYPE, FordExplorerSnowChangeStateMessage.STREAM_CODEC, FordExplorerSnowChangeStateMessage::handle);
        registrar.playToClient(MonorailChangeStateMessage.TYPE, MonorailChangeStateMessage.STREAM_CODEC, MonorailChangeStateMessage::handle);
        registrar.playToClient(CarEntityPlayRecord.TYPE, CarEntityPlayRecord.STREAM_CODEC, CarEntityPlayRecord::handle);
        registrar.playToServer(UpdateVehicleControlMessage.TYPE, UpdateVehicleControlMessage.STREAM_CODEC, UpdateVehicleControlMessage::handle);
        registrar.playToClient(FordExplorerUpdatePositionStateMessage.TYPE, FordExplorerUpdatePositionStateMessage.STREAM_CODEC, FordExplorerUpdatePositionStateMessage::handle);
        registrar.playToClient(FordExplorerSnowUpdatePositionStateMessage.TYPE, FordExplorerSnowUpdatePositionStateMessage.STREAM_CODEC, FordExplorerSnowUpdatePositionStateMessage::handle);
        registrar.playToClient(MonorailUpdatePositionStateMessage.TYPE, MonorailUpdatePositionStateMessage.STREAM_CODEC, MonorailUpdatePositionStateMessage::handle);
        registrar.playToServer(SwitchSeatMessage.TYPE, SwitchSeatMessage.STREAM_CODEC, SwitchSeatMessage::handle);
        registrar.playToServer(ChangeStationMessage.TYPE, ChangeStationMessage.STREAM_CODEC, ChangeStationMessage::handle);
        registrar.playBidirectional(MicroraptorDismountMessage.TYPE, MicroraptorDismountMessage.STREAM_CODEC, MicroraptorDismountMessage::handle);
        registrar.playToServer(BlueprintPlacePacket.TYPE, BlueprintPlacePacket.STREAM_CODEC, BlueprintPlacePacket::handle);
    }

    public static final List<BlockEntity> ENTITIES = Collections.synchronizedList(new ArrayList<>());
    public static HashMap<BlockPos, Int2ObjectArrayMap<ItemStack>> slotMap = new HashMap<>();

    public static void sendToAllNear(Level level, BlockPos pos, double radius, CustomPacketPayload message) {
        if (!(level instanceof ServerLevel serverLevel)) {
            return;
        }
        double radiusSq = radius * radius;
        double x = pos.getX() + 0.5d;
        double y = pos.getY() + 0.5d;
        double z = pos.getZ() + 0.5d;
        for (ServerPlayer player : serverLevel.players()) {
            if (player.distanceToSqr(x, y, z) <= radiusSq) {
                PacketDistributor.sendToPlayer(player, message);
            }
        }
    }

    /** Convenience overload: fixed 64-block radius. */
    public static void sendToAllNear(Level level, BlockPos pos, CustomPacketPayload message) {
        sendToAllNear(level, pos, 64.0d, message);
    }

    public static void switchHybridizerCombinerMode(boolean mode, BlockPos pos, ResourceKey<Level> dimension) {
        sendToServer(new SwitchHybridizerCombinatorMode(mode, pos, dimension));
    }

    public static void setIncubatorTemperature(BlockPos incubator, int slot, int temp, ResourceKey<Level> dimension) {
        sendToServer(new SetIncubatorTempServerboundPacket(incubator, slot, temp, dimension));
    }

    public static void removeRemovedEntities(){
        synchronized (ENTITIES) {
            ENTITIES.removeIf(BlockEntity::isRemoved);
        }
    }

    public static void sendToServer(CustomPacketPayload message) {
        PacketDistributor.sendToServer(message);
    }

    public static void sendTo(ServerPlayer player, CustomPacketPayload message) {
        PacketDistributor.sendToPlayer(player, message);
    }
}
