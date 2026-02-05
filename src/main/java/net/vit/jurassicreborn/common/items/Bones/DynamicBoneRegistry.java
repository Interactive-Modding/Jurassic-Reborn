package net.vit.jurassicreborn.common.items.Bones;

import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Bone;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.HashMap;

public class DynamicBoneRegistry {

    public static HashMap<Bone.BoneGroup, ArrayList<RegistryObject<BoneItem>>> BoneMap = new HashMap<>();

    public static DeferredRegister<Item> BoneDeferredRegister = DeferredRegister.create(ForgeRegistries.ITEMS, JurassicReborn.MODID);

//    public static void addBoneGroup(Bone.BoneGroup group){
//        group.registerToHashMap(BoneMap, BoneDeferredRegister);
//    }

}
