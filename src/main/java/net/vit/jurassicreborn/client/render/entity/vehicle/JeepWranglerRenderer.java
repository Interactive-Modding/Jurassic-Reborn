package net.vit.jurassicreborn.client.render.entity.vehicle;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.vit.jurassicreborn.common.entities.vehicle.JeepWranglerEntity;
import net.vit.jurassicreborn.common.entities.vehicle.SornaJeepWranglerEntity;

public class JeepWranglerRenderer extends CarRenderer<JeepWranglerEntity> {

    /* ------------------------------------------------------------------ */
    /*  provide the animator for this model                               */
    /* ------------------------------------------------------------------ */
    private static CarAnimator makeAnimator() {
        return new CarAnimator()
                .addDoor(new CarAnimator.Door("door left main", 0, true))
                .addDoor(new CarAnimator.Door("door right main", 1, false));
    }

    /* ------------------------------------------------------------------ */
    /*  ctor used by the registry / factory                               */
    /* ------------------------------------------------------------------ */
    public JeepWranglerRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, "jeep_wrangler", makeAnimator());
    }

    /* ------------------------------------------------------------------ */
    /*  required by CarRenderer, returns the same animator instance type  */
    /* ------------------------------------------------------------------ */
    @Override
    protected CarAnimator createCarAnimator() {
        return makeAnimator();
    }
}
