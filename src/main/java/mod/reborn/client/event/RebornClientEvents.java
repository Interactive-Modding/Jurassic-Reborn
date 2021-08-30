package mod.reborn.client.event;

import com.mojang.blaze3d.platform.GlStateManager;
import mod.reborn.RebornMod;
import mod.reborn.server.items.ItemHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public class RebornClientEvents {
    private boolean isGUI;
    public static boolean replacedPlayerModel;

    public static RebornClientEvents Register(IEventBus eventBus) {
        return new RebornClientEvents(eventBus);
    }

    public void Log() {
        RebornMod.LOGGER.info("Successfully triggered the registry of the Reborn Client Events.");
    }

    private RebornClientEvents(IEventBus eventBus) {
        eventBus.addListener(this::onPlayerModelRender);
        //eventBus.addListener(this::onRenderPlayer);
        eventBus.addListener(this::onGUIRender);
        eventBus.addListener(this::tick);
    }

    private void onPlayerModelRender(ModelPlayerRenderEvent.Render.Pre event) {
        PlayerEntity player = event.getPlayer();
        Entity entity = player.getRidingEntity();
        /* if(entity instanceof HelicopterEntity) {
            HelicopterEntity vehicle = (HelicopterEntity) entity;
            vehicle.doPlayerRotations(player, event.getPartialTicks());
        }*/
    }

    private void onGUIRender(GuiScreenEvent.DrawScreenEvent.Pre event) {
        this.isGUI = true;
    }

    private void tick(TickEvent.ClientTickEvent event) {
        RebornMod.timerTicks++;
    }

    private void onRenderTick(TickEvent.RenderTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            this.isGUI = false;
        }
    }

    private void onGameOverlay(RenderGameOverlayEvent.Post event) {
        /*Minecraft mc = Minecraft.getInstance();
        ClientPlayerEntity player = mc.player;
        for(Hand hand : Hand.values()) {
            assert player != null;
            ItemStack stack = player.getHeldItem(hand);
            if(stack.getItem() == ItemHandler.DART_GUN) {
                ItemStack dartItem = DartGun.getDartItem(stack);
                if(!dartItem.isEmpty()) {
                    shootableItemRender(mc);
                }
                break;
            }
            if(stack.getItem() == ItemHandler.GLOCK || stack.getItem() == ItemHandler.SPAS_12 || stack.getItem() == ItemHandler.UTS15 || stack.getItem() == ItemHandler.REMINGTON) {
                ItemStack bullet = Gun.getBullet(stack);
                if(!bullet.isEmpty()) {
                    shootableItemRender(mc);
                }
                break;
            }
        }*/
    }

    private void shootableItemRender(Minecraft mc) {
        /*ItemRenderer renderItem = mc.getItemRenderer();
        FontRenderer fontRenderer = mc.fontRenderer;
        ScaledResolution scaledResolution = new ScaledResolution(mc);
        mc.render
        int xPosition = scaledResolution.getScaledWidth() - 18;
        int yPosition = scaledResolution.getScaledHeight() - 18;

        renderItem.renderItemAndEffectIntoGUI(bullet, xPosition, yPosition);
        String s = String.valueOf(bullet.getCount());
        GlStateManager.disableDepthTest();
        fontRenderer.drawStringWithShadow(s, xPosition + 17 - fontRenderer.getStringWidth(s), yPosition + 9, 0xFFFFFFFF);
        GlStateManager.enableDepthTest();*/
    }
}
