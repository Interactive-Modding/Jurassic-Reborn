package net.vit.jurassicreborn.client.model;


import com.github.alexthe666.citadel.client.model.container.TabulaModelContainer;

public class TabulaModelUV extends TabulaModelContainer {
    public TabulaModelUV(TabulaModelContainer container, int height, int width) {
        super(container.getName(), container.getAuthor(), width, height, container.getCubes(), container.getProjectVersion());
    }
}