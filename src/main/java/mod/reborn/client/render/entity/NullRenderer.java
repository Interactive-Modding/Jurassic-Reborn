package mod.reborn.client.render.entity;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class NullRenderer extends Render<Entity> {

    public NullRenderer(RenderManager renderManager) {
	super(renderManager);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
	return null;
    }

}
