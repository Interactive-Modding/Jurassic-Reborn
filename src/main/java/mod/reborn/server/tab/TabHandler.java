package mod.reborn.server.tab;

import mod.reborn.server.items.ItemHandler;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class TabHandler {
    public static final ItemGroup ITEMS = new ItemGroup("rebornmod.items") {
        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(ItemHandler.AMBER);
        }
    };

    public static final ItemGroup BLOCKS = new ItemGroup("rebornmod.blocks") {
        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(Item.getItemFromBlock(BlockHandler.GYPSUM_BRICKS));
        }
    };

    public static final RebornDNATab DNA = new RebornDNATab("rebornmod.dna");
    public static final RebornFossilTab FOSSILS = new RebornFossilTab("rebornmod.fossils");
    public static final RebornPlantTab PLANTS = new RebornPlantTab("rebornmod.plants");
    public static final RebornFoodTab FOODS = new RebornFoodTab("rebornmod.foods");
    public static final RebornDecorationsTab DECORATIONS = new RebornDecorationsTab("rebornmod.decorations");

    public static final ItemGroup EGGS = new ItemGroup("rebornmod.eggs") {
        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(ItemHandler.SPAWN_EGG);
        }
    };

    public static final ItemGroup CREATIVE = new ItemGroup("rebornmod.creative") {
        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(ItemHandler.BIRTHING_WAND);
        }

        @Override
        public boolean hasSearchBar() {
            return super.hasSearchBar();
        }
    };
}
