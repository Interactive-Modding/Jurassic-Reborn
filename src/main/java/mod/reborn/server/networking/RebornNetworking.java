package mod.reborn.common.networking;

import mod.reborn.RebornMod;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class RebornNetworking {

    public static SimpleChannel instance;
    private static int ID = 0;

    public static int NextID() {
        return ID++;
    }

    public static void registerMessages() {
        instance = NetworkRegistry.newSimpleChannel(new ResourceLocation(RebornMod.MOD_ID, "networking"), () -> "1.0", s -> true, s -> true);
    }
}
