package net.vit.jurassicreborn.client.render.block;

import com.github.alexthe666.citadel.client.model.TabulaModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.common.blocks.entities.incubator.IncubatorBlock;
import net.vit.jurassicreborn.common.blocks.entities.incubator.IncubatorBlockEntity;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;
import net.vit.jurassicreborn.common.items.genetics.DinosaurEggItem;
import net.vit.jurassicreborn.common.util.block.ModdedModel;
import net.vit.jurassicreborn.common.legacy.tabula.TabulaModelHelper;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class IncubatorRenderer implements BlockEntityRenderer<IncubatorBlockEntity>, BlockEntityRendererProvider {
    private static final Map<Dinosaur, TabulaModel> EGG_MODELS = new HashMap<>();
    private static final TabulaModel DEFAULT_EGG_MODEL;
    private static final ResourceLocation DEFAULT_EGG_TEXTURE;

    private final GeoBlockRenderer<IncubatorBlockEntity> delegate;

    static {
        TabulaModel model;
        ResourceLocation texture;
        try {
            model = new TabulaModel(TabulaModelHelper.loadTabulaModel("/assets/jurassicreborn/models/entities/egg/tyrannosaurus"));
            texture = ResourceLocation.fromNamespaceAndPath(JurassicReborn.MODID, "textures/entities/egg/tyrannosaurus.png");
        } catch (Exception e) {
            model = null;
            texture = null;
            e.printStackTrace();
        }
        DEFAULT_EGG_MODEL = model;
        DEFAULT_EGG_TEXTURE = texture;
    }

    public IncubatorRenderer(BlockEntityRendererProvider.Context rendererProvider) {
        this.delegate = new GeoBlockRenderer<>(new ModdedModel<>(
                JurassicReborn.resource("geo/incubator.geo.json"),
                JurassicReborn.resource("textures/block/incubator.png"),
                JurassicReborn.resource("animations/incubator.animation.json")
        )) {};
    }

    @Override
    public BlockEntityRenderer create(Context pContext) {
        return new IncubatorRenderer(pContext);
    }

    @Override
    public void render(IncubatorBlockEntity tile,
                       float partialTick,
                       PoseStack poseStack,
                       MultiBufferSource bufferSource,
                       int packedLight,
                       int packedOverlay) {
        renderEgg(tile.getItem(0), tile, poseStack, bufferSource, packedLight, 0.6f, 0.7f);
        renderEgg(tile.getItem(1), tile, poseStack, bufferSource, packedLight, 0.2f, 0.2f);
        renderEgg(tile.getItem(3), tile, poseStack, bufferSource, packedLight, 0.8f, 0.5f);
        renderEgg(tile.getItem(4), tile, poseStack, bufferSource, packedLight, 0.6f, 0.2f);
        renderEgg(tile.getItem(2), tile, poseStack, bufferSource, packedLight, 0.3f, 0.5f);

        delegate.render(tile, partialTick, poseStack, bufferSource, packedLight, packedOverlay);
    }

    private static TabulaModel getEggModel(Dinosaur dino) {
        if (dino != null) {
            return EGG_MODELS.computeIfAbsent(dino, d -> {
                try {
                    return new TabulaModel(TabulaModelHelper.loadTabulaModel("/assets/jurassicreborn/models/entities/egg/" + d.getName().toLowerCase(Locale.ENGLISH)));
                } catch (Exception e) {
                    return DEFAULT_EGG_MODEL;
                }
            });
        }
        return DEFAULT_EGG_MODEL;
    }

    private static ResourceLocation getEggTexture(Dinosaur dino) {
        if (dino != null) {
            ResourceLocation texture = ResourceLocation.fromNamespaceAndPath(JurassicReborn.MODID, "textures/entities/egg/" + dino.getName().toLowerCase(Locale.ENGLISH) + ".png");
            if (Minecraft.getInstance().getResourceManager().getResource(texture).isPresent()) {
                return texture;
            }
        }
        return DEFAULT_EGG_TEXTURE;
    }

    private void renderEgg(ItemStack stack, IncubatorBlockEntity tile, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, float xMod, float zMod){
        if(stack.isEmpty()){
            return;
        }

        poseStack.pushPose();

        Direction facing = tile.getBlockState().getValue(IncubatorBlock.FACING);
        poseStack.translate(0.5D, 0.0D, 0.5D);
        poseStack.mulPose(Axis.YP.rotationDegrees(-facing.toYRot()));
        poseStack.translate(-0.5D, 0.0D, -0.5D);

        poseStack.translate(xMod, 0.65F, zMod);
        poseStack.translate(0.0F, 1.45F, 0.0F);
        poseStack.scale(-0.5F, -0.5F, -0.5F);

        if(stack.getItem() instanceof DinosaurEggItem eggItem){
            Dinosaur dino = eggItem.getDino();
            TabulaModel model = getEggModel(dino);
            ResourceLocation texture = getEggTexture(dino);
            var vertexConsumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(texture));
            model.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        }

        poseStack.popPose();
    }
}
