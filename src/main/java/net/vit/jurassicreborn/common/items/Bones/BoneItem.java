package net.vit.jurassicreborn.common.items.Bones;

import net.vit.jurassicreborn.common.entities.Dinosaurs.Bone;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class BoneItem extends Item {

    private final Bone.BoneGroup groupOwner;
    private final Bone shape;

    public BoneItem(Properties pProperties, Bone member, Bone.BoneGroup groupOwner) {
        super(pProperties);
        this.groupOwner = groupOwner;
        this.shape = member;
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack pStack) {
        MutableComponent template = Component.translatable("item." + this.shape + "name");
        String replaced = template.getString().replace("{dino}", this.groupOwner.getOwner().getName());
        return Component.literal(replaced);
    }

    public Bone getShape(){
        return shape;
    }

    public Bone.BoneGroup getOwner(){
        return this.groupOwner;
    }


}
