package net.vit.jurassicreborn.common.items.misc;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.vit.jurassicreborn.common.entities.item.BlueprintEntity;
import net.vit.jurassicreborn.common.entities.ModEntities;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;

public class BlueprintItem extends Item {
    public static final String NBT_TYPE = "BlueprintType";

    public BlueprintItem(Properties props) {
        super(props);
    }

    public static void setType(ItemStack stack, BlueprintEntity.Type type) {
        stack.getOrCreateTag().putString(NBT_TYPE, type.name());
    }

    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        Direction face = ctx.getClickedFace();
        if (!face.getAxis().isHorizontal()) {
            return InteractionResult.PASS;
        }

        Level level = ctx.getLevel();
        ItemStack stack = ctx.getItemInHand();
        BlueprintEntity.Type type = getStoredType(stack);

        // Hang ON the clicked face, like paintings
        BlockPos anchor = ctx.getClickedPos().relative(face);

        BlueprintEntity entity = new BlueprintEntity(
                ModEntities.BLUEPRINT.get(),
                level,
                anchor,
                face,
                type
        );

        if (!entity.survives()) {
            return InteractionResult.FAIL;
        }

        if (!level.isClientSide) {
            level.addFreshEntity(entity);
            entity.playPlacementSound();
            Player player = ctx.getPlayer();
            if (player == null || !player.getAbilities().instabuild) {
                stack.shrink(1);
            }
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        BlueprintEntity.Type[] values = BlueprintEntity.Type.values();
        if (values.length == 0) {
            return InteractionResultHolder.pass(stack);
        }

        BlueprintEntity.Type current = getStoredType(stack);
        int nextIndex = (current.ordinal() + 1) % values.length;
        BlueprintEntity.Type next = values[nextIndex];
        setType(stack, next);

        if (level.isClientSide) {
            player.displayClientMessage(new TextComponent("Blueprint set to: " + displayName(next)), true);
        }

        player.swing(hand);
        return InteractionResultHolder.success(stack);
    }
            @Override
            public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> lines, TooltipFlag flag) {
                BlueprintEntity.Type type = getStoredType(stack);
                lines.add(new TextComponent("Design: " + displayName(type)).withStyle(ChatFormatting.AQUA));
                lines.add(new TextComponent("Right-click in the air to change design").withStyle(ChatFormatting.GRAY));
                lines.add(new TextComponent("Note: Hitbox is 1x1 block!").withStyle(ChatFormatting.GRAY));
            }

    private BlueprintEntity.Type getStoredType(ItemStack stack) {
        if (stack.hasTag() && stack.getTag().contains(NBT_TYPE)) {
            try {
                return BlueprintEntity.Type.valueOf(stack.getTag().getString(NBT_TYPE));
            } catch (IllegalArgumentException ignored) {}
        }
        BlueprintEntity.Type fallback = BlueprintEntity.Type.TYRANNOSAURUS;
        setType(stack, fallback);
        return fallback;
    }

    private static String displayName(BlueprintEntity.Type type) {
        String lower = type.name().toLowerCase(Locale.ROOT).replace('_', ' ');
        return Character.toUpperCase(lower.charAt(0)) + lower.substring(1);
    }
}
