package net.vit.jurassicreborn.mixin;

import net.vit.jurassicreborn.client.JurassicClient;
import net.vit.jurassicreborn.client.render.block.DisplayBlockRendererWithoutLevel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DisplayBlockRendererWithoutLevel.class)
public class DisplayBlockMixin {
    private static boolean executed = false;
    @Inject(method = "<init>", at = @At("RETURN"))
    private void initDisplayBlockRendererMixin(BlockEntityRenderDispatcher pBlockEntityRenderDispatcher, EntityModelSet pEntityModelSet, CallbackInfo ci){
        if(!executed) {
            JurassicClient.displayBlockRendererWithoutLevel = new DisplayBlockRendererWithoutLevel(pBlockEntityRenderDispatcher, pEntityModelSet);
            executed = true;
        }

    }
}
