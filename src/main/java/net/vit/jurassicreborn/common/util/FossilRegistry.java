//package net.vit.JurassicReborn.common.util;
//
//import net.minecraft.world.item.Item;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.level.block.Block;
//import net.minecraftforge.registries.DeferredRegister;
//import net.minecraftforge.registries.RegistryObject;
//import net.vit.JurassicReborn.common.blocks.ModBlocks;
//import net.vit.JurassicReborn.common.entities.Dinosaurs.Dinosaur;
//import net.vit.JurassicReborn.common.items.Fossils.FaunaFossilBlockItem;
//import net.vit.JurassicReborn.common.items.ModItems;
//
//import java.util.HashMap;
//import java.util.Locale;
//
//public class FossilRegistry {
//    public static final HashMap<Dinosaur, RegistryObject<Item>> FOSSIL_ITEMS = new HashMap<>();
//
//    public static void registerFossilItems(DeferredRegister<Item> registry) {
//        for (Dinosaur dino : Dinosaur.DINOS) {
//            if (dino.isHybrid() || dino == Dinosaur.EMPTY) continue;
//
//            String name = dino.getName().toLowerCase(Locale.ROOT).replaceAll(" ", "_");
//            Block fossilBlock = ModBlocks.getFossilBlockFor(dino);
//
//            if (fossilBlock != null) {
//                RegistryObject<Item> fossilItem = registry.register(name + "_fossil", () ->
//                        new FaunaFossilBlockItem(fossilBlock, new Item.Properties()));
//                FOSSIL_ITEMS.put(dino, fossilItem);
//            }
//        }
//    }
//
//    public static ItemStack getFossilItem(Dinosaur dino) {
//        RegistryObject<Item> item = FOSSIL_ITEMS.get(dino);
//        return item != null ? FossilUtil.setDino(item.get().getDefaultInstance(), dino) : ItemStack.EMPTY;
//    }
//
//    public static ItemStack getEncasedFossilItem(Dinosaur dino) {
//        // These are now registered individually in ModItems, not through this registry
//        // So we just call the item directly from ModItems via helper
//        Item item = ModItems.getEncasedItemFor(dino);
//        return item != null ? new ItemStack(item) : ItemStack.EMPTY;
//    }
//}
