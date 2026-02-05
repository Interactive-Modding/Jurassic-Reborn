package net.vit.jurassicreborn.common.datagen;

import net.minecraft.advancements.critereon.EnchantmentPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.BlockFamily;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.predicates.InvertedLootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.RegistryObject;
import net.vit.jurassicreborn.common.blocks.ModBlocks;
import net.vit.jurassicreborn.common.blocks.wood.WoodBlocks;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;
import net.vit.jurassicreborn.common.items.ModItems;
import net.vit.jurassicreborn.common.plants.Plant;
import net.vit.jurassicreborn.common.plants.PlantHandler;

import java.util.Set;

public class JRBlockLoot extends BlockLootSubProvider {

    private static final float[] NORMAL_LEAVES_SAPLING_CHANCES = new float[]{0.05F, 0.0625F, 0.083333336F, 0.1F};
    private static final float[] JUNGLE_LEAVES_SAPLING_CHANGES = new float[]{0.025F, 0.027777778F, 0.03125F, 0.041666668F, 0.1F};
    private static final float[] NORMAL_LEAVES_STICK_CHANCES = new float[]{0.02F, 0.022222223F, 0.025F, 0.033333335F, 0.1F};

    // 1.19.2: BlockLootSubProvider needs (Set<Item> explosionResistant, FeatureFlagSet)
    public JRBlockLoot() {
        super(Set.<Item>of(), FeatureFlags.VANILLA_SET);
    }

    private static LootTable.Builder simpleSingleItem(ItemLike item) {
        return LootTable.lootTable().withPool(
                LootPool.lootPool()
                        .add(LootItem.lootTableItem(item))
                        .when(ExplosionCondition.survivesExplosion())
        );
    }

    // 1.19.2: override generate(), not addTables()
    @Override
    protected void generate() {
        dropSelf(ModBlocks.ARAUCARIA_SAPLING.get());
        dropSelf(ModBlocks.GINKGO_SAPLING.get());
        dropSelf(ModBlocks.CALAMITES_SAPLING.get());
        dropSelf(ModBlocks.PHOENIX_SAPLING.get());
        dropSelf(ModBlocks.PSARONIUS_SAPLING.get());
        dropSelf(ModBlocks.MAGNOLIA_SAPLING.get());
        dropSelf(ModBlocks.WEST_INDIAN_LILAC.get());
        add(ModBlocks.POTTED_ARAUCARIA_SAPLING.get(), block -> createPotFlowerItemTable(ModBlocks.ARAUCARIA_SAPLING.get()));
        add(ModBlocks.POTTED_GINKGO_SAPLING.get(), block -> createPotFlowerItemTable(ModBlocks.GINKGO_SAPLING.get()));
        add(ModBlocks.POTTED_CALAMITES_SAPLING.get(), block -> createPotFlowerItemTable(ModBlocks.CALAMITES_SAPLING.get()));
        add(ModBlocks.POTTED_PHOENIX_SAPLING.get(), block -> createPotFlowerItemTable(ModBlocks.PHOENIX_SAPLING.get()));
        add(ModBlocks.POTTED_PSARONIUS_SAPLING.get(), block -> createPotFlowerItemTable(ModBlocks.PSARONIUS_SAPLING.get()));
        add(ModBlocks.POTTED_MAGNOLIA_SAPLING.get(), block -> createPotFlowerItemTable(ModBlocks.MAGNOLIA_SAPLING.get()));
        PlantHandler.getPlants().stream()
                .filter(Plant::shouldRegister)
                .map(Plant::getBlock)
                .filter(block -> block != Blocks.AIR)
                .forEach(block -> add(block, simpleSingleItem(block)));

        dropSelf(ModBlocks.BUG_CRATE.get());
        dropSelf(ModBlocks.HOLOGRAM_BLOCK.get());
        dropSelf(ModBlocks.PARK_BENCH.get());
        dropSelf(ModBlocks.TRASH_CAN.get());
        dropSelf(ModBlocks.AMBER_BLOCK.get());
        dropSelf(ModBlocks.SKELETON_ASSEMBLY.get());
        dropSelf(ModBlocks.CLEANING_STATION.get());
        dropSelf(ModBlocks.CLEAR_GLASS.get());
        dropSelf(ModBlocks.CLEAR_GLASS_PANE.get());
        dropSelf(ModBlocks.AMBER_MOSQUITO.get());
        dropSelf(ModBlocks.AMBER_APHID.get());
        dropSelf(ModBlocks.SEA_LAMPREY.get());
        dropSelf(ModBlocks.FROZEN_LEECH.get());

        add(ModBlocks.CULTIVATE_BOTTOM.get(), noDrop());
        add(ModBlocks.CULTIVATE_TOP.get(), noDrop());

        for (Dinosaur dino : Dinosaur.DINOS) {
            Block encasedBlock = ModBlocks.getEncasedBlockFor(dino);
            if (encasedBlock != null) dropSelf(encasedBlock);

            Block fossilBlock = ModBlocks.getFossilBlockFor(dino);
            if (fossilBlock != null) dropSelf(fossilBlock);
        }

        this.add(ModBlocks.WILD_POTATO_PLANT.get(), block -> createCropDrops(
                block,
                ModItems.WILD_POTATO.get(),
                ModItems.WILD_POTATO_SEEDS.get(),
                LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                        .setProperties(StatePropertiesPredicate.Builder.properties()
                                .hasProperty(BlockStateProperties.AGE_5, 5)))
        );

        this.add(ModBlocks.AJUGINUCULA_SMITHII.get(), block -> createCropDrops(
                block,
                ModItems.AJUGINUCULA_SMITHII_LEAVES.get(),
                ModItems.AJUGINUCULA_SMITHII_SEEDS.get(),
                LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                        .setProperties(StatePropertiesPredicate.Builder.properties()
                                .hasProperty(BlockStateProperties.AGE_7, 7)))
        );

        this.add(ModBlocks.RHAMNUS_SALICIFOLIUS.get(), block -> createCropDrops(
                block,
                ModItems.RHAMNUS_BERRIES.get(),
                ModItems.RHAMNUS_SEEDS.get(),
                LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                        .setProperties(StatePropertiesPredicate.Builder.properties()
                                .hasProperty(BlockStateProperties.AGE_7, 7)))
        );

        this.add(ModBlocks.WILD_ONION.get(), block -> createCropDrops(
                block,
                ModItems.WILD_ONION.get(),
                ModItems.WILD_ONION.get(),
                LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                        .setProperties(StatePropertiesPredicate.Builder.properties()
                                .hasProperty(BlockStateProperties.AGE_5, 5)))
        );

        dropSelf(ModBlocks.DEAD_AULOPORA.get());
        dropSelf(ModBlocks.DEAD_CLADOCHONUS.get());
        dropSelf(ModBlocks.DEAD_ENALLHELIA.get());
        dropSelf(ModBlocks.DEAD_HIPPURITES_RADIOSUS.get());
        dropSelf(ModBlocks.DEAD_LITHOSTROTION.get());
        dropSelf(ModBlocks.DEAD_STYLOPHYLLOPSIS.get());
        add(ModBlocks.DISPLAY_BLOCK.get(), noDrop());
        dropSelf(ModBlocks.DNA_COMBINER_HYBRIDIZER.get());
        dropSelf(ModBlocks.DNA_EXTRACTOR.get());
        dropSelf(ModBlocks.DNA_SEQUENCER.get());
        dropSelf(ModBlocks.DNA_SYNTHESIZER.get());

        dropSelf(ModBlocks.EMBRYO_CALCIFICATION_MACHINE.get());
        dropSelf(ModBlocks.EMBRYONIC_MACHINE.get());

        dropSelf(ModBlocks.ENCASED_FAUNA_FOSSIL.get());
        add(ModBlocks.FLORA_FOSSIL.get(), createMultiItemTable(
                ModItems.PLANT_FOSSIL.get(), ModItems.PLANT_FOSSIL_0.get(),
                ModItems.PLANT_FOSSIL_1.get(), ModItems.PLANT_FOSSIL_2.get(),
                ModItems.PLANT_FOSSIL_3.get(), ModItems.TWIG_FOSSIL.get()));
        add(ModBlocks.DEEPSLATE_FLORA_FOSSIL.get(), createMultiItemTable(
                ModItems.PLANT_FOSSIL.get(), ModItems.PLANT_FOSSIL_0.get(),
                ModItems.PLANT_FOSSIL_1.get(), ModItems.PLANT_FOSSIL_2.get(),
                ModItems.PLANT_FOSSIL_3.get()));
        dropSelf(ModBlocks.FAUNA_FOSSIL.get());
        dropSelf(ModBlocks.FEEDER.get());
        dropSelf(ModBlocks.FOSSIL_GRINDER.get());

        dropSelf(ModBlocks.FOSSILIZED_TRACKWAY_BIPED_MEDIUM.get());
        dropSelf(ModBlocks.FOSSILIZED_TRACKWAY_BIPED_SMALL.get());
        dropSelf(ModBlocks.FOSSILIZED_TRACKWAY_RAPTOR.get());

        dropSelf(ModBlocks.GRACILARIA.get());

        dropSelf(ModBlocks.GYPSUM_BRICKS.get());
        dropSelf(ModBlocks.GYPSUM_COBBLESTONE.get());
        dropSelf(ModBlocks.GYPSUM_COBBLESTONE_PATHWAY.get());
        dropSelf(ModBlocks.GYPSUM_MIXED_PATH.get());
        dropSelf(ModBlocks.GYPSUM_PATHWAY.get());
        this.add(ModBlocks.GYPSUM_STONE.get(), block -> LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(applyExplosionDecay(block,
                                LootItem.lootTableItem(ModItems.GYPSUM_POWDER.get())
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 8.0F)))
                                        .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))))));
        dropSelf(ModBlocks.GYPSUM_STONE_PANEL.get());
        dropSelf(ModBlocks.GYPSUM_TILES.get());

        dropSelf(ModBlocks.HIGH_SECURITY_FENCE_BASE.get());
        dropSelf(ModBlocks.HIGH_SECURITY_FENCE_POLE.get());
        dropSelf(ModBlocks.HIGH_SECURITY_FENCE_WIRE.get());

        dropSelf(ModBlocks.INCUBATOR.get());

        dropOther(ModBlocks.KRILL_SWARM.get(), ModItems.KRILL.get());

        dropSelf(ModBlocks.LOW_SECURITY_FENCE_BASE.get());
        dropSelf(ModBlocks.LOW_SECURITY_FENCE_POLE.get());
        dropSelf(ModBlocks.LOW_SECURITY_FENCE_WIRE.get());

        dropSelf(ModBlocks.MED_SECURITY_FENCE_BASE.get());
        dropSelf(ModBlocks.MED_SECURITY_FENCE_POLE.get());
        dropSelf(ModBlocks.MED_SECURITY_FENCE_WIRE.get());

        dropSelf(ModBlocks.PALEO_BALE_CYCAD.get());
        dropSelf(ModBlocks.PALEO_BALE_CYCADEOIDEA.get());
        dropSelf(ModBlocks.PALEO_BALE_FERN.get());
        dropSelf(ModBlocks.PALEO_BALE_LEAVES.get());
        dropSelf(ModBlocks.PALEO_BALE_OTHER.get());
        dropSelf(ModBlocks.PEAT.get());
        dropSelf(ModBlocks.PEAT_MOSS.get());
        dropOther(ModBlocks.PLANKTON_SWARM.get(), ModItems.PLANKTON.get());

        // update method refs: BlockLootSubProvider:: (not BlockLoot::)
        add(ModBlocks.REINFORCED_DOOR.get(), block -> createDoorTable(block));
        dropSelf(ModBlocks.REINFORCED_BRICKS.get());
        dropSelf(ModBlocks.REINFORCED_STONE_PATHWAY.get());
        dropSelf(ModBlocks.REINFORCED_STONE.get());
        dropSelf(ModBlocks.REINFORCED_STONE_PANEL.get());
        dropSelf(ModBlocks.REINFORCED_STONE_TILES.get());

        dropSelf(ModBlocks.REFINED_GYPSUM_PANEL.get());
        add(ModBlocks.SECURITY_DOOR.get(), block -> createDoorTable(block));

        dropSelf(ModBlocks.TOUR_RAIL.get());
        dropSelf(ModBlocks.TOUR_RAIL_SLOW.get());
        dropSelf(ModBlocks.TOUR_RAIL_MEDIUM.get());
        dropSelf(ModBlocks.TOUR_RAIL_FAST.get());

        dropSelf(WoodBlocks.ARAUCARIA_LOG.get());
        dropSelf(WoodBlocks.CALAMITES_LOG.get());
        dropSelf(WoodBlocks.GINKGO_LOG.get());
        dropSelf(WoodBlocks.MAGNOLIA_LOG.get());
        dropSelf(WoodBlocks.PHOENIX_LOG.get());
        dropSelf(WoodBlocks.PSARONIUS_LOG.get());

        dropSelf(WoodBlocks.STRIPPED_ARAUCARIA_LOG.get());
        dropSelf(WoodBlocks.STRIPPED_CALAMITES_LOG.get());
        dropSelf(WoodBlocks.STRIPPED_GINKGO_LOG.get());
        dropSelf(WoodBlocks.STRIPPED_MAGNOLIA_LOG.get());
        dropSelf(WoodBlocks.STRIPPED_PHOENIX_LOG.get());
        dropSelf(WoodBlocks.STRIPPED_PSARONIUS_LOG.get());

        dropSelf(WoodBlocks.ARAUCARIA_WOOD.get());
        dropSelf(WoodBlocks.CALAMITES_WOOD.get());
        dropSelf(WoodBlocks.GINKGO_WOOD.get());
        dropSelf(WoodBlocks.MAGNOLIA_WOOD.get());
        dropSelf(WoodBlocks.PHOENIX_WOOD.get());
        dropSelf(WoodBlocks.PSARONIUS_WOOD.get());

        dropSelf(WoodBlocks.STRIPPED_ARAUCARIA_WOOD.get());
        dropSelf(WoodBlocks.STRIPPED_CALAMITES_WOOD.get());
        dropSelf(WoodBlocks.STRIPPED_GINKGO_WOOD.get());
        dropSelf(WoodBlocks.STRIPPED_MAGNOLIA_WOOD.get());
        dropSelf(WoodBlocks.STRIPPED_PHOENIX_WOOD.get());
        dropSelf(WoodBlocks.STRIPPED_PSARONIUS_WOOD.get());

        this.add(WoodBlocks.ARAUCARIA_LEAVES.get(), block -> createLeavesDrops(block, ModBlocks.ARAUCARIA_SAPLING.get(), NORMAL_LEAVES_SAPLING_CHANCES));
        this.add(WoodBlocks.CALAMITES_LEAVES.get(), block -> createLeavesDrops(block, ModBlocks.CALAMITES_SAPLING.get(), NORMAL_LEAVES_SAPLING_CHANCES));
        this.add(WoodBlocks.GINKGO_LEAVES.get(), block -> createLeavesDrops(block, ModBlocks.GINKGO_SAPLING.get(), NORMAL_LEAVES_SAPLING_CHANCES));
        this.add(WoodBlocks.MAGNOLIA_LEAVES.get(), block -> createLeavesDrops(block, ModBlocks.MAGNOLIA_SAPLING.get(), NORMAL_LEAVES_SAPLING_CHANCES));
        this.add(WoodBlocks.PHOENIX_LEAVES.get(), block -> createPhoenixLeavesDrops(block, ModBlocks.PHOENIX_SAPLING.get(), NORMAL_LEAVES_SAPLING_CHANCES));
        this.add(WoodBlocks.PSARONIUS_LEAVES.get(), block -> createLeavesDrops(block, ModBlocks.PSARONIUS_SAPLING.get(), NORMAL_LEAVES_SAPLING_CHANCES));

        ModBlockFamilies.getAllFamilies().forEach(family -> {
            dropSelf(family.getBaseBlock());
            Block button = family.get(BlockFamily.Variant.BUTTON);
            if (button != null) dropSelf(button);

            Block fence = family.get(BlockFamily.Variant.FENCE);
            if (fence != null) dropSelf(fence);

            Block gate = family.get(BlockFamily.Variant.FENCE_GATE);
            if (gate != null) dropSelf(gate);

            Block wall = family.get(BlockFamily.Variant.WALL);
            if (wall != null) dropSelf(wall);

            Block plate = family.get(BlockFamily.Variant.PRESSURE_PLATE);
            if (plate != null) dropSelf(plate);

            Block slab = family.get(BlockFamily.Variant.SLAB);
            if (slab != null) add(slab, block1 -> createSlabItemTable(block1));

            Block stairs = family.get(BlockFamily.Variant.STAIRS);
            if (stairs != null) dropSelf(stairs);

            Block sign = family.get(BlockFamily.Variant.SIGN);
            if (sign != null) dropSelf(sign);

            Block hangingSign = family.getHangingSign();
            if (hangingSign != null) dropSelf(hangingSign);

            Block door = family.get(BlockFamily.Variant.DOOR);
            if (door != null) add(door, block1 -> createDoorTable(block1));

            Block trapdoor = family.get(BlockFamily.Variant.TRAPDOOR);
            if (trapdoor != null) dropSelf(trapdoor);
        });

        dropSelf(WoodBlocks.PETRIFIED_ARAUCARIA_LOG.get());
        dropSelf(WoodBlocks.PETRIFIED_CALAMITES_LOG.get());
        dropSelf(WoodBlocks.PETRIFIED_GINKGO_LOG.get());
        dropSelf(WoodBlocks.PETRIFIED_MAGNOLIA_LOG.get());
        dropSelf(WoodBlocks.PETRIFIED_PHOENIX_LOG.get());
        dropSelf(WoodBlocks.PETRIFIED_PSARONIUS_LOG.get());
    }

    private LootTable.Builder createPhoenixLeavesDrops(Block leavesBlock, Block sapling, float... saplingChances) {
        return createLeavesDrops(leavesBlock, sapling, saplingChances)
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .when(InvertedLootItemCondition.invert(
                                MatchTool.toolMatches(ItemPredicate.Builder.item()
                                        .of(Tags.Items.SHEARS))))
                        .when(InvertedLootItemCondition.invert(
                                MatchTool.toolMatches(ItemPredicate.Builder.item()
                                        .hasEnchantment(new EnchantmentPredicate(Enchantments.SILK_TOUCH, MinMaxBounds.Ints.atLeast(1))))))
                        .add(applyExplosionDecay(leavesBlock,
                                LootItem.lootTableItem(ModItems.PHOENIX_FRUIT.get())
                                        .when(LootItemRandomChanceCondition.randomChance(0.10F)))));
    }

    protected static LootTable.Builder createMultiItemTable(ItemLike... items) {
        LootPool.Builder builder = LootPool.lootPool();
        for (ItemLike itemLike : items) {
            builder.add(LootItem.lootTableItem(itemLike));
        }
        return LootTable.lootTable().withPool(builder.when(ExplosionCondition.survivesExplosion()));
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        Set<RegistryObject<? extends Block>> exclude = Set.of(
                ModBlocks.AMBER_ORE, ModBlocks.DEEPSLATE_AMBER_ORE,
                ModBlocks.DEEPSLATE_ICE_SHARD_ORE, ModBlocks.ICE_SHARD_ORE,
                ModBlocks.NEST_FOSSIL
        );
        return ModBlocks.MOD_BLOCKS.getEntries().stream()
                .filter(ro -> !exclude.contains(ro))
                .map(RegistryObject::get)
                .toList();
    }
}
