package mod.reborn.server.item.block;

import net.minecraft.block.Block;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import mod.reborn.server.util.LangUtils;

public class CultivateItemBlock extends ItemBlock {
    public CultivateItemBlock(Block block) {
        super(block);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    @Override
    public int getMetadata(int metadata) {
        return metadata;
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        EnumDyeColor color = EnumDyeColor.byMetadata(stack.getItemDamage());
        return this.block.getLocalizedName().replace("{color}", LangUtils.translate(LangUtils.COLORS.get(color.getName())));
    }
}
