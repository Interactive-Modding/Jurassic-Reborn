package mod.reborn.server.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtils;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class PotionDart extends Dart {
    public PotionDart() {
        super((entity, stack) -> PotionUtils.getEffectsFromStack(stack).forEach(entity::addPotionEffect), -1);
    }

    @Override
    public int getDartColor(ItemStack stack) {
        return PotionUtils.getColor(stack);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        PotionUtils.addPotionTooltip(stack, tooltip, 1.0F);
    }
}
