package mod.reborn.client.render.entity;

import java.util.stream.IntStream;

import javax.annotation.Nullable;
import javax.vecmath.Vector2d;
import javax.vecmath.Vector4d;

import mod.reborn.client.model.animation.entity.vehicle.CarAnimator;
import mod.reborn.server.entity.vehicle.VehicleEntity;
import net.ilexiconn.llibrary.client.model.tabula.TabulaModel;
import net.ilexiconn.llibrary.client.model.tabula.container.TabulaModelContainer;
import net.minecraft.client.renderer.RenderHelper;
import mod.reborn.RebornMod;
import mod.reborn.client.model.TabulaModelUV;
import mod.reborn.server.entity.ai.util.MathUtils;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import mod.reborn.server.entity.vehicle.HelicopterEntity;
import mod.reborn.server.tabula.TabulaModelHelper;

@SideOnly(Side.CLIENT)
public abstract class CarRenderer<E extends VehicleEntity> extends Render<E> {
    private static final ResourceLocation[] DESTROY_STAGES = IntStream.range(0, 10).mapToObj(n -> new ResourceLocation(String.format("textures/blocks/destroy_stage_%d.png", n))).toArray(ResourceLocation[]::new);



    protected final String carName;
    protected CarAnimator animator;
    protected final ResourceLocation texture;
    protected TabulaModel baseModel;
    protected TabulaModel destroyModel;

    protected CarRenderer(RenderManager renderManager, String carName) {
        super(renderManager);
        this.carName = carName;
        this.animator = createCarAnimator();
        texture = new ResourceLocation(RebornMod.MODID, "textures/entities/" + carName + "/" + carName + ".png");
        try {
            TabulaModelContainer container = TabulaModelHelper.loadTabulaModel("/assets/rebornmod/models/entities/" + carName + "/" + carName + ".tbl");
            this.baseModel = new TabulaModel(container, animator);
            this.destroyModel = new TabulaModel(new TabulaModelUV(container, 16, 16), animator);
        } catch (Exception e) {
            throw new RuntimeException("Unable to load car " + carName,e);
        }

    }

    @Override
    public void doRender(E entity, double x, double y, double z, float yaw, float partialTicks) {
        this.animator.partialTicks = partialTicks;
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        this.bindEntityTexture(entity);
        this.renderModel(entity, x, y, z, yaw, partialTicks, false);
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
        GlStateManager.disableBlend();
        super.doRender(entity, x, y, z, yaw, partialTicks);
    }

    protected void renderModel(E entity, double x, double y, double z, float yaw, float partialTicks, boolean destroy) {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x, (float) y + 1.25F, (float) z);
        GlStateManager.rotate(180 - yaw, 0, 1, 0);
        this.doCarRotations(entity, partialTicks);
        GlStateManager.scale(-1, -1, 1);
        (destroy ? this.destroyModel : this.baseModel).render(entity, 0, 0, 0, 0, 0, 0.0625F);
        GlStateManager.popMatrix();
    }

    protected void doCarRotations(VehicleEntity entity, float partialTicks) {
        if(!(entity instanceof HelicopterEntity)) {
            double backValue = entity.backValue.getValueForRendering(partialTicks);
            double frontValue = entity.frontValue.getValueForRendering(partialTicks);
            double leftValue = entity.leftValue.getValueForRendering(partialTicks);
            double rightValue = entity.rightValue.getValueForRendering(partialTicks);

            Vector4d vec = entity.getCarDimensions();
            Vector2d rot = entity.getBackWheelRotationPoint();

            GlStateManager.translate(0, rot.x, rot.y);
            float localRotationPitch = (float) MathUtils.cosineFromPoints(new Vec3d(frontValue, 0, vec.w), new Vec3d(backValue, 0, vec.w), new Vec3d(backValue, 0, vec.y));//No need for cosine as is a right angled triangle. I'm to lazy to work out the right maths. //TODO: SOHCAHTOA this
            GlStateManager.rotate(frontValue < backValue ? -localRotationPitch : localRotationPitch, 1, 0, 0);
            GlStateManager.translate(0, -rot.x, -rot.y);
            float localRotationRoll = (float) MathUtils.cosineFromPoints(new Vec3d(rightValue, 0, vec.z), new Vec3d(leftValue, 0, vec.z), new Vec3d(leftValue, 0, vec.x));//TODO: same as above
            GlStateManager.rotate(leftValue < rightValue ? localRotationRoll : -localRotationRoll, 0, 0, 1);
            entity.pitch = frontValue < backValue ? localRotationPitch : -localRotationPitch;
            entity.roll= leftValue < rightValue ? localRotationRoll : -localRotationRoll;
//            entity.tmpY=(float) rot.x;
//            entity.tmpZ = (float) rot.y;
        }
    }

    protected abstract CarAnimator createCarAnimator();

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(E entity) {
        return this.texture;
    }
}