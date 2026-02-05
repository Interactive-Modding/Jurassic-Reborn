package net.vit.jurassicreborn.client.render.block;

import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.world.item.BlockItem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.common.blocks.ModBlocks;
import net.vit.jurassicreborn.common.blocks.parkBlocks.TourRailBlock;

@Mod.EventBusSubscriber(modid = JurassicReborn.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class TourRailColorHandler {

    @SubscribeEvent
    public static void onBlockColors(RegisterColorHandlersEvent.Block event) {
        BlockColors colors = event.getBlockColors();
        colors.register((state, level, pos, tint) -> {
                    if (state.getBlock() instanceof TourRailBlock block && tint == 1) {
                        return block.getSpeedType().getColor();
                    }
                    return 0xFFFFFF;
                },
                ModBlocks.TOUR_RAIL.get(),
                ModBlocks.TOUR_RAIL_MEDIUM.get(),
                ModBlocks.TOUR_RAIL_SLOW.get(),
                ModBlocks.TOUR_RAIL_FAST.get());
    }

    @SubscribeEvent
    public static void onItemColors(RegisterColorHandlersEvent.Item event) {
        ItemColors colors = event.getItemColors();
        colors.register((stack, tint) -> {
                    if (stack.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof TourRailBlock block && tint == 1) {
                        return block.getSpeedType().getColor();
                    }
                    return 0xFFFFFF;
                },
                ModBlocks.TOUR_RAIL.get(),
                ModBlocks.TOUR_RAIL_MEDIUM.get(),
                ModBlocks.TOUR_RAIL_SLOW.get(),
                ModBlocks.TOUR_RAIL_FAST.get());
    }
}
