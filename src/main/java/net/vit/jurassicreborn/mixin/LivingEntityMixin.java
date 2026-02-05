package net.vit.jurassicreborn.mixin;

import net.vit.jurassicreborn.common.blocks.parkBlocks.TourRailBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockStateProperties.class)
public class LivingEntityMixin {
//    static CompoundTag f_135042_ = new CompoundTag();

    @Inject(method = "<clinit>", at = @At("RETURN"))
    private static void mmm(CallbackInfo ci){
        TourRailBlock.SHAPE.getName();
    }
}
