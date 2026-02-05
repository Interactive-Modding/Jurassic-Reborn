package net.vit.jurassicreborn.common.items;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.vit.jurassicreborn.JurassicReborn;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

public class TabHandler {

    // rotating icon helpers
    private static final Map<String, Long> SCROLLING_TAB_UPDATE_TIME = new LinkedHashMap<>();
    private static final Map<String, Integer> SCROLLING_TAB_ICON_INDEX = new LinkedHashMap<>();
    private static final Map<String, List<Supplier<ItemStack>>> SCROLLING_TAB_ICON_SUPPLIERS = new LinkedHashMap<>();

    // bookkeeping
    private static final Map<CreativeModeTab, List<Supplier<ItemStack>>> TAB_ITEM_SUPPLIERS = new LinkedHashMap<>();
    private static final Map<CreativeModeTab, Component> TAB_TITLES = new LinkedHashMap<>();
    private static final Map<CreativeModeTab, Supplier<ItemStack>> TAB_ICON_SUPPLIERS = new LinkedHashMap<>();

    // --- define tabs ---
    public static final CreativeModeTab ITEMS       = makeTab("jurassicreborn.items", rotatingIcons(
            "amber_mosquito",
            "amber_aphid",
            "dna_base_material"
    ));
    public static final CreativeModeTab BLOCKS      = makeSimpleTab("jurassicreborn.blocks", stackSupplier("gypsum_bricks"));
    public static final CreativeModeTab DECORATIONS = makeSimpleTab("jurassicreborn.decorations", stackSupplier("blueprint"));
    public static final CreativeModeTab DNA         = makeSimpleTab("jurassicreborn.dna", stackSupplier("dna_base_material"));
    public static final CreativeModeTab SPAWN_EGGS  = makeSimpleTab("jurassicreborn.spawn_eggs", () -> {
        ItemStack velociraptor = stackSupplier("spawn_egg/velociraptor_spawn_egg").get();
        if (!velociraptor.isEmpty()) {
            return velociraptor;
        }
        return stackSupplier("goat_spawn_egg").get();
    });
    public static final CreativeModeTab FOSSILS     = makeSimpleTab("jurassicreborn.fossils", stackSupplier("fauna_fossil_block_item"));
    public static final CreativeModeTab FOODS       = makeTab("jurassicreborn.foods", rotatingIcons(
            "cooked_shark_meat",
            "raw_shark_meat",
            "fun_fries"
    ));
    public static final CreativeModeTab PLANTS      = makeSimpleTab("jurassicreborn.plants", stackSupplier("plant_callus"));

    // --- builders ---
    public static CreativeModeTab makeSimpleTab(String name, Supplier<ItemStack> iconSupplier) {
        return buildTab(name, sanitize(iconSupplier));
    }

    public static CreativeModeTab makeTab(String name, List<Supplier<ItemStack>> iconSuppliers) {
        return buildTab(name, createIconSupplier(name, iconSuppliers));
    }

    private static CreativeModeTab buildTab(String name, Supplier<ItemStack> iconSupplier) {
        Component title = Component.translatable("itemGroup." + name);
        final CreativeModeTab[] holder = new CreativeModeTab[1];

        // 1.19.2: must pass Row & index to builder
        CreativeModeTab tab = CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
                .title(title)
                .icon(iconSupplier)
                .displayItems(new CreativeModeTab.DisplayItemsGenerator() {
                    // 1.19.2: (FeatureFlagSet, Output, boolean)
                    @Override
                    public void accept(FeatureFlagSet flags, CreativeModeTab.Output output, boolean hasPermissions) {
                        populate(holder[0], output);
                    }
                })
                .build();

        holder[0] = tab;

        TAB_TITLES.put(tab, title);
        TAB_ICON_SUPPLIERS.put(tab, iconSupplier);
        return tab;
    }

    private static Supplier<ItemStack> createIconSupplier(String name, List<Supplier<ItemStack>> iconSuppliers) {
        if (iconSuppliers == null || iconSuppliers.isEmpty()) {
            return () -> ItemStack.EMPTY;
        }

        List<Supplier<ItemStack>> sanitized = iconSuppliers.stream()
                .filter(Objects::nonNull)
                .map(TabHandler::sanitize)
                .toList();

        if (sanitized.isEmpty()) {
            return () -> ItemStack.EMPTY;
        }

        if (sanitized.size() == 1) {
            return sanitized.get(0);
        }

        SCROLLING_TAB_ICON_SUPPLIERS.put(name, sanitized);
        return () -> {
            List<Supplier<ItemStack>> suppliers = SCROLLING_TAB_ICON_SUPPLIERS.get(name);
            if (suppliers == null || suppliers.isEmpty()) {
                return ItemStack.EMPTY;
            }

            long now = System.currentTimeMillis();
            long last = SCROLLING_TAB_UPDATE_TIME.getOrDefault(name, 0L);
            int index = SCROLLING_TAB_ICON_INDEX.getOrDefault(name, 0);

            if (now - last >= 5000L) {
                index = (index + 1) % suppliers.size();
                SCROLLING_TAB_ICON_INDEX.put(name, index);
                SCROLLING_TAB_UPDATE_TIME.put(name, now);
            }

            Supplier<ItemStack> current = suppliers.get(Math.min(index, suppliers.size() - 1));
            return current != null ? current.get() : ItemStack.EMPTY;
        };
    }

    // --- Forge events (1.19.2 signatures) ---
    public static void registerCreativeModeTabs(CreativeModeTabEvent.Register event) {
        // On 1.19.2, we don’t have IDs to compare later; the event returns the tab instances.
        // Just re-register each tab with its title/icon and generator.
        for (Map.Entry<CreativeModeTab, Component> e : TAB_TITLES.entrySet()) {
            CreativeModeTab tab = e.getKey();
            Component title = e.getValue();
            Supplier<ItemStack> iconSupplier = TAB_ICON_SUPPLIERS.getOrDefault(tab, () -> ItemStack.EMPTY);

            event.registerCreativeModeTab(new ResourceLocation(JurassicReborn.MODID, extractPath(title.getString())),
                    builder -> builder
                            .title(title)
                            .icon(iconSupplier)
                            .displayItems((flags, output, hasPerms) -> populate(tab, output)));
        }
    }

    public static void fillTabContents(CreativeModeTabEvent.BuildContents event) {
        CreativeModeTab evtTab = event.getTab(); // 1.19.2: this is a CreativeModeTab
        for (CreativeModeTab tab : TAB_TITLES.keySet()) {
            if (evtTab == tab) {
                populate(tab, event::accept);
            }
        }
    }

    private static String extractPath(String key) {
        int i = key.lastIndexOf('.');
        return i >= 0 ? key.substring(i + 1) : key;
    }

    private static void populate(CreativeModeTab tab, CreativeModeTab.Output output) {
        TAB_ITEM_SUPPLIERS.getOrDefault(tab, List.of())
                .stream()
                .map(Supplier::get)
                .forEach(output::accept);
    }

    public static void addToTab(CreativeModeTab tab, Supplier<ItemStack> stackSupplier) {
        TAB_ITEM_SUPPLIERS.computeIfAbsent(tab, k -> new ArrayList<>()).add(stackSupplier);
    }
    public static void addToTab(CreativeModeTab tab, RegistryObject<? extends Item> itemSupplier) {
        addToTab(tab, () -> itemSupplier.get().getDefaultInstance());
    }
    public static void addItemToTab(CreativeModeTab tab, Supplier<? extends Item> itemSupplier) {
        addToTab(tab, () -> new ItemStack(itemSupplier.get()));
    }

    private static Supplier<ItemStack> sanitize(Supplier<ItemStack> supplier) {
        return () -> {
            if (supplier == null) {
                return ItemStack.EMPTY;
            }
            ItemStack stack = supplier.get();
            return stack == null ? ItemStack.EMPTY : stack;
        };
    }

    private static Supplier<ItemStack> stackSupplier(String path) {
        return () -> stackFromRegistry(path);
    }

    private static List<Supplier<ItemStack>> rotatingIcons(String... itemIds) {
        List<Supplier<ItemStack>> suppliers = new ArrayList<>();
        for (String id : itemIds) {
            suppliers.add(stackSupplier(id));
        }
        return suppliers;
    }

    private static ItemStack stackFromRegistry(String path) {
        if (path == null || path.isEmpty()) {
            return ItemStack.EMPTY;
        }
        ResourceLocation id = path.contains(":") ? ResourceLocation.tryParse(path)
                : new ResourceLocation(JurassicReborn.MODID, path);
        if (id == null) {
            return ItemStack.EMPTY;
        }
        Item item = ForgeRegistries.ITEMS.getValue(id);
        if (item == null) {
            return ItemStack.EMPTY;
        }
        ItemStack stack = item.getDefaultInstance();
        return stack == null ? ItemStack.EMPTY : stack;
    }
}