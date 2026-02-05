package net.vit.jurassicreborn.common.util.block;



public interface TemperatureControl{
    void setTemperature(int index, int value);

    int getTemperature(int index);

    int getTemperatureCount();
}
