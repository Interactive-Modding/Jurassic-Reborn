package mod.reborn.server.world.structure;

import mod.reborn.server.conf.RebornConfig;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.fml.common.FMLCommonHandler;
import mod.reborn.RebornMod;

import javax.annotation.Nonnull;

public class StructureUtils {

    private static final String DATA_ID = RebornMod.MODID + "_structure_info";

    @Nonnull
    public static StructureData getStructureData() {
        World world = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(0);
        if(world == null) {
            throw new RuntimeException("Overworld returned null");
        }
        StructureData data = (StructureData)world.loadData(StructureData.class, DATA_ID);
        if(data == null) {
            data = new StructureData(DATA_ID);
            data.markDirty(); //Is this really needed
            world.setData(DATA_ID, data);
        }
        return data;
    }

    public static class StructureData extends WorldSavedData {

        private boolean visitorCenter = RebornConfig.STRUCTURE_GENERATION.visitorcentergeneration;
        private boolean raptorPaddock = RebornConfig.STRUCTURE_GENERATION.raptorgeneration;
        private BlockPos visitorCenterPosition;

        public StructureData(String string) {
            super(string);
            if(!string.equals(DATA_ID)) {
                throw new RuntimeException("Invalid identifier: " + string);
            }
        }

        @Override
        public NBTTagCompound writeToNBT(NBTTagCompound compound) {
            compound.setBoolean("VisitorCenter", this.visitorCenter);
            compound.setBoolean("RaptorPaddock", this.raptorPaddock);
            compound.setLong("VisitorCenterBlockPosition", this.visitorCenterPosition.toLong());
            return compound;
        }

        @Override
        public void readFromNBT(NBTTagCompound nbt) {
            this.visitorCenter = nbt.getBoolean("VisitorCenter");
            this.raptorPaddock = nbt.getBoolean("RaptorPaddock");
            this.visitorCenterPosition = BlockPos.fromLong(nbt.getLong("VisitorCenterBlockPosition"));
        }

        public boolean isVisitorCenter() {
            return visitorCenter;
        }

        public boolean isRaptorPaddock() {
            return raptorPaddock;
        }

        public BlockPos getVisitorCenterPosition() {
            return visitorCenterPosition;
        }

        public void setVisitorCenterPosition(BlockPos visitorCenterPosition) {
            this.visitorCenterPosition = visitorCenterPosition;
            this.markDirty();
        }
    }
}