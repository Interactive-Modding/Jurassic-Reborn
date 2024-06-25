package mod.reborn.client.render.entity;

import mod.reborn.client.model.animation.entity.vehicle.CarAnimator;

import mod.reborn.server.entity.vehicle.MonorailEntity;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class MonorailRenderer extends CarRenderer<MonorailEntity> {

    public MonorailRenderer(RenderManager manager) {
        super(manager, "monorail");
    }

    @Override
    protected CarAnimator createCarAnimator() {
        return new CarAnimator()
                .addDoor(new CarAnimator.Door("door left main", 0, true))
                .addDoor(new CarAnimator.Door("door right main", 1, false))
                .addDoor(new CarAnimator.Door("Back door left main", 2, true))
                .addDoor(new CarAnimator.Door("Back door right main", 3, false));
    }
}