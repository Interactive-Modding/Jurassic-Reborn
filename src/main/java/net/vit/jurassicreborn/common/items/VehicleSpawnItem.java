package net.vit.jurassicreborn.common.items;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.vit.jurassicreborn.common.entities.vehicle.VehicleEntity;

/**
 * Spawns a particular vehicle EntityType when right-clicked on a block.
 */
public class VehicleSpawnItem extends Item {

    // Hold a lazy reference instead of a direct EntityType
    private final DeferredHolder<EntityType<?>, ? extends EntityType<?>> vehicleType;

    public VehicleSpawnItem(DeferredHolder<EntityType<?>, ? extends EntityType<?>> type, Properties props) {
        super(props.stacksTo(1)); // always 1 per stack
        this.vehicleType = type;
    }

    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        Level level = ctx.getLevel();
        if (level.isClientSide) return InteractionResult.SUCCESS;

        EntityType<?> type = vehicleType.get(); // safe to call now (Forge registry initialized)
        if (type == null) return InteractionResult.FAIL;

        BlockPos spawnPos = ctx.getClickedPos().relative(ctx.getClickedFace());
        Entity entity = type.create((ServerLevel) level);
        if (entity == null) return InteractionResult.FAIL;

        entity.moveTo(
                spawnPos.getX() + 0.5,
                spawnPos.getY(),
                spawnPos.getZ() + 0.5,
                ctx.getPlayer() != null ? ctx.getPlayer().getYRot() : 0,
                0
        );

        if (entity instanceof VehicleEntity vehicle) {
            vehicle.setItem(ctx.getItemInHand());
        }

        level.addFreshEntity(entity);
        if (!ctx.getPlayer().isCreative()) {
            ctx.getItemInHand().shrink(1);
        }
        return InteractionResult.CONSUME;
    }
}
