package mod.reborn.client.render.entity;

import mod.reborn.RebornMod;
import mod.reborn.client.model.TabulaModelUV;
import mod.reborn.client.model.animation.entity.vehicle.HelicopterAnimator;
import mod.reborn.client.model.animation.entity.vehicle.HelicopterNewAnimator;
import mod.reborn.server.entity.ai.util.MathUtils;
import mod.reborn.server.entity.vehicle.HelicopterEntity;
import mod.reborn.server.entity.vehicle.HelicopterEntityNew;
import mod.reborn.server.entity.vehicle.VehicleEntity;
import mod.reborn.server.tabula.TabulaModelHelper;
import net.ilexiconn.llibrary.client.model.tabula.TabulaModel;
import net.ilexiconn.llibrary.client.model.tabula.container.TabulaModelContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import javax.vecmath.Vector2d;
import javax.vecmath.Vector4d;
import java.util.stream.IntStream;

@SideOnly(Side.CLIENT)
public class HelicopterNewRenderer extends Render<HelicopterEntityNew> {
    private static final ResourceLocation[] DESTROY_STAGES = IntStream.range(0, 10).mapToObj(n -> new ResourceLocation(String.format("textures/blocks/destroy_stage_%d.png", n))).toArray(ResourceLocation[]::new);
    private static ResourceLocation positionLights;
    protected HelicopterNewAnimator animator;
    protected final ResourceLocation texture;
    protected TabulaModel baseModel;
    protected TabulaModel destroyModel;

    public HelicopterNewRenderer(RenderManager renderManager) {
        super(renderManager);
        this.animator = new HelicopterNewAnimator();
        texture = new ResourceLocation(RebornMod.MODID, "textures/entities/helicopter/helicopter.png");
        positionLights = new ResourceLocation(RebornMod.MODID, "textures/entities/helicopter/helicopter_position_lights.png");
        try {
            TabulaModelContainer container = TabulaModelHelper.loadTabulaModel(new ResourceLocation(RebornMod.MODID, "models/entities/helicopter/helicopter"));
            this.baseModel = new TabulaModel(container, animator);
            this.destroyModel = new TabulaModel(new TabulaModelUV(container, 16, 16), animator);
        } catch (Exception e) {
            throw new RuntimeException("Unable to load helicopter", e);
        }
    }

    @Override
    public void doRender(HelicopterEntityNew helicopter, double x, double y, double z, float yaw, float partialTicks) {

        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x, (float) y + 1.5F, (float) z);
        GlStateManager.rotate(180.0F - yaw, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate((float) helicopter.interpRotationPitch.getValueForRendering(partialTicks), 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate((float) helicopter.interpRotationRoll.getValueForRendering(partialTicks), 0.0F, 0.0F, 1.0F);

        float f4 = 1f;
        GlStateManager.scale(f4, f4, f4);
        GlStateManager.scale(1.0F / f4, 1.0F / f4, 1.0F / f4);
        this.bindEntityTexture(helicopter);
        GlStateManager.scale(-1.0F, -1.0F, 1.0F);
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        this.baseModel.render(helicopter, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
        if (helicopter.getControllingPassenger() != null && Minecraft.getMinecraft().world.getTotalWorldTime() % 60 < 10) { //Every 3 seconds blink for 1 second
            GlStateManager.color(6, 6, 6, 1F);
            this.bindTexture(positionLights);
            this.baseModel.render(helicopter, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
        }
        int destroyStage = Math.min(10, (int) (10 - (helicopter.getHealth() / helicopter.getMaxHealth()) * 10)) - 1;
        if (destroyStage >= 0) {
            GlStateManager.color(1, 1, 1, 0.5F);
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.DST_COLOR, GlStateManager.DestFactor.SRC_COLOR, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.doPolygonOffset(-3, -3);
            GlStateManager.enablePolygonOffset();
            RenderHelper.disableStandardItemLighting();
            this.bindTexture(DESTROY_STAGES[destroyStage]);
            this.destroyModel.render(helicopter, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
            GlStateManager.doPolygonOffset(0, 0);
            GlStateManager.disablePolygonOffset();
            RenderHelper.enableStandardItemLighting();
        }
        GlStateManager.disableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.popMatrix();
        super.doRender(helicopter, x, y, z, yaw, partialTicks);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(HelicopterEntityNew entity) {
        return this.texture;
    }
}
