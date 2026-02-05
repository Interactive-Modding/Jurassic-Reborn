package net.vit.jurassicreborn.common.items.misc;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PotionDart extends Dart {

    public PotionDart() {
        // Applies all potion effects to the entity
        super((entity, stack) -> PotionUtils.getMobEffects(stack).forEach(effect -> entity.addEffect(effect)), -1);
    }

    @Override
    public int getDartColor(ItemStack stack) {
        return PotionUtils.getColor(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flag) {
        PotionUtils.addPotionTooltip(stack, tooltip, 1.0F);
    }
}
