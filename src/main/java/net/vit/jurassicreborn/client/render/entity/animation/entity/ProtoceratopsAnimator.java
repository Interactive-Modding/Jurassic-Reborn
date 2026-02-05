package net.vit.jurassicreborn.client.render.entity.animation.entity;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import net.vit.jurassicreborn.client.model.AnimatableModel;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimator;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.ProtoceratopsEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ProtoceratopsAnimator extends EntityAnimator<ProtoceratopsEntity> {
    @Override
    protected void performAnimations(AnimatableModel model, ProtoceratopsEntity entity, float f, float f1, float ticks, float rotationYaw, float rotationPitch, float scale) {
        {
            float globalSpeed = 0.5F;
            float globalHeight = 0.5F;

            AdvancedModelBox head = model.getCube("Head1");
            AdvancedModelBox neck1 = model.getCube("Neck");
            AdvancedModelBox body = model.getCube("Hips");

            AdvancedModelBox tail1 = model.getCube("Tail1");
            AdvancedModelBox tail2 = model.getCube("Tail2");
            AdvancedModelBox tail3 = model.getCube("Tail3");
            AdvancedModelBox tail4 = model.getCube("Tail4");
            AdvancedModelBox tail5 = model.getCube("Tail5");

            AdvancedModelBox thighLeft = model.getCube("legThighLeft");
            AdvancedModelBox thighRight = model.getCube("legThighRight");

            AdvancedModelBox[] tail = new AdvancedModelBox[]{tail5, tail4, tail3, tail2, tail1};
            AdvancedModelBox[] neck = new AdvancedModelBox[]{head, neck1};

            model.bob(body, globalSpeed * 1.0F, globalHeight * 1.0F, false, f, f1);
            model.bob(thighLeft, globalSpeed * 1.0F, globalHeight * 0.9F, false, f, f1);
            model.bob(thighRight, globalSpeed * 1.0F, globalHeight * 1.0F, false, f, f1);

            model.chainWave(tail, globalSpeed * 1.0F, globalHeight * 0.25F, 3, f, f1);
            model.chainSwing(tail, globalSpeed * 0.5F, globalHeight * 0.25F, 3, f, f1);
            model.chainWave(neck, globalSpeed * 1.0F, globalHeight * 0.25F, -3, f, f1);

            model.chainWave(tail, globalSpeed * 0.25F, globalHeight * 1.0F, 3, ticks, 0.025F);
            model.chainWave(neck, globalSpeed * 0.25F, globalHeight * 1.0F, -3, ticks, 0.025F);

            entity.tailBuffer.applyChainSwingBuffer(tail);
        }
    }
}
