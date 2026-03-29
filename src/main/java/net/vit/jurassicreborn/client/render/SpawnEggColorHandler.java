package net.vit.jurassicreborn.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.registries.DeferredItem;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.common.datagen.data.ModDataComponent;
import net.vit.jurassicreborn.common.items.misc.DinosaurSpawnEggItem;
import net.vit.jurassicreborn.common.items.ModItems;

import java.util.Collection;

@EventBusSubscriber(
        modid = JurassicReborn.MODID,
        bus   = EventBusSubscriber.Bus.MOD,
        value = Dist.CLIENT
)
public class SpawnEggColorHandler {

    @SubscribeEvent
    public static void onItemColors(RegisterColorHandlersEvent.Item event) {
        ItemColors colors = event.getItemColors();
        Collection<DeferredItem<DinosaurSpawnEggItem>> eggs = ModItems.DINO_SPAWN_EGGS.values();

        for (DeferredItem<DinosaurSpawnEggItem> egg : eggs) {
            DinosaurSpawnEggItem item = egg.get();

            colors.register((ItemStack stack, int tintIndex) -> {

                // 0 = animated fallback, 1 = male, 2 = female
                int gender = stack.getOrDefault(
                        ModDataComponent.GENDER_MODE.get(), 0
                );

                // Animated fallback (creative / unset stacks)
                if (gender == 0) {
                    var level = Minecraft.getInstance().level;
                    long time = level == null
                            ? System.currentTimeMillis() / 50
                            : level.getGameTime();
                    gender = (int) ((time / 20) % 2) + 1;
                }

                // PRIMARY LAYER
                if (tintIndex == 0) {
                    int rgb = (gender == 2)
                            ? item.getDinosaur().getEggPrimaryColorFemale()
                            : item.getDinosaur().getEggPrimaryColorMale();

                    return 0xFF000000 | rgb; // FORCE OPAQUE
                }

                // SECONDARY LAYER
                if (tintIndex == 1) {
                    int rgb = (gender == 2)
                            ? item.getDinosaur().getEggSecondaryColorFemale()
                            : item.getDinosaur().getEggSecondaryColorMale();

                    return 0xFF000000 | rgb; // FORCE OPAQUE
                }

                // IMPORTANT: let vanilla render untouched layers
                return -1;
            }, item);
        }
    }
}
