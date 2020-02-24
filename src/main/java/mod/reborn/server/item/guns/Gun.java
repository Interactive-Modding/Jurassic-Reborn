package mod.reborn.server.item.guns;

import com.google.common.collect.Lists;
import mod.reborn.server.entity.item.BulletEntity;
import mod.reborn.server.item.ItemHandler;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.util.*;
import net.minecraft.world.World;

import java.util.Collections;
import java.util.List;

public class Gun extends Item {
    private int amount;
    private float speed;
    private float inaccuracy;
    private float pitchoffset;
    private SoundEvent fire;
    private SoundEvent empty;
    private SoundEvent reloadsound;
    private int reloadcooldown;
    private int shotcooldown;
    private int damage;
    public Gun(CreativeTabs creativeTab, int AmountPerShot, SoundEvent EmptySound, SoundEvent FireSound, SoundEvent ReloadSound, int clipSize, int reloadcooldown, int shotcooldown, float speed, float inaccuracy, float pitchofsset, int damage) {
        this.setCreativeTab(creativeTab);
        this.setMaxStackSize(1);
        this.amount = AmountPerShot;
        this.fire = FireSound;
        this.empty = EmptySound;
        this.reloadsound = ReloadSound;
        this.setClipSize(clipSize);
        this.reloadcooldown = reloadcooldown;
        this.shotcooldown = shotcooldown;
        this.speed = speed;
        this.inaccuracy = inaccuracy;
        this.pitchoffset = pitchofsset;
        this.damage = damage;
    }

    private void setClipSize(int clipSize) {
        MAX_CARRY_SIZE = clipSize;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public float setSpeed(float speed) {
        return this.speed = speed;
    }

    public float getSpeed() {
        return speed;
    }

    public int getAmount() {
        return amount;
    }

    public SoundEvent getFireSound() {
        return this.fire;
    }

    public SoundEvent getEmptySound() {
        return empty;
    }

    public SoundEvent getReloadsound() {
        return this.reloadsound;
    }

    private static int MAX_CARRY_SIZE;

    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        SoundEvent event = null;
        int amount = this.amount;

        ItemStack bullet = getBullet(itemstack);
        if(bullet.isEmpty()) {
            List<Slot> list = Lists.newArrayList(playerIn.inventoryContainer.inventorySlots);
            Collections.reverse(list);
            if(setBullet(itemstack,
                    list.stream()
                            .map(Slot::getStack)
                            .filter(stack -> stack.getItem() == ItemHandler.BULLET)
                            .findFirst()
                            .orElse(ItemStack.EMPTY))) {
                event = this.getReloadsound();
                this.setCooldown(playerIn, this, reloadcooldown);
            } else if (bullet.getCount() <= this.getAmount()) {
                event = this.getEmptySound();
            }
        } else if (!worldIn.isRemote) {
                for (int i = 0; i < this.getAmount(); i++) {
                    this.shoot(playerIn, worldIn, handIn, amount);
                    if (!playerIn.capabilities.isCreativeMode) {
                        bullet.shrink(1);
                    }
                }
            this.setCooldown(playerIn, this, 15);
            setBullet(itemstack, bullet);
            event = this.getFireSound();
        }

        if(event != null) {
            worldIn.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ, event, SoundCategory.NEUTRAL, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
        }


        playerIn.addStat(StatList.getObjectUseStats(this));
        return new ActionResult(EnumActionResult.SUCCESS, itemstack);
    }

    public void shoot(EntityPlayer playerIn, World worldIn, EnumHand handIn, int amount) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        ItemStack bullet = getBullet(itemstack);
        BulletEntity bulletEntity = new BulletEntity(worldIn, playerIn, bullet);
        bulletEntity.setDamage(this.damage);
        bulletEntity.shoot(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, pitchoffset, this.speed, this.inaccuracy);
        worldIn.spawnEntity(bulletEntity);
    }

    public static void setCooldown(EntityPlayer player, Item item, int cooldown) {
        player.getCooldownTracker().setCooldown(item, cooldown);
    }

    public static ItemStack getBullet(ItemStack gun) {
        NBTTagCompound nbt = gun.getOrCreateSubCompound("bullets");
        ItemStack stack = new ItemStack(nbt.getCompoundTag("itemstack"));
        stack.setCount(Math.min(stack.getCount(), MAX_CARRY_SIZE));
        return stack;
    }

    public static boolean setBullet(ItemStack gun, ItemStack bullet) {
        boolean hadItem = !bullet.isEmpty();
        ItemStack bullet2 = bullet.splitStack(MAX_CARRY_SIZE);
        gun.getOrCreateSubCompound("bullets").setTag("itemstack", bullet2.serializeNBT());
        return hadItem;
    }
}
