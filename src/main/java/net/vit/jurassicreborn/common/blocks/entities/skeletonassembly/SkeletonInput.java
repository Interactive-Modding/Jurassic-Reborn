package net.vit.jurassicreborn.common.blocks.entities.skeletonassembly;

import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;

public class SkeletonInput {
    public final Dinosaur dinosaur;
    public final boolean fresh;

    public SkeletonInput(Dinosaur dinosaur, boolean fresh) {
        this.dinosaur = dinosaur;
        this.fresh = fresh;
    }
}
