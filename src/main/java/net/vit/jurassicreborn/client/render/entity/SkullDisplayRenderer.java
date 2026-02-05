package net.vit.jurassicreborn.client.render.entity;

import com.github.alexthe666.citadel.client.model.container.TabulaModelContainer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.common.blocks.ModBlocks;
import net.vit.jurassicreborn.common.blocks.SkullDisplayBlock;
import net.vit.jurassicreborn.common.blocks.entities.SkullDisplayBlockEntity;
import net.vit.jurassicreborn.common.legacy.tabula.TabulaModelHelper;
import com.github.alexthe666.citadel.client.model.TabulaModel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Locale;

@OnlyIn(Dist.CLIENT)
public class SkullDisplayRenderer implements BlockEntityRenderer<SkullDisplayBlockEntity> {

    public SkullDisplayRenderer(BlockEntityRendererProvider.Context ctx) {}

    @Override
    public void render(SkullDisplayBlockEntity tile, float partialTicks,
                       PoseStack poseStack, MultiBufferSource buffer,
                       int packedLight, int packedOverlay) {

        BlockState state = tile.getBlockState();
        if (state.getBlock() != ModBlocks.SKULL_DISPLAY.get()) return;


        boolean horizontal = state.getValue(SkullDisplayBlock.FACING).getAxis() == Direction.Axis.Y;

        // Load Tabula model & texture if needed
        if (tile.model == null && tile.hasData()) {
            try {
                String dino = tile.getDinosaur().getName()
                        .toLowerCase(Locale.ENGLISH)
                        .replace(' ', '_');
                String modelOrient   = horizontal ? "horizontal" : "vertical";
                String textureOrient = horizontal ? "vertical"   : "horizontal";

                TabulaModelContainer container = TabulaModelHelper.loadTabulaModel(
                        new ResourceLocation(JurassicReborn.MODID,
                                "models/block/skull_display/" + dino + '_' + modelOrient));
                tile.model = new TabulaModel(container);
                if (container != null && container.getScale() != null && container.getScale().length >= 3) {
                    tile.modelScale = new float[] {
                            (float) container.getScale()[0],
                            (float) container.getScale()[1],
                            (float) container.getScale()[2]
                    };
                }

                // NOTE: textures/**block**/ (singular) is correct for 1.19+
                tile.texture = new ResourceLocation(JurassicReborn.MODID,
                        "textures/block/skull_display/" + dino + '_' +
                                (tile.isFossilized() ? "fossilized" : "fresh") + '_' +
                                textureOrient + ".png");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        poseStack.pushPose();
        poseStack.translate(0.5, 0.5, 0.5);
        poseStack.mulPose(Vector3f.YP.rotationDegrees(tile.getAngle()));

        float sx = tile.modelScale[0];
        float sy = tile.modelScale[1];
        float sz = tile.modelScale[2];
        poseStack.scale(1.6F * sx, -1.6F * sy, 1.6F * sz);

        if (!horizontal) {
            poseStack.translate(0, 0.15F * sy, -0.18F * sz); // scale aware!
        }
        poseStack.translate(0.0F, -1.194F * sy, 0.0F); // scale aware!

        if (tile.model != null) {
            VertexConsumer vc = buffer.getBuffer(RenderType.entityCutoutNoCull(tile.texture));
            tile.model.renderToBuffer(poseStack, vc,
                    packedLight, packedOverlay,
                    1.0F, 1.0F, 1.0F, 1.0F);
        }
        poseStack.popPose();
    }
}
