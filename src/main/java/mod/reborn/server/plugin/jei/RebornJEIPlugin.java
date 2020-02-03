package mod.reborn.server.plugin.jei;

import com.google.common.collect.Lists;
import mezz.jei.api.*;
import mezz.jei.api.ingredients.IIngredientBlacklist;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import mezz.jei.api.recipe.transfer.IRecipeTransferRegistry;
import mod.reborn.client.gui.*;
import mod.reborn.server.api.*;
import mod.reborn.server.block.BlockHandler;
import mod.reborn.server.block.tree.AncientDoorBlock;
import mod.reborn.server.container.*;
import mod.reborn.server.entity.EntityHandler;
import mod.reborn.server.item.ItemHandler;
import mod.reborn.server.plant.Plant;
import mod.reborn.server.plant.PlantHandler;
import net.minecraft.block.Block;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import mod.reborn.server.dinosaur.Dinosaur;
import mod.reborn.server.plugin.jei.category.calcification.CalcificationInput;
import mod.reborn.server.plugin.jei.category.calcification.CalcificationRecipeCategory;
import mod.reborn.server.plugin.jei.category.calcification.CalcificationRecipeWrapper;
import mod.reborn.server.plugin.jei.category.cleaningstation.CleanableInput;
import mod.reborn.server.plugin.jei.category.cleaningstation.CleaningStationRecipeCategory;
import mod.reborn.server.plugin.jei.category.cleaningstation.CleaningStationRecipeWrapper;
import mod.reborn.server.plugin.jei.category.cultivate.CultivateInput;
import mod.reborn.server.plugin.jei.category.cultivate.CultivatorRecipeCategory;
import mod.reborn.server.plugin.jei.category.cultivate.CultivatorRecipeWrapper;
import mod.reborn.server.plugin.jei.category.dnasequencer.DNASequencerRecipeCategory;
import mod.reborn.server.plugin.jei.category.dnasequencer.DNASequencerRecipeWrapper;
import mod.reborn.server.plugin.jei.category.dnasequencer.SequencerInput;
import mod.reborn.server.plugin.jei.category.dnasynthesizer.DNASequencerTransferHandler;
import mod.reborn.server.plugin.jei.category.dnasynthesizer.DNASynthesizerRecipeCategory;
import mod.reborn.server.plugin.jei.category.dnasynthesizer.DNASynthesizerRecipeWrapper;
import mod.reborn.server.plugin.jei.category.dnasynthesizer.SynthesizerInput;
import mod.reborn.server.plugin.jei.category.embroyonicmachine.EmbryoInput;
import mod.reborn.server.plugin.jei.category.embroyonicmachine.EmbryonicRecipeCategory;
import mod.reborn.server.plugin.jei.category.embroyonicmachine.EmbryonicRecipeWrapper;
import mod.reborn.server.plugin.jei.category.fossilgrinder.FossilGrinderRecipeCategory;
import mod.reborn.server.plugin.jei.category.fossilgrinder.FossilGrinderRecipeWrapper;
import mod.reborn.server.plugin.jei.category.fossilgrinder.GrinderInput;
import mod.reborn.server.plugin.jei.category.incubator.IncubatorInput;
import mod.reborn.server.plugin.jei.category.incubator.IncubatorRecipeCategory;
import mod.reborn.server.plugin.jei.category.incubator.IncubatorRecipeWrapper;
import mod.reborn.server.plugin.jei.category.skeletonassembly.SkeletonAssemblyRecipeCategory;
import mod.reborn.server.plugin.jei.category.skeletonassembly.SkeletonAssemblyRecipeWrapper;
import mod.reborn.server.plugin.jei.category.skeletonassembly.SkeletonInput;
import mod.reborn.server.plugin.jei.vanilla.TippedDartRecipeWrapper;

import java.awt.*;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@JEIPlugin
@SideOnly(Side.CLIENT) //TODO: add SequencableItem
public class RebornJEIPlugin implements IModPlugin {

    public static final String FOSSIL_GRINDER = "rebornmod.fossil_grinder";
    public static final String CLEANING_STATION = "rebornmod.cleaning_station";
    public static final String DNA_SYNTHASIZER = "rebornmod.dna_synthesizer";
    public static final String EMBRYOMIC_MACHINE = "rebornmod.embryonic_machine";
    public static final String EMBRYO_CALCIFICATION_MACHINE = "rebornmod.embryo_calcification_machine";
    public static final String SKELETON_ASSEMBLY = "rebornmod.skeleton_assembly";
    public static final String DNA_SEQUENCER = "rebornmod.dna_sequencer";
    public static final String CULTIVATEOR = "rebornmod.cultivator";
    public static final String INCUBATOR = "rebornmod.incubator";


    @Override
    public void register(IModRegistry registry) {
        IIngredientBlacklist blacklist = registry.getJeiHelpers().getIngredientBlacklist();

        Collection<AncientDoorBlock> doors = BlockHandler.ANCIENT_DOORS.values();
        for (Block door : doors) {
            blacklist.addIngredientToBlacklist(new ItemStack(door));
        }

        blacklist.addIngredientToBlacklist(new ItemStack(BlockHandler.CULTIVATOR_TOP, 1, OreDictionary.WILDCARD_VALUE));
        blacklist.addIngredientToBlacklist(new ItemStack(BlockHandler.DISPLAY_BLOCK));
        blacklist.addIngredientToBlacklist(new ItemStack(BlockHandler.KRILL_SWARM));
        blacklist.addIngredientToBlacklist(new ItemStack(BlockHandler.PLANKTON_SWARM));
//        blacklist.addIngredientToBlacklist(new ItemStack(BlockHandler.TOUR_RAIL_POWERED)); TODO
        blacklist.addIngredientToBlacklist(new ItemStack(BlockHandler.RHAMNUS_SALICIFOLIUS_PLANT));
        blacklist.addIngredientToBlacklist(new ItemStack(BlockHandler.AJUGINUCULA_SMITHII));
        blacklist.addIngredientToBlacklist(new ItemStack(BlockHandler.WILD_ONION));
        blacklist.addIngredientToBlacklist(new ItemStack(BlockHandler.WILD_POTATO_PLANT));
        blacklist.addIngredientToBlacklist(new ItemStack(BlockHandler.GRACILARIA));
        blacklist.addIngredientToBlacklist(new ItemStack(ItemHandler.HATCHED_EGG));

        //register recipe hander stuff
        registry.handleRecipes(GrinderInput.class, FossilGrinderRecipeWrapper::new, FOSSIL_GRINDER);
        registry.addRecipeCatalyst(new ItemStack(BlockHandler.FOSSIL_GRINDER), FOSSIL_GRINDER);

        registry.handleRecipes(CleanableInput.class, CleaningStationRecipeWrapper::new, CLEANING_STATION);
        registry.addRecipeCatalyst(new ItemStack(BlockHandler.CLEANING_STATION), CLEANING_STATION);

        registry.handleRecipes(SynthesizerInput.class, DNASynthesizerRecipeWrapper::new, DNA_SYNTHASIZER);
        registry.addRecipeCatalyst(new ItemStack(BlockHandler.DNA_SYNTHESIZER), DNA_SYNTHASIZER);

        registry.handleRecipes(EmbryoInput.class, EmbryonicRecipeWrapper::new, EMBRYOMIC_MACHINE);
        registry.addRecipeCatalyst(new ItemStack(BlockHandler.EMBRYONIC_MACHINE), EMBRYOMIC_MACHINE);

        registry.handleRecipes(CalcificationInput.class, CalcificationRecipeWrapper::new, EMBRYO_CALCIFICATION_MACHINE);
        registry.addRecipeCatalyst(new ItemStack(BlockHandler.EMBRYO_CALCIFICATION_MACHINE), EMBRYO_CALCIFICATION_MACHINE);

        registry.handleRecipes(SkeletonInput.class, SkeletonAssemblyRecipeWrapper::new, SKELETON_ASSEMBLY);
        registry.addRecipeCatalyst(new ItemStack(BlockHandler.SKELETON_ASSEMBLY), SKELETON_ASSEMBLY);

        registry.handleRecipes(SequencerInput.class, DNASequencerRecipeWrapper::new, DNA_SEQUENCER);
        registry.addRecipeCatalyst(new ItemStack(BlockHandler.DNA_SEQUENCER), DNA_SEQUENCER);

        registry.handleRecipes(CultivateInput.class, CultivatorRecipeWrapper::new, CULTIVATEOR);
        NonNullList<ItemStack> list = NonNullList.create();
        BlockHandler.CULTIVATOR_BOTTOM.getSubBlocks(CreativeTabs.SEARCH, list);
        list.forEach(item -> registry.addRecipeCatalyst(item, CULTIVATEOR));

        registry.handleRecipes(IncubatorInput.class, IncubatorRecipeWrapper::new, INCUBATOR);
        registry.addRecipeCatalyst(new ItemStack(BlockHandler.INCUBATOR), INCUBATOR);

        registry.addRecipeClickArea(FossilGrinderGui.class, 78, 33, 26, 19, FOSSIL_GRINDER);
        registry.addRecipeClickArea(CleaningStationGui.class, 78, 33, 26, 19, CLEANING_STATION);
        registry.addRecipeClickArea(DNASynthesizerGui.class, 78, 33, 26, 19, DNA_SYNTHASIZER);
        registry.addRecipeClickArea(EmbryonicMachineGui.class, 78, 33, 26, 19, EMBRYOMIC_MACHINE);
        registry.addRecipeClickArea(EmbryoCalcificationMachineGui.class, 66, 30, 26, 19, EMBRYO_CALCIFICATION_MACHINE);
        registry.addRecipeClickArea(SkeletonAssemblyGui.class, 106, 50, 26, 19, SKELETON_ASSEMBLY);
        registry.addRecipeClickArea(DNASequencerGui.class, 86, 18, 23, 52, DNA_SEQUENCER);
        addHollowClickHandler(registry, CultivateGui.class, new Rectangle(98, 20, 64, 64), new Rectangle(116, 38, 27, 27), CULTIVATEOR);

        IRecipeTransferRegistry recipeTransferRegistry = registry.getRecipeTransferRegistry();

        recipeTransferRegistry.addRecipeTransferHandler(FossilGrinderContainer.class, FOSSIL_GRINDER, 0, 6, 12, 36);
        recipeTransferRegistry.addRecipeTransferHandler(CleaningStationContainer.class, CLEANING_STATION, 0, 2, 8, 36);
        recipeTransferRegistry.addRecipeTransferHandler(DNASynthesizerContainer.class, DNA_SYNTHASIZER, 0, 3, 7, 36);
        recipeTransferRegistry.addRecipeTransferHandler(EmbryonicMachineContainer.class, EMBRYOMIC_MACHINE, 0, 3, 7, 36);
        recipeTransferRegistry.addRecipeTransferHandler(EmbryoCalcificationMachineContainer.class, EMBRYO_CALCIFICATION_MACHINE, 0, 2, 3, 36);
        recipeTransferRegistry.addRecipeTransferHandler(SkeletonAssemblyContainer.class, SKELETON_ASSEMBLY, 1, 25, 26, 36);
        recipeTransferRegistry.addRecipeTransferHandler(new DNASequencerTransferHandler(registry.getJeiHelpers().recipeTransferHandlerHelper()), DNA_SEQUENCER);
        recipeTransferRegistry.addRecipeTransferHandler(CultivateContainer.class, CULTIVATEOR, 0, 1, 4, 36);

        //register recipes
        registry.addRecipes(getAllItems(GrindableItem::getGrindableItem, GrinderInput::new), FOSSIL_GRINDER);
        registry.addRecipes(getAllItems(CleanableItem::getCleanableItem, CleanableInput::new), CLEANING_STATION);
        registry.addRecipes(getAllItems(SynthesizableItem::getSynthesizableItem, SynthesizerInput::new), DNA_SYNTHASIZER);
        registry.addRecipes(getAllItems(SequencableItem::getSequencableItem, SequencerInput::new), DNA_SEQUENCER);

        registry.addRecipes(getDinos(CultivateInput::new, dino -> dino.getBirthType() == Dinosaur.BirthType.LIVE_BIRTH), CULTIVATEOR);
        registry.addRecipes(getDinos(IncubatorInput::new, dino -> dino.getBirthType() == Dinosaur.BirthType.EGG_LAYING), INCUBATOR);
        registry.addRecipes(getDinos(CalcificationInput::new), EMBRYO_CALCIFICATION_MACHINE);
        registry.addRecipes(getDinos(EmbryoInput.DinosaurInput::new), EMBRYOMIC_MACHINE);
        registry.addRecipes(getPlants(EmbryoInput.PlantInput::new), EMBRYOMIC_MACHINE);

        for (Dinosaur dinosaur : EntityHandler.getRegisteredDinosaurs()) {
            registry.addRecipes(Lists.newArrayList(new SkeletonInput(dinosaur, false), new SkeletonInput(dinosaur, true)), SKELETON_ASSEMBLY);
        }

        registry.addRecipes(ForgeRegistries.POTION_TYPES.getValuesCollection().stream().map(TippedDartRecipeWrapper::new).collect(Collectors.toList()), VanillaRecipeCategoryUid.CRAFTING);
    }

    private <T> List<T> getDinos(Function<Dinosaur,T> func) {
        return getDinos(func, dino -> true);
    }

    private <T> List<T> getDinos(Function<Dinosaur,T> func, Predicate<Dinosaur> filter) {
        return EntityHandler.getRegisteredDinosaurs().stream().filter(filter).map(func).collect(Collectors.toList());
    }

    private <T> List<T> getPlants(Function<Plant,T> func) {
        return PlantHandler.getPrehistoricPlantsAndTrees().stream().map(func).collect(Collectors.toList());
    }

    private <T> List<T> getAllItems(Function<ItemStack, RebornIngredientItem> ingredientFunction, Function<ItemStack, T> tFunction) {
        List<T> list = Lists.newArrayList();
        ForgeRegistries.ITEMS.getValuesCollection()
                .stream()
                .map(ItemStack::new)
                .map(ingredientFunction)
                .filter(Objects::nonNull)
                .map(RebornIngredientItem::getJEIRecipeTypes)
                .forEach(l -> list.addAll(l.stream().map(tFunction).collect(Collectors.toList())));
        return list;
    }

    private void addHollowClickHandler(IModRegistry registry, Class<? extends GuiContainer> guiContainerClass, Rectangle size, Rectangle ignore, String... recipeCategoryUids) {
        registry.addRecipeClickArea(guiContainerClass, size.x, size.y, ignore.x - size.x, size.height, recipeCategoryUids);
        registry.addRecipeClickArea(guiContainerClass, ignore.x + ignore.width, size.y, (size.x + size.width) - (ignore.x + ignore.width), size.height, recipeCategoryUids);
        registry.addRecipeClickArea(guiContainerClass, ignore.x, size.y, ignore.width, ignore.y - size.y, recipeCategoryUids);
        registry.addRecipeClickArea(guiContainerClass, ignore.x, ignore.y + ignore.height, ignore.width, (size.y + size.height) - (ignore.y + ignore.height), recipeCategoryUids);

    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();

        registry.addRecipeCategories(
                new FossilGrinderRecipeCategory(guiHelper),
                new CleaningStationRecipeCategory(guiHelper),
                new DNASynthesizerRecipeCategory(guiHelper),
                new EmbryonicRecipeCategory(guiHelper),
                new CalcificationRecipeCategory(guiHelper),
                new SkeletonAssemblyRecipeCategory(guiHelper),
                new DNASequencerRecipeCategory(guiHelper),
                new CultivatorRecipeCategory(guiHelper),
                new IncubatorRecipeCategory(guiHelper)
        );
    }
}