package mod.reborn.client.render.entity;

import mod.reborn.client.model.animation.entity.vehicle.CarAnimator;
import mod.reborn.server.entity.vehicle.GyrosphereEntity;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GyrosphereRenderer extends CarRenderer<GyrosphereEntity> {

    public GyrosphereRenderer(RenderManager manager) {
        super(manager, "gyrosphere");
    }

    @Override
    protected CarAnimator createCarAnimator() {
        return new CarAnimator()
                .addDoor(new CarAnimator.Door("door left main", 0, true))
                .addDoor(new CarAnimator.Door("door right main", 1, false));
    }
}