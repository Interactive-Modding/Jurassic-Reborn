package net.vit.jurassicreborn.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public class PlayerShoulderMixin {

    @Inject(method = "removeEntitiesOnShoulder", at = @At("HEAD"), cancellable = true)
    private void jurassicreborn$keepMicroraptorOnShoulder(CallbackInfo ci) {
        Player player = (Player) (Object) this;
        if (hasMicroraptor(player.getShoulderEntityLeft()) || hasMicroraptor(player.getShoulderEntityRight())) {
            ci.cancel();
        }
    }

    private static boolean hasMicroraptor(CompoundTag tag) {
        return !tag.isEmpty() && tag.getString("id").contains("microraptor");
    }
}
