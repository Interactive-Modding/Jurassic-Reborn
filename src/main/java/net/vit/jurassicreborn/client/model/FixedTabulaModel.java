package net.vit.jurassicreborn.client.model;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.ITabulaModelAnimator;
import com.github.alexthe666.citadel.client.model.TabulaModel;
import com.github.alexthe666.citadel.client.model.container.TabulaCubeContainer;
import com.github.alexthe666.citadel.client.model.container.TabulaCubeGroupContainer;
import com.github.alexthe666.citadel.client.model.container.TabulaModelContainer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class FixedTabulaModel extends TabulaModel {
    public FixedTabulaModel(TabulaModelContainer container, ITabulaModelAnimator<?> tabulaAnimator) {
        super(container, tabulaAnimator);
        this.cubes.clear();
        this.identifierMap.clear();
        this.rootBoxes.clear();
        for (TabulaCubeContainer cube : container.getCubes()) {
            this.parseCube(cube, null);
        }
        container.getCubeGroups().forEach(this::parseCubeGroup);
        for (AdvancedModelBox rootBox : this.rootBoxes) {
            TabulaModelFixer.fixModel(rootBox);
        }
        this.updateDefaultPose();
    }

    public FixedTabulaModel(TabulaModelContainer container) {
        this(container, null);
    }

    private void parseCubeGroup(TabulaCubeGroupContainer container) {
        for (TabulaCubeContainer cube : container.getCubes()) {
            this.parseCube(cube, null);
        }
        container.getCubeGroups().forEach(this::parseCubeGroup);
    }

    private void parseCube(TabulaCubeContainer cube, AdvancedModelBox parent) {
        AdvancedModelBox box = this.createBox(cube);

        String baseName = cube.getName();
        String uniqueName = baseName;
        int nameCounter = 1;
        while (this.cubes.containsKey(uniqueName)) {
            uniqueName = baseName + "_" + nameCounter++;
        }
        this.cubes.put(uniqueName, box);

        String baseId = cube.getIdentifier();
        String uniqueId = baseId;
        int idCounter = 1;
        while (this.identifierMap.containsKey(uniqueId)) {
            uniqueId = baseId + "_" + idCounter++;
        }
        this.identifierMap.put(uniqueId, box);

        if (parent != null) {
            parent.addChild(box);
        } else {
            this.rootBoxes.add(box);
        }

        for (TabulaCubeContainer child : cube.getChildren()) {
            this.parseCube(child, box);
        }
    }

    private AdvancedModelBox createBox(TabulaCubeContainer cube) {
        int[] textureOffset = cube.getTextureOffset();
        double[] position = cube.getPosition();
        double[] rotation = cube.getRotation();
        double[] offset = cube.getOffset();
        int[] dimensions = cube.getDimensions();
        boolean mirror = cube.isTextureMirrorEnabled();

        FixedModelRenderer box = new FixedModelRenderer(this, cube.getName());
        box.setTextureOffset(textureOffset[0], textureOffset[1]);
        box.setRotationPoint((float) position[0], (float) position[1], (float) position[2]);

        box.addBox(
                (float) offset[0], (float) offset[1], (float) offset[2],
                dimensions[0], dimensions[1], dimensions[2],
                0.0F,
                mirror
        );
        box.rotateAngleX = (float) Math.toRadians(rotation[0]);
        box.rotateAngleY = (float) Math.toRadians(rotation[1]);
        box.rotateAngleZ = (float) Math.toRadians(rotation[2]);
        return box;
    }
}
