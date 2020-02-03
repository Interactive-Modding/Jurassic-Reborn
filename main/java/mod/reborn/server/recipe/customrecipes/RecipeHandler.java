package mod.reborn.server.recipe.customrecipes;

import mod.reborn.RebornMod;

import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber(modid= RebornMod.MODID)
public class RecipeHandler {
    @SubscribeEvent
    public static void onRecipeReg(RegistryEvent.Register<IRecipe> event) {
	event.getRegistry().registerAll(new RecipeDartTippedPotion().setRegistryName("dart_tipped_potion"));
    }
}
