package net.vit.jurassicreborn.common.entities.vehicle;

import com.google.common.collect.Lists;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.vit.jurassicreborn.JurassicReborn;

import java.util.List;
import java.util.function.Supplier;

@EventBusSubscriber(modid = JurassicReborn.MODID, bus = EventBusSubscriber.Bus.GAME)
public class InterpValue {
    private static final List<InterpValue> VALUES = Lists.newArrayList();
    private static final List<InterpValue> TO_REMOVE = Lists.newArrayList();

    private final Supplier<Boolean> aliveCheck;
    private double speed, target, current, prev;
    private boolean init;

    public InterpValue(Entity e, double speed) {
        this(e::isAlive, speed);
    }
    public double getCurrent() { return current; }

    /** Adjust the speed at which we approach the target value. */
    public void setSpeed(double speed) { this.speed = speed; }
    public InterpValue(Supplier<Boolean> aliveCheck, double speed) {
        this.aliveCheck = aliveCheck;
        this.speed = speed;
        synchronized (VALUES) {
            VALUES.add(this);
        }
    }
    public double getValueForRendering(float partialTicks) {
        return getRenderValue(partialTicks);
    }

    public boolean tickClient() {          // returns true if fully at target
        tick();
        return current == target;
    }
    public void setTarget(double t) {
        if (!init) { init = true; reset(t); }
        else this.target = t;
    }

    public void reset(double t) {
        prev = current = target = t;
    }

    public double getRenderValue(float partial) {
        return prev + (current - prev) * partial;
    }

    // ---- internal ticking ----
    public void tick() {
        if (!aliveCheck.get()) {
            synchronized (TO_REMOVE) { TO_REMOVE.add(this); }
            return;
        }
        prev = current;
        if (Math.abs(current - target) <= speed) current = target;
        else current += current < target ? speed : -speed;
    }
    public void update() {
        this.tick();
    }
    @SubscribeEvent
    public static void onLevelTick(LevelTickEvent.Post event) {
        synchronized (VALUES) {
            VALUES.forEach(InterpValue::tick);   // advance each value
            VALUES.removeAll(TO_REMOVE);         // purge those flagged for deletion
            TO_REMOVE.clear();
        }
    }

    // ---- NBT ----
    public CompoundTag writeToNBT(HolderLookup.Provider provider, CompoundTag tag) {
        tag.putDouble("target", target);
        tag.putDouble("current", current);
        return tag;
    }
    public void readFromNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        target = current = prev = nbt.getDouble("current");
    }
}
