package net.vit.jurassicreborn.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.vit.jurassicreborn.JurassicReborn;
import net.minecraft.nbt.CompoundTag;
import net.vit.jurassicreborn.common.items.misc.DinosaurSpawnEggItem;
import net.vit.jurassicreborn.common.items.ModItems;
import net.minecraftforge.registries.RegistryObject;

import java.util.Collection;

@Mod.EventBusSubscriber(
        modid = JurassicReborn.MODID,
        bus   = Mod.EventBusSubscriber.Bus.MOD,
        value = Dist.CLIENT
)
public class SpawnEggColorHandler {

    @SubscribeEvent
    public static void onItemColors(ColorHandlerEvent.Item event) {
        ItemColors colors = event.getItemColors();
        Collection<RegistryObject<DinosaurSpawnEggItem>> eggs = ModItems.DINO_SPAWN_EGGS.values();
        for (RegistryObject<DinosaurSpawnEggItem> egg : eggs) {
            DinosaurSpawnEggItem item = egg.get();
            colors.register(
                    (ItemStack stack, int tintIndex) -> {
                        CompoundTag tag = stack.getOrCreateTag();
                        int gender = tag.contains("GenderMode") ? tag.getInt("GenderMode") : 0;
                        if (gender == 0) {
                            var level = Minecraft.getInstance().level;
                            long time = level == null ? System.currentTimeMillis() / 50 : level.getGameTime();
                            gender = (int) ((time / 20) % 2) + 1;
                        }
                        if (tintIndex == 0) {
                            return (gender == 2)
                                    ? item.getDinosaur().getEggPrimaryColorFemale()
                                    : item.getDinosaur().getEggPrimaryColorMale();
                        } else if (tintIndex == 1) {
                            return (gender == 2)
                                    ? item.getDinosaur().getEggSecondaryColorFemale()
                                    : item.getDinosaur().getEggSecondaryColorMale();
                        }
                        return 0xFFFFFF;
                    },
                    item
            );
        }
    }
}
