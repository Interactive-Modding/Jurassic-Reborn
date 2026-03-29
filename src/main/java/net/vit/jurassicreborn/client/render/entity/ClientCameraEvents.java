package net.vit.jurassicreborn.client.render.entity;

import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.CalculateDetachedCameraDistanceEvent;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.client.render.RenderingHandler;
import net.vit.jurassicreborn.common.entities.vehicle.HelicopterEntity;

@EventBusSubscriber(modid = JurassicReborn.MODID, value = Dist.CLIENT)
public final class ClientCameraEvents {
    @SubscribeEvent
    public static void onCalculateDetachedCameraDistance(CalculateDetachedCameraDistanceEvent event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player != null && minecraft.player.getVehicle() instanceof HelicopterEntity) {
            event.setDistance((float) RenderingHandler.INSTANCE.getThirdPersonViewDistance());
        }
    }
}