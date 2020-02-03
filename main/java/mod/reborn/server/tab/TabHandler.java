package mod.reborn.server.tab;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import mod.reborn.server.block.BlockHandler;
import mod.reborn.server.item.ItemHandler;

public class TabHandler {
    public static final CreativeTabs ITEMS = new CreativeTabs("rebornmod.items") {
        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(ItemHandler.AMBER);
        }
    };

    public static final CreativeTabs BLOCKS = new CreativeTabs("rebornmod.blocks") {
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

    public static final CreativeTabs EGGS = new CreativeTabs("rebornmod.eggs") {
        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(ItemHandler.SPAWN_EGG);
        }
    };

    public static final CreativeTabs CREATIVE = new CreativeTabs("rebornmod.creative") {
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
