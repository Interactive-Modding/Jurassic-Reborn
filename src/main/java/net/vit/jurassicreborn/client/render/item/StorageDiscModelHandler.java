//package net.vit.jurassicreborn.client.render.item;
//
//import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
//import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
//import it.unimi.dsi.fastutil.ints.Int2ObjectMap.Entry;
//import it.unimi.dsi.fastutil.objects.ObjectIterator;
//import net.minecraft.client.resources.model.BakedModel;
//import net.minecraft.client.resources.model.ModelResourceLocation;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.client.event.ModelBakeEvent;
//import net.minecraftforge.client.event.ModelRegistryEvent;
//import net.minecraftforge.eventbus.api.SubscribeEvent;
//import net.minecraftforge.fml.common.Mod;
//import net.vit.jurassicreborn.JurassicReborn;
//import net.vit.jurassicreborn.common.items.genetics.StorageDiscModelData;
//
//@Mod.EventBusSubscriber(modid = JurassicReborn.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
//public final class StorageDiscModelHandler {
//    private StorageDiscModelHandler() {}
//package net.vit.jurassicreborn.client.render.item;
//
//import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
//import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
//import it.unimi.dsi.fastutil.objects.ObjectIterator;
//import net.minecraft.client.resources.model.BakedModel;
//import net.minecraft.client.resources.model.ModelResourceLocation;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.client.event.ModelEvent;
//import net.minecraftforge.eventbus.api.SubscribeEvent;
//import net.minecraftforge.fml.common.Mod;
//import net.vit.jurassicreborn.JurassicReborn;
//import net.vit.jurassicreborn.common.items.genetics.StorageDiscModelData;
//
//@Mod.EventBusSubscriber(modid = JurassicReborn.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
//public final class StorageDiscModelHandler {
//    private StorageDiscModelHandler() {}
//
//    @SubscribeEvent
//    public static void onRegisterAdditional(ModelEvent.RegisterAdditional event) {
//        Int2ObjectMap<ResourceLocation> models = StorageDiscModelData.getModels();
//        for (Int2ObjectMap.Entry<ResourceLocation> entry : models.int2ObjectEntrySet()) {
//            event.register(new ModelResourceLocation(entry.getValue(), "inventory"));
//        }
//    }
//
//    @SubscribeEvent
//    public static void onBakingCompleted(ModelEvent.BakingCompleted event) {
//        ModelResourceLocation storageDisc = new ModelResourceLocation(
//                new ResourceLocation(JurassicReborn.MODID, "storage_disc"),
//                "inventory"
//        );
//
//        var models = event.getModels();
//        BakedModel baseModel = models.get(storageDisc);
//        if (baseModel == null) return;
//
//        Int2ObjectMap<ResourceLocation> modelLocations = StorageDiscModelData.getModels();
//        Int2ObjectMap<BakedModel> bakedVariants = new Int2ObjectArrayMap<>();
//
//        for (Int2ObjectMap.Entry<ResourceLocation> entry : modelLocations.int2ObjectEntrySet()) {
//            ModelResourceLocation loc = new ModelResourceLocation(entry.getValue(), "inventory");
//            BakedModel variant = models.get(loc);
//            if (variant != null) {
//                bakedVariants.put(entry.getIntKey(), variant);
//            }
//        }
//
//        StorageDiscBakedModel wrapped = new StorageDiscBakedModel(baseModel, bakedVariants);
//        models.put(storageDisc, wrapped);
//    }
//}
//    @SubscribeEvent
//    public static void onRegisterAdditional(ModelRegistryEvent event) {
//        Int2ObjectMap<ResourceLocation> models = StorageDiscModelData.getModels();
//        ObjectIterator<Int2ObjectMap.Entry<ResourceLocation>> iterator = models.int2ObjectEntrySet().iterator();
//        while (iterator.hasNext()) {
//            Int2ObjectMap.Entry<ResourceLocation> entry = iterator.next();
//            ModelLoader.addSpecialModel(new ModelResourceLocation(entry.getValue(), "inventory"));
//        }
//    }
//
//    @SubscribeEvent
//    public static void onBakingCompleted(ModelBakeEvent event) {
//        ModelResourceLocation storageDisc = new ModelResourceLocation(
//                new ResourceLocation(JurassicReborn.MODID, "storage_disc"),
//                "inventory"
//        );
//
//        // This map is mutable in the relevant 1.19.x Forge builds
//        var models = event.getModelRegistry();
//
//        BakedModel baseModel = models.get(storageDisc);
//        if (baseModel == null) return;
//
//        Int2ObjectMap<ResourceLocation> modelLocations = StorageDiscModelData.getModels();
//        ObjectIterator<Int2ObjectMap.Entry<ResourceLocation>> it = modelLocations.int2ObjectEntrySet().iterator();
//        Int2ObjectMap<BakedModel> bakedVariants = new Int2ObjectArrayMap<>();
//
//        while (it.hasNext()) {
//            Int2ObjectMap.Entry<ResourceLocation> entry = it.next();
//            ModelResourceLocation loc = new ModelResourceLocation(entry.getValue(), "inventory");
//            BakedModel variant = models.get(loc);
//            if (variant != null) {
//                bakedVariants.put(entry.getIntKey(), variant);
//            }
//        }
//
//        StorageDiscBakedModel wrapped = new StorageDiscBakedModel(baseModel, bakedVariants);
//        models.put(storageDisc, wrapped);
//    }
//}
