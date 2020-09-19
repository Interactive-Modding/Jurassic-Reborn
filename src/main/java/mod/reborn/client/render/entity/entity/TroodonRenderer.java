package mod.reborn.client.render.entity.entity;

import mod.reborn.RebornMod;
import mod.reborn.client.render.entity.DinosaurRenderer;
import mod.reborn.client.render.entity.dinosaur.DinosaurRenderInfo;
import mod.reborn.server.entity.DinosaurEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TroodonRenderer extends DinosaurRenderer {
    public TroodonRenderer(DinosaurRenderInfo renderInfo, RenderManager renderManager) {
        super(renderInfo, renderManager);
        this.addLayer(new GlowingEyesLayer(this));
    }


    @SideOnly(Side.CLIENT)
    public static class GlowingEyesLayer implements LayerRenderer<DinosaurEntity> {
        private final DinosaurRenderer renderer;

        public GlowingEyesLayer(DinosaurRenderer renderer) {
            this.renderer = renderer;
        }

        @Override
        public void doRenderLayer(DinosaurEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
            if (!entitylivingbaseIn.isInvisible() && !entitylivingbaseIn.areEyelidsClosed() && !entitylivingbaseIn.getEntityWorld().isDaytime()) {
                ResourceLocation texture = new ResourceLocation(RebornMod.MODID, "textures/entities/troodon/troodon_eyes.png");
                ITextureObject textureObject = Minecraft.getMinecraft().getTextureManager().getTexture(texture);
                if (textureObject != TextureUtil.MISSING_TEXTURE) {
                    this.renderer.bindTexture(texture);
                    int i = 61680;
                    int j = i % 65536;
                    int k = 0;
                    OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) j, (float) k);
                    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                    this.renderer.getMainModel().render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
                    i = entitylivingbaseIn.getBrightnessForRender();
                    j = i % 65536;
                    k = i / 65536;
                    OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) j, (float) k);
                    this.renderer.setLightmap(entitylivingbaseIn);

                }
            }
        }

        public boolean shouldCombineTextures()
        {
            return true;
        }
    }
}
