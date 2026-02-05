package net.vit.jurassicreborn.common.items.misc;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.decoration.Painting;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.tags.ITagManager;
import net.vit.jurassicreborn.common.entities.item.MuralPaintingEntity;

import java.util.List;

public class MuralItem extends Item {

    /**  #jurassicreborn:mural_variants  */
    public static final TagKey<PaintingVariant> MURAL_VARIANTS_TAG =
            TagKey.create(ForgeRegistries.Keys.PAINTING_VARIANTS,
                    new ResourceLocation("jurassicreborn", "mural_variants"));

    public MuralItem(Properties props) { super(props); }

    // ─────────────────────────────────────────
    // Placement (wall-only)
    // ─────────────────────────────────────────
    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        Direction face = ctx.getClickedFace();
        if (!face.getAxis().isHorizontal()) return InteractionResult.PASS;

        Holder<PaintingVariant> variant = pickVariant();   // now a Holder, not List
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
    private Holder<PaintingVariant> pickVariant() {
        ITagManager<PaintingVariant> tagManager = ForgeRegistries.PAINTING_VARIANTS.tags();
        var tag = tagManager.getTag(MURAL_VARIANTS_TAG);      // ITag<PaintingVariant>

        if (tag == null || tag.isEmpty()) return null;

        // choose a random *PaintingVariant*
        List<PaintingVariant> list = tag.stream().toList();
        PaintingVariant chosen     = list.get(RandomSource.create().nextInt(list.size()));

        // convert it to a Holder the constructor wants
        return ForgeRegistries.PAINTING_VARIANTS.getHolder(chosen).orElse(null);
    }
}
