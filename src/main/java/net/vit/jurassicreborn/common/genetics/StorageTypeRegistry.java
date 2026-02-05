package net.vit.jurassicreborn.common.genetics;

import java.util.HashMap;
import java.util.Objects;
import java.util.function.Supplier;

public class StorageTypeRegistry {
    private static final HashMap<String, Supplier<? extends StorageType>> STORAGE_TYPES = new HashMap<>();

    public static void init() {
        register("DinoDNA", DinosaurDNAStorageType::new);
        register("PlantDNA", PlantDNAStorageType::new);
    }

    private static void register(String id, Supplier<? extends StorageType> storageType) {
        if(STORAGE_TYPES.containsKey(id))
            return;
        STORAGE_TYPES.put(id, Objects.requireNonNull(storageType));
    }

    public static StorageType getStorageType(String id) {
        init();
        if (id == null || id.isEmpty()) {
            id = "DinoDNA";
        }
        return STORAGE_TYPES.get(id).get();
    }
}