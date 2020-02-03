package mod.reborn.server.genetics;

import com.google.common.base.Supplier;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class StorageTypeRegistry {
    private static final Map<String, Supplier<? extends StorageType>> STORAGE_TYPES = new HashMap<>();

    public static void init() {
        register("DinoDNA", DinosaurDNAStorageType::new);
        register("PlantDNA", PlantDNAStorageType::new);
    }

    private static void register(String id, Supplier<? extends StorageType> storageType) {
        STORAGE_TYPES.put(id, Objects.requireNonNull(storageType));
    }

    public static StorageType getStorageType(String id) {
        if (id == null || id.isEmpty()) {
            id = "DinoDNA";
        }
        return STORAGE_TYPES.get(id).get();
    }
}
