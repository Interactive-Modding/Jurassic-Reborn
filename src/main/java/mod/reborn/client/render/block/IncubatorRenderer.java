package mod.reborn.client.render.block;

import mod.reborn.RebornMod;
import mod.reborn.client.model.IncubatorLidModel;
import mod.reborn.client.model.IncubatorModel;
import mod.reborn.client.render.RenderingHandler;
import mod.reborn.client.render.entity.dinosaur.DinosaurRenderInfo;
import mod.reborn.server.block.BlockHandler;
import mod.reborn.server.block.entity.IncubatorBlockEntity;
import mod.reborn.server.entity.EntityHandler;

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
    	model.renderAll(); //render block
    	GlStateManager.translate(0, -f + 0.15f, 0.23f);
    	lid_model.renderAll(); //render lid
    	GlStateManager.translate(0, f - 0.15f, -0.23f);
    	if (te.getWorld().getBlockState(te.getPos()).getBlock() == BlockHandler.INCUBATOR)
    	{
            GlStateManager.cullFace(GlStateManager.CullFace.FRONT);
            GlStateManager.disableRescaleNormal();

            this.renderEgg(te.getStackInSlot(0), x, y, z, 0.55, 0.65);
            this.renderEgg(te.getStackInSlot(1), x, y, z, 0.3, 0.3);
            this.renderEgg(te.getStackInSlot(3), x, y, z, 0.7, 0.5);
            this.renderEgg(te.getStackInSlot(4), x, y, z, 0.55, 0.3);
            this.renderEgg(te.getStackInSlot(2), x, y, z, 0.4, 0.5);

            GlStateManager.enableRescaleNormal();
            GlStateManager.cullFace(GlStateManager.CullFace.BACK);
        }
    	model.renderAll();
    	GlStateManager.translate(0, -f + 0.15f, 0.23f);
    	lid_model.renderAll();
    	GlStateManager.disableRescaleNormal();
    	GlStateManager.popMatrix();
    	GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);    	
    }
    
    private void renderEgg(ItemStack stack, double x, double y, double z, double xOffset, double zOffset) 
    {
        if (!stack.isEmpty()) 
        {
            GlStateManager.pushMatrix();
            GlStateManager.translate(-0.5f, -0.65f, -0.45f);
            GlStateManager.translate(xOffset, 1.45, zOffset);
            GlStateManager.scale(-0.5F, -0.5F, -0.5F);
            DinosaurRenderInfo renderDef = RenderingHandler.INSTANCE.getRenderInfo(EntityHandler.getDinosaurById(stack.getItemDamage()));
            this.mc.getTextureManager().bindTexture(renderDef.getEggTexture());
            renderDef.getEggModel().render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
            GlStateManager.popMatrix();
        }
    }
  }
}
