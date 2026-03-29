package net.vit.jurassicreborn.client.render.entity.animation.entity;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import net.vit.jurassicreborn.client.model.AnimatableModel;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimator;
import net.neoforged.api.distmarker.Dist;import net.neoforged.api.distmarker.OnlyIn;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.ZhenyuanopterusEntity;

@OnlyIn(Dist.CLIENT)
public class ZhenyuanopterusAnimator extends AbstractPterosaurAnimator<ZhenyuanopterusEntity> {
    @Override
    protected float groundDegree() {
        return 1.0F;
    }

    @Override
    protected float frontOffset() {
        return -0.35F;
    }
}