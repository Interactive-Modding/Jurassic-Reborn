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

public class SPAS12 extends Item {

    public SPAS12() {
        this.setCreativeTab(TabHandler.ITEMS);
        this.setMaxStackSize(1);
    }

    private static final int MAX_CARRY_SIZE = 12;
    private static final int BULLETS_PER_SHOT = 4;//TODO config ?
    
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
        	    .filter(stack -> stack.getItem() == ItemHandler.BULLET_PACK_12)
        	    .findFirst()
        	    .orElse(ItemStack.EMPTY))) {
                event = SoundEvents.BLOCK_LEVER_CLICK;
                this.setCooldown(playerIn, this, 80);
            } else {
                event = SoundEvents.BLOCK_COMPARATOR_CLICK;
            }
            if (bullet.getCount() < 8) {
                event = SoundEvents.BLOCK_COMPARATOR_CLICK;
                playerIn.inventory.add(bullet.getCount(), new ItemStack(ItemHandler.BULLET));
                bullet.setCount(0);
            }
        } else if (!worldIn.isRemote) {
                this.shoot(playerIn, worldIn, handIn);
                this.shoot(playerIn, worldIn, handIn);
                this.shoot(playerIn, worldIn, handIn);
                this.shoot(playerIn, worldIn, handIn);
                this.setCooldown(playerIn, this, 15);
                if (!playerIn.capabilities.isCreativeMode) {
                    bullet.shrink(BULLETS_PER_SHOT);
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

    public static void shoot(EntityPlayer playerIn, World worldIn,  EnumHand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        ItemStack bullet = getBullet(itemstack);
        BulletEntity bulletEntity = new BulletEntity(worldIn, playerIn, bullet);
        bulletEntity.setDamage(10);
        bulletEntity.shoot(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 5F, 0.8F, 10F);
        worldIn.spawnEntity(bulletEntity);
    }

    public static void setCooldown(EntityPlayer player, Item item, int cooldown) {
        player.getCooldownTracker().setCooldown(item, cooldown);
    }
    
    public static ItemStack getBullet(ItemStack glock) {
        NBTTagCompound nbt = glock.getOrCreateSubCompound("spas12");
        ItemStack stack = new ItemStack(nbt.getCompoundTag("itemstack"));
        stack.setCount(Math.min(stack.getCount(), MAX_CARRY_SIZE));
        return stack;
    }
    
    public static boolean setBullet(ItemStack glock, ItemStack bullet) {
        boolean hadItem = !bullet.isEmpty();
        ItemStack bullet2 = bullet.splitStack(MAX_CARRY_SIZE);
        glock.getOrCreateSubCompound("spas12").setTag("itemstack", bullet2.serializeNBT());
        return hadItem;
    }
}