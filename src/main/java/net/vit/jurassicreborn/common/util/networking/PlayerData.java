package net.vit.jurassicreborn.common.util.networking;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.player.Player;
import net.vit.jurassicreborn.common.paleopad.App;
import net.vit.jurassicreborn.common.paleopad.AppHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PlayerData {
    public static final String IDENTIFIER = "jurassicreborn:playerdata";
    private static final Map<UUID, PlayerData> PLAYER_DATA = new HashMap<>();

    private final Map<String, CompoundTag> appdata = new HashMap<>();
    private final List<App> openApps = new ArrayList<>();

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

    public CompoundTag toTag() {
        CompoundTag tag = new CompoundTag();
        saveNBTData(tag);
        return tag;
    }

    public void loadFromTag(CompoundTag tag) {
        loadNBTData(tag);
    }

    private void loadFromPlayer(Player player) {
        CompoundTag tag = player.getPersistentData().getCompound(IDENTIFIER);
        if (!tag.isEmpty()) {
            loadNBTData(tag);
        }
    }

    private void saveToPlayer(Player player) {
        player.getPersistentData().put(IDENTIFIER, toTag());
    }

    public static PlayerData get(Player player) {
        return PLAYER_DATA.computeIfAbsent(player.getUUID(), uuid -> {
            PlayerData data = new PlayerData();
            if (!player.level().isClientSide) {
                data.loadFromPlayer(player);
            }
            return data;
        });
    }

    public static void save(Player player) {
        PlayerData data = PLAYER_DATA.get(player.getUUID());
        if (data != null) {
            data.saveToPlayer(player);
        }
    }

    public static void remove(Player player) {
        PLAYER_DATA.remove(player.getUUID());
    }

    public static void copy(Player original, Player player) {
        PlayerData originalData = PLAYER_DATA.get(original.getUUID());
        if (originalData == null) {
            originalData = new PlayerData();
            originalData.loadFromPlayer(original);
        }
        PlayerData clone = new PlayerData();
        clone.loadFromTag(originalData.toTag());
        PLAYER_DATA.put(player.getUUID(), clone);
        if (!player.level().isClientSide) {
            clone.saveToPlayer(player);
        }
    }
}
