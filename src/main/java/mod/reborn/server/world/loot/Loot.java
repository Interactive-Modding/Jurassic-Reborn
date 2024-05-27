package mod.reborn.server.world.loot;

import mod.reborn.server.item.DisplayBlockItem;
import mod.reborn.server.item.FossilItem;
import mod.reborn.server.item.ItemHandler;
import mod.reborn.server.plant.Plant;
import mod.reborn.server.plant.PlantHandler;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.*;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.conditions.RandomChance;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraft.world.storage.loot.functions.SetCount;
import net.minecraft.world.storage.loot.functions.SetMetadata;
import net.minecraft.world.storage.loot.functions.SetNBT;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import mod.reborn.RebornMod;
import mod.reborn.server.dinosaur.Dinosaur;
import mod.reborn.server.entity.EntityHandler;
import mod.reborn.server.genetics.DinoDNA;
import mod.reborn.server.genetics.GeneticsHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Loot {
    public static final ResourceLocation GENETICIST_HOUSE_CHEST = new ResourceLocation(RebornMod.MODID, "structure/geneticist_house");
    public static final ResourceLocation ICE_FOSSIL_CHEST = new ResourceLocation(RebornMod.MODID, "structure/ice_fossil_chest");

    public static final ResourceLocation RAPTOR_CHEST = new ResourceLocation(RebornMod.MODID, "structure/raptor_chest");
    public static final ResourceLocation VISITOR_GROUND_STORAGE = new ResourceLocation(RebornMod.MODID, "structure/visitor_centre/ground_storage");
    public static final ResourceLocation VISITOR_CONTROL_ROOM = new ResourceLocation(RebornMod.MODID, "structure/visitor_centre/control_room");
    public static final ResourceLocation VISITOR_LABORATORY = new ResourceLocation(RebornMod.MODID, "structure/visitor_centre/laboratory");
    public static final ResourceLocation VISITOR_CRYONICS = new ResourceLocation(RebornMod.MODID, "structure/visitor_centre/cryonics");
    public static final ResourceLocation VISITOR_INFIRMARY = new ResourceLocation(RebornMod.MODID, "structure/visitor_centre/infirmary");
    public static final ResourceLocation VISITOR_GARAGE = new ResourceLocation(RebornMod.MODID, "structure/visitor_centre/garage");
    public static final ResourceLocation VISITOR_STAFF_DORMS = new ResourceLocation(RebornMod.MODID, "structure/visitor_centre/staff_dorms");
    public static final ResourceLocation VISITOR_KITCHEN = new ResourceLocation(RebornMod.MODID, "structure/visitor_centre/kitchen");
    public static final ResourceLocation VISITOR_DORM_TOWER = new ResourceLocation(RebornMod.MODID, "structure/visitor_centre/dorm_tower");
    public static final ResourceLocation VISITOR_DINING_HALL = new ResourceLocation(RebornMod.MODID, "structure/visitor_centre/dining_hall");

    public static final DinosaurData DINOSAUR_DATA = new DinosaurData();
    public static final PlantData PLANT_DATA = new PlantData();
    public static final RandomDNA RANDOM_DNA = new RandomDNA();
    public static final RandomDNA FULL_DNA = new RandomDNA(true);


    private static long tableID = 0;

    public static PoolBuilder pool(String name) {
        return new PoolBuilder(name);
    }

    public static EntryBuilder entry(Item item) {
        return new EntryBuilder(item);
    }

    public static EntryBuilder entry(Block block) {
        return new EntryBuilder(Item.getItemFromBlock(block));
    }

    public static MultiEntryBuilder entries(Item... items) {
        return new MultiEntryBuilder(items);
    }

    public static LootEntry[] entries(Object... entryData) {
        LootEntry[] builders = new LootEntry[entryData.length / 3];
        for (int i = 0; i < entryData.length; i += 3) {
            Object itemData = entryData[i];
            Item item = itemData instanceof Block ? Item.getItemFromBlock((Block) itemData) : (Item) itemData;
            EntryBuilder entry = Loot.entry(item).count((int) entryData[i + 1], (int) entryData[i + 2]);
            builders[i / 3] = entry.build();
        }
        return builders;
    }

    @SuppressWarnings("unchecked")
    public static void handleTable(LootTable table, ResourceLocation name) {
        boolean frozen = false;
        if (table.isFrozen()) {//Some mods like to replace the loot tables which ends up with the loot table being frozen.
            frozen = true;
            ReflectionHelper.setPrivateValue(LootTable.class, table, false, "isFrozen");
            for (LootPool lootPool : ((List<LootPool>) ReflectionHelper.getPrivateValue(LootTable.class, table, "pools", "field_186466_c"))) { //Unfreeze all the pools
                ReflectionHelper.setPrivateValue(LootPool.class, lootPool, false, "isFrozen");
            }
        }
        

        if (name == LootTableList.GAMEPLAY_FISHING) {
            LootEntry gracilaria = Loot.entry(ItemHandler.GRACILARIA).weight(25).build();
            table.addPool(Loot.pool("jwr_gracilaria").rolls(1, 1).chance(0.1F).entry(gracilaria).build());
        } else if (name == LootTableList.CHESTS_VILLAGE_BLACKSMITH || name == LootTableList.CHESTS_NETHER_BRIDGE || name == LootTableList.CHESTS_SIMPLE_DUNGEON || name == LootTableList.CHESTS_STRONGHOLD_CORRIDOR || name == LootTableList.CHESTS_DESERT_PYRAMID || name == LootTableList.CHESTS_ABANDONED_MINESHAFT) {
            LootEntry plantFossil = Loot.entry(ItemHandler.PLANT_FOSSIL).weight(5).count(1, 3).build();
            LootEntry twig = Loot.entry(ItemHandler.TWIG_FOSSIL).weight(5).count(1, 3).build();
            LootEntry amber = Loot.entry(ItemHandler.AMBER).weight(2).count(0, 1).data(0, 1).build();
            LootEntry display = Loot.entry(ItemHandler.DISPLAY_BLOCK).function(DINOSAUR_DATA).weight(25).build();
            LootEntry skull = Loot.entry(ItemHandler.FOSSILS.get("skull")).weight(2).function(DINOSAUR_DATA).count(1, 2).build();

            table.addPool(Loot.pool("jwr_fossils").rolls(1, 2).entries(plantFossil, twig, amber, display, skull).build());

            LootEntry[] records = Loot.entries(ItemHandler.JURASSICRAFT_THEME_DISC, ItemHandler.DONT_MOVE_A_MUSCLE_DISC, ItemHandler.TROODONS_AND_RAPTORS_DISC).buildEntries();
            table.addPool(Loot.pool("jwr_records").rolls(0, 2).entries(records).build());
        } else if (name == LootTableList.CHESTS_VILLAGE_BLACKSMITH || name == LootTableList.CHESTS_NETHER_BRIDGE || name == LootTableList.CHESTS_SIMPLE_DUNGEON || name == LootTableList.CHESTS_STRONGHOLD_CORRIDOR || name == LootTableList.CHESTS_DESERT_PYRAMID || name == LootTableList.CHESTS_ABANDONED_MINESHAFT || name == LootTableList.CHESTS_IGLOO_CHEST || name == LootTableList.CHESTS_JUNGLE_TEMPLE || name == LootTableList.CHESTS_WOODLAND_MANSION || name == LootTableList.CHESTS_STRONGHOLD_LIBRARY) {
            LootEntry actionFigure = Loot.entry(ItemHandler.DISPLAY_BLOCK).function(DINOSAUR_DATA).weight(1).build();
            table.addPool(Loot.pool("jwr_fossils_1").rolls(1, 2).entries(actionFigure).build());
        }   else if (name == Loot.VISITOR_GROUND_STORAGE) {
            LootEntry amber = Loot.entry(ItemHandler.FROZEN_LEECH).count(0, 1).build();
            LootEntry amber_2 = Loot.entry(ItemHandler.SEA_LAMPREY).count(0, 1).build();
            LootEntry amber_1 = Loot.entry(ItemHandler.AMBER).data(0, 1).count(0, 2).build();
            LootEntry wool = Loot.entry(Blocks.WOOL).data(0, 15).count(0, 64).build();
            table.addPool(Loot.pool("jwr_items").rolls(5, 6).entries(amber,amber_1,amber_2, wool).build());
        } else if (name == Loot.VISITOR_LABORATORY) {
            LootEntry softTissue = Loot.entry(ItemHandler.SOFT_TISSUE).count(0, 3).function(DINOSAUR_DATA).build();
            LootEntry plantSoftTissue = Loot.entry(ItemHandler.PLANT_SOFT_TISSUE).count(0, 3).function(PLANT_DATA).build();
            LootEntry amber = Loot.entry(ItemHandler.AMBER).data(0, 1).count(0, 6).build();
            table.addPool(Loot.pool("jwr_items").rolls(3, 4).entries( softTissue, plantSoftTissue, amber).build());
        } else if (name == Loot.VISITOR_CRYONICS) {
            LootEntry dna = Loot.entry(ItemHandler.DNA).function(DINOSAUR_DATA).function(FULL_DNA).build();
            table.addPool(Loot.pool("jwr_items").rolls(7, 12).entries(dna).build());
        } else if (name == Loot.ICE_FOSSIL_CHEST) {
            LootEntry actionFigure = Loot.entry(ItemHandler.DISPLAY_BLOCK).function(DINOSAUR_DATA).weight(1).build();
            LootEntry twig = Loot.entry(ItemHandler.TWIG_FOSSIL).weight(5).count(1, 3).build();
            LootEntry plantFossil = Loot.entry(ItemHandler.PLANT_FOSSIL).weight(5).count(1, 3).build();
            LootEntry amber = Loot.entry(ItemHandler.FROZEN_LEECH).count(0, 1).build();
            table.addPool(Loot.pool("jwr_items").rolls(5, 6).entries(actionFigure,twig,plantFossil,amber).build());
        } else if (name == Loot.RAPTOR_CHEST) {
            LootEntry actionFigure = Loot.entry(ItemHandler.DISPLAY_BLOCK).function(DINOSAUR_DATA).weight(1).build();
            LootEntry twig = Loot.entry(ItemHandler.TWIG_FOSSIL).weight(5).count(1, 3).build();
            LootEntry plantFossil = Loot.entry(ItemHandler.PLANT_FOSSIL).weight(5).count(1, 3).build();
            LootEntry amber = Loot.entry(ItemHandler.AMBER).data(0, 1).count(0, 1).build();
            table.addPool(Loot.pool("jwr_items").rolls(5, 6).entries(actionFigure,twig,plantFossil,amber).build());
        } else if (name == Loot.VISITOR_DINING_HALL) {
            LootEntry amber = Loot.entry(ItemHandler.AMBER).weight(2).count(0, 1).data(0, 1).build();
            LootEntry tooth = Loot.entry(ItemHandler.FOSSILS.get("teeth")).weight(2).function(DINOSAUR_DATA).count(1, 2).build();
            LootEntry actionFigure = Loot.entry(ItemHandler.DISPLAY_BLOCK).function(DINOSAUR_DATA).weight(1).build();
            table.addPool(Loot.pool("jwr_items").rolls(8, 11).entries(amber, tooth, actionFigure).build());
        }
        if(frozen) { //If the table was originally frozen, then freeze it.
            table.freeze();
        }
    }
    public static class PoolBuilder {
        private String name;
        private int minRolls;
        private int maxRolls = 1;
        private int minBonusRolls;
        private int maxBonusRolls;
        private List<LootCondition> conditions = new ArrayList<>();
        private List<LootEntry> entries = new ArrayList<>();

        private PoolBuilder(String name) {
            this.name = name;
        }

        public PoolBuilder rolls(int min, int max) {
            this.minRolls = min;
            this.maxRolls = max;
            return this;
        }

        public PoolBuilder bonusRolls(int min, int max) {
            this.minBonusRolls = min;
            this.maxBonusRolls = max;
            return this;
        }

        public PoolBuilder condition(LootCondition condition) {
            this.conditions.add(condition);
            return this;
        }

        public PoolBuilder chance(float chance) {
            return this.condition(new RandomChance(chance));
        }

        public PoolBuilder entry(LootEntry entry) {
            this.entries.add(entry);
            return this;
        }

        public PoolBuilder entries(LootEntry... entries) {
            for (LootEntry entry : entries) {
                this.entry(entry);
            }
            return this;
        }

        public LootPool build() {
            LootEntry[] entries = this.entries.toArray(new LootEntry[this.entries.size()]);
            LootCondition[] conditions = this.conditions.toArray(new LootCondition[this.conditions.size()]);
            return new LootPool(entries, conditions, new RandomValueRange(this.minRolls, this.maxRolls), new RandomValueRange(this.minBonusRolls, this.maxBonusRolls), this.name);
        }
    }

    public static class EntryBuilder {
        protected Item item;
        protected int weight = 1;
        protected int quality = 0;
        protected List<LootCondition> conditions = new ArrayList<>();
        protected List<LootFunction> functions = new ArrayList<>();

        private EntryBuilder(Item item) {
            this.item = item;
        }

        public EntryBuilder weight(int weight) {
            this.weight = weight;
            return this;
        }

        public EntryBuilder quality(int quality) {
            this.quality = quality;
            return this;
        }

        public EntryBuilder condition(LootCondition condition) {
            this.conditions.add(condition);
            return this;
        }

        public EntryBuilder tag(NBTTagCompound compound) {
            return this.function(new SetNBT(new LootCondition[0], compound));
        }

        public EntryBuilder function(LootFunction function) {
            this.functions.add(function);
            return this;
        }

        public EntryBuilder count(int min, int max) {
            return this.function(new SetCount(new LootCondition[0], new RandomValueRange(min, max)));
        }

        public EntryBuilder data(int min, int max) {
            return this.function(new SetMetadata(new LootCondition[0], new RandomValueRange(min, max)));
        }

        public EntryBuilder data(int data) {
            return this.data(data, data);
        }

        public LootEntry build() {
            LootCondition[] conditions = this.conditions.toArray(new LootCondition[this.conditions.size()]);
            LootFunction[] functions = this.functions.toArray(new LootFunction[this.functions.size()]);
            return new LootEntryItem(this.item, this.weight, this.quality, functions, conditions, this.item.getUnlocalizedName() + "_" + tableID++);
        }
    }

    public static class MultiEntryBuilder extends EntryBuilder {
        protected Item[] items;

        private MultiEntryBuilder(Item... items) {
            super(null);
            this.items = items;
        }

        public LootEntry[] buildEntries() {
            LootCondition[] conditions = this.conditions.toArray(new LootCondition[this.conditions.size()]);
            LootFunction[] functions = this.functions.toArray(new LootFunction[this.functions.size()]);
            LootEntry[] entries = new LootEntry[this.items.length];
            for (int i = 0; i < this.items.length; i++) {
                Item item = this.items[i];
                entries[i] = new LootEntryItem(item, this.weight, this.quality, functions, conditions, item.getUnlocalizedName() + "_" + tableID++);
            }
            return entries;
        }
    }

    public static class DinosaurData extends LootFunction {
        public DinosaurData() {
            super(new LootCondition[0]);
        }

        public DinosaurData(LootCondition[] conditions) {
            super(conditions);
        }

        @Override
        public ItemStack apply(ItemStack stack, Random rand, LootContext context) {
            List<Dinosaur> dinosaurs = EntityHandler.getRegisteredDinosaurs();
            Dinosaur dinosaur = dinosaurs.get(rand.nextInt(dinosaurs.size()));
            if (stack.getItem() instanceof FossilItem && !dinosaur.isHybrid) {
                String boneName = dinosaur.getBones()[rand.nextInt(dinosaur.getBones().length)];
                stack = new ItemStack(ItemHandler.FOSSILS.get(boneName), 1, EntityHandler.getDinosaurId(dinosaur));
                return stack;
            } else if (stack.getItem() instanceof DisplayBlockItem) {
                DisplayBlockItem s = (DisplayBlockItem) stack.getItem();
                stack.setItemDamage(s.getMetadata(EntityHandler.getDinosaurId(dinosaur), 0, false));
                return stack;
            } else {
                stack.setItemDamage(EntityHandler.getDinosaurId(dinosaur));
                return stack;
            }

        }
    }

    public static class PlantData extends LootFunction {
        public PlantData() {
            super(new LootCondition[0]);
        }

        public PlantData(LootCondition[] conditions) {
            super(conditions);
        }

        @Override
        public ItemStack apply(ItemStack stack, Random rand, LootContext context) {
            List<Plant> plants = PlantHandler.getPrehistoricPlants();
            Plant plant = plants.get(rand.nextInt(plants.size()));
            stack.setItemDamage(PlantHandler.getPlantId(plant));
            return stack;
        }
    }

    public static class RandomDNA extends LootFunction {
        private boolean full;

        public RandomDNA() {
            super(new LootCondition[0]);
        }

        public RandomDNA(LootCondition[] conditions) {
            super(conditions);
        }

        public RandomDNA(boolean full) {
            this();
            this.full = full;
        }

        @Override
        public ItemStack apply(ItemStack stack, Random rand, LootContext context) {
            int quality = (rand.nextInt(10) + 1) * 5;
            if (this.full) {
                quality = 100;
            }
            DinoDNA dna = new DinoDNA(EntityHandler.getDinosaurById(stack.getItemDamage()), quality, GeneticsHelper.randomGenetics(rand));
            NBTTagCompound compound = new NBTTagCompound();
            dna.writeToNBT(compound);
            stack.setTagCompound(compound);
            return stack;
        }
    }

    public static class WrittenBook extends LootFunction {
        private final String title;
        private final String author;
        private final String[] pages;

        public WrittenBook(LootCondition[] conditions, String title, String author, String[] pages) {
            super(conditions);
            this.title = title;
            this.author = author;
            this.pages = pages;
        }

        @Override
        public ItemStack apply(ItemStack stack, Random rand, LootContext context) {
            NBTTagCompound compound = stack.getTagCompound();
            if (compound == null) {
                compound = new NBTTagCompound();
                stack.setTagCompound(compound);
            }
            compound.setBoolean("resolved", false);
            compound.setInteger("generation", 0);
            compound.setString("title", this.title);
            compound.setString("author", this.author);
            NBTTagList pages = new NBTTagList();
            for (String page : this.pages) {
                pages.appendTag(new NBTTagString(page));
            }
            compound.setTag("pages", pages);
            return stack;
        }
    }
}
