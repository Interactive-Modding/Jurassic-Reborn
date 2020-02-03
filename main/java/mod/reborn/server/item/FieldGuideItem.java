package mod.reborn.server.item;

import mod.reborn.server.tab.TabHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import mod.reborn.RebornMod;
import mod.reborn.server.entity.DinosaurEntity;
import mod.reborn.server.message.OpenFieldGuideGuiMessage;

public class FieldGuideItem extends Item {
    public FieldGuideItem() {
        super();
        this.setCreativeTab(TabHandler.ITEMS);
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase target, EnumHand hand) {
        if (target instanceof DinosaurEntity) {
            if (!player.world.isRemote) {
                RebornMod.NETWORK_WRAPPER.sendTo(new OpenFieldGuideGuiMessage((DinosaurEntity) target), (EntityPlayerMP) player);
            }
            return true;
        }
        return false;
    }
}
