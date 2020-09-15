package mod.reborn;

import mod.reborn.server.message.*;
import mod.reborn.server.proxy.ServerProxy;
import net.ilexiconn.llibrary.server.network.NetworkWrapper;
import net.ilexiconn.llibrary.server.update.UpdateHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import org.apache.logging.log4j.Logger;
import mod.reborn.server.command.ForceAnimationCommand;
import mod.reborn.server.command.SpawnStructureCommand;

@Mod(modid = RebornMod.MODID, name = RebornMod.NAME, version = RebornMod.VERSION, dependencies = "required-after:llibrary@[1.7.15,);required-after:forge@[14.23.5.2772,)")
public class RebornMod {
    public static final String MODID = "rebornmod";
    public static final String NAME = "JWReborn";
    public static final String VERSION = "1.0-1.12.2";

    @SuppressWarnings("unused")
    public static final String LLIBRARY_VERSION = "1.7.15";
    @SidedProxy(serverSide = "mod.reborn.server.proxy.ServerProxy", clientSide = "mod.reborn.client.proxy.ClientProxy")
    public static ServerProxy PROXY;

    @Instance(RebornMod.MODID)
    public static RebornMod INSTANCE;

    public static long timerTicks;

    @NetworkWrapper({ OpenPaleoPadEntityMessage.class, PlacePaddockSignMessage.class, ChangeTemperatureMessage.class, SwitchHybridizerCombinatorMode.class, SetOrderMessage.class, OpenFieldGuideGuiMessage.class, UpdateVehicleControlMessage.class, BiPacketOrder.class, MicroraptorDismountMessage.class, FordExplorerChangeStateMessage.class, FordExplorerUpdatePositionStateMessage.class, DNASequenceTransferClicked.class, CultivatorSyncNutrients.class, CarEntityPlayRecord.class, AttemptMoveToSeatMessage.class, TileEntityFieldsMessage.class, HelicopterDirectionMessage.class, HelicopterEngineMessage.class})
    public static SimpleNetworkWrapper NETWORK_WRAPPER;

    private static Logger logger;
    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        PROXY.onPreInit(event);
    }

    @Mod.EventHandler
    public void onInit(FMLInitializationEvent event) {
        PROXY.onInit(event);
    }

    @Mod.EventHandler
    public void onPostInit(FMLPostInitializationEvent event) {
        PROXY.onPostInit(event);
    }

    @Mod.EventHandler
    public void onServerStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new ForceAnimationCommand());
        event.registerServerCommand(new SpawnStructureCommand());
    }

    public static Logger getLogger() {
        return logger;
    }
}