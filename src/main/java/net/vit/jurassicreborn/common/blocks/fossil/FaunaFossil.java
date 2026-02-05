package net.vit.jurassicreborn.common.blocks.fossil;

import net.vit.jurassicreborn.common.blocks.ModBlocks;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;
import net.vit.jurassicreborn.common.util.TimePeriod;
import net.minecraft.core.BlockPos;
import java.util.Random;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * When this block is changed in any way, it should update to store the Dinosaur
 *      in it's associated BlockEntity from it's y-level
 */
public class FaunaFossil extends Block implements FossilBlock, EntityBlock {



    public FaunaFossil(Properties p_49795_) {
        super(p_49795_);
    }

    @Override
    public boolean mustBandage() {
        return true;
    }

    @Override
    public BlockState getEncasedFossil() {
        return ModBlocks.ENCASED_FAUNA_FOSSIL.get().defaultBlockState();
    }

//    @Override
//    public @NotNull InteractionResult use(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult result) {
//        if(player.getItemInHand(hand).getItem() == ModItems.PLASTER_AND_BANDAGE.get()){
//            level.setBlock(pos, this.getEncasedFossil(), 1);
//        }
//
//        return super.use(state, level, pos, player, hand, result);
//    }

    public static void setDinosaurFromPos(BlockPos pPos, Level pLevel, Random pRandom){
        TimePeriod posPeriod = TimePeriod.byYValue(pPos.getY());
        BlockEntity fossilEntity = pLevel.getBlockEntity(pPos);

        if(!(fossilEntity instanceof FaunaFossilBlockEntity faunaFossilEntity)){
            return;
        }

        if(faunaFossilEntity.getDinosaur() != null){
            return;
        }

        List<Dinosaur> dinoList = Dinosaur.DINOSAURS_BY_PERIOD_MAP.get(posPeriod);

        if(dinoList == null){
            return;
        }

        int randomDino = pRandom.nextInt(dinoList.size());

        Dinosaur dino = dinoList.get(randomDino);

        faunaFossilEntity.setDinosaur(dino);

    }


    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pPos, @NotNull BlockState pState) {
        return new FaunaFossilBlockEntity(pPos, pState);
    }
}
