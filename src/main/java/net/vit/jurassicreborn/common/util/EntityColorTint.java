package net.vit.jurassicreborn.common.util;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.Entity;

import java.awt.*;
import java.util.ArrayList;

public class EntityColorTint {
    /**
     * This should be changed every time an entity in the class list is rendered. Default is nothing.
     */
    public static Color tintColor = new Color(0, 0, 0, 0);

    public static ArrayList<Class<? extends Entity>> entityClasses = new ArrayList<>();

    private static EntityRenderer<?> renderer;


    /**
     * @return the current tint color
     */
    public static Color getColor() {
        return tintColor;
    }

    public static void clearColor(){
        tintColor = new Color(0, 0, 0, 0);
    }

    /**
     * The color is reset right after my mixin renders the entity with the color.
     * @param color The desired tint color
     */
    public static void setColor(Color color) {
        tintColor = color;
    }

    public static void setEntityToColor(EntityRenderer<?> renderer){
        EntityColorTint.renderer = renderer;
    }


    public static void addEntityClassToList(Class<? extends Entity> clazz) {
        entityClasses.add(clazz);
    }

    public static void removeEntityClassFromList(Class<? extends Entity> clazz) {
        entityClasses.remove(clazz);
    }

    /**
     *
     * @param test The entity to be tested
     * @return Whether or not there is a class in the list that is a superclass of the test entity
     */
    public static boolean isEntityInList(Entity test){
        for(Class<? extends Entity> entity : entityClasses){
            if(entity.isAssignableFrom(test.getClass())){
                return true;
            }
        }
        return false;
    }
}
