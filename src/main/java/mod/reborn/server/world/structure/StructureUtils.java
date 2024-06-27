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

        private boolean IslaSornaLab = RebornConfig.STRUCTURE_GENERATION.islasornalabgenereation;

        private boolean JPSanDiego = RebornConfig.STRUCTURE_GENERATION.jpsandiegogeneration;

        private boolean raptorPaddock = RebornConfig.STRUCTURE_GENERATION.raptorgeneration;

        private boolean IceFossilDigsite = RebornConfig.STRUCTURE_GENERATION.icegeneration;

        private boolean DesertDigsite = RebornConfig.STRUCTURE_GENERATION.desertgeneration;
        private boolean AmberMine = RebornConfig.STRUCTURE_GENERATION.minegeneration;

        private boolean AbandonedPaddock = RebornConfig.STRUCTURE_GENERATION.abandonedgeneration;
        private BlockPos visitorCenterPosition;

        private BlockPos IslaSornaLabPosition;

        private BlockPos JPSanDiegoPosition;


        public StructureData(String string) {
            super(string);
            if(!string.equals(DATA_ID)) {
                throw new RuntimeException("Invalid identifier: " + string);
            }
        }

        @Override
        public NBTTagCompound writeToNBT(NBTTagCompound compound) {
            compound.setBoolean("VisitorCenter", this.visitorCenter);
            compound.setBoolean("IslaSornaLab", this.IslaSornaLab);
            compound.setBoolean("JPSanDiego", this.JPSanDiego);
            compound.setBoolean("RaptorPaddock", this.raptorPaddock);
            compound.setBoolean("AbandonedPaddock",this.AbandonedPaddock);
            compound.setBoolean("IceFossilDigsite",this.IceFossilDigsite);
            compound.setBoolean("DesertDigsite",this.DesertDigsite);
            compound.setBoolean("AmberMine",this.AmberMine);
            compound.setLong("VisitorCenterBlockPosition", this.visitorCenterPosition.toLong());
            compound.setLong("IslaSornaLabBlockPosition", this.IslaSornaLabPosition.toLong());
            compound.setLong("SanDiegoBlockPosition",this.JPSanDiegoPosition.toLong());
            return compound;
        }

        @Override
        public void readFromNBT(NBTTagCompound nbt) {
            this.visitorCenter = nbt.getBoolean("VisitorCenter");
            this.IslaSornaLab = nbt.getBoolean("IslaSornaLab");
            this.JPSanDiego = nbt.getBoolean("JPSanDiego");
            this.raptorPaddock = nbt.getBoolean("RaptorPaddock");
            this.AbandonedPaddock= nbt.getBoolean("AbandonedPaddock");
            this.IceFossilDigsite= nbt.getBoolean("IceFossilDigsite");
            this.DesertDigsite= nbt.getBoolean("DesertDigsite");
            this.AmberMine= nbt.getBoolean("AmberMine");
            this.visitorCenterPosition = BlockPos.fromLong(nbt.getLong("VisitorCenterBlockPosition"));
            this.IslaSornaLabPosition = BlockPos.fromLong(nbt.getLong("IslaSornaLabPosition"));
            this.JPSanDiegoPosition = BlockPos.fromLong(nbt.getLong("SanDiegoPosition"));

        }

        public boolean isVisitorCenter() {
            return visitorCenter;
        }

        public boolean isIslaSornaLab()  {
            return IslaSornaLab;
        }
        public boolean isJPSanDiego()  {
            return JPSanDiego;
        }

        public boolean isRaptorPaddock() {
            return raptorPaddock;
        }

        public boolean isAbandonedPaddock() {
            return AbandonedPaddock;
        }
        public boolean isIceFossilDigsite() {
            return IceFossilDigsite;
        }
        public boolean isDesertDigsite() {
            return DesertDigsite;
        }

        public boolean isAmberMine() {
            return AmberMine;
        }


        public BlockPos getVisitorCenterPosition() {
            return visitorCenterPosition;
        }

        public BlockPos getIslaSornaLabPosition()  {
            return IslaSornaLabPosition;
        }

        public BlockPos getJPSanDiegoPosition()  {
            return JPSanDiegoPosition;
        }

        public void setIslaSornaLabPosition(BlockPos IslaSornaLabPosition) {
            this.IslaSornaLabPosition = IslaSornaLabPosition;
            this.markDirty();
        }

        public void setJPSanDiegoPosition(BlockPos JPSanDiegoPosition) {
            this.JPSanDiegoPosition = JPSanDiegoPosition;
            this.markDirty();
        }

        public void setVisitorCenterPosition(BlockPos visitorCenterPosition) {
            this.visitorCenterPosition = visitorCenterPosition;
            this.markDirty();
        }
    }
}