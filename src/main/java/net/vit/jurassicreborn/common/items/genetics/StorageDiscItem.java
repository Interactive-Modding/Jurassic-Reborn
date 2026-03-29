package net.vit.jurassicreborn.common.items.genetics;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomModelData;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;
import net.vit.jurassicreborn.common.genetics.*;
import net.vit.jurassicreborn.common.plants.PlantHandler;
import net.vit.jurassicreborn.common.util.api.SynthesizableItem;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import org.jetbrains.annotations.Nullable;
import java.util.List;
import net.vit.jurassicreborn.common.util.ItemStackNbtUtil;

public class StorageDiscItem extends Item implements SynthesizableItem {
    public StorageDiscItem(Properties pProperties) {
        super(pProperties);
    }


    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> toolTip, TooltipFlag pIsAdvanced) {

        CompoundTag tag = ItemStackNbtUtil.getTag(stack);
        if(tag == null) {
            toolTip.add(Component.translatable("cage.empty").withStyle(ChatFormatting.DARK_RED));
            return;
        }else if(!tag.contains("DNA")){
            toolTip.add(Component.translatable("cage.empty").withStyle(ChatFormatting.DARK_RED));
            return;
        }


        CompoundTag dna = tag.getCompound("DNA");

        String storageId = dna.getString("StorageId");
        StorageType type = StorageTypeRegistry.getStorageType(storageId);
        if (type != null) {
            type.load(tag);
            type.addInformation(stack, toolTip);
        }

        super.appendHoverText(stack, context, toolTip, pIsAdvanced);
    }

    @Override
    public boolean isSynthesizable(ItemStack stack) {
        CompoundTag tagCompound = ItemStackNbtUtil.getTag(stack);
        return tagCompound != null && tagCompound.contains("DNA") && tagCompound.getCompound("DNA").getInt("DNAQuality") == 100;
    }

    @Override
    public ItemStack getSynthesizedItem(ItemStack stack, RandomSource random) {
        CompoundTag tag = ItemStackNbtUtil.getTag(stack);
        StorageType type = StorageTypeRegistry.getStorageType(tag.getCompound("DNA").getString("StorageId"));
        DNA dna = type.load(tag);

        ItemStack result = type.createItem();
        return result == null ? ItemStack.EMPTY : result;
    }

    @Override
    public List<Pair<Float, ItemStack>> getChancedOutputs(ItemStack inputItem) {
        CompoundTag tag = ItemStackNbtUtil.getTag(inputItem);
        StorageType type = StorageTypeRegistry.getStorageType(tag.getCompound("DNA").getString("StorageId"));
        type.load(tag);
        ItemStack result = type.createItem();
        if (result.isEmpty()) {
            return Lists.newArrayList();
        }
        return Lists.newArrayList(new Pair<>(100F, result));
    }

    @Override
    public List<ItemStack> getJEIRecipeTypes() {
        List<ItemStack> list = Lists.newArrayList();

        Dinosaur.DINOS.forEach(dino -> {
            DinoDNA dna = new DinoDNA(dino, 100, "");
            ItemStack stack = new ItemStack(this);
            CompoundTag nbt = new CompoundTag();
            dna.writeToNBT(nbt);
            ItemStackNbtUtil.setTag(stack, nbt);
            applyCustomModelData(stack);
            list.add(stack);
        });

        PlantHandler.getPlants().forEach((plant) -> {
            PlantDNA dna = new PlantDNA(PlantHandler.getPlantId(plant), 100);
            ItemStack stack = new ItemStack(this);
            CompoundTag nbt = new CompoundTag();
            dna.writeToNBT(nbt);
            ItemStackNbtUtil.setTag(stack, nbt);
            applyCustomModelData(stack);
            list.add(stack);

        });
        return list;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, level, entity, slot, selected);
        if (!level.isClientSide) {
            applyCustomModelData(stack);
        }
    }

    @Override
    public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entity) {
        if (!entity.level().isClientSide) {
            applyCustomModelData(stack);
        }
        return super.onEntityItemUpdate(stack, entity);
    }

    public static void applyCustomModelData(ItemStack stack) {
        if (stack.isEmpty() || !(stack.getItem() instanceof StorageDiscItem)) {
            return;
        }

        CompoundTag tag = ItemStackNbtUtil.getTag(stack);
        if (tag == null || !tag.contains("DNA")) {
            clearCustomModelData(stack, tag);
            return;
        }

        CompoundTag dnaTag = tag.getCompound("DNA");
        String storageId = dnaTag.getString("StorageId");
        int modelData = 0;

        if ("DinoDNA".equals(storageId)) {
            Dinosaur dinosaur = Dinosaur.getDinosaurByName(dnaTag.getString("Dinosaur"));
            modelData = StorageDiscModelData.resolveDinosaur(dinosaur);
        } else if ("PlantDNA".equals(storageId)) {
            String plantId = dnaTag.getString("Plant");
            if (!plantId.isEmpty()) {
                ResourceLocation location = ResourceLocation.tryParse(plantId);
                modelData = StorageDiscModelData.resolvePlant(location);
            }
        }

        if (modelData > 0) {
            CustomModelData currentData = stack.get(DataComponents.CUSTOM_MODEL_DATA);
            if (currentData == null || currentData.value() != modelData) {
                stack.set(DataComponents.CUSTOM_MODEL_DATA, new CustomModelData(modelData));
            }
            if (!tag.contains("CustomModelData") || tag.getInt("CustomModelData") != modelData) {
                tag.putInt("CustomModelData", modelData);
                ItemStackNbtUtil.setTag(stack, tag);
            }
        } else {
            clearCustomModelData(stack, tag);
        }
    }

    private static void clearCustomModelData(ItemStack stack, @Nullable CompoundTag tag) {
        if (stack.has(DataComponents.CUSTOM_MODEL_DATA)) {
            stack.remove(DataComponents.CUSTOM_MODEL_DATA);
        }
        if (tag == null) {
            return;
        }
        if (tag.contains("CustomModelData")) {
            tag.remove("CustomModelData");
            if (tag.isEmpty()) {
                ItemStackNbtUtil.setTag(stack, null);
            } else {
                ItemStackNbtUtil.setTag(stack, tag);
            }
        }
    }
}