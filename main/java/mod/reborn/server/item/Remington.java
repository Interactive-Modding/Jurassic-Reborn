package mod.reborn.server.item;

import com.google.common.collect.Lists;
import mod.reborn.server.entity.item.BulletEntity;
import mod.reborn.server.tab.TabHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.util.*;
import net.minecraft.world.World;

import java.util.Collections;
import java.util.List;

public class Remington extends Item {

    public Remington() {
        this.setCreativeTab(TabHandler.ITEMS);
        this.setMaxStackSize(1);
    }

    private static final int MAX_CARRY_SIZE = 1; //TODO config ?

    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        SoundEvent event = null;

        ItemStack bullet = getBullet(itemstack);
        if(bullet.isEmpty()) {
            List<Slot> list = Lists.newArrayList(playerIn.inventoryContainer.inventorySlots);
            Collections.reverse(list);
            if(setBullet(itemstack,
                    list.stream()
                            .map(Slot::getStack)
                            .filter(stack -> stack.getItem() instanceof Bullet)
                            .findFirst()
                            .orElse(ItemStack.EMPTY))) {
                playerIn.getCooldownTracker().setCooldown(this, 15);
                event = SoundEvents.BLOCK_LEVER_CLICK;
            } else {
                event = SoundEvents.BLOCK_COMPARATOR_CLICK;
            }
        } else if (!worldIn.isRemote) {
            this.shoot(playerIn, worldIn, handIn, this, 10);
            if (!playerIn.capabilities.isCreativeMode) {
                bullet.shrink(1);
            }
            setBullet(itemstack, bullet);
            event = SoundEvents.ENTITY_SNOWBALL_THROW;
        }

        if(event != null) {
            worldIn.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ, event, SoundCategory.NEUTRAL, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
        }


        playerIn.addStat(StatList.getObjectUseStats(this));
        return new ActionResult(EnumActionResult.SUCCESS, itemstack);
    }

    public static void shoot(EntityPlayer playerIn, World worldIn,  EnumHand handIn, Item item, int cooldown) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        ItemStack bullet = getBullet(itemstack);
        playerIn.getCooldownTracker().setCooldown(item, cooldown);
        BulletEntity bulletEntity = new BulletEntity(worldIn, playerIn, bullet);
        bulletEntity.setDamage(20);
        bulletEntity.shoot(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 3F, 0.1F);
        worldIn.spawnEntity(bulletEntity);
    }

    public static ItemStack getBullet(ItemStack remington) {
        NBTTagCompound nbt = remington.getOrCreateSubCompound("remington");
        ItemStack stack = new ItemStack(nbt.getCompoundTag("itemstack"));
        stack.setCount(Math.min(stack.getCount(), MAX_CARRY_SIZE));
        return stack;
    }

    public static boolean setBullet(ItemStack remington, ItemStack bullet) {
        boolean hadItem = !bullet.isEmpty();
        ItemStack bullet2 = bullet.splitStack(MAX_CARRY_SIZE);
        remington.getOrCreateSubCompound("remington").setTag("itemstack", bullet2.serializeNBT());
        return hadItem;
    }
}
