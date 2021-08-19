package mod.reborn.client.render.block;

import mod.reborn.RebornMod;
import mod.reborn.client.model.IncubatorLidModel;
import mod.reborn.client.model.IncubatorModel;
import mod.reborn.server.block.entity.IncubatorBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;

public class IncubatorRenderer extends TileEntitySpecialRenderer<IncubatorBlockEntity> {
    private Minecraft mc;
    private final IncubatorModel MODEL = new IncubatorModel();
    private final IncubatorLidModel LID_MODEL = new IncubatorLidModel();
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
    	IncubatorLidModel lid_model = LID_MODEL;
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
    	f = f*0.6f;
    	model.renderAll();
    	GlStateManager.translate(0, -f + 0.15f, 0.23f);
    	lid_model.renderAll();
    	GlStateManager.disableRescaleNormal();
    	GlStateManager.popMatrix();
    	GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
    	
    }
}
