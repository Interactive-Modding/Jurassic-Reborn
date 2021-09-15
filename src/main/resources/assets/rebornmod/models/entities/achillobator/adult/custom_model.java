// Made with Blockbench 3.8.4
// Exported for Minecraft version 1.15 - 1.16
// Paste this class into your mod and generate all required imports


public class custom_model extends EntityModel<Entity> {
	private final ModelRenderer root;
	private final ModelRenderer body3;
	private final ModelRenderer tail1;
	private final ModelRenderer tail2;
	private final ModelRenderer tail3;
	private final ModelRenderer tail4;
	private final ModelRenderer tail5;
	private final ModelRenderer tail6;
	private final ModelRenderer body2;
	private final ModelRenderer body1;
	private final ModelRenderer neck1;
	private final ModelRenderer neck2;
	private final ModelRenderer neck3;
	private final ModelRenderer neck4;
	private final ModelRenderer neck5;
	private final ModelRenderer Head;
	private final ModelRenderer SnoutridgeL;
	private final ModelRenderer SnoutridgeL;
	private final ModelRenderer SnoutridgeL;
	private final ModelRenderer SnoutridgeL;
	private final ModelRenderer Snout1;
	private final ModelRenderer UpperTeethLeft;
	private final ModelRenderer UpperTeethRight;
	private final ModelRenderer UpperTeethFront;
	private final ModelRenderer Snout2;
	private final ModelRenderer Snout3;
	private final ModelRenderer Jaw;
	private final ModelRenderer LowerTeethRight;
	private final ModelRenderer LowerTeethLeft;
	private final ModelRenderer LowerTeethFront;
	private final ModelRenderer Underneck;
	private final ModelRenderer Underneck;
	private final ModelRenderer Leftarm;
	private final ModelRenderer Leftforearm;
	private final ModelRenderer Lefthand;
	private final ModelRenderer claw6;
	private final ModelRenderer claw5;
	private final ModelRenderer claw5;
	private final ModelRenderer claw5;
	private final ModelRenderer claw6;
	private final ModelRenderer claw5;
	private final ModelRenderer Rightarm;
	private final ModelRenderer Rightforearm;
	private final ModelRenderer Righthand;
	private final ModelRenderer claw6;
	private final ModelRenderer claw5;
	private final ModelRenderer claw5;
	private final ModelRenderer claw5;
	private final ModelRenderer claw6;
	private final ModelRenderer claw5;
	private final ModelRenderer Leftthigh;
	private final ModelRenderer Leftshin;
	private final ModelRenderer Leftupperfoot;
	private final ModelRenderer Leftfoot;
	private final ModelRenderer Lefttoe;
	private final ModelRenderer Lefttoeclaw1;
	private final ModelRenderer Lefttoeclaw2;
	private final ModelRenderer Rightthigh;
	private final ModelRenderer Rightshin;
	private final ModelRenderer Rightupperfoot;
	private final ModelRenderer Rightfoot;
	private final ModelRenderer Righttoe;
	private final ModelRenderer Righttoeclaw1;
	private final ModelRenderer Righttoeclaw2;

	public custom_model() {
		textureWidth = 128;
		textureHeight = 128;

		root = new ModelRenderer(this);
		root.setRotationPoint(0.0F, 0.0F, 0.0F);
		

		body3 = new ModelRenderer(this);
		body3.setRotationPoint(0.0F, 9.7F, -0.5F);
		root.addChild(body3);
		setRotationAngle(body3, -0.3643F, 0.0F, 0.0F);
		body3.setTextureOffset(4, 80).addBox(-4.0F, -5.0F, -5.9F, 8.0F, 9.0F, 10.0F, 0.0F, false);

		tail1 = new ModelRenderer(this);
		tail1.setRotationPoint(0.0F, -4.9F, 3.3F);
		body3.addChild(tail1);
		setRotationAngle(tail1, 0.0489F, 0.0F, 0.0F);
		tail1.setTextureOffset(0, 36).addBox(-3.5F, 0.0F, 0.0F, 7.0F, 7.0F, 8.0F, 0.0F, false);

		tail2 = new ModelRenderer(this);
		tail2.setRotationPoint(0.0F, 0.2F, 7.0F);
		tail1.addChild(tail2);
		setRotationAngle(tail2, 0.0911F, 0.0F, 0.0F);
		tail2.setTextureOffset(32, 34).addBox(-3.0F, 0.0F, 0.0F, 6.0F, 6.0F, 9.0F, 0.0F, false);

		tail3 = new ModelRenderer(this);
		tail3.setRotationPoint(0.0F, 0.2F, 8.1F);
		tail2.addChild(tail3);
		setRotationAngle(tail3, 0.182F, 0.0F, 0.0F);
		tail3.setTextureOffset(65, 35).addBox(-2.5F, 0.0F, 0.0F, 5.0F, 5.0F, 9.0F, 0.0F, false);

		tail4 = new ModelRenderer(this);
		tail4.setRotationPoint(0.0F, 0.2F, 8.0F);
		tail3.addChild(tail4);
		setRotationAngle(tail4, 0.2731F, 0.0F, 0.0F);
		tail4.setTextureOffset(95, 34).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 4.0F, 9.0F, 0.0F, false);

		tail5 = new ModelRenderer(this);
		tail5.setRotationPoint(0.0F, 0.2F, 8.7F);
		tail4.addChild(tail5);
		setRotationAngle(tail5, 0.1367F, 0.0F, 0.0F);
		tail5.setTextureOffset(101, 52).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 3.0F, 9.0F, 0.0F, false);

		tail6 = new ModelRenderer(this);
		tail6.setRotationPoint(0.0F, 0.2F, 8.4F);
		tail5.addChild(tail6);
		setRotationAngle(tail6, 0.0456F, 0.0F, 0.0F);
		tail6.setTextureOffset(101, 64).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 2.0F, 10.0F, 0.0F, false);

		body2 = new ModelRenderer(this);
		body2.setRotationPoint(0.0F, -4.9F, -5.8F);
		body3.addChild(body2);
		body2.setTextureOffset(43, 83).addBox(-3.5F, 0.0F, -8.0F, 7.0F, 8.0F, 8.0F, 0.0F, false);

		body1 = new ModelRenderer(this);
		body1.setRotationPoint(0.0F, 1.3F, -6.2F);
		body2.addChild(body1);
		setRotationAngle(body1, -0.3187F, 0.0F, 0.0F);
		body1.setTextureOffset(77, 89).addBox(-2.5F, 0.0F, -3.0F, 5.0F, 6.0F, 5.0F, 0.0F, false);

		neck1 = new ModelRenderer(this);
		neck1.setRotationPoint(0.0F, 1.97F, 0.9F);
		body1.addChild(neck1);
		setRotationAngle(neck1, -0.4098F, 0.0F, 0.0F);
		neck1.setTextureOffset(3, 69).addBox(-2.0F, 0.0F, -5.0F, 4.0F, 5.0F, 4.0F, 0.0F, false);

		neck2 = new ModelRenderer(this);
		neck2.setRotationPoint(0.0F, 0.24F, -3.46F);
		neck1.addChild(neck2);
		setRotationAngle(neck2, -0.3082F, 0.0F, 0.0F);
		neck2.setTextureOffset(24, 69).addBox(-2.0F, 0.0F, -3.0F, 4.0F, 5.0F, 3.0F, 0.0F, false);

		neck3 = new ModelRenderer(this);
		neck3.setRotationPoint(0.0F, 0.0F, -2.9F);
		neck2.addChild(neck3);
		setRotationAngle(neck3, 0.2344F, 0.0F, 0.0F);
		neck3.setTextureOffset(41, 70).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 5.0F, 2.0F, 0.0F, false);

		neck4 = new ModelRenderer(this);
		neck4.setRotationPoint(0.0F, 0.0F, -2.0F);
		neck3.addChild(neck4);
		setRotationAngle(neck4, 0.4651F, 0.0F, 0.0F);
		neck4.setTextureOffset(70, 70).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 5.0F, 2.0F, 0.0F, false);

		neck5 = new ModelRenderer(this);
		neck5.setRotationPoint(0.0F, 0.0F, -2.0F);
		neck4.addChild(neck5);
		setRotationAngle(neck5, 0.4128F, 0.0F, 0.0F);
		neck5.setTextureOffset(57, 70).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 5.0F, 2.0F, 0.0F, false);

		Head = new ModelRenderer(this);
		Head.setRotationPoint(0.0F, 0.01F, -2.01F);
		neck5.addChild(Head);
		setRotationAngle(Head, 0.3187F, 0.0F, 0.0F);
		Head.setTextureOffset(87, 19).addBox(-2.5F, 0.0F, -5.0F, 5.0F, 5.0F, 5.0F, 0.0F, false);

		SnoutridgeL = new ModelRenderer(this);
		SnoutridgeL.setRotationPoint(1.27F, -0.47F, -3.16F);
		Head.addChild(SnoutridgeL);
		setRotationAngle(SnoutridgeL, -0.2477F, 0.1223F, -0.114F);
		SnoutridgeL.setTextureOffset(58, 37).addBox(0.0F, 0.0F, 0.0F, 1.0F, 3.0F, 2.0F, 0.0F, false);

		SnoutridgeL = new ModelRenderer(this);
		SnoutridgeL.setRotationPoint(-0.26F, 1.68F, -3.57F);
		SnoutridgeL.addChild(SnoutridgeL);
		setRotationAngle(SnoutridgeL, 0.4403F, 0.0675F, -0.0176F);
		SnoutridgeL.setTextureOffset(56, 27).addBox(0.0F, 0.0F, 0.0F, 1.0F, 3.0F, 4.0F, 0.0F, false);

		SnoutridgeL = new ModelRenderer(this);
		SnoutridgeL.setRotationPoint(-1.27F, -0.47F, -3.16F);
		Head.addChild(SnoutridgeL);
		setRotationAngle(SnoutridgeL, -0.2477F, -0.1223F, 0.114F);
		SnoutridgeL.setTextureOffset(58, 37).addBox(-1.0F, 0.0F, 0.0F, 1.0F, 3.0F, 2.0F, 0.0F, false);

		SnoutridgeL = new ModelRenderer(this);
		SnoutridgeL.setRotationPoint(0.26F, 1.68F, -3.57F);
		SnoutridgeL.addChild(SnoutridgeL);
		setRotationAngle(SnoutridgeL, 0.4403F, -0.0675F, 0.0176F);
		SnoutridgeL.setTextureOffset(56, 27).addBox(-1.0F, 0.0F, 0.0F, 1.0F, 3.0F, 4.0F, 0.0F, false);

		Snout1 = new ModelRenderer(this);
		Snout1.setRotationPoint(0.0F, 2.1F, -4.3F);
		Head.addChild(Snout1);
		setRotationAngle(Snout1, 0.0628F, 0.0F, 0.0F);
		Snout1.setTextureOffset(26, 0).addBox(-2.0F, -1.5F, -6.0F, 4.0F, 3.0F, 6.0F, 0.0F, false);

		UpperTeethLeft = new ModelRenderer(this);
		UpperTeethLeft.setRotationPoint(-1.9F, 1.34F, -6.03F);
		Snout1.addChild(UpperTeethLeft);
		UpperTeethLeft.setTextureOffset(76, 18).addBox(0.0F, 0.0F, 0.2F, 0.0F, 1.0F, 5.0F, 0.0F, false);

		UpperTeethRight = new ModelRenderer(this);
		UpperTeethRight.setRotationPoint(1.9F, 1.34F, -6.03F);
		Snout1.addChild(UpperTeethRight);
		UpperTeethRight.setTextureOffset(76, 18).addBox(0.0F, 0.0F, 0.2F, 0.0F, 1.0F, 5.0F, 0.0F, false);

		UpperTeethFront = new ModelRenderer(this);
		UpperTeethFront.setRotationPoint(0.0F, 1.41F, -5.82F);
		Snout1.addChild(UpperTeethFront);
		UpperTeethFront.setTextureOffset(78, 13).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 1.0F, 0.0F, 0.0F, false);

		Snout2 = new ModelRenderer(this);
		Snout2.setRotationPoint(0.0F, 0.5F, -3.58F);
		Head.addChild(Snout2);
		setRotationAngle(Snout2, 0.0628F, 0.0F, 0.0F);
		Snout2.setTextureOffset(40, 17).addBox(-1.5F, -0.5F, -6.0F, 3.0F, 1.0F, 6.0F, 0.0F, false);

		Snout3 = new ModelRenderer(this);
		Snout3.setRotationPoint(0.0F, -0.11F, -5.69F);
		Snout2.addChild(Snout3);
		setRotationAngle(Snout3, 0.6723F, 0.0F, 0.0F);
		Snout3.setTextureOffset(25, 12).addBox(-1.5F, -0.5F, -1.0F, 3.0F, 1.0F, 1.0F, 0.0F, false);

		Jaw = new ModelRenderer(this);
		Jaw.setRotationPoint(0.0F, 4.4F, -3.9F);
		Head.addChild(Jaw);
		setRotationAngle(Jaw, 0.8196F, 0.0F, 0.0F);
		Jaw.setTextureOffset(51, 0).addBox(-1.5F, -0.5F, -6.0F, 3.0F, 1.0F, 6.0F, 0.0F, false);

		LowerTeethRight = new ModelRenderer(this);
		LowerTeethRight.setRotationPoint(1.4F, -1.4F, -5.74F);
		Jaw.addChild(LowerTeethRight);
		LowerTeethRight.setTextureOffset(77, 16).addBox(0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 4.0F, 0.0F, false);

		LowerTeethLeft = new ModelRenderer(this);
		LowerTeethLeft.setRotationPoint(-1.4F, -1.4F, -5.74F);
		Jaw.addChild(LowerTeethLeft);
		LowerTeethLeft.setTextureOffset(77, 16).addBox(0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 4.0F, 0.0F, false);

		LowerTeethFront = new ModelRenderer(this);
		LowerTeethFront.setRotationPoint(0.0F, -1.4F, -5.84F);
		Jaw.addChild(LowerTeethFront);
		LowerTeethFront.setTextureOffset(78, 12).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 1.0F, 0.0F, 0.0F, false);

		Underneck = new ModelRenderer(this);
		Underneck.setRotationPoint(0.0F, 2.73F, -2.84F);
		Head.addChild(Underneck);
		setRotationAngle(Underneck, -0.4149F, 0.0F, 0.0F);
		Underneck.setTextureOffset(67, 59).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 2.0F, 3.0F, 0.0F, false);

		Underneck = new ModelRenderer(this);
		Underneck.setRotationPoint(0.0F, 0.0052F, 3.1347F);
		Underneck.addChild(Underneck);
		setRotationAngle(Underneck, -0.192F, 0.0F, 0.0F);
		Underneck.setTextureOffset(67, 59).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 2.0F, 3.0F, 0.0F, false);

		Leftarm = new ModelRenderer(this);
		Leftarm.setRotationPoint(-3.5F, 5.3F, -7.1F);
		body2.addChild(Leftarm);
		setRotationAngle(Leftarm, -0.3187F, 0.6829F, -0.0873F);
		Leftarm.setTextureOffset(118, 3).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 6.0F, 2.0F, 0.0F, false);

		Leftforearm = new ModelRenderer(this);
		Leftforearm.setRotationPoint(0.3F, 5.9F, -0.3F);
		Leftarm.addChild(Leftforearm);
		setRotationAngle(Leftforearm, -1.5935F, -0.192F, 0.0873F);
		Leftforearm.setTextureOffset(119, 12).addBox(-0.99F, 0.0F, -2.0F, 2.0F, 4.0F, 2.0F, 0.0F, false);

		Lefthand = new ModelRenderer(this);
		Lefthand.setRotationPoint(0.0F, 3.5F, -1.2F);
		Leftforearm.addChild(Lefthand);
		setRotationAngle(Lefthand, 0.2276F, 0.1396F, -0.2269F);
		Lefthand.setTextureOffset(120, 22).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 3.0F, 1.0F, 0.0F, false);

		claw6 = new ModelRenderer(this);
		claw6.setRotationPoint(0.01F, 1.5F, 0.0F);
		Lefthand.addChild(claw6);
		setRotationAngle(claw6, 0.4554F, 0.0686F, 0.4562F);
		claw6.setTextureOffset(9, 0).addBox(-0.5F, 1.2F, -1.3F, 1.0F, 2.0F, 1.0F, 0.0F, false);

		claw5 = new ModelRenderer(this);
		claw5.setRotationPoint(0.01F, 2.48F, -1.14F);
		claw6.addChild(claw5);
		setRotationAngle(claw5, 0.5966F, -0.008F, -0.0173F);
		claw5.setTextureOffset(0, 2).addBox(-0.5F, 0.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

		claw5 = new ModelRenderer(this);
		claw5.setRotationPoint(0.0F, 1.0F, 0.0F);
		Lefthand.addChild(claw5);
		setRotationAngle(claw5, 0.3643F, 0.0F, 0.0F);
		claw5.setTextureOffset(0, 0).addBox(-0.5F, 1.5F, -1.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);

		claw5 = new ModelRenderer(this);
		claw5.setRotationPoint(-0.01F, 3.81F, -1.36F);
		claw5.addChild(claw5);
		setRotationAngle(claw5, 0.5966F, 0.0F, 0.0F);
		claw5.setTextureOffset(0, 2).addBox(-0.5F, 0.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

		claw6 = new ModelRenderer(this);
		claw6.setRotationPoint(0.0F, 1.5F, 0.0F);
		Lefthand.addChild(claw6);
		setRotationAngle(claw6, 0.4554F, 0.0456F, -0.4098F);
		claw6.setTextureOffset(9, 0).addBox(-0.5F, 1.2F, -1.3F, 1.0F, 2.0F, 1.0F, 0.0F, false);

		claw5 = new ModelRenderer(this);
		claw5.setRotationPoint(0.01F, 2.48F, -1.17F);
		claw6.addChild(claw5);
		setRotationAngle(claw5, 0.5966F, -0.008F, -0.0173F);
		claw5.setTextureOffset(0, 2).addBox(-0.5F, 0.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

		Rightarm = new ModelRenderer(this);
		Rightarm.setRotationPoint(3.5F, 5.3F, -7.1F);
		body2.addChild(Rightarm);
		setRotationAngle(Rightarm, -0.3187F, -0.5918F, 0.0873F);
		Rightarm.setTextureOffset(107, 3).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 6.0F, 2.0F, 0.0F, false);

		Rightforearm = new ModelRenderer(this);
		Rightforearm.setRotationPoint(-0.3F, 5.9F, -0.3F);
		Rightarm.addChild(Rightforearm);
		setRotationAngle(Rightforearm, -1.3659F, 0.192F, -0.0873F);
		Rightforearm.setTextureOffset(107, 12).addBox(-1.01F, 0.0F, -2.0F, 2.0F, 4.0F, 2.0F, 0.0F, false);

		Righthand = new ModelRenderer(this);
		Righthand.setRotationPoint(0.0F, 3.5F, -1.2F);
		Rightforearm.addChild(Righthand);
		setRotationAngle(Righthand, -0.0911F, -0.1396F, 0.2276F);
		Righthand.setTextureOffset(120, 22).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 3.0F, 1.0F, 0.0F, false);

		claw6 = new ModelRenderer(this);
		claw6.setRotationPoint(0.01F, 1.5F, 0.0F);
		Righthand.addChild(claw6);
		setRotationAngle(claw6, 0.4554F, 0.0686F, 0.4562F);
		claw6.setTextureOffset(9, 0).addBox(-0.5F, 1.2F, -1.3F, 1.0F, 2.0F, 1.0F, 0.0F, false);

		claw5 = new ModelRenderer(this);
		claw5.setRotationPoint(0.01F, 2.58F, -0.96F);
		claw6.addChild(claw5);
		setRotationAngle(claw5, 0.2824F, -0.008F, -0.0173F);
		claw5.setTextureOffset(0, 2).addBox(-0.5F, 0.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

		claw5 = new ModelRenderer(this);
		claw5.setRotationPoint(0.0F, 1.0F, 0.0F);
		Righthand.addChild(claw5);
		setRotationAngle(claw5, 0.3643F, 0.0F, 0.0F);
		claw5.setTextureOffset(0, 0).addBox(-0.5F, 1.5F, -1.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);

		claw5 = new ModelRenderer(this);
		claw5.setRotationPoint(-0.01F, 3.81F, -1.36F);
		claw5.addChild(claw5);
		setRotationAngle(claw5, 0.5966F, 0.0F, 0.0F);
		claw5.setTextureOffset(0, 2).addBox(-0.5F, 0.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

		claw6 = new ModelRenderer(this);
		claw6.setRotationPoint(0.0F, 1.5F, 0.0F);
		Righthand.addChild(claw6);
		setRotationAngle(claw6, 0.4554F, 0.0456F, -0.4098F);
		claw6.setTextureOffset(9, 0).addBox(-0.5F, 1.2F, -1.3F, 1.0F, 2.0F, 1.0F, 0.0F, false);

		claw5 = new ModelRenderer(this);
		claw5.setRotationPoint(0.01F, 2.52F, -1.16F);
		claw6.addChild(claw5);
		setRotationAngle(claw5, 0.5966F, -0.008F, -0.0173F);
		claw5.setTextureOffset(0, 2).addBox(-0.5F, 0.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

		Leftthigh = new ModelRenderer(this);
		Leftthigh.setRotationPoint(-2.0F, 8.8F, 0.5F);
		root.addChild(Leftthigh);
		setRotationAngle(Leftthigh, 1.3203F, 0.0F, 0.0F);
		Leftthigh.setTextureOffset(30, 53).addBox(-4.0F, -2.5F, -7.0F, 4.0F, 5.0F, 9.0F, 0.0F, false);

		Leftshin = new ModelRenderer(this);
		Leftshin.setRotationPoint(-2.0F, -2.2F, -6.9F);
		Leftthigh.addChild(Leftshin);
		setRotationAngle(Leftshin, -0.5363F, 0.0F, 0.0F);
		Leftshin.setTextureOffset(16, 22).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 7.0F, 3.0F, 0.0F, false);

		Leftupperfoot = new ModelRenderer(this);
		Leftupperfoot.setRotationPoint(0.0F, 7.0F, 2.5F);
		Leftshin.addChild(Leftupperfoot);
		setRotationAngle(Leftupperfoot, -1.0776F, 0.0F, 0.0F);
		Leftupperfoot.setTextureOffset(40, 24).addBox(-1.0F, 0.0F, -2.0F, 2.0F, 6.0F, 2.0F, 0.0F, false);

		Leftfoot = new ModelRenderer(this);
		Leftfoot.setRotationPoint(-1.0F, 3.9F, 0.3F);
		Leftupperfoot.addChild(Leftfoot);
		setRotationAngle(Leftfoot, 0.2731F, 0.0F, 0.0F);
		Leftfoot.setTextureOffset(4, 118).addBox(-0.5F, 0.0F, -5.0F, 2.0F, 2.0F, 5.0F, 0.0F, false);

		Lefttoe = new ModelRenderer(this);
		Lefttoe.setRotationPoint(1.0F, -0.1F, -1.1F);
		Leftfoot.addChild(Lefttoe);
		setRotationAngle(Lefttoe, -0.743F, 0.0F, 0.0F);
		Lefttoe.setTextureOffset(45, 111).addBox(0.5F, 0.0F, -3.0F, 1.0F, 2.0F, 3.0F, 0.0F, true);

		Lefttoeclaw1 = new ModelRenderer(this);
		Lefttoeclaw1.setRotationPoint(0.0F, 0.1F, -2.7F);
		Lefttoe.addChild(Lefttoeclaw1);
		setRotationAngle(Lefttoeclaw1, -0.1518F, 0.0F, 0.0F);
		Lefttoeclaw1.setTextureOffset(82, 111).addBox(0.5F, 0.0F, -2.0F, 1.0F, 1.0F, 2.0F, 0.0F, true);

		Lefttoeclaw2 = new ModelRenderer(this);
		Lefttoeclaw2.setRotationPoint(0.0F, 0.5F, -1.2F);
		Lefttoeclaw1.addChild(Lefttoeclaw2);
		setRotationAngle(Lefttoeclaw2, -2.1328F, 0.0F, 0.0F);
		Lefttoeclaw2.setTextureOffset(100, 111).addBox(0.5F, 0.0F, 0.0F, 1.0F, 1.0F, 2.0F, 0.0F, true);

		Rightthigh = new ModelRenderer(this);
		Rightthigh.setRotationPoint(2.0F, 8.8F, 0.5F);
		root.addChild(Rightthigh);
		setRotationAngle(Rightthigh, 0.6374F, 0.0F, 0.0F);
		Rightthigh.setTextureOffset(2, 53).addBox(0.0F, -2.5F, -7.0F, 4.0F, 5.0F, 9.0F, 0.0F, false);

		Rightshin = new ModelRenderer(this);
		Rightshin.setRotationPoint(2.0F, -2.4F, -6.9F);
		Rightthigh.addChild(Rightshin);
		setRotationAngle(Rightshin, -0.5363F, 0.0F, 0.0F);
		Rightshin.setTextureOffset(2, 22).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 7.0F, 3.0F, 0.0F, false);

		Rightupperfoot = new ModelRenderer(this);
		Rightupperfoot.setRotationPoint(0.0F, 7.0F, 2.5F);
		Rightshin.addChild(Rightupperfoot);
		setRotationAngle(Rightupperfoot, -1.0776F, 0.0F, 0.0F);
		Rightupperfoot.setTextureOffset(30, 24).addBox(-1.0F, 0.0F, -2.0F, 2.0F, 6.0F, 2.0F, 0.0F, false);

		Rightfoot = new ModelRenderer(this);
		Rightfoot.setRotationPoint(0.0F, 3.9F, 0.3F);
		Rightupperfoot.addChild(Rightfoot);
		setRotationAngle(Rightfoot, 0.4363F, 0.0F, 0.0F);
		Rightfoot.setTextureOffset(5, 104).addBox(-0.5F, 0.0F, -5.0F, 2.0F, 2.0F, 5.0F, 0.0F, true);

		Righttoe = new ModelRenderer(this);
		Righttoe.setRotationPoint(-2.0F, -0.1F, -1.1F);
		Rightfoot.addChild(Righttoe);
		setRotationAngle(Righttoe, -0.743F, 0.0F, 0.0F);
		Righttoe.setTextureOffset(29, 111).addBox(0.5F, 0.0F, -3.0F, 1.0F, 2.0F, 3.0F, 0.0F, true);

		Righttoeclaw1 = new ModelRenderer(this);
		Righttoeclaw1.setRotationPoint(0.0F, 0.1F, -2.7F);
		Righttoe.addChild(Righttoeclaw1);
		setRotationAngle(Righttoeclaw1, -0.1518F, 0.0F, 0.0F);
		Righttoeclaw1.setTextureOffset(66, 111).addBox(0.5F, 0.0F, -2.0F, 1.0F, 1.0F, 2.0F, 0.0F, true);

		Righttoeclaw2 = new ModelRenderer(this);
		Righttoeclaw2.setRotationPoint(0.0F, 0.5F, -1.2F);
		Righttoeclaw1.addChild(Righttoeclaw2);
		setRotationAngle(Righttoeclaw2, -2.1331F, 0.0F, 0.0F);
		Righttoeclaw2.setTextureOffset(66, 118).addBox(0.5F, 0.0F, 0.0F, 1.0F, 1.0F, 2.0F, 0.0F, true);
	}

	@Override
	public void setRotationAngles(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
		//previously the render function, render code was moved to a method below
	}

	@Override
	public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
		root.render(matrixStack, buffer, packedLight, packedOverlay);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}