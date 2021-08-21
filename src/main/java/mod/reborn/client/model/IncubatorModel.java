package mod.reborn.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

import mod.reborn.server.block.entity.IncubatorBlockEntity;


public class IncubatorModel extends ModelBase {
    public double[] modelScale = new double[] { 1.0D, 1.4D, 1.0D };
    public ModelRenderer base1;
    public ModelRenderer base2;
    public ModelRenderer base3;
    public ModelRenderer base4;
    public ModelRenderer base5;
    public ModelRenderer base6;
    public ModelRenderer base7;
    public ModelRenderer base8;
    public ModelRenderer base9;
    public ModelRenderer base10;
    public ModelRenderer bar_bottom;
    public ModelRenderer bar_top;
    public ModelRenderer column1;
    public ModelRenderer column2;
    public ModelRenderer column3;
    public ModelRenderer egg_holder1;
    public ModelRenderer vent;
    public ModelRenderer egg_holder2;
    public ModelRenderer egg_holder3;
    public ModelRenderer egg_holder4;
    public ModelRenderer egg_holder5;
    public ModelRenderer thermostat;
    public ModelRenderer desk;
    public ModelRenderer vitals_monitor;
    public ModelRenderer keyboard;

    public IncubatorModel() {
        this.textureWidth = 256;
        this.textureHeight = 256;
        this.bar_bottom = new ModelRenderer(this, 0, 201);
        this.bar_bottom.setRotationPoint(0.0F, 8.0F, 3.0F);
        this.bar_bottom.addBox(-1.5F, -26.0F, 4.0F, 3, 22, 4, 0.0F);
        this.bar_top = new ModelRenderer(this, 18, 211);
        this.bar_top.setRotationPoint(0.0F, -26.0F, 3.0F);
        this.bar_top.addBox(-1.5F, -17.0F, 2.0F, 3, 17, 3, 0.0F);
        this.vitals_monitor = new ModelRenderer(this, 44, 221);
        this.vitals_monitor.setRotationPoint(-2.8F, 0.0F, 0.0F);
        this.vitals_monitor.addBox(0.0F, -5.0F, -4.0F, 1, 5, 8, 0.0F);
        this.setRotateAngle(vitals_monitor, 0.0F, 0.0F, 0.31869712141416456F);
        this.base6 = new ModelRenderer(this, 69, 88);
        this.base6.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.base6.addBox(-7.5F, -2.0F, -7.5F, 15, 2, 15, 0.0F);
        this.desk = new ModelRenderer(this, 36, 206);
        this.desk.setRotationPoint(-8.5F, 3.0F, 0.0F);
        this.desk.addBox(-7.0F, -1.0F, -5.0F, 7, 2, 10, 0.0F);
        this.base3 = new ModelRenderer(this, 0, 133);
        this.base3.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.base3.addBox(-8.5F, 0.0F, -7.0F, 17, 4, 14, 0.0F);
        this.egg_holder4 = new ModelRenderer(this, 135, 160);
        this.egg_holder4.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.egg_holder4.addBox(-6.0F, 0.0F, -8.5F, 12, 4, 17, 0.0F);
        this.base8 = new ModelRenderer(this, 61, 65);
        this.base8.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.base8.addBox(-8.0F, -2.0F, -7.0F, 16, 2, 14, 0.0F);
        this.thermostat = new ModelRenderer(this, 83, 214);
        this.thermostat.setRotationPoint(8.0F, 2.0F, 0.0F);
        this.thermostat.addBox(0.0F, -1.5F, -3.5F, 1, 3, 7, 0.0F);
        this.egg_holder2 = new ModelRenderer(this, 135, 90);
        this.egg_holder2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.egg_holder2.addBox(-8.0F, 0.0F, -7.0F, 16, 4, 14, 0.0F);
        this.base1 = new ModelRenderer(this, 0, 87);
        this.base1.setRotationPoint(0.0F, 20.0F, 0.0F);
        this.base1.addBox(-8.0F, 0.0F, -8.0F, 16, 4, 16, 0.0F);
        this.base10 = new ModelRenderer(this, 70, 131);
        this.base10.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.base10.addBox(-6.0F, -2.0F, -8.5F, 12, 2, 17, 0.0F);
        this.vent = new ModelRenderer(this, 69, 224);
        this.vent.setRotationPoint(5.0F, -7.5F, 1.5F);
        this.vent.addBox(0.0F, 0.0F, -4.0F, 2, 6, 4, 0.0F);
        this.setRotateAngle(vent, 0.0F, -0.23387411976724018F, 0.0F);
        this.base2 = new ModelRenderer(this, 0, 109);
        this.base2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.base2.addBox(-7.0F, 0.0F, -8.5F, 14, 4, 17, 0.0F);
        this.column3 = new ModelRenderer(this, 56, 35);
        this.column3.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.column3.addBox(-7.0F, -12.0F, -6.0F, 14, 16, 12, 0.0F);
        this.egg_holder1 = new ModelRenderer(this, 135, 65);
        this.egg_holder1.setRotationPoint(0.0F, -14.0F, 0.0F);
        this.egg_holder1.addBox(-7.5F, 0.0F, -7.5F, 15, 4, 15, 0.0F);
        this.egg_holder5 = new ModelRenderer(this, 135, 113);
        this.egg_holder5.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.egg_holder5.addBox(-8.5F, 0.0F, -6.0F, 17, 4, 12, 0.0F);
        this.base5 = new ModelRenderer(this, 0, 155);
        this.base5.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.base5.addBox(-9.0F, 0.0F, -6.0F, 18, 4, 12, 0.0F);
        this.egg_holder3 = new ModelRenderer(this, 135, 134);
        this.egg_holder3.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.egg_holder3.addBox(-7.0F, 0.0F, -8.0F, 14, 4, 16, 0.0F);
        this.base4 = new ModelRenderer(this, 0, 174);
        this.base4.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.base4.addBox(-6.0F, 0.0F, -9.0F, 12, 4, 18, 0.0F);
        this.column1 = new ModelRenderer(this, 0, 34);
        this.column1.setRotationPoint(0.0F, -4.0F, 0.0F);
        this.column1.addBox(-6.5F, -12.0F, -6.5F, 13, 16, 13, 0.0F);
        this.column2 = new ModelRenderer(this, 112, 33);
        this.column2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.column2.addBox(-6.0F, -12.0F, -7.0F, 12, 15, 14, 0.0F);
        this.base9 = new ModelRenderer(this, 0, 67);
        this.base9.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.base9.addBox(-8.5F, -2.0F, -6.0F, 17, 2, 12, 0.0F);
        this.keyboard = new ModelRenderer(this, 62, 206);
        this.keyboard.setRotationPoint(-3.0F, -0.5F, 0.0F);
        this.keyboard.addBox(-3.0F, -1.0F, -2.5F, 3, 1, 5, 0.0F);
        this.setRotateAngle(keyboard, 0.0F, 0.0F, -0.136659280431156F);
        this.base7 = new ModelRenderer(this, 69, 108);
        this.base7.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.base7.addBox(-7.0F, -2.0F, -8.0F, 14, 2, 16, 0.0F);
        this.base4.addChild(this.bar_bottom);
        this.bar_bottom.addChild(this.bar_top);
        this.desk.addChild(this.vitals_monitor);
        this.base1.addChild(this.base6);
        this.egg_holder5.addChild(this.desk);
        this.base1.addChild(this.base3);
        this.egg_holder1.addChild(this.egg_holder4);
        this.base1.addChild(this.base8);
        this.egg_holder5.addChild(this.thermostat);
        this.egg_holder1.addChild(this.egg_holder2);
        this.base1.addChild(this.base10);
        this.column3.addChild(this.vent);
        this.base1.addChild(this.base2);
        this.column1.addChild(this.column3);
        this.column3.addChild(this.egg_holder1);
        this.egg_holder1.addChild(this.egg_holder5);
        this.base1.addChild(this.base5);
        this.egg_holder1.addChild(this.egg_holder3);
        this.base1.addChild(this.base4);
        this.base7.addChild(this.column1);
        this.column1.addChild(this.column2);
        this.base1.addChild(this.base9);
        this.desk.addChild(this.keyboard);
        this.base1.addChild(this.base7);
    }

    public void renderAll()
    {
    	this.base1.render(0.04f);
    }
    
    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
