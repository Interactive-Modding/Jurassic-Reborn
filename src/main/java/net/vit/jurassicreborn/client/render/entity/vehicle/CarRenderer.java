package net.vit.jurassicreborn.client.render.entity.vehicle;

import com.github.alexthe666.citadel.client.model.TabulaModel;
import com.github.alexthe666.citadel.client.model.container.TabulaModelContainer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.client.model.TabulaModelUV;
import net.vit.jurassicreborn.common.JurassicConfig;
import net.vit.jurassicreborn.common.entities.vehicle.HelicopterEntity;
import net.vit.jurassicreborn.common.entities.vehicle.VehicleEntity;
import net.vit.jurassicreborn.common.legacy.tabula.TabulaModelHelper;
import net.vit.jurassicreborn.common.util.MathUtils;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector4f;

import java.util.stream.IntStream;

public abstract class CarRenderer<E extends VehicleEntity> extends EntityRenderer<E> {

    private static final ResourceLocation[] DESTROY_STAGES =
            IntStream.range(0, 10)
                    .mapToObj(i -> ResourceLocation.withDefaultNamespace("textures/block/destroy_stage_" + i + ".png"))
                    .toArray(ResourceLocation[]::new);

    protected final String carName;
    protected final CarAnimator animator;
    protected final ResourceLocation texture;
    protected final TabulaModel baseModel;
    protected final TabulaModel destroyModel;

    protected CarRenderer(EntityRendererProvider.Context ctx, String carName) {
        this(ctx, carName, null);
    }

    protected CarRenderer(EntityRendererProvider.Context ctx, String carName, @Nullable CarAnimator animator) {
        super(ctx);
        this.carName = carName;
        this.animator = animator != null ? animator : createCarAnimator();
        this.texture = ResourceLocation.fromNamespaceAndPath(
                JurassicReborn.MODID,
                "textures/entities/" + carName + "/" + carName + ".png"
        );

        try {
            String path = "/assets/jurassicreborn/models/entities/" + carName + "/" + carName + ".tbl";
            TabulaModelContainer container = TabulaModelHelper.loadTabulaModel(path);
            if (container == null) {
                throw new IllegalArgumentException("Tabula model not found: " + path);
            }

            this.baseModel = new TabulaModel(container, this.animator);
            this.destroyModel = new TabulaModel(new TabulaModelUV(container, 16, 16), this.animator);
        } catch (Exception ex) {
            throw new RuntimeException("Unable to load Tabula model for " + carName, ex);
        }
    }

    @Override
    public void render(E entity,
                       float entityYaw,
                       float partialTicks,
                       PoseStack pose,
                       MultiBufferSource buffer,
                       int packedLight) {
        this.animator.partialTicks = partialTicks;

        pose.pushPose();
        pose.translate(0.0F, 1.25F, 0.0F);
        pose.mulPose(Axis.YP.rotationDegrees(180.0F - entityYaw));

        this.doCarRotations(entity, partialTicks, pose);

        // Tabula uses a left-handed setup
        pose.scale(-1.0F, -1.0F, 1.0F);

        // Keep the first file's main render behavior
        this.renderPass(
                this.baseModel,
                entity,
                partialTicks,
                pose,
                buffer.getBuffer(RenderType.entityCutout(this.getTextureLocation(entity))),
                packedLight
        );

        // Keep the second file's fixed destroy overlay behavior
        int destroyStage = Math.min(10, (int) (10.0F - (entity.getHealth() / VehicleEntity.MAX_HEALTH) * 10.0F)) - 1;
        if (destroyStage >= 0) {
            this.renderPass(
                    this.destroyModel,
                    entity,
                    partialTicks,
                    pose,
                    buffer.getBuffer(RenderType.crumbling(DESTROY_STAGES[destroyStage])),
                    packedLight
            );
        }

        pose.popPose();
        super.render(entity, entityYaw, partialTicks, pose, buffer, packedLight);
    }

    protected void renderPass(TabulaModel model,
                              E entity,
                              float partialTicks,
                              PoseStack pose,
                              VertexConsumer consumer,
                              int packedLight) {
        this.animator.setRotationAngles(
                model,
                entity,
                0.0F,
                0.0F,
                entity.tickCount + partialTicks,
                entity.getYRot(),
                entity.getXRot(),
                0.0625F
        );
        model.renderToBuffer(pose, consumer, packedLight, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
    }

    protected void doCarRotations(E entity, float partialTicks, PoseStack pose) {
        if (entity instanceof HelicopterEntity) {
            return;
        }

        double backValue = entity.backValue.getRenderValue(partialTicks);
        double frontValue = entity.frontValue.getRenderValue(partialTicks);
        double leftValue = entity.leftValue.getRenderValue(partialTicks);
        double rightValue = entity.rightValue.getRenderValue(partialTicks);

        Vector4f vec = entity.getCarDimensions();
        Vec2 rot = entity.getBackWheelRotationPoint();

        pose.translate(0.0F, rot.x, rot.y);

        float localRotationPitch = (float) MathUtils.cosineFromPoints(
                new Vec3(frontValue, 0.0D, vec.w()),
                new Vec3(backValue, 0.0D, vec.w()),
                new Vec3(backValue, 0.0D, vec.y())
        );
        localRotationPitch = Mth.clamp(localRotationPitch, -45.0F, 45.0F);
        if (JurassicConfig.enableVehicleTilting) {
            pose.mulPose(Axis.XP.rotationDegrees(frontValue < backValue ? -localRotationPitch : localRotationPitch));
        }

        float localRotationRoll = (float) MathUtils.cosineFromPoints(
                new Vec3(rightValue, 0.0D, vec.z()),
                new Vec3(leftValue, 0.0D, vec.z()),
                new Vec3(leftValue, 0.0D, vec.x())
        );
        localRotationRoll = Mth.clamp(localRotationRoll, -45.0F, 45.0F);
        if (JurassicConfig.enableVehicleTilting) {
            pose.mulPose(Axis.ZP.rotationDegrees(leftValue < rightValue ? localRotationRoll : -localRotationRoll));
        }

        pose.translate(0.0F, -rot.x, -rot.y);

        entity.pitch = JurassicConfig.enableVehicleTilting
                ? (frontValue < backValue ? localRotationPitch : -localRotationPitch)
                : 0.0F;
        entity.roll = JurassicConfig.enableVehicleTilting
                ? (leftValue < rightValue ? localRotationRoll : -localRotationRoll)
                : 0.0F;
    }

    @Nullable
    @Override
    public ResourceLocation getTextureLocation(E entity) {
        return this.texture;
    }

    protected static <T extends VehicleEntity> EntityRendererProvider<T> factory(
            String carName,
            java.util.function.Supplier<CarAnimator> supplier
    ) {
        return ctx -> {
            CarAnimator animator = supplier.get();
            return new CarRenderer<T>(ctx, carName, animator) {
                @Override
                protected CarAnimator createCarAnimator() {
                    return animator;
                }
            };
        };
    }

    protected abstract CarAnimator createCarAnimator();
}