package net.vit.jurassicreborn.client.render.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimation;
import net.vit.jurassicreborn.common.blocks.entities.ActionFigureBlockEntity;
import net.vit.jurassicreborn.common.entities.DinosaurEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import org.jetbrains.annotations.NotNull;

public class DisplayBlockEntityRender implements BlockEntityRenderer<ActionFigureBlockEntity> {
    public static final int ACTION_FIGURE_VARIANT = 0;
    private static final float BASE_ACTION_FIGURE_SCALE = 0.15f;
    private static final float MINIMUM_ACTION_FIGURE_HEIGHT = 0.25f;
    private static final float SKELETON_SCALE = 1.0f;

    public DisplayBlockEntityRender(){
        super();
    }


    @Override
    public void render(ActionFigureBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, @NotNull MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
        pPoseStack.pushPose();
//        pPoseStack.translate(pBlockEntity.getBlockPos().getX(), pBlockEntity.getBlockPos().getY(), pBlockEntity.getBlockPos().getZ());

//        pPoseStack.mulPose(Quaternion.fromXYZDegrees(new Vector3f(0.0f, (float)pBlockEntity.getRot(), 0.0f)));

        DinosaurEntity entity = pBlockEntity.getEntity();
        if (entity == null) {
            pPoseStack.popPose();
            return;
        }

        pPoseStack.translate(0.5, 0, 0.5);

        float scale = entity.isSkeleton() ? SKELETON_SCALE : BASE_ACTION_FIGURE_SCALE;
        if (!entity.isSkeleton()) {
            float baseHeight = entity.getDimensions(entity.getPose()).height;
            float scaledHeight = baseHeight * scale;
            if (scaledHeight > 0.0f && scaledHeight < MINIMUM_ACTION_FIGURE_HEIGHT) {
                scale *= MINIMUM_ACTION_FIGURE_HEIGHT / scaledHeight;
            }
        }
        pPoseStack.scale(scale, scale, scale);

        pPoseStack.mulPose(Vector3f.YP.rotationDegrees(pBlockEntity.getRot()));




        if (entity.getAnimation() != EntityAnimation.IDLE.get()) {
            entity.setAnimation(EntityAnimation.IDLE.get());
        }//            Entity cam = Minecraft.getInstance();
//            cam.getPos
//            Camera cam = Minecraft.getInstance().gameRenderer.getMainCamera();
//            Vec3 pos = cam.getPosition();
//            float f = Mth.lerp(pPartialTick, entity.yRotO, entity.getYRot());


        Minecraft.getInstance().getEntityRenderDispatcher().render(entity, 0, 0, 0, 0, 0f, pPoseStack, pBufferSource, pPackedLight);
        pPoseStack.popPose();

    }

}
