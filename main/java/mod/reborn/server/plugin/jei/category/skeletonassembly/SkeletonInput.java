package mod.reborn.server.plugin.jei.category.skeletonassembly;

import mod.reborn.server.dinosaur.Dinosaur;

public class SkeletonInput {
    public final Dinosaur dinosaur;
    public final boolean fresh;

    public SkeletonInput(Dinosaur dinosaur, boolean fresh) {
        this.dinosaur = dinosaur;
        this.fresh = fresh;
    }
}
