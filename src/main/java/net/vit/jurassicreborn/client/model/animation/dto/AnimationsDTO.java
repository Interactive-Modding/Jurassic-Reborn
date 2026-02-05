package net.vit.jurassicreborn.client.model.animation.dto;

import java.util.Map;

/**
 * This class can be loaded via {@link Gson#fromJson}. It represents the poses of the animations of a model.
 *
 * @author WorldSEnder
 */
public class AnimationsDTO {
    /**
     * Maps an {@link EntityAnimation} as a string to the list of sequential poses
     */
    public Map<String, PoseDTO[]> poses;
    public int version;
}
