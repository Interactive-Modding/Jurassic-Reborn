package mod.reborn.server.plugin.jei.category.incubator;

import mod.reborn.server.dinosaur.Dinosaur;

public class IncubatorInput {

    private final Dinosaur dinosaur;

    public IncubatorInput(Dinosaur dinosaur) {
        this.dinosaur = dinosaur;
    }

    public Dinosaur getDinosaur() {
        return this.dinosaur;
    }

}