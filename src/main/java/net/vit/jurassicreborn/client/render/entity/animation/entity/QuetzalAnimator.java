package net.vit.jurassicreborn.client.render.entity.animation.entity;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import net.vit.jurassicreborn.client.model.AnimatableModel;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimator;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.QuetzalEntity;
import net.neoforged.api.distmarker.Dist;import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class QuetzalAnimator extends AbstractPterosaurAnimator<QuetzalEntity> {
    @Override
    protected String headCube() {
        return "Head ";
    }

    @Override
    protected String altHeadCube() {
        return "Head";
    }


}