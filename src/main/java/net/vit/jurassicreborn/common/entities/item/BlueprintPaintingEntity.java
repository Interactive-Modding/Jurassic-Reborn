package net.vit.jurassicreborn.common.entities.item;

import com.google.common.collect.Iterables;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.Painting;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.vit.jurassicreborn.common.items.ModItems;

public class BlueprintPaintingEntity extends Painting {

    private ResourceLocation tex;

    public BlueprintPaintingEntity(EntityType<? extends Painting> type, Level level) {
        super(type, level);
    }

    public BlueprintPaintingEntity(Level level, BlockPos pos, Direction dir, Holder<PaintingVariant> variant) {
        super(level, pos, dir, variant);
    }

    public void setBlueprintTexture(ResourceLocation rl) {
        this.tex = rl;
    }

    public ResourceLocation getBlueprintTexture() {
        return this.tex;
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setDirection(this.getDirection());

        if (tag.contains("BlueprintTexture")) {
            this.tex = ResourceLocation.parse(tag.getString("BlueprintTexture"));
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);

        if (this.tex != null) {
            tag.putString("BlueprintTexture", this.tex.toString());
        }
    }

    @Override
    public void dropItem(Entity breaker) {
        Level level = this.level();
        if (!level.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
            return;
        }
        if (breaker instanceof Player player && player.getAbilities().instabuild) {
            return;
        }

        this.spawnAtLocation(ModItems.BLUEPRINT.get());
    }

    @Override
    public ItemStack getPickResult() {
        return new ItemStack(ModItems.BLUEPRINT.get());
    }

    @Override
    public ItemStack getPickedResult(HitResult hit) {
        return new ItemStack(ModItems.BLUEPRINT.get());
    }

    @Override
    public boolean survives() {
        Level level = this.level();

        if (!Iterables.isEmpty(level.getCollisions(this, this.getBoundingBox()))) {
            return false;
        }

        Holder<PaintingVariant> variant = this.getVariant();
        int w = Math.max(1, variant.value().width());
        int h = Math.max(1, variant.value().height());

        Direction dir = this.getDirection();
        Direction right = dir.getClockWise();

        BlockPos origin = this.getPos()
                .relative(dir.getOpposite())
                .relative(right, -(w / 2) + 1);

        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                BlockPos check = origin.relative(right, x).below(y);
                if (!level.getBlockState(check).isFaceSturdy(level, check, dir)) {
                    return false;
                }
            }
        }

        return level.getEntities(this, this.getBoundingBox(),
                entity -> entity instanceof BlueprintPaintingEntity).isEmpty();
    }
}
