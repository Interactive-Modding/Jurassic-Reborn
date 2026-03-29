package net.vit.jurassicreborn.client.model;


import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import it.unimi.dsi.fastutil.objects.ObjectList;

public class TabulaModelFixer {

    public static void fixModel(AdvancedModelBox root) {
        applyOffsetFix(root);
    }

    private static void applyOffsetFix(BasicModelPart part) {
        if (part instanceof AdvancedModelBox) {
            AdvancedModelBox advBox = (AdvancedModelBox) part;

            advBox.rotationPointX += advBox.offsetX;
            advBox.rotationPointY += advBox.offsetY;
            advBox.rotationPointZ += advBox.offsetZ;

            advBox.offsetX = 0;
            advBox.offsetY = 0;
            advBox.offsetZ = 0;
        }

        if (part instanceof FixedModelRenderer) {
            ObjectList<BasicModelPart> children = ((FixedModelRenderer) part).childModels;
            if (children != null) {
                for (BasicModelPart child : children) {
                    applyOffsetFix(child);
                }
            }
        }
    }

}
