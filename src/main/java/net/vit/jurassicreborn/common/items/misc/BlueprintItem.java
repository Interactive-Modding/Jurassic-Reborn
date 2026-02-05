package net.vit.jurassicreborn.common.items.misc;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.decoration.Painting;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.tags.ITagManager;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.common.entities.item.BlueprintPaintingEntity;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

public class BlueprintItem extends Item {

    /* tag key with all allowed variants */
    public static final TagKey<PaintingVariant> BLUEPRINT_VARIANTS_TAG =
            TagKey.create(ForgeRegistries.Keys.PAINTING_VARIANTS,
                    new ResourceLocation(JurassicReborn.MODID, "blueprint_variants"));

    /* NBT key that stores the chosen variant id */
    private static final String NBT_VARIANT = "VariantId";

    public BlueprintItem(Properties props) { super(props); }

    /* ─────────────────────────────
       Right-click on block (place)
       ───────────────────────────── */
    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        Direction face = ctx.getClickedFace();
        if (!face.getAxis().isHorizontal()) return InteractionResult.PASS;

        Holder<PaintingVariant> variant = getStoredVariant(ctx.getItemInHand(), ctx.getLevel());
        if (variant == null) return InteractionResult.PASS;

        Level    level = ctx.getLevel();
        BlockPos pos   = ctx.getClickedPos().relative(face);

        BlueprintPaintingEntity painting =
                new BlueprintPaintingEntity(level, pos, face, variant);

        if (painting.survives()) {
            if (!level.isClientSide) {
                painting.setBlueprintTexture(textureFor(variant));   // helper below
                level.addFreshEntity(painting);
                painting.playPlacementSound();
                if (!ctx.getPlayer().isCreative()) ctx.getItemInHand().shrink(1);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return InteractionResult.CONSUME;
    }

    /* ─────────────────────────────
       Right-click in the air (cycle)
       ───────────────────────────── */
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        // pull every variant *object* in the tag
        List<PaintingVariant> list = ForgeRegistries.PAINTING_VARIANTS.tags()
                .getTag(BLUEPRINT_VARIANTS_TAG).stream().toList();
        if (list.isEmpty()) return InteractionResultHolder.pass(stack);

        /* find current index */
        ResourceLocation curId = getStoredId(stack);
        int idx = 0;
        if (curId != null) {
            for (int i = 0; i < list.size(); i++)
                if (ForgeRegistries.PAINTING_VARIANTS.getKey(list.get(i)).equals(curId))
                { idx = i; break; }
        }

        PaintingVariant nextPv = list.get((idx + 1) % list.size());

        /* write NBT */
        stack.getOrCreateTag()
                .putString(NBT_VARIANT, ForgeRegistries.PAINTING_VARIANTS.getKey(nextPv).toString());

        if (level.isClientSide)
            player.displayClientMessage(Component.literal(
                            "Blueprint set to: " + ForgeRegistries.PAINTING_VARIANTS.getKey(nextPv).getPath()),
                    true);

        player.swing(hand);
        return InteractionResultHolder.success(stack);
    }

    /* ─────────────────────────────
       Tooltip
       ───────────────────────────── */
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level,
                                List<Component> lines, TooltipFlag flag) {
        ResourceLocation id = getStoredId(stack);
        if (id != null)
            lines.add(Component.literal("Design: " + id.getPath()).withStyle(ChatFormatting.AQUA));

        lines.add(Component.literal("Right-click in the air to change design")
                .withStyle(ChatFormatting.GRAY));
    }

    /* ─────────────────────────────
       Helpers
       ───────────────────────────── */

    /** variant currently stored in stack; if none, pick first in tag and save */
    private Holder<PaintingVariant> getStoredVariant(ItemStack stack, Level level) {
        ResourceLocation id = getStoredId(stack);

        if (id != null) {
            PaintingVariant pv = ForgeRegistries.PAINTING_VARIANTS.getValue(id);
            if (pv != null) return ForgeRegistries.PAINTING_VARIANTS.getHolder(pv).orElse(null);
        }

        // no NBT yet → initialize
        List<Holder<PaintingVariant>> list = getVariantList();
        if (list.isEmpty()) return null;

        stack.getOrCreateTag().putString(NBT_VARIANT,
                ForgeRegistries.PAINTING_VARIANTS.getKey(list.get(0).value()).toString());
        return list.get(0);
    }

    private @Nullable ResourceLocation getStoredId(ItemStack stack) {
        if (!stack.hasTag() || !stack.getTag().contains(NBT_VARIANT)) return null;
        return new ResourceLocation(stack.getTag().getString(NBT_VARIANT));
    }

    private List<Holder<PaintingVariant>> getVariantList() {
        return ForgeRegistries.PAINTING_VARIANTS.tags()
                .getTag(BLUEPRINT_VARIANTS_TAG).stream()
                .map(pv -> ForgeRegistries.PAINTING_VARIANTS.getHolder(pv).orElse(null))
                .filter(Objects::nonNull)
                .toList();
    }

    /** build texture path for renderer helper */
    private static ResourceLocation textureFor(Holder<PaintingVariant> var) {
        ResourceLocation id = ForgeRegistries.PAINTING_VARIANTS.getKey(var.value());
        return new ResourceLocation(JurassicReborn.MODID,
                "textures/painting/" + id.getPath() + ".png");
    }
}
