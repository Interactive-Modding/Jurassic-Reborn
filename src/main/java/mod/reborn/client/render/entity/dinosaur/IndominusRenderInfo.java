package mod.reborn.client.render.entity.dinosaur;

import mod.reborn.client.model.animation.entity.IndominusAnimator;
import mod.reborn.client.render.entity.entity.IndominusRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import mod.reborn.server.entity.DinosaurEntity;
import mod.reborn.server.entity.EntityHandler;

public class IndominusRenderInfo extends DinosaurRenderInfo
{
    public IndominusRenderInfo(float parShadowSize)
    {
        super(EntityHandler.INDOMINUS, new IndominusAnimator(), parShadowSize);
    }

    @Override
    public Render<? super DinosaurEntity> createRenderFor(RenderManager manager)
    {
        return new IndominusRenderer(this, manager);
    }
}
