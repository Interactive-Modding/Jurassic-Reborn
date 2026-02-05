package net.vit.jurassicreborn.common.items.Fossils;

import com.mojang.datafixers.util.Pair;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.vit.jurassicreborn.common.util.FossilUtil;
import net.vit.jurassicreborn.common.util.ItemsUtil;
import net.vit.jurassicreborn.common.util.LangUtil;
import net.vit.jurassicreborn.common.util.api.CleanableItem;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;
import net.vit.jurassicreborn.common.items.ModItems;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class EncasedFaunaFossilBlockItem extends FossilBlockItem implements CleanableItem {
    // Store the default dinosaur to fall back on
    private final Dinosaur defaultDino;

    public EncasedFaunaFossilBlockItem(Block block, Dinosaur defaultDino, Properties properties) {
        super(block, properties);
        this.defaultDino = defaultDino;
    }

    /**
     * Read the dinosaur from NBT.
     * If not present, fall back to the default dinosaur.
     */
    private Dinosaur readDino(ItemStack stack) {
        Dinosaur dino = FossilUtil.getDino(stack);
        return (dino == null || dino == Dinosaur.EMPTY) ? defaultDino : dino;
    }

    @Override
    public Component getName(ItemStack stack) {
        Dinosaur dino = readDino(stack);
        String dinoName = LangUtil.getDinoName(dino).getString();
        return Component.literal("Encased " + dinoName + " Fossil");
    }

    @Override
    public boolean isCleanable(ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack getCleanedItem(ItemStack stack, RandomSource random) {
        Dinosaur dino = readDino(stack);
        String[] bones = dino.getBones();
        // Use the mapping stored in ModItems.BONES
        LinkedHashMap<String, ?> boneMap = ModItems.BONES.get(dino);
        if (boneMap == null || bones == null || bones.length == 0) return ItemStack.EMPTY;
        // Choose a random bone name from the array
        String boneKey = bones.length > 1 ? bones[random.nextInt(bones.length)] : bones[0];

        Item bone = ((net.minecraftforge.registries.RegistryObject<Item>) boneMap.get(boneKey)).get();
        return new ItemStack(bone, 1);
    }

    @Override
    public List<Pair<Float, ItemStack>> getChancedOutputs(ItemStack inputItem) {
        Dinosaur dino = readDino(inputItem);
        String[] bones = dino.getBones();
        if (bones == null || bones.length == 0) {
            return new ArrayList<>();
        }

        float chance = 100f / bones.length;
        List<Pair<Float, ItemStack>> list = new ArrayList<>();
        for (String bone : bones) {
            Item boneItem = ItemsUtil.getFossilDinosaurBone(dino, bone);
            if (boneItem != null) {
                list.add(Pair.of(chance, new ItemStack(boneItem)));
            }
        }
        return list;
    }


}
