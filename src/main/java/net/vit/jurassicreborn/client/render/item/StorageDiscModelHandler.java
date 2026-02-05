package net.vit.jurassicreborn.client.render.item;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap.Entry;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.common.items.genetics.StorageDiscModelData;

import java.util.Map;

@Mod.EventBusSubscriber(modid = JurassicReborn.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class StorageDiscModelHandler {
    private StorageDiscModelHandler() {}

    @SubscribeEvent
    public static void onRegisterAdditional(ModelEvent.RegisterAdditional event) {
        Int2ObjectMap<ResourceLocation> models = StorageDiscModelData.getModels();
        ObjectIterator<Int2ObjectMap.Entry<ResourceLocation>> iterator = models.int2ObjectEntrySet().iterator();
        while (iterator.hasNext()) {
            Int2ObjectMap.Entry<ResourceLocation> entry = iterator.next();
            event.register(new ModelResourceLocation(entry.getValue(), "inventory"));
        }
    }

    @SubscribeEvent
    public static void onModifyBakingResult(ModelEvent.ModifyBakingResult event) {
        injectStorageDiscModel(event.getModels());
    }

    @SubscribeEvent
    public static void onBakingCompleted(ModelEvent.BakingCompleted event) {
        injectStorageDiscModel(event.getModels());
    }

    private static void injectStorageDiscModel(Map<?, BakedModel> models) {
        ResourceLocation storageDiscLoc = new ResourceLocation(JurassicReborn.MODID, "storage_disc");
        ModelResourceLocation storageDiscModelLoc = new ModelResourceLocation(storageDiscLoc, "inventory");

        BakedModel baseModel = (BakedModel) models.get(storageDiscModelLoc);
        if (baseModel == null || baseModel instanceof StorageDiscBakedModel)
            baseModel = (BakedModel) models.get(storageDiscLoc);

        if (baseModel == null || baseModel instanceof StorageDiscBakedModel) return;

        Int2ObjectMap<ResourceLocation> modelLocations = StorageDiscModelData.getModels();
        Int2ObjectMap<BakedModel> bakedVariants = new Int2ObjectArrayMap<>();

        for (Int2ObjectMap.Entry<ResourceLocation> entry : modelLocations.int2ObjectEntrySet()) {
            ModelResourceLocation loc = new ModelResourceLocation(entry.getValue(), "inventory");
            BakedModel variant = (BakedModel) models.get(loc);
            if (variant == null) variant = (BakedModel) models.get(entry.getValue());
            if (variant != null) bakedVariants.put(entry.getIntKey(), variant);
        }

        StorageDiscBakedModel wrapped = new StorageDiscBakedModel(baseModel, bakedVariants);
        try {
            // Try both possible key types
            if (models.containsKey(storageDiscModelLoc))
                ((Map<ModelResourceLocation, BakedModel>) models).put(storageDiscModelLoc, wrapped);
            else
                ((Map<ResourceLocation, BakedModel>) models).put(storageDiscLoc, wrapped);
        } catch (UnsupportedOperationException e) {
            JurassicReborn.getLogger().warn(
                    "Skipping storage disc model injection because baked model map is immutable ({}).",
                    models.getClass().getName(), e
            );
        }
    }
}
