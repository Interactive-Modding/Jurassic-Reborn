package net.vit.jurassicreborn.mixin;

import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static net.vit.jurassicreborn.common.util.ItemStackCreativeModeTabSystem.jurassicreborn$fillItemStacks;

@Mixin(CreativeModeInventoryScreen.class)
public abstract class CreativeModeInventoryScreenMixin extends EffectRenderingInventoryScreen<CreativeModeInventoryScreen.ItemPickerMenu> {

    @Shadow private static int selectedTab;

    public CreativeModeInventoryScreenMixin(CreativeModeInventoryScreen.ItemPickerMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

//    @Inject(method = "renderBg", at = @At("HEAD"))
//    public void addItemStacks(PoseStack pPoseStack, float pPartialTick, int pX, int pY, CallbackInfo ci){
//        if((this.menu).items != null){
//            for(var item : ItemStackCreativeModeTabSystem.getItemStacksForTab(CreativeModeTab.TABS[selectedTab])){
//                this.menu.items.add(item);
//            }
//        }
//
//    }

    @Redirect(method = "selectTab", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/CreativeModeTab;fillItemList(Lnet/minecraft/core/NonNullList;)V"))
    public void redirectFillSelectTab(CreativeModeTab instance, NonNullList<ItemStack> itemStacks){
        jurassicreborn$fillItemStacks(instance, itemStacks);
    }

//    @Redirect(method = "refreshSearchResults", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/CreativeModeTab;fillItemList(Lnet/minecraft/core/NonNullList;)V"))
//    public void redirectFillRefreshResults(CreativeModeTab instance, NonNullList<ItemStack> itemStacks){
//        JurassicReborn$fillItemStacks(instance, itemStacks);
//    }


//    @Redirect(method = "refreshSearchResults", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Item;fillItemCategory(Lnet/minecraft/world/item/CreativeModeTab;Lnet/minecraft/core/NonNullList;)V"))
//    public void redirectFillItemCategory(Item instance, CreativeModeTab pCategory, NonNullList<ItemStack> pItems){
//        JurassicReborn$fillItemStacks(pCategory, pItems);
//        instance.
//    }


}
