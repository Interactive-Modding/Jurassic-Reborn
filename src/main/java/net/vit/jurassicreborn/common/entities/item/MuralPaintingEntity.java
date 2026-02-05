package net.vit.jurassicreborn.common.entities.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.Painting;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.vit.jurassicreborn.common.items.ModItems;

public class MuralPaintingEntity extends Painting {

    /* no-arg ctor for EntityType */
    public MuralPaintingEntity(EntityType<? extends Painting> type, Level level) {
        super(type, level);
    }
    @Override
    public ItemStack getPickResult() {
        return new ItemStack(ModItems.MURAL.get());
    }
    /* convenience ctor used by the item */
    public MuralPaintingEntity(Level level, BlockPos pos,
                               Direction dir, Holder<PaintingVariant> variant) {
        super(level, pos, dir, variant);
    }
    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setDirection(this.getDirection());
    }
    /* drop correct item */
    @Override
    public void dropItem(net.minecraft.world.entity.Entity breaker) {
        if (!level.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) return;
        if (breaker instanceof Player p && p.getAbilities().instabuild)     return;
        spawnAtLocation(ModItems.MURAL.get());
    }

    /* middle-click result */
    @Override
    public ItemStack getPickedResult(HitResult hit) {
        return new ItemStack(ModItems.MURAL.get());
    }
}
