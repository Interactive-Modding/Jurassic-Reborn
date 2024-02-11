package mod.reborn.client.event;

import mod.reborn.RebornMod;
import mod.reborn.client.proxy.ClientProxy;
import mod.reborn.server.block.SkullDisplay;
import mod.reborn.server.block.entity.SkullDisplayEntity;
import mod.reborn.server.entity.DinosaurEntity;
import mod.reborn.server.entity.vehicle.HelicopterEntity;
import mod.reborn.server.entity.vehicle.VehicleEntity;
import mod.reborn.server.entity.vehicle.MultiSeatedEntity;
import mod.reborn.server.item.DartGun;
import mod.reborn.server.item.ItemHandler;
import mod.reborn.server.item.guns.Gun;
import mod.reborn.server.message.AttemptMoveToSeatMessage;
import net.ilexiconn.llibrary.LLibrary;
import net.ilexiconn.llibrary.client.util.ClientUtils;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerCustomHead;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.opengl.GL11;

import java.util.List;
import java.util.Map;

public class ClientEventHandler {
    private static final Minecraft MC = Minecraft.getMinecraft();
    private static final ResourceLocation MEMBERS_BADGE = new ResourceLocation(RebornMod.MODID, "textures/items/members.png");
    private static final ResourceLocation TESTERS_BADGE = new ResourceLocation(RebornMod.MODID, "textures/items/testers.png");

    private boolean isGUI;
    public static boolean replacedPlayerModel;
    @SubscribeEvent
    public void tick(TickEvent.ClientTickEvent event) {
        RebornMod.timerTicks++;
    }

    @SubscribeEvent
    public void onGUIRender(GuiScreenEvent.DrawScreenEvent.Pre event) {
        this.isGUI = true;
    }

    @SuppressWarnings("rawtypes")
    @SubscribeEvent
    public static void onRenderPlayer(RenderPlayerEvent.Pre event)
    {
        if(!replacedPlayerModel)
        {
            Render render = Minecraft.getMinecraft().getRenderManager().getEntityClassRenderObject(AbstractClientPlayer.class);
            Map<String, RenderPlayer> skinMap = render.getRenderManager().getSkinMap();
            fixPlayerRenderers(skinMap.get("default"), false);
            fixPlayerRenderers(skinMap.get("slim"), true);
            //skinMap.forEach((key, value) -> patchPlayerRender(key, value.));
            replacedPlayerModel = true;
        }
    }
    @SubscribeEvent
    public static void hightlightEvent(final DrawBlockHighlightEvent e) {
        if (e.getTarget().typeOfHit == RayTraceResult.Type.BLOCK)
        {
            final BlockPos blockpos = e.getTarget().getBlockPos();
            final IBlockState iblockstate = e.getPlayer().world.getBlockState(blockpos);
            if (iblockstate.getBlock() instanceof SkullDisplay) {

                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                GlStateManager.glLineWidth(2.0F);
                GlStateManager.disableTexture2D();
                GlStateManager.depthMask(false);

                if (iblockstate.getMaterial() != Material.AIR && e.getPlayer().world.getWorldBorder().contains(blockpos)) {
                    final double x = e.getPlayer().lastTickPosX + (e.getPlayer().posX - e.getPlayer().lastTickPosX) * (double) e.getPartialTicks();
                    final double y = e.getPlayer().lastTickPosY + (e.getPlayer().posY - e.getPlayer().lastTickPosY) * (double) e.getPartialTicks();
                    final double z = e.getPlayer().lastTickPosZ + (e.getPlayer().posZ - e.getPlayer().lastTickPosZ) * (double) e.getPartialTicks();

                    Vec3d pos = new Vec3d(blockpos.getX() + 0.5D, blockpos.getY(), blockpos.getZ() + 0.5D).subtract(new Vec3d(x, y, z));
                    GL11.glPushMatrix();
                    GL11.glTranslated(pos.x, pos.y, pos.z);
                    final TileEntity tile = e.getPlayer().world.getTileEntity(blockpos);

                    if (tile != null && tile instanceof SkullDisplayEntity && ((SkullDisplayEntity) tile).hasData())
                        GlStateManager.rotate(((SkullDisplayEntity) tile).getAngle(), 0.0F, 1.0F, 0.0F);

                    GL11.glTranslated(-0.5D, 0D, -0.5D);
                    RenderGlobal.drawSelectionBoundingBox(iblockstate.getCollisionBoundingBox(e.getPlayer().world, blockpos).grow(0.002D), 0.0f, 0.0f, 0.0f, 0.4f);
                    GL11.glPopMatrix();

                }

                GlStateManager.depthMask(true);
                GlStateManager.enableTexture2D();
                GlStateManager.disableBlend();
                e.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerModelRender(ModelPlayerRenderEvent.Render.Pre event)
    {
        EntityPlayer player = event.getEntityPlayer();
        Entity entity = player.getRidingEntity();
        if(entity instanceof HelicopterEntity)
        {
            HelicopterEntity vehicle = (HelicopterEntity) entity;
            vehicle.doPlayerRotations(player, event.getPartialTicks());
        }
    }

    private static void fixPlayerRenderers(RenderPlayer player, boolean slimArms)
    {
        ModelBiped model = new CustomModelPlayer(0.0F, slimArms);
        List<LayerRenderer<EntityLivingBase>> layers = ObfuscationReflectionHelper.getPrivateValue(RenderLivingBase.class, player, (Boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment") ? "layerRenderers" : "field_177097_h");
        if(layers != null)
        {
            for(int i = 0; i < layers.size(); i++) {
                LayerRenderer<EntityLivingBase> layer = layers.get(i);
                if(layer instanceof LayerCustomHead)
                    layers.remove(layer);
            }
            layers.add(new LayerCustomHead(model.bipedHead));
        }

        ObfuscationReflectionHelper.setPrivateValue(RenderLivingBase.class, player, model, (Boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment") ? "mainModel" : "field_77045_g");
    }

    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            this.isGUI = false;
        }
    }


    @SubscribeEvent
    public void onGameOverlay(RenderGameOverlayEvent.Post event) {
    	if(event.getType() != ElementType.FOOD) return;
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer player = mc.player;
        for(EnumHand hand : EnumHand.values()) {
            ItemStack stack = player.getHeldItem(hand);
            if(stack.getItem() == ItemHandler.DART_GUN) {
                ItemStack dartItem = DartGun.getDartItem(stack);
                if(!dartItem.isEmpty()) {
                    RenderItem renderItem = mc.getRenderItem();
                    FontRenderer fontRenderer = mc.fontRenderer;
                    ScaledResolution scaledResolution = new ScaledResolution(mc);

                    int xPosition = scaledResolution.getScaledWidth() - 18;
                    int yPosition = scaledResolution.getScaledHeight() - 18;

                    renderItem.renderItemAndEffectIntoGUI(dartItem, xPosition, yPosition);
                    String s = String.valueOf(dartItem.getCount());
                    GlStateManager.disableDepth();
                    fontRenderer.drawStringWithShadow(s, xPosition + 17 - fontRenderer.getStringWidth(s), yPosition + 9, 0xFFFFFFFF);
                    GlStateManager.enableDepth();
                }
                break;
            }
            if(stack.getItem() == ItemHandler.GLOCK || stack.getItem() == ItemHandler.SPAS_12 || stack.getItem() == ItemHandler.UTS15 || stack.getItem() == ItemHandler.REMINGTON) {
                ItemStack bullet = Gun.getBullet(stack);
                if(!bullet.isEmpty()) {
                    RenderItem renderItem = mc.getRenderItem();
                    FontRenderer fontRenderer = mc.fontRenderer;
                    ScaledResolution scaledResolution = new ScaledResolution(mc);

                    int xPosition = scaledResolution.getScaledWidth() - 18;
                    int yPosition = scaledResolution.getScaledHeight() - 18;

                    renderItem.renderItemAndEffectIntoGUI(bullet, xPosition, yPosition);
                    String s = String.valueOf(bullet.getCount());
                    GlStateManager.disableDepth();
                    fontRenderer.drawStringWithShadow(s, xPosition + 17 - fontRenderer.getStringWidth(s), yPosition + 9, 0xFFFFFFFF);
                    GlStateManager.enableDepth();
                }
                break;
            }
        }
    }

    @SubscribeEvent
    public void keyInputEvent(InputEvent.KeyInputEvent event) {
        int i = 0;
        for(KeyBinding binding : ClientProxy.getKeyHandler().VEHICLE_KEY_BINDINGS) {
            if(binding.isPressed()) {
                EntityPlayer player = Minecraft.getMinecraft().player;
                Entity entity = player.getRidingEntity();
                if(entity instanceof MultiSeatedEntity) {
                    int fromSeat = ((MultiSeatedEntity)entity).getSeatForEntity(player);
                    if(fromSeat != -1) {
                        RebornMod.NETWORK_WRAPPER.sendToServer(new AttemptMoveToSeatMessage(entity, fromSeat, i));
                    }
                }
                break;
            }
            ++i;
        }
    }


    @SubscribeEvent
    public void onPlayerRender(RenderPlayerEvent.Post event) {
        EntityPlayer player = event.getEntityPlayer();

        if (!player.isElytraFlying() && !player.isPlayerSleeping() && player.deathTime <= 0 && !player.isInvisible() && !player.isInvisibleToPlayer(MC.player) && ClientProxy.TESTERS.contains(player.getUniqueID())) {
            GlStateManager.pushMatrix();

            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            if (this.isGUI) {
                OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240, 240);
            }

            RenderPlayer renderer = event.getRenderer();

            GlStateManager.translate(event.getX(), event.getY(), event.getZ());

            GlStateManager.rotate(-ClientUtils.interpolate(this.isGUI ? player.renderYawOffset : player.prevRenderYawOffset, player.renderYawOffset, LLibrary.PROXY.getPartialTicks()), 0.0F, 1.0F, 0.0F);

            if (player.isSneaking()) {
                GlStateManager.translate(0.0F, -0.2F, 0.0F);
                GlStateManager.rotate((float) Math.toDegrees(-renderer.getMainModel().bipedBody.rotateAngleY), 0.0F, 1.0F, 0.0F);
                GlStateManager.rotate((float) Math.toDegrees(0.5F), 1.0F, 0.0F, 0.0F);
                GlStateManager.translate(0.0F, -0.15F, -0.68F);
            } else {
                renderer.getMainModel().bipedBody.postRender(0.0625F);
                GlStateManager.rotate((float) Math.toDegrees(-renderer.getMainModel().bipedBody.rotateAngleY) * 2.0F, 0.0F, 1.0F, 0.0F);
            }

            GlStateManager.translate(-0.1F, 1.4F, 0.14F);

            float scale = 0.35F;

            GlStateManager.scale(scale, -scale, scale);

            GlStateManager.disableCull();

            MC.getTextureManager().bindTexture(TESTERS_BADGE);

            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder buffer = tessellator.getBuffer();

            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
            buffer.pos(0.0, 0.0, 0.0).tex(0.0, 0.0).endVertex();
            buffer.pos(1.0, 0.0, 0.0).tex(1.0, 0.0).endVertex();
            buffer.pos(1.0, 1.0, 0.0).tex(1.0, 1.0).endVertex();
            buffer.pos(0.0, 1.0, 0.0).tex(0.0, 1.0).endVertex();
            tessellator.draw();

            GlStateManager.popMatrix();

            if (this.isGUI) {
                OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, OpenGlHelper.lastBrightnessX, OpenGlHelper.lastBrightnessY);
            }
        }

        if (!player.isElytraFlying() && !player.isPlayerSleeping() && player.deathTime <= 0 && !player.isInvisible() && !player.isInvisibleToPlayer(MC.player) && ClientProxy.MEMBERS.contains(player.getUniqueID())) {
            GlStateManager.pushMatrix();

            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            if (this.isGUI) {
                OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240, 240);
            }

            RenderPlayer renderer = event.getRenderer();

            GlStateManager.translate(event.getX(), event.getY(), event.getZ());

            GlStateManager.rotate(-ClientUtils.interpolate(this.isGUI ? player.renderYawOffset : player.prevRenderYawOffset, player.renderYawOffset, LLibrary.PROXY.getPartialTicks()), 0.0F, 1.0F, 0.0F);

            if (player.isSneaking()) {
                GlStateManager.translate(0.0F, -0.2F, 0.0F);
                GlStateManager.rotate((float) Math.toDegrees(-renderer.getMainModel().bipedBody.rotateAngleY), 0.0F, 1.0F, 0.0F);
                GlStateManager.rotate((float) Math.toDegrees(0.5F), 1.0F, 0.0F, 0.0F);
                GlStateManager.translate(0.0F, -0.15F, -0.68F);
            } else {
                renderer.getMainModel().bipedBody.postRender(0.0625F);
                GlStateManager.rotate((float) Math.toDegrees(-renderer.getMainModel().bipedBody.rotateAngleY) * 2.0F, 0.0F, 1.0F, 0.0F);
            }

            GlStateManager.translate(-0.1F, 1.4F, 0.14F);

            float scale = 0.35F;

            GlStateManager.scale(scale, -scale, scale);

            GlStateManager.disableCull();

            MC.getTextureManager().bindTexture(MEMBERS_BADGE);

            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder buffer = tessellator.getBuffer();

            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
            buffer.pos(0.0, 0.0, 0.0).tex(0.0, 0.0).endVertex();
            buffer.pos(1.0, 0.0, 0.0).tex(1.0, 0.0).endVertex();
            buffer.pos(1.0, 1.0, 0.0).tex(1.0, 1.0).endVertex();
            buffer.pos(0.0, 1.0, 0.0).tex(0.0, 1.0).endVertex();
            tessellator.draw();

            GlStateManager.popMatrix();

            if (this.isGUI) {
                OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, OpenGlHelper.lastBrightnessX, OpenGlHelper.lastBrightnessY);
            }
        }
    }
    @SubscribeEvent
    public static void onRenderWorldLast(final RenderWorldLastEvent event) {

        if (!Minecraft.isGuiEnabled())
            return;

        final Entity cameraEntity = ClientProxy.MC.getRenderViewEntity();
        Frustum frustrum = new Frustum();
        final double viewX = cameraEntity.lastTickPosX + (cameraEntity.posX - cameraEntity.lastTickPosX) * event.getPartialTicks();
        final double viewY = cameraEntity.lastTickPosY + (cameraEntity.posY - cameraEntity.lastTickPosY) * event.getPartialTicks();
        final double viewZ = cameraEntity.lastTickPosZ + (cameraEntity.posZ - cameraEntity.lastTickPosZ) * event.getPartialTicks();
        frustrum.setPosition(viewX, viewY, viewZ);

        final List<Entity> loadedEntities = ClientProxy.MC.world.getLoadedEntityList();
        for (final Entity entity : loadedEntities) {
            if (entity != null && entity instanceof DinosaurEntity) {
                if (entity.isInRangeToRender3d(cameraEntity.getPosition().getX(), cameraEntity.getPosition().getY(), cameraEntity.getPosition().getZ()) && (frustrum.isBoundingBoxInFrustum(entity.getRenderBoundingBox().grow(0.5D))) && entity.isEntityAlive()) {
                    ((DinosaurEntity) entity).isRendered = true;
                } else {
                    ((DinosaurEntity) entity).isRendered = false;
                }
            }
        }
    }
}
