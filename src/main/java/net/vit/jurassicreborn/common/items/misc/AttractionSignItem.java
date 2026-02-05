package net.vit.jurassicreborn.common.items.misc;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.vit.jurassicreborn.common.entities.item.AttractionSignEntity;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;

public class AttractionSignItem extends Item {
    private final AttractionSignEntity.AttractionSignType type;

    public AttractionSignItem(AttractionSignEntity.AttractionSignType type, Properties props) {
        super(props);
        this.type = type;
    }

    public AttractionSignEntity.AttractionSignType getType() {
        return this.type;
    }

    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        Level world = ctx.getLevel();
        BlockPos pos = ctx.getClickedPos();
        Direction face = ctx.getClickedFace();
        ItemStack stack = ctx.getItemInHand();

        // only horizontal walls
        if (!face.getAxis().isHorizontal()) {
            return InteractionResult.PASS;
        }

        BlockPos spawn = pos.relative(face);
        var entity = new AttractionSignEntity(world, spawn, face, this.type);
        if (entity.survives()) {
            if (!world.isClientSide) {
                world.addFreshEntity(entity);
                stack.shrink(1);
            }
            return InteractionResult.sidedSuccess(world.isClientSide());
        }
        return InteractionResult.FAIL;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("Note: Hitbox is 1x1 block!").withStyle(ChatFormatting.GRAY));
        // or for vanilla color:
        // tooltip.add(Component.translatable("tooltip.jurassicreborn.sign_hitbox_small"));
        super.appendHoverText(stack, world, tooltip, flag);
    }

    @Override
    public Component getName(ItemStack stack) {
        String subKey = "attraction_sign." + this.type.name().toLowerCase(Locale.ROOT) + ".name";
        Component subtypeName = Component.translatable(subKey);
        return Component.translatable("item.attraction_sign.name", subtypeName);
    }
}