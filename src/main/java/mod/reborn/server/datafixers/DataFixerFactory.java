package mod.reborn.server.datafixers;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;

import java.util.function.Function;

public class DataFixerFactory implements IFixableData {
    private final int version;
    private final Function<NBTTagCompound, NBTTagCompound> function;

    public DataFixerFactory(int version, Function<NBTTagCompound, NBTTagCompound> function) {
        this.version = version;
        this.function = function;
    }

    @Override
    public int getFixVersion() {
        return version;
    }

    @Override
    public NBTTagCompound fixTagCompound(NBTTagCompound compound) {
        return function.apply(compound);
    }
}
