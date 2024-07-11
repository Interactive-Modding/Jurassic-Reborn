package mod.reborn.server.entity.ai.util;

import com.google.common.collect.Lists;
import mod.reborn.RebornMod;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ServerTickEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.List;
import java.util.function.Supplier;

@EventBusSubscriber(modid = RebornMod.MODID)
public class InterpValue implements INBTSerializable<NBTTagCompound> {

    private static final List<InterpValue> INSTANCES = Lists.newArrayList();
    private static final List<InterpValue> MARKED_REMOVE = Lists.newArrayList();

    private final Supplier<Boolean> supplier;

    private double speed;
    private double target;
    private double current;
    private double previousCurrent;
    private boolean initialized;

    public InterpValue(Entity entity, double speed) {
        this(entity::isEntityAlive, speed);
    }

    public InterpValue(Supplier<Boolean> supplier, double speed) {
        this.speed = speed;
        this.supplier = supplier;
        synchronized (INSTANCES) {
            INSTANCES.add(this);
        }
    }

    public void setTarget(double target) {
        if (!initialized) {
            initialized = true;
            reset(target);
        } else {
            this.target = target;
        }
    }

    public void reset(double target) {
        this.previousCurrent = target;
        this.current = target;
        this.target = target;
    }

    private void tickInterp() {
        if (!supplier.get()) {
            synchronized (MARKED_REMOVE) {
                MARKED_REMOVE.add(this);
            }
            return;
        }
        this.previousCurrent = current;
        if (Math.abs(current - target) <= speed) {
            current = target;
        } else if (current < target) {
            current += speed;
        } else {
            current -= speed;
        }
    }

    public double getValueForRendering(float partialTicks) {
        return previousCurrent + (current - previousCurrent) * partialTicks;
    }

    public double getCurrent() {
        return current;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setDouble("target", target);
        tag.setDouble("current", current);
        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        this.target = nbt.getDouble("target");
        this.current = nbt.getDouble("current");
        this.previousCurrent = current;
    }

    @SubscribeEvent
    public static void onTick(TickEvent event) {
        Side side = FMLCommonHandler.instance().getSide();
        if ((event instanceof ClientTickEvent && side.isClient()) || (event instanceof ServerTickEvent && side.isServer())) {
            synchronized (INSTANCES) {
                List<InterpValue> toRemove = Lists.newArrayList();
                for (InterpValue interpValue : INSTANCES) {
                    interpValue.tickInterp();
                    if (MARKED_REMOVE.contains(interpValue)) {
                        toRemove.add(interpValue);
                    }
                }
                synchronized (MARKED_REMOVE) {
                    INSTANCES.removeAll(toRemove);
                    MARKED_REMOVE.clear();
                }
            }
        }
    }
}
