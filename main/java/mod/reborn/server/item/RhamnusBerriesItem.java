package mod.reborn.server.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

/**
 * Created by Codyr on 01/10/2017.
 */
public class RhamnusBerriesItem extends ItemFood {

    public RhamnusBerriesItem(int i, float v) {
        super(5, 0.6F, false);

    }

    protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player) {
        player.addPotionEffect(new PotionEffect(MobEffects.POISON, 1400, 1));
    }
}


