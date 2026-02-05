package net.vit.jurassicreborn.common.items;

import net.vit.jurassicreborn.common.blocks.ModBlocks;
import net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurHandler;
import net.vit.jurassicreborn.common.items.misc.DinosaurSpawnEggItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Supplier;

public class TabHandler {

    public static ArrayList<CreativeModeTab> tabs = new ArrayList<>();


    public static HashMap<String, ArrayDeque<Item>> SCROLLING_TAB_ITEMS = new HashMap<>();

    public static final CreativeModeTab ITEMS = makeTab("jurassicreborn.items", List.of(ModItems.APHID_AMBER,ModItems.MOSQUITO_AMBER));
//    public static final CreativeModeTab CREATIVE = makeTab(new CreativeModeTab("jurassicreborn.creative") {
//        @Override
//        public ItemStack makeIcon() {
//            return ModItems.BIRTHING_WAND.get().getDefaultInstance();
//        }
//
//        @Override
//        public boolean hasSearchBar() {
//            return true;
//        }
//    });
    public static final CreativeModeTab BLOCKS = makeSimpleTab("jurassicreborn.blocks", ModItems.GYPSUM_BRICKS);

    public static final CreativeModeTab DECORATIONS = makeTab("jurassicreborn.decorations", new ArrayList<>(ModItems.ACTION_FIGURES.values()));

    public static final CreativeModeTab DNA = makeSimpleTab("jurassicreborn.dna", ModItems.DINOSAUR_DNA.get(DinosaurHandler.MAMENCHISAURUS));
    public static final CreativeModeTab SPAWN_EGGS = makeSimpleTab("jurassicreborn.spawn_eggs", () -> {
        RegistryObject<DinosaurSpawnEggItem> egg = ModItems.getSpawnEgg(DinosaurHandler.VELOCIRAPTOR);
        if (egg != null) {
            return egg.get();
        }
        return ModItems.GOAT_SPAWN_EGG.get();
    });

    public static final CreativeModeTab FOSSILS = makeSimpleTab("jurassicreborn.fossils",ModItems.FAUNA_FOSSIL_BLOCK);

    public static final CreativeModeTab FOODS = makeTab("jurassicreborn.foods", ModItems.ALL_MEATS);

    public static final CreativeModeTab PLANTS = makeSimpleTab("jurassicreborn.plants", ModItems.PLANT_CALLUS);

    public static <I extends Item> CreativeModeTab makeSimpleTab(String name, Supplier<I> icon) {
        return new CreativeModeTab(name) {
            @Override
            public ItemStack makeIcon() {
                return icon.get().getDefaultInstance();
            }
        };
    }

        public static <I extends Item> CreativeModeTab makeTab(String name, List<Supplier<? extends I>> icon){
        CreativeModeTab tab = new CreativeModeTab(name){

            private long prev = System.currentTimeMillis();

            @Override
            public ItemStack getIconItem() {//this bit in particular makes the icon scroll/change between the specified items, or registered dinosaur display cases
                if(!SCROLLING_TAB_ITEMS.containsKey(name) ) {
                    return super.getIconItem();
                } else{

                    if(System.currentTimeMillis() >= prev + 5000){
                        Item i = SCROLLING_TAB_ITEMS.get(name).poll();
                        SCROLLING_TAB_ITEMS.get(name).addLast(i);
                        prev = System.currentTimeMillis();
                    }
//                    SCROLLING_TAB_ITEMS.get(name).addLast(i);
                    return SCROLLING_TAB_ITEMS.get(name).peek().getDefaultInstance();
                }
            }

            @Override
            public @NotNull ItemStack makeIcon() {
                if(icon == null){
                    return ItemStack.EMPTY;
                }


                if (icon.size() != 1) {
                    ArrayDeque<Item> itemsQueue = new ArrayDeque<>();
                    for (Supplier<? extends I> itemSup : icon) {
                        itemsQueue.push(itemSup.get());
                    }
                    SCROLLING_TAB_ITEMS.put(name, itemsQueue);
                }
                return icon.get(0).get().getDefaultInstance();
            }
        };
        tabs.add(tab);
        return tab;
    }
    public static CreativeModeTab makeTab(CreativeModeTab tab){
        tabs.add(tab);
        return tab;
    }

}
