package net.vit.jurassicreborn.common.datagen;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.BlockFamily;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.common.blocks.ModBlocks;
import net.vit.jurassicreborn.common.blocks.wood.WoodBlocks;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;
import net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurHandler;
import net.vit.jurassicreborn.common.items.ModItems;

public class JRItemModelProvider extends ItemModelProvider {
    public JRItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, JurassicReborn.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {

        simpleBlockItem(ModBlocks.AMBER_ORE.get().asItem());
        simpleBlockItem(ModBlocks.AMBER_BLOCK.get().asItem());

        DinosaurHandler.doDinosInit();
        for (Dinosaur dinosaur : Dinosaur.DINOSAUR_IDS.keySet()) {
            if (dinosaur == Dinosaur.EMPTY) {
                continue;
            }
            String modelName = "spawn_egg/" + ModItems.getSpawnEggModelName(dinosaur);
            withExistingParent(modelName, mcLoc("item/template_spawn_egg"));
        }

        ModBlockFamilies.getAllFamilies().forEach(family -> {
            Block baseBlock = family.getBaseBlock();
            String name = name(baseBlock.asItem());

            TrapDoorBlock trapDoor = (TrapDoorBlock) family.get(BlockFamily.Variant.TRAPDOOR);
            if (trapDoor != null) {
                ResourceLocation trapDoorName = BuiltInRegistries.ITEM.getKey(trapDoor.asItem());
                String modelName = "block/" + trapDoorName.getPath() + "_bottom";
                simpleBlockItem(trapDoor.asItem(), modLoc(modelName));
            }

            DoorBlock door = (DoorBlock) family.get(BlockFamily.Variant.DOOR);
            if (door != null) {
                generatedItem(door.asItem());
            }

            Block slab = family.get(BlockFamily.Variant.SLAB);
            if (slab != null) {
                simpleBlockItem(slab.asItem());
            }

            Block stairs = family.get(BlockFamily.Variant.STAIRS);
            if (stairs != null) {
                simpleBlockItem(stairs.asItem());
            }

            Block plate = family.get(BlockFamily.Variant.PRESSURE_PLATE);
            if (plate != null) {
                simpleBlockItem(plate.asItem());
            }

            ButtonBlock buttonBlock = (ButtonBlock) family.get(BlockFamily.Variant.BUTTON);
            if (buttonBlock != null) {
                String buttonName = name(buttonBlock.asItem());
                buttonInventory(buttonName, modLoc("block/" + name));
            }

            FenceBlock fenceBlock = (FenceBlock) family.get(BlockFamily.Variant.FENCE);
            if (fenceBlock != null) {
                fenceInventory(name(fenceBlock.asItem()), modLoc("block/" + name));
            }

            Block fenceGate = family.get(BlockFamily.Variant.FENCE_GATE);
            if (fenceGate != null) {
                simpleBlockItem(fenceGate.asItem());
            }

            Block wall = family.get(BlockFamily.Variant.WALL);
            if (wall != null) {
                wallInventory(name(wall.asItem()), modLoc("block/" + name));
            }
        });

        simpleBlockItem(WoodBlocks.PETRIFIED_ARAUCARIA_LOG.get().asItem());
        simpleBlockItem(WoodBlocks.PETRIFIED_CALAMITES_LOG.get().asItem());
        simpleBlockItem(WoodBlocks.PETRIFIED_GINKGO_LOG.get().asItem());
        simpleBlockItem(WoodBlocks.PETRIFIED_MAGNOLIA_LOG.get().asItem());
        simpleBlockItem(WoodBlocks.PETRIFIED_PHOENIX_LOG.get().asItem());
        simpleBlockItem(WoodBlocks.PETRIFIED_PSARONIUS_LOG.get().asItem());

        simpleBlockItem(WoodBlocks.PETRIFIED_ARAUCARIA_LOG.get().asItem());
        simpleBlockItem(WoodBlocks.PETRIFIED_CALAMITES_LOG.get().asItem());
        simpleBlockItem(WoodBlocks.PETRIFIED_GINKGO_LOG.get().asItem());
        simpleBlockItem(WoodBlocks.PETRIFIED_MAGNOLIA_LOG.get().asItem());
        simpleBlockItem(WoodBlocks.PETRIFIED_PHOENIX_LOG.get().asItem());
        simpleBlockItem(WoodBlocks.PETRIFIED_PSARONIUS_LOG.get().asItem());

        simpleBlockItem(WoodBlocks.ARAUCARIA_WOOD.get().asItem());
        simpleBlockItem(WoodBlocks.CALAMITES_WOOD.get().asItem());
        simpleBlockItem(WoodBlocks.GINKGO_WOOD.get().asItem());
        simpleBlockItem(WoodBlocks.MAGNOLIA_WOOD.get().asItem());
        simpleBlockItem(WoodBlocks.PHOENIX_WOOD.get().asItem());
        simpleBlockItem(WoodBlocks.PSARONIUS_WOOD.get().asItem());

        simpleBlockItem(WoodBlocks.STRIPPED_ARAUCARIA_LOG.get().asItem());
        simpleBlockItem(WoodBlocks.STRIPPED_CALAMITES_LOG.get().asItem());
        simpleBlockItem(WoodBlocks.STRIPPED_GINKGO_LOG.get().asItem());
        simpleBlockItem(WoodBlocks.STRIPPED_MAGNOLIA_LOG.get().asItem());
        simpleBlockItem(WoodBlocks.STRIPPED_PHOENIX_LOG.get().asItem());
        simpleBlockItem(WoodBlocks.STRIPPED_PSARONIUS_LOG.get().asItem());

        simpleBlockItem(WoodBlocks.STRIPPED_ARAUCARIA_WOOD.get().asItem());
        simpleBlockItem(WoodBlocks.STRIPPED_CALAMITES_WOOD.get().asItem());
        simpleBlockItem(WoodBlocks.STRIPPED_GINKGO_WOOD.get().asItem());
        simpleBlockItem(WoodBlocks.STRIPPED_MAGNOLIA_WOOD.get().asItem());
        simpleBlockItem(WoodBlocks.STRIPPED_PHOENIX_WOOD.get().asItem());
        simpleBlockItem(WoodBlocks.STRIPPED_PSARONIUS_WOOD.get().asItem());

        simpleBlockItem(WoodBlocks.ARAUCARIA_LOG.get().asItem());
        simpleBlockItem(WoodBlocks.CALAMITES_LOG.get().asItem());
        simpleBlockItem(WoodBlocks.GINKGO_LOG.get().asItem());
        simpleBlockItem(WoodBlocks.MAGNOLIA_LOG.get().asItem());
        simpleBlockItem(WoodBlocks.PHOENIX_LOG.get().asItem());
        simpleBlockItem(WoodBlocks.PSARONIUS_LOG.get().asItem());

        generatedItemBlockTexture(ModBlocks.ARAUCARIA_SAPLING.get().asItem());
        generatedItemBlockTexture(ModBlocks.CALAMITES_SAPLING.get().asItem());
        generatedItemBlockTexture(ModBlocks.GINKGO_SAPLING.get().asItem());
        generatedItemBlockTexture(ModBlocks.MAGNOLIA_SAPLING.get().asItem());
        generatedItemBlockTexture(ModBlocks.PHOENIX_SAPLING.get().asItem());
        generatedItemBlockTexture(ModBlocks.PSARONIUS_SAPLING.get().asItem());
        generatedItem(ModItems.BLACK_JEEP_WRANGLER.get());
        generatedItem(ModItems.BLUE_JEEP_WRANGLER.get());
        generatedItem(ModItems.PINK_JEEP_WRANGLER.get());
        generatedItem(ModItems.PURPLE_JEEP_WRANGLER.get());
        generatedItem(ModItems.SORNA_JEEP_WRANGLER.get());
        generatedItem(ModItems.LIME_JEEP_WRANGLER.get());
        generatedItem(ModItems.GREEN_JEEP_WRANGLER.get());
        generatedItem(ModItems.AMBER.get());


        boatItem(ModItems.ARAUCARIA_BOAT.get());
        boatItem(ModItems.ARAUCARIA_CHEST_BOAT.get());
        boatItem(ModItems.CALAMITES_BOAT.get());
        boatItem(ModItems.CALAMITES_CHEST_BOAT.get());
        boatItem(ModItems.GINKGO_BOAT.get());
        boatItem(ModItems.GINKGO_CHEST_BOAT.get());
        boatItem(ModItems.MAGNOLIA_BOAT.get());
        boatItem(ModItems.MAGNOLIA_CHEST_BOAT.get());
        boatItem(ModItems.PHOENIX_BOAT.get());
        boatItem(ModItems.PHOENIX_CHEST_BOAT.get());
        boatItem(ModItems.PSARONIUS_BOAT.get());
        boatItem(ModItems.PSARONIUS_CHEST_BOAT.get());

        generatedItem(ModBlocks.REINFORCED_DOOR.get().asItem());
        generatedItem(ModBlocks.SECURITY_DOOR.get().asItem());
    }

    protected void simpleBlockItem(Item item, ResourceLocation loc) {
        String s = BuiltInRegistries.ITEM.getKey(item).toString();
        getBuilder(s)
                .parent(getExistingFile(loc));
    }

    protected String name(Item item) {
        return BuiltInRegistries.ITEM.getKey(item).getPath();
    }

    protected void simpleBlockItem(Item item) {
        simpleBlockItem(item,modLoc("block/" + BuiltInRegistries.ITEM.getKey(item).getPath()));
    }


    private void generatedItem(Item item ,ResourceLocation texture) {
        String path = BuiltInRegistries.ITEM.getKey(item).getPath();
        singleTexture(path, mcLoc("item/generated"),
                "layer0", texture);
    }

    private void generatedItem(Item item) {
        generatedItem(item,modLoc("item/"+BuiltInRegistries.ITEM.getKey(item).getPath()));
    }

    private void generatedItemBlockTexture(Item item) {
        generatedItem(item,modLoc("block/"+BuiltInRegistries.ITEM.getKey(item).getPath()));
    }

    private void boatItem(Item item) {
        String path = BuiltInRegistries.ITEM.getKey(item).getPath();
        singleTexture(path, mcLoc("item/generated"), "layer0", modLoc("item/" + path));
    }

}
