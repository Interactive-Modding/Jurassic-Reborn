package net.vit.jurassicreborn.common.items.Fossils;

import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;
import net.vit.jurassicreborn.common.util.LangUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public abstract class FossilBlockItem extends BlockItem {
    public FossilBlockItem(Block pBlock, Properties pProperties) {
        super(pBlock, pProperties);
    }

    public ItemStack getStackWithDino(Dinosaur dino){
        ItemStack defaultStack = this.getDefaultInstance();

        return setDino(defaultStack, dino);
    }

    @Override
    public Component getName(ItemStack pStack) {
        Dinosaur dinosaur = this.getDino(pStack);
        return Component.literal(Component.translatable("block.jurassicreborn.fossil_block").getString().replace("{dinosaur}", LangUtil.getDinoName(dinosaur).getString()) );
    }

    public static Dinosaur getDino(ItemStack pStack){
        Dinosaur dinosaur = Dinosaur.getDinosaurByName(pStack.getTag() != null ? pStack.getTag().getCompound("BlockEntityTag").getString("Dinosaur") : Dinosaur.EMPTY.getName());
        return dinosaur;
    }

    public static ItemStack setDino(ItemStack pStack, Dinosaur dino){
        CompoundTag tag = pStack.getOrCreateTag();
        CompoundTag blockEntityTag = tag.getCompound("BlockEntityTag");
        blockEntityTag.putString("Dinosaur", dino.getName());
        tag.put("BlockEntityTag", blockEntityTag);
        pStack.setTag(tag);
        return pStack;
    }
}
