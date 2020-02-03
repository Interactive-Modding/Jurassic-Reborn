package mod.reborn.client.render.item.models;

import javax.vecmath.Matrix4f;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraftforge.client.model.BakedModelWrapper;

import java.util.List;

public class GuiItemModelWrapper extends BakedModelWrapper<IBakedModel> {

    private List<TransformType> transformTypes = Lists.newArrayList(TransformType.GUI, TransformType.FIXED, TransformType.NONE);

    private final IBakedModel guiModel;
    
    public GuiItemModelWrapper(IBakedModel originalModel, IBakedModel guiModel) {
	super(originalModel);
	this.guiModel = guiModel;
    }
    
    @Override
    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(TransformType cameraTransformType) {
        return transformTypes.contains(cameraTransformType) ? guiModel.handlePerspective(cameraTransformType) : super.handlePerspective(cameraTransformType);
    }

}
