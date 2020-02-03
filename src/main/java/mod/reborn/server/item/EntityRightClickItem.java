package mod.reborn.server.item;

import mod.reborn.server.tab.TabHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

import com.google.common.base.Function;

public class EntityRightClickItem extends Item {
    
    private final Function<Interaction, Boolean> func;
    
    public EntityRightClickItem(Function<Interaction, Boolean> func) {
        this.func = func;
        this.setCreativeTab(TabHandler.ITEMS);
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase target, EnumHand hand) {
	return func.apply(new Interaction(stack, player, target, hand));
    }
    
    public class Interaction {
	private final ItemStack stack;
	private final EntityPlayer player;
	private final EntityLivingBase target;
	private final EnumHand hand;
	
	public Interaction(ItemStack stack, EntityPlayer player, EntityLivingBase target, EnumHand hand) {
	    this.stack = stack;
	    this.player = player;
	    this.target = target;
	    this.hand = hand;
	}
	
	public ItemStack getStack() {
	    return stack;
	}
	
	public EntityPlayer getPlayer() {
	    return player;
	}
	
	public EntityLivingBase getTarget() {
	    return target;
	}
	
	public EnumHand getHand() {
	    return hand;
	}
    }
}
