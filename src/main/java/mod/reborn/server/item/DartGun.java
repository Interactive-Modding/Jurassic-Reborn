package mod.reborn.server.item;

import com.google.common.collect.Lists;
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
import mod.reborn.server.entity.item.TranquilizerDartEntity;

import java.util.Collections;
import java.util.List;

public class DartGun extends Item {

    public DartGun() {
        this.setCreativeTab(TabHandler.ITEMS);
        this.setMaxStackSize(1);
    }

    private static final int MAX_CARRY_SIZE = 12; //TODO config ?
    
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);

        SoundEvent event = null;
        
        ItemStack dartItem = getDartItem(itemstack);
        if(dartItem.isEmpty()) {
            List<Slot> list = Lists.newArrayList(playerIn.inventoryContainer.inventorySlots);
            Collections.reverse(list);
            if(setDartItem(itemstack, 
        	    list.stream()
        	    .map(Slot::getStack)
        	    .filter(stack -> stack.getItem() instanceof Dart)
        	    .findFirst()
        	    .orElse(ItemStack.EMPTY))) {
        	event = SoundEvents.ENTITY_ITEM_PICKUP;
            } else {
        	event = SoundEvents.BLOCK_COMPARATOR_CLICK;
            }
        } else if (!worldIn.isRemote) {
            if(!dartItem.hasTagCompound())
                dartItem.setTagCompound(new NBTTagCompound());
            dartItem.getTagCompound().setString("uuid", playerIn.getGameProfile().getId().toString());
            TranquilizerDartEntity dart = new TranquilizerDartEntity(worldIn, playerIn, dartItem);
            dart.shoot(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 2.5F, 0.5F);
            worldIn.spawnEntity(dart);
            if (!playerIn.capabilities.isCreativeMode) {
                dartItem.shrink(1);
            }
            setDartItem(itemstack, dartItem);
            event = SoundEvents.ENTITY_SNOWBALL_THROW;
        }
        
        if(event != null) {
            worldIn.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ, event, SoundCategory.NEUTRAL, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
        }


        playerIn.addStat(StatList.getObjectUseStats(this));
        return new ActionResult(EnumActionResult.SUCCESS, itemstack);
    }
    
    public static ItemStack getDartItem(ItemStack dartGun) {
        NBTTagCompound nbt = dartGun.getOrCreateSubCompound("dart_gun");
        ItemStack stack = new ItemStack(nbt.getCompoundTag("itemstack"));
        stack.setCount(Math.min(stack.getCount(), MAX_CARRY_SIZE));
        return stack;
    }
    
    public static boolean setDartItem(ItemStack dartGun, ItemStack dartItem) {
        boolean hadItem = !dartItem.isEmpty();
        ItemStack dartItem2 = dartItem.splitStack(MAX_CARRY_SIZE);
        dartGun.getOrCreateSubCompound("dart_gun").setTag("itemstack", dartItem2.serializeNBT());
        return hadItem;
    }
}