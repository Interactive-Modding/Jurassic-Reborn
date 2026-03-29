package net.vit.jurassicreborn.common.items.Bones;

import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Bone;
import net.minecraft.world.item.Item;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.ArrayList;
import java.util.HashMap;

public class DynamicBoneRegistry {

    public static HashMap<Bone.BoneGroup, ArrayList<DeferredHolder<Item, BoneItem>>> BoneMap = new HashMap<>();

    public static DeferredRegister<Item> BoneDeferredRegister = DeferredRegister.create(Registries.ITEM, JurassicReborn.MODID);

//    public static void addBoneGroup(Bone.BoneGroup group){
//        group.registerToHashMap(BoneMap, BoneDeferredRegister);
//    }

}
