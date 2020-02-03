package mod.reborn.server.item;

import mod.reborn.server.tab.TabHandler;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import mod.reborn.server.entity.item.AttractionSignEntity;
import mod.reborn.server.util.LangUtils;

public class AttractionSignItem extends Item {
    public AttractionSignItem() {
        this.setCreativeTab(TabHandler.DECORATIONS);
        this.setHasSubtypes(true);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return LangUtils.translate(this.getUnlocalizedName() + ".name").replace("{type}", LangUtils.getAttractionSignName(stack));
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
    	ItemStack stack = player.getHeldItem(hand);
        if (side != EnumFacing.DOWN && side != EnumFacing.UP) {
            BlockPos offset = pos.offset(side);

            if (player.canPlayerEdit(offset, side, stack)) {
                AttractionSignEntity sign = new AttractionSignEntity(world, offset, side, AttractionSignEntity.AttractionSignType.values()[stack.getItemDamage()]);

                if (sign.onValidSurface()) {
                    if (!world.isRemote) {
                        world.spawnEntity(sign);
                    }

                    stack.shrink(1);

                    return EnumActionResult.SUCCESS;
                }
            }
        }

        return EnumActionResult.PASS;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems) {
        if(this.isInCreativeTab(tab))
        for (AttractionSignEntity.AttractionSignType signType : AttractionSignEntity.AttractionSignType.values()) {
            subItems.add(new ItemStack(this, 1, signType.ordinal()));
        }
    }
}
