package mod.reborn.client.render.entity;

import mod.reborn.RebornMod;
import mod.reborn.client.model.AnimatableModel;
import mod.reborn.client.model.animation.entity.CrabAnimator;
import mod.reborn.client.model.animation.entity.SharkAnimator;
import mod.reborn.server.entity.animal.EntityCrab;
import mod.reborn.server.entity.animal.EntityShark;
import mod.reborn.server.tabula.TabulaModelHelper;
import net.ilexiconn.llibrary.client.model.tabula.container.TabulaModelContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;

@SideOnly(Side.CLIENT)
public class SharkRenderer extends RenderLiving<EntityShark> {
    private static ResourceLocation SHARK_TEXTURE;

    private static final AnimatableModel SHARK;
    static {
        TabulaModelContainer shark = null;
        try {
            shark = TabulaModelHelper.loadTabulaModel("assets/rebornmod/models/entities/shark/adult/shark");
        } catch (IOException e) {
            e.printStackTrace();
        }
        SHARK = new AnimatableModel(shark, new SharkAnimator());
        SHARK_TEXTURE = new ResourceLocation(RebornMod.MODID, "textures/entities/shark/shark.png");
    }


    public SharkRenderer(RenderManager renderManager) {
        super(renderManager, SHARK, 0.0F);
    }

    @Override
    public void preRenderCallback(EntityShark entity, float partialTick) {
        float scale = entity.isChild() ? 0.2F : 1.7F;
        GlStateManager.scale(scale, scale, scale);
    }

    @Override
    public void doRender(EntityShark entity, double x, double y, double z, float entityYaw, float partialTicks) {
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    @Override
    public ResourceLocation getEntityTexture(EntityShark entity) {
        return SHARK_TEXTURE;
    }
}
