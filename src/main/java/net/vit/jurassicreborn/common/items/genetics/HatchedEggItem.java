package net.vit.jurassicreborn.common.items.genetics;

import net.vit.jurassicreborn.common.entities.DinosaurEntity;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;
import net.vit.jurassicreborn.common.items.ModItems;
import net.vit.jurassicreborn.common.items.TabHandler;
import net.vit.jurassicreborn.common.util.LangUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class HatchedEggItem extends DNAContainerItem {

    private final Dinosaur dino;

    public HatchedEggItem(Properties properties, Dinosaur dino) {
        super(properties);
        this.dino = dino;
    }

    @Override
    public Component getName(ItemStack pStack) {
        return Component.literal(
                Component.translatable(
                        dino.givesDirectBirth() ? "item.JurassicReborn.gestated" : "item.JurassicReborn.hatched_egg"
                ).getString().replace("{dino}", LangUtil.getDinoName(this.dino).getString())
        );
    }

    /** Returns male=true / female=false. Persists to stack NBT "Gender". */
    public boolean getGender(@Nullable Player player, ItemStack stack) {
        CompoundTag nbt = stack.getOrCreateTag();
        if (nbt.contains("Gender")) {
            return nbt.getBoolean("Gender");
        }
        boolean gender = (player != null ? player.level.random.nextBoolean()
                : ((stack.hashCode() & 1) == 0));
        nbt.putBoolean("Gender", gender);
        stack.setTag(nbt);
        return gender;
    }

    @Override
    public void fillItemCategory(CreativeModeTab pCategory, NonNullList<ItemStack> pItems) {
        if (pCategory == TabHandler.DNA || pCategory == CreativeModeTab.TAB_SEARCH) {
            if (pItems.stream().anyMatch((stack) -> stack.is(this))) return;

            var eggItem = ModItems.hatchedDinoEggs.get(dino);
            if (eggItem != null) {
                ItemStack defaultDNAItem = eggItem.get().getDefaultInstance();
                defaultDNAItem.getOrCreateTag().putBoolean("isCreative", true);
                pItems.add(defaultDNAItem);
            }
        } else {
            super.fillItemCategory(pCategory, pItems);
        }
    }

    @Override
    public int getContainerId(ItemStack stack) {
        return Dinosaur.DINOS.indexOf(dino);
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        Level level = context.getLevel();
        if (level.isClientSide) return InteractionResult.PASS;

        BlockPos pos = context.getClickedPos().relative(context.getClickedFace());
        Direction side = context.getClickedFace();
        Player player = context.getPlayer();

        double hitX = context.getClickLocation().x;
        double hitY = context.getClickLocation().y;
        double hitZ = context.getClickLocation().z;

        if (side == Direction.EAST || side == Direction.WEST) {
            hitX = 1.0F - hitX;
        } else if (side == Direction.NORTH || side == Direction.SOUTH) {
            hitZ = 1.0F - hitZ;
        }

        if (level.isInWorldBounds(pos)) {
            Dinosaur dinosaur = dino;
            DinosaurEntity entity = DinosaurEntity.CLASS_TYPE_LIST
                    .get(dinosaur.getDinosaurClass()).get().create(level);
            if (entity == null) return InteractionResult.PASS;

            entity.setPos(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D);

            // ✅ Finalize first so vanilla/AI setup runs BEFORE we apply DNA.
            if (level instanceof ServerLevel sl) {
                entity.finalizeSpawn(sl, sl.getCurrentDifficultyAt(entity.blockPosition()),
                        MobSpawnType.SPAWN_EGG, null, null);
            }

            // Now apply DNA/quality/gender from the egg so nothing overwrites it afterward.
            entity.setAge(0);
            entity.setGenetics(this.getGeneticCode((player != null ? player.getRandom() : level.random), stack));
            entity.setDNAQuality(this.getDNAQuality(player != null && player.isCreative(), stack));
            entity.setMale(this.getGender(player, stack));
            if (player != null && !player.isCrouching()) {
                entity.setOwner(player);
            }

            level.addFreshEntity(entity);

            if (player != null && !player.isCreative()) {
                stack.shrink(1);
            }
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> lore, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, lore, pIsAdvanced);
        CompoundTag tag = pStack.getTag();
        if (tag != null && tag.contains("Gender")) {
            lore.add(Component.translatable("tooltip.jurassicreborn.gender." +
                    (tag.getBoolean("Gender") ? "male" : "female")));
        }
    }
}
