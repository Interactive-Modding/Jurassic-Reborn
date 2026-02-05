package net.vit.jurassicreborn.common.util;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.vit.jurassicreborn.mixin.accessors.CreativeModeTabAccessor;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class ItemStackCreativeModeTabSystem {
    public static final HashMap<CreativeModeTab, NonNullList<Supplier<ItemStack>>> TAB_ITEM_SUPPLIER_HASH_MAP = new HashMap<>();

    public static final HashMap<CreativeModeTab, NonNullList<ItemStack>> TAB_ITEM_HASH_MAP = new LinkedHashMap<>();

    private static boolean applied = false;

    @SafeVarargs
    public static void addItemStacksToTab(CreativeModeTab tab, NonNullList<Supplier<ItemStack>>... stacks){

        for(NonNullList<Supplier<ItemStack>> stack : stacks){
            if(TAB_ITEM_SUPPLIER_HASH_MAP.containsKey(tab)){
                NonNullList<Supplier<ItemStack>> tabMap = TAB_ITEM_SUPPLIER_HASH_MAP.remove(tab);
                NonNullList<Supplier<ItemStack>> newTabMap = NonNullList.create();//wheee smart

                newTabMap.addAll(tabMap);

                newTabMap.addAll(stack);

                TAB_ITEM_SUPPLIER_HASH_MAP.put(tab, newTabMap);

            }else {
                TAB_ITEM_SUPPLIER_HASH_MAP.put(tab, stack);
            }
            if(tab != CreativeModeTab.TAB_SEARCH) {
                addItemStacksToTab(CreativeModeTab.TAB_SEARCH, stacks);
            }
        }

    }


    public static void apply(){
        if(getApplied())
            return;

        for(CreativeModeTab key : TAB_ITEM_SUPPLIER_HASH_MAP.keySet()){
            NonNullList<Supplier<ItemStack>> stackSuppliers = TAB_ITEM_SUPPLIER_HASH_MAP.get(key);

            NonNullList<ItemStack> stacks = collect(stackSuppliers);
            TAB_ITEM_HASH_MAP.put(key, stacks);

        }
        applied = true;
    }

    public static boolean getApplied(){
        return applied;
    }



    public static NonNullList<ItemStack> getItemStacksForTab(CreativeModeTab tab){

        //if the hash map hasn't been built yet, build it-- and the recursivly ensure it's actually completed.
        //Shouldn't mess up, but I will Eat My Words later.
        //this is to prevent unregistered items getting called.
        if(!getApplied()){
            apply();
            return getItemStacksForTab(tab);
        }

        NonNullList<ItemStack> stacks = TAB_ITEM_HASH_MAP.get(tab);
        if(stacks == null){
            stacks = NonNullList.create();
        }

        return stacks;
    }


    public static void jurassicreborn$fillItemStacks(CreativeModeTab instance, NonNullList<ItemStack> itemStacks) {
        if(instance == CreativeModeTab.TAB_SEARCH) {
            ((CreativeModeTabAccessor) instance).jurassicreborn$callFillItemList(itemStacks);
            NonNullList<ItemStack> searchStacks = ItemStackCreativeModeTabSystem.getItemStacksForTab(instance);
            if(!searchStacks.isEmpty()) {
                itemStacks.addAll(searchStacks);
            }
            return;
        }

        NonNullList<ItemStack> stacks = ItemStackCreativeModeTabSystem.getItemStacksForTab(instance);

        instance.fillItemList(stacks);

        if(!stacks.isEmpty()){
            itemStacks.addAll(stacks);
        }
    }

    public static NonNullList<ItemStack> collect(NonNullList<Supplier<ItemStack>> itemSuppliers){

        NonNullList<ItemStack> collected;

        collected = itemSuppliers.stream().collect(collector);

        return collected;


    }

    private static final Collector<Supplier<ItemStack>, ?, NonNullList<ItemStack>> collector = new Collector<Supplier<ItemStack>, ArrayList<ItemStack>, NonNullList<ItemStack>>() {
        @Override
        public Supplier<ArrayList<ItemStack>> supplier() {
            return ArrayList::new;
        }

        @Override
        public BiConsumer<ArrayList<ItemStack>, Supplier<ItemStack>> accumulator() {
            return ((itemStacks, itemStackSupplier) -> {
                itemStacks.add(itemStackSupplier.get());
            });
        }

        @Override
        public BinaryOperator<ArrayList<ItemStack>> combiner() {
            return (one, two) ->{
                one.addAll(two);
                return one;
            };
        }

        @Override
        public Function<ArrayList<ItemStack>, NonNullList<ItemStack>> finisher() {
            return (list) -> {
                NonNullList<ItemStack> ret = NonNullList.create();
                for (int i = 0; i < list.size(); i++) {
                    ret.add(i, list.get(i));
                }
                return ret;
            };
        }

        @Override
        public Set<Characteristics> characteristics() {
            return Collections.unmodifiableSet(EnumSet.of(Collector.Characteristics.UNORDERED));
        }
    };
}
