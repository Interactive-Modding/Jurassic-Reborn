package mod.reborn.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class IncubatorLidModel extends ModelBase{
	
	public ModelRenderer glass_main1;
    public ModelRenderer glass_main2;
    public ModelRenderer glass_main3;
    public ModelRenderer glass_main4;
    public ModelRenderer glass_main5;
    public ModelRenderer layer_2_glass1;
    public ModelRenderer layer_2_glass2;
    public ModelRenderer layer_2_glass3;
    public ModelRenderer layer_2_glass4;
    public ModelRenderer layer_2_glass5;
    public ModelRenderer layer_3_glass1;
    public ModelRenderer layer_3_glass2;
    public ModelRenderer layer_3_glass3;
    public ModelRenderer layer_4_glass1;
    public ModelRenderer layer_4_glass2;
    public ModelRenderer layer_4_glass3;
    
    
    public IncubatorLidModel() 
    {
    	this.textureWidth = 256;
        this.textureHeight = 256;
        
        this.layer_2_glass3 = new ModelRenderer(this, 194, 118);
        this.layer_2_glass3.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.layer_2_glass3.addBox(-7.0F, 0.0F, -6.0F, 14, 1, 12, 0.0F);
        this.layer_2_glass2 = new ModelRenderer(this, 194, 133);
        this.layer_2_glass2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.layer_2_glass2.addBox(-6.0F, 0.0F, -7.0F, 12, 1, 14, 0.0F);
        this.layer_2_glass1 = new ModelRenderer(this, 195, 100);
        this.layer_2_glass1.setRotationPoint(-7.5F, -1.0F, 0.0F);
        this.layer_2_glass1.addBox(-6.5F, 0.0F, -6.5F, 13, 1, 13, 0.0F);
        this.glass_main4 = new ModelRenderer(this, 192, 62);
        this.glass_main4.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.glass_main4.addBox(-16.0F, 0.0F, -6.0F, 17, 1, 12, 0.0F);
        this.layer_3_glass2 = new ModelRenderer(this, 200, 214);
        this.layer_3_glass2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.layer_3_glass2.addBox(-5.0F, 0.0F, -6.0F, 10, 1, 12, 0.0F);
        this.glass_main1 = new ModelRenderer(this, 192, 2);
        this.glass_main1.setRotationPoint(7.5F, -3.0F, -6.0F);
        this.glass_main1.addBox(-15.0F, 0.0F, -7.5F, 15, 1, 15, 0.0F);
        this.glass_main3 = new ModelRenderer(this, 192, 21);
        this.glass_main3.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.glass_main3.addBox(-14.5F, 0.0F, -8.0F, 14, 1, 16, 0.0F);
        this.layer_3_glass3 = new ModelRenderer(this, 200, 201);
        this.layer_3_glass3.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.layer_3_glass3.addBox(-6.0F, 0.0F, -5.0F, 12, 1, 10, 0.0F);
        this.layer_2_glass4 = new ModelRenderer(this, 197, 166);
        this.layer_2_glass4.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.layer_2_glass4.addBox(-5.0F, 0.0F, -7.5F, 10, 1, 15, 0.0F);
        this.layer_4_glass1 = new ModelRenderer(this, 157, 202);
        this.layer_4_glass1.setRotationPoint(0.0F, -1.0F, 0.0F);
        this.layer_4_glass1.addBox(-4.5F, 0.0F, -4.5F, 9, 1, 9, 0.0F);
        this.layer_4_glass2 = new ModelRenderer(this, 157, 218);
        this.layer_4_glass2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.layer_4_glass2.addBox(-5.0F, 0.0F, -4.0F, 10, 1, 8, 0.0F);
        this.layer_4_glass3 = new ModelRenderer(this, 157, 230);
        this.layer_4_glass3.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.layer_4_glass3.addBox(-4.0F, 0.0F, -5.0F, 8, 1, 10, 0.0F);
        this.glass_main2 = new ModelRenderer(this, 192, 42);
        this.glass_main2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.glass_main2.addBox(-15.5F, 0.0F, -7.0F, 16, 1, 14, 0.0F);
        this.layer_3_glass1 = new ModelRenderer(this, 200, 187);
        this.layer_3_glass1.setRotationPoint(0.0F, -1.0F, 0.0F);
        this.layer_3_glass1.addBox(-5.5F, 0.0F, -5.5F, 11, 1, 11, 0.0F);
        this.glass_main5 = new ModelRenderer(this, 192, 79);
        this.glass_main5.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.glass_main5.addBox(-13.5F, 0.0F, -8.5F, 12, 1, 17, 0.0F);
        this.layer_2_glass5 = new ModelRenderer(this, 194, 151);
        this.layer_2_glass5.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.layer_2_glass5.addBox(-7.5F, 0.0F, -5.0F, 15, 1, 10, 0.0F);
        
        this.layer_2_glass1.addChild(this.layer_2_glass3);
        this.layer_2_glass1.addChild(this.layer_2_glass2);
        this.glass_main1.addChild(this.layer_2_glass1);
        this.glass_main1.addChild(this.glass_main4);
        this.layer_3_glass1.addChild(this.layer_3_glass2);
        this.glass_main1.addChild(this.glass_main3);
        this.layer_3_glass1.addChild(this.layer_3_glass3);
        this.layer_2_glass1.addChild(this.layer_2_glass4);
        this.layer_3_glass1.addChild(this.layer_4_glass1);
        this.layer_4_glass1.addChild(this.layer_4_glass2);
        this.layer_4_glass1.addChild(this.layer_4_glass3);
        this.glass_main1.addChild(this.glass_main2);
        this.layer_2_glass1.addChild(this.layer_3_glass1);
        this.glass_main1.addChild(this.glass_main5);
        this.layer_2_glass1.addChild(this.layer_2_glass5);
	}
    
    public void renderAll()
    {
    	this.glass_main1.render(0.04f);
    }

}
