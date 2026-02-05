package net.vit.jurassicreborn.common.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public interface ModPacket<T extends ModPacket<T>> {


    void handleOnRenderThread(T packet, Supplier<NetworkEvent.Context> context);

    void write(FriendlyByteBuf buffer);


}
