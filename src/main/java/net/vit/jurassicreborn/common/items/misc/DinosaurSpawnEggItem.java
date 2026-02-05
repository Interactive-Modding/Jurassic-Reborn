package net.vit.jurassicreborn.common.items.misc;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.chat.TextComponent;
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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.common.entities.DinosaurEntity;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;
import net.vit.jurassicreborn.common.genetics.GeneticsHelper;
import net.vit.jurassicreborn.common.items.TabHandler;
import net.vit.jurassicreborn.common.util.LangUtil;

import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;

/**
 * Spawn egg item bound to a specific dinosaur type.
 */
public class DinosaurSpawnEggItem extends Item {
    private static final String TAG_GENDER = "GenderMode";

    private final Dinosaur dinosaur;
    private final Supplier<? extends EntityType<? extends DinosaurEntity>> entityTypeSupplier;

    public DinosaurSpawnEggItem(Dinosaur dinosaur,
                                Supplier<? extends EntityType<? extends DinosaurEntity>> entityTypeSupplier) {
        super(new Item.Properties().tab(TabHandler.SPAWN_EGGS));
        this.dinosaur = dinosaur;
        this.entityTypeSupplier = entityTypeSupplier;
    }

    public Dinosaur getDinosaur() {
        return this.dinosaur;
    }

    @Override
    public Component getName(ItemStack stack) {
        Component dinoName = this.dinosaur.getTranslatedName();
        return new TranslatableComponent("item.jurassicreborn.spawn_egg_name", dinoName);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (player.isShiftKeyDown()) {
            int mode = changeMode(stack);
            if (world.isClientSide) {
                String template = LangUtil.translate("item.spawnegg.change_gender");
                String genderText = LangUtil.getGender(mode).getString();
                String msg = template.replace("{mode}", genderText);
                player.displayClientMessage(new TextComponent(msg), true);
            }
            return InteractionResultHolder.success(stack);
        }
        return InteractionResultHolder.pass(stack);
    }

    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        Level world = ctx.getLevel();
        if (world.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        BlockPos pos = ctx.getClickedPos();
        ItemStack stack = ctx.getItemInHand();
        Player player = ctx.getPlayer();
        BlockEntity be = world.getBlockEntity(pos);

        EntityType<?> resolvedType = resolveEntityType();
        if (resolvedType == null) {
            return InteractionResult.PASS;
        }

        if (be instanceof SpawnerBlockEntity spawner) {
            spawner.getSpawner().setEntityId(resolvedType);
            spawner.setChanged();
            if (player != null && !player.isCreative()) {
                stack.shrink(1);
            }
            return InteractionResult.CONSUME;
        }

        BlockPos spawnPos = pos.relative(ctx.getClickedFace());
        double x = spawnPos.getX() + 0.5;
        double y = spawnPos.getY();
        double z = spawnPos.getZ() + 0.5;

        DinosaurEntity ent = spawnDinosaur(world, player, stack, resolvedType, x, y, z);
        if (ent != null) {
            if (player != null && !player.isCreative()) {
                stack.shrink(1);
            }
            world.addFreshEntity(ent);
            return InteractionResult.CONSUME;
        }
        return InteractionResult.PASS;
    }

    private EntityType<?> resolveEntityType() {
        EntityType<?> type = entityTypeSupplier.get();
        if (type != null) {
            return type;
        }
        String base = this.dinosaur.getName().toLowerCase(Locale.ROOT);
        ResourceLocation key = new ResourceLocation(JurassicReborn.MODID, base);
        type = ForgeRegistries.ENTITIES.getValue(key);
        if (type == null) {
            ResourceLocation alt = new ResourceLocation(JurassicReborn.MODID, "velociraptor" + base);
            type = ForgeRegistries.ENTITIES.getValue(alt);
        }
        return type;
    }

    private DinosaurEntity spawnDinosaur(Level world, Player player, ItemStack stack,
                                         EntityType<?> entityType,
                                         double x, double y, double z) {
        Entity raw = entityType.create(world);
        if (!(raw instanceof DinosaurEntity dino)) {
            return null;
        }

        dino.setPos(x, y, z);
        dino.setYRot(world.random.nextFloat() * 360F);
        dino.setGenetics(GeneticsHelper.randomGenetics(world.random));
        dino.setDNAQuality(100);

        int gender = getMode(stack);
        if (gender == 1) {
            dino.setMale(true);
        } else if (gender == 2) {
            dino.setMale(false);
        } else {
            dino.setMale(world.random.nextBoolean());
        }

        if (player != null && player.isShiftKeyDown()) {
            dino.setAge(0);
        }
        return dino;
    }

    private int getMode(ItemStack stack) {
        return stack.getOrCreateTag().getInt(TAG_GENDER);
    }

    private int changeMode(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        int mode = (tag.getInt(TAG_GENDER) + 1) % 3;
        tag.putInt(TAG_GENDER, mode);
        stack.setTag(tag);
        return mode;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, Level world,
                                List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(new TextComponent(
                LangUtil.translate("lore.spawnegg.use_on_spawner")
        ));
        tooltip.add(new TextComponent(
                LangUtil.translate("lore.spawnegg.sneak_to_change_gender")
        ));
        super.appendHoverText(stack, world, tooltip, flag);
    }
}