package net.vit.jurassicreborn.client.render.entity.vehicle;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.vit.jurassicreborn.common.entities.vehicle.FordExplorerSnowEntity;
import net.vit.jurassicreborn.common.entities.vehicle.MonorailEntity;

public class MonorailRenderer extends CarRenderer<MonorailEntity> {

    /* ------------------------------------------------------------------ */
    /*  provide the animator for this model                               */
    /* ------------------------------------------------------------------ */
    private static CarAnimator makeAnimator() {
        return new CarAnimator()
                .addDoor(new CarAnimator.Door("door left main", 0, true))
                .addDoor(new CarAnimator.Door("door right main", 1, false))
                .addDoor(new CarAnimator.Door("Back door left main", 2, true))
                .addDoor(new CarAnimator.Door("Back door right main", 3, false));
    }

    /* ------------------------------------------------------------------ */
    /*  ctor used by the registry / factory                               */
    /* ------------------------------------------------------------------ */
    public MonorailRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, "monorail", makeAnimator());
    }

    /* ------------------------------------------------------------------ */
    /*  required by CarRenderer, returns the same animator instance type  */
    /* ------------------------------------------------------------------ */
    @Override
    protected CarAnimator createCarAnimator() {
        return makeAnimator();
    }
}
