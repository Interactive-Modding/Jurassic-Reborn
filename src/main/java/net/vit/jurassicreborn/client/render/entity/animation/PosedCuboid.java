package net.vit.jurassicreborn.client.render.entity.animation;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;

public class PosedCuboid {
    public float rotationX;
    public float rotationY;
    public float rotationZ;

    public float positionX;
    public float positionY;
    public float positionZ;

    public PosedCuboid(AdvancedModelBox cuboid) {
        this.rotationX = cuboid.defaultRotationX;
        this.rotationY = cuboid.defaultRotationY;
        this.rotationZ = cuboid.defaultRotationZ;

        this.positionX = cuboid.defaultPositionX;
        this.positionY = cuboid.defaultPositionY;
        this.positionZ = cuboid.defaultPositionZ;
    }
}
