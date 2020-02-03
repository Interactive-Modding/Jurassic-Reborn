package mod.reborn.server.datafixers;

import com.google.common.collect.Lists;
import mod.reborn.RebornMod;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.FixTypes;
import net.minecraftforge.common.util.ModFixs;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.List;

public class RebornModDataFixers {
    private static final int DATAFIXER_VERSION = 2;
    private static final ModFixs modFixs = FMLCommonHandler.instance().getDataFixer().init(RebornMod.MODID, DATAFIXER_VERSION);

    public static void init() {

        List<String> tileEntityList = Lists.newArrayList("tour_rail", "cultivate_bottom", "cleaning_station", "fossil_grinder", "dna_sequencer", "dna_synthesizer", "embryonic_machine", "embryo_calcification_machi", "dna_extractor", "dna_combinator_hybridizer", "incubator", "display_block", "feeder", "bug_crate", "tileentityelectricfence", "tileentityelectricpole", "tileentityelectricbase");
        modFixs.registerFix(FixTypes.BLOCK_ENTITY, new DataFixerFactory(1, nbt -> {
            ResourceLocation teLoc = new ResourceLocation(nbt.getString("id"));
            String path = teLoc.getResourcePath();
            if(teLoc.getResourceDomain().equals("minecraft") && tileEntityList.contains(path)) {
                nbt.setString("id", RebornMod.MODID + ":" + path);
            }
            return nbt;
        }));
        
        modFixs.registerFix(FixTypes.ITEM_INSTANCE, new DataFixerFactory(2, nbt -> {
        	if("rebornmod:iron_nugget".equals(nbt.getString("id")))
        		nbt.setString("id", "minecraft:iron_nugget");	
            return nbt;
        }));

        modFixs.registerFix(FixTypes.ENTITY, new DataFixerFactory(1, compound -> {
            if("rebornmod.mural".equals(compound.getString("id"))) {
                compound.setString("id", "rebornmod:entities.mural");
            }
            return compound;
        }));
    }
}
