package net.vit.jurassicreborn.common.items;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.vit.jurassicreborn.common.entities.vehicle.boat.JurassicBoat;
import net.vit.jurassicreborn.common.entities.vehicle.boat.JurassicChestBoat;
import net.vit.jurassicreborn.common.entities.vehicle.boat.ModBoatType;

import java.util.List;

public class JurassicBoatItem extends Item {
    private final boolean hasChest;
    private final ModBoatType type;

    public JurassicBoatItem(boolean hasChest, ModBoatType type, Properties properties) {
        super(properties);
        this.hasChest = hasChest;
        this.type = type;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        HitResult hitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.ANY);
        if (hitResult.getType() == HitResult.Type.MISS) {
            return InteractionResultHolder.pass(itemStack);
        }

        Vec3 viewVector = player.getViewVector(1.0F);
        double reach = 5.0D;
        List<Entity> entities = level.getEntities(player, player.getBoundingBox().expandTowards(viewVector.scale(reach)).inflate(1.0D), entity -> !entity.isSpectator() && entity.isPickable());
        Vec3 eyePosition = player.getEyePosition(1.0F);

        for (Entity entity : entities) {
            AABB aabb = entity.getBoundingBox().inflate(entity.getPickRadius());
            if (aabb.contains(eyePosition)) {
                return InteractionResultHolder.pass(itemStack);
            }
        }

        if (hitResult.getType() != HitResult.Type.BLOCK) {
            return InteractionResultHolder.pass(itemStack);
        }

        Boat boat = this.createBoat(level, hitResult);
        boat.setYRot(player.getYRot());
        boat.setXRot(0.0F);
        boat.yRotO = boat.getYRot();
        boat.xRotO = boat.getXRot();

        if (!level.noCollision(boat, boat.getBoundingBox().inflate(-0.1D))) {
            return InteractionResultHolder.fail(itemStack);
        }

        if (!level.isClientSide) {
            level.addFreshEntity(boat);
            level.gameEvent(player, GameEvent.ENTITY_PLACE, hitResult.getLocation());
            if (!player.getAbilities().instabuild) {
                itemStack.shrink(1);
            }
            player.awardStat(Stats.ITEM_USED.get(this));
            level.playSound(null, boat.getX(), boat.getY(), boat.getZ(),
                    SoundEvents.WOOD_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
        }

        return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide());
    }

    private Boat createBoat(Level level, HitResult hitResult) {
        double x = hitResult.getLocation().x;
        double y = hitResult.getLocation().y;
        double z = hitResult.getLocation().z;
        if (this.hasChest) {
            JurassicChestBoat boat = new JurassicChestBoat(level, x, y, z);
            boat.setVariant(this.type);
            return boat;
        } else {
            JurassicBoat boat = new JurassicBoat(level, x, y, z);
            boat.setVariant(this.type);
            return boat;
        }
    }
}
