package net.vit.jurassicreborn.common.items;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.vit.jurassicreborn.common.entities.vehicle.VehicleEntity;

/** Spawns a particular vehicle EntityType when right-clicked on a block. */
public class VehicleSpawnItem extends Item {

    private final EntityType<?> vehicleType;

    public VehicleSpawnItem(EntityType<?> type, Properties props) {
        super(props.stacksTo(1));     // always 1 per stack
        this.vehicleType = type;
    }

    /* Right-click on a block */
    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        Level level = ctx.getLevel();
        if (level.isClientSide) return InteractionResult.SUCCESS;

        BlockPos clickPos = ctx.getClickedPos().relative(ctx.getClickedFace());
        var entity = vehicleType.create((ServerLevel) level);
        if (entity == null) return InteractionResult.FAIL;

        // Center on block; yaw from player
        entity.moveTo(
                clickPos.getX() + 0.5,
                clickPos.getY(),
                clickPos.getZ() + 0.5,
                ctx.getPlayer().getYRot(),
                0);
        if (entity instanceof VehicleEntity vehicle) {
            vehicle.setItem(ctx.getItemInHand());
        }
        level.addFreshEntity(entity);
        ctx.getItemInHand().shrink(1);
        return InteractionResult.CONSUME;
    }
}
