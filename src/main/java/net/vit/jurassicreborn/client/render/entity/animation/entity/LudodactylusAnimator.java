package net.vit.jurassicreborn.client.render.entity.animation.entity;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import net.vit.jurassicreborn.client.model.AnimatableModel;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimator;
import net.neoforged.api.distmarker.Dist;import net.neoforged.api.distmarker.OnlyIn;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.LudodactylusEntity;

@OnlyIn(Dist.CLIENT)
public class LudodactylusAnimator extends AbstractPterosaurAnimator<LudodactylusEntity> {
    @Override
    protected float groundSpeed() {
        return 0.45F;
    }

    @Override
    protected float groundDegree() {
        return 1.0F;
    }

    @Override
    protected float groundHeight() {
        return 1.0F;
    }
}