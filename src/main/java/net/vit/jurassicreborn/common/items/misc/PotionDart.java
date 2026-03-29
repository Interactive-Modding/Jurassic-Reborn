package net.vit.jurassicreborn.common.items.misc;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.PotionContents;

import java.util.List;

public class PotionDart extends Dart {

    public PotionDart() {
        super((entity, stack) -> {
            PotionContents contents = stack.get(DataComponents.POTION_CONTENTS);
            if (contents != null) {
                contents.getAllEffects().forEach(effect ->
                        entity.addEffect(new MobEffectInstance(effect))
                );
            }
        }, -1);
    }

    @Override
    public int getDartColor(ItemStack stack) {
        PotionContents contents = stack.get(DataComponents.POTION_CONTENTS);
        return contents != null ? contents.getColor() : 0xFFFFFF;
    }

    @Override
    public void appendHoverText(
            ItemStack stack,
            Item.TooltipContext context,
            List<Component> tooltip,
            TooltipFlag flag
    ) {
        PotionContents contents = stack.get(DataComponents.POTION_CONTENTS);
        if (contents != null) {
            contents.addPotionTooltip(tooltip::add, 1.0F, 1.0F);
        }
    }

}
