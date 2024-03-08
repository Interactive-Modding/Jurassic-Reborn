package mod.reborn.client.render.entity;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import mod.reborn.server.entity.VenomEntity;


@SideOnly(Side.CLIENT)
public class VenomRenderer implements IRenderFactory<VenomEntity> {

    @Override
    public Render<? super VenomEntity> createRenderFor(RenderManager manager) {
        return new Renderer(manager);
    }

    public static class Renderer extends Render<VenomEntity> {
        protected Renderer(RenderManager renderManager) {
            super(renderManager);
        }

        @Override
        protected ResourceLocation getEntityTexture(VenomEntity entity) {
            return null;
        }
    }
}