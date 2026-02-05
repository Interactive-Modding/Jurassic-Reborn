package net.vit.jurassicreborn.common.blocks.entities;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.vit.jurassicreborn.common.blocks.entities.DNABlocks.DNACombinatorHybridizer.DNACombinatorHybridizerMenu;
import net.vit.jurassicreborn.common.blocks.entities.DNABlocks.DNAExtractor.DNAExtractorMenu;
import net.vit.jurassicreborn.common.blocks.entities.DNABlocks.DNASequencer.DNASequencerMenu;
import net.vit.jurassicreborn.common.blocks.entities.DNABlocks.DNASynthesizer.DNASynthesizerMenu;
import net.vit.jurassicreborn.common.blocks.entities.Embryoncis.EmbryoCalcificationMachine.EmbryoCalcificationMachineMenu;
import net.vit.jurassicreborn.common.blocks.entities.Embryoncis.EmbryonicMachine.EmbryonicMachineMenu;
import net.vit.jurassicreborn.common.blocks.entities.bugcrate.BugCrateMenu;
import net.vit.jurassicreborn.common.blocks.entities.cleaner.CleanerMenu;
import net.vit.jurassicreborn.common.blocks.entities.cultivator.CultivatorMenu;
import net.vit.jurassicreborn.common.blocks.entities.feeder.FeederMenu;
import net.vit.jurassicreborn.common.blocks.entities.trashcan.TrashCanMenu;
import net.vit.jurassicreborn.common.blocks.entities.grinder.FossilGrinderMenu;
import net.vit.jurassicreborn.common.blocks.entities.incubator.IncubatorMenu;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.common.blocks.entities.skeletonassembly.SkeletonAssemblerMenu;

import java.util.HashMap;

public class ModMenuTypes {

    public static HashMap<ResourceLocation, ModMenuSupplier<?>> modMenuSupplier = new HashMap<>();
    public static DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.MENU_TYPES, JurassicReborn.MODID);

    public static RegistryObject<MenuType<CleanerMenu>> CLEANER = MENU_TYPES.register("cleaner", () -> IForgeMenuType.create((id, inv, data) -> new CleanerMenu(id, inv)));

    public static RegistryObject<MenuType<DNACombinatorHybridizerMenu>> COMBINATOR = MENU_TYPES.register("combinator", () -> IForgeMenuType.create((id, inv, data) -> new DNACombinatorHybridizerMenu(id, inv)));

    public static RegistryObject<MenuType<FossilGrinderMenu>> FOSSIL_GRINDER = MENU_TYPES.register("fossil_grinder", () -> IForgeMenuType.create((id, inv, data) -> new FossilGrinderMenu(id, inv)));

    public static RegistryObject<MenuType<FeederMenu>> FEEDER = MENU_TYPES.register("feeder", () -> IForgeMenuType.create((id, inv, data) -> new FeederMenu(id, inv)));
    public static RegistryObject<MenuType<BugCrateMenu>> BUG_CRATE = MENU_TYPES.register("bug_crate", () -> IForgeMenuType.create((id, inv, data) -> new BugCrateMenu(id, inv)));
    public static RegistryObject<MenuType<TrashCanMenu>> TRASH_CAN = MENU_TYPES.register("trash_can", () -> IForgeMenuType.create((id, inv, data) -> new TrashCanMenu(id, inv)));

    public static final RegistryObject<MenuType<SkeletonAssemblerMenu>> SKELETON_ASSEMBLER = MENU_TYPES.register("skeleton_assembly", () -> IForgeMenuType.create((id, inv, data) -> new SkeletonAssemblerMenu(id, inv,data)));
    public static RegistryObject<MenuType<DNASequencerMenu>> DNA_SEQUENCER = MENU_TYPES.register("dna_sequencer", () -> IForgeMenuType.create((id, inv, data) -> new DNASequencerMenu(id, inv)));

    public static RegistryObject<MenuType<DNAExtractorMenu>> DNA_EXTRACTOR = MENU_TYPES.register("dna_extractor", () -> IForgeMenuType.create((id, inv, data) -> new DNAExtractorMenu(id, inv)));

    public static RegistryObject<MenuType<DNASynthesizerMenu>> DNA_SYNTHESIZER = MENU_TYPES.register("dna_synthesizer", () -> IForgeMenuType.create((id, inv, data) -> new DNASynthesizerMenu(id, inv)));

    public static RegistryObject<MenuType<IncubatorMenu>> INCUBATOR = MENU_TYPES.register("incubator", () -> IForgeMenuType.create((id, inv, data) -> new IncubatorMenu(id, inv)));

    public static RegistryObject<MenuType<EmbryonicMachineMenu>> EMBRYONIC_MACHINE = MENU_TYPES.register("embryonic_machine", () -> IForgeMenuType.create((id, inv, data) -> new EmbryonicMachineMenu(id, inv)));
    public static RegistryObject<MenuType<EmbryoCalcificationMachineMenu>> EMBRYO_CALCIFICATION_MACHINE = MENU_TYPES.register("embryo_calcification_machine", () -> IForgeMenuType.create((id, inv, data) -> new EmbryoCalcificationMachineMenu(id, inv)));
    public static RegistryObject<MenuType<CultivatorMenu>> CULTIVATOR = MENU_TYPES.register("cultivator", () -> IForgeMenuType.create((id, inv, data) -> new CultivatorMenu(id, inv)));


    public interface ModMenuSupplier<T extends AbstractContainerMenu> {

        T create(int id, Inventory inv, BlockEntity entity);
    }

}
