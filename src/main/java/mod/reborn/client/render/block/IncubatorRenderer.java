package mod.reborn.client.render.block;

import mod.reborn.RebornMod;
import mod.reborn.client.model.IncubatorModel;
import mod.reborn.client.render.RenderingHandler;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import mod.reborn.client.render.entity.dinosaur.DinosaurRenderInfo;
import mod.reborn.server.block.BlockHandler;
import mod.reborn.server.block.entity.IncubatorBlockEntity;
import mod.reborn.server.entity.EntityHandler;

public class IncubatorRenderer extends TileEntitySpecialRenderer<IncubatorBlockEntity> {
    private Minecraft mc;
    private final IncubatorModel MODEL = new IncubatorModel();
    private static final ResourceLocation TEXTURES = new ResourceLocation(RebornMod.MODID + ":textures/blocks/incubator.png");
    
    public IncubatorRenderer() 
    {
		this.mc = Minecraft.getMinecraft();
	}
    
    @Override
    public void render(IncubatorBlockEntity te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) 
    {
    	GlStateManager.enableDepth();
    	GlStateManager.depthFunc(515);
    	GlStateManager.depthMask(true);
    	this.bindTexture(TEXTURES);
    	IncubatorModel model = MODEL;
    	float angle = te.lidAngle;
    	float prevAngle = te.prevLidAngle;
    	GlStateManager.pushMatrix();
    	GlStateManager.enableRescaleNormal();
    	GlStateManager.translate((float)x + 0.5f, (float)y + 1.0f, (float)z + 0.5f);
    	GlStateManager.scale(1.0f, -1.0f, -1.0f);
    	GlStateManager.translate(0.5f, 0.5f, 0.5f);
    	GlStateManager.translate(-0.5f, -0.5f, -0.5f);
    	
    	float f = prevAngle + (angle - prevAngle) * partialTicks;
    	f = 1.0f - f;
    	f = 1.0f - f*f*f;
    	model.glass_main1.rotateAngleZ = (f * ((float)Math.PI)/2.0f);
    	model.renderAll();
    	GlStateManager.disableRescaleNormal();
    	GlStateManager.popMatrix();
    	GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
  }
}
