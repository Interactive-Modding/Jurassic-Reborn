package mod.reborn.server.recipe;

import mod.reborn.server.block.BlockHandler;
import mod.reborn.server.block.tree.TreeType;
import mod.reborn.server.entity.EntityHandler;
import mod.reborn.server.item.ItemHandler;
import mod.reborn.server.dinosaur.Dinosaur;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;


public class SmeltingRecipeHandler {
    public static void init() {
        for (Dinosaur dinosaur : EntityHandler.getRegisteredDinosaurs()) {
            int id = EntityHandler.getDinosaurId(dinosaur);
            GameRegistry.addSmelting(new ItemStack(ItemHandler.DINOSAUR_MEAT, 1, id), new ItemStack(ItemHandler.DINOSAUR_STEAK, 1, id), 5F);
        }
        
        for (TreeType type : TreeType.values()) {
            GameRegistry.addSmelting(BlockHandler.ANCIENT_LOGS.get(type), new ItemStack(Items.COAL, 1, 1), 0.15F);
        }
        
        GameRegistry.addSmelting(new ItemStack(BlockHandler.GYPSUM_COBBLESTONE), new ItemStack(BlockHandler.GYPSUM_STONE), 1.5F);
        GameRegistry.addSmelting(new ItemStack(Items.POTIONITEM, 1, 0), new ItemStack(ItemHandler.DNA_NUCLEOTIDES), 1.0F);
        GameRegistry.addSmelting(ItemHandler.GRACILARIA, new ItemStack(ItemHandler.LIQUID_AGAR), 0.5F);

        GameRegistry.addSmelting(ItemHandler.OILED_POTATO_STRIPS, new ItemStack(ItemHandler.FUN_FRIES), 0.5F);

        GameRegistry.addSmelting(ItemHandler.WILD_POTATO, new ItemStack(ItemHandler.WILD_POTATO_COOKED), 0.5F);
        GameRegistry.addSmelting(ItemHandler.GOAT_RAW, new ItemStack(ItemHandler.GOAT_COOKED), 0.5F);
    }
}
