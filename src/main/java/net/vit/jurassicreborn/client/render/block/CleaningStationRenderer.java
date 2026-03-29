package net.vit.jurassicreborn.client.render.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.vit.jurassicreborn.common.blocks.ModBlocks;
import net.vit.jurassicreborn.common.blocks.entities.cleaner.CleanerBlock;
import net.vit.jurassicreborn.common.blocks.entities.cleaner.CleanerBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

@OnlyIn(Dist.CLIENT)
public class CleaningStationRenderer implements BlockEntityRenderer<CleanerBlockEntity> {
    private Minecraft mc = Minecraft.getInstance();



//    private Material TEXTURE = new Material(TextureAtlas.LOCATION_BLOCKS, JurassicReborn.resource("block/cleaning_station"));



//    public static TabulaModel CLEANER_MODEL;
//    public static ResourceLocation CLEANER_TEXTURE;

//    static {
//        TabulaModelContainer cleaner = null;
//        try {
//
//            cleaner = TabulaModelHelper.loadTabulaModel("assets/JurassicReborn/models/block/cleaning_station");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
////        SHARK = new AnimatableModel(shark, new SharkAnimator());
//        CLEANER_MODEL = new TabulaModel(cleaner);
//        CLEANER_TEXTURE = ResourceLocation.parse(JurassicReborn.MODID + ":" + "textures/block/cleaning_station.png");
//    }
//    private Minecraft



    public CleaningStationRenderer(){super();}

//    @Override
//    public void render(CleanerBlockEntity tileEntity, double x, double y, double z, float p_180535_8_, int p_180535_9_, float alpha) {
//        World world = tileEntity.getWorld();
//
//        IBlockState state = world.getBlockState(tileEntity.getPos());
//
//        if (state.getBlock() == ModBlocks.CLEANING_STATION) {
//            EnumFacing value = state.getValue(OrientedBlock.FACING);
//
//            if (value == EnumFacing.NORTH || value == EnumFacing.SOUTH) {
//                value = value.getOpposite();
//            }
//
//            int rotation = value.getHorizontalIndex() * 90;
//            float scale = 0.25F;
//
//            GlStateManager.pushMatrix();
//            GlStateManager.translate(x + 0.5, y + 1.5F, z + 0.5);
//
//            GlStateManager.rotate(rotation, 0, 1, 0);
//            GlStateManager.translate(0.0, -1.1, -0.05);
//            GlStateManager.scale(-scale, -scale, scale);
//            GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
//
//            this.mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
//
//            RenderItem renderItem = this.mc.getRenderItem();
//
//            ItemStack cleanable = tileEntity.getStackInSlot(0);
//
//            if (cleanable != null) {
//                renderItem.renderItem(cleanable, renderItem.getItemModelMesher().getItemModel(cleanable));
//            }
//
//            GlStateManager.popMatrix();
//        }
//    }

    @Override
    public void render(CleanerBlockEntity blockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
        BlockState state = blockEntity.getBlockState();

        if(state.getBlock() != ModBlocks.CLEANING_STATION.get()){
            return;
        }

        ItemStack cleanable;

        Direction facing = state.getValue(CleanerBlock.FACING);

        if(facing == Direction.NORTH || facing == Direction.SOUTH){
            facing = facing.getOpposite();
        }
        float rotation = facing.toYRot();
        float scale = 0.5F;
        pPoseStack.pushPose();
        pPoseStack.translate(0.5D, 0.375D, 0.5D);
        pPoseStack.mulPose(Axis.YP.rotationDegrees(-rotation));
//        pPoseStack.translate(-0.5D, -0.5D, -0.5D);
        pPoseStack.scale(scale, scale, scale);

//        VertexConsumer consumer = TEXTURE.buffer(pBufferSource, RenderType::entityCutout);

        cleanable = blockEntity.getItem(0);


        ItemRenderer itemRenderer = mc.getItemRenderer();

        //            BakedModel bakedmodel = itemRenderer.getModel(cleanable, level, null, 0/*this shouldn't matter, just don't use a compass in the cleaner lol*/);

        itemRenderer.renderStatic(cleanable, ItemDisplayContext.GROUND, pPackedLight, pPackedOverlay, pPoseStack, pBufferSource, blockEntity.getLevel(), 0);

//        BlockRenderDispatcher blockRender = mc.getBlockRenderer();

//        BakedModel cleanerModel = blockRender.getBlockModel(state);
//
//        VertexConsumer vertexConsumer = ModMaterials.CLEANING_STATION_MATERIAL.buffer(pBufferSource, RenderType::entityCutout);

//        blockRender.renderSingleBlock(state, pPoseStack, pBufferSource, pPackedLight, pPackedOverlay);

//        blockRender.renderSingleBlock();







//        CLEANER_MODEL.renderToBuffer(pPoseStack, consumer, pPackedLight, pPackedOverlay, 1f, 1f, 1f, 1f);

//        BakedModel blockModel = mc.getBlockRenderer().getBlockModel(state);
//        mc.getBlockRenderer().renderSingleBlock(state, pPoseStack, pBufferSource, pPackedLight, pPackedOverlay, ModelData.EMPTY, RenderType.translucent());
        pPoseStack.popPose();

//        pPoseStack.scale();


    }



}
