package net.vit.jurassicreborn.common.util;

import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;
import net.vit.jurassicreborn.common.items.ModItems;

import javax.annotation.Nullable;

public class ItemsUtil {

    @Nullable
    public static Item getFreshDinosaurBone(Dinosaur dino, String bone){
        java.util.LinkedHashMap<String, DeferredItem<Item>> map = ModItems.FRESH_BONES.get(dino);
        if (map == null) return null;
        DeferredItem<Item> regObj = map.get(bone);
        return regObj != null ? regObj.get() : null;
    }

    @Nullable
    public static Item getFossilDinosaurBone(Dinosaur dino, String bone){
        if(dino.isHybrid)
            return null;
        java.util.LinkedHashMap<String, DeferredItem<Item>> map = ModItems.BONES.get(dino);
        if (map == null) return null;
        DeferredItem<Item> regObj = map.get(bone);
        return regObj != null ? regObj.get() : null;
    }

    public static Item getMeatForDinosaur(Dinosaur dino){
        DeferredItem<Item> regObj = ModItems.MEATS.get(dino);
        return regObj != null ? regObj.get() : null;
    }

    public static Item getSteakForDinosaur(Dinosaur dino){
        DeferredItem<Item> regObj = ModItems.STEAKS.get(dino);
        return regObj != null ? regObj.get() : null;
    }
}
