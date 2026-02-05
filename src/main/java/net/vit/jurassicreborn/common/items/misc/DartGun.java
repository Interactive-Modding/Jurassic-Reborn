package net.vit.jurassicreborn.common.items.misc;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.vit.jurassicreborn.common.entities.item.TrackingDartEntity;
import net.vit.jurassicreborn.common.entities.item.TranquilizerDartEntity;

/**
 * Shoots either a TranquilizerDartEntity or a TrackingDartEntity
 * depending on which Dart is in the inventory
 */
public class DartGun extends Item {

    public DartGun() {
        super(new Item.Properties().stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack gunStack = player.getItemInHand(hand);

        // Find a dart in the player's inventory (hotbar first)
        ItemStack dartStack = findDart(player);
        if (dartStack.isEmpty()) {
            level.playSound(player, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.LEVER_CLICK, SoundSource.PLAYERS, 1.0F, 1.0F);
            return InteractionResultHolder.fail(gunStack);
        }

        if (!level.isClientSide) {
            ItemStack dartCopy = dartStack.copy();
            dartCopy.setCount(1);

            LivingEntity thrower = player;
            if (dartCopy.getItem() instanceof TrackerDart) {
                TrackingDartEntity dartEntity = new TrackingDartEntity(level, thrower, dartCopy);
                dartEntity.shootFromRotation(thrower, thrower.getXRot(), thrower.getYRot(), 0.0F, 2.5F, 1.0F);
                level.addFreshEntity(dartEntity);

            } else {
                TranquilizerDartEntity dartEntity = new TranquilizerDartEntity(level, thrower, dartCopy);
                dartEntity.shootFromRotation(thrower, thrower.getXRot(), thrower.getYRot(), 0.0F, 2.5F, 1.0F);
                level.addFreshEntity(dartEntity);
            }

            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F);

            if (!player.getAbilities().instabuild) {
                dartStack.shrink(1);
            }
            player.awardStat(Stats.ITEM_USED.get(this));
        }

        return InteractionResultHolder.sidedSuccess(gunStack, level.isClientSide());
    }

    /** Find the first ItemStack in the player's inventory that is a Dart */
    private ItemStack findDart(Player player) {
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (!stack.isEmpty() && stack.getItem() instanceof Dart) {
                return stack;
            }
        }
        return ItemStack.EMPTY;
    }
}
