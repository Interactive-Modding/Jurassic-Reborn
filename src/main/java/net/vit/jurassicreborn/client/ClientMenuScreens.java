package net.vit.jurassicreborn.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.client.screens.*;
import net.vit.jurassicreborn.common.blocks.entities.ModMenuTypes;

@EventBusSubscriber(
        modid = JurassicReborn.MODID,
        bus = EventBusSubscriber.Bus.MOD,
        value = Dist.CLIENT
)
public final class ClientMenuScreens {

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {

        event.register(ModMenuTypes.FOSSIL_GRINDER.get(), FossilGrinderScreen::new);
        event.register(ModMenuTypes.CLEANER.get(), CleanerScreen::new);
        event.register(ModMenuTypes.COMBINATOR.get(), DNACombinatorHybridizerScreen::new);
        event.register(ModMenuTypes.DNA_SEQUENCER.get(), DNASequencerScreen::new);
        event.register(ModMenuTypes.DNA_EXTRACTOR.get(), DNAExtractorScreen::new);
        event.register(ModMenuTypes.DNA_SYNTHESIZER.get(), DNASynthesizerScreen::new);
        event.register(ModMenuTypes.INCUBATOR.get(), IncubatorScreen::new);
        event.register(ModMenuTypes.EMBRYONIC_MACHINE.get(), EmbryronicMachineScreen::new);
        event.register(ModMenuTypes.EMBRYO_CALCIFICATION_MACHINE.get(), EmbryoCalcificationMachineScreen::new);
        event.register(ModMenuTypes.CULTIVATOR.get(), CultivatorScreen::new);
        event.register(ModMenuTypes.FEEDER.get(), FeederScreen::new);
        event.register(ModMenuTypes.BUG_CRATE.get(), BugCrateScreen::new);
        event.register(ModMenuTypes.TRASH_CAN.get(), TrashCanScreen::new);
        event.register(ModMenuTypes.SKELETON_ASSEMBLER.get(), SkeletonAssemblerScreen::new);
    }
}
