package net.vit.jurassicreborn.common.items;


import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.client.JurassicClient;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.vit.jurassicreborn.common.entities.DinosaurEntity;
import net.vit.jurassicreborn.common.blocks.entities.feeder.FeederBlockEntity;
import net.vit.jurassicreborn.common.network.Network;
import net.vit.jurassicreborn.common.util.message.OpenPaleoPadEntityMessage;
import net.vit.jurassicreborn.common.paleopad.FeederTrackerApp;
import net.vit.jurassicreborn.common.paleopad.AppHandler;

public class PaleoPadItem extends Item {
    public PaleoPadItem() {
        super(new Item.Properties()
                .stacksTo(1)
                .tab(TabHandler.ITEMS)
        );
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (world.isClientSide) {
            openPaleoPad();
        }
        return InteractionResultHolder.success(stack);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
        if (target instanceof DinosaurEntity) {
            if (!player.level.isClientSide) {
                if (player instanceof net.minecraft.server.level.ServerPlayer serverPlayer) {
                    Network.sendTo(serverPlayer, new OpenPaleoPadEntityMessage((DinosaurEntity) target));
                }
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        Level level = ctx.getLevel();
        Player player = ctx.getPlayer();
        BlockPos pos = ctx.getClickedPos();
        if (player != null && level.getBlockEntity(pos) instanceof FeederBlockEntity feeder) {
            Component name = feeder.getName();
            ItemStack stack = ctx.getItemInHand();
            if (stack.hasCustomHoverName()) {
                name = stack.getHoverName();
                feeder.setCustomName(name);
            }
            if (level.isClientSide) {
                if (!feeder.hasCustomName()) {
                    openFeederNameScreen(pos);
                } else {
                    openPaleoPadFeederTracker();
                }
            } else {
                FeederTrackerApp.addFeeder(player, pos, name);
            }
            return InteractionResult.SUCCESS;
        }
        return super.useOn(ctx);
    }



    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
        if (entity instanceof Player player) {
            setString(stack, "LastOwner", player.getUUID().toString());
        }
    }

    public void setString(ItemStack stack, String key, String value) {
        CompoundTag nbt = stack.getOrCreateTag();
        nbt.putString(key, value);
        stack.setTag(nbt);
    }

    @OnlyIn(Dist.CLIENT)
    private void openPaleoPad() {
        JurassicClient.openPaleoPad();
    }

    @OnlyIn(Dist.CLIENT)
    private void openFeederNameScreen(BlockPos pos) {
        JurassicClient.openFeederNameScreen(pos);
    }

    @OnlyIn(Dist.CLIENT)
    private void openPaleoPadFeederTracker() {
        JurassicClient.openPaleoPad(AppHandler.INSTANCE.feederTracker);
    }
}
