package net.vit.jurassicreborn.common.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.registries.BuiltInRegistries;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.common.blocks.ModBlocks;
import net.vit.jurassicreborn.common.blocks.entities.DNABlocks.DNASequencer.DNASequencerMenu;
import net.vit.jurassicreborn.common.blocks.entities.DNABlocks.DNASynthesizer.DNASynthesizerMenu;
import net.vit.jurassicreborn.common.blocks.entities.ModMenuTypes;
import net.vit.jurassicreborn.common.blocks.entities.cleaner.CleanerMenu;
import net.vit.jurassicreborn.common.blocks.entities.cultivator.CultivatorMenu;
import net.vit.jurassicreborn.common.blocks.entities.grinder.FossilGrinderMenu;
import net.vit.jurassicreborn.common.blocks.entities.incubator.IncubatorMenu;
import net.vit.jurassicreborn.common.blocks.entities.DNABlocks.DNACombinatorHybridizer.DNACombinatorHybridizerMenu;
import net.vit.jurassicreborn.common.blocks.entities.bugcrate.BugCrateMenu;
import net.vit.jurassicreborn.common.blocks.entities.skeletonassembly.SkeletonAssemblerBlockEntity;
import net.vit.jurassicreborn.common.blocks.entities.skeletonassembly.SkeletonAssemblerMenu;
import net.vit.jurassicreborn.common.blocks.entities.skeletonassembly.SkeletonAssemblyCategory;
import net.vit.jurassicreborn.common.blocks.entities.skeletonassembly.SkeletonInput;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;
import net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurHandler;
import net.vit.jurassicreborn.common.items.ModItems;
import net.vit.jurassicreborn.common.jei.cleaningstation.CleanableInput;
import net.vit.jurassicreborn.common.jei.cleaningstation.CleaningStationCategory;
import net.vit.jurassicreborn.common.jei.cleaningstation.CleaningStationRecipeExtension;
import net.vit.jurassicreborn.common.jei.cultivate.CultivateInput;
import net.vit.jurassicreborn.common.jei.cultivate.CultivatorRecipeCategory;
import net.vit.jurassicreborn.common.jei.cultivate.CultivatorRecipeExtension;
import net.vit.jurassicreborn.common.jei.dnaextractor.DNAExtractorCategory;
import net.vit.jurassicreborn.common.jei.dnaextractor.DNAExtractorRecipeExtension;
import net.vit.jurassicreborn.common.jei.dnasequencer.DNASequencerCategory;
import net.vit.jurassicreborn.common.jei.dnasequencer.DNASequencerRecipeExtension;
import net.vit.jurassicreborn.common.jei.dnasequencer.SequencerInput;
import net.vit.jurassicreborn.common.jei.dnasynthesizer.DNASynthesizerCategory;
import net.vit.jurassicreborn.common.jei.dnasynthesizer.DNASynthesizerRecipeExtension;
import net.vit.jurassicreborn.common.jei.dnasynthesizer.SynthesizerInput;
import net.vit.jurassicreborn.common.jei.embryonic.EmbryoInput;
import net.vit.jurassicreborn.common.jei.embryonic.EmbryonicRecipeCategory;
import net.vit.jurassicreborn.common.jei.embryonic.EmbryonicRecipeExtension;
import net.vit.jurassicreborn.common.jei.embryoniccalcification.CalcificationInput;
import net.vit.jurassicreborn.common.jei.embryoniccalcification.CalcificationRecipeCategory;
import net.vit.jurassicreborn.common.jei.embryoniccalcification.CalcificationRecipeExtension;
import net.vit.jurassicreborn.common.jei.fossilgrinder.FossilGrinderCategory;
import net.vit.jurassicreborn.common.jei.fossilgrinder.FossilGrinderRecipeExtension;
import net.vit.jurassicreborn.common.jei.fossilgrinder.GrinderInput;
import net.vit.jurassicreborn.common.jei.incubator.IncubatorInput;
import net.vit.jurassicreborn.common.jei.incubator.IncubatorRecipeCategory;
import net.vit.jurassicreborn.common.jei.incubator.IncubatorRecipeExtension;
import net.vit.jurassicreborn.common.jei.dnacombinator.DNACombinatorCategory;
import net.vit.jurassicreborn.common.jei.dnacombinator.DNACombinatorRecipeExtension;
import net.vit.jurassicreborn.common.jei.dnahybridizer.DNAHybridizerCategory;
import net.vit.jurassicreborn.common.jei.dnahybridizer.DNAHybridizerRecipeExtension;
import net.vit.jurassicreborn.common.jei.bugcrate.BugCrateCategory;
import net.vit.jurassicreborn.common.jei.bugcrate.BugCrateRecipeExtension;
import net.vit.jurassicreborn.common.jei.skeletonassembly.SkeletonAssemblyRecipeExtension;
import net.vit.jurassicreborn.common.plants.Plant;
import net.vit.jurassicreborn.common.plants.PlantHandler;
import net.vit.jurassicreborn.common.util.api.CleanableItem;
import net.vit.jurassicreborn.common.util.api.GrindableItem;
import net.vit.jurassicreborn.common.util.api.SequencableItem;
import net.vit.jurassicreborn.common.util.api.SynthesizableItem;
import net.vit.jurassicreborn.common.util.BreedableBug;

import java.util.ArrayList;
import java.util.List;

@JeiPlugin
public class JRJeiPlugin implements IModPlugin {
    private static final ResourceLocation UID = ResourceLocation.parse(JurassicReborn.MODID + ":" + "jei_plugin");

    @Override
    public ResourceLocation getPluginUid() {
        return UID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IGuiHelper gui = registration.getJeiHelpers().getGuiHelper();
        registration.addRecipeCategories(
                new SkeletonAssemblyCategory(gui),
                new CleaningStationCategory(gui),
                new FossilGrinderCategory(gui),
                new DNASequencerCategory(gui),
                new DNASynthesizerCategory(gui),
                new DNAExtractorCategory(gui),
                new EmbryonicRecipeCategory(gui),
                new CalcificationRecipeCategory(gui),
                new CultivatorRecipeCategory(gui),
                new IncubatorRecipeCategory(gui),
                new DNACombinatorCategory(gui),
                new DNAHybridizerCategory(gui),
                new BugCrateCategory(gui)
        );
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.SKELETON_ASSEMBLY.get()), SkeletonAssemblyCategory.TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.CLEANING_STATION.get()), CleaningStationCategory.TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.FOSSIL_GRINDER.get()), FossilGrinderCategory.TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.DNA_SEQUENCER.get()), DNASequencerCategory.TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.DNA_SYNTHESIZER.get()), DNASynthesizerCategory.TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.DNA_EXTRACTOR.get()), DNAExtractorCategory.TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.EMBRYONIC_MACHINE.get()), EmbryonicRecipeCategory.TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.EMBRYO_CALCIFICATION_MACHINE.get()), CalcificationRecipeCategory.TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.CULTIVATE_BOTTOM.get()), CultivatorRecipeCategory.TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.INCUBATOR.get()), IncubatorRecipeCategory.TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.DNA_COMBINER_HYBRIDIZER.get()), DNACombinatorCategory.TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.DNA_COMBINER_HYBRIDIZER.get()), DNAHybridizerCategory.TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.BUG_CRATE.get()), BugCrateCategory.TYPE);
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        List<SkeletonAssemblyRecipeExtension> recipes = new ArrayList<>();
        for (Dinosaur dino : Dinosaur.DINOS) {
            if (dino.getRecipe() == null) continue;
            if (ModItems.BONES.containsKey(dino))
                recipes.add(new SkeletonAssemblyRecipeExtension(new SkeletonInput(dino, false)));
            if (ModItems.FRESH_BONES.containsKey(dino))
                recipes.add(new SkeletonAssemblyRecipeExtension(new SkeletonInput(dino, true)));
        }
        registration.addRecipes(SkeletonAssemblyCategory.TYPE, recipes);

        List<CleaningStationRecipeExtension> cleaning = new ArrayList<>();
        BuiltInRegistries.ITEM.stream().forEach(item -> {
            ItemStack stack = new ItemStack(item);
            CleanableItem clean = CleanableItem.getCleanableItem(stack);
            if (clean != null && clean.isCleanable(stack)) {
                cleaning.add(new CleaningStationRecipeExtension(new CleanableInput(stack)));
            }
        });
        registration.addRecipes(CleaningStationCategory.TYPE, cleaning);

        List<FossilGrinderRecipeExtension> grinding = new ArrayList<>();
        BuiltInRegistries.ITEM.stream().forEach(item -> {
            ItemStack stack = new ItemStack(item);
            GrindableItem grind = GrindableItem.getGrindableItem(stack);
            if (grind != null && grind.isGrindable(stack)) {
                grinding.add(new FossilGrinderRecipeExtension(new GrinderInput(stack)));
            }
        });
        registration.addRecipes(FossilGrinderCategory.TYPE, grinding);
        List<DNASequencerRecipeExtension> sequencing = new ArrayList<>();
        BuiltInRegistries.ITEM.stream().forEach(item -> {
            ItemStack stack = new ItemStack(item);
            var seq = SequencableItem.getSequencableItem(stack);
            if (seq != null && seq.isSequencable(stack)) {
                sequencing.add(new DNASequencerRecipeExtension(new SequencerInput(stack)));
            }
        });
        registration.addRecipes(DNASequencerCategory.TYPE, sequencing);


        List<DNASynthesizerRecipeExtension> synthesize = new ArrayList<>();
        var discItem = ModItems.STORAGE_DISC.get();
        for (ItemStack stack : discItem.getJEIRecipeTypes()) {
            if (discItem.isSynthesizable(stack)) {
                synthesize.add(new DNASynthesizerRecipeExtension(new SynthesizerInput(stack)));
            }
        }
        registration.addRecipes(DNASynthesizerCategory.TYPE, synthesize);
        registration.addRecipes(DNAExtractorCategory.TYPE, DNAExtractorRecipeExtension.createRecipes());

        List<EmbryonicRecipeExtension> embryonic = new ArrayList<>();
        for (Dinosaur dino : Dinosaur.DINOS) {
            EmbryoInput in = new EmbryoInput.DinosaurInput(dino);
            if (in.isValid()) embryonic.add(new EmbryonicRecipeExtension(in));
        }
        for (Plant plant : PlantHandler.getPrehistoricPlantsAndTrees()) {
            EmbryoInput in = new EmbryoInput.PlantInput(plant);
            if (in.isValid()) embryonic.add(new EmbryonicRecipeExtension(in));
        }
        registration.addRecipes(EmbryonicRecipeCategory.TYPE, embryonic);

        List<CalcificationRecipeExtension> calc = new ArrayList<>();
        for (Dinosaur dino : Dinosaur.DINOS) {
            if (dino.getBirthType() == Dinosaur.BirthType.EGG_LAYING && (!dino.isMarineCreature()|| dino == DinosaurHandler.CALYMENE || dino == DinosaurHandler.BEELZEBUFO)
                    && ModItems.dinoEggs.containsKey(dino)) {
                calc.add(new CalcificationRecipeExtension(new CalcificationInput(dino)));
            }
        }
        registration.addRecipes(CalcificationRecipeCategory.TYPE, calc);
        List<CultivatorRecipeExtension> cultivate = new ArrayList<>();
        for (Dinosaur dino : Dinosaur.DINOS) {
            if (ModItems.SYRINGES.containsKey(dino) && (dino.isMarineCreature() || dino.isMammal())) {
                cultivate.add(new CultivatorRecipeExtension(new CultivateInput(dino)));
            }
        }
        registration.addRecipes(CultivatorRecipeCategory.TYPE, cultivate);

        List<IncubatorRecipeExtension> incubate = new ArrayList<>();
        for (Dinosaur dino : Dinosaur.DINOS) {
            if (ModItems.dinoEggs.containsKey(dino)) {
                incubate.add(new IncubatorRecipeExtension(new IncubatorInput(dino)));
            }
        }
        registration.addRecipes(IncubatorRecipeCategory.TYPE, incubate);

        List<DNACombinatorRecipeExtension> combinator = new ArrayList<>();
        for (ItemStack stack : ModItems.STORAGE_DISC.get().getJEIRecipeTypes()) {
            combinator.add(new DNACombinatorRecipeExtension(stack));
        }
        registration.addRecipes(DNACombinatorCategory.TYPE, combinator);

        List<DNAHybridizerRecipeExtension> hybrid = new ArrayList<>();
        for (Dinosaur dino : Dinosaur.DINOS) {
            if (dino instanceof net.vit.jurassicreborn.common.entities.EntityUtils.Hybrid) {
                hybrid.add(new DNAHybridizerRecipeExtension(dino));
            }
        }
        registration.addRecipes(DNAHybridizerCategory.TYPE, hybrid);

        List<BugCrateRecipeExtension> bugs = new ArrayList<>();
        BuiltInRegistries.ITEM.stream().forEach(item -> {
            ItemStack stack = new ItemStack(item);
            if (BreedableBug.isBug(stack)) {
                bugs.add(new BugCrateRecipeExtension(stack));
            }
        });
        registration.addRecipes(BugCrateCategory.TYPE, bugs);
    }
    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(
                SkeletonAssemblerMenu.class,
                ModMenuTypes.SKELETON_ASSEMBLER.get(),
                SkeletonAssemblyCategory.TYPE,
                /* recipeSlotStart */ 1,
                /* recipeSlotCount */ SkeletonAssemblerBlockEntity.GRID_W * SkeletonAssemblerBlockEntity.GRID_H,
                /* inventorySlotStart */ SkeletonAssemblerBlockEntity.RESULT_SLOT + 1,
                /* inventorySlotCount */ 36
        );

        registration.addRecipeTransferHandler(
                CleanerMenu.class,
                ModMenuTypes.CLEANER.get(),
                CleaningStationCategory.TYPE,
                /* recipeSlotStart */ 0,
                /* recipeSlotCount */ 2,
                /* inventorySlotStart */ 2,
                /* inventorySlotCount */ 36
        );

        registration.addRecipeTransferHandler(
                FossilGrinderMenu.class,
                ModMenuTypes.FOSSIL_GRINDER.get(),
                FossilGrinderCategory.TYPE,
                /* recipeSlotStart */ 0,
                /* recipeSlotCount */ 6,
                /* inventorySlotStart */ 12,
                /* inventorySlotCount */ 36
        );
        registration.addRecipeTransferHandler(
                DNASequencerMenu.class,
                ModMenuTypes.DNA_SEQUENCER.get(),
                DNASequencerCategory.TYPE,
                /* recipeSlotStart */ 0,
                /* recipeSlotCount */ 6,
                /* inventorySlotStart */ 9,
                /* inventorySlotCount */ 36
        );

        registration.addRecipeTransferHandler(
                DNASynthesizerMenu.class,
                ModMenuTypes.DNA_SYNTHESIZER.get(),
                DNASynthesizerCategory.TYPE,
                /* recipeSlotStart */ 0,
                /* recipeSlotCount */ 3,
                /* inventorySlotStart */ 7,
                /* inventorySlotCount */ 36
        );
        registration.addRecipeTransferHandler(
                CultivatorMenu.class,
                ModMenuTypes.CULTIVATOR.get(),
                CultivatorRecipeCategory.TYPE,
                /* recipeSlotStart */ 0,
                /* recipeSlotCount */ 4,
                /* inventorySlotStart */ 4,
                /* inventorySlotCount */ 36
        );
        registration.addRecipeTransferHandler(
                IncubatorMenu.class,
                ModMenuTypes.INCUBATOR.get(),
                IncubatorRecipeCategory.TYPE,
                /* recipeSlotStart */ 0,
                /* recipeSlotCount */ 6,
                /* inventorySlotStart */ 6,
                /* inventorySlotCount */ 36
        );

        registration.addRecipeTransferHandler(
                DNACombinatorHybridizerMenu.class,
                ModMenuTypes.COMBINATOR.get(),
                DNACombinatorCategory.TYPE,
                /* recipeSlotStart */ 8,
                /* recipeSlotCount */ 2,
                /* inventorySlotStart */ 12,
                /* inventorySlotCount */ 36
        );
        registration.addRecipeTransferHandler(
                DNACombinatorHybridizerMenu.class,
                ModMenuTypes.COMBINATOR.get(),
                DNAHybridizerCategory.TYPE,
                /* recipeSlotStart */ 0,
                /* recipeSlotCount */ 8,
                /* inventorySlotStart */ 12,
                /* inventorySlotCount */ 36
        );
        registration.addRecipeTransferHandler(
                BugCrateMenu.class,
                ModMenuTypes.BUG_CRATE.get(),
                BugCrateCategory.TYPE,
                /* recipeSlotStart */ 0,
                /* recipeSlotCount */ 6,
                /* inventorySlotStart */ 9,
                /* inventorySlotCount */ 36
        );
    }
}
