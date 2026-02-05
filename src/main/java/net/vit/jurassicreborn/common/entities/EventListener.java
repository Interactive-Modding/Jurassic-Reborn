package net.vit.jurassicreborn.common.entities;

import net.vit.jurassicreborn.common.blocks.wood.WoodBlocks;
import net.vit.jurassicreborn.common.entities.animal.CrabEntity;
import net.vit.jurassicreborn.common.entities.animal.SharkEntity;
import net.vit.jurassicreborn.common.entities.animal.GoatEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.entity.MobCategory;

import java.util.HashMap;

import static net.vit.jurassicreborn.common.CommonRegistries.addBlocksToBlockEntity;
import static net.vit.jurassicreborn.common.CommonRegistries.addLogsToStrippables;

public class EventListener {

    public static void registerAttributes(final EntityAttributeCreationEvent event){//lord if this works I swear to god.
        HashMap<EntityType<?>, AttributeSupplier.Builder> typeMap = new HashMap<>();
        ModEntities.MOD_ENTITY_TYPES.getEntries().forEach((type) -> {//what the fuck is this abomination that I have created - gamma
            if(type.get().getBaseClass().isAssignableFrom(LivingEntity.class) &&
                    !(type.get() == ModEntities.CRAB.get() || type.get() == ModEntities.SHARK.get() || type.get() == ModEntities.GOAT.get())) {
                AttributeSupplier.Builder supplier = DinosaurEntity.createAttributes();
//                try {
//                    ;if(!(type.get() == CRAB_ENTITY_TYPE.get() || type.get() == SHARK_ENTITY_TYPE.get())) {
//                        event.put(type, DinosaurEntity.createAttributes().build());
//                    }
//                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
//                    throw new RuntimeException(e);
//                }
                event.put(((EntityType<? extends LivingEntity>) type.get()), supplier.build());
            }
        });
        event.put(ModEntities.CRAB.get(), CrabEntity.createAttributes().build());
        event.put(ModEntities.SHARK.get(), SharkEntity.createAttributes().build());
        event.put(ModEntities.GOAT.get(), GoatEntity.createAttributes().build());

    }

    public static void finalizeSetup(final FMLLoadCompleteEvent evt){
        //Make sure the signs are actually added to the sign block entity
        addBlocksToBlockEntity(BlockEntityType.SIGN, WoodBlocks.getSigns());

        //Make our logs able to be stripped
        HashMap<Block, Block> logMap = new HashMap<>();

        logMap.put(WoodBlocks.ARAUCARIA_LOG.get(),WoodBlocks.STRIPPED_ARAUCARIA_LOG.get());
        logMap.put(WoodBlocks.ARAUCARIA_WOOD.get(),WoodBlocks.STRIPPED_ARAUCARIA_WOOD.get());

        logMap.put(WoodBlocks.PHOENIX_LOG.get(),WoodBlocks.STRIPPED_PHOENIX_LOG.get());
        logMap.put(WoodBlocks.PHOENIX_WOOD.get(),WoodBlocks.STRIPPED_PHOENIX_WOOD.get());

        logMap.put(WoodBlocks.GINKGO_LOG.get(),WoodBlocks.STRIPPED_GINKGO_LOG.get());
        logMap.put(WoodBlocks.GINKGO_WOOD.get(),WoodBlocks.STRIPPED_GINKGO_WOOD.get());

        logMap.put(WoodBlocks.CALAMITES_LOG.get(),WoodBlocks.STRIPPED_CALAMITES_LOG.get());
        logMap.put(WoodBlocks.CALAMITES_WOOD.get(),WoodBlocks.STRIPPED_CALAMITES_WOOD.get());

        logMap.put(WoodBlocks.PSARONIUS_LOG.get(),WoodBlocks.STRIPPED_PSARONIUS_LOG.get());
        logMap.put(WoodBlocks.PSARONIUS_WOOD.get(),WoodBlocks.STRIPPED_PSARONIUS_WOOD.get());

        logMap.put(WoodBlocks.MAGNOLIA_LOG.get(),WoodBlocks.STRIPPED_MAGNOLIA_LOG.get());
        logMap.put(WoodBlocks.MAGNOLIA_WOOD.get(),WoodBlocks.STRIPPED_MAGNOLIA_WOOD.get());

        addLogsToStrippables(logMap);


    }




}
