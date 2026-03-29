package net.vit.jurassicreborn.client.render.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

// Made with Blockbench 4.12.6
// Exported for Minecraft version 1.17 or later with Mojang mappings

public class trash_can<T extends Entity> extends EntityModel<T> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath("jurassicreborn", "trash_can"), "main");
    private final ModelPart Trash_can;
    private final ModelPart Top;
    private final ModelPart Fill;
    private final ModelPart Side;
    private final ModelPart Bottom;

    public trash_can(ModelPart root) {
        this.Trash_can = root.getChild("Trash_can");
        this.Top = this.Trash_can.getChild("Top");
        this.Fill = this.Trash_can.getChild("Fill");
        this.Side = this.Trash_can.getChild("Side");
        this.Bottom = this.Trash_can.getChild("Bottom");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition Trash_can = partdefinition.addOrReplaceChild("Trash_can", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition Top = Trash_can.addOrReplaceChild("Top", CubeListBuilder.create().texOffs(0, 6).addBox(-5.0F, -15.0F, 4.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
        .texOffs(4, 3).addBox(4.0F, -15.0F, 4.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
        .texOffs(0, 3).addBox(4.0F, -15.0F, -5.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
        .texOffs(4, 0).addBox(-5.0F, -15.0F, -5.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition north_r1 = Top.addOrReplaceChild("north_r1", CubeListBuilder.create().texOffs(0, 42).addBox(4.0F, -1.0F, -4.0F, 1.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -20.0F, -4.0F, 1.5708F, 0.0F, 1.5708F));

        PartDefinition south_r1 = Top.addOrReplaceChild("south_r1", CubeListBuilder.create().texOffs(10, 43).addBox(4.0F, -1.0F, -4.0F, 1.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -20.0F, 5.0F, 1.5708F, 0.0F, 1.5708F));

        PartDefinition west_r1 = Top.addOrReplaceChild("west_r1", CubeListBuilder.create().texOffs(22, 40).addBox(4.0F, -1.0F, -5.0F, 1.0F, 1.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, -20.0F, 0.0F, 0.0F, 0.0F, 1.5708F));

        PartDefinition east_r1 = Top.addOrReplaceChild("east_r1", CubeListBuilder.create().texOffs(34, 41).addBox(4.0F, -1.0F, -5.0F, 1.0F, 1.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.0F, -20.0F, 0.0F, 0.0F, 0.0F, 1.5708F));

        PartDefinition Fill = Trash_can.addOrReplaceChild("Fill", CubeListBuilder.create().texOffs(32, 25).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 7.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition inner_fill_r1 = Fill.addOrReplaceChild("inner_fill_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -9.0F, 2.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
        .texOffs(20, 42).addBox(-4.0F, -7.0F, 1.0F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.5F, -5.0F, -3.75F, -0.1447F, 0.1601F, -0.2346F));

        PartDefinition inner_fill_r2 = Fill.addOrReplaceChild("inner_fill_r2", CubeListBuilder.create().texOffs(46, 40).addBox(-4.0F, -7.0F, 1.0F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.75F, -5.0F, -2.25F, 0.3655F, 0.147F, -0.3219F));

        PartDefinition inner_fill_r3 = Fill.addOrReplaceChild("inner_fill_r3", CubeListBuilder.create().texOffs(0, 51).addBox(-4.0F, -7.0F, 1.0F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -5.0F, 0.0F, 0.0F, 0.0F, 0.1309F));

        PartDefinition Side = Trash_can.addOrReplaceChild("Side", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition south_side_r1 = Side.addOrReplaceChild("south_side_r1", CubeListBuilder.create().texOffs(32, 0).addBox(-6.0F, -1.0F, -4.0F, 11.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -7.0F, 4.0F, -1.5708F, 0.0F, 1.5708F));

        PartDefinition north_side_r1 = Side.addOrReplaceChild("north_side_r1", CubeListBuilder.create().texOffs(32, 11).addBox(-6.0F, -1.0F, -4.0F, 11.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -7.0F, -5.0F, -1.5708F, 0.0F, 1.5708F));

        PartDefinition east_side_r1 = Side.addOrReplaceChild("east_side_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -1.0F, -5.0F, 11.0F, 1.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.0F, -7.0F, 0.0F, 0.0F, 0.0F, 1.5708F));

        PartDefinition west_side_r1 = Side.addOrReplaceChild("west_side_r1", CubeListBuilder.create().texOffs(0, 11).addBox(-6.0F, -1.0F, -5.0F, 11.0F, 1.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, -7.0F, 0.0F, 0.0F, 0.0F, 1.5708F));

        PartDefinition Bottom = Trash_can.addOrReplaceChild("Bottom", CubeListBuilder.create().texOffs(0, 33).addBox(1.0F, 6.0F, -4.0F, 8.0F, 1.0F, 8.0F, new CubeDeformation(0.0F))
        .texOffs(0, 22).addBox(0.0F, 5.0F, -5.0F, 10.0F, 1.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, -7.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int i, int i1, int i2) {
        Trash_can.render(poseStack, vertexConsumer, i, i1, i2);
    }
}

