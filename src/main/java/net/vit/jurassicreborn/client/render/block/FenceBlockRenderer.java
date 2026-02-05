package net.vit.jurassicreborn.client.render.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.vit.jurassicreborn.client.model.ElectricFenceModels;
import net.vit.jurassicreborn.client.render.block.ElectricFenceBaseModel;
import net.vit.jurassicreborn.common.blocks.entities.fence.ElectricFenceBaseBlock;
import net.vit.jurassicreborn.common.blocks.entities.fence.ElectricFenceBaseBlockEntity;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class FenceBlockRenderer implements BlockEntityRenderer<ElectricFenceBaseBlockEntity> {

    private final GeoBlockRenderer<ElectricFenceBaseBlockEntity> delegate;

    public FenceBlockRenderer(BlockEntityRendererProvider.Context context) {
        this.delegate = new GeoBlockRenderer<>(new ElectricFenceBaseModel()) {};
    }

    @Override
    public void render(ElectricFenceBaseBlockEntity be,
                       float pt,
                       PoseStack pose,
                       MultiBufferSource buf,
                       int light,
                       int overlay) {

        var st      = be.getBlockState();
        var block   = (ElectricFenceBaseBlock) st.getBlock();
        var variant = ElectricFenceModels.resolve(st, block.getType());

        pose.pushPose();

        /* centre → rotate → un-centre */
        pose.translate(0.5, 0.0, 0.5);
        pose.mulPose(Axis.YP.rotationDegrees(variant.yRot()));
        pose.translate(-0.5, 0.0, -0.5);

        delegate.render(be, pt, pose, buf, light, overlay);

        pose.popPose();
    }
}
