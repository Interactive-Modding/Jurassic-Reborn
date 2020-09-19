package mod.reborn.client.render.entity.dinosaur;

import mod.reborn.client.model.animation.entity.IndominusAnimator;
import mod.reborn.client.model.animation.entity.TroodonAnimator;
import mod.reborn.client.render.entity.entity.TroodonRenderer;
import mod.reborn.server.entity.DinosaurEntity;
import mod.reborn.server.entity.EntityHandler;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;

public class TroodonRenderInfo extends DinosaurRenderInfo
{
    public TroodonRenderInfo(float parShadowSize)
    {
        super(EntityHandler.TROODON, new TroodonAnimator(), parShadowSize);
    }

    @Override
    public Render<? super DinosaurEntity> createRenderFor(RenderManager manager)
    {
        return new TroodonRenderer(this, manager);
    }
}
