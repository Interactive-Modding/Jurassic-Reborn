package net.vit.jurassicreborn.client.render.entity.vehicle;

import com.github.alexthe666.citadel.client.model.TabulaModel;
import com.github.alexthe666.citadel.client.model.container.TabulaModelContainer;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.common.RebornConfig;
import net.vit.jurassicreborn.common.entities.vehicle.VehicleEntity;
import net.vit.jurassicreborn.common.legacy.tabula.TabulaModelHelper;
import net.vit.jurassicreborn.common.util.MathUtils;
import org.jetbrains.annotations.Nullable;

import net.minecraft.world.phys.Vec2;
import com.mojang.math.Vector4f;

import java.util.stream.IntStream;

/**
 * Generic renderer for every Tabula‑based car model.
 * Concrete subclasses only have to provide a {@link CarAnimator} via the constructor
 * or by overriding {@link #createCarAnimator()}.
 */
public abstract class CarRenderer<E extends VehicleEntity> extends EntityRenderer<E> {

    /* --------------------------------------------------------------------- */
    /*  Destroy‑stage overlay                                               */
    /* --------------------------------------------------------------------- */

    private static final ResourceLocation[] DESTROY_STAGES =
            IntStream.range(0, 10)
                    .mapToObj(i -> new ResourceLocation("textures/block/destroy_stage_" + i + ".png"))
                    .toArray(ResourceLocation[]::new);

    /* --------------------------------------------------------------------- */
    /*  Instance fields                                                     */
    /* --------------------------------------------------------------------- */

    private final CarAnimator    animator;
    private final ResourceLocation texture;
    private final TabulaModel    model;

    /* --------------------------------------------------------------------- */
    /*  Ctor                                                                */
    /* --------------------------------------------------------------------- */

    protected CarRenderer(EntityRendererProvider.Context ctx,
                          String carName,
                          CarAnimator animator) {
        super(ctx);
        this.animator = animator;
        this.texture  = new ResourceLocation(JurassicReborn.MODID,
                "textures/entities/" + carName + "/" + carName + ".png");
        try {
            String path = "/assets/jurassicreborn/models/entities/" + carName + "/" + carName + ".tbl";
            TabulaModelContainer container = TabulaModelHelper.loadTabulaModel(path);
            if (container == null) {
                throw new IllegalArgumentException("Tabula model not found: " + path);
            }
            this.model = new TabulaModel(container, animator);
        } catch (Exception ex) {
            throw new RuntimeException("Unable to load Tabula model for " + carName, ex);
        }
    }

    /* --------------------------------------------------------------------- */
    /*  Render loop                                                         */
    /* --------------------------------------------------------------------- */

    @Override
    public void render(E car,
                       float entityYaw,
                       float partialTicks,
                       PoseStack pose,
                       MultiBufferSource buffer,
                       int packedLight) {

        this.animator.partialTicks = partialTicks;

        // ---------- root transform ----------
        pose.pushPose();
        pose.translate(0, 1.25F, 0);
        pose.mulPose(Vector3f.YP.rotationDegrees(180 - entityYaw));

        applySuspensionPitchRoll(car, partialTicks, pose);

        // Tabula uses left‑handed system
        pose.scale(-1, -1, 1);
        animator.setRotationAngles(
                model,
                car,
                0,
                0,
                car.tickCount + partialTicks,
                car.getYRot(),
                car.getXRot(),
                0.0625F
        );
        // ---------- main pass ----------------
        VertexConsumer vc = buffer.getBuffer(RenderType.entityCutout(getTextureLocation(car)));
        model.renderToBuffer(
                pose, vc,
                packedLight, OverlayTexture.NO_OVERLAY,
                1.0F, 1.0F, 1.0F, 1.0F);
        // ---------- damage overlay -----------
        int stage = Math.min(9, 9 - (int) (car.getHealth() / VehicleEntity.MAX_HEALTH * 10));
        if (stage >= 0) {
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            VertexConsumer damage = buffer.getBuffer(RenderType.entityTranslucent(DESTROY_STAGES[stage]));
            model.renderToBuffer(pose, damage, packedLight, OverlayTexture.NO_OVERLAY, 1, 1, 1, 0.5F);
            RenderSystem.disableBlend();
        }

        pose.popPose();
        super.render(car, entityYaw, partialTicks, pose, buffer, packedLight);
    }

    /* --------------------------------------------------------------------- */
    /*  Suspension helper                                                   */
    /* --------------------------------------------------------------------- */

    private static void applySuspensionPitchRoll(VehicleEntity car, float pt, PoseStack pose) {
        double back  = car.backValue .getRenderValue(pt);
        double front = car.frontValue.getRenderValue(pt);
        double left  = car.leftValue .getRenderValue(pt);
        double right = car.rightValue.getRenderValue(pt);

        Vector4f d   = car.getCarDimensions();
        Vec2 rot = car.getBackWheelRotationPoint();

        pose.translate(0, rot.x, rot.y);

        // pitch
        float pitch = (float) MathUtils.cosineFromPoints(
                new Vec3(front, 0, d.w()), new Vec3(back, 0, d.w()), new Vec3(back, 0, d.y()));
        pitch = Mth.clamp(pitch, -45f, 45f);
        if (RebornConfig.enableVehicleTilting)
            pose.mulPose(Vector3f.XP.rotationDegrees(back > front ? -pitch : pitch));        // roll
        float roll = (float) MathUtils.cosineFromPoints(
                new Vec3(right, 0, d.z()), new Vec3(left, 0, d.z()), new Vec3(left, 0, d.x()));
        roll = Mth.clamp(roll, -45f, 45f);
        if (RebornConfig.enableVehicleTilting)
            pose.mulPose(Vector3f.ZP.rotationDegrees(left > right ? -roll : roll));
        pose.translate(0, -rot.x, -rot.y);

        car.pitch = RebornConfig.enableVehicleTilting ? (back > front ? -pitch : pitch) : 0;
        car.roll  = RebornConfig.enableVehicleTilting ? (left > right ? -roll  : roll) : 0;
    }
    /* --------------------------------------------------------------------- */
    /*  Vanilla hooks                                                       */
    /* --------------------------------------------------------------------- */

    @Nullable
    @Override
    public ResourceLocation getTextureLocation(E entity) { return texture; }

    /* --------------------------------------------------------------------- */
    /*  Convenience factory                                                 */
    /* --------------------------------------------------------------------- */

    protected static <T extends VehicleEntity> EntityRendererProvider<T> factory(String carName,
                                                                                 java.util.function.Supplier<CarAnimator> sup) {
        return ctx -> new CarRenderer<T>(ctx, carName, sup.get()) {
            @Override
            protected CarAnimator createCarAnimator() { return sup.get(); }
        };
    }

    /* --------------------------------------------------------------------- */
    /*  Sub‑classes still may customise                                    */
    /* --------------------------------------------------------------------- */

    protected abstract CarAnimator createCarAnimator();
}
