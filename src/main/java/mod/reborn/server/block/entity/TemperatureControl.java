package mod.reborn.server.block.entity;

import net.minecraft.inventory.IInventory;

public interface TemperatureControl extends IInventory {
    void setTemperature(int index, int value);

    int getTemperature(int index);

    int getTemperatureCount();
}
