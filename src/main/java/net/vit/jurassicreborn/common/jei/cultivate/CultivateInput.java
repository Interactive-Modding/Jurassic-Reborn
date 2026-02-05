package net.vit.jurassicreborn.common.jei.cultivate;

import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;

/** Simple wrapper storing the dinosaur for cultivator recipes. */
public class CultivateInput {
    public final Dinosaur dino;
    public CultivateInput(Dinosaur dino) { this.dino = dino; }
}