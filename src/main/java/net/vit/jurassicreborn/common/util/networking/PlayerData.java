package net.vit.jurassicreborn.common.util.networking;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.vit.jurassicreborn.common.paleopad.App;
import net.vit.jurassicreborn.common.paleopad.AppHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.text.html.parser.Entity;
import java.util.*;

public class PlayerData {
    public static final String IDENTIFIER = "jurassicreborn:playerdata";

    private final Map<String, CompoundTag> appdata = new HashMap<>();
    private final List<App> openApps = new ArrayList<>();

    // --- Capability Hooks ---

    public void saveNBTData(CompoundTag nbt) {
        ListTag appDataList = new ListTag();

        for (Map.Entry<String, CompoundTag> data : appdata.entrySet()) {
            CompoundTag appData = new CompoundTag();
            appData.putString("Name", data.getKey());
            appData.put("Data", data.getValue().copy());
            appDataList.add(appData);
        }
        nbt.put("RBAppData", appDataList);
    }

    public void loadNBTData(CompoundTag nbt) {
        appdata.clear();
        ListTag appDataList = nbt.getList("RBAppData", CompoundTag.TAG_COMPOUND);

        for (int i = 0; i < appDataList.size(); i++) {
            CompoundTag appData = appDataList.getCompound(i);
            String name = appData.getString("Name");
            CompoundTag data = appData.getCompound("Data");
            appdata.put(name, data);

            for (App app : AppHandler.INSTANCE.getApps()) {
                if (app.getName().equals(name)) {
                    app.readAppFromNBT(data);
                    break;
                }
            }
        }
    }

    public List<App> getOpenApps() {
        return openApps;
    }

    public Map<String, CompoundTag> getAppdata() {
        return appdata;
    }

    public void openApp(App app) {
        if (appdata.containsKey(app.getName())) {
            app.readAppFromNBT(appdata.get(app.getName()));
        }
        app.init();
        app.open();
        if (!openApps.contains(app)) openApps.add(app);
    }

    public void closeApp(App app) {
        CompoundTag data = new CompoundTag();
        app.writeAppToNBT(data);
        appdata.put(app.getName(), data);
        openApps.remove(app);
    }

    // --- Capability getter ---

    /**
     * Retrieve PlayerData from a player.
     */
    public static final Capability<PlayerData> CAPABILITY = CapabilityManager.get(new CapabilityToken<>(){});

    public static void attach(Player player, AttachCapabilitiesEvent<Entity> event) {
        if (player instanceof Player) {
            event.addCapability(new ResourceLocation("jurassicreborn", "playerdata"), new PlayerDataProvider());
        }
    }

    // Get instance for this player
    public static PlayerData get(Player player) {
        return player.getCapability(CAPABILITY).orElseThrow(() -> new IllegalStateException("No PlayerData capability attached!"));
    }
    public static class PlayerDataProvider implements ICapabilitySerializable<CompoundTag> {
        private final PlayerData instance = new PlayerData();
        private final LazyOptional<PlayerData> optional = LazyOptional.of(() -> instance);

        @Nonnull
        @Override
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
            return cap == CAPABILITY ? optional.cast() : LazyOptional.empty();
        }

        @Override
        public CompoundTag serializeNBT() {
            CompoundTag tag = new CompoundTag();
            instance.saveNBTData(tag);
            return tag;
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            instance.loadNBTData(nbt);
        }
    }

}
