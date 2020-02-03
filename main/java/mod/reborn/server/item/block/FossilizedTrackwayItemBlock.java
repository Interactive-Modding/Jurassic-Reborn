package mod.reborn.server.item.block;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import mod.reborn.server.block.FossilizedTrackwayBlock;
import mod.reborn.server.util.LangUtils;

public class FossilizedTrackwayItemBlock extends ItemBlock {
    public FossilizedTrackwayItemBlock(Block block) {
        super(block);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        FossilizedTrackwayBlock.TrackwayType type = this.getType(stack);
        return this.block.getLocalizedName().replace("{variant}", LangUtils.translate("trackway." + type.getName() + ".name"));
    }

    private FossilizedTrackwayBlock.TrackwayType getType(ItemStack stack) {
        FossilizedTrackwayBlock.TrackwayType[] values = FossilizedTrackwayBlock.TrackwayType.values();
        return values[stack.getItemDamage() % values.length];
    }

    @Override
    public int getMetadata(int metadata) {
        return metadata;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName() + "." + this.getType(stack).getName();
    }
}
