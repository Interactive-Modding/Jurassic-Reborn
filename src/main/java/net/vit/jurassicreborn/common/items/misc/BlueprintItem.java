package net.vit.jurassicreborn.common.items.misc;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.client.screens.SelectBlueprintScreen;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class BlueprintItem extends Item {

    public static final TagKey<PaintingVariant> BLUEPRINT_VARIANTS_TAG = TagKey.create(
            Registries.PAINTING_VARIANT,
            ResourceLocation.fromNamespaceAndPath(JurassicReborn.MODID, "blueprint_variants")
    );

    public BlueprintItem(Properties props) {
        super(props);
    }

    public Optional<Holder<PaintingVariant>> getPreviewVariant(ItemStack stack) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) {
            return Optional.empty();
        }

        Registry<PaintingVariant> registry = mc.level.registryAccess().registryOrThrow(Registries.PAINTING_VARIANT);

        return registry.holders()
                .filter(holder -> holder.is(BLUEPRINT_VARIANTS_TAG))
                .findFirst()
                .map(holder -> (Holder<PaintingVariant>) holder);
    }

    @Nullable
    public ResourceLocation getPreviewTexture(ItemStack stack) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) {
            return null;
        }

        Registry<PaintingVariant> registry = mc.level.registryAccess().registryOrThrow(Registries.PAINTING_VARIANT);

        return getPreviewVariant(stack)
                .flatMap(holder -> registry.getResourceKey(holder.value()).map(key -> key.location()))
                .map(id -> ResourceLocation.fromNamespaceAndPath(
                        JurassicReborn.MODID,
                        "textures/painting/" + id.getPath() + ".png"
                ))
                .orElse(null);
    }

    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        Direction face = ctx.getClickedFace();
        if (!face.getAxis().isHorizontal()) {
            return InteractionResult.PASS;
        }

        Level level = ctx.getLevel();
        if (ctx.getPlayer() != null) {
            if (level.isClientSide) {
                openClientGui(ctx.getClickedPos(), face, ctx.getHand());
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        return InteractionResult.PASS;
    }

    private void openClientGui(BlockPos pos, Direction face, net.minecraft.world.InteractionHand hand) {
        doOpenScreen(pos, face, hand);
    }

    @OnlyIn(Dist.CLIENT)
    private void doOpenScreen(BlockPos pos, Direction face, InteractionHand hand) {
        Minecraft.getInstance().setScreen(new SelectBlueprintScreen(pos, face, hand));
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("Right-click on a wall to pick and place a blueprint")
                .withStyle(ChatFormatting.GRAY));
    }
}