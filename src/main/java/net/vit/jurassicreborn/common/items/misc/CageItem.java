package net.vit.jurassicreborn.common.items.misc;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.vit.jurassicreborn.common.entities.item.CageEntity;

import java.util.List;
import net.vit.jurassicreborn.common.util.ItemStackNbtUtil;

/**
 * Item capable of capturing most non-boss land entities.
 */
public class CageItem extends Item {

    public CageItem() {
        super(new Item.Properties().stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!world.isClientSide) {
            CompoundTag tag = ItemStackNbtUtil.getTag(stack);
            if (tag != null && tag.contains("EntityTag")) {
                CompoundTag entityTag = tag.getCompound("EntityTag");
                Entity released = net.minecraft.world.entity.EntityType.loadEntityRecursive(entityTag, world, e -> e);
                if (released != null) {
                    released.moveTo(player.blockPosition().getX(), player.blockPosition().getY(), player.blockPosition().getZ(), released.getYRot(), released.getXRot());
                    world.addFreshEntity(released);
                    ItemStackNbtUtil.setTag(stack, null);
                    return InteractionResultHolder.success(stack);
                }
            } else {
                Vec3 eyePos = player.getEyePosition(1.0F);
                Vec3 look = player.getLookAngle();
                Vec3 reach = eyePos.add(look.scale(5.0D));
                HitResult result = rayTraceEntities(world, player, eyePos, reach);
                if (result instanceof EntityHitResult hit) {
                    Entity entity = hit.getEntity();
                    if (entity instanceof LivingEntity && !(entity instanceof Player)) {
                        CageEntity.captureEntity((LivingEntity) entity, stack);
                        return InteractionResultHolder.success(stack);
                    }
                }
            }
        }
        return InteractionResultHolder.pass(stack);
    }

    private HitResult rayTraceEntities(Level world, Player player, Vec3 start, Vec3 end) {
        List<Entity> entities = world.getEntities(player, player.getBoundingBox().inflate(5.0D));
        for (Entity entity : entities) {
            if (entity.getBoundingBox().inflate(0.3F).clip(start, end).isPresent()) {
                return new EntityHitResult(entity);
            }
        }
        return null;
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        CompoundTag tag = ItemStackNbtUtil.getTag(stack);
        if (tag != null) {
            if (tag.contains("name")) {
                tooltip.add(Component.translatable("tooltip.cage.stored").append(tag.getString("name")).withStyle(ChatFormatting.GREEN));
            } else if (tag.contains("EntityTag") && tag.getCompound("EntityTag").contains("id")) {
                tooltip.add(Component.translatable("tooltip.cage.stored").append(tag.getCompound("EntityTag").getString("id")).withStyle(ChatFormatting.RED));
            }
        } else {
            tooltip.add(Component.translatable("tooltip.cage.stored").append(Component.translatable("cage.empty")).withStyle(ChatFormatting.RED));
        }
    }
}
