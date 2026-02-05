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

    public static RegistryObject<MenuType<CleanerMenu>> CLEANER = MENU_TYPES.register("cleaner", () -> new MenuType<>(CleanerMenu::new));

    public static RegistryObject<MenuType<DNACombinatorHybridizerMenu>> COMBINATOR = MENU_TYPES.register("combinator", () -> new MenuType<>(DNACombinatorHybridizerMenu::new));

    public static RegistryObject<MenuType<FossilGrinderMenu>> FOSSIL_GRINDER = MENU_TYPES.register("fossil_grinder", () -> new MenuType<>(FossilGrinderMenu::new));

    public static RegistryObject<MenuType<FeederMenu>> FEEDER = MENU_TYPES.register("feeder", () -> new MenuType<>(FeederMenu::new));
    public static RegistryObject<MenuType<BugCrateMenu>> BUG_CRATE = MENU_TYPES.register("bug_crate", () -> new MenuType<>(BugCrateMenu::new));
    public static RegistryObject<MenuType<TrashCanMenu>> TRASH_CAN = MENU_TYPES.register("trash_can", () -> new MenuType<>(TrashCanMenu::new));

    public static final RegistryObject<MenuType<SkeletonAssemblerMenu>> SKELETON_ASSEMBLER = MENU_TYPES.register("skeleton_assembly", () -> IForgeMenuType.create(SkeletonAssemblerMenu::new));
    public static RegistryObject<MenuType<DNASequencerMenu>> DNA_SEQUENCER = MENU_TYPES.register("dna_sequencer", () -> new MenuType<>(DNASequencerMenu::new));

    public static RegistryObject<MenuType<DNAExtractorMenu>> DNA_EXTRACTOR = MENU_TYPES.register("dna_extractor", () -> new MenuType<>(DNAExtractorMenu::new));

    public static RegistryObject<MenuType<DNASynthesizerMenu>> DNA_SYNTHESIZER = MENU_TYPES.register("dna_synthesizer", () -> new MenuType<>(DNASynthesizerMenu::new));

    public static RegistryObject<MenuType<IncubatorMenu>> INCUBATOR = MENU_TYPES.register("incubator", () -> new MenuType<>(IncubatorMenu::new));

    public static RegistryObject<MenuType<EmbryonicMachineMenu>> EMBRYONIC_MACHINE = MENU_TYPES.register("embryonic_machine", () -> new MenuType<>(EmbryonicMachineMenu::new));
    public static RegistryObject<MenuType<EmbryoCalcificationMachineMenu>> EMBRYO_CALCIFICATION_MACHINE = MENU_TYPES.register("embryo_calcification_machine", () -> new MenuType<>(EmbryoCalcificationMachineMenu::new));
    public static RegistryObject<MenuType<CultivatorMenu>> CULTIVATOR = MENU_TYPES.register("cultivator", () -> new MenuType<>(CultivatorMenu::new));


    public interface ModMenuSupplier<T extends AbstractContainerMenu> {

        T create(int id, Inventory inv, BlockEntity entity);
    }

}
