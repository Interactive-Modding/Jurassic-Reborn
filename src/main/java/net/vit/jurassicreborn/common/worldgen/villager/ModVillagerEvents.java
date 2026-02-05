package net.vit.jurassicreborn.common.worldgen.villager;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.common.blocks.ModBlocks;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;
import net.vit.jurassicreborn.common.worldgen.structure.ModStructureKeys;
import net.vit.jurassicreborn.common.items.ModItems;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(modid = JurassicReborn.MODID)
public class ModVillagerEvents {
    @SubscribeEvent
    public static void addCustomTrades(VillagerTradesEvent event) {
        if (event.getType() == ModVillagers.PALEONTOLOGIST.get()) {
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();
            ItemStack fauna = new ItemStack(ModBlocks.FAUNA_FOSSIL.get());
            trades.get(1).add((trader, rand) -> new MerchantOffer(new ItemStack(Items.EMERALD, 2), fauna,10,8,0.02F));
            trades.get(1).add((trader, rand) -> new MerchantOffer(new ItemStack(Items.PAPER), new ItemStack(ModItems.AMBER.get(), 3), 10, 8, 0.02F));
            trades.get(1).add((trader, rand) -> new MerchantOffer(new ItemStack(Items.EMERALD), new ItemStack(ModItems.PLANT_FOSSIL.get(), 3), 10, 8, 0.02F));
            trades.get(1).add((trader, rand) -> new MerchantOffer(new ItemStack(Items.EMERALD), new ItemStack(ModItems.PLANT_FOSSIL_0.get(), 3), 10, 8, 0.02F));
            trades.get(1).add((trader, rand) -> new MerchantOffer(new ItemStack(Items.EMERALD), new ItemStack(ModItems.PLANT_FOSSIL_1.get(), 3), 10, 8, 0.02F));
            trades.get(1).add((trader, rand) -> new MerchantOffer(new ItemStack(Items.EMERALD), new ItemStack(ModItems.PLANT_FOSSIL_2.get(), 3), 10, 8, 0.02F));
            trades.get(1).add((trader, rand) -> new MerchantOffer(new ItemStack(Items.EMERALD), new ItemStack(ModItems.PLANT_FOSSIL_3.get(), 3), 10, 8, 0.02F));
            trades.get(1).add((trader, rand) -> new MerchantOffer(new ItemStack(Items.EMERALD), new ItemStack(ModItems.FOSSILIZED_EGG_1.get(), 3), 10, 8, 0.02F));
            trades.get(1).add((trader, rand) -> new MerchantOffer(new ItemStack(Items.EMERALD), new ItemStack(ModItems.FOSSILIZED_EGG_2.get(), 3), 10, 8, 0.02F));
            trades.get(1).add((trader, rand) -> new MerchantOffer(new ItemStack(Items.EMERALD), new ItemStack(ModItems.FOSSILIZED_EGG_3.get(), 3), 10, 8, 0.02F));
            trades.get(1).add((trader, rand) -> {
                // Pick a random dinosaur from the BONES map
                List<Map.Entry<Dinosaur, LinkedHashMap<String, RegistryObject<Item>>>> dinoEntries = new ArrayList<>(ModItems.BONES.entrySet());
                if (dinoEntries.isEmpty()) return null; // fallback if none registered

                Map.Entry<Dinosaur, LinkedHashMap<String, RegistryObject<Item>>> randomDino = dinoEntries.get(rand.nextInt(dinoEntries.size()));
                // Pick a random bone from that dinosaur
                List<RegistryObject<Item>> bones = new ArrayList<>(randomDino.getValue().values());
                if (bones.isEmpty()) return null; // fallback if no bones
                RegistryObject<Item> randomBone = bones.get(rand.nextInt(bones.size()));return new MerchantOffer(new ItemStack(Items.EMERALD), new ItemStack(randomBone.get(), 1), 10, 8, 0.02F);});

            trades.get(2).add((trader, rand) -> new MerchantOffer(new ItemStack(Items.EMERALD, 4), new ItemStack(ModBlocks.FAUNA_FOSSIL.get()),3,12,0.05F));
            trades.get(2).add((trader, rand) -> new MerchantOffer(new ItemStack(Items.EMERALD, 4), new ItemStack(ModBlocks.FOSSIL_GRINDER.get()),3,12,0.05F));
            trades.get(2).add((trader, rand) -> new MerchantOffer(new ItemStack(Items.EMERALD, 4), new ItemStack(ModBlocks.CLEANING_STATION.get()),3,12,0.05F));
            trades.get(2).add((trader, rand) -> new MerchantOffer(new ItemStack(Items.EMERALD, 4), new ItemStack(ModBlocks.SKELETON_ASSEMBLY.get()),3,12,0.05F));
            trades.get(3).add((trader, rand) -> new MerchantOffer(new ItemStack(Items.EMERALD, 5), new ItemStack(ModItems.MOSQUITO_AMBER.get()),5,12,0.05F));
            trades.get(3).add((trader, rand) -> new MerchantOffer(new ItemStack(Items.EMERALD, 5), new ItemStack(ModItems.APHID_AMBER.get()),5,12,0.05F));
            trades.get(3).add((trader, rand) -> new MerchantOffer(new ItemStack(Items.EMERALD, 5), new ItemStack(ModItems.SEA_LAMPREY.get()),5,12,0.05F));
            trades.get(3).add((trader, rand) -> new MerchantOffer(new ItemStack(Items.EMERALD, 5), new ItemStack(ModItems.FROZEN_LEECH_ITEM.get()),5,12,0.05F));
        }
        if (event.getType() == ModVillagers.GENETICIST.get()) {
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();
            trades.get(1).add((trader, rand) -> new MerchantOffer(new ItemStack(Items.EMERALD, rand.nextInt(2) + 1), new ItemStack(ModItems.PETRI_DISH.get(), rand.nextInt(5) + 8),10,8,0.02F));
            trades.get(1).add((trader, rand) -> new MerchantOffer(new ItemStack(Items.EMERALD), new ItemStack(ModItems.LIQUID_AGAR.get(), rand.nextInt(5) + 10),10,8,0.02F));
            trades.get(1).add((trader, rand) -> new MerchantOffer(new ItemStack(Items.EMERALD), new ItemStack(ModItems.EMPTY_TEST_TUBE.get(), rand.nextInt(4) + 14),10,8,0.02F));
            trades.get(1).add((trader, rand) -> new MerchantOffer(new ItemStack(Items.EMERALD, 3), new ItemStack(ModItems.STORAGE_DISC.get(), rand.nextInt(8) +1),10,8,0.02F));
            trades.get(1).add((trader, rand) -> new MerchantOffer(new ItemStack(Items.EMERALD), new ItemStack(ModItems.EMPTY_SYRINGE.get(), rand.nextInt(5) + 10),10,8,0.02F));
            trades.get(1).add((trader, rand) -> new MerchantOffer(new ItemStack(Items.EMERALD, rand.nextInt(2) + 1), new ItemStack(ModItems.DNA_NUCLEOTIDES.get(), rand.nextInt(4) + 14),10,8,0.02F));
            trades.get(1).add((trader, rand) -> new MerchantOffer(new ItemStack(ModItems.DNA_NUCLEOTIDES.get(), rand.nextInt(4) + 14), new ItemStack(Items.EMERALD),10,8,0.02F));
            trades.get(1).add((trader, rand) -> new MerchantOffer(new ItemStack(Items.GLASS, rand.nextInt(10) + 10), new ItemStack(Items.EMERALD, rand.nextInt(2) + 1),10,8,0.02F));
            trades.get(1).add((trader, rand) -> new MerchantOffer(new ItemStack(ModItems.BASIC_CIRCUIT.get(), rand.nextInt(5) + 5), new ItemStack(Items.EMERALD, rand.nextInt(3) + 1),10,8,0.02F));

            trades.get(2).add((trader, rand) -> new MerchantOffer(new ItemStack(Items.EMERALD, rand.nextInt(2) + 6), new ItemStack(ModBlocks.DNA_EXTRACTOR.get()),5,12,0.05F));
            trades.get(2).add((trader, rand) -> new MerchantOffer(new ItemStack(Items.EMERALD, rand.nextInt(2) + 5), new ItemStack(ModBlocks.DNA_SEQUENCER.get()),5,12,0.05F));
            trades.get(2).add((trader, rand) -> new MerchantOffer(new ItemStack(Items.EMERALD, rand.nextInt(3) + 5), new ItemStack(ModBlocks.DNA_COMBINER_HYBRIDIZER.get()),5,12,0.05F));
            trades.get(2).add((trader, rand) -> new MerchantOffer(new ItemStack(Items.EMERALD, rand.nextInt(2) + 5), new ItemStack(ModBlocks.DNA_SYNTHESIZER.get()),5,12,0.05F));
            trades.get(2).add((trader, rand) -> new MerchantOffer(new ItemStack(Items.EMERALD, rand.nextInt(4) + 4), new ItemStack(ModBlocks.EMBRYONIC_MACHINE.get()),5,12,0.05F));
            trades.get(2).add((trader, rand) -> new MerchantOffer(new ItemStack(ModItems.ADVANCED_CIRCUIT.get(), rand.nextInt(2) + 2), new ItemStack(Items.EMERALD, rand.nextInt(2) + 2),10,12,0.05F));
            trades.get(2).add((trader, rand) -> new MerchantOffer(new ItemStack(ModItems.ADVANCED_CIRCUIT.get(), rand.nextInt(5) + 10), new ItemStack(Items.EMERALD),10,12,0.05F));

            trades.get(3).add((trader, rand) -> new MerchantOffer(new ItemStack(Items.DIAMOND, rand.nextInt(2) + 6), new ItemStack(ModItems.UNFINISHED_CAR.get()),5,15,0.05F));
            trades.get(3).add((trader, rand) -> new MerchantOffer(new ItemStack(Items.EMERALD, rand.nextInt(10) + 5), new ItemStack(ModItems.MOSQUITO_AMBER.get()),5,15,0.05F));
            trades.get(3).add((trader, rand) -> new MerchantOffer(new ItemStack(Items.DIAMOND, rand.nextInt(3) + 5), new ItemStack(ModBlocks.INCUBATOR.get()),5,15,0.05F));
            trades.get(3).add((trader, rand) -> new MerchantOffer(new ItemStack(Items.DIAMOND, rand.nextInt(4) + 4), new ItemStack(ModBlocks.EMBRYONIC_MACHINE.get()),5,15,0.05F));
            trades.get(3).add((trader, rand) -> new MerchantOffer(new ItemStack(ModItems.COMPUTER_SCREEN.get(), rand.nextInt(2) + 2), new ItemStack(Items.DIAMOND, rand.nextInt(2) + 2),10,15,0.05F));

            trades.get(4).add((trader, rand) -> new MerchantOffer(new ItemStack(ModItems.MOSQUITO_AMBER.get()), new ItemStack(ModItems.SEA_LAMPREY.get(), rand.nextInt(5) + 1),10,20,0.05F));
            trades.get(4).add((trader, rand) -> new MerchantOffer(new ItemStack(Items.DIAMOND, rand.nextInt(20) + 5), new ItemStack(ModItems.MOSQUITO_AMBER.get(), rand.nextInt(5) + 1),10,20,0.05F));
        }

        if (event.getType() == VillagerProfession.CARTOGRAPHER) {
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();
            trades.get(4).add(new JurassicStructureMapForEmeralds(14, ModStructureKeys.JP_SAN_DIEGO, "filled_map.jurassicreborn.jp_san_diego"));
            trades.get(4).add(new JurassicStructureMapForEmeralds(16, ModStructureKeys.ISLA_SORNA_LAB, "filled_map.jurassicreborn.isla_sorna_lab"));
            trades.get(4).add(new JurassicStructureMapForEmeralds(18, ModStructureKeys.VISITOR_CENTRE, "filled_map.jurassicreborn.visitorcenter"));
        }
    }
}
