package net.vit.jurassicreborn.common.items.Bones;

import net.vit.jurassicreborn.common.entities.Dinosaurs.Bone;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.LiteralContents;
import net.minecraft.network.chat.contents.TranslatableContents;
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
        TranslatableContents translatedDinoless = new TranslatableContents("item." + this.shape.toString() + "name");

        LiteralContents translated = new LiteralContents(MutableComponent.create(translatedDinoless).getString().replaceAll("\\{dino}", this.groupOwner.getOwner().getName()));

        return MutableComponent.create(translated);
    }

    public Bone getShape(){
        return shape;
    }

    public Bone.BoneGroup getOwner(){
        return this.groupOwner;
    }


}
