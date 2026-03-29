package net.vit.jurassicreborn.common.items.misc;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.core.registries.Registries;
import net.vit.jurassicreborn.common.entities.item.MuralPaintingEntity;

import java.util.List;

public class MuralItem extends Item {

    /**  #jurassicreborn:mural_variants  */
    public static final TagKey<PaintingVariant> MURAL_VARIANTS_TAG =
            TagKey.create(Registries.PAINTING_VARIANT,
                    ResourceLocation.parse("jurassicreborn" + ":" + "mural_variants"));

    public MuralItem(Properties props) { super(props); }

    // ─────────────────────────────────────────
    // Placement (wall-only)
    // ─────────────────────────────────────────
    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        Direction face = ctx.getClickedFace();
        if (!face.getAxis().isHorizontal()) return InteractionResult.PASS;

        Holder<PaintingVariant> variant = pickVariant(ctx.getLevel());   // now a Holder, not List
        if (variant == null) return InteractionResult.PASS;

        Level     level = ctx.getLevel();
        BlockPos  pos   = ctx.getClickedPos().relative(face);

        // constructor wants a single Holder - this compiles on 1.19.2
        MuralPaintingEntity painting =
                new MuralPaintingEntity(level, pos, face, variant);

        if (painting.survives()) {
            if (!level.isClientSide) {
                level.addFreshEntity(painting);
                painting.playPlacementSound();
                if (!ctx.getPlayer().isCreative()) ctx.getItemInHand().shrink(1);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return InteractionResult.CONSUME;
    }

    /** Pick a random Holder<PaintingVariant> from #jurassicreborn:mural_variants */
    private Holder<PaintingVariant> pickVariant(Level level) {
        var registry = level.registryAccess().registryOrThrow(Registries.PAINTING_VARIANT);
        var tag = registry.getTag(MURAL_VARIANTS_TAG).orElse(null);

        if (tag == null || tag.size() == 0) return null;

        List<Holder<PaintingVariant>> list = tag.stream().toList();
        return list.get(RandomSource.create().nextInt(list.size()));
    }
}
