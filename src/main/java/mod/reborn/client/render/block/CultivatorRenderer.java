package mod.reborn.client.render.block;

import mod.reborn.server.block.entity.CultivatorBlockEntity;
import mod.reborn.server.entity.DinosaurEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import org.lwjgl.opengl.GL11;

import javax.vecmath.Vector3f;

public class CultivatorRenderer extends TileEntitySpecialRenderer<CultivatorBlockEntity> {
    private Minecraft mc = Minecraft.getMinecraft();

    @Override
    public void render(final CultivatorBlockEntity tileEntity, final double x, final double y, final double z, final float partialTicks, final int destroyStage, final float alpha) {
        GlStateManager.pushMatrix();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.translate(x + 0.5, y + 1, z + 0.5);

        if(tileEntity.isProcessing(0) && tileEntity.getField(1) != 0) {
            long timer = getWorld() != null ? getWorld().getTotalWorldTime() : 0;
            DinosaurEntity entity = tileEntity.getDinosaurEntity();
            if (entity != null) {
                float percentageDone = tileEntity.getField(0) / (float)tileEntity.getField(1);
                Vector3f vec = entity.getDinosaurCultivatorRotation();
                GlStateManager.rotate(90F, -1, 0, 0);
                GlStateManager.rotate(vec.y, 0, 0, 0);
                GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
                GlStateManager.rotate(vec.z, 0, 0, 0);
                GlStateManager.scale(percentageDone, percentageDone, percentageDone);
                GlStateManager.enableLight(2);
                GlStateManager.scale(.04, .04, .04);

                this.mc.getRenderManager().renderEntity(entity, 0, 0, 0, 0, 0, false);
            }
        }

        GlStateManager.popMatrix();
    }
}