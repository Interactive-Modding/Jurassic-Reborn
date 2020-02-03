package mod.reborn.client.model.animation;

import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;

public class PosedCuboid {
    public float rotationX;
    public float rotationY;
    public float rotationZ;

    public float positionX;
    public float positionY;
    public float positionZ;

    public PosedCuboid(AdvancedModelRenderer cuboid) {
        this.rotationX = cuboid.defaultRotationX;
        this.rotationY = cuboid.defaultRotationY;
        this.rotationZ = cuboid.defaultRotationZ;

        this.positionX = cuboid.defaultPositionX;
        this.positionY = cuboid.defaultPositionY;
        this.positionZ = cuboid.defaultPositionZ;
    }
}
