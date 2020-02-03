package mod.reborn.server.item.block;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import mod.reborn.server.block.NestFossilBlock;
import mod.reborn.server.util.LangUtils;

public class NestFossilItemBlock extends ItemBlock {
    private boolean encased;

    public NestFossilItemBlock(Block block, boolean encased) {
        super(block);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
        this.encased = encased;
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return LangUtils.translate(this.encased ? "tile.encased_nest_fossil.name" : "tile.nest_fossil.name");
    }

    private NestFossilBlock.Variant getVariant(ItemStack stack) {
        NestFossilBlock.Variant[] values = NestFossilBlock.Variant.values();
        return values[stack.getItemDamage() % values.length];
    }

    @Override
    public int getMetadata(int metadata) {
        return metadata;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName() + "." + this.getVariant(stack).getName();
    }
}
