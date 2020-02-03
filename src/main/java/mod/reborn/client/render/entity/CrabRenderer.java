package mod.reborn.client.render.entity;

import mod.reborn.RebornMod;
import mod.reborn.client.model.AnimatableModel;
import mod.reborn.client.model.animation.entity.CrabAnimator;
import mod.reborn.client.model.animation.entity.GoatAnimator;
import mod.reborn.server.entity.animal.EntityCrab;
import mod.reborn.server.entity.animal.GoatEntity;
import mod.reborn.server.tabula.TabulaModelHelper;
import net.ilexiconn.llibrary.client.model.tabula.container.TabulaModelContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.util.EnumMap;
import java.util.Locale;

@SideOnly(Side.CLIENT)
public class CrabRenderer extends RenderLiving<EntityCrab> {
    private static ResourceLocation CRAB_TEXTURE;

    private static final AnimatableModel CRAB;
    static {
        TabulaModelContainer crab = null;
        try {
            crab = TabulaModelHelper.loadTabulaModel("assets/rebornmod/models/entities/crab/adult/crab");
        } catch (IOException e) {
            e.printStackTrace();
        }
        CRAB = new AnimatableModel(crab, new CrabAnimator());
        CRAB_TEXTURE = new ResourceLocation(RebornMod.MODID, "textures/entities/crab/crab.png");
    }


    public CrabRenderer(RenderManager renderManager) {
        super(renderManager, CRAB, 0.4F);
    }

    @Override
    public void preRenderCallback(EntityCrab entity, float partialTick) {
        float scale = entity.isChild() ? 0.2F : 0.47F;
        GlStateManager.scale(scale, scale, scale);
    }

    @Override
    public void doRender(EntityCrab entity, double x, double y, double z, float entityYaw, float partialTicks) {
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    @Override
    public ResourceLocation getEntityTexture(EntityCrab entity) {
        return CRAB_TEXTURE;
    }
}
