package net.vit.jurassicreborn.common.items.Fossils;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.vit.jurassicreborn.common.blocks.ModBlocks;
import net.vit.jurassicreborn.common.blocks.entities.EncasedFaunaFossilBlockEntity;
import net.vit.jurassicreborn.common.blocks.fossil.FossilBlock;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;

public class PlasterAndBandageItem extends Item {
    public PlasterAndBandageItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext ctx) {
        Player player = ctx.getPlayer();
        Level world = ctx.getLevel();
        BlockPos pos = ctx.getClickedPos();
        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();

        if (block instanceof FossilBlock fossilBlock && fossilBlock.mustBandage()) {
            if (!world.isClientSide) {
                Dinosaur dino = fossilBlock.getDinosaur();

                if (dino == null || dino == Dinosaur.EMPTY) {
                    return InteractionResult.PASS;
                }

                // Get the correct per-dino encased block
                Block encasedBlock = ModBlocks.getEncasedBlockFor(dino);
                if (encasedBlock == null) {
                    return InteractionResult.FAIL;
                }

                BlockState encasedState = encasedBlock.defaultBlockState();
                world.setBlock(pos, encasedState, 3);

                if (world.getBlockEntity(pos) instanceof EncasedFaunaFossilBlockEntity entity) {
                    entity.setDino(dino); // Keep for legacy compatibility / loot tracking
                }

                // Consume the item
                if (player != null && !player.isCreative()) {
                    stack.shrink(1);
                }
            }

            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

}
