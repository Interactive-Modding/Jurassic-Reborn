package net.vit.jurassicreborn.common.items.misc;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.vit.jurassicreborn.common.entities.item.MuralEntity;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class MuralItem extends Item {
    public MuralItem(Properties props) {
        super(props);
    }

    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        Direction face = ctx.getClickedFace();
        if (!face.getAxis().isHorizontal()) {
            return InteractionResult.PASS;
        }

        Level level = ctx.getLevel();
        List<MuralEntity.Type> options = new ArrayList<>();

        for (MuralEntity.Type type : MuralEntity.Type.values()) {
            MuralEntity mural = new MuralEntity(level, ctx.getClickedPos(), face, type);
            if (mural.survives()) {
                options.add(type);
            }
        }

        if (options.isEmpty()) {
            return InteractionResult.PASS;
        }

        MuralEntity.Type chosen = options.get(level.random.nextInt(options.size()));
        MuralEntity mural = new MuralEntity(level, ctx.getClickedPos(), face, chosen);

        if (!level.isClientSide) {
            level.addFreshEntity(mural);
            mural.playPlacementSound();
            Player player = ctx.getPlayer();
            ItemStack stack = ctx.getItemInHand();
            if (player == null || !player.getAbilities().instabuild) {
                stack.shrink(1);
            }
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(new TextComponent("Note: Hitbox is 1x1 block!").withStyle(ChatFormatting.GRAY));
        super.appendHoverText(stack, world, tooltip, flag);
    }
}
