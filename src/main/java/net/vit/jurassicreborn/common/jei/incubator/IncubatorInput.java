package net.vit.jurassicreborn.common.jei.incubator;

import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;

/** Simple wrapper storing the dinosaur for incubator recipes. */
public class IncubatorInput {
    private final Dinosaur dinosaur;

    public IncubatorInput(Dinosaur dinosaur) {
        this.dinosaur = dinosaur;
    }

    public Dinosaur getDinosaur() {
        return this.dinosaur;
    }
}
