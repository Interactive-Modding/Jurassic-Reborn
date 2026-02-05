package net.vit.jurassicreborn.common.worldgen.structure;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraftforge.server.ServerLifecycleHooks;
import net.vit.jurassicreborn.JurassicReborn;

import javax.annotation.Nonnull;

/**
 * Utilities for structure generation data storage.
 */
public class StructureUtils {
    private static final String DATA_ID = JurassicReborn.MODID + "_structure_info";

    @Nonnull
    public static StructureData getStructureData() {
        ServerLevel level = ServerLifecycleHooks.getCurrentServer().overworld();
        return level.getDataStorage().computeIfAbsent(StructureData::load, StructureData::new, DATA_ID);
    }

    public static class StructureData extends SavedData {
        private boolean visitorCenter = true;
        private boolean islaSornaLab = true;
        private boolean jpSanDiego = true;
        private boolean raptorPaddock = true;
        private boolean iceFossilDigsite = true;
        private boolean desertDigsite = true;
        private boolean amberMine = true;
        private boolean abandonedPaddock = true;

        private BlockPos visitorCenterPosition = BlockPos.ZERO;
        private BlockPos islaSornaLabPosition = BlockPos.ZERO;
        private BlockPos jpSanDiegoPosition = BlockPos.ZERO;

        public StructureData() {
            super();
        }

        public StructureData(String id) {
            super();
        }

        public static StructureData load(CompoundTag tag) {
            StructureData data = new StructureData();
            data.read(tag);
            return data;
        }

        @Override
        public CompoundTag save(CompoundTag tag) {
            tag.putBoolean("VisitorCenter", visitorCenter);
            tag.putBoolean("IslaSornaLab", islaSornaLab);
            tag.putBoolean("JPSanDiego", jpSanDiego);
            tag.putBoolean("RaptorPaddock", raptorPaddock);
            tag.putBoolean("AbandonedPaddock", abandonedPaddock);
            tag.putBoolean("IceFossilDigsite", iceFossilDigsite);
            tag.putBoolean("DesertDigsite", desertDigsite);
            tag.putBoolean("AmberMine", amberMine);
            tag.putLong("VisitorCenterBlockPosition", visitorCenterPosition.asLong());
            tag.putLong("IslaSornaLabBlockPosition", islaSornaLabPosition.asLong());
            tag.putLong("SanDiegoBlockPosition", jpSanDiegoPosition.asLong());
            return tag;
        }

        public void read(CompoundTag tag) {
            visitorCenter = tag.getBoolean("VisitorCenter");
            islaSornaLab = tag.getBoolean("IslaSornaLab");
            jpSanDiego = tag.getBoolean("JPSanDiego");
            raptorPaddock = tag.getBoolean("RaptorPaddock");
            abandonedPaddock = tag.getBoolean("AbandonedPaddock");
            iceFossilDigsite = tag.getBoolean("IceFossilDigsite");
            desertDigsite = tag.getBoolean("DesertDigsite");
            amberMine = tag.getBoolean("AmberMine");
            visitorCenterPosition = BlockPos.of(tag.getLong("VisitorCenterBlockPosition"));
            islaSornaLabPosition = BlockPos.of(tag.getLong("IslaSornaLabBlockPosition"));
            jpSanDiegoPosition = BlockPos.of(tag.getLong("SanDiegoBlockPosition"));
        }

        public boolean isVisitorCenter() {
            return visitorCenter;
        }

        public boolean isIslaSornaLab() {
            return islaSornaLab;
        }

        public boolean isJPSanDiego() {
            return jpSanDiego;
        }

        public boolean isRaptorPaddock() {
            return raptorPaddock;
        }

        public boolean isAbandonedPaddock() {
            return abandonedPaddock;
        }

        public boolean isIceFossilDigsite() {
            return iceFossilDigsite;
        }

        public boolean isDesertDigsite() {
            return desertDigsite;
        }

        public boolean isAmberMine() {
            return amberMine;
        }

        public BlockPos getVisitorCenterPosition() {
            return visitorCenterPosition;
        }

        public BlockPos getIslaSornaLabPosition() {
            return islaSornaLabPosition;
        }

        public BlockPos getJPSanDiegoPosition() {
            return jpSanDiegoPosition;
        }

        public void setIslaSornaLabPosition(BlockPos pos) {
            islaSornaLabPosition = pos;
            setDirty();
        }

        public void setJPSanDiegoPosition(BlockPos pos) {
            jpSanDiegoPosition = pos;
            setDirty();
        }

        public void setVisitorCenterPosition(BlockPos pos) {
            visitorCenterPosition = pos;
            setDirty();
        }
    }
}
