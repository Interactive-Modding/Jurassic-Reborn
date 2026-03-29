package net.vit.jurassicreborn.common.items.guns;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.vit.jurassicreborn.common.entities.item.BulletEntity;
import net.vit.jurassicreborn.common.items.ModItems;
import net.vit.jurassicreborn.common.util.ItemStackNbtUtil;

public class Gun extends Item {
    private final int amountPerShot;
    private final float speed;
    private final float inaccuracy;
    private final float pitchOffset; // kept for API compat, not used below
    private final SoundEvent fireSound;
    private final SoundEvent emptySound;
    private final SoundEvent reloadSound;
    private final int clipSize;
    private final int reloadCooldown;
    private final int shotCooldown;
    private final int damage;

    private static final String NBT_BULLET_KEY = "LoadedBullet";

    public Gun(
            int amountPerShot,
            SoundEvent emptySound,
            SoundEvent fireSound,
            SoundEvent reloadSound,
            int clipSize,
            int reloadCooldown,
            int shotCooldown,
            float speed,
            float inaccuracy,
            float pitchOffset,
            int damage
    ) {
        super(new Item.Properties().stacksTo(1));
        this.amountPerShot = amountPerShot;
        this.emptySound = emptySound;
        this.fireSound = fireSound;
        this.reloadSound = reloadSound;
        this.clipSize = clipSize;
        this.reloadCooldown = reloadCooldown;
        this.shotCooldown = shotCooldown;
        this.speed = speed;
        this.inaccuracy = inaccuracy;
        this.pitchOffset = pitchOffset;
        this.damage = damage;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack gunStack = player.getItemInHand(hand);

        // Respect cooldowns
        if (player.getCooldowns().isOnCooldown(this)) {
            return InteractionResultHolder.pass(gunStack);
        }

        // Client: optimistic success; server does the work
        if (level.isClientSide) {
            return InteractionResultHolder.success(gunStack);
        }

        ItemStack clip = getLoadedBullet(player.level().registryAccess(), gunStack);

        // Reload if empty
        if (clip.isEmpty()) {
            ItemStack invAmmo = findBulletInInventory(player);

            if (invAmmo.isEmpty() && !player.getAbilities().instabuild) {
                play(level, player, emptySound, 1f, 1f);
                return InteractionResultHolder.fail(gunStack);
            }

            int toLoad = player.getAbilities().instabuild ? clipSize : Math.min(invAmmo.getCount(), clipSize);
            if (toLoad <= 0) {
                play(level, player, emptySound, 1f, 1f);
                return InteractionResultHolder.fail(gunStack);
            }

            ItemStack newClip = player.getAbilities().instabuild
                    ? new ItemStack(ModItems.BULLET.get(), clipSize)
                    : copyWithCount(invAmmo, toLoad);

            setLoadedBullet(player.level().registryAccess(), gunStack, newClip);

            if (!player.getAbilities().instabuild) invAmmo.shrink(toLoad);

            // 🔊 RELOAD sound (only)
            play(level, player, reloadSound, 1f, 1f);
            player.getCooldowns().addCooldown(this, reloadCooldown);
            return InteractionResultHolder.success(gunStack);
        }

        // Fire
        for (int i = 0; i < amountPerShot; i++) {
            shootBullet(player, level, hand, clip);

            if (!player.getAbilities().instabuild) {
                clip.shrink(1);
                if (clip.isEmpty()) break;
            }
        }

        setLoadedBullet(player.level().registryAccess(), gunStack, clip.isEmpty() ? ItemStack.EMPTY : clip);

        // 🔊 FIRE sound
        play(level, player, fireSound, 1f, 1f);
        player.getCooldowns().addCooldown(this, shotCooldown);
        player.awardStat(Stats.ITEM_USED.get(this));
        return InteractionResultHolder.success(gunStack);
    }

    /** Spawn one bullet with correct origin/aim. */
    private void shootBullet(Player shooter, Level level, InteractionHand hand, ItemStack ammoStack) {
        BulletEntity bullet = new BulletEntity(level, shooter, ammoStack.copy());
        bullet.setOwner(shooter);
        bullet.setDamage(this.damage);

        bullet.setSilent(true);
        bullet.setNoGravity(true);

        // Spawn from eye with a small forward offset to avoid self-collision
        Vec3 look = shooter.getViewVector(1.0F);
        Vec3 eye = shooter.getEyePosition(1.0F);
        double spawnOffset = 0.5D; // half-block forward
        bullet.moveTo(
                eye.x + look.x * spawnOffset,
                eye.y - 0.1D + look.y * spawnOffset,
                eye.z + look.z * spawnOffset,
                shooter.getYRot(),
                shooter.getXRot()
        );

        // Shoot in the look direction (no odd pitch/yaw offsets)
        bullet.shoot(look.x, look.y, look.z, this.speed, this.inaccuracy);

        level.addFreshEntity(bullet);
    }

    private ItemStack findBulletInInventory(Player player) {
        for (ItemStack stack : player.getInventory().items) {
            if (!stack.isEmpty() && stack.is(ModItems.BULLET.get())) return stack;
        }
        return ItemStack.EMPTY;
    }

    private ItemStack getLoadedBullet(HolderLookup.Provider provider, ItemStack gun) {
        CompoundTag tag = ItemStackNbtUtil.getTag(gun);
        if (tag != null && tag.contains(NBT_BULLET_KEY)) {
            ItemStack stack = ItemStack.parseOptional(provider, tag.getCompound(NBT_BULLET_KEY));
            if (!stack.isEmpty() && stack.is(ModItems.BULLET.get())) return stack;
        }
        return ItemStack.EMPTY;
    }

    private void setLoadedBullet(HolderLookup.Provider provider, ItemStack gun, ItemStack bulletStack) {
        if (bulletStack.isEmpty()) {
            if (ItemStackNbtUtil.hasTag(gun)) {
                CompoundTag t = ItemStackNbtUtil.getTag(gun);
                t.remove(NBT_BULLET_KEY);
                ItemStackNbtUtil.setTag(gun, t.isEmpty() ? null : t);
            }
        } else {
            CompoundTag tag = ItemStackNbtUtil.getOrCreateTag(gun);
            tag.put(NBT_BULLET_KEY, bulletStack.save(provider));
            ItemStackNbtUtil.setTag(gun, tag);
        }
    }

    private static ItemStack copyWithCount(ItemStack src, int count) {
        ItemStack copy = src.copy();
        copy.setCount(count);
        return copy;
    }

    private static void play(Level level, Player player, SoundEvent sound, float vol, float pitch) {
        if (sound != null) {
            level.playSound(null, player.getX(), player.getY(), player.getZ(), sound, SoundSource.PLAYERS, vol, pitch);
        }
    }
}
