package mod.reborn.server.item;

import mod.reborn.RebornMod;
import mod.reborn.server.block.entity.BugCrateBlockEntity;
import mod.reborn.server.entity.DinosaurEntity;
import mod.reborn.server.message.OpenFieldGuideGuiMessage;
import mod.reborn.server.message.OpenPaleoPadEntityMessage;
import mod.reborn.server.message.SyncPaleoPadMessage;
import mod.reborn.server.tab.TabHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PaleoPadItem extends Item {
    public PaleoPadItem()
    {
        super();
        this.setMaxStackSize(1);
        this.setCreativeTab(TabHandler.ITEMS);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (world.isRemote) {

            RebornMod.PROXY.openPaleoPad();
        }
        return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase target, EnumHand hand) {
        if (target instanceof DinosaurEntity) {
            if (!player.world.isRemote) {
                RebornMod.NETWORK_WRAPPER.sendTo(new OpenPaleoPadEntityMessage((DinosaurEntity) target), (EntityPlayerMP) player);
            }
            return true;
        }
        return false;
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected)
    {
        if (entity instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer) entity;

            setString(stack, "LastOwner", player.getUniqueID().toString());
        }
    }

    public void setString(ItemStack stack, String key, String value)
    {
        NBTTagCompound nbt = getNBT(stack);

        nbt.setString(key, value);

        stack.setTagCompound(nbt);
    }

    private NBTTagCompound getNBT(ItemStack stack)
    {
        NBTTagCompound nbt = stack.getTagCompound();

        if (nbt == null)
        {
            nbt = new NBTTagCompound();
        }

        return nbt;
    }
}
