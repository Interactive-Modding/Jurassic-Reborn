package net.vit.jurassicreborn.client.model;

import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

/**
 * Replacement for the legacy Tabula renderer. The modern client already handles offsets,
 * so we just delegate to AdvancedModelBox#render.
 */
public class FixedModelRenderer extends AdvancedModelBox {

    public FixedModelRenderer(AdvancedEntityModel<?> model, String name) {
        super(model, name);
    }

    @Override
    public void render(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay) {
        super.render(poseStack, buffer, packedLight, packedOverlay);
    }
}
