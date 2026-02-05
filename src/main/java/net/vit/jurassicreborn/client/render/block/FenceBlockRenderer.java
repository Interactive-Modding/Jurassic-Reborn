package net.vit.jurassicreborn.client.render.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.vit.jurassicreborn.client.model.ElectricFenceModels;
import net.vit.jurassicreborn.common.blocks.entities.fence.ElectricFenceBaseBlock;
import net.vit.jurassicreborn.common.blocks.entities.fence.ElectricFenceBaseBlockEntity;
import net.vit.jurassicreborn.common.blocks.entities.incubator.IncubatorBlockEntity;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

public class FenceBlockRenderer extends GeoBlockRenderer<ElectricFenceBaseBlockEntity> {

    public FenceBlockRenderer(BlockEntityRendererProvider.Context context) {
        super(context, new ElectricFenceBaseModel());
    }

    // FenceBlockRenderer.java
    @Override
    public void render(ElectricFenceBaseBlockEntity be, float pt,
                       PoseStack pose, MultiBufferSource buf, int light) {

        var st      = be.getBlockState();
        var block   = (ElectricFenceBaseBlock) st.getBlock();
        var variant = ElectricFenceModels.resolve(st, block.getType());

        pose.pushPose();

        /* centre → rotate → un-centre */
        pose.translate(0.5, 0.0, 0.5);
        pose.mulPose(Vector3f.YP.rotationDegrees(variant.yRot()));
        pose.translate(-0.5, 0.0, -0.5);

        super.render(be, pt, pose, buf, light);

        pose.popPose();
    }
}
