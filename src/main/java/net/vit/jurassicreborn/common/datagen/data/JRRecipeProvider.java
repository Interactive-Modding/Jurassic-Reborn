package net.vit.jurassicreborn.common.datagen.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.BlockFamily;

import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.flag.FeatureFlags;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.common.blocks.ModBlocks;
import net.vit.jurassicreborn.common.blocks.wood.WoodBlocks;
import net.vit.jurassicreborn.common.datagen.CleaningRecipeBuilder;
import net.vit.jurassicreborn.common.datagen.ModBlockFamilies;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;
import net.vit.jurassicreborn.common.entities.item.AttractionSignEntity;
import net.vit.jurassicreborn.common.blocks.wood.WoodBlocks;
import net.vit.jurassicreborn.common.datagen.ModBlockFamilies;
import net.vit.jurassicreborn.common.items.ModItems;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class JRRecipeProvider extends RecipeProvider {

    public JRRecipeProvider(
            PackOutput output,
            CompletableFuture<HolderLookup.Provider> lookupProvider
    ) {
        super(output, lookupProvider);
    }


    private static final List<BoatRecipeData> BOAT_RECIPES = List.of(
            new BoatRecipeData("araucaria", WoodBlocks.ARAUCARIA_PLANKS.get()),
            new BoatRecipeData("calamites", WoodBlocks.CALAMITES_PLANKS.get()),
            new BoatRecipeData("ginkgo", WoodBlocks.GINKGO_PLANKS.get()),
            new BoatRecipeData("magnolia", WoodBlocks.MAGNOLIA_PLANKS.get()),
            new BoatRecipeData("phoenix", WoodBlocks.PHOENIX_PLANKS.get()),
            new BoatRecipeData("psaronius", WoodBlocks.PSARONIUS_PLANKS.get())
    );


    @Override
    protected void buildRecipes(RecipeOutput output) {
        ModBlockFamilies.getAllFamilies()
                .filter(ModBlockFamilies.ModBlockFamily::shouldGenerateRecipe)
                .forEach(family -> generateRecipes(output, family.getFamily(), FeatureFlags.REGISTRY.allFlags()));

        shapeless(WoodBlocks.ARAUCARIA_PLANKS.get(), 4)
                .requires(Ingredient.of(WoodBlocks.ARAUCARIA_LOG.get(), WoodBlocks.STRIPPED_ARAUCARIA_LOG.get(), WoodBlocks.ARAUCARIA_WOOD.get(), WoodBlocks.STRIPPED_ARAUCARIA_WOOD.get()))
                .unlockedBy(getHasName(WoodBlocks.ARAUCARIA_LOG.get()), has(WoodBlocks.ARAUCARIA_LOG.get()))
                .save(output);

        shapeless(WoodBlocks.CALAMITES_PLANKS.get(), 4)
                .requires(Ingredient.of(WoodBlocks.CALAMITES_LOG.get(), WoodBlocks.STRIPPED_CALAMITES_LOG.get(), WoodBlocks.CALAMITES_WOOD.get(), WoodBlocks.STRIPPED_CALAMITES_WOOD.get()))
                .unlockedBy(getHasName(WoodBlocks.CALAMITES_LOG.get()), has(WoodBlocks.CALAMITES_LOG.get()))
                .save(output);

        shapeless(WoodBlocks.GINKGO_PLANKS.get(), 4)
                .requires(Ingredient.of(WoodBlocks.GINKGO_LOG.get(), WoodBlocks.STRIPPED_GINKGO_LOG.get(), WoodBlocks.GINKGO_WOOD.get(), WoodBlocks.STRIPPED_GINKGO_WOOD.get()))
                .unlockedBy(getHasName(WoodBlocks.GINKGO_LOG.get()), has(WoodBlocks.GINKGO_LOG.get()))
                .save(output);

        shapeless(WoodBlocks.PHOENIX_PLANKS.get(), 4)
                .requires(Ingredient.of(WoodBlocks.PHOENIX_LOG.get(), WoodBlocks.STRIPPED_PHOENIX_LOG.get(), WoodBlocks.PHOENIX_WOOD.get(), WoodBlocks.STRIPPED_PHOENIX_WOOD.get()))
                .unlockedBy(getHasName(WoodBlocks.PHOENIX_LOG.get()), has(WoodBlocks.PHOENIX_LOG.get()))
                .save(output);

        shapeless(WoodBlocks.PSARONIUS_PLANKS.get(), 4)
                .requires(Ingredient.of(WoodBlocks.PSARONIUS_LOG.get(), WoodBlocks.STRIPPED_PSARONIUS_LOG.get(), WoodBlocks.PSARONIUS_WOOD.get(), WoodBlocks.STRIPPED_PSARONIUS_WOOD.get()))
                .unlockedBy(getHasName(WoodBlocks.PSARONIUS_LOG.get()), has(WoodBlocks.PSARONIUS_LOG.get()))
                .save(output);

        shapeless(WoodBlocks.MAGNOLIA_PLANKS.get(), 4)
                .requires(Ingredient.of(WoodBlocks.MAGNOLIA_LOG.get(), WoodBlocks.STRIPPED_MAGNOLIA_LOG.get(), WoodBlocks.MAGNOLIA_WOOD.get(), WoodBlocks.STRIPPED_MAGNOLIA_WOOD.get()))
                .unlockedBy(getHasName(WoodBlocks.MAGNOLIA_LOG.get()), has(WoodBlocks.MAGNOLIA_LOG.get()))
                .save(output);

        hangingSignRecipe(output, WoodBlocks.ARAUCARIA_HANGING_SIGN.get(), WoodBlocks.STRIPPED_ARAUCARIA_LOG.get(), WoodBlocks.STRIPPED_ARAUCARIA_WOOD.get());
        hangingSignRecipe(output, WoodBlocks.CALAMITES_HANGING_SIGN.get(), WoodBlocks.STRIPPED_CALAMITES_LOG.get(), WoodBlocks.STRIPPED_CALAMITES_WOOD.get());
        hangingSignRecipe(output, WoodBlocks.GINKGO_HANGING_SIGN.get(), WoodBlocks.STRIPPED_GINKGO_LOG.get(), WoodBlocks.STRIPPED_GINKGO_WOOD.get());
        hangingSignRecipe(output, WoodBlocks.MAGNOLIA_HANGING_SIGN.get(), WoodBlocks.STRIPPED_MAGNOLIA_LOG.get(), WoodBlocks.STRIPPED_MAGNOLIA_WOOD.get());
        hangingSignRecipe(output, WoodBlocks.PHOENIX_HANGING_SIGN.get(), WoodBlocks.STRIPPED_PHOENIX_LOG.get(), WoodBlocks.STRIPPED_PHOENIX_WOOD.get());
        hangingSignRecipe(output, WoodBlocks.PSARONIUS_HANGING_SIGN.get(), WoodBlocks.STRIPPED_PSARONIUS_LOG.get(), WoodBlocks.STRIPPED_PSARONIUS_WOOD.get());

        BOAT_RECIPES.forEach(data -> {
            ItemLike planks = data.planksItem();
            ItemLike boat = item(data.boatId());
            ItemLike chestBoat = item(data.chestBoatId());

            shaped(boat)
                    .group("boat")
                    .pattern("P P")
                    .pattern("PPP")
                    .define('P', planks)
                    .unlockedBy("has_planks", has(planks))
                    .save(output);

            shapeless(chestBoat)
                    .group("chest_boat")
                    .requires(boat)
                    .requires(Items.CHEST)
                    .unlockedBy("has_boat", has(boat))
                    .unlockedBy("has_chest", has(Items.CHEST))
                    .save(output);
        });

        baleRecipe(output, ModBlocks.SMALL_CYCAD.get(), ModBlocks.PALEO_BALE_CYCAD.get(), "paleo_bale_cycad", 1);
        baleRecipe(output, ModBlocks.CYCADEOIDEA.get(), ModBlocks.PALEO_BALE_CYCADEOIDEA.get(), "paleo_bale_cycadeoidea", 1);

        baleRecipe(output, WoodBlocks.ARAUCARIA_LEAVES.get(), ModBlocks.PALEO_BALE_LEAVES.get(), "paleo_bale_leaves", 1);
        baleRecipe(output, WoodBlocks.PSARONIUS_LEAVES.get(), ModBlocks.PALEO_BALE_LEAVES.get(), "paleo_bale_leaves", 1);
        baleRecipe(output, WoodBlocks.MAGNOLIA_LEAVES.get(), ModBlocks.PALEO_BALE_LEAVES.get(), "paleo_bale_leaves", 1);
        baleRecipe(output, WoodBlocks.PHOENIX_LEAVES.get(), ModBlocks.PALEO_BALE_LEAVES.get(), "paleo_bale_leaves", 1);
        baleRecipe(output, WoodBlocks.CALAMITES_LEAVES.get(), ModBlocks.PALEO_BALE_LEAVES.get(), "paleo_bale_leaves", 1);
        baleRecipe(output, WoodBlocks.GINKGO_LEAVES.get(), ModBlocks.PALEO_BALE_LEAVES.get(), "paleo_bale_leaves", 1);

        baleRecipe(output, ModBlocks.SMALL_CHAIN_FERN.get(), ModBlocks.PALEO_BALE_FERN.get(), "paleo_bale_fern", 1);
        baleRecipe(output, ModBlocks.SMALL_ROYAL_FERN.get(), ModBlocks.PALEO_BALE_FERN.get(), "paleo_bale_fern", 1);
        baleRecipe(output, ModBlocks.RAPHAELIA.get(), ModBlocks.PALEO_BALE_FERN.get(), "paleo_bale_fern", 1);
        baleRecipe(output, ModBlocks.BRISTLE_FERN.get(), ModBlocks.PALEO_BALE_FERN.get(), "paleo_bale_fern", 1);
        baleRecipe(output, ModBlocks.CINNAMON_FERN.get(), ModBlocks.PALEO_BALE_FERN.get(), "paleo_bale_fern", 1);
        baleRecipe(output, ModBlocks.TEMPSKYA.get(), ModBlocks.PALEO_BALE_FERN.get(), "paleo_bale_fern", 2);

        baleRecipe(output, ModBlocks.AJUGINUCULA_SMITHII.get(), ModBlocks.PALEO_BALE_OTHER.get(), "paleo_bale_other", 1);
        baleRecipe(output, ModBlocks.CRY_PANSY.get(), ModBlocks.PALEO_BALE_OTHER.get(), "paleo_bale_other", 1);
        baleRecipe(output, ModBlocks.RHACOPHYTON.get(), ModBlocks.PALEO_BALE_OTHER.get(), "paleo_bale_other", 2);
        baleRecipe(output, ModBlocks.LARGESTIPULE_LEATHER_ROOT.get(), ModBlocks.PALEO_BALE_OTHER.get(), "paleo_bale_other", 1);
        baleRecipe(output, ModBlocks.WILD_ONION.get(), ModBlocks.PALEO_BALE_OTHER.get(), "paleo_bale_other", 1);
        baleRecipe(output, ModBlocks.DICROIDIUM_ZUBERI.get(), ModBlocks.PALEO_BALE_OTHER.get(), "paleo_bale_other", 2);
        baleRecipe(output, ModBlocks.ZAMITES.get(), ModBlocks.PALEO_BALE_OTHER.get(), "paleo_bale_other", 2);
        baleRecipe(output, ModBlocks.UMALTOLEPIS.get(), ModBlocks.PALEO_BALE_OTHER.get(), "paleo_bale_other", 2);
        baleRecipe(output, ModBlocks.LIRIODENDRITES.get(), ModBlocks.PALEO_BALE_OTHER.get(), "paleo_bale_other", 2);
        baleRecipe(output, ModBlocks.DICKSONIA.get(), ModBlocks.PALEO_BALE_OTHER.get(), "paleo_bale_other", 2);
        baleRecipe(output, ModBlocks.HELICONIA.get(), ModBlocks.PALEO_BALE_OTHER.get(), "paleo_bale_other", 2);
        baleRecipe(output, ModBlocks.GRAMINIDITES_BAMBUSOIDES.get(), ModBlocks.PALEO_BALE_OTHER.get(), "paleo_bale_other", 2);
        baleRecipe(output, ModBlocks.RHAMNUS_SALICIFOLIUS.get(), ModBlocks.PALEO_BALE_OTHER.get(), "paleo_bale_other", 1);
        baleRecipe(output, ModBlocks.WOOLLY_STALKED_BEGONIA.get(), ModBlocks.PALEO_BALE_OTHER.get(), "paleo_bale_other", 1);

        shaped(ModItems.IRON_BLADES.get())
                .pattern("I I")
                .pattern(" S ")
                .pattern("I I")
                .define('S', Tags.Items.RODS_WOODEN)
                .define('I',Tags.Items.INGOTS_IRON)
                .unlockedBy(getHasName(Items.IRON_INGOT),has(Tags.Items.INGOTS_IRON))
                .save(output);

        shaped(ModItems.IRON_ROD.get(),4)
                .pattern("ISI")
                .pattern("ISI")
                .pattern("ISI")
                .define('S', Tags.Items.RODS_WOODEN)
                .define('I',Tags.Items.INGOTS_IRON)
                .unlockedBy(getHasName(Items.IRON_INGOT),has(Tags.Items.INGOTS_IRON))
                .save(output);
        shaped(ModBlocks.PEAT.get())
                .pattern("ABA")
                .pattern("ACA")
                .define('A', Items.DIRT)
                .define('B',Items.WATER_BUCKET)
                .define('C',Items.DEAD_BUSH)
                .unlockedBy(getHasName(Items.DIRT),has(Items.DIRT))
                .save(output);
        shaped(ModItems.KEYBOARD.get())
                .pattern("AAA")
                .pattern("AAA")
                .pattern("BCD")
                .define('A', Items.STONE_BUTTON)
                .define('B',Items.STONE_PRESSURE_PLATE)
                .define('C',Items.STONE_SLAB)
                .define('D',ModItems.BASIC_CIRCUIT.get())
                .unlockedBy(getHasName(ModItems.BASIC_CIRCUIT.get()),has(ModItems.BASIC_CIRCUIT.get()))
                .save(output);
        shaped(ModBlocks.FEEDER.get())
                .group(JurassicReborn.MODID + ":feeder")
                .pattern("ABA")
                .pattern("CDC")
                .pattern("EEE")
                .define('A', Items.IRON_TRAPDOOR)
                .define('B', Items.DISPENSER)
                .define('C', Items.IRON_INGOT)
                .define('D', Items.CHEST)
                .define('E', Blocks.COBBLESTONE)
                .unlockedBy(getHasName(Items.CHEST), has(Items.CHEST))
                .save(output);
        shaped(ModItems.BLUEPRINT.get())
                .group(JurassicReborn.MODID + ":blueprint")
                .pattern("ABA")
                .pattern("BCB")
                .pattern("ABA")
                .define('A', Items.IRON_INGOT)
                .define('B', Items.BLUE_WOOL)
                .define('C', Items.PAINTING)
                .unlockedBy(getHasName(Items.PAINTING), has(Items.PAINTING))
                .save(output);
        shaped(ModBlocks.GYPSUM_COBBLESTONE.get())
                .pattern("AA")
                .pattern("AA")
                .define('A', ModItems.GYPSUM_POWDER.get())
                .unlockedBy(getHasName(ModItems.GYPSUM_POWDER.get()), has(ModItems.GYPSUM_POWDER.get()))
                .save(output);

        shaped(ModBlocks.GYPSUM_COBBLESTONE_PATHWAY.get(),4)
                .pattern("AA")
                .pattern("AA")
                .define('A', ModBlocks.GYPSUM_COBBLESTONE.get())
                .unlockedBy(getHasName(ModBlocks.GYPSUM_COBBLESTONE.get()), has(ModBlocks.GYPSUM_COBBLESTONE.get()))
                .save(output);

        shaped(ModBlocks.GYPSUM_PATHWAY.get(),5)
                .pattern("A A")
                .pattern("AAA")
                .define('A', ModBlocks.GYPSUM_STONE.get())
                .unlockedBy(getHasName(ModBlocks.GYPSUM_STONE.get()), has(ModBlocks.GYPSUM_STONE.get()))
                .save(output);
        shaped(ModItems.CAGE.get())
                .group(JurassicReborn.MODID + ":cage")
                .pattern("BAB")
                .pattern("ACA")
                .pattern("BAB")
                .define('A', ModItems.IRON_ROD.get())
                .define('B', Items.DIAMOND)
                .define('C', Items.LEAD)
                .unlockedBy(getHasName(ModItems.IRON_ROD.get()), has(ModItems.IRON_ROD.get()))
                .save(output);

        shaped(ModItems.AQUATIC_CAGE.get())
                .group(JurassicReborn.MODID + ":aquatic_cage")
                .pattern("BAB")
                .pattern("ACA")
                .pattern("BAB")
                .define('A', Items.STRING)
                .define('B', Items.DIAMOND)
                .define('C', Items.LEAD)
                .unlockedBy(getHasName(Items.LEAD), has(Items.LEAD))
                .save(output);
        shaped(ModBlocks.GYPSUM_MIXED_PATH.get(),4)
                .pattern("AA")
                .pattern("AA")
                .define('A', ModBlocks.GYPSUM_PATHWAY.get())
                .unlockedBy(getHasName(ModBlocks.GYPSUM_PATHWAY.get()), has(ModBlocks.GYPSUM_PATHWAY.get()))
                .save(output);

        shaped(ModBlocks.GYPSUM_TILES.get(),4)
                .pattern("AA")
                .pattern("AA")
                .define('A', ModBlocks.GYPSUM_MIXED_PATH.get())
                .unlockedBy(getHasName(ModBlocks.GYPSUM_MIXED_PATH.get()), has(ModBlocks.GYPSUM_MIXED_PATH.get()))
                .save(output);

        shaped(ModBlocks.GYPSUM_STONE_PANEL.get(),9)
                .pattern("AAA")
                .pattern("AAA")
                .pattern("AAA")
                .define('A', ModBlocks.GYPSUM_STONE.get())
                .unlockedBy(getHasName(ModBlocks.GYPSUM_STONE.get()), has(ModBlocks.GYPSUM_STONE.get()))
                .save(output);

        shaped(ModBlocks.REFINED_GYPSUM_PANEL.get(),9)
                .pattern("AAA")
                .pattern("AAA")
                .pattern("AAA")
                .define('A', ModBlocks.GYPSUM_PATHWAY.get())
                .unlockedBy(getHasName(ModBlocks.GYPSUM_PATHWAY.get()), has(ModBlocks.GYPSUM_PATHWAY.get()))
                .save(output);

        shaped(ModItems.GYROSPHERE.get())
                .group(JurassicReborn.MODID + ":gyrosphere")
                .pattern("AAA")
                .pattern("ADA")
                .pattern("BCB")
                .define('A', Items.GLASS_PANE)
                .define('B', Blocks.IRON_BLOCK)
                .define('C', ModItems.ENGINE_SYSTEM.get())
                .define('D', ModItems.GYROSPHERE_INTERIOR.get())
                .unlockedBy(getHasName(ModItems.GYROSPHERE_INTERIOR.get()), has(ModItems.GYROSPHERE_INTERIOR.get()))
                .save(output);
        shapeless(Items.BROWN_DYE, 2)
                .group("minecraft:dye")
                .requires(ModItems.RHAMNUS_BERRIES.get(), 4)
                .unlockedBy(getHasName(ModItems.RHAMNUS_BERRIES.get()), has(ModItems.RHAMNUS_BERRIES.get()))
                .save(output);
        //Reinforced brick block recipes
        shaped(ModBlocks.REINFORCED_BRICKS.get(),8)
                .pattern("SSS")
                .pattern("SIS")
                .pattern("SSS")
                .define('S', Blocks.STONE_BRICKS)
                .define('I',Tags.Items.INGOTS_IRON)
                .unlockedBy(getHasName(Items.IRON_INGOT),has(Tags.Items.INGOTS_IRON))
                .save(output);

        shaped(ModBlocks.REINFORCED_STONE.get(),8)
                .pattern("SSS")
                .pattern("SIS")
                .pattern("SSS")
                .define('S', Blocks.STONE)
                .define('I',Tags.Items.INGOTS_IRON)
                .unlockedBy(getHasName(Items.IRON_INGOT),has(Tags.Items.INGOTS_IRON))
                .save(output);
        shaped(ModBlocks.AMBER_BLOCK.get(),1)
                .pattern("SSS")
                .pattern("SSS")
                .pattern("SSS")
                .define('S', ModItems.AMBER.get())
                .unlockedBy(getHasName(ModItems.AMBER.get()),has(ModItems.AMBER.get()))
                .save(output);

        shaped(ModBlocks.REINFORCED_BRICKS.get(),4)
                .pattern("SS")
                .pattern("SS")
                .define('S', ModBlocks.REINFORCED_STONE.get())
                .unlockedBy(getHasName(ModBlocks.REINFORCED_STONE.get()),has(ModBlocks.REINFORCED_STONE.get()))
                .save(output, JurassicReborn.resource("reinforced_bricks_alt"));
//
//       shaped(ModBlocks.REINFORCED_BRICK_STAIRS.get(),4)
//                .pattern("A  ")
//                .pattern("AA ")
//                .pattern("AAA")
//                .define('A', ModBlocks.REINFORCED_BRICKS.get())
//                .unlockedBy(getHasName(ModBlocks.REINFORCED_BRICKS.get()), has(ModBlocks.REINFORCED_BRICKS.get()))
//                .save(output);
//
//       shaped(ModBlocks.REINFORCED_BRICK_SLAB.get(),6)
//                .pattern("AAA")
//                .define('A', ModBlocks.REINFORCED_BRICKS.get())
//                .unlockedBy(getHasName(ModBlocks.REINFORCED_BRICKS.get()), has(ModBlocks.REINFORCED_BRICKS.get()))
//                .save(output);
//
//       shaped(ModBlocks.REINFORCED_BRICK_WALL.get(),6)
//                .pattern("AAA")
//                .pattern("AAA")
//                .define('A', ModBlocks.REINFORCED_BRICKS.get())
//                .unlockedBy(getHasName(ModBlocks.REINFORCED_BRICKS.get()), has(ModBlocks.REINFORCED_BRICKS.get()))
//                .save(output);
//
//       shaped(ModBlocks.REINFORCED_BRICK_PRESSURE_PLATE.get())
//                .pattern("AA")
//                .define('A', ModBlocks.REINFORCED_BRICKS.get())
//                .unlockedBy(getHasName(ModBlocks.REINFORCED_BRICKS.get()), has(ModBlocks.REINFORCED_BRICKS.get()))
//                .save(output);
//
//        shapeless(ModBlocks.REINFORCED_BRICK_BUTTON.get())
//                .requires(ModBlocks.REINFORCED_BRICKS.get())
//                .unlockedBy(getHasName(ModBlocks.REINFORCED_BRICKS.get()), has(ModBlocks.REINFORCED_BRICKS.get()))
//                .save(output);
        shaped(ModItems.HELICOPTER.get())
                .pattern("ABC")
                .pattern("DEF")
                .pattern("ADA")
                .define('A', ModItems.CAR_TIRE.get())
                .define('B', ModItems.IRON_BLADES.get())
                .define('C', Items.GREEN_DYE)
                .define('D', Blocks.IRON_BLOCK)
                .define('E', ModItems.ENGINE_SYSTEM.get())
                .define('F', ModItems.CAR_WINDSCREEN.get())
                .unlockedBy(getHasName(ModItems.ENGINE_SYSTEM.get()), has(ModItems.ENGINE_SYSTEM.get()))
                .save(output);

        shaped(ModItems.JEEP_WRANGLER.get())
                .pattern("ABA")
                .pattern("CDB")
                .pattern("ABA")
                .define('A', Items.LIGHT_GRAY_DYE)
                .define('B', Items.RED_DYE)
                .define('C', ModItems.CAR_TIRE.get())
                .define('D', ModItems.UNFINISHED_CAR.get())
                .unlockedBy(getHasName(ModItems.UNFINISHED_CAR.get()), has(ModItems.UNFINISHED_CAR.get()))
                .save(output);
        shaped(ModBlocks.REINFORCED_STONE_PATHWAY.get(),6)
                .pattern("   ")
                .pattern("AAA")
                .pattern("AAA")
                .define('A', ModBlocks.REINFORCED_STONE.get())
                .unlockedBy(getHasName(ModBlocks.REINFORCED_STONE.get()), has(ModBlocks.REINFORCED_STONE.get()))
                .save(output);

        shaped(ModBlocks.REINFORCED_STONE_TILES.get(),4)
                .pattern("   ")
                .pattern(" AA")
                .pattern(" AA")
                .define('A', ModBlocks.REINFORCED_STONE_PATHWAY.get())
                .unlockedBy(getHasName(ModBlocks.REINFORCED_STONE_PATHWAY.get()), has(ModBlocks.REINFORCED_STONE_PATHWAY.get()))
                .save(output);

        shaped(ModBlocks.REINFORCED_STONE_PANEL.get(),9)
                .pattern("AAA")
                .pattern("AAA")
                .pattern("AAA")
                .define('A', ModBlocks.REINFORCED_STONE.get())
                .unlockedBy(getHasName(ModBlocks.REINFORCED_STONE.get()), has(ModBlocks.REINFORCED_STONE.get()))
                .save(output);
        shaped(ModBlocks.PEAT_MOSS.get(),1)
                .pattern("BBB")
                .pattern("BAB")
                .pattern("BBB")
                .define('A', ModBlocks.PEAT.get())
                .define('B', Blocks.MOSS_CARPET)
                .unlockedBy(getHasName(ModBlocks.PEAT.get()), has(ModBlocks.PEAT.get()))
                .save(output);

        shaped(ModBlocks.REINFORCED_DOOR.get(),3)
                .group(JurassicReborn.MODID + ":reinforced_door")
                .pattern("AA")
                .pattern("AA")
                .pattern("AA")
                .define('A', ModBlocks.REINFORCED_STONE.get())
                .unlockedBy(getHasName(ModBlocks.REINFORCED_STONE.get()), has(ModBlocks.REINFORCED_STONE.get()))
                .save(output);

        shaped(ModBlocks.SECURITY_DOOR.get(),3)
                .group(JurassicReborn.MODID + ":security_door")
                .pattern("AA")
                .pattern("AB")
                .pattern("AA")
                .define('A', ModBlocks.REINFORCED_STONE.get())
                .define('B', Items.GLASS_PANE)
                .unlockedBy(getHasName(ModBlocks.REINFORCED_STONE.get()), has(ModBlocks.REINFORCED_STONE.get()))
                .save(output);
        for (DyeColor color : DyeColor.values()) {
            ItemLike pane = switch (color) {
                case WHITE -> Blocks.WHITE_STAINED_GLASS_PANE;
                case ORANGE -> Blocks.ORANGE_STAINED_GLASS_PANE;
                case MAGENTA -> Blocks.MAGENTA_STAINED_GLASS_PANE;
                case LIGHT_BLUE -> Blocks.LIGHT_BLUE_STAINED_GLASS_PANE;
                case YELLOW -> Blocks.YELLOW_STAINED_GLASS_PANE;
                case LIME -> Blocks.LIME_STAINED_GLASS_PANE;
                case PINK -> Blocks.PINK_STAINED_GLASS_PANE;
                case GRAY -> Blocks.GRAY_STAINED_GLASS_PANE;
                case LIGHT_GRAY -> Blocks.LIGHT_GRAY_STAINED_GLASS_PANE;
                case CYAN -> Blocks.CYAN_STAINED_GLASS_PANE;
                case PURPLE -> Blocks.PURPLE_STAINED_GLASS_PANE;
                case BLUE -> Blocks.BLUE_STAINED_GLASS_PANE;
                case BROWN -> Blocks.BROWN_STAINED_GLASS_PANE;
                case GREEN -> Blocks.GREEN_STAINED_GLASS_PANE;
                case RED -> Blocks.RED_STAINED_GLASS_PANE;
                case BLACK -> Blocks.BLACK_STAINED_GLASS_PANE;
            };
            shaped(ModItems.CULTIVATORS.get(color).get())
                    .group(JurassicReborn.MODID + ":cultivate_bottom")
                    .pattern("ABA")
                    .pattern("A A")
                    .pattern("BCB")
                    .define('A', pane)
                    .define('B', Items.IRON_INGOT)
                    .define('C', Items.COMPARATOR)
                    .unlockedBy(getHasName(Items.COMPARATOR), has(Items.COMPARATOR))
                    .save(output, JurassicReborn.resource("cultivate_bottom_" + color.getName()));
        }
        shapeless(ModBlocks.TOUR_RAIL_SLOW.get())
                .group(JurassicReborn.MODID + ":tour_rails_loop")
                .requires(ModBlocks.TOUR_RAIL.get())
                .unlockedBy(getHasName(ModBlocks.TOUR_RAIL.get()), has(ModBlocks.TOUR_RAIL.get()))
                .save(output);

        shapeless(ModBlocks.TOUR_RAIL_MEDIUM.get())
                .group(JurassicReborn.MODID + ":tour_rails_loop")
                .requires(ModBlocks.TOUR_RAIL_SLOW.get())
                .unlockedBy(getHasName(ModBlocks.TOUR_RAIL_SLOW.get()), has(ModBlocks.TOUR_RAIL_SLOW.get()))
                .save(output);

        shapeless(ModBlocks.TOUR_RAIL_FAST.get())
                .group(JurassicReborn.MODID + ":tour_rails_loop")
                .requires(ModBlocks.TOUR_RAIL_MEDIUM.get())
                .unlockedBy(getHasName(ModBlocks.TOUR_RAIL_MEDIUM.get()), has(ModBlocks.TOUR_RAIL_MEDIUM.get()))
                .save(output);

        shapeless(ModBlocks.TOUR_RAIL.get())
                .group(JurassicReborn.MODID + ":tour_rails_loop")
                .requires(ModBlocks.TOUR_RAIL_FAST.get())
                .unlockedBy(getHasName(ModBlocks.TOUR_RAIL_FAST.get()), has(ModBlocks.TOUR_RAIL_FAST.get()))
                .save(output, JurassicReborn.resource("tour_rail_loop"));


        CompoundTag waterTag = new CompoundTag();
        waterTag.putString("Potion", "minecraft:water");

        Ingredient waterPotion = Ingredient.of(Items.POTION);

        smelting(waterPotion, ModItems.DNA_NUCLEOTIDES.get(), 0.35F, 200)
                .unlockedBy(getHasName(Items.POTION), has(Items.POTION))
                .save(output);

        blasting(waterPotion, ModItems.DNA_NUCLEOTIDES.get(), 0.35F, 100)
                .unlockedBy(getHasName(Items.POTION), has(Items.POTION))
                .save(output, JurassicReborn.resource(getItemName(ModItems.DNA_NUCLEOTIDES.get()) + "_from_blasting"));

        campfireCooking(waterPotion, ModItems.DNA_NUCLEOTIDES.get(), 0.35F, 600)
                .unlockedBy(getHasName(Items.POTION), has(Items.POTION))
                .save(output, JurassicReborn.resource(getItemName(ModItems.DNA_NUCLEOTIDES.get()) + "_from_campfire"));

        smelting(Ingredient.of(ModBlocks.GRACILARIA.get()), ModItems.LIQUID_AGAR.get(), 0.35F, 200)
                .unlockedBy(getHasName(ModBlocks.GRACILARIA.get()), has(ModBlocks.GRACILARIA.get()))
                .save(output);
        blasting(Ingredient.of(ModBlocks.GRACILARIA.get()), ModItems.LIQUID_AGAR.get(), 0.35F, 100)
                .unlockedBy(getHasName(ModBlocks.GRACILARIA.get()), has(ModBlocks.GRACILARIA.get()))
                .save(output, JurassicReborn.resource(getItemName(ModItems.LIQUID_AGAR.get()) + "_from_blasting"));
        campfireCooking(Ingredient.of(ModBlocks.GRACILARIA.get()), ModItems.LIQUID_AGAR.get(), 0.35F, 600)
                .unlockedBy(getHasName(ModBlocks.GRACILARIA.get()), has(ModBlocks.GRACILARIA.get()))
                .save(output, JurassicReborn.resource(getItemName(ModItems.LIQUID_AGAR.get()) + "_from_campfire_cooking"));
        smoking(Ingredient.of(ModBlocks.GRACILARIA.get()), ModItems.LIQUID_AGAR.get(), 0.35F, 100)
                .unlockedBy(getHasName(ModBlocks.GRACILARIA.get()), has(ModBlocks.GRACILARIA.get()))
                .save(output, JurassicReborn.resource(getItemName(ModItems.LIQUID_AGAR.get()) + "_from_smoking"));

        smelting(Ingredient.of(ModItems.SHARK_MEAT_RAW.get()), ModItems.SHARK_MEAT_COOKED.get(), 0.35F, 200)
                .unlockedBy(getHasName(ModItems.SHARK_MEAT_RAW.get()), has(ModItems.SHARK_MEAT_RAW.get()))
                .save(output);
        blasting(Ingredient.of(ModItems.SHARK_MEAT_RAW.get()), ModItems.SHARK_MEAT_COOKED.get(), 0.35F, 100)
                .unlockedBy(getHasName(ModItems.SHARK_MEAT_RAW.get()), has(ModItems.SHARK_MEAT_RAW.get()))
                .save(output, JurassicReborn.resource(getItemName(ModItems.SHARK_MEAT_COOKED.get()) + "_from_blasting"));
        campfireCooking(Ingredient.of(ModItems.SHARK_MEAT_RAW.get()), ModItems.SHARK_MEAT_COOKED.get(), 0.35F, 600)
                .unlockedBy(getHasName(ModItems.SHARK_MEAT_RAW.get()), has(ModItems.SHARK_MEAT_RAW.get()))
                .save(output, JurassicReborn.resource(getItemName(ModItems.SHARK_MEAT_COOKED.get()) + "_from_campfire_cooking"));
        smoking(Ingredient.of(ModItems.SHARK_MEAT_RAW.get()), ModItems.SHARK_MEAT_COOKED.get(), 0.35F, 100)
                .unlockedBy(getHasName(ModItems.SHARK_MEAT_RAW.get()), has(ModItems.SHARK_MEAT_RAW.get()))
                .save(output, JurassicReborn.resource(getItemName(ModItems.SHARK_MEAT_COOKED.get()) + "_from_smoking"));
        smelting(Ingredient.of(ModItems.CRAB_MEAT_RAW.get()), ModItems.CRAB_MEAT_COOKED.get(), 0.35F, 200)
                .unlockedBy(getHasName(ModItems.CRAB_MEAT_RAW.get()), has(ModItems.CRAB_MEAT_RAW.get()))
                .save(output);
        blasting(Ingredient.of(ModItems.CRAB_MEAT_RAW.get()), ModItems.CRAB_MEAT_COOKED.get(), 0.35F, 100)
                .unlockedBy(getHasName(ModItems.CRAB_MEAT_RAW.get()), has(ModItems.CRAB_MEAT_RAW.get()))
                .save(output, JurassicReborn.resource(getItemName(ModItems.CRAB_MEAT_COOKED.get()) + "_from_blasting"));
        campfireCooking(Ingredient.of(ModItems.CRAB_MEAT_RAW.get()), ModItems.CRAB_MEAT_COOKED.get(), 0.35F, 600)
                .unlockedBy(getHasName(ModItems.CRAB_MEAT_RAW.get()), has(ModItems.CRAB_MEAT_RAW.get()))
                .save(output, JurassicReborn.resource(getItemName(ModItems.CRAB_MEAT_COOKED.get()) + "_from_campfire_cooking"));
        smoking(Ingredient.of(ModItems.CRAB_MEAT_RAW.get()), ModItems.CRAB_MEAT_COOKED.get(), 0.35F, 100)
                .unlockedBy(getHasName(ModItems.CRAB_MEAT_RAW.get()), has(ModItems.CRAB_MEAT_RAW.get()))
                .save(output, JurassicReborn.resource(getItemName(ModItems.CRAB_MEAT_COOKED.get()) + "_from_smoking"));
        smelting(Ingredient.of(ModItems.GOAT_RAW.get()), ModItems.GOAT_COOKED.get(), 0.35F, 200)
                .unlockedBy(getHasName(ModItems.GOAT_RAW.get()), has(ModItems.GOAT_RAW.get()))
                .save(output);
        blasting(Ingredient.of(ModItems.GOAT_RAW.get()), ModItems.GOAT_COOKED.get(), 0.35F, 100)
                .unlockedBy(getHasName(ModItems.GOAT_RAW.get()), has(ModItems.GOAT_RAW.get()))
                .save(output, JurassicReborn.resource(getItemName(ModItems.GOAT_COOKED.get()) + "_from_blasting"));
        campfireCooking(Ingredient.of(ModItems.GOAT_RAW.get()), ModItems.GOAT_COOKED.get(), 0.35F, 600)
                .unlockedBy(getHasName(ModItems.GOAT_RAW.get()), has(ModItems.GOAT_RAW.get()))
                .save(output, JurassicReborn.resource(getItemName(ModItems.GOAT_COOKED.get()) + "_from_campfire_cooking"));
        smoking(Ingredient.of(ModItems.GOAT_RAW.get()), ModItems.GOAT_COOKED.get(), 0.35F, 100)
                .unlockedBy(getHasName(ModItems.GOAT_RAW.get()), has(ModItems.GOAT_RAW.get()))
                .save(output, JurassicReborn.resource(getItemName(ModItems.GOAT_COOKED.get()) + "_from_smoking"));
        ModItems.MEATS.forEach((dino, meat) -> {
            ItemLike steak = ModItems.STEAKS.get(dino).get();
            smoking(Ingredient.of(meat.get()), steak, 0.35F, 100)
                    .unlockedBy(getHasName(meat.get()), has(meat.get()))
                    .save(output, JurassicReborn.resource(getItemName(steak) + "_from_smoking"));
            smelting(Ingredient.of(meat.get()), steak, 0.35F, 200)
                    .unlockedBy(getHasName(meat.get()), has(meat.get()))
                    .save(output, JurassicReborn.resource(getItemName(steak) + "_from_smelting"));
            blasting(Ingredient.of(meat.get()), steak, 0.35F, 100)
                    .unlockedBy(getHasName(meat.get()), has(meat.get()))
                    .save(output, JurassicReborn.resource(getItemName(steak) + "_from_blasting"));
            campfireCooking(Ingredient.of(meat.get()), steak, 0.35F, 600)
                    .unlockedBy(getHasName(meat.get()), has(meat.get()))
                    .save(output, JurassicReborn.resource(getItemName(steak) + "_from_campfire_cooking"));
        });

        shaped(ModItems.BLACK_JEEP_WRANGLER.get())
                .pattern("ABA")
                .pattern("CDB")
                .pattern("ABA")
                .define('A', Items.LIGHT_GRAY_DYE)
                .define('B', Items.BLACK_DYE)
                .define('C', ModItems.CAR_TIRE.get())
                .define('D', ModItems.UNFINISHED_CAR.get())
                .unlockedBy(getHasName(ModItems.UNFINISHED_CAR.get()), has(ModItems.UNFINISHED_CAR.get()))
                .save(output);

        shaped(ModItems.BLUE_JEEP_WRANGLER.get())
                .pattern("ABA")
                .pattern("CDB")
                .pattern("ABA")
                .define('A', Items.LIGHT_GRAY_DYE)
                .define('B', Items.CYAN_DYE)
                .define('C', ModItems.CAR_TIRE.get())
                .define('D', ModItems.UNFINISHED_CAR.get())
                .unlockedBy(getHasName(ModItems.UNFINISHED_CAR.get()), has(ModItems.UNFINISHED_CAR.get()))
                .save(output);

        shaped(ModItems.GREEN_JEEP_WRANGLER.get())
                .pattern("ABA")
                .pattern("CDB")
                .pattern("ABA")
                .define('A', Items.LIGHT_GRAY_DYE)
                .define('B', Items.GREEN_DYE)
                .define('C', ModItems.CAR_TIRE.get())
                .define('D', ModItems.UNFINISHED_CAR.get())
                .unlockedBy(getHasName(ModItems.UNFINISHED_CAR.get()), has(ModItems.UNFINISHED_CAR.get()))
                .save(output);

        shaped(ModItems.LIME_JEEP_WRANGLER.get())
                .pattern("ABA")
                .pattern("CDB")
                .pattern("ABA")
                .define('A', Items.LIGHT_GRAY_DYE)
                .define('B', Items.LIME_DYE)
                .define('C', ModItems.CAR_TIRE.get())
                .define('D', ModItems.UNFINISHED_CAR.get())
                .unlockedBy(getHasName(ModItems.UNFINISHED_CAR.get()), has(ModItems.UNFINISHED_CAR.get()))
                .save(output);

        shaped(ModItems.PINK_JEEP_WRANGLER.get())
                .pattern("ABA")
                .pattern("CDB")
                .pattern("ABA")
                .define('A', Items.LIGHT_GRAY_DYE)
                .define('B', Items.PINK_DYE)
                .define('C', ModItems.CAR_TIRE.get())
                .define('D', ModItems.UNFINISHED_CAR.get())
                .unlockedBy(getHasName(ModItems.UNFINISHED_CAR.get()), has(ModItems.UNFINISHED_CAR.get()))
                .save(output);

        shaped(ModItems.PURPLE_JEEP_WRANGLER.get())
                .pattern("ABA")
                .pattern("CDB")
                .pattern("ABA")
                .define('A', Items.LIGHT_GRAY_DYE)
                .define('B', Items.PURPLE_DYE)
                .define('C', ModItems.CAR_TIRE.get())
                .define('D', ModItems.UNFINISHED_CAR.get())
                .unlockedBy(getHasName(ModItems.UNFINISHED_CAR.get()), has(ModItems.UNFINISHED_CAR.get()))
                .save(output);

        shaped(ModItems.SORNA_JEEP_WRANGLER.get())
                .pattern("ABA")
                .pattern("CDB")
                .pattern("ABA")
                .define('A', Items.BLACK_DYE)
                .define('B', Items.GREEN_DYE)
                .define('C', ModItems.CAR_TIRE.get())
                .define('D', ModItems.UNFINISHED_CAR.get())
                .unlockedBy(getHasName(ModItems.UNFINISHED_CAR.get()), has(ModItems.UNFINISHED_CAR.get()))
                .save(output);

        shaped(ModItems.GLOCK.get())
                .pattern("CCC")
                .pattern(" EA")
                .define('A', Items.STRING)
                .define('C', Items.IRON_INGOT)
                .define('E', Items.REDSTONE)
                .unlockedBy(getHasName(Items.REDSTONE), has(Items.REDSTONE))
                .save(output);

        shaped(ModItems.REMINGTON.get())
                .pattern("EAC")
                .pattern("CDC")
                .pattern("  C")
                .define('A', Items.STRING)
                .define('C', Items.IRON_INGOT)
                .define('D', Items.GOLD_INGOT)
                .define('E', Items.REDSTONE)
                .unlockedBy(getHasName(Items.REDSTONE), has(Items.REDSTONE))
                .save(output);

        shaped(ModItems.SPAS12.get())
                .pattern("  A")
                .pattern("CDC")
                .pattern("ECC")
                .define('A', Items.STRING)
                .define('C', Items.IRON_INGOT)
                .define('D', Items.GOLD_INGOT)
                .define('E', Items.REDSTONE)
                .unlockedBy(getHasName(Items.REDSTONE), has(Items.REDSTONE))
                .save(output);

        shaped(ModItems.UTS15.get())
                .pattern(" AE")
                .pattern("CCC")
                .pattern("DDC")
                .define('A', Items.STRING)
                .define('C', Items.IRON_INGOT)
                .define('D', Items.GOLD_INGOT)
                .define('E', Items.REDSTONE)
                .unlockedBy(getHasName(Items.REDSTONE), has(Items.REDSTONE))
                .save(output);

        shaped(ModItems.BULLET.get())
                .group(JurassicReborn.MODID + ":bullet")
                .pattern("AAE")
                .pattern(" EC")
                .define('A', Items.GUNPOWDER)
                .define('E', Items.GOLD_INGOT)
                .define('C', Items.IRON_INGOT)
                .unlockedBy(getHasName(Items.GUNPOWDER), has(Items.GUNPOWDER))
                .save(output);




        shaped(ModBlocks.BUG_CRATE.get())
                .pattern("AAA")
                .pattern("BCB")
                .pattern("DDD")
                .define('A',Tags.Items.NUGGETS_IRON)
                .define('B', ItemTags.PLANKS)
                .define('C',Tags.Items.CHESTS_WOODEN)
                .define('D', Ingredient.of(Blocks.STONE_SLAB,Blocks.STONE_BRICK_SLAB))
                .unlockedBy(getHasName(Blocks.STONE_SLAB),has(Blocks.STONE_SLAB))
                .save(output);

        shaped(ModBlocks.EMBRYONIC_MACHINE.get())
                .pattern("ABC")
                .pattern("ADC")
                .pattern("EEC")
                .define('A',Tags.Items.NUGGETS_IRON)
                .define('B', Blocks.PISTON)
                .define('C',Items.STONE_BUTTON)
                .define('D', Items.GLOWSTONE_DUST)
                .define('E',Tags.Items.INGOTS_IRON)
                .unlockedBy(getHasName(Blocks.PISTON),has(Blocks.PISTON))
                .save(output);

        shaped(ModItems.EMPTY_TEST_TUBE.get(),8)
                .pattern("A")
                .pattern("A")
                .define('A',Blocks.GLASS)
                .unlockedBy(getHasName(Blocks.GLASS),has(Blocks.GLASS))
                .save(output);

        shaped(ModItems.FORD_EXPLORER.get())
                .pattern("GWG")
                .pattern("LCR")
                .pattern("YYY")
                .define('R',Items.GREEN_DYE)
                .define('W',Items.GLASS_PANE)
                .define('G',Items.RED_DYE)
                .define('L',Items.LIME_DYE)
                .define('C',ModItems.UNFINISHED_CAR.get())
                .define('Y',Items.YELLOW_DYE)
                .unlockedBy(getHasName(ModItems.UNFINISHED_CAR.get()),has(ModItems.UNFINISHED_CAR.get()))
                .save(output);
        shaped(ModItems.MONORAIL.get())
                .group(JurassicReborn.MODID + ":ford_explorer")
                .pattern("RWG")
                .pattern("GCR")
                .pattern("YYY")
                .define('R', Items.LAPIS_LAZULI)
                .define('W', Items.GLASS_PANE)
                .define('G', Items.LIGHT_BLUE_DYE)
                .define('C', ModItems.UNFINISHED_CAR.get())
                .define('Y', Items.IRON_BLOCK)
                .unlockedBy("has_unfinished_car", has(ModItems.UNFINISHED_CAR.get()))
                .save(output);
        shaped(ModItems.FORD_EXPLORER_SNOW.get())
                .pattern("RWY")
                .pattern("RCR")
                .pattern("YYY")
                .define('R',Items.BROWN_DYE)
                .define('W',Items.GLASS_PANE)
                .define('C',ModItems.UNFINISHED_CAR.get())
                .define('Y',Items.LIGHT_GRAY_DYE)
                .unlockedBy(getHasName(ModItems.UNFINISHED_CAR.get()),has(ModItems.UNFINISHED_CAR.get()))
                .save(output);
        shaped(ModBlocks.PARK_BENCH.get())
                .group(JurassicReborn.MODID + ":park_bench")
                .pattern("PPP")
                .pattern("DPD")
                .pattern("D D")
                .define('P', ItemTags.PLANKS)
                .define('D', Blocks.POLISHED_DEEPSLATE)
                .unlockedBy(getHasName(Blocks.POLISHED_DEEPSLATE), has(Blocks.POLISHED_DEEPSLATE))
                .save(output);
        shaped(ModBlocks.TRASH_CAN.get())
                .group(JurassicReborn.MODID + ":trash_can")
                .pattern("PPP")
                .pattern("DDD")
                .pattern("DDD")
                .define('P', Blocks.YELLOW_CONCRETE)
                .define('D', Blocks.GREEN_CONCRETE)
                .unlockedBy(getHasName(Blocks.GREEN_CONCRETE), has(Blocks.GREEN_CONCRETE))
                .save(output);
        shaped(ModBlocks.FOSSIL_GRINDER.get())
                .pattern("WRL")
                .pattern("WYW")
                .pattern("LLL")
                .define('W',Items.ORANGE_DYE)
                .define('R',ModBlocks.REINFORCED_STONE.get())
                .define('L',Items.GRAY_DYE)
                .define('Y',ModItems.IRON_BLADES.get())
                .unlockedBy(getHasName(ModItems.IRON_BLADES.get()),has(ModItems.IRON_BLADES.get()))
                .save(output);
        shaped(ModItems.PADDOCK_SIGN.get())
                .group(JurassicReborn.MODID + ":paddock_sign")
                .pattern("AAA")
                .pattern("BBB")
                .pattern("AAA")
                .define('A', Tags.Items.NUGGETS_IRON)
                .define('B', ItemTags.SIGNS)
                .unlockedBy(getHasName(Items.IRON_NUGGET), has(Tags.Items.NUGGETS_IRON))
                .save(output);

        AttractionSignEntity.AttractionSignType[] types = AttractionSignEntity.AttractionSignType.values();
        for (int i = 0; i < types.length; i++) {
            DeferredHolder<Item, Item> sign = ModItems.ATTRACTION_SIGNS.get(types[i]);
            if (i == 0) {
                shaped(sign.get())
                        .group(JurassicReborn.MODID + ":attraction_sign")
                        .pattern("AAA")
                        .pattern("BBB")
                        .pattern("AAA")
                        .define('A', Tags.Items.INGOTS_IRON)
                        .define('B', ItemTags.SIGNS)
                        .unlockedBy(getHasName(Items.IRON_INGOT), has(Items.IRON_INGOT))
                        .save(output);
            } else {
                DeferredHolder<Item, Item> prev = ModItems.ATTRACTION_SIGNS.get(types[i - 1]);
                shapeless(sign.get())
                        .group(JurassicReborn.MODID + ":attraction_sign")
                        .requires(prev.get())
                        .unlockedBy(getHasName(prev.get()), has(prev.get()))
                        .save(output, JurassicReborn.MODID + ":" + sign.getId().getPath() + "_from_" + prev.getId().getPath());
            }
        }
        DeferredHolder<Item, Item> first = ModItems.ATTRACTION_SIGNS.get(types[0]);
        DeferredHolder<Item, Item> last = ModItems.ATTRACTION_SIGNS.get(types[types.length - 1]);
        shapeless(first.get())
                .group(JurassicReborn.MODID + ":attraction_sign")
                .requires(last.get())
                .unlockedBy(getHasName(last.get()), has(last.get()))
                .save(output, JurassicReborn.MODID + ":" + first.getId().getPath() + "_from_" + last.getId().getPath());

        shapeless(ModItems.BASIC_CIRCUIT.get(), 2)
                .requires(Ingredient.of(Tags.Items.NUGGETS_IRON), 4)
                .requires(Items.GOLD_NUGGET, 4)
                .requires(Items.REDSTONE)
                .unlockedBy(getHasName(Items.REDSTONE), has(Items.REDSTONE))
                .save(output);

        shapeless(ModItems.ADVANCED_CIRCUIT.get())
                .requires(ModItems.BASIC_CIRCUIT.get(), 2)
                .requires(Tags.Items.NUGGETS_IRON)
                .requires(Items.GOLD_NUGGET)
                .requires(Items.REDSTONE)
                .unlockedBy(getHasName(ModItems.BASIC_CIRCUIT.get()), has(ModItems.BASIC_CIRCUIT.get()))
                .save(output);

        shaped(ModItems.LASER.get())
                .group(JurassicReborn.MODID + ":laser")
                .pattern("ABA")
                .pattern("ACA")
                .pattern("ADA")
                .define('A', Tags.Items.NUGGETS_IRON)
                .define('B', Items.DIAMOND)
                .define('C', ModItems.BASIC_CIRCUIT.get())
                .define('D', Items.REDSTONE)
                .unlockedBy(getHasName(ModItems.BASIC_CIRCUIT.get()), has(ModItems.BASIC_CIRCUIT.get()))
                .save(output);

        shaped(ModItems.DISC_DRIVE.get())
                .group(JurassicReborn.MODID + ":disc_reader")
                .pattern("ABC")
                .pattern("D  ")
                .pattern("DDE")
                .define('A', ModItems.ADVANCED_CIRCUIT.get())
                .define('B', ModItems.LASER.get())
                .define('C', Items.IRON_INGOT)
                .define('D', Tags.Items.NUGGETS_IRON)
                .define('E', Items.STONE_BUTTON)
                .unlockedBy(getHasName(ModItems.ADVANCED_CIRCUIT.get()), has(ModItems.ADVANCED_CIRCUIT.get()))
                .save(output);

        shaped(ModItems.COMPUTER_SCREEN.get())
                .pattern("ABA")
                .pattern("CDE")
                .pattern("FGA")
                .define('A', Items.IRON_INGOT)
                .define('B', Items.GREEN_DYE)
                .define('C', Items.ORANGE_DYE)
                .define('D', Items.GLASS_PANE)
                .define('E', Items.BLUE_DYE)
                .define('F', ModItems.BASIC_CIRCUIT.get())
                .define('G', Items.QUARTZ)
                .unlockedBy(getHasName(ModItems.BASIC_CIRCUIT.get()), has(ModItems.BASIC_CIRCUIT.get()))
                .save(output);

        shaped(ModItems.STORAGE_DISC.get())
                .group(JurassicReborn.MODID + ":storage_disc")
                .pattern(" A ")
                .pattern("ABA")
                .pattern(" A ")
                .define('A', Tags.Items.NUGGETS_IRON)
                .define('B', ModItems.BASIC_CIRCUIT.get())
                .unlockedBy(getHasName(ModItems.BASIC_CIRCUIT.get()), has(ModItems.BASIC_CIRCUIT.get()))
                .save(output);

        shaped(ModItems.CAR_CHASSIS.get())
                .pattern("AAA")
                .pattern("A A")
                .pattern("AAA")
                .define('A', Items.IRON_INGOT)
                .unlockedBy(getHasName(Items.IRON_INGOT), has(Items.IRON_INGOT))
                .save(output);

        shaped(ModItems.CAR_SEATS.get())
                .pattern("ABA")
                .pattern("ACA")
                .pattern("DDD")
                .define('A', Items.LEATHER)
                .define('B', Items.WHITE_WOOL)
                .define('C', Items.SADDLE)
                .define('D', Items.IRON_INGOT)
                .unlockedBy(getHasName(Items.SADDLE), has(Items.SADDLE))
                .save(output);

        shaped(ModItems.CAR_TIRE.get(),2)
                .pattern("ABA")
                .pattern("BCB")
                .pattern("ABA")
                .define('A', Items.BLACK_DYE)
                .define('B', Items.SLIME_BALL)
                .define('C', Items.PISTON)
                .unlockedBy(getHasName(Items.PISTON), has(Items.PISTON))
                .save(output);

        shaped(ModItems.CAR_WINDSCREEN.get())
                .pattern(" A ")
                .pattern("ABA")
                .pattern(" A ")
                .define('A', Items.IRON_INGOT)
                .define('B', Items.GLASS_PANE)
                .unlockedBy(getHasName(Items.GLASS_PANE), has(Items.GLASS_PANE))
                .save(output);

        shaped(ModItems.ENGINE_SYSTEM.get())
                .pattern("ABC")
                .pattern("DEF")
                .pattern("AGH")
                .define('A', Items.IRON_BARS)
                .define('B', Items.IRON_INGOT)
                .define('C', Items.LEVER)
                .define('D', Items.COMPARATOR)
                .define('E', Items.REPEATER)
                .define('F', Items.CAULDRON)
                .define('G', Items.STONE_PRESSURE_PLATE)
                .define('H', Items.PISTON)
                .unlockedBy(getHasName(Items.IRON_BARS), has(Items.IRON_BARS))
                .save(output);

        shaped(ModItems.UNFINISHED_CAR.get())
                .pattern("ABC")
                .pattern("DEF")
                .pattern("DDD")
                .define('A', ModItems.CAR_SEATS.get())
                .define('B', ModItems.CAR_CHASSIS.get())
                .define('C', ModItems.CAR_WINDSCREEN.get())
                .define('D', ModItems.CAR_TIRE.get())
                .define('E', ModItems.ENGINE_SYSTEM.get())
                .define('F', Items.IRON_INGOT)
                .unlockedBy(getHasName(ModItems.CAR_CHASSIS.get()), has(ModItems.CAR_CHASSIS.get()))
                .save(output);

        shaped(ModItems.PLASTER_AND_BANDAGE.get(),9)
                .pattern("ABA")
                .pattern("BCB")
                .pattern("ABA")
                .define('A', Items.PAPER)
                .define('B', ModItems.GYPSUM_POWDER.get())
                .define('C', Items.WHITE_WOOL)
                .unlockedBy(getHasName(ModItems.GYPSUM_POWDER.get()), has(ModItems.GYPSUM_POWDER.get()))
                .save(output);

        shaped(ModItems.PETRI_DISH.get(),4)
                .pattern("A A")
                .pattern("AAA")
                .define('A', Items.GLASS_PANE)
                .unlockedBy(getHasName(Items.GLASS_PANE), has(Items.GLASS_PANE))
                .save(output);

        shapeless(ModItems.PETRI_DISH_AGAR.get(),4)
                .requires(ModItems.PETRI_DISH.get(),4)
                .requires(ModItems.LIQUID_AGAR.get())
                .unlockedBy(getHasName(ModItems.LIQUID_AGAR.get()), has(ModItems.LIQUID_AGAR.get()))
                .save(output);

        shapeless(ModItems.PLANT_CELLS_PETRI_DISH.get())
                .requires(ModItems.PLANT_CELLS.get())
                .requires(ModItems.PETRI_DISH_AGAR.get())
                .unlockedBy(getHasName(ModItems.PLANT_CELLS.get()), has(ModItems.PLANT_CELLS.get()))
                .save(output);

        shaped(ModItems.PALEO_PAD.get())
                .pattern("BDB")
                .pattern("ACA")
                .pattern("BDB")
                .define('A', ModItems.BASIC_CIRCUIT.get())
                .define('B', Items.IRON_INGOT)
                .define('C', Items.GLASS_PANE)
                .define('D', Items.DIAMOND)
                .unlockedBy(getHasName(ModItems.BASIC_CIRCUIT.get()), has(ModItems.BASIC_CIRCUIT.get()))
                .save(output);

        shaped(ModBlocks.GYPSUM_STONE.get())
                .pattern("AAA")
                .pattern("AAA")
                .pattern("AAA")
                .define('A', ModItems.GYPSUM_POWDER.get())
                .unlockedBy(getHasName(ModItems.GYPSUM_POWDER.get()), has(ModItems.GYPSUM_POWDER.get()))
                .save(output);

        //Gypsum Brick block recipe
        shaped(ModBlocks.GYPSUM_BRICKS.get(),4)
                .pattern("AA")
                .pattern("AA")
                .define('A', ModBlocks.GYPSUM_STONE.get())
                .unlockedBy(getHasName(ModBlocks.GYPSUM_STONE.get()), has(ModBlocks.GYPSUM_STONE.get()))
                .save(output);
//
//       shaped(ModBlocks.GYPSUM_BRICK_STAIRS.get(),4)
//                .pattern("A  ")
//                .pattern("AA ")
//                .pattern("AAA")
//                .define('A', ModBlocks.GYPSUM_BRICKS.get())
//                .unlockedBy(getHasName(ModBlocks.GYPSUM_BRICKS.get()), has(ModBlocks.GYPSUM_BRICKS.get()))
//                .save(output);
//
//       shaped(ModBlocks.GYPSUM_BRICK_SLAB.get(),6)
//                .pattern("AAA")
//                .define('A', ModBlocks.GYPSUM_BRICKS.get())
//                .unlockedBy(getHasName(ModBlocks.GYPSUM_BRICKS.get()), has(ModBlocks.GYPSUM_BRICKS.get()))
//                .save(output);
//
//       shaped(ModBlocks.GYPSUM_BRICK_WALL.get(),6)
//                .pattern("AAA")
//                .pattern("AAA")
//                .define('A', ModBlocks.GYPSUM_BRICKS.get())
//                .unlockedBy(getHasName(ModBlocks.GYPSUM_BRICKS.get()), has(ModBlocks.GYPSUM_BRICKS.get()))
//                .save(output);
//
//       shaped(ModBlocks.GYPSUM_BRICK_PRESSURE_PLATE.get())
//                .pattern("AA")
//                .define('A', ModBlocks.GYPSUM_BRICKS.get())
//                .unlockedBy(getHasName(ModBlocks.GYPSUM_BRICKS.get()), has(ModBlocks.GYPSUM_BRICKS.get()))
//                .save(output);
//
//        shapeless(ModBlocks.GYPSUM_BRICK_BUTTON.get())
//                .requires(ModBlocks.GYPSUM_BRICKS.get())
//                .unlockedBy(getHasName(ModBlocks.GYPSUM_BRICKS.get()), has(ModBlocks.GYPSUM_BRICKS.get()))
//                .save(output);

        shaped(ModItems.FINE_NET.get())
                .group(JurassicReborn.MODID + ":fine_net")
                .pattern("ABC")
                .pattern(" BB")
                .pattern("B A")
                .define('A', Items.STRING)
                .define('B', Items.STICK)
                .define('C', Items.WHITE_WOOL)
                .unlockedBy(getHasName(Items.STRING), has(Items.STRING))
                .save(output);

        shaped(ModBlocks.LOW_SECURITY_FENCE_BASE.get(),6)
                .pattern("ABA")
                .pattern("CCC")
                .pattern(" D ")
                .define('A', Tags.Items.NUGGETS_IRON)
                .define('B', Items.IRON_INGOT)
                .define('C', Blocks.STONE)
                .define('D', Items.REDSTONE)
                .unlockedBy(getHasName(Items.REDSTONE), has(Items.REDSTONE))
                .save(output);

        shaped(ModBlocks.LOW_SECURITY_FENCE_WIRE.get(),16)
                .pattern("AAA")
                .pattern(" B ")
                .pattern("AAA")
                .define('A', Items.IRON_INGOT)
                .define('B', Items.REDSTONE)
                .unlockedBy(getHasName(Items.REDSTONE), has(Items.REDSTONE))
                .save(output);

        shaped(ModBlocks.LOW_SECURITY_FENCE_POLE.get(),2)
                .pattern("A")
                .pattern("B")
                .pattern("B")
                .define('A', Items.REDSTONE)
                .define('B', Items.IRON_INGOT)
                .unlockedBy(getHasName(Items.REDSTONE), has(Items.REDSTONE))
                .save(output);

        shaped(ModBlocks.CLEANING_STATION.get())
                .pattern("AAA")
                .pattern("BCB")
                .pattern("DED")
                .define('A', Items.IRON_INGOT)
                .define('B', Items.REDSTONE)
                .define('C', Items.GLASS_PANE)
                .define('D', Items.IRON_BLOCK)
                .define('E', Items.BUCKET)
                .unlockedBy(getHasName(Items.BUCKET), has(Items.BUCKET))
                .save(output);

        shaped(ModBlocks.SKELETON_ASSEMBLY.get())
                .pattern("ABB")
                .pattern("CDC")
                .pattern("EFE")
                .define('A', Items.LAPIS_LAZULI)
                .define('B', Items.PAPER)
                .define('C', ItemTags.WOODEN_SLABS)
                .define('D', Blocks.CRAFTING_TABLE)
                .define('E', ItemTags.WOODEN_FENCES)
                .define('F', ModBlocks.LOW_SECURITY_FENCE_WIRE.get())
                .unlockedBy(getHasName(ModBlocks.LOW_SECURITY_FENCE_WIRE.get()), has(ModBlocks.LOW_SECURITY_FENCE_WIRE.get()))
                .save(output);

        shaped(ModBlocks.TOUR_RAIL.get(),16)
                .group(JurassicReborn.MODID + ":tour_rail")
                .pattern("BRB")
                .pattern("TRT")
                .pattern("BRB")
                .define('B', Items.IRON_BARS)
                .define('R', Items.RAIL)
                .define('T', Tags.Items.DUSTS_REDSTONE)
                .unlockedBy(getHasName(Items.RAIL), has(Items.RAIL))
                .save(output);

        shaped(ModItems.MURAL.get())
                .group(JurassicReborn.MODID + ":mural")
                .pattern("ABA")
                .pattern("BCB")
                .pattern("ABA")
                .define('A', Items.IRON_INGOT)
                .define('B', Items.WHITE_WOOL)
                .define('C', Items.PAINTING)
                .unlockedBy(getHasName(Items.PAINTING), has(Items.PAINTING))
                .save(output);

        shaped(ModItems.FIELD_GUIDE.get())
                .pattern("ABA")
                .pattern("CCC")
                .pattern("ABA")
                .define('A', Items.LIGHT_BLUE_WOOL)
                .define('B', Items.BONE)
                .define('C', Items.PAPER)
                .unlockedBy(getHasName(Items.PAPER), has(Items.PAPER))
                .save(output);

        shaped(ModItems.EMPTY_SYRINGE.get())
                .pattern(" A ")
                .pattern(" B ")
                .pattern("AAA")
                .define('A', Tags.Items.NUGGETS_IRON)
                .define('B', ModItems.EMPTY_TEST_TUBE.get())
                .unlockedBy(getHasName(ModItems.EMPTY_TEST_TUBE.get()), has(ModItems.EMPTY_TEST_TUBE.get()))
                .save(output);

        shaped(ModItems.PREGNANCY_TEST.get())
                .pattern("P  ")
                .pattern(" R ")
                .pattern("  W")
                .define('P', Items.PINK_WOOL)
                .define('R', Tags.Items.DUSTS_REDSTONE)
                .define('W', Items.WHITE_WOOL)
                .unlockedBy(getHasName(Items.PINK_WOOL), has(Items.PINK_WOOL))
                .save(output);

        shaped(ModItems.DART_GUN.get())
                .group(JurassicReborn.MODID + ":dart_gun")
                .pattern("AAA")
                .pattern("CCC")
                .pattern(" EC")
                .define('A', Items.STRING)
                .define('C', Items.IRON_INGOT)
                .define('E', Items.REDSTONE)
                .unlockedBy(getHasName(Items.IRON_INGOT), has(Items.IRON_INGOT))
                .save(output);

        shaped(ModItems.DART_TRANQUILIZER.get(),6)
                .group(JurassicReborn.MODID + ":dart_tranquilizer")
                .pattern(" F")
                .pattern("N ")
                .define('F', Items.FLINT)
                .define('N', Tags.Items.NUGGETS_IRON)
                .unlockedBy(getHasName(Items.FLINT), has(Items.FLINT))
                .save(output);

        shaped(ModItems.TRACKER_DART.get(),6)
                .group(JurassicReborn.MODID + ":tracking_dart")
                .pattern(" F")
                .pattern("N ")
                .define('F', Items.COMPASS)
                .define('N', Tags.Items.NUGGETS_IRON)
                .unlockedBy(getHasName(Items.COMPASS), has(Items.COMPASS))
                .save(output);

        shapeless(ModItems.DART_POISON_CYCASIN.get())
                .requires(ModItems.DART_TRANQUILIZER.get())
                .requires(Items.SPIDER_EYE)
                .unlockedBy(getHasName(ModItems.DART_TRANQUILIZER.get()), has(ModItems.DART_TRANQUILIZER.get()))
                .save(output, JurassicReborn.resource("dart_poison"));

        shapeless(ModItems.DART_POISON_EXECUTIONER_CONCOCTION.get())
                .requires(ModItems.DART_POISON_CYCASIN.get())
                .requires(Items.SPIDER_EYE)
                .unlockedBy(getHasName(ModItems.DART_POISON_CYCASIN.get()), has(ModItems.DART_POISON_CYCASIN.get()))
                .save(output, JurassicReborn.resource("dart_lethal"));

        shaped(ModItems.DNA_ANALYZER.get())
                .pattern("ABA")
                .pattern("CDA")
                .pattern("AAA")
                .define('A', Tags.Items.NUGGETS_IRON)
                .define('B', Items.GLASS)
                .define('C', ModItems.ADVANCED_CIRCUIT.get())
                .define('D', ModItems.LASER.get())
                .unlockedBy(getHasName(ModItems.ADVANCED_CIRCUIT.get()), has(ModItems.ADVANCED_CIRCUIT.get()))
                .save(output);

        shaped(ModBlocks.DNA_EXTRACTOR.get())
                .pattern("AAA")
                .pattern("BCD")
                .pattern("EAA")
                .define('A', Items.IRON_INGOT)
                .define('B', ModItems.COMPUTER_SCREEN.get())
                .define('C', ModItems.DNA_ANALYZER.get())
                .define('D', Items.GLASS_PANE)
                .define('E', ModItems.DISC_DRIVE.get())
                .unlockedBy(getHasName(ModItems.DNA_ANALYZER.get()), has(ModItems.DNA_ANALYZER.get()))
                .save(output);

        shaped(ModBlocks.DNA_SEQUENCER.get())
                .pattern("ABA")
                .pattern("CBA")
                .pattern("DBE")
                .define('A', Items.IRON_INGOT)
                .define('B', ModItems.DNA_ANALYZER.get())
                .define('C', ModItems.COMPUTER_SCREEN.get())
                .define('D', ModItems.DISC_DRIVE.get())
                .define('E', ModItems.KEYBOARD.get())
                .unlockedBy(getHasName(ModItems.DNA_ANALYZER.get()), has(ModItems.DNA_ANALYZER.get()))
                .save(output);
        shaped(ModItems.GYROSPHERE_SEATS.get())
                .group(JurassicReborn.MODID + ":gyrosphere_seats")
                .pattern("ABA")
                .pattern("ACA")
                .pattern("DDD")
                .define('A', Items.LEATHER)
                .define('B', Items.BLUE_WOOL)
                .define('C', Items.SADDLE)
                .define('D', Items.IRON_INGOT)
                .unlockedBy(getHasName(Items.SADDLE), has(Items.SADDLE))
                .save(output);

        shaped(ModItems.GYROSPHERE_HOOP.get())
                .group(JurassicReborn.MODID + ":gyrosphere_hoop")
                .pattern("AAA")
                .pattern("A A")
                .pattern("AAA")
                .define('A', ModItems.IRON_ROD.get())
                .unlockedBy(getHasName(ModItems.IRON_ROD.get()), has(ModItems.IRON_ROD.get()))
                .save(output);

        shaped(ModItems.GYROSPHERE_INTERIOR.get())
                .group(JurassicReborn.MODID + ":gyrosphere_interior")
                .pattern("CCC")
                .pattern("BAB")
                .pattern("CCC")
                .define('A', ModItems.GYROSPHERE_SEATS.get())
                .define('B', ModItems.GYROSPHERE_HOOP.get())
                .define('C', Items.IRON_INGOT)
                .unlockedBy(getHasName(ModItems.GYROSPHERE_SEATS.get()), has(ModItems.GYROSPHERE_SEATS.get()))
                .save(output);
        shaped(ModBlocks.MED_SECURITY_FENCE_BASE.get(),6)
                .pattern("ABA")
                .pattern("CCC")
                .pattern("BDB")
                .define('A', Tags.Items.NUGGETS_IRON)
                .define('B', Items.IRON_INGOT)
                .define('C', Blocks.STONE)
                .define('D', Items.REDSTONE)
                .unlockedBy(getHasName(Items.REDSTONE), has(Items.REDSTONE))
                .save(output);

        shaped(ModBlocks.MED_SECURITY_FENCE_WIRE.get(),6)
                .pattern("AAA")
                .pattern(" B ")
                .pattern("AAA")
                .define('A', ModBlocks.LOW_SECURITY_FENCE_WIRE.get())
                .define('B', Items.REDSTONE)
                .unlockedBy(getHasName(ModBlocks.LOW_SECURITY_FENCE_WIRE.get()), has(ModBlocks.LOW_SECURITY_FENCE_WIRE.get()))
                .save(output);

        shaped(ModBlocks.MED_SECURITY_FENCE_POLE.get(),2)
                .pattern("AA")
                .pattern("BB")
                .pattern("BB")
                .define('A', Items.REDSTONE)
                .define('B', Items.IRON_INGOT)
                .unlockedBy(getHasName(Items.REDSTONE), has(Items.REDSTONE))
                .save(output);

        shaped(ModBlocks.HIGH_SECURITY_FENCE_BASE.get(),6)
                .pattern("BBB")
                .pattern("CCC")
                .pattern("BBB")
                .define('B', Items.IRON_INGOT)
                .define('C', ModBlocks.MED_SECURITY_FENCE_BASE.get())
                .unlockedBy(getHasName(ModBlocks.MED_SECURITY_FENCE_BASE.get()), has(ModBlocks.MED_SECURITY_FENCE_BASE.get()))
                .save(output);

        shaped(ModBlocks.HIGH_SECURITY_FENCE_WIRE.get(),6)
                .pattern("AAA")
                .pattern(" B ")
                .pattern("AAA")
                .define('A', ModBlocks.MED_SECURITY_FENCE_WIRE.get())
                .define('B', Items.REDSTONE)
                .unlockedBy(getHasName(ModBlocks.MED_SECURITY_FENCE_WIRE.get()), has(ModBlocks.MED_SECURITY_FENCE_WIRE.get()))
                .save(output);

        shaped(ModBlocks.HIGH_SECURITY_FENCE_POLE.get(),2)
                .pattern("AAA")
                .pattern("BBB")
                .pattern("BBB")
                .define('A', Items.REDSTONE)
                .define('B', Items.IRON_INGOT)
                .unlockedBy(getHasName(Items.REDSTONE), has(Items.REDSTONE))
                .save(output);

        shaped(ModBlocks.DNA_SYNTHESIZER.get())
                .pattern("AAB")
                .pattern("ACD")
                .pattern("AAC")
                .define('A', Items.IRON_INGOT)
                .define('B', ModItems.COMPUTER_SCREEN.get())
                .define('C', ModItems.ADVANCED_CIRCUIT.get())
                .define('D', ModItems.DISC_DRIVE.get())
                .unlockedBy(getHasName(ModItems.ADVANCED_CIRCUIT.get()), has(ModItems.ADVANCED_CIRCUIT.get()))
                .save(output);

        shaped(ModBlocks.DNA_COMBINER_HYBRIDIZER.get())
                .pattern("ABA")
                .pattern("CDC")
                .define('A', ModItems.COMPUTER_SCREEN.get())
                .define('B', ModItems.BASIC_CIRCUIT.get())
                .define('C', Items.IRON_INGOT)
                .define('D', ModItems.DISC_DRIVE.get())
                .unlockedBy(getHasName(ModItems.BASIC_CIRCUIT.get()), has(ModItems.BASIC_CIRCUIT.get()))
                .save(output);

        shaped(ModBlocks.INCUBATOR.get())
                .pattern("ABA")
                .pattern("CCC")
                .pattern("BDB")
                .define('A', Items.GLASS)
                .define('B', Items.IRON_INGOT)
                .define('C', Items.COMPARATOR)
                .define('D', ModItems.KEYBOARD.get())
                .unlockedBy(getHasName(ModItems.KEYBOARD.get()), has(ModItems.KEYBOARD.get()))
                .save(output);

        shaped(ModBlocks.EMBRYO_CALCIFICATION_MACHINE.get())
                .pattern("ABC")
                .pattern("DED")
                .pattern("DFD")
                .define('A', Items.IRON_INGOT)
                .define('B', Items.BOWL)
                .define('C', ModItems.KEYBOARD.get())
                .define('D', ModItems.IRON_ROD.get())
                .define('E', ModItems.BASIC_CIRCUIT.get())
                .define('F', Items.IRON_BLOCK)
                .unlockedBy(getHasName(ModItems.IRON_ROD.get()), has(ModItems.IRON_ROD.get()))
                .save(output);

        shaped(ModBlocks.CLEAR_GLASS.get(),8)
                .group(JurassicReborn.MODID + ":clear_glass")
                .pattern("AAA")
                .pattern("ABA")
                .pattern("AAA")
                .define('A', Blocks.GLASS)
                .define('B', Items.IRON_INGOT)
                .unlockedBy(getHasName(Blocks.GLASS), has(Blocks.GLASS))
                .save(output);
        shaped(ModBlocks.CLEAR_GLASS_PANE.get(),16)
                .group(JurassicReborn.MODID + ":clear_glass_pane")
                .pattern("AAA")
                .pattern("AAA")
                .define('A', ModBlocks.CLEAR_GLASS.get().asItem())
                .unlockedBy(getHasName(ModBlocks.CLEAR_GLASS.get().asItem()), has(ModBlocks.CLEAR_GLASS.get().asItem()))
                .save(output);
        shaped(ModItems.AMBER_CANE.get())
                .group(JurassicReborn.MODID + ":amber_cane")
                .pattern("A")
                .pattern("B")
                .pattern("B")
                .define('A', ModItems.MOSQUITO_AMBER.get())
                .define('B', Items.STICK)
                .unlockedBy(getHasName(ModItems.MOSQUITO_AMBER.get()), has(ModItems.MOSQUITO_AMBER.get()))
                .save(output);

        shaped(ModItems.AMBER_KEYCHAIN.get())
                .group(JurassicReborn.MODID + ":amber_keychain")
                .pattern("A")
                .pattern("B")
                .define('A', ModItems.MOSQUITO_AMBER.get())
                .define('B', Items.IRON_INGOT)
                .unlockedBy(getHasName(ModItems.MOSQUITO_AMBER.get()), has(ModItems.MOSQUITO_AMBER.get()))
                .save(output);
        shaped(ModBlocks.HOLOGRAM_BLOCK.get(),1)
                .pattern("AAA")
                .pattern("BBB")
                .pattern("CBC")
                .define('A', Items.GLASS_PANE)
                .define('B', Items.DIAMOND)
                .define('C', ModItems.ADVANCED_CIRCUIT.get())
                .unlockedBy(getHasName(ModItems.ADVANCED_CIRCUIT.get()), has(ModItems.ADVANCED_CIRCUIT.get()))
                .save(output);

        shaped(ModItems.MR_DNA_KEYCHAIN.get())
                .group(JurassicReborn.MODID + ":mr_dna_keychain")
                .pattern("A")
                .pattern("B")
                .define('A', ModItems.DNA_NUCLEOTIDES.get())
                .define('B', Items.IRON_INGOT)
                .unlockedBy(getHasName(ModItems.DNA_NUCLEOTIDES.get()), has(ModItems.DNA_NUCLEOTIDES.get()))
                .save(output);

        shapeless(ModItems.AJUGINUCULA_SMITHII_OIL.get())
                .group(JurassicReborn.MODID + ":ajuginucula_smithii_oil")
                .requires(ModItems.AJUGINUCULA_SMITHII_LEAVES.get(), 4)
                .requires(Items.GLASS_BOTTLE)
                .unlockedBy(getHasName(ModItems.AJUGINUCULA_SMITHII_LEAVES.get()), has(ModItems.AJUGINUCULA_SMITHII_LEAVES.get()))
                .save(output);

        shapeless(ModItems.OILED_POTATO_STRIPS.get())
                .group(JurassicReborn.MODID + ":oiled_potato_strips")
                .requires(ModItems.WILD_POTATO.get(), 4)
                .requires(ModItems.AJUGINUCULA_SMITHII_OIL.get())
                .unlockedBy(getHasName(ModItems.WILD_POTATO.get()), has(ModItems.WILD_POTATO.get()))
                .save(output);

        shapeless(ModItems.OILED_POTATO_STRIPS.get(), 4)
                .group(JurassicReborn.MODID + ":oiled_potato_strips")
                .requires(Items.POTATO, 2)
                .requires(ModItems.AJUGINUCULA_SMITHII_OIL.get())
                .unlockedBy(getHasName(Items.POTATO), has(Items.POTATO))
                .save(output, JurassicReborn.resource("oiled_potato_strips_alt"));

        shapeless(ModItems.CHILEAN_SEA_BASS.get())
                .group(JurassicReborn.MODID + ":chilean_sea_bass")
                .requires(Items.COOKED_COD)
                .requires(ModItems.WILD_ONION.get())
                .requires(Items.CARROT)
                .requires(ModItems.AJUGINUCULA_SMITHII_LEAVES.get())
                .unlockedBy(getHasName(Items.COOKED_COD), has(Items.COOKED_COD))
                .save(output);
        ItemLike[] dinoSteaks = ModItems.STEAKS.values().stream().map(DeferredHolder::get).toArray(ItemLike[]::new);
        ItemLike[] meats = Stream.concat(
                        Stream.of(Items.COOKED_BEEF, Items.COOKED_CHICKEN, Items.COOKED_COD,
                                Items.COOKED_MUTTON, Items.COOKED_PORKCHOP, Items.COOKED_RABBIT),
                        Arrays.stream(dinoSteaks))
                .toArray(ItemLike[]::new);
        shapeless(ModItems.GROWTH_SERUM.get())
                .group(JurassicReborn.MODID + ":growth_serum")
                .requires(Items.GOLDEN_CARROT)
                .requires(ModItems.EMPTY_SYRINGE.get())
                .requires(Items.WATER_BUCKET)
                .requires(Ingredient.of(meats))
                .unlockedBy(getHasName(ModItems.EMPTY_SYRINGE.get()), has(ModItems.EMPTY_SYRINGE.get()))
                .save(output);

        for (Dinosaur dinosaur : Dinosaur.DINOS) {
            if (dinosaur != Dinosaur.EMPTY) {
                String s = "bones/" + dinosaur.getName().toLowerCase().replace(" ", "_") + "_skull";
                ItemLike fossil = ModBlocks.getEncasedBlockFor(dinosaur);

                if (fossil != null) {
                    ResourceLocation skullId = JurassicReborn.resource(s);

                    Item bone = BuiltInRegistries.ITEM.get(skullId);
                    if (bone == Items.AIR) {
                        JurassicReborn.getLogger().warn("Missing skull item {} for {}", skullId, dinosaur.getName());
                        continue;
                    }

                    ResourceLocation fossilItemId = Objects.requireNonNull(
                            BuiltInRegistries.ITEM.getKey(fossil.asItem()),
                            "Unregistered item for fossil block: " + fossil
                    );

                    CleaningRecipeBuilder.cleaning(fossil, bone)
                            .save(output, JurassicReborn.resource(fossilItemId.getPath() + "_cleaning"));
                } else {
                    JurassicReborn.getLogger().warn("No encased fossil block for {}", dinosaur.getName());
                }
            }
        }
    }

    private static RecipeCategory defaultCategory(ItemLike itemLike) {
        Item item = itemLike.asItem();
        if (item instanceof BlockItem) {
            return RecipeCategory.BUILDING_BLOCKS;
        }
        if (item.components().has(DataComponents.FOOD)) {
            return RecipeCategory.FOOD;
        }
        return RecipeCategory.MISC;
    }

    private static ShapedRecipeBuilder shaped(ItemLike result) {
        return ShapedRecipeBuilder.shaped(defaultCategory(result), result);
    }

    private static ShapedRecipeBuilder shaped(ItemLike result, int count) {
        return ShapedRecipeBuilder.shaped(defaultCategory(result), result, count);
    }

    private static ShapelessRecipeBuilder shapeless(ItemLike result) {
        return ShapelessRecipeBuilder.shapeless(defaultCategory(result), result);
    }

    private static ShapelessRecipeBuilder shapeless(ItemLike result, int count) {
        return ShapelessRecipeBuilder.shapeless(defaultCategory(result), result, count);
    }

    private static SimpleCookingRecipeBuilder smelting(Ingredient ingredient, ItemLike result, float experience, int cookingTime) {
        return SimpleCookingRecipeBuilder.smelting(ingredient, defaultCategory(result), result, experience, cookingTime);
    }

    private static SimpleCookingRecipeBuilder blasting(Ingredient ingredient, ItemLike result, float experience, int cookingTime) {
        return SimpleCookingRecipeBuilder.blasting(ingredient, defaultCategory(result), result, experience, cookingTime);
    }

    private static SimpleCookingRecipeBuilder smoking(Ingredient ingredient, ItemLike result, float experience, int cookingTime) {
        return SimpleCookingRecipeBuilder.smoking(ingredient, defaultCategory(result), result, experience, cookingTime);
    }

    private static SimpleCookingRecipeBuilder campfireCooking(Ingredient ingredient, ItemLike result, float experience, int cookingTime) {
        return SimpleCookingRecipeBuilder.campfireCooking(ingredient, defaultCategory(result), result, experience, cookingTime);
    }

    private static ItemLike item(String path) {
        Item result = BuiltInRegistries.ITEM.get(JurassicReborn.location(path));
        if (result == Items.AIR) {
            throw new IllegalStateException("Missing item: " + path);
        }
        return result;
    }

    private record BoatRecipeData(String woodName, ItemLike planks) {

        ItemLike planksItem() {
            return planks;
        }

        String boatId() {
            return woodName + "_boat";
        }

        String chestBoatId() {
            return woodName + "_chest_boat";
        }
    }

    private void hangingSignRecipe(
            RecipeOutput output,
            ItemLike hangingSign,
            ItemLike strippedLog,
            ItemLike strippedWood
    ) {
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, hangingSign, 6)
                .group(JurassicReborn.MODID + ":hanging_sign")
                .pattern("SSS")
                .pattern("C C")
                .pattern("SSS")
                .define('S', Ingredient.of(strippedLog, strippedWood))
                .define('C', Items.CHAIN)
                .unlockedBy(getHasName(strippedLog), has(strippedLog))
                .unlockedBy(getHasName(Items.CHAIN), has(Items.CHAIN))
                .save(output);
    }


    private void baleRecipe(
            RecipeOutput output,
            ItemLike ingredient,
            ItemLike result,
            String group,
            int count
    ) {
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, result, count)
                .group(JurassicReborn.MODID + ":" + group)
                .pattern("AAA")
                .pattern("AAA")
                .pattern("AAA")
                .define('A', ingredient)
                .unlockedBy(getHasName(ingredient), has(ingredient))
                .save(output, JurassicReborn.resource(group + "_from_" + getItemName(ingredient)));
    }
}