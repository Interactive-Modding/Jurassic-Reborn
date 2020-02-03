package mod.reborn.client.model.animation.dto;

public class PoseDTO {
    /**
     * The pose's tabular file location
     */
    public String pose;
    public transient int index;
    /**
     * The ticks it takes to transition into the pose
     */
    public float time;
}
