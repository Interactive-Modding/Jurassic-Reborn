package mod.reborn.client.render.entity;

import mod.reborn.client.model.AnimatableModel;
import mod.reborn.client.model.animation.entity.GoatAnimator;
import net.ilexiconn.llibrary.client.model.tabula.container.TabulaModelContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import mod.reborn.RebornMod;
import mod.reborn.server.entity.animal.GoatEntity;
import mod.reborn.server.tabula.TabulaModelHelper;

import java.io.IOException;
import java.util.EnumMap;
import java.util.Locale;

@SideOnly(Side.CLIENT)
public class GoatRenderer extends RenderLiving<GoatEntity> {
    private static final EnumMap<GoatEntity.Variant, ResourceLocation> KID_TEXTURE = new EnumMap<>(GoatEntity.Variant.class);
    private static final EnumMap<GoatEntity.Variant, ResourceLocation> BILLY_TEXTURE = new EnumMap<>(GoatEntity.Variant.class);
    private static final EnumMap<GoatEntity.Variant, ResourceLocation> NANNY_TEXTURE = new EnumMap<>(GoatEntity.Variant.class);
    private static final AnimatableModel KID_MODEL;
    private static final AnimatableModel BILLY_MODEL;
    private static final AnimatableModel NANNY_MODEL;

    static {
        TabulaModelContainer kid = null;
        TabulaModelContainer billy = null;
        TabulaModelContainer nanny = null;
        try {
            kid = TabulaModelHelper.loadTabulaModel("assets/rebornmod/models/entities/goat_kid/adult/goat_kid");
            billy = TabulaModelHelper.loadTabulaModel("assets/rebornmod/models/entities/goat_billy/adult/goat_billy");
            nanny = TabulaModelHelper.loadTabulaModel("assets/rebornmod/models/entities/goat_nanny/adult/goat_nanny");
        } catch (IOException e) {
            e.printStackTrace();
        }
        KID_MODEL = new AnimatableModel(kid, new GoatAnimator());
        BILLY_MODEL = new AnimatableModel(billy, new GoatAnimator());
        NANNY_MODEL = new AnimatableModel(nanny, new GoatAnimator());
        for (GoatEntity.Variant variant : GoatEntity.Variant.values()) {
            String name = variant.name().toLowerCase(Locale.ENGLISH);
            KID_TEXTURE.put(variant, new ResourceLocation(RebornMod.MODID, "textures/entities/goat/kid_" + name + ".png"));
            NANNY_TEXTURE.put(variant, new ResourceLocation(RebornMod.MODID, "textures/entities/goat/nanny_" + name + ".png"));
            BILLY_TEXTURE.put(variant, new ResourceLocation(RebornMod.MODID, "textures/entities/goat/billy_" + name + ".png"));
        }
    }

    public GoatRenderer(RenderManager renderManager) {
        super(renderManager, BILLY_MODEL, 0.4F);
    }

    @Override
    public void preRenderCallback(GoatEntity entity, float partialTick) {
        float scale = entity.isChild() ? 0.2F : 0.47F;
        GlStateManager.scale(scale, scale, scale);
    }

    @Override
    public void doRender(GoatEntity entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GoatEntity.Type type = entity.getType();
        this.mainModel = type == GoatEntity.Type.KID ? KID_MODEL : type == GoatEntity.Type.BILLY ? BILLY_MODEL : NANNY_MODEL;
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    @Override
    public ResourceLocation getEntityTexture(GoatEntity entity) {
        GoatEntity.Type type = entity.getType();
        if (type == GoatEntity.Type.KID) {
            return KID_TEXTURE.get(entity.getVariant());
        } else if (type == GoatEntity.Type.BILLY) {
            return BILLY_TEXTURE.get(entity.getVariant());
        } else {
            return NANNY_TEXTURE.get(entity.getVariant());
        }
    }
}
