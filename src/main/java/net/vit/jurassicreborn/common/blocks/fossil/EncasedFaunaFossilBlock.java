package net.vit.jurassicreborn.common.blocks.fossil;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import java.util.Random;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.vit.jurassicreborn.common.blocks.entities.EncasedFaunaFossilBlockEntity;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;
import net.vit.jurassicreborn.common.items.ModItems;
import net.vit.jurassicreborn.common.util.FossilUtil;
import net.vit.jurassicreborn.common.util.ItemsUtil;
import net.vit.jurassicreborn.common.util.api.CleanableItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class EncasedFaunaFossilBlock extends Block implements EncasedFossil, EntityBlock, CleanableItem {
    private final Dinosaur defaultDino;

    public EncasedFaunaFossilBlock(Properties properties, Dinosaur defaultDino) {
        super(properties);
        this.defaultDino = defaultDino;
    }

    private Dinosaur readDino(ItemStack stack) {
        Dinosaur dino = FossilUtil.getDino(stack);
        return (dino == null || dino == Dinosaur.EMPTY) ? defaultDino : dino;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new EncasedFaunaFossilBlockEntity(pos, state);
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player) {
        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof EncasedFaunaFossilBlockEntity fossilBe) {
            ItemStack stack = new ItemStack(this);
            fossilBe.saveToItem(stack); // Save dino tag
            return stack;
        }
        return super.getCloneItemStack(state, target, level, pos, player);
    }

    @Override
    public boolean isCleanable(ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack getCleanedItem(ItemStack stack, Random random) {
        Dinosaur dino = readDino(stack);
        String[] bones = dino.getBones();
        LinkedHashMap<String, ?> boneMap = ModItems.BONES.get(dino);

        if (boneMap == null || bones == null || bones.length == 0)
            return ItemStack.EMPTY;
        String boneKey = bones.length > 1 ? bones[random.nextInt(bones.length)] : bones[0];
        Item bone = ((net.minecraftforge.registries.RegistryObject<Item>) boneMap.get(boneKey)).get();

        return new ItemStack(bone);
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