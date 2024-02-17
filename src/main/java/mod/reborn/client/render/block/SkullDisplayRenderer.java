package mod.reborn.client.render.block;

import mod.reborn.RebornMod;
import net.ilexiconn.llibrary.client.model.tabula.TabulaModel;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.RayTraceResult;
import java.util.Random;
import mod.reborn.client.proxy.ClientProxy;
import mod.reborn.server.block.BlockHandler;
import mod.reborn.server.block.SkullDisplay;
import mod.reborn.server.block.entity.SkullDisplayEntity;
import mod.reborn.server.tabula.TabulaModelHelper;
import org.lwjgl.opengl.GL11;

public class SkullDisplayRenderer extends TileEntitySpecialRenderer<SkullDisplayEntity> {
	
    @Override
    public void render(SkullDisplayEntity tile, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
    	final IBlockState blockState = tile.getWorld().getBlockState(tile.getPos());
        if (blockState.getBlock() == BlockHandler.SKULL_DISPLAY) {
            GlStateManager.pushMatrix();
            GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GlStateManager.enableBlend();
            GlStateManager.disableCull();

            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.translate(x + 0.5, y + 0.5, z + 0.5);

            final EnumFacing.Axis axis = blockState.getValue(SkullDisplay.FACING).getAxis();
            final boolean horizontal;
			if (axis == EnumFacing.Axis.Y) {
				horizontal = true;
			} else {
				horizontal = false;
			}
         
            GlStateManager.rotate(tile.getAngle(), 0.0F, 1.0F, 0.0F);

            if (tile.model == null) {
				if (tile.hasData()) {
					try {
						final String dinosaur = tile.getDinosaur().getName().toString();
						final boolean isFossilized = tile.isFossilized();
						if (horizontal) {
							tile.model = new TabulaModel(TabulaModelHelper.loadTabulaModel(new ResourceLocation(RebornMod.MODID, "models/block/skull_display/" + dinosaur  + "_" + "horizontal")));
							tile.texture = new ResourceLocation(RebornMod.MODID, "textures/blocks/skull_display/" + dinosaur + "_" + (isFossilized ? "fossilized" : "fresh") + "_" + "vertical.png");

						} else {
							tile.model = new TabulaModel(TabulaModelHelper.loadTabulaModel(new ResourceLocation(RebornMod.MODID, "models/block/skull_display/" + dinosaur + "_" + "vertical")));
							tile.texture = new ResourceLocation(RebornMod.MODID, "textures/blocks/skull_display/" + dinosaur + "_" + (isFossilized ? "fossilized" : "fresh") + "_" + "horizontal.png");

						}

					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			}
				GlStateManager.scale(1.6, -1.6, 1.6);

			if(!horizontal)
			   GlStateManager.translate(0, 0.15, -0.18);

			GlStateManager.translate(0.0F, -1.194F, 0.0F);
			if(tile.model != null) {
				ClientProxy.MC.getTextureManager().bindTexture(tile.texture);
				tile.model.render(null, 0, 0, 0, 0, 0, 0.0625F);
			}
			GlStateManager.disableBlend();
			GlStateManager.enableCull();
			GlStateManager.popMatrix();
        }
    }
}
