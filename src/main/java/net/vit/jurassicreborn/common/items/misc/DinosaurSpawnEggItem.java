package net.vit.jurassicreborn.common.items.misc;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.common.datagen.data.ModDataComponent;
import net.vit.jurassicreborn.common.entities.DinosaurEntity;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;
import net.vit.jurassicreborn.common.genetics.GeneticsHelper;
import net.vit.jurassicreborn.common.util.LangUtil;

import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;

/**
 * Spawn egg item bound to a specific dinosaur type.
 */
public class DinosaurSpawnEggItem extends Item {

    private final Dinosaur dinosaur;
    private final Supplier<? extends EntityType<? extends DinosaurEntity>> entityTypeSupplier;

    public DinosaurSpawnEggItem(
            Dinosaur dinosaur,
            Supplier<? extends EntityType<? extends DinosaurEntity>> entityTypeSupplier
    ) {
        super(new Item.Properties());
        this.dinosaur = dinosaur;
        this.entityTypeSupplier = entityTypeSupplier;
    }

    public Dinosaur getDinosaur() {
        return this.dinosaur;
    }

    @Override
    public Component getName(ItemStack stack) {
        return Component.translatable(
                "item.jurassicreborn.spawn_egg_name",
                this.dinosaur.getTranslatedName()
        );
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (player.isShiftKeyDown()) {
            int mode = changeMode(stack);

            if (level.isClientSide) {
                String template = LangUtil.translate("item.spawnegg.change_gender");
                String genderText = LangUtil.getGender(mode).getString();
                player.displayClientMessage(
                        Component.literal(template.replace("{mode}", genderText)),
                        true
                );
            }
            return InteractionResultHolder.success(stack);
        }

        return InteractionResultHolder.pass(stack);
    }

    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        Level level = ctx.getLevel();
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        BlockPos pos = ctx.getClickedPos();
        ItemStack stack = ctx.getItemInHand();
        Player player = ctx.getPlayer();
        BlockEntity be = level.getBlockEntity(pos);

        EntityType<?> type = resolveEntityType();
        if (type == null) {
            return InteractionResult.PASS;
        }

        if (be instanceof SpawnerBlockEntity spawner) {
            spawner.getSpawner().setEntityId(type, level, level.getRandom(), pos);
            spawner.setChanged();

            if (player != null && !player.isCreative()) {
                stack.shrink(1);
            }
            return InteractionResult.CONSUME;
        }

        BlockPos spawnPos = pos.relative(ctx.getClickedFace());

        DinosaurEntity entity = spawnDinosaur(
                level,
                player,
                stack,
                type,
                spawnPos.getX() + 0.5D,
                spawnPos.getY(),
                spawnPos.getZ() + 0.5D
        );

        if (entity != null) {
            level.addFreshEntity(entity);
            if (player != null && !player.isCreative()) {
                stack.shrink(1);
            }
            return InteractionResult.CONSUME;
        }

        return InteractionResult.PASS;
    }

    private EntityType<?> resolveEntityType() {
        EntityType<?> type = entityTypeSupplier.get();
        if (type != null) return type;

        String base = this.dinosaur.getName().toLowerCase(Locale.ROOT);
        ResourceLocation key = ResourceLocation.fromNamespaceAndPath(JurassicReborn.MODID, base);
        type = BuiltInRegistries.ENTITY_TYPE.get(key);

        if (type == null) {
            ResourceLocation alt =
                    ResourceLocation.fromNamespaceAndPath(JurassicReborn.MODID, "velociraptor_" + base);
            type = BuiltInRegistries.ENTITY_TYPE.get(alt);
        }

        return type;
    }

    private DinosaurEntity spawnDinosaur(
            Level level,
            Player player,
            ItemStack stack,
            EntityType<?> type,
            double x, double y, double z
    ) {
        Entity raw = type.create(level);
        if (!(raw instanceof DinosaurEntity dino)) return null;

        dino.setPos(x, y, z);
        dino.setYRot(level.random.nextFloat() * 360F);
        dino.setGenetics(GeneticsHelper.randomGenetics(level.random));
        dino.setDNAQuality(100);

        int gender = getMode(stack);
        if (gender == 1) dino.setMale(true);
        else if (gender == 2) dino.setMale(false);
        else dino.setMale(level.random.nextBoolean());

        if (player != null && player.isShiftKeyDown()) {
            dino.setAge(0);
        }

        return dino;
    }

    private int getMode(ItemStack stack) {
        Integer mode = stack.get(ModDataComponent.GENDER_MODE.get());
        return mode == null ? 0 : mode;
    }

    private int changeMode(ItemStack stack) {
        int next = (getMode(stack) + 1) % 3;
        stack.set(ModDataComponent.GENDER_MODE.get(), next);
        return next;
    }


    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(
            ItemStack stack,
            TooltipContext context,
            List<Component> tooltip,
            TooltipFlag flag
    ) {
        tooltip.add(Component.literal(
                LangUtil.translate("lore.spawnegg.use_on_spawner")
        ));
        tooltip.add(Component.literal(
                LangUtil.translate("lore.spawnegg.sneak_to_change_gender")
        ));
    }
}
