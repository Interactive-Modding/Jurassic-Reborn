package mod.reborn.client.model;

import org.spongepowered.asm.mixin.MixinEnvironment;

@SideOnly(MixinEnvironment.Side.CLIENT)
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

    private void parseCube(TabulaCubeContainer cube, FixedModelRenderer parent) {
        FixedModelRenderer box = this.createBox(cube);
        this.cubes.put(cube.getName(), box);
        this.identifierMap.put(cube.getIdentifier(), box);
        if (parent != null) {
            parent.addChild(box);
        } else {
            this.rootBoxes.add(box);
        }
        for (TabulaCubeContainer child : cube.getChildren()) {
            this.parseCube(child, box);
        }
    }

    private FixedModelRenderer createBox(TabulaCubeContainer cube) {
        int[] textureOffset = cube.getTextureOffset();
        double[] position = cube.getPosition();
        double[] rotation = cube.getRotation();
        double[] offset = cube.getOffset();
        int[] dimensions = cube.getDimensions();
        FixedModelRenderer box = new FixedModelRenderer(this, cube.getName());
        box.setTextureOffset(textureOffset[0], textureOffset[1]);
        box.setRotationPoint((float) position[0], (float) position[1], (float) position[2]);
        box.addBox((float) offset[0], (float) offset[1], (float) offset[2], dimensions[0], dimensions[1], dimensions[2], 0.0F);
        box.rotateAngleX = (float) Math.toRadians(rotation[0]);
        box.rotateAngleY = (float) Math.toRadians(rotation[1]);
        box.rotateAngleZ = (float) Math.toRadians(rotation[2]);
        return box;
    }
}