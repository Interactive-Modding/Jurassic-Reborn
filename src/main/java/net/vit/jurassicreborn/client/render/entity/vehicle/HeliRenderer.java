package net.vit.jurassicreborn.client.render.entity.vehicle;

import net.vit.jurassicreborn.common.entities.vehicle.HelicopterEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class HeliRenderer extends HelicopterRenderer<HelicopterEntity> {

    public HeliRenderer(EntityRendererProvider.Context context) {
        super(context, "helicopter");
    }

    @Override
    protected HelicopterAnimator createCarAnimator() {
        return new HelicopterAnimator();
    }
}
