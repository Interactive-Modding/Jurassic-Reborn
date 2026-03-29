package net.vit.jurassicreborn.common.datagen;

import com.google.common.collect.Maps;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.BlockFamily;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.block.Block;
import net.vit.jurassicreborn.common.blocks.ModBlocks;
import net.vit.jurassicreborn.common.blocks.wood.WoodBlocks;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.stream.Stream;

public class ModBlockFamilies {
    private static final Map<Block, ModBlockFamily> MAP = Maps.newHashMap();

    public static final ModBlockFamily MAGNOLIA_PLANKS = familyBuilder(WoodBlocks.MAGNOLIA_PLANKS.get())
            .button(WoodBlocks.MAGNOLIA_BUTTON.get())
            .fence(WoodBlocks.MAGNOLIA_FENCE.get())
            .fenceGate(WoodBlocks.MAGNOLIA_FENCE_GATE.get())
            .pressurePlate(WoodBlocks.MAGNOLIA_PRESSURE_PLATE.get())
            .sign(WoodBlocks.MAGNOLIA_SIGN.get(), WoodBlocks.MAGNOLIA_WALL_SIGN.get())
            .hangingSign(WoodBlocks.MAGNOLIA_HANGING_SIGN.get(), WoodBlocks.MAGNOLIA_WALL_HANGING_SIGN.get())
            .slab(WoodBlocks.MAGNOLIA_SLAB.get())
            .stairs(WoodBlocks.MAGNOLIA_STAIRS.get())
            .door(WoodBlocks.MAGNOLIA_DOOR.get())
            .trapdoor(WoodBlocks.MAGNOLIA_TRAPDOOR.get())
            .recipeGroupPrefix("wooden").recipeUnlockedBy("has_planks").getFamily();

    public static final ModBlockFamily GINKGO_PLANKS = familyBuilder(WoodBlocks.GINKGO_PLANKS.get())
            .button(WoodBlocks.GINKGO_BUTTON.get())
            .fence(WoodBlocks.GINKGO_FENCE.get())
            .fenceGate(WoodBlocks.GINKGO_FENCE_GATE.get())
            .pressurePlate(WoodBlocks.GINKGO_PRESSURE_PLATE.get())
            .sign(WoodBlocks.GINKGO_SIGN.get(), WoodBlocks.GINKGO_WALL_SIGN.get())
            .hangingSign(WoodBlocks.GINKGO_HANGING_SIGN.get(), WoodBlocks.GINKGO_WALL_HANGING_SIGN.get())
            .slab(WoodBlocks.GINKGO_SLAB.get())
            .stairs(WoodBlocks.GINKGO_STAIRS.get())
            .door(WoodBlocks.GINKGO_DOOR.get())
            .trapdoor(WoodBlocks.GINKGO_TRAPDOOR.get())
            .recipeGroupPrefix("wooden").recipeUnlockedBy("has_planks").getFamily();

    public static final ModBlockFamily CALAMITES_PLANKS = familyBuilder(WoodBlocks.CALAMITES_PLANKS.get())
            .button(WoodBlocks.CALAMITES_BUTTON.get())
            .fence(WoodBlocks.CALAMITES_FENCE.get())
            .fenceGate(WoodBlocks.CALAMITES_FENCE_GATE.get())
            .pressurePlate(WoodBlocks.CALAMITES_PRESSURE_PLATE.get())
            .sign(WoodBlocks.CALAMITES_SIGN.get(), WoodBlocks.CALAMITES_WALL_SIGN.get())
            .hangingSign(WoodBlocks.CALAMITES_HANGING_SIGN.get(), WoodBlocks.CALAMITES_WALL_HANGING_SIGN.get())
            .slab(WoodBlocks.CALAMITES_SLAB.get())
            .stairs(WoodBlocks.CALAMITES_STAIRS.get())
            .door(WoodBlocks.CALAMITES_DOOR.get())
            .trapdoor(WoodBlocks.CALAMITES_TRAPDOOR.get())
            .recipeGroupPrefix("wooden").recipeUnlockedBy("has_planks").getFamily();

    public static final ModBlockFamily PHOENIX_PLANKS = familyBuilder(WoodBlocks.PHOENIX_PLANKS.get())
            .button(WoodBlocks.PHOENIX_BUTTON.get())
            .fence(WoodBlocks.PHOENIX_FENCE.get())
            .fenceGate(WoodBlocks.PHOENIX_FENCE_GATE.get())
            .pressurePlate(WoodBlocks.PHOENIX_PRESSURE_PLATE.get())
            .sign(WoodBlocks.PHOENIX_SIGN.get(), WoodBlocks.PHOENIX_WALL_SIGN.get())
            .hangingSign(WoodBlocks.PHOENIX_HANGING_SIGN.get(), WoodBlocks.PHOENIX_WALL_HANGING_SIGN.get())
            .slab(WoodBlocks.PHOENIX_SLAB.get())
            .stairs(WoodBlocks.PHOENIX_STAIRS.get())
            .door(WoodBlocks.PHOENIX_DOOR.get())
            .trapdoor(WoodBlocks.PHOENIX_TRAPDOOR.get())
            .recipeGroupPrefix("wooden").recipeUnlockedBy("has_planks").getFamily();

    public static final ModBlockFamily PSARONIUS_PLANKS = familyBuilder(WoodBlocks.PSARONIUS_PLANKS.get())
            .button(WoodBlocks.PSARONIUS_BUTTON.get())
            .fence(WoodBlocks.PSARONIUS_FENCE.get())
            .fenceGate(WoodBlocks.PSARONIUS_FENCE_GATE.get())
            .pressurePlate(WoodBlocks.PSARONIUS_PRESSURE_PLATE.get())
            .sign(WoodBlocks.PSARONIUS_SIGN.get(), WoodBlocks.PSARONIUS_WALL_SIGN.get())
            .hangingSign(WoodBlocks.PSARONIUS_HANGING_SIGN.get(), WoodBlocks.PSARONIUS_WALL_HANGING_SIGN.get())
            .slab(WoodBlocks.PSARONIUS_SLAB.get())
            .stairs(WoodBlocks.PSARONIUS_STAIRS.get())
            .door(WoodBlocks.PSARONIUS_DOOR.get())
            .trapdoor(WoodBlocks.PSARONIUS_TRAPDOOR.get())
            .recipeGroupPrefix("wooden").recipeUnlockedBy("has_planks").getFamily();

    public static final ModBlockFamily ARAUCARIA_PLANKS = familyBuilder(WoodBlocks.ARAUCARIA_PLANKS.get())
            .button(WoodBlocks.ARAUCARIA_BUTTON.get())
            .fence(WoodBlocks.ARAUCARIA_FENCE.get())
            .fenceGate(WoodBlocks.ARAUCARIA_FENCE_GATE.get())
            .pressurePlate(WoodBlocks.ARAUCARIA_PRESSURE_PLATE.get())
            .sign(WoodBlocks.ARAUCARIA_SIGN.get(), WoodBlocks.ARAUCARIA_WALL_SIGN.get())
            .hangingSign(WoodBlocks.ARAUCARIA_HANGING_SIGN.get(), WoodBlocks.ARAUCARIA_WALL_HANGING_SIGN.get())
            .slab(WoodBlocks.ARAUCARIA_SLAB.get())
            .stairs(WoodBlocks.ARAUCARIA_STAIRS.get())
            .door(WoodBlocks.ARAUCARIA_DOOR.get())
            .trapdoor(WoodBlocks.ARAUCARIA_TRAPDOOR.get())
            .recipeGroupPrefix("wooden").recipeUnlockedBy("has_planks").getFamily();

    public static final ModBlockFamily GYPSUM_BRICKS = familyBuilder(ModBlocks.GYPSUM_BRICKS.get())
            .button(ModBlocks.GYPSUM_BRICK_BUTTON.get())
            .pressurePlate(ModBlocks.GYPSUM_BRICK_PRESSURE_PLATE.get())
            .slab(ModBlocks.GYPSUM_BRICK_SLAB.get())
            .stairs(ModBlocks.GYPSUM_BRICK_STAIRS.get())
            .wall(ModBlocks.GYPSUM_BRICK_WALL.get())
            .getFamily();

    public static final ModBlockFamily REINFORCED_BRICKS = familyBuilder(ModBlocks.REINFORCED_BRICKS.get())
            .button(ModBlocks.REINFORCED_BRICK_BUTTON.get())
            .pressurePlate(ModBlocks.REINFORCED_BRICK_PRESSURE_PLATE.get())
            .slab(ModBlocks.REINFORCED_BRICK_SLAB.get())
            .stairs(ModBlocks.REINFORCED_BRICK_STAIRS.get())
            .wall(ModBlocks.REINFORCED_BRICK_WALL.get())
            .getFamily();

    public static final ModBlockFamily GYPSUM_STONE = familyBuilder(ModBlocks.GYPSUM_STONE.get())
            .button(ModBlocks.GYPSUM_STONE_BUTTON.get())
            .pressurePlate(ModBlocks.GYPSUM_STONE_PRESSURE_PLATE.get())
            .slab(ModBlocks.GYPSUM_STONE_SLAB.get())
            .stairs(ModBlocks.GYPSUM_STONE_STAIRS.get())
            .wall(ModBlocks.GYPSUM_STONE_WALL.get())
            .getFamily();

    public static final ModBlockFamily GYPSUM_COBBLESTONE = familyBuilder(ModBlocks.GYPSUM_COBBLESTONE.get())
            .button(ModBlocks.GYPSUM_COBBLESTONE_BUTTON.get())
            .pressurePlate(ModBlocks.GYPSUM_COBBLESTONE_PRESSURE_PLATE.get())
            .slab(ModBlocks.GYPSUM_COBBLESTONE_SLAB.get())
            .stairs(ModBlocks.GYPSUM_COBBLESTONE_STAIRS.get())
            .wall(ModBlocks.GYPSUM_COBBLESTONE_WALL.get())
            .getFamily();

    public static final ModBlockFamily GYPSUM_COBBLESTONE_PATHWAY = familyBuilder(ModBlocks.GYPSUM_COBBLESTONE_PATHWAY.get())
            .button(ModBlocks.GYPSUM_COBBLESTONE_PATHWAY_BUTTON.get())
            .pressurePlate(ModBlocks.GYPSUM_COBBLESTONE_PATHWAY_PRESSURE_PLATE.get())
            .slab(ModBlocks.GYPSUM_COBBLESTONE_PATHWAY_SLAB.get())
            .stairs(ModBlocks.GYPSUM_COBBLESTONE_PATHWAY_STAIRS.get())
            .wall(ModBlocks.GYPSUM_COBBLESTONE_PATHWAY_WALL.get())
            .getFamily();

    public static final ModBlockFamily GYPSUM_PATHWAY = familyBuilder(ModBlocks.GYPSUM_PATHWAY.get())
            .button(ModBlocks.GYPSUM_PATHWAY_BUTTON.get())
            .pressurePlate(ModBlocks.GYPSUM_PATHWAY_PRESSURE_PLATE.get())
            .slab(ModBlocks.GYPSUM_PATHWAY_SLAB.get())
            .stairs(ModBlocks.GYPSUM_PATHWAY_STAIRS.get())
            .wall(ModBlocks.GYPSUM_PATHWAY_WALL.get())
            .getFamily();

    public static final ModBlockFamily GYPSUM_MIXED_PATH = familyBuilder(ModBlocks.GYPSUM_MIXED_PATH.get())
            .button(ModBlocks.GYPSUM_MIXED_PATH_BUTTON.get())
            .pressurePlate(ModBlocks.GYPSUM_MIXED_PATH_PRESSURE_PLATE.get())
            .slab(ModBlocks.GYPSUM_MIXED_PATH_SLAB.get())
            .stairs(ModBlocks.GYPSUM_MIXED_PATH_STAIRS.get())
            .wall(ModBlocks.GYPSUM_MIXED_PATH_WALL.get())
            .getFamily();

    public static final ModBlockFamily GYPSUM_TILES = familyBuilder(ModBlocks.GYPSUM_TILES.get())
            .button(ModBlocks.GYPSUM_TILES_BUTTON.get())
            .pressurePlate(ModBlocks.GYPSUM_TILES_PRESSURE_PLATE.get())
            .slab(ModBlocks.GYPSUM_TILES_SLAB.get())
            .stairs(ModBlocks.GYPSUM_TILES_STAIRS.get())
            .wall(ModBlocks.GYPSUM_TILES_WALL.get())
            .getFamily();

    public static final ModBlockFamily REFINED_GYPSUM_PANEL = familyBuilder(ModBlocks.REFINED_GYPSUM_PANEL.get())
            .button(ModBlocks.REFINED_GYPSUM_PANEL_BUTTON.get())
            .pressurePlate(ModBlocks.REFINED_GYPSUM_PANEL_PRESSURE_PLATE.get())
            .slab(ModBlocks.REFINED_GYPSUM_PANEL_SLAB.get())
            .stairs(ModBlocks.REFINED_GYPSUM_PANEL_STAIRS.get())
            .wall(ModBlocks.REFINED_GYPSUM_PANEL_WALL.get())
            .getFamily();

    public static final ModBlockFamily GYPSUM_STONE_PANEL = familyBuilder(ModBlocks.GYPSUM_STONE_PANEL.get())
            .button(ModBlocks.GYPSUM_STONE_PANEL_BUTTON.get())
            .pressurePlate(ModBlocks.GYPSUM_STONE_PANEL_PRESSURE_PLATE.get())
            .slab(ModBlocks.GYPSUM_STONE_PANEL_SLAB.get())
            .stairs(ModBlocks.GYPSUM_STONE_PANEL_STAIRS.get())
            .wall(ModBlocks.GYPSUM_STONE_PANEL_WALL.get())
            .getFamily();

    public static final ModBlockFamily REINFORCED_STONE = familyBuilder(ModBlocks.REINFORCED_STONE.get())
            .button(ModBlocks.REINFORCED_STONE_BUTTON.get())
            .pressurePlate(ModBlocks.REINFORCED_STONE_PRESSURE_PLATE.get())
            .slab(ModBlocks.REINFORCED_STONE_SLAB.get())
            .stairs(ModBlocks.REINFORCED_STONE_STAIRS.get())
            .wall(ModBlocks.REINFORCED_STONE_WALL.get())
            .getFamily();

    public static final ModBlockFamily REINFORCED_STONE_TILES = familyBuilder(ModBlocks.REINFORCED_STONE_TILES.get())
            .button(ModBlocks.REINFORCED_STONE_TILES_BUTTON.get())
            .pressurePlate(ModBlocks.REINFORCED_STONE_TILES_PRESSURE_PLATE.get())
            .slab(ModBlocks.REINFORCED_STONE_TILES_SLAB.get())
            .stairs(ModBlocks.REINFORCED_STONE_TILES_STAIRS.get())
            .wall(ModBlocks.REINFORCED_STONE_TILES_WALL.get())
            .getFamily();

    public static final ModBlockFamily REINFORCED_STONE_PATHWAY = familyBuilder(ModBlocks.REINFORCED_STONE_PATHWAY.get())
            .button(ModBlocks.REINFORCED_STONE_PATHWAY_BUTTON.get())
            .pressurePlate(ModBlocks.REINFORCED_STONE_PATHWAY_PRESSURE_PLATE.get())
            .slab(ModBlocks.REINFORCED_STONE_PATHWAY_SLAB.get())
            .stairs(ModBlocks.REINFORCED_STONE_PATHWAY_STAIRS.get())
            .wall(ModBlocks.REINFORCED_STONE_PATHWAY_WALL.get())
            .getFamily();

    public static final ModBlockFamily REINFORCED_STONE_PANEL = familyBuilder(ModBlocks.REINFORCED_STONE_PANEL.get())
            .button(ModBlocks.REINFORCED_STONE_PANEL_BUTTON.get())
            .pressurePlate(ModBlocks.REINFORCED_STONE_PANEL_PRESSURE_PLATE.get())
            .slab(ModBlocks.REINFORCED_STONE_PANEL_SLAB.get())
            .stairs(ModBlocks.REINFORCED_STONE_PANEL_STAIRS.get())
            .wall(ModBlocks.REINFORCED_STONE_PANEL_WALL.get())
            .getFamily();


    private static ModFamilyBuilder familyBuilder(Block pBaseBlock) {
        return new ModFamilyBuilder(pBaseBlock);
    }

    public static Stream<ModBlockFamily> getAllFamilies() {
        return MAP.values().stream();
    }

    public static final class ModBlockFamily {
        private final BlockFamily family;
        private final Block hangingSign;
        private final Block wallHangingSign;

        private ModBlockFamily(BlockFamily family, Block hangingSign, Block wallHangingSign) {
            this.family = family;
            this.hangingSign = hangingSign;
            this.wallHangingSign = wallHangingSign;
        }

        public BlockFamily getFamily() {
            return family;
        }

        public Block getBaseBlock() {
            return family.getBaseBlock();
        }

        public Block get(BlockFamily.Variant variant) {
            return family.get(variant);
        }

        public boolean shouldGenerateRecipe() {
            return family.shouldGenerateRecipe();
        }

        public @Nullable Block getHangingSign() {
            return hangingSign;
        }

        public @Nullable Block getWallHangingSign() {
            return wallHangingSign;
        }
    }

    private static final class ModFamilyBuilder {
        private final BlockFamily.Builder builder;
        private Block hangingSign;
        private Block wallHangingSign;

        private ModFamilyBuilder(Block baseBlock) {
            this.builder = new BlockFamily.Builder(baseBlock);
        }

        public ModFamilyBuilder button(Block button) {
            builder.button(button);
            return this;
        }

        public ModFamilyBuilder fence(Block fence) {
            builder.fence(fence);
            return this;
        }

        public ModFamilyBuilder fenceGate(Block fenceGate) {
            builder.fenceGate(fenceGate);
            return this;
        }

        public ModFamilyBuilder pressurePlate(Block pressurePlate) {
            builder.pressurePlate(pressurePlate);
            return this;
        }

        public ModFamilyBuilder sign(Block sign, Block wallSign) {
            builder.sign(sign, wallSign);
            return this;
        }

        public ModFamilyBuilder hangingSign(Block hangingSign, Block wallHangingSign) {
            this.hangingSign = hangingSign;
            this.wallHangingSign = wallHangingSign;
            return this;
        }

        public ModFamilyBuilder slab(Block slab) {
            builder.slab(slab);
            return this;
        }

        public ModFamilyBuilder stairs(Block stairs) {
            builder.stairs(stairs);
            return this;
        }

        public ModFamilyBuilder door(Block door) {
            builder.door(door);
            return this;
        }

        public ModFamilyBuilder trapdoor(Block trapdoor) {
            builder.trapdoor(trapdoor);
            return this;
        }

        public ModFamilyBuilder wall(Block wall) {
            builder.wall(wall);
            return this;
        }

        public ModFamilyBuilder recipeGroupPrefix(String prefix) {
            builder.recipeGroupPrefix(prefix);
            return this;
        }

        public ModFamilyBuilder recipeUnlockedBy(String unlock) {
            builder.recipeUnlockedBy(unlock);
            return this;
        }

        public ModBlockFamily getFamily() {
            ModBlockFamily family = new ModBlockFamily(builder.getFamily(), hangingSign, wallHangingSign);
            ModBlockFamily duplicate = MAP.put(family.getBaseBlock(), family);
            if (duplicate != null) {
                throw new IllegalStateException("Duplicate family definition for " + BuiltInRegistries.BLOCK.getKey(family.getBaseBlock()));
            }
            return family;
        }
    }
}
