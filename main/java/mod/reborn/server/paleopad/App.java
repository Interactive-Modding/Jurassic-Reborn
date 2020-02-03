package mod.reborn.server.paleopad;

import net.minecraft.nbt.NBTTagCompound;

public abstract class App
{
    private boolean previouslyOpened;

    public abstract String getName();

    public abstract void update();

    public void readAppFromNBT(NBTTagCompound nbt)
    {
        this.readFromNBT(nbt);
        previouslyOpened = nbt.getBoolean("PreviouslyOpened");
    }

    public void writeAppToNBT(NBTTagCompound nbt)
    {
        this.writeToNBT(nbt);
        nbt.setBoolean("PreviouslyOpened", previouslyOpened);
    }

    public abstract void writeToNBT(NBTTagCompound nbt);

    public abstract void readFromNBT(NBTTagCompound nbt);

    public abstract void init();

    public void open()
    {
        previouslyOpened = true;
    }

    public boolean hasBeenPreviouslyOpened()
    {
        return previouslyOpened;
    }
}