package net.vit.jurassicreborn.common.items;

import net.minecraft.core.registries.Registries;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.vit.jurassicreborn.JurassicReborn;

import java.util.*;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = JurassicReborn.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class TabHandler {

    // ---- REGISTRY ----
    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, JurassicReborn.MODID);

    // rotating icon helpers
    private static final Map<String, Long> SCROLLING_TAB_UPDATE_TIME = new LinkedHashMap<>();
    private static final Map<String, Integer> SCROLLING_TAB_ICON_INDEX = new LinkedHashMap<>();
    private static final Map<String, List<Supplier<ItemStack>>> SCROLLING_TAB_ICON_SUPPLIERS = new LinkedHashMap<>();

    // bookkeeping
    private static final Map<ResourceLocation, List<Supplier<ItemStack>>> TAB_ITEM_SUPPLIERS = new LinkedHashMap<>();
    private static final Map<ResourceLocation, Supplier<ItemStack>> TAB_ICON_SUPPLIERS = new LinkedHashMap<>();

    // --- define tabs ---
    public static final RegistryObject<CreativeModeTab> ITEMS = registerTab("items", rotatingIcons(
            "amber_mosquito", "amber_aphid", "dna_base_material"
    ));

    public static final RegistryObject<CreativeModeTab> BLOCKS = registerTab("blocks", stackSupplier("gypsum_bricks"));
    public static final RegistryObject<CreativeModeTab> DECORATIONS = registerTab("decorations", stackSupplier("blueprint"));
    public static final RegistryObject<CreativeModeTab> DNA = registerTab("dna", stackSupplier("dna_base_material"));
    public static final RegistryObject<CreativeModeTab> SPAWN_EGGS = registerTab("spawn_eggs", () -> {
        ItemStack velociraptor = stackSupplier("spawn_egg/velociraptor_spawn_egg").get();
        return velociraptor.isEmpty() ? stackSupplier("goat_spawn_egg").get() : velociraptor;
    });
    public static final RegistryObject<CreativeModeTab> FOSSILS = registerTab("fossils", stackSupplier("fauna_fossil_block_item"));
    public static final RegistryObject<CreativeModeTab> FOODS = registerTab("foods", rotatingIcons(
            "cooked_shark_meat", "raw_shark_meat", "fun_fries"
    ));
    public static final RegistryObject<CreativeModeTab> PLANTS = registerTab("plants", stackSupplier("plant_callus"));

    // --- registration helpers ---
    private static RegistryObject<CreativeModeTab> registerTab(String name, Supplier<ItemStack> iconSupplier) {
        ResourceLocation id = new ResourceLocation(JurassicReborn.MODID, name);
        TAB_ICON_SUPPLIERS.put(id, iconSupplier);

        return TABS.register(name, () -> CreativeModeTab.builder()
                .title(Component.translatable("itemGroup.jurassicreborn." + name))
                .icon(iconSupplier)
                .displayItems((parameters, output) ->
                        TAB_ITEM_SUPPLIERS.getOrDefault(id, List.of())
                                .forEach(supplier -> output.accept(supplier.get())))
                .build());
    }

    private static RegistryObject<CreativeModeTab> registerTab(String name, List<Supplier<ItemStack>> iconSuppliers) {
        return registerTab(name, createIconSupplier(name, iconSuppliers));
    }

    private static Supplier<ItemStack> createIconSupplier(String name, List<Supplier<ItemStack>> iconSuppliers) {
        if (iconSuppliers == null || iconSuppliers.isEmpty()) return () -> ItemStack.EMPTY;

        List<Supplier<ItemStack>> sanitized = iconSuppliers.stream()
                .filter(Objects::nonNull)
                .map(TabHandler::sanitize)
                .toList();

        if (sanitized.isEmpty()) return () -> ItemStack.EMPTY;
        if (sanitized.size() == 1) return sanitized.get(0);

        SCROLLING_TAB_ICON_SUPPLIERS.put(name, sanitized);
        return () -> {
            List<Supplier<ItemStack>> suppliers = SCROLLING_TAB_ICON_SUPPLIERS.get(name);
            if (suppliers == null || suppliers.isEmpty()) return ItemStack.EMPTY;

            long now = System.currentTimeMillis();
            long last = SCROLLING_TAB_UPDATE_TIME.getOrDefault(name, 0L);
            int index = SCROLLING_TAB_ICON_INDEX.getOrDefault(name, 0);

            if (now - last >= 5000L) {
                index = (index + 1) % suppliers.size();
                SCROLLING_TAB_ICON_INDEX.put(name, index);
                SCROLLING_TAB_UPDATE_TIME.put(name, now);
            }

            Supplier<ItemStack> current = suppliers.get(index);
            return current != null ? current.get() : ItemStack.EMPTY;
        };
    }

    // --- BuildCreativeModeTabContentsEvent for adding items to vanilla tabs ---
    @SubscribeEvent
    public static void onBuildContents(BuildCreativeModeTabContentsEvent event) {
        // Vanilla and custom tab keys use BuiltInRegistries
        ResourceLocation tabId = BuiltInRegistries.CREATIVE_MODE_TAB.getKey(event.getTab());
        if (tabId != null && TAB_ITEM_SUPPLIERS.containsKey(tabId)) {
            for (Supplier<ItemStack> stack : TAB_ITEM_SUPPLIERS.get(tabId)) {
                event.accept(stack.get());
            }
        }
    }

    // --- public add methods ---
    public static void addToTab(ResourceLocation tabId, Supplier<ItemStack> stackSupplier) {
        TAB_ITEM_SUPPLIERS.computeIfAbsent(tabId, k -> new ArrayList<>()).add(stackSupplier);
    }

    public static void addToTab(ResourceLocation tabId, RegistryObject<? extends Item> itemSupplier) {
        addToTab(tabId, () -> itemSupplier.get().getDefaultInstance());
    }

    public static void addItemToTab(ResourceLocation tabId, Supplier<? extends Item> itemSupplier) {
        addToTab(tabId, () -> new ItemStack(itemSupplier.get()));
    }

    // --- helpers ---
    private static Supplier<ItemStack> sanitize(Supplier<ItemStack> supplier) {
        return () -> supplier == null ? ItemStack.EMPTY : Optional.ofNullable(supplier.get()).orElse(ItemStack.EMPTY);
    }

    private static Supplier<ItemStack> stackSupplier(String path) {
        return () -> stackFromRegistry(path);
    }

    private static List<Supplier<ItemStack>> rotatingIcons(String... itemIds) {
        List<Supplier<ItemStack>> suppliers = new ArrayList<>();
        for (String id : itemIds) suppliers.add(stackSupplier(id));
        return suppliers;
    }

    private static ItemStack stackFromRegistry(String path) {
        if (path == null || path.isEmpty()) return ItemStack.EMPTY;
        ResourceLocation id = path.contains(":") ? ResourceLocation.tryParse(path)
                : new ResourceLocation(JurassicReborn.MODID, path);
        if (id == null) return ItemStack.EMPTY;

        Item item = ForgeRegistries.ITEMS.getValue(id);
        if (item == null) return ItemStack.EMPTY;

        return item.getDefaultInstance();
    }
}
