package mod.reborn.server.entity.ai.util;

import mod.reborn.server.entity.DinosaurEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.ClassInheritanceMultiMap;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityTravelToDimensionEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DinosaurPersistenceHandler {

    private static final Map<UUID, DinosaurEntity> globalEntityTracker = new HashMap<>();

    /**
     * Tracks a dinosaur entity globally by its UUID.
     */
    public static void trackEntity(DinosaurEntity entity) {
        globalEntityTracker.put(entity.getUniqueID(), entity);
    }

    /**
     * Removes a dinosaur entity from the global tracker by its UUID.
     */
    public static void untrackEntity(DinosaurEntity entity) {
        globalEntityTracker.remove(entity.getUniqueID());
    }

    /**
     * Retrieves a tracked dinosaur entity by its UUID.
     */
    public static DinosaurEntity getTrackedEntity(UUID uuid) {
        return globalEntityTracker.get(uuid);
    }

    /**
     * Handles entity joining the world to ensure proper tracking.
     */
    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof DinosaurEntity) {
            DinosaurEntity dinosaur = (DinosaurEntity) event.getEntity();
            trackEntity(dinosaur);
        }
    }

    /**
     * Handles dimension travel to ensure dinosaurs persist across dimensions.
     */
    @SubscribeEvent
    public static void onEntityTravelToDimension(EntityTravelToDimensionEvent event) {
        if (event.getEntity() instanceof DinosaurEntity) {
            DinosaurEntity dinosaur = (DinosaurEntity) event.getEntity();
            trackEntity(dinosaur);
        }
    }

    /**
     * Handles chunk unloading to prevent dinosaurs from being improperly removed.
     */
    @SubscribeEvent
    public static void onChunkUnload(ChunkEvent.Unload event) {
        for (ClassInheritanceMultiMap<Entity> entityList : event.getChunk().getEntityLists()) {
            for (Entity entity : entityList) {
                if (entity instanceof DinosaurEntity) {
                    DinosaurEntity dinosaur = (DinosaurEntity) entity;
                    trackEntity(dinosaur);
                }
            }
        }
    }
}
