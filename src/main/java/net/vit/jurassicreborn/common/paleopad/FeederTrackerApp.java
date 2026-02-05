package net.vit.jurassicreborn.common.paleopad;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.vit.jurassicreborn.common.blocks.entities.feeder.FeederBlockEntity;
import net.vit.jurassicreborn.common.util.networking.PlayerData;

import java.util.ArrayList;
import java.util.List;

/**
 * App that tracks feeders and their food levels.
 */
public class FeederTrackerApp extends App {

    /** entries for tracked feeders */
    public static class TrackedFeeder {
        public final BlockPos pos;
        public String name;

        public TrackedFeeder(BlockPos pos, String name) {
            this.pos = pos;
            this.name = name;
        }
    }

    public static final int LOW_FOOD_THRESHOLD = 10;

    private final List<TrackedFeeder> feeders = new ArrayList<>();

    @Override
    public String getName() {
        return "Feeder Tracker";
    }

    public List<TrackedFeeder> getFeeders() {
        return feeders;
    }

    public void registerFeeder(BlockPos pos, String name) {
        for (TrackedFeeder f : feeders) {
            if (f.pos.equals(pos)) {
                f.name = name;
                return;
            }
        }
        feeders.add(new TrackedFeeder(pos, name));
    }

    /**
     * Remove a feeder from the registry.
     */
    public void unregisterFeeder(BlockPos pos) {
        feeders.removeIf(f -> f.pos.equals(pos));
    }

    /**
     * Utility called when a player registers a feeder via the Paleo Pad.
     */
    public static void addFeeder(Player player, BlockPos pos, Component name) {
        FeederTrackerApp app = (FeederTrackerApp) AppHandler.INSTANCE.feederTracker;
        PlayerData data = PlayerData.get(player);

        // Load existing data if the app isn't currently open
        if (!data.getOpenApps().contains(app)) {
            CompoundTag existing = data.getAppdata().get(app.getName());
            if (existing != null) {
                app.readAppFromNBT(existing);
            }
        }

        app.registerFeeder(pos, name.getString());

        // Persist back to the player's stored app data
        CompoundTag out = new CompoundTag();
        app.writeAppToNBT(out);
        data.getAppdata().put(app.getName(), out);

        FeederTrackerServerHelper.sync(player, out);
    }

    /**
     * Utility called when a feeder is removed from the world.
     */
    public static void removeFeeder(Player player, BlockPos pos) {
        FeederTrackerApp app = (FeederTrackerApp) AppHandler.INSTANCE.feederTracker;
        PlayerData data = PlayerData.get(player);

        if (!data.getOpenApps().contains(app)) {
            CompoundTag existing = data.getAppdata().get(app.getName());
            if (existing != null) {
                app.readAppFromNBT(existing);
            }
        }

        app.unregisterFeeder(pos);

        CompoundTag out = new CompoundTag();
        app.writeAppToNBT(out);
        data.getAppdata().put(app.getName(), out);

        FeederTrackerServerHelper.sync(player, out);
    }

    /** Calculate total food items inside the feeder. */
    public int getFood(Level level, TrackedFeeder feeder) {
        if (level != null && level.getBlockEntity(feeder.pos) instanceof FeederBlockEntity be) {
            int total = 0;
            for (int i = 0; i < be.getContainerSize(); i++) {
                total += be.getItem(i).getCount();
            }
            return total;
        }
        return 0;
    }

    @Override
    public void update() { /* no-op */ }

    @Override
    public void writeToNBT(CompoundTag nbt) {
        ListTag list = new ListTag();
        for (TrackedFeeder f : feeders) {
            CompoundTag tag = new CompoundTag();
            tag.putInt("x", f.pos.getX());
            tag.putInt("y", f.pos.getY());
            tag.putInt("z", f.pos.getZ());
            tag.putString("Name", f.name);
            list.add(tag);
        }
        nbt.put("Feeders", list);
    }

    @Override
    public void readFromNBT(CompoundTag nbt) {
        feeders.clear();
        ListTag list = nbt.getList("Feeders", CompoundTag.TAG_COMPOUND);
        for (int i = 0; i < list.size(); i++) {
            CompoundTag tag = list.getCompound(i);
            BlockPos pos = new BlockPos(tag.getInt("x"), tag.getInt("y"), tag.getInt("z"));
            String name = tag.getString("Name");
            feeders.add(new TrackedFeeder(pos, name));
        }
    }

    @Override
    public void init() { /* nothing needed */ }
}

