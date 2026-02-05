package net.vit.jurassicreborn.common.util;

import net.minecraft.world.entity.Entity;

import java.awt.*;

public interface TintModifier {
    Color getColor();

    void setColor(Color color);

    void addEntityClassToList(Class<? extends Entity> clazz);
    void removeEntityClassFromList(Class<? extends Entity> clazz);
}
