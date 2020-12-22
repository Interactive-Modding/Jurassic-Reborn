package mod.reborn.server.item;

import mod.reborn.server.entity.vehicle.*;
import mod.reborn.server.tab.TabHandler;
import mod.reborn.server.util.LangUtils;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class VehicleItem extends Item {

    public static final String[] variants = new String[] { "ford_explorer", "jeep_wrangler", "helicopter", "ford_explorer_snow" };
    // public static final String[] localized = new String[variants.length];

    public VehicleItem() {

        this.setCreativeTab(TabHandler.ITEMS);
        this.setMaxStackSize(1);
        this.setHasSubtypes(true);
        // Caching the localized files, is that needed? Don't uncomment as changing the
        // language at the runtime would break the cache
        /*
         * if(FMLCommonHandler.instance().getSide() == Side.CLIENT) { for (int i = 0; i
         * < variants.length; i++) { localized[i] = LangUtils.translate("item." +
         * variants[i] + ".name"); } }
         */
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag tooltipFlag) {
        super.addInformation(stack, world, tooltip, tooltipFlag);
        int meta = stack.getMetadata();
        tooltip.add(LangUtils.translate("item.vehicle_item.place.name").replace("{variant}", LangUtils.translate("item." + variants[MathHelper.clamp(meta, 0, variants.length - 1)] + ".name") /* localized[meta] */));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getUnlocalizedName(ItemStack stack) {
        int meta = stack.getMetadata();
        return LangUtils.translate("item." + variants[MathHelper.clamp(meta, 0, variants.length - 1)]);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.getHeldItem(hand);

        if (!world.isRemote) {
            pos = pos.offset(side);
            Entity entity = null;
            if (stack.getMetadata() == 0) {
                entity = new FordExplorerEntity(world);
            } else if (stack.getMetadata() == 1) {
                entity = new JeepWranglerEntity(world);
            } else if (stack.getMetadata() == 3) {
                entity = new FordExplorerSnowEntity(world);
            } else if (stack.getMetadata() == 2) {
                entity = new HelicopterEntityNew(world);
            }
            if (stack.getMetadata() == 2) {
                entity.setPositionAndRotation(pos.getX() + hitX, pos.getY() + hitY, pos.getZ() + hitZ, player.rotationYaw, 0.0F);
            } else {
                entity.setPositionAndRotation(pos.getX() + 0.5F, pos.getY(), pos.getZ() + 0.5F, player.rotationYaw, 0.0F);
            }

            world.spawnEntity(entity);

            stack.shrink(1);
        }

        return EnumActionResult.SUCCESS;
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (this.isInCreativeTab(tab)) {
            for (int i = 0; i < 4; ++i) {
                items.add(new ItemStack(this, 1, i));
            }
        }
    }

}
