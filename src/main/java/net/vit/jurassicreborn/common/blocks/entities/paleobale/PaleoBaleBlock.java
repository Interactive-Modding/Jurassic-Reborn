package net.vit.jurassicreborn.common.blocks.entities.paleobale;

import net.vit.jurassicreborn.common.blocks.ModBlocks;
import net.vit.jurassicreborn.common.blocks.wood.WoodBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.*;
import java.util.stream.Collectors;

public class PaleoBaleBlock extends RotatedPillarBlock {
    private final Variant variant;
    public PaleoBaleBlock(Variant v, Properties p_55926_) {
        super(p_55926_);
        variant = v;
    }

    public Variant getVariant() {
        return this.variant;
    }

    @Override
    public void fallOn(Level world, BlockState blockState, BlockPos pos, Entity pEntity, float fallDistance) {
        pEntity.causeFallDamage(fallDistance, 0.2F, pEntity.damageSources().fall());
    }

    public enum Variant implements StringRepresentable {
        CYCADEOIDEA(),
        CYCAD(),//ModBlocks.SMALL_CYCAD.get()),
        FERN(),//ModBlocks.SMALL_CHAIN_FERN.get(), ModBlocks.SMALL_ROYAL_FERN.get(), ModBlocks.RAPHAELIA.get(), ModBlocks.BRISTLE_FERN.get(), ModBlocks.CINNAMON_FERN.get(), ModBlocks.TEMPSKYA.get()),
        LEAVES(),//WoodBlocks.ARAUCARIA_LEAVES.get()),
        OTHER();//ModBlocks.AJUGINUCULA_SMITHII.get(), ModBlocks.RHACOPHYTON.get(), ModBlocks.GRAMINIDITES_BAMBUSOIDES.get(), ModBlocks.HELICONIA.get(), ModBlocks.RHAMNUS_SALICIFOLIUS.get(), ModBlocks.LARGESTIPULE_LEATHER_ROOT.get(), ModBlocks.RHACOPHYTON.get(), ModBlocks.CRY_PANSY.get(), ModBlocks.DICKSONIA.get(), ModBlocks.DICROIDIUM_ZUBERI.get(), ModBlocks.WILD_ONION.get(), ModBlocks.ZAMITES.get(), ModBlocks.UMALTOLEPIS.get(), ModBlocks.LIRIODENDRITES.get(), ModBlocks.WOOLLY_STALKED_BEGONIA.get());

        private final List<Item> ingredients;

        Variant(Item... ingredients) {
            this.ingredients = Arrays.stream(ingredients).collect(Collectors.toList());
        }

        @Override
        public String getSerializedName() {
            return this.name().toLowerCase(Locale.ENGLISH);
        }

        @Override
        public String toString() {
            return this.name().toLowerCase(Locale.ENGLISH);
        }

        public Item[] getIngredients() {
            return this.ingredients.toArray(new Item[0]);
        }

        public List<Item> getIngredientsAsList(){
            return this.ingredients;
        }
    }
}
