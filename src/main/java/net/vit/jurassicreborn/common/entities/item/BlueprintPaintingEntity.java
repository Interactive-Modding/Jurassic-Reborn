package net.vit.jurassicreborn.common.entities.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
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

    /*...*/
    public BlueprintPaintingEntity(EntityType<? extends Painting> type, Level level) {
        super(type, level);
    }
    private ResourceLocation tex;

    public void setBlueprintTexture(ResourceLocation rl) { this.tex = rl; }
    public ResourceLocation getBlueprintTexture()        { return tex; }

    /*...*/
    public BlueprintPaintingEntity(Level level, BlockPos pos,
                             Direction dir, Holder<PaintingVariant> variant) {
        super(level, pos, dir, variant);
    }
    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setDirection(this.getDirection());
    }

    /*...*/
    @Override
    public void dropItem(net.minecraft.world.entity.Entity breaker) {
        if (!level.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) return;
        if (breaker instanceof Player p && p.getAbilities().instabuild)     return;
        spawnAtLocation(ModItems.BLUEPRINT.get());
    }
    @Override
    public ItemStack getPickResult() {
        return new ItemStack(ModItems.BLUEPRINT.get());
    }
    @Override
    public boolean survives() {
        // 1. Nothing may be inside the hit-box
        if (!com.google.common.collect.Iterables.isEmpty(
                level.getCollisions(this, getBoundingBox())))
            return false;

        // 2. Check wall solidity behind the whole mural
        Holder<PaintingVariant> var = getVariant();              // use getter
        int w = var.value().getWidth()  / 16;   // 96 px → 6 blocks
        int h = var.value().getHeight() / 16;   // 64 px → 4 blocks

        Direction dir   = getDirection();          // the face we stick to
        Direction right = dir.getClockWise();      // horizontal axis of mural

        // start at upper-left corner relative to centre block
        BlockPos origin = getPos().relative(dir.getOpposite())
                .relative(right, -(w / 2) + 1);

        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                BlockPos check = origin.relative(right, x).below(y);
                if (!level.getBlockState(check)
                        .isFaceSturdy(level, check, dir))
                    return false;
            }
        }

        // 3. no overlap with other blueprint murals
        return level.getEntities(this, getBoundingBox(),
                e -> e instanceof BlueprintPaintingEntity).isEmpty();
    }

    /*...*/
    @Override
    public ItemStack getPickedResult(HitResult hit) {
        return new ItemStack(ModItems.BLUEPRINT.get());
    }
}
