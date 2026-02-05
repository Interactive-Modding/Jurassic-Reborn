package net.vit.jurassicreborn.client.render.entity.vehicle;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.vit.jurassicreborn.common.entities.vehicle.BlackJeepWranglerEntity;
import net.vit.jurassicreborn.common.entities.vehicle.BlueJeepWranglerEntity;

public class BlackJeepWranglerRenderer extends CarRenderer<BlackJeepWranglerEntity> {

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
    public BlackJeepWranglerRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, "black_jeep_wrangler", makeAnimator());
    }

    /* ------------------------------------------------------------------ */
    /*  required by CarRenderer, returns the same animator instance type  */
    /* ------------------------------------------------------------------ */
    @Override
    protected CarAnimator createCarAnimator() {
        return makeAnimator();
    }
}
