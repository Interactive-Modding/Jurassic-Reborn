package net.vit.jurassicreborn.mixin;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.vit.jurassicreborn.client.render.RenderingHandler;
import net.vit.jurassicreborn.common.entities.vehicle.HelicopterEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(Camera.class)
public abstract class CameraMixin {
    @ModifyArg(
            method = "setup",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/Camera;getMaxZoom(F)F"
            ),
            index = 0
    )
    private float jurassicreborn$useHelicopterZoomDistance(float vanillaDistance) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player != null && minecraft.player.getVehicle() instanceof HelicopterEntity) {
            return (float) RenderingHandler.INSTANCE.getThirdPersonViewDistance();
        }
        return vanillaDistance;
    }
}