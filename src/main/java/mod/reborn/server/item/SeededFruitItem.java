package mod.reborn.server.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class SeededFruitItem extends ItemFood {
    private Item seed;

    public SeededFruitItem(Item seed, int amount, float saturation) {
        super(amount, saturation, false);
        this.seed = seed;
    }

    @Override
    protected void onFoodEaten(ItemStack stack, World world, EntityPlayer player) {
        super.onFoodEaten(stack, world, player);
        if (!world.isRemote && player.getRNG().nextDouble() < 0.5) {
            player.inventory.addItemStackToInventory(new ItemStack(this.seed));
        }
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 14;
    }
}
