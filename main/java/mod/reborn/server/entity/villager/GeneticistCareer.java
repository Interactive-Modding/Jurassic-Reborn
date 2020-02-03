package mod.reborn.server.entity.villager;

import mod.reborn.RebornMod;
import mod.reborn.server.block.BlockHandler;
import mod.reborn.server.entity.EntityHandler;
import mod.reborn.server.item.ItemHandler;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipe;
import net.minecraftforge.fml.common.registry.VillagerRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GeneticistCareer extends VillagerRegistry.VillagerCareer {
    public GeneticistCareer(VillagerRegistry.VillagerProfession parent) {
        super(parent, RebornMod.MODID + ":geneticist");
        this.addTrade(1, (EntityVillager.ITradeList) (merchant, recipeList, random) -> {
            List<MerchantRecipe> possible = new ArrayList<>();
            possible.add(new MerchantRecipe(new ItemStack(Items.EMERALD, random.nextInt(2) + 1), new ItemStack(ItemHandler.PETRI_DISH, random.nextInt(5) + 8)));
            possible.add(new MerchantRecipe(new ItemStack(Items.EMERALD), new ItemStack(ItemHandler.LIQUID_AGAR, random.nextInt(5) + 10)));
            possible.add(new MerchantRecipe(new ItemStack(Items.EMERALD), new ItemStack(ItemHandler.EMPTY_TEST_TUBE, random.nextInt(4) + 14)));
            possible.add(new MerchantRecipe(new ItemStack(Items.EMERALD, 3), new ItemStack(ItemHandler.STORAGE_DISC, random.nextInt(8) +1)));
            possible.add(new MerchantRecipe(new ItemStack(Items.EMERALD), new ItemStack(ItemHandler.EMPTY_SYRINGE, random.nextInt(5) + 10)));
            possible.add(new MerchantRecipe(new ItemStack(Items.EMERALD, random.nextInt(2) + 1), new ItemStack(ItemHandler.DNA_NUCLEOTIDES, random.nextInt(4) + 14)));
            possible.add(new MerchantRecipe(new ItemStack(ItemHandler.DNA_NUCLEOTIDES, random.nextInt(4) + 14), new ItemStack(Items.EMERALD)));
            possible.add(new MerchantRecipe(new ItemStack(Blocks.GLASS, random.nextInt(10) + 10), new ItemStack(Items.EMERALD, random.nextInt(2) + 1)));
            possible.add(new MerchantRecipe(new ItemStack(ItemHandler.BASIC_CIRCUIT, random.nextInt(5) + 5), new ItemStack(Items.EMERALD, random.nextInt(3) + 1)));
            int count = 1;
            if (random.nextInt(5) == 0) {
                count += random.nextInt(2);
            }
            for (int i = 0; i < count; i++) {
                recipeList.add(possible.get(random.nextInt(possible.size())));
            }
        });
        this.addTrade(2, (EntityVillager.ITradeList) (merchant, recipeList, random) -> {
            List<MerchantRecipe> possible = new ArrayList<>();
            possible.add(new MerchantRecipe(new ItemStack(Items.EMERALD, random.nextInt(2) + 6), new ItemStack(BlockHandler.DNA_EXTRACTOR)));
            possible.add(new MerchantRecipe(new ItemStack(Items.EMERALD, random.nextInt(2) + 5), new ItemStack(BlockHandler.DNA_SEQUENCER)));
            possible.add(new MerchantRecipe(new ItemStack(Items.EMERALD, random.nextInt(3) + 5), new ItemStack(BlockHandler.DNA_COMBINATOR_HYBRIDIZER)));
            possible.add(new MerchantRecipe(new ItemStack(Items.EMERALD, random.nextInt(2) + 5), new ItemStack(BlockHandler.DNA_SYNTHESIZER)));
            possible.add(new MerchantRecipe(new ItemStack(Items.EMERALD, random.nextInt(4) + 4), new ItemStack(BlockHandler.EMBRYONIC_MACHINE)));
            possible.add(new MerchantRecipe(new ItemStack(ItemHandler.ADVANCED_CIRCUIT, random.nextInt(2) + 2), new ItemStack(Items.EMERALD, random.nextInt(2) + 2)));
            possible.add(new MerchantRecipe(new ItemStack(ItemHandler.ADVANCED_CIRCUIT, random.nextInt(5) + 10), new ItemStack(Items.EMERALD)));
            int count = 1;
            for (int i = 0; i < count; i++) {
                recipeList.add(possible.get(random.nextInt(possible.size())));
            }
        });
        this.addTrade(3, (EntityVillager.ITradeList) (merchant, recipeList, random) -> {
            List<MerchantRecipe> possible = new ArrayList<>();
            possible.add(new MerchantRecipe(new ItemStack(Items.DIAMOND, random.nextInt(2) + 6), new ItemStack(ItemHandler.UNFINISHED_CAR)));
            possible.add(new MerchantRecipe(new ItemStack(Items.EMERALD, random.nextInt(10) + 5), new ItemStack(ItemHandler.AMBER)));
            possible.add(new MerchantRecipe(new ItemStack(Items.DIAMOND, random.nextInt(3) + 5), new ItemStack(BlockHandler.INCUBATOR)));
            possible.add(new MerchantRecipe(new ItemStack(Items.EMERALD, random.nextInt(2) + 5), new ItemStack(BlockHandler.DISPLAY_BLOCK, 1,random.nextInt(EntityHandler.getHighestID() + 1))));
            possible.add(new MerchantRecipe(new ItemStack(Items.DIAMOND, random.nextInt(4) + 4), new ItemStack(BlockHandler.EMBRYONIC_MACHINE)));
            possible.add(new MerchantRecipe(new ItemStack(ItemHandler.COMPUTER_SCREEN, random.nextInt(2) + 2), new ItemStack(Items.DIAMOND, random.nextInt(2) + 2)));
            possible.add(new MerchantRecipe(new ItemStack(Items.DIAMOND, random.nextInt(20) + 10), new ItemStack(ItemHandler.HATCHED_EGG, 1, random.nextInt(EntityHandler.getHighestID() + 1))));
            int count = 1;
            for (int i = 0; i < count; i++) {
                recipeList.add(possible.get(random.nextInt(possible.size())));
            }
        });
        this.addTrade(4, (EntityVillager.ITradeList) (merchant, recipeList, random) -> {
            List<MerchantRecipe> possible = new ArrayList<>();
            possible.add(new MerchantRecipe(new ItemStack(ItemHandler.AMBER, 1), new ItemStack(ItemHandler.SEA_LAMPREY, random.nextInt(5) + 1)));
            possible.add(new MerchantRecipe(new ItemStack(Items.DIAMOND, random.nextInt(20) + 5), new ItemStack(ItemHandler.AMBER, random.nextInt(5) + 1)));
        });
    }
}