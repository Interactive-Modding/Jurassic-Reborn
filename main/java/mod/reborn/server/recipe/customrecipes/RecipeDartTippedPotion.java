package mod.reborn.server.recipe.customrecipes;

import mod.reborn.server.item.ItemHandler;

import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class RecipeDartTippedPotion extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe
{
    @Override
    public boolean matches(InventoryCrafting inv, World worldIn) {
        if (inv.getWidth() == 3 && inv.getHeight() == 3) {
            for (int i = 0; i < inv.getWidth(); ++i) {
                for (int j = 0; j < inv.getHeight(); ++j) {
                    ItemStack itemstack = inv.getStackInRowAndColumn(i, j);
                    if (itemstack.isEmpty()) {
                        return false;
                    }
                    Item item = itemstack.getItem();
                    if (i == 1 && j == 1) {
                        if (item != Items.POTIONITEM){
                            return false;
                        }
                    } else if (item != ItemHandler.DART_TRANQUILIZER) {
                        return false;
                    }
                }
            }

            return true;
        } else {
            return false;
        }
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        ItemStack itemstack = inv.getStackInRowAndColumn(1, 1);

        if (itemstack.getItem() != Items.POTIONITEM) {
            return ItemStack.EMPTY;
        }
        else {
            ItemStack itemstack1 = new ItemStack(ItemHandler.DART_TIPPED_POTION, 8);
            PotionUtils.addPotionToItemStack(itemstack1, PotionUtils.getPotionFromItem(itemstack));
            PotionUtils.appendEffects(itemstack1, PotionUtils.getFullEffectsFromItem(itemstack));
            return itemstack1;
        }
    }

    @Override
    public ItemStack getRecipeOutput() {
        return ItemStack.EMPTY;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
        return NonNullList.<ItemStack>withSize(inv.getSizeInventory(), ItemStack.EMPTY);
    }

    @Override
    public boolean isDynamic() {
        return true;
    }

    @Override
    public boolean canFit(int width, int height)
    {
        return width >= 3 && height >= 3;
    }
}