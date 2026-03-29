package net.vit.jurassicreborn.common.blocks.entities;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import net.vit.jurassicreborn.JurassicReborn;
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
import net.vit.jurassicreborn.common.blocks.entities.grinder.FossilGrinderMenu;
import net.vit.jurassicreborn.common.blocks.entities.incubator.IncubatorMenu;
import net.vit.jurassicreborn.common.blocks.entities.skeletonassembly.SkeletonAssemblerMenu;
import net.vit.jurassicreborn.common.blocks.entities.trashcan.TrashCanMenu;

import java.util.HashMap;

public class ModMenuTypes {

    public static final HashMap<ResourceLocation, ModMenuSupplier<?>> MOD_MENU_SUPPLIERS = new HashMap<>();

    public static final DeferredRegister<MenuType<?>> MENU_TYPES =
            DeferredRegister.create(Registries.MENU, JurassicReborn.MODID);

    public static final DeferredHolder<MenuType<?>, MenuType<CleanerMenu>> CLEANER =
            MENU_TYPES.register("cleaner",
                    () -> new MenuType<>(CleanerMenu::new, FeatureFlags.VANILLA_SET));

    public static final DeferredHolder<MenuType<?>, MenuType<DNACombinatorHybridizerMenu>> COMBINATOR =
            MENU_TYPES.register("combinator",
                    () -> new MenuType<>(DNACombinatorHybridizerMenu::new, FeatureFlags.VANILLA_SET));

    public static final DeferredHolder<MenuType<?>, MenuType<FossilGrinderMenu>> FOSSIL_GRINDER =
            MENU_TYPES.register("fossil_grinder",
                    () -> new MenuType<>(FossilGrinderMenu::new, FeatureFlags.VANILLA_SET));

    public static final DeferredHolder<MenuType<?>, MenuType<FeederMenu>> FEEDER =
            MENU_TYPES.register("feeder",
                    () -> new MenuType<>(FeederMenu::new, FeatureFlags.VANILLA_SET));

    public static final DeferredHolder<MenuType<?>, MenuType<BugCrateMenu>> BUG_CRATE =
            MENU_TYPES.register("bug_crate",
                    () -> new MenuType<>(BugCrateMenu::new, FeatureFlags.VANILLA_SET));

    public static final DeferredHolder<MenuType<?>, MenuType<TrashCanMenu>> TRASH_CAN =
            MENU_TYPES.register("trash_can",
                    () -> new MenuType<>(TrashCanMenu::new, FeatureFlags.VANILLA_SET));

    public static final DeferredHolder<MenuType<?>, MenuType<SkeletonAssemblerMenu>> SKELETON_ASSEMBLER =
            MENU_TYPES.register("skeleton_assembly",
                    () -> new MenuType<>(SkeletonAssemblerMenu::new, FeatureFlags.VANILLA_SET));

    public static final DeferredHolder<MenuType<?>, MenuType<DNASequencerMenu>> DNA_SEQUENCER =
            MENU_TYPES.register("dna_sequencer",
                    () -> new MenuType<>(DNASequencerMenu::new, FeatureFlags.VANILLA_SET));

    public static final DeferredHolder<MenuType<?>, MenuType<DNAExtractorMenu>> DNA_EXTRACTOR =
            MENU_TYPES.register("dna_extractor",
                    () -> new MenuType<>(DNAExtractorMenu::new, FeatureFlags.VANILLA_SET));

    public static final DeferredHolder<MenuType<?>, MenuType<DNASynthesizerMenu>> DNA_SYNTHESIZER =
            MENU_TYPES.register("dna_synthesizer",
                    () -> new MenuType<>(DNASynthesizerMenu::new, FeatureFlags.VANILLA_SET));

    public static final DeferredHolder<MenuType<?>, MenuType<IncubatorMenu>> INCUBATOR =
            MENU_TYPES.register("incubator",
                    () -> new MenuType<>(IncubatorMenu::new, FeatureFlags.VANILLA_SET));

    public static final DeferredHolder<MenuType<?>, MenuType<EmbryonicMachineMenu>> EMBRYONIC_MACHINE =
            MENU_TYPES.register("embryonic_machine",
                    () -> new MenuType<>(EmbryonicMachineMenu::new, FeatureFlags.VANILLA_SET));

    public static final DeferredHolder<MenuType<?>, MenuType<EmbryoCalcificationMachineMenu>> EMBRYO_CALCIFICATION_MACHINE =
            MENU_TYPES.register("embryo_calcification_machine",
                    () -> new MenuType<>(EmbryoCalcificationMachineMenu::new, FeatureFlags.VANILLA_SET));

    public static final DeferredHolder<MenuType<?>, MenuType<CultivatorMenu>> CULTIVATOR =
            MENU_TYPES.register("cultivator",
                    () -> new MenuType<>(CultivatorMenu::new, FeatureFlags.VANILLA_SET));

    public static void register(IEventBus bus) {
        MENU_TYPES.register(bus);
    }

    public interface ModMenuSupplier<T extends AbstractContainerMenu> {
        T create(int id, Inventory inv, BlockEntity entity);
    }
}
