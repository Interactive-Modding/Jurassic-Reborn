package net.vit.jurassicreborn.common.entities.Dinosaurs;


import java.util.ArrayList;
import java.util.Arrays;

public enum Bone{
    ARM_BONES("arm_bones"),
    FOOT_BONES("foot_bones"),
    LEG_BONES("leg_bones"),
    NECK_VERTEBRAE("neck_vertebrae"),
    RIBCAGE("ribcage"),
    SKULL("skull"),
    SHOULDER("shoulder"),
    CLAW("claw"),
    PELVIS("pelvis"),
    TAIL_VERTEBRAE("tail_vertebrae"),
    TOOTH("tooth"),
    RADULA("radula"),
    BEAK("beak");


    private final String name;

    Bone(String name){
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public static class BoneGroup {

        private ArrayList<Bone> bones = new ArrayList<>();

        private Dinosaur owner;

        public BoneGroup(Dinosaur owner){
            this.owner = owner;
        }

        public Dinosaur getOwner() {
            return owner;
        }

        public void setOwner(Dinosaur owner) {
            this.owner = owner;
        }

        public BoneGroup addBones(Bone... b){
            this.bones.addAll(Arrays.asList(b));
            return this;
        }

        public BoneGroup addBone(Bone b){
            this.bones.add(b);
            return this;
        }

//        public void registerToHashMap(HashMap<BoneGroup, ArrayList<RegistryObject<BoneItem>>> toRegister, DeferredRegister<Item> registerer){
//            ArrayList<RegistryObject<BoneItem>> temp = new ArrayList<>();
//            for(Bone b : this.bones){
//                temp.add(registerer.register(b.name + this.owner.getName(), () -> new BoneItem(new Item.Properties(), b, this)));
//            }
//            toRegister.put(this, temp);
//        }


    }
}

