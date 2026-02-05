package net.vit.jurassicreborn.common.network;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import net.vit.jurassicreborn.common.util.message.OpenPaleoPadEntityMessage;

import java.util.*;
import java.util.stream.Collectors;

import static net.vit.jurassicreborn.JurassicReborn.resource;

public class Network {
    private Network(){
        this.channel.registerMessage(id++, SwitchHybridizerCombinatorMode.class,
                SwitchHybridizerCombinatorMode::write,
                SwitchHybridizerCombinatorMode::read,
                SwitchHybridizerCombinatorMode::handle);
        this.channel.registerMessage(id++, SetIncubatorTempServerboundPacket.class,
                SetIncubatorTempServerboundPacket::write,
                SetIncubatorTempServerboundPacket::read,
                SetIncubatorTempServerboundPacket::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
        this.channel.registerMessage(
                id++,
                OpenPaleoPadEntityMessage.class,
                OpenPaleoPadEntityMessage::write,
                OpenPaleoPadEntityMessage::read,
                OpenPaleoPadEntityMessage::handle,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT)
        );
        channel.registerMessage(
                id++,
                PaddockSignPlacePacket.class,
                PaddockSignPlacePacket::encode,
                PaddockSignPlacePacket::decode,
                PaddockSignPlacePacket::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER)
        );
        this.channel.registerMessage(
                id++,
                SetHologramDinosaurPacket.class,
                SetHologramDinosaurPacket::encode,
                SetHologramDinosaurPacket::decode,
                SetHologramDinosaurPacket::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER)
        );
        this.channel.registerMessage(
                id++,
                TrackDinosaurPacket.class,
                TrackDinosaurPacket::toBytes,
                TrackDinosaurPacket::new,
                TrackDinosaurPacket::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER)
        );
        this.channel.registerMessage(
                id++,
                SetOrderPacket.class,
                SetOrderPacket::toBytes,
                SetOrderPacket::new,
                SetOrderPacket::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER)
        );
        this.channel.registerMessage(
                id++,
                NameFeederPacket.class,
                NameFeederPacket::encode,
                NameFeederPacket::decode,
                NameFeederPacket::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER)
        );
        this.channel.registerMessage(
                id++,
                SyncFeederTrackerPacket.class,
                SyncFeederTrackerPacket::encode,
                SyncFeederTrackerPacket::decode,
                SyncFeederTrackerPacket::handle,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT)
        );
        this.channel.registerMessage(id++, FordExplorerChangeStateMessage.class,
                FordExplorerChangeStateMessage::encode,
                FordExplorerChangeStateMessage::decode,
                FordExplorerChangeStateMessage::handle);
        this.channel.registerMessage(id++, FordExplorerSnowChangeStateMessage.class,
                FordExplorerSnowChangeStateMessage::encode,
                FordExplorerSnowChangeStateMessage::decode,
                FordExplorerSnowChangeStateMessage::handle);
        this.channel.registerMessage(id++, MonorailChangeStateMessage.class,
                MonorailChangeStateMessage::encode,
                MonorailChangeStateMessage::decode,
                MonorailChangeStateMessage::handle);
        this.channel.registerMessage(id++, CarEntityPlayRecord.class,
                CarEntityPlayRecord::encode,
                CarEntityPlayRecord::decode,
                CarEntityPlayRecord::handle);
        this.channel.registerMessage(id++, UpdateVehicleControlMessage.class,
                UpdateVehicleControlMessage::encode,
                UpdateVehicleControlMessage::decode,
                UpdateVehicleControlMessage::handle);
        this.channel.registerMessage(id++, FordExplorerUpdatePositionStateMessage.class,
                FordExplorerUpdatePositionStateMessage::encode,
                FordExplorerUpdatePositionStateMessage::new,
                FordExplorerUpdatePositionStateMessage::handle,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT)
        );this.channel.registerMessage(id++, FordExplorerSnowUpdatePositionStateMessage.class,
                FordExplorerSnowUpdatePositionStateMessage::encode,
                FordExplorerSnowUpdatePositionStateMessage::new,
                FordExplorerSnowUpdatePositionStateMessage::handle,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT)
        );this.channel.registerMessage(id++, MonorailUpdatePositionStateMessage.class,
                MonorailUpdatePositionStateMessage::encode,
                MonorailUpdatePositionStateMessage::new,
                MonorailUpdatePositionStateMessage::handle,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT)
        );
        this.channel.registerMessage(id++, SwitchSeatMessage.class,
                SwitchSeatMessage::encode,
                SwitchSeatMessage::decode,
                SwitchSeatMessage::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
        this.channel.registerMessage(id++, ChangeStationMessage.class,
                ChangeStationMessage::encode,
                ChangeStationMessage::decode,
                ChangeStationMessage::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
        this.channel.registerMessage(id++, MicroraptorDismountMessage.class,
                MicroraptorDismountMessage::encode,
                MicroraptorDismountMessage::decode,
                MicroraptorDismountMessage::handle);
    }

    public static Network INSTANCE;
    public static final String version = "1";
    private final SimpleChannel channel = NetworkRegistry.ChannelBuilder
            .named(resource("main"))
            .serverAcceptedVersions((v) -> version.equals(v) || NetworkRegistry.ABSENT.equals(v) || NetworkRegistry.ACCEPTVANILLA.equals(v))
            .clientAcceptedVersions((v) -> version.equals(v) || NetworkRegistry.ABSENT.equals(v) || NetworkRegistry.ACCEPTVANILLA.equals(v))
            .networkProtocolVersion(() -> "1")
            .simpleChannel();
//    public static final SimpleChannel forge_channel = NetworkRegistry.newSimpleChannel(
//            resource("main_1"),
//            () -> version,
//            version::equals,
//            version::equals
//    );
    public static void init(){
        INSTANCE = new Network();
    }

    public static int id = 0;
    public static void sendToAllNear(Level level, BlockPos pos, double radius, Object msg) {
        INSTANCE.channel.send(
                PacketDistributor.NEAR.with(() ->
                        new PacketDistributor.TargetPoint(
                                pos.getX() + 0.5d,
                                pos.getY() + 0.5d,
                                pos.getZ() + 0.5d,
                                radius,
                                level.dimension()
                        )
                ),
                msg
        );
    }

    /*...*/
    public static void sendToAllNear(Level level, BlockPos pos, Object msg) {
        sendToAllNear(level, pos, 64.0d, msg);
    }
    public static final List<BlockEntity> ENTITIES = Collections.synchronizedList(new ArrayList<>());
    public static HashMap<BlockPos, Int2ObjectArrayMap<ItemStack>> slotMap = new HashMap<>();

    public static void switchHybridizerCombinerMode(boolean mode, BlockPos pos, ResourceKey<Level> dimension){
        INSTANCE.channel.sendToServer(new SwitchHybridizerCombinatorMode(mode, pos, dimension));
    }

    public static void setIncubatorTemperature(BlockPos incubator, int slot, int temp, ResourceKey<Level> dimension){
        INSTANCE.channel.sendToServer(new SetIncubatorTempServerboundPacket(incubator, slot, temp, dimension));
    }

    public static void removeRemovedEntities(){
        synchronized (ENTITIES) {
            ENTITIES.removeIf(BlockEntity::isRemoved);
        }
    }
    public static void sendToServer(Object message) {
        INSTANCE.channel.sendToServer(message);
    }
    public static void sendTo(ServerPlayer player, Object message) {
        INSTANCE.channel.send(PacketDistributor.PLAYER.with(() -> player), message);
    }
}
