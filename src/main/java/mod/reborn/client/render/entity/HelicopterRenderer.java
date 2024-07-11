package mod.reborn.client.render.entity;

import mod.reborn.RebornMod;
import mod.reborn.client.model.TabulaModelUV;
import mod.reborn.client.model.animation.entity.vehicle.HelicopterAnimator;
import mod.reborn.server.entity.ai.util.MathUtils;
import mod.reborn.server.entity.vehicle.HelicopterEntity;
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

import javax.annotation.Nullable;
import javax.vecmath.Vector2d;
import javax.vecmath.Vector4d;
import java.util.stream.IntStream;

@SideOnly(Side.CLIENT)
public abstract class HelicopterRenderer<E extends HelicopterEntity> extends Render<E> {
    private static final ResourceLocation[] DESTROY_STAGES = IntStream.range(0, 10).mapToObj(n -> new ResourceLocation(String.format("textures/blocks/destroy_stage_%d.png", n))).toArray(ResourceLocation[]::new);
    private static ResourceLocation positionLights;
    protected final String helicopterName;
    protected HelicopterAnimator animator;
    protected final ResourceLocation texture;
    protected TabulaModel baseModel;
    protected TabulaModel destroyModel;
    protected int passedRenderTicks = 0;

    protected HelicopterRenderer(RenderManager renderManager, String helicopterName) {
        super(renderManager);
        this.helicopterName = helicopterName;
        this.animator = createCarAnimator();
        texture = new ResourceLocation(RebornMod.MODID, "textures/entities/" + helicopterName + "/" + helicopterName + ".png");
        positionLights = new ResourceLocation(RebornMod.MODID, "textures/entities/" + helicopterName + "/" + helicopterName + "_position_lights.png");
        try {
            TabulaModelContainer container = TabulaModelHelper.loadTabulaModel(new ResourceLocation(RebornMod.MODID, "models/entities/" + helicopterName + "/" + helicopterName));
            this.baseModel = new TabulaModel(container, animator);
            this.destroyModel = new TabulaModel(new TabulaModelUV(container, 16, 16), animator);
        } catch (Exception e) {
            throw new RuntimeException("Unable to load helicopter " + helicopterName, e);
        }
        this.passedRenderTicks = 0;
    }

    @Override
    public void doRender(E entity, double x, double y, double z, float yaw, float partialTicks) {
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        this.bindEntityTexture(entity);
        this.renderModel(entity, x, y, z, yaw, partialTicks, false);
        this.renderPositionLamp(entity, x, y, z, yaw, partialTicks);

        this.renderDestroyTexture(entity, x, y, z, yaw, partialTicks);

        GlStateManager.disableBlend();
        super.doRender(entity, x, y, z, yaw, partialTicks);
    }

    protected void renderModel(E entity, double x, double y, double z, float yaw, float partialTicks, boolean destroy) {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x, (float) y + 1.25F, (float) z);
        GlStateManager.rotate(180 - yaw, 0, 1, 0);
        GlStateManager.rotate(entity.pitch, -1f, 0f, 0f);
        GlStateManager.rotate(entity.roll, 0f, 0f, 1f);
        // GlStateManager.translate(0, 0, -2.5);
        GlStateManager.scale(-1, -1, 1);
        (destroy ? this.destroyModel : this.baseModel).render(entity, 0, 0, 0, 0, 0, 0.0625F);
        GlStateManager.popMatrix();
    }

    protected void renderPositionLamp(E entity, double x, double y, double z, float yaw, float partialTicks) {
        if (entity.getControllingPassenger() != null || entity.getCurrentEngineSpeed() > 1) {
            if (Minecraft.getMinecraft().world.getTotalWorldTime() - this.passedRenderTicks > entity.getPositionLightFrequency() * 2) {
                this.passedRenderTicks = (int) Minecraft.getMinecraft().world.getTotalWorldTime();
            }
            if (Minecraft.getMinecraft().world.getTotalWorldTime() - this.passedRenderTicks <= entity.getPositionLightFrequency()) {
                GlStateManager.color(6, 6, 6, 1F);
                this.bindTexture(positionLights);
                this.renderModel(entity, x, y, z, yaw, partialTicks, false);
            }
        }
    }

    protected void renderDestroyTexture(E entity, double x, double y, double z, float yaw, float partialTicks) {
        int destroyStage = Math.min(10, (int) (10 - (entity.getHealth() / VehicleEntity.MAX_HEALTH) * 10)) - 1;
        if (destroyStage >= 0) {
            GlStateManager.color(1, 1, 1, 0.5F);
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.DST_COLOR, GlStateManager.DestFactor.SRC_COLOR, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.doPolygonOffset(-3, -3);
            GlStateManager.enablePolygonOffset();
            RenderHelper.disableStandardItemLighting();
            this.bindTexture(DESTROY_STAGES[destroyStage]);
            this.renderModel(entity, x, y, z, yaw, partialTicks, true);
            GlStateManager.doPolygonOffset(0, 0);
            GlStateManager.disablePolygonOffset();
            RenderHelper.enableStandardItemLighting();
        }
    }

    protected void doCarRotations(E entity, float partialTicks) {
        if (!(entity instanceof HelicopterEntity)) {
            double backValue = entity.backValue.getValueForRendering(partialTicks);
            double frontValue = entity.frontValue.getValueForRendering(partialTicks);
            double leftValue = entity.leftValue.getValueForRendering(partialTicks);
            double rightValue = entity.rightValue.getValueForRendering(partialTicks);

            Vector4d vec = entity.getCarDimensions();
            Vector2d rot = entity.getBackWheelRotationPoint();

            GlStateManager.translate(0, rot.x, rot.y);
            float localRotationPitch = (float) MathUtils.cosineFromPoints(new Vec3d(frontValue, 0, vec.w), new Vec3d(backValue, 0, vec.w), new Vec3d(backValue, 0, vec.y));// No need for cosine as is a right angled triangle. I'm to lazy to
            // work out the right maths. //TODO: SOHCAHTOA this
            GlStateManager.rotate(frontValue < backValue ? -localRotationPitch : localRotationPitch, 1, 0, 0);
            GlStateManager.translate(0, -rot.x, -rot.y);
            float localRotationRoll = (float) MathUtils.cosineFromPoints(new Vec3d(rightValue, 0, vec.z), new Vec3d(leftValue, 0, vec.z), new Vec3d(leftValue, 0, vec.x));// TODO: same as above
            GlStateManager.rotate(leftValue < rightValue ? localRotationRoll : -localRotationRoll, 0, 0, 1);
        }
    }

    protected abstract HelicopterAnimator createCarAnimator();

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(E entity) {
        return this.texture;
    }
}
