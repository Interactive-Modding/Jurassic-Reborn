package mod.reborn.client.render.block;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import mod.reborn.server.block.entity.DisplayBlockEntity;
import mod.reborn.server.entity.DinosaurEntity;

public class DisplayBlockRenderer extends TileEntitySpecialRenderer<DisplayBlockEntity> {
    private Minecraft mc = Minecraft.getMinecraft();

    @Override
    public void render(DisplayBlockEntity tileEntity, double x, double y, double z, float p_180535_8_, int p_180535_9_, float alpha) {
        GlStateManager.pushMatrix();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.translate(x + 0.5, y, z + 0.5);

        GlStateManager.rotate(tileEntity.getRot(), 0.0F, 1.0F, 0.0F);

        double scale = tileEntity.getEntity().isSkeleton() ? 1.0 : 0.15;
        GlStateManager.scale(scale, scale, scale);

        DinosaurEntity entity = tileEntity.getEntity();
        if (entity != null) {
            this.mc.getRenderManager().renderEntity(entity, 0, 0, 0, 0, 0, false);
        }

        GlStateManager.popMatrix();
    }
}
