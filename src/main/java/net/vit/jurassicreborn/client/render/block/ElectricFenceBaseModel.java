package net.vit.jurassicreborn.client.render.block;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.client.model.ElectricFenceModels;
import net.vit.jurassicreborn.common.blocks.entities.fence.ElectricFenceBaseBlock;
import net.vit.jurassicreborn.common.blocks.entities.fence.ElectricFenceBaseBlockEntity;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ElectricFenceBaseModel extends AnimatedGeoModel<ElectricFenceBaseBlockEntity> {


    @Override
    public ResourceLocation getModelResource(ElectricFenceBaseBlockEntity be) {
        var st      = be.getBlockState();
        var block   = (ElectricFenceBaseBlock) st.getBlock();
        var variant = ElectricFenceModels.resolve(st, block.getType());

        return JurassicReborn.resource("geo/" + variant.modelPath() + ".geo.json");
    }


    @Override
    public ResourceLocation getTextureResource(ElectricFenceBaseBlockEntity be) {
        var st      = be.getBlockState();
        var block   = (ElectricFenceBaseBlock) st.getBlock();
        var variant = ElectricFenceModels.resolve(st, block.getType());

        ResourceLocation tex = JurassicReborn.resource("textures/block/"
                + variant.modelPath() + ".png");

        var rm   = Minecraft.getInstance().getResourceManager();
        boolean exists = rm.getResource(tex).isPresent();

        if (!exists) {
            tex = JurassicReborn.resource("textures/block/" +
                    block.getType().getPath() + ".png");
        }
        return tex;
    }

    @Override
    public ResourceLocation getAnimationResource(ElectricFenceBaseBlockEntity be) {
        return JurassicReborn.resource("animations/empty.animation.json");
    }
}
