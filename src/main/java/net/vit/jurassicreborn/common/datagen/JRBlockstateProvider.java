package net.vit.jurassicreborn.common.datagen;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.BlockFamily;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.level.block.*;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.common.blocks.ModBlocks;
import net.vit.jurassicreborn.common.blocks.wood.WoodBlocks;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;

import static net.minecraftforge.client.model.generators.ModelProvider.BLOCK_FOLDER;

public class JRBlockstateProvider extends BlockStateProvider {
    public JRBlockstateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, JurassicReborn.MODID, exFileHelper);
    }

    protected static final ExistingFileHelper.ResourceType TEXTURE = new ExistingFileHelper.ResourceType(PackType.CLIENT_RESOURCES, ".png", "textures");


    @Override
    protected void registerStatesAndModels() {

        simpleBlock(ModBlocks.AMBER_ORE.get());
        simpleBlock(ModBlocks.AMBER_BLOCK.get());



        ModBlockFamilies.getAllFamilies().forEach(family -> {
            Block baseBlock = family.getBaseBlock();
            simpleBlock(baseBlock);
            ResourceLocation location = BuiltInRegistries.BLOCK.getKey(baseBlock);
            ResourceLocation baseTexture = modLoc("block/" + location.getPath());

            Block sign = family.get(BlockFamily.Variant.SIGN);
            Block wallSign = family.get(BlockFamily.Variant.WALL_SIGN);
            if (sign != null && wallSign != null) {
                signBlock((StandingSignBlock) sign, (WallSignBlock) wallSign, baseTexture);
            }

            TrapDoorBlock trapdoor = (TrapDoorBlock) family.get(BlockFamily.Variant.TRAPDOOR);
            if (trapdoor != null) {
                ResourceLocation trapLoc = BuiltInRegistries.BLOCK.getKey(trapdoor);
                trapdoorBlock(trapdoor, modLoc("block/" + trapLoc.getPath()), false);
            }

            DoorBlock door = (DoorBlock) family.get(BlockFamily.Variant.DOOR);
            if (door != null) {
                simpleDoorBlock(door);
            }

            StairBlock stairBlock = (StairBlock) family.get(BlockFamily.Variant.STAIRS);
            if (stairBlock != null) {
                stairsBlock(stairBlock, baseTexture);
            }

            SlabBlock slabBlock = (SlabBlock) family.get(BlockFamily.Variant.SLAB);
            if (slabBlock != null) {
                slabBlock(slabBlock, baseTexture, baseTexture);
            }

            ButtonBlock button = (ButtonBlock) family.get(BlockFamily.Variant.BUTTON);
            if (button != null) {
                buttonBlock(button, baseTexture);
            }

            PressurePlateBlock plate = (PressurePlateBlock) family.get(BlockFamily.Variant.PRESSURE_PLATE);
            if (plate != null) {
                pressurePlateBlock(plate, baseTexture);
            }

            FenceBlock fence = (FenceBlock) family.get(BlockFamily.Variant.FENCE);
            if (fence != null) {
                fenceBlock(fence, baseTexture);
            }

            FenceGateBlock gate = (FenceGateBlock) family.get(BlockFamily.Variant.FENCE_GATE);
            if (gate != null) {
                fenceGateBlock(gate, baseTexture);
            }

            WallBlock wall = (WallBlock) family.get(BlockFamily.Variant.WALL);
            if (wall != null) {
                wallBlock(wall, baseTexture);
            }
        });

        logBlock(WoodBlocks.ARAUCARIA_LOG.get());
        logBlock(WoodBlocks.CALAMITES_LOG.get());
        logBlock(WoodBlocks.GINKGO_LOG.get());
        logBlock(WoodBlocks.MAGNOLIA_LOG.get());
        logBlock(WoodBlocks.PHOENIX_LOG.get());
        logBlock(WoodBlocks.PSARONIUS_LOG.get());

        woodBlock(WoodBlocks.ARAUCARIA_WOOD.get(),"araucaria");
        woodBlock(WoodBlocks.CALAMITES_WOOD.get(),"calamites");
        woodBlock(WoodBlocks.GINKGO_WOOD.get(),"ginkgo");
        woodBlock(WoodBlocks.MAGNOLIA_WOOD.get(),"magnolia");
        woodBlock(WoodBlocks.PHOENIX_WOOD.get(),"phoenix");
        woodBlock(WoodBlocks.PSARONIUS_WOOD.get(),"psaronius");

        logBlock(WoodBlocks.PETRIFIED_ARAUCARIA_LOG.get());
        logBlock(WoodBlocks.PETRIFIED_CALAMITES_LOG.get());
        logBlock(WoodBlocks.PETRIFIED_GINKGO_LOG.get());
        logBlock(WoodBlocks.PETRIFIED_MAGNOLIA_LOG.get());
        logBlock(WoodBlocks.PETRIFIED_PHOENIX_LOG.get());
        logBlock(WoodBlocks.PETRIFIED_PSARONIUS_LOG.get());

        logBlock(WoodBlocks.STRIPPED_ARAUCARIA_LOG.get());
        logBlock(WoodBlocks.STRIPPED_CALAMITES_LOG.get());
        logBlock(WoodBlocks.STRIPPED_GINKGO_LOG.get());
        logBlock(WoodBlocks.STRIPPED_MAGNOLIA_LOG.get());
        logBlock(WoodBlocks.STRIPPED_PHOENIX_LOG.get());
        logBlock(WoodBlocks.STRIPPED_PSARONIUS_LOG.get());

        woodBlock(WoodBlocks.STRIPPED_ARAUCARIA_WOOD.get(),"araucaria",true);
        woodBlock(WoodBlocks.STRIPPED_CALAMITES_WOOD.get(),"calamites",true);
        woodBlock(WoodBlocks.STRIPPED_GINKGO_WOOD.get(),"ginkgo",true);
        woodBlock(WoodBlocks.STRIPPED_MAGNOLIA_WOOD.get(),"magnolia",true);
        woodBlock(WoodBlocks.STRIPPED_PHOENIX_WOOD.get(),"phoenix",true);
        woodBlock(WoodBlocks.STRIPPED_PSARONIUS_WOOD.get(),"psaronius",true);

        sapling(ModBlocks.ARAUCARIA_SAPLING.get());
        sapling(ModBlocks.CALAMITES_SAPLING.get());
        sapling(ModBlocks.GINKGO_SAPLING.get());
        sapling(ModBlocks.MAGNOLIA_SAPLING.get());
        sapling(ModBlocks.PHOENIX_SAPLING.get());
        sapling(ModBlocks.PSARONIUS_SAPLING.get());

        ancientLeavesBlock(WoodBlocks.ARAUCARIA_LEAVES.get());
        ancientLeavesBlock(WoodBlocks.CALAMITES_LEAVES.get());
        ancientLeavesBlock(WoodBlocks.GINKGO_LEAVES.get());
        ancientLeavesBlock(WoodBlocks.MAGNOLIA_LEAVES.get());
        ancientLeavesBlock(WoodBlocks.PHOENIX_LEAVES.get());
        ancientLeavesBlock(WoodBlocks.PSARONIUS_LEAVES.get());


        for (Dinosaur dinosaur : Dinosaur.DINOS) {
            if (dinosaur!= Dinosaur.EMPTY) {
                Block fossil = ModBlocks.getEncasedBlockFor(dinosaur);
                if (fossil != null) {
                    ResourceLocation fossilName = BuiltInRegistries.BLOCK.getKey(fossil);

                 //   if (models().existingFileHelper.exists(modLoc("block/"+fossilName.getPath()), TEXTURE)) {
                 //       simpleBlock(fossil);
                 //   } else {
                        simpleBlock(fossil, models().cubeAll(fossilName.getPath(), modLoc("block/encased_fossil")));
                 //   }
                }
            }
        }

        simpleDoorBlock(ModBlocks.REINFORCED_DOOR.get());
        simpleDoorBlock(ModBlocks.SECURITY_DOOR.get());
    }

    public void simpleDoorBlock(DoorBlock door){
        ResourceLocation doorLoc = BuiltInRegistries.BLOCK.getKey(door);
        ResourceLocation top = modLoc("block/"+doorLoc.getPath()+"_top");
        ResourceLocation bottom = modLoc("block/"+doorLoc.getPath()+"_bottom");
        doorBlock(door,bottom,top);
    }

    public void ancientLeavesBlock(Block block) {
        String name = name(block);
        ResourceLocation texture = modLoc("block/"+name);

        ModelFile file = models().withExistingParent(name, BLOCK_FOLDER + "/leaves")
                .texture("all", texture);

        simpleBlock(block,file);
    }

    public void sapling(SaplingBlock block) {
        simpleBlock(block ,cross(block));
    }

    public ModelFile cross(Block block) {
        return models().cross(name(block), blockTexture(block));
    }

    public void woodBlock(RotatedPillarBlock block,String base) {
        woodBlock(block,base,false);
    }

    public void woodBlock(RotatedPillarBlock block,String base,boolean stripped) {
        ResourceLocation texture = modLoc("block/"+(stripped ? "stripped_": "")+base+"_log");
        ModelFile modelFile = models().cubeColumn(name(block), texture, texture);
        axisBlock(block, modelFile, modelFile);
    }

    protected String name(Block block) {
        return BuiltInRegistries.BLOCK.getKey(block).getPath();
    }
}
